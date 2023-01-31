package com.back.common.craw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.CrawConstant;
import com.back.entity.pojo.North;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.pojo.Up;
import com.back.entity.vo.NorthVo;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.StockPushVo;

import com.back.entity.vo.UpVo;
import com.back.service.NorthService;
import com.back.service.ReviewdataService;
import com.back.service.UpService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 爬取股票信息工具类
 */
@Component
@Slf4j
public class CrawUtil {

	@Autowired
	private ReviewdataService reviewdataService;
	@Autowired
	private NorthService northService;
	@Autowired
	private UpService upService;

	private static final JavaScriptProvider<ShJS> shPr = new JavaScriptProvider<>();
	/** <股票代码,股票信息> */
	public static Map<String, StockPushVo> StockCodeMap = new HashMap<>();
	/** <股票名称,股票信息> */
	public static Map<String, StockPushVo> StockNameMap = new HashMap<>();
	/**
	 * 每日复盘数据
	 */
	public static Map<String, Object> dayReviewMap = new HashMap<>();

	@PostConstruct
	public void init() throws Exception {
		Reviewdata reviewdata = reviewdataService.list().get(CommonConstant.ZERO);
		North north = northService.list().get(CommonConstant.ZERO);
		Up up = upService.list().get(CommonConstant.ZERO);
		//复盘数据
		dayReviewMap.put(CrawConstant.REVIEW, ReviewDataVo.of(reviewdata));
		//北向资金
		dayReviewMap.put(CrawConstant.NORTH,NorthVo.of(north));
		//上涨家数
		dayReviewMap.put(CrawConstant.UP,UpVo.of(up));
	}

	/**
	 * 周一到周五凌晨5点 初始化股票map缓存及每日更新 避免新股出现
	 */
	@PostConstruct
	@Scheduled(cron = "0 0 5 ? * MON-FRI")
	public void initMap() throws Exception {
		DayRestShData();
		DayRestSzData();
		getShData().forEach(e -> {
			StockCodeMap.put(e.getStockCode(), e);
			// 去除空格
			StockNameMap.put(e.getStockName().replace(" ", ""), e);
		});
		getSzData().forEach(e -> {
			StockCodeMap.put(e.getStockCode(), e);
			// 去除空格
			StockNameMap.put(e.getStockName().replace(" ", ""), e);
		});
		log.info("初始/每日更新股票数据缓存成功");
	}

