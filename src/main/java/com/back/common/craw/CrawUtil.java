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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.CrawConstant;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.StockPushVo;

import com.back.service.ReviewdataService;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.script.ScriptException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

	private static final JavaScriptProvider<ShJS> shPr = new JavaScriptProvider<>();
	/** <股票代码,股票信息> */
	public static Map<String, StockPushVo> StockCodeMap = new HashMap<>();
	/** <股票名称,股票信息> */
	public static Map<String, StockPushVo> StockNameMap = new HashMap<>();

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
	public static ReviewDataVo getReviewData() {
		ReviewDataVo vo = new ReviewDataVo();
		//历史新高
		vo.setHistoryHigh(
				CrawUtil.getDayData(CrawConstant.QUESTION_HISTORY_HIGH, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//一年新高
		vo.setYearHigh(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_HIGH, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//一年新低
		vo.setYearLow(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_LOW, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日跌停
		vo.setDownLimit(CrawUtil.getDayData(CrawConstant.QUESTION_DOWN_LIMIT, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日涨停
		vo.setUpLimit(CrawUtil.getDayData(CrawConstant.QUESTION_UP_LIMIT, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日非一字涨停
		vo.setNoOneUp(CrawUtil.getDayData(CrawConstant.QUESTION_NO_ONE_UP, CrawConstant.STOCK).stream().collect(Collectors.toMap(StockPushVo::getStockCode, i -> i)));
		//今日跌幅超5%
		vo.setDownFive(CrawUtil.getNum(CrawConstant.QUESTION_DOWN_FIVE, CrawConstant.STOCK));
		//今日涨幅超5%
		vo.setUpFive(CrawUtil.getNum(CrawConstant.QUESTION_UP_FIVE, CrawConstant.STOCK));
		//总成交额
		vo.setTurnOver(CrawUtil.getTurnOver());
		//上证指数涨跌
		vo.setSH_INDEX(CrawUtil.getIndex(CrawConstant.SH_INDEX_URL).getTrend());
		//深证成指涨跌
		vo.setBusiness_INDEX(CrawUtil.getIndex(CrawConstant.BUS_INDEX_URL).getTrend());
		//创业扳指涨跌
		vo.setBusiness_INDEX(CrawUtil.getIndex(CrawConstant.BUS_INDEX_URL).getTrend());
		//上涨家数
		vo.setUpAll(CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK));
		// 09:25上涨家数
		CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.NINE_TWENTY_FIVE + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK);
		// 10:00上涨家数
		CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.TEN + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK);
		// 11:00上涨家数
		CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.ELEVEN_THIRTY + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK);
		// 14:00上涨家数
		CrawUtil.getNum(CrawConstant.QUESTION_DAY + CrawConstant.FOURTEEN + CrawConstant.QUESTION_UP_ALL, CrawConstant.STOCK);
		//沪股通
		CrawUtil.getNorth(CrawConstant.HGTB_URL);
		//深股通
		CrawUtil.getNorth(CrawConstant.SGTB_URL);
		return vo;
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
		log.info("开始爬取个股信息：股票编码{}", stockCode);
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
			log.info("个股爬取结果：{}", vo.toString());
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
		log.info("爬取结果：{}", json.toString());
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
		List<StockPushVo> res = map.stream().map(StockPushVo::reviewData).filter(e -> e != null)
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
		return res;
	}

	/**
	 *  今日数量
	 *  如：上涨家数
	 */
	public static Integer getNum(String question,String secondary_intent) {
		log.info("开始爬取：条件{}",question);
		ResponseEntity<String> entity = getWenCai(question, secondary_intent,CommonConstant.ONE);
		return resolutionNum(entity);
	}


	/** 爬取指数详情 */
	public static StockPushVo getIndex(String url){
		Document document = null;
		StockPushVo stockPushVo = new StockPushVo();
		try {
			document = Jsoup.connect(url).get();
			// 获取主体，本质是个list
			Elements body = document.getElementsByTag("body");
			//现值
			String nowPrice = body.first().getElementsByClass("board-xj arr-rise").first().text();
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
		try {
			num = Jsoup.connect(url).get().getElementsByTag("body").first()
					.getElementsByClass("m-table J-ajax-table").first()
					.getElementsByTag("tbody").first()
					.getElementsByTag("tr").first()
					.getElementsByClass("tc").first().nextElementSibling()
					.text();

		} catch (IOException e) {
			e.printStackTrace();
		}finally {
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