	/**
	 * 周一到周五凌晨5点 爬取深交所股票，保存为xlsx文件
	 *
	 * @throws Exception
	 */
	public void DayRestSzData() {
		try {
			// 下载地址
			String downURL = CrawConstant.SZ_URL;
			// 地址
			URL url = new URL(downURL);
			// 打开地址
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			// 获取流
			InputStream is = urlConnection.getInputStream();
			// 写入流
			Random random = new Random();
			// 如果文件存在则替换/重写
			FileOutputStream fos = new FileOutputStream(CrawConstant.SZ_EXCEL);

			// 写入文件
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			log.info("每日更新深交所股票结束");
			// 关闭流
			fos.close();
			is.close();
			urlConnection.disconnect(); // 断开连接
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 周一到周五凌晨5点 爬取上交所股票，保存为js脚本
	 */
	public void DayRestShData() {
		RestTemplate restTemplate = new RestTemplate();
		// 处理乱码问题
		restTemplate.getMessageConverters().set(CommonConstant.ONE,
				new StringHttpMessageConverter(StandardCharsets.UTF_8));
		ResponseEntity<String> entity = restTemplate.getForEntity(CrawConstant.SH_URL, String.class);
		String res = entity.getBody();
		try {
			FileOutputStream fos = new FileOutputStream(CrawConstant.SH_JS);
			fos.write(res.getBytes());
			log.info("每日更新上交所股票结束");
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 执行js脚本获取上交所股票
	 *
	 * @return
	 */
	public static List<StockPushVo> getShData() {
		log.info("开始执行上交所股票js文件");
		List<StockPushVo> data = null;
		try {
			JavaScriptProvider jsProvider = new JavaScriptProvider();
			ShJS shJS = shPr.loadJs(CrawConstant.SH_JS, ShJS.class);
			// 转换数据类型并过滤
			data = shJS.get_data().stream().map(StockPushVo::valueofSh)
					.filter(vo -> vo.getStockCode().startsWith("SH6")).collect(Collectors.toList());
			log.info("执行上交所股票js文件结束");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ScriptException e) {
			e.printStackTrace();
		} finally {
			return data;
		}
	}

	/**
	 * 从excel获取深交所股票，poi
	 *
	 * @return
	 */
	public static List<StockPushVo> getSzData() {
		List<StockPushVo> list = new ArrayList<>();
		try {
			log.info("开始操作深交所股票excel文件");
			// 利用poi操作excel.xlsx文件
			InputStream inputStream = new FileInputStream(new File(CrawConstant.SZ_EXCEL));
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(CommonConstant.ZERO);
			int lastRow = sheet.getLastRowNum();
			for (int i = 1; i < lastRow; i++) {
				XSSFRow row = sheet.getRow(i);
				if (row != null) {
					StockPushVo vo = new StockPushVo();
					vo.setStockCode(CrawConstant.SZ + row.getCell(CommonConstant.FOUR).getStringCellValue());
					vo.setStockName(row.getCell(CommonConstant.FIVE).getStringCellValue());
					list.add(vo);
				}
			}
			log.info("操作深交所股票excel文件结束");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 今日数据
	 */
	public static void getReviewData() {
		ReviewDataVo reviewDataVo = new ReviewDataVo();
		NorthVo northVo = new NorthVo();
		UpVo upVo = new UpVo();
		//历史新高
		reviewDataVo.setHistoryHigh(
				CrawUtil.getDayData(CrawConstant.QUESTION_HISTORY_HIGH, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//一年新高
		reviewDataVo.setYearHigh(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_HIGH, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//一年新低
		reviewDataVo.setYearLow(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_LOW, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日跌停
		reviewDataVo.setDownLimit(CrawUtil.getDayData(CrawConstant.QUESTION_DOWN_LIMIT, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日涨停
		reviewDataVo.setUpLimit(CrawUtil.getDayData(CrawConstant.QUESTION_UP_LIMIT, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日非一字涨停
		reviewDataVo.setNoOneUp(CrawUtil.getDayData(CrawConstant.QUESTION_NO_ONE_UP, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日跌幅超5%
		reviewDataVo.setDownFive(CrawUtil.getDayData(CrawConstant.QUESTION_DOWN_FIVE, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日涨幅超5%
		reviewDataVo.setUpFive(CrawUtil.getDayData(CrawConstant.QUESTION_UP_FIVE, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//总成交额
		reviewDataVo.setTurnOver(CrawUtil.getTurnOver());
		//上证指数涨跌
		String shTrend = CrawUtil.getIndex(CrawConstant.SH_INDEX_URL).getTrend();
		reviewDataVo.setSH_INDEX(shTrend);
		//深证成指涨跌
		reviewDataVo.setSZ_INDEX(CrawUtil.getIndex(CrawConstant.SZ_INDEX_URL).getTrend());
		//创业扳指涨跌
		reviewDataVo.setBusiness_INDEX(CrawUtil.getIndex(CrawConstant.BUS_INDEX_URL).getTrend());
		//上涨家数
		Integer upAll = CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK);
		reviewDataVo.setUpAll(upAll);

		upVo.setFifteenup(upAll);
		// 09:25上涨家数
		upVo.setNinetfup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.NINE_TWENTY_FIVE + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 10:00上涨家数
		upVo.setTenup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.TEN + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 11:00上涨家数
		upVo.setElevenup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.ELEVEN + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 13:00上涨家数
		upVo.setThirteenup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.THIRTEENT + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 14:00上涨家数
		upVo.setFourteenup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.FOURTEEN + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 14:30上涨家数
		upVo.setFourteentheup(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.FOURTEENTHE + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));

		//沪股通
		String hg = CrawUtil.getNorth(CrawConstant.HGTB_URL);
		northVo.setHgtb(hg);
		//深股通
		String sg = CrawUtil.getNorth(CrawConstant.SGTB_URL);
		northVo.setSgtb(sg);
		//总计
		northVo.setNorthAll(BigDecimal.valueOf(Float.valueOf(hg)).add(BigDecimal.valueOf(Float.valueOf(sg))).setScale(CommonConstant.TWO,BigDecimal.ROUND_DOWN).toString());
		northVo.setShIndex(shTrend);

		//日期
		Calendar c=Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month=c.get(Calendar.MONTH)+1;
		int date=c.get(Calendar.DATE);
		String rdid = year+""+month+""+date;
		reviewDataVo.setRdid(rdid);
		northVo.setRdid(rdid);
		upVo.setRdid(rdid);
		//复盘数据
		dayReviewMap.put(CrawConstant.REVIEW,reviewDataVo);
		//北向资金
		dayReviewMap.put(CrawConstant.NORTH,northVo);
		//上涨家数
		dayReviewMap.put(CrawConstant.UP,upVo);
	}


	/**
	 * 爬取雪球个股信息
	 *
	 * @param stockCode
	 *            股票代码
	 * @return
	 */
	public static StockPushVo getOne(String stockCode) {
		// 例如：https://xueqiu.com/S/SH600546
		String url =  CrawConstant.XUE_QIU_ONE + stockCode;
		Document document = null;
		List<String> list = new ArrayList<>();
		StockPushVo vo = null;
		try {
			document = Jsoup.connect(url).get();
			// 获取主体，本质是个list
			Elements body = document.getElementsByTag("body");
			// getElementsByClass根据类选择器，getElementsByTag根据标签选择器
			// 股票名称
			String stockName = body.first().getElementsByClass("stock-name").first().text();
			// 现价
			String nowPrice = body.first().getElementsByClass("stock-current").first().text();
			// 涨跌
			String trend = body.first().getElementsByClass("stock-change").first().text()
					.split(" ")[CommonConstant.ONE];
			// 成交额
			Elements elements = body.first().getElementsByClass("separateTop");
			String turnover = elements.first().getElementsByTag("td").get(CommonConstant.THREE).text()
					.split("：")[CommonConstant.ONE];
			// 填充信息
			vo = StockCodeMap.get(stockCode);
			vo.setStockName(stockName);
			vo.setNowPrice(nowPrice);
			vo.setTrend(trend);
			vo.setTurnover(turnover);
			vo.setXueQiuLink(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 爬取问财信息
	 *
	 * @param question
	 *            条件
	 * @return
	 */
	public static ResponseEntity<String> getWenCai(String question, String secondary_intent,int page) {
		RestTemplate restTemplate = new RestTemplate();
		log.info("开始爬取问财信息，条件：{}", question);
		// 封装请求内容
		Map<String, Object> params = new HashMap<>();
		Map<String, Object> addMap = new HashMap<>();
		addMap.put("contentType", "json");
		addMap.put("searchInfo", true);
		Map<String, Object> urpMap = new HashMap<>();
		urpMap.put("scene", 1);
		urpMap.put("company", 1);
		urpMap.put("business", 1);
		addMap.put("urp", urpMap);
		params.put("add_info", addMap);
		params.put("block_list", "");
		HashMap<String, String> logMap = new HashMap<>();
		logMap.put("input_type", "typewrite");
		params.put("log_info", logMap);
		params.put("page", page);
		params.put("perpage", CommonConstant.One_Hundred);
		params.put("query_area", "");
		params.put("question", question);
		params.put("rsh", "Ths_iwencai_Xuangu_z2chzlngxpcdgidg71ddgqenmcv2x4jy");
		params.put("secondary_intent", secondary_intent);
		params.put("source", "Ths_iwencai_Xuangu");
		params.put("version", "2.0");
		HttpHeaders headers = new HttpHeaders();
		String heXinV = JavaScriptProvider.getHeXinV();
		log.info("本次问财hexin-V:{}", heXinV);
		headers.set("hexin-v", heXinV);
		headers.set("Cookie",
				"other_uid=Ths_iwencai_Xuangu_z2chzlngxpcdgidg71ddgqenmcv2x4jy; ta_random_userid=hxo4x7vhm6; cid=2078830176e1a4273353b8af5f0cef731673344264; PHPSESSID=01611bc000b9c08efa02f2adf7e42cde; cid=2078830176e1a4273353b8af5f0cef731673344264; ComputerID=2078830176e1a4273353b8af5f0cef731673344264; WafStatus=0; v="
						+ heXinV);
		headers.set("Connection", "keep-alive");
		headers.set("Accept-Language", "zh-CN,zh;q=0.9");
		headers.set("Accept", "application/json, text/plain, */*");
		headers.set("User-Agent", UserAgent.getUserAgentWindows());
		headers.set("Host", "www.iwencai.com");
		headers.set("Content-Length", "369");
		headers.set("Content-Type", "application/json");
		HttpEntity httpEntity = new HttpEntity(params, headers);
		ResponseEntity<String> entity = restTemplate.postForEntity(CrawConstant.WENCAI_URL, httpEntity, String.class);
		return entity;
	}

	/**
	 * 基础解析
	 *
	 * @param entity
	 * @return
	 */
	public static JSONObject baseResolution(ResponseEntity<String> entity) {
		JSONObject json = JSONObject.parseObject(entity.getBody());
		JSONObject data = (JSONObject) json.get("data");
		JSONArray answer = (JSONArray) data.get("answer");
		JSONObject answerZero = (JSONObject) answer.get(0);
		return answerZero;
	}

	/**
	 * 解析出个股
	 *
	 * @param entity
	 * @return
	 */
	public static List<StockPushVo> resolution(ResponseEntity<String> entity) {
		JSONObject answerZero = baseResolution(entity);
		JSONArray txt = (JSONArray) answerZero.get("txt");
		JSONObject txtZero = (JSONObject) txt.get(0);
		JSONObject content = (JSONObject) txtZero.get("content");
		JSONArray components = (JSONArray) content.get("components");
		JSONObject componentsZero = (JSONObject) components.get(0);
		JSONObject componentsZeroData = (JSONObject) componentsZero.get("data");
		JSONArray datas = (JSONArray) componentsZeroData.get("datas");
		List<Map> map = datas.toJavaList(Map.class);
		// 转换并过滤非上交与深交的股票
		List<StockPushVo> res = map.stream().map(StockPushVo::of).filter(e -> e != null)
				.collect(Collectors.toList());
		return res;
	}

	/**
	 * 解析出数量
	 *
	 * @param entity
	 * @return
	 */
	public static int resolutionNum(ResponseEntity<String> entity) {
		JSONObject answerZero = baseResolution(entity);
		JSONArray txt = (JSONArray) answerZero.get("txt");
		JSONObject txtZero = (JSONObject) txt.get(0);
		JSONObject content = (JSONObject) txtZero.get("content");
		JSONArray components = (JSONArray) content.get("components");
		JSONObject componentsZero = (JSONObject) components.get(0);
		JSONObject componentsZeroData = (JSONObject) componentsZero.get("data");
		JSONObject meta = (JSONObject) componentsZeroData.get("meta");
		JSONObject extra = (JSONObject) meta.get("extra");
		return (Integer) extra.get("row_count");
	}

	/**
	 * 今日数据
	 * 如：一年新高等
	 */
	public static List<StockPushVo> getDayData(String question,String secondary_intent) {
		ResponseEntity<String> entity = getWenCai(question, secondary_intent,CommonConstant.ONE);
		List<StockPushVo> res = new ArrayList<>();
		log.info("开始爬取：条件{}",question);
		//总数量
		int sum = resolutionNum(entity);
		//小于100：一页
		if (sum <= CommonConstant.One_Hundred){
			res.addAll(resolution(entity));
			//大于100：多页
		}else {
			//总页数
			int pageSum = sum/CommonConstant.One_Hundred + CommonConstant.ONE;
			//第一页
			res.addAll(resolution(entity));
			//从第二页开始遍历
			for (int i = 2; i <= pageSum; i++) {
				res.addAll(resolution(getWenCai(question, secondary_intent,i)));
			}
		}
		log.info("结果{}",res);
		return res;
	}

	/**
	 *  今日数量
	 *  如：上涨家数
	 */
	public static Integer getNum(String question,String secondary_intent) {
		log.info("开始爬取：条件{}",question);
		ResponseEntity<String> entity = getWenCai(question, secondary_intent,CommonConstant.ONE);
		int res = resolutionNum(entity);
		log.info("结果：{}",res);
		return res;
	}


	/** 爬取指数详情 */
	public static StockPushVo getIndex(String url){
		Document document = null;
		StockPushVo stockPushVo = new StockPushVo();
		log.info("爬取：url：{}",url);
		try {
			document = Jsoup.connect(url).get();
			// 获取主体，本质是个list
			Elements body = document.getElementsByTag("body");
			//现值
			String nowPrice = body.first().getElementsByClass("board-hq").first().getElementsByTag("span").last().text();
			stockPushVo.setNowPrice(nowPrice);
			//成交额
			Elements dd = body.first().getElementsByClass("board-infos").first().getElementsByTag("dd");
			stockPushVo.setTurnover(dd.last().text());
			//涨跌
			String trend = dd.get(dd.size()-CommonConstant.TWO).text();
			stockPushVo.setTrend(trend);
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.info("结果：{}",stockPushVo);
		return stockPushVo;
	}

	/**
	 * 北向资金
	 * @param url
	 *      沪股通/深股通
	 * @return
	 */
	public static String getNorth(String url){
		Document document = null;
		String num = null;
		log.info("爬取：url：{}",url);
		try {
			num = Jsoup.connect(url).get().getElementsByTag("body").first()
					.getElementsByClass("m-table J-ajax-table").first()
					.getElementsByTag("tbody").first()
					.getElementsByTag("tr").first()
					.getElementsByClass("tc").first().nextElementSibling()
					.text().split(CrawConstant.YI)[CommonConstant.ZERO];

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			log.info("结果：{}",num);
			return num;
		}
	}

	/**
	 * 今日成交额(同花顺全A)
	 */
	public static String getTurnOver() {
		ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_TURNOVER, CrawConstant.ZHI_SHU,CommonConstant.ONE);
		JSONObject data = baseResolution(entity);
		String content = (String) data.get("text_answer");
		return content.split(CrawConstant.TURNOVER)[CommonConstant.ONE].replace(CrawConstant.PERIOD, "");
	}


}
