package com.back.common.craw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.CrawConstant;
import com.back.common.utils.CodeUtil;
import com.back.common.utils.DateUtil;
import com.back.common.utils.MathUtil;
import com.back.entity.pojo.North;
import com.back.entity.pojo.Reviewdata;
import com.back.entity.pojo.Up;
import com.back.entity.vo.BaseStockVo;
import com.back.entity.vo.NorthVo;
import com.back.entity.vo.ReviewDataVo;
import com.back.entity.vo.StockPushVo;

import com.back.entity.vo.UpLimitVo;
import com.back.entity.vo.UpVo;
import com.back.service.NorthService;
import com.back.service.ReviewdataService;
import com.back.service.UpService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.sun.org.apache.regexp.internal.RE;
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
import org.springframework.http.HttpMethod;
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

	private static final JavaScriptProvider<ShJS> shPr = new JavaScriptProvider<>();
	/** <股票代码,股票名称> */
	public static Map<String, StockPushVo> StockCodeMap = new HashMap<>();
	/** <股票名称,股票代码> */
	public static Map<String, String> StockNameCodeMap = new HashMap<>();
	/**
	 * 复盘数据
	 */
	public static ReviewDataVo vo = new ReviewDataVo();

	public static int crawSum ;

	@PostConstruct
	public void init() throws Exception {
		//复盘数据
		vo = reviewdataService.list().stream()
				.sorted(Comparator.comparing(Reviewdata::getRdid).reversed())
				.map(ReviewDataVo::of).collect(Collectors.toList()).get(CommonConstant.ZERO);
		crawSum = 0;
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
			if (!StockCodeMap.containsKey(e.getStockCode())){
				StockCodeMap.put(e.getStockCode(), e);
				// 去除空格
				StockNameCodeMap.put(e.getStockName().replace(" ", ""), e.getStockCode());
			}
		});
		getSzData().forEach(e -> {
			if (!StockCodeMap.containsKey(e.getStockCode())){
				StockCodeMap.put(e.getStockCode(), e);
				// 去除空格
				StockNameCodeMap.put(e.getStockName().replace(" ", ""), e.getStockCode());
			}
		});
		crawSum = 0;
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
	public static Map<String, Object> getReviewData() {
		Map<String, Object> res = new HashMap<>();
		ReviewDataVo reviewDataVo = new ReviewDataVo();
		UpVo upVo = new UpVo();
		//历史新高
		reviewDataVo.setHistoryHigh(
				CrawUtil.getDayData(CrawConstant.QUESTION_HISTORY_HIGH, CrawConstant.STOCK));
		//一年新高
		reviewDataVo.setYearHigh(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_HIGH, CrawConstant.STOCK));
		//一年新低
		reviewDataVo.setYearLow(CrawUtil.getDayData(CrawConstant.QUESTION_YEAR_LOW, CrawConstant.STOCK));
		//今日跌停
		reviewDataVo.setDownLimit(CrawUtil.getDayData(CrawConstant.QUESTION_DOWN_LIMIT, CrawConstant.STOCK));

		//今日涨停
		reviewDataVo.setUpLimit(CrawUtil.getUpData(CrawConstant.QUESTION_UP_LIMIT, CrawConstant.STOCK));

		//今日非一字涨停
		reviewDataVo.setNoOneUp(CrawUtil.getDayData(CrawConstant.QUESTION_NO_ONE_UP, CrawConstant.STOCK));
		//今日跌幅超5%
		reviewDataVo.setDownFive(CrawUtil.getDayData(CrawConstant.QUESTION_DOWN_FIVE, CrawConstant.STOCK));
		//今日涨幅超5%
		reviewDataVo.setUpFive(CrawUtil.getDayData(CrawConstant.QUESTION_UP_FIVE, CrawConstant.STOCK));
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

		//北向资金
		NorthVo northVo = getNorthJme();
		northVo.setShIndex(shTrend);

		String rdid = DateUtil.getRdid();
		reviewDataVo.setRdid(rdid);
		northVo.setRdid(rdid);
		upVo.setRdid(rdid);

		vo = reviewDataVo;
		//复盘数据
		res.put(CrawConstant.REVIEW,reviewDataVo);
		//北向资金
		res.put(CrawConstant.NORTH,northVo);
		//上涨家数
		res.put(CrawConstant.UP,upVo);
		return res;
	}

	/**
	 *  北向资金净买入，渲染后的html爬取
	 * @return
	 */
	public static NorthVo getNorthJme() {
		NorthVo vo = new NorthVo();
		String url = CrawConstant.NORTH_JME_URL;
		final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

		webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
		webClient.getOptions().setActiveXNative(false);//不启用ActiveX
		webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
		webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
		webClient.getOptions().setDownloadImages(false);//不下载图片
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

		HtmlPage page = null;
		try {
			page = webClient.getPage(url);//尝试加载上面图片例子给出的网页
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			webClient.close();
		}

		webClient.waitForBackgroundJavaScript(8000);//异步JS执行需要耗时,所以这里线程要阻塞8秒,等待异步JS执行结束

		String pageXml = page.asXml();//直接将加载完成的页面转换成xml格式的字符串
		Document parse = Jsoup.parse(pageXml);
		// 获取主体，本质是个list
		Element body = parse.getElementsByTag("body").first();
		// 北向资金净买额
		vo.setNorthAll(body.getElementById("north_jme").text());
		// 沪股通净买额
		vo.setHgtb(body.getElementById("north_h_jme").text());
		// 深股通净买额
		vo.setSgtb(body.getElementById("north_s_jme").text());
		return vo;
	}

	/**
	 * 爬取个股信息
	 *
	 * @param stockCode
	 *            股票代码
	 * @return
	 */
	public static StockPushVo getOneBySinA(String stockCode){
		RestTemplate restTemplate = new RestTemplate();
		StockPushVo vo = null;
		//小写字母的代码
		String lowwerCode = stockCode.substring(CommonConstant.ZERO, CommonConstant.TWO).toLowerCase()
				+ stockCode.substring(CommonConstant.TWO);
		//请求头
		HttpHeaders headers = new HttpHeaders();
		headers.set("Referer",CrawConstant.HEADER_REFERER.replace("$",lowwerCode));
		HttpEntity httpEntity = new HttpEntity(null, headers);
		ResponseEntity<String> entity = restTemplate.exchange(CrawConstant.SINA_ONE+lowwerCode, HttpMethod.GET, httpEntity, String.class);
		//var hq_str_sh600519="贵州茅台,1800.010,1797.000,1784.000,1805.970,1775.000,1784.000,1784.100,1667577,
		// 2980574806.000,54000,1784.000,100,1783.910,200,1783.890,200,1783.660,100,
		// 1783.600,100,1784.100,100,1784.110,100,1784.160,200,1784.210,800,1784.300,2023-02-08,15:00:00,00,";
		String body = entity.getBody();
		String substring = body.substring(body.indexOf("\""), body.length() - CommonConstant.TWO);
		String[] split = substring.split(",");
		String nameTemp = split[CommonConstant.ZERO];
		String stockName = nameTemp.substring(CommonConstant.ONE);
		String nowPrice = split[CommonConstant.THREE];
		String yesTodayPrice = split[CommonConstant.TWO];
		String temp = String.valueOf(BigDecimal.valueOf((Float.valueOf(nowPrice)-Float.valueOf(yesTodayPrice))
				/Float.valueOf(yesTodayPrice)*100).setScale(CommonConstant.TWO,BigDecimal.ROUND_HALF_DOWN));
		String trend = null;
		if (temp.startsWith("-")){
			trend = temp.substring(CommonConstant.ZERO,CommonConstant.FIVE)+"%";
		}else {
			trend = temp.substring(CommonConstant.ZERO,CommonConstant.FOUR)+"%";
		}

		String turnover = MathUtil.formatNum(split[CommonConstant.NINE],false);

		// 填充信息
		vo = StockCodeMap.get(stockCode);
		if (vo == null){
			vo = new StockPushVo(stockCode,stockName);
			StockCodeMap.put(stockCode,vo);
			StockNameCodeMap.put(stockName,stockCode);
		}
		vo.setStockName(stockName);
		vo.setNowPrice(nowPrice);
		vo.setTrend(trend);
		vo.setTurnover(turnover);
		//填充链接
		vo.fillLink();
		//日期
		vo.setRdid(DateUtil.getRdid());
		return vo;
	}

	/**
	 * 爬取雪球个股信息
	 *
	 * @param stockCode
	 *            股票代码
	 * @return
	 */
	public static StockPushVo getOneByXueQiu(String stockCode) {
		// 例如：https://xueqiu.com/S/SH600546
		String url =  CrawConstant.XUE_QIU_ONE + stockCode;
		Document document = null;
		List<String> list = new ArrayList<>();
		StockPushVo vo = null;
		try {
			document = Jsoup.connect(url).get();
			// 获取主体，本质是个list
			Element body = document.getElementsByTag("body").first();
			// getElementsByClass根据类选择器，getElementsByTag根据标签选择器
			// 股票名称
			String temp = body.getElementsByClass("stock-name").first().text();
			String stockName = temp.substring(CommonConstant.ZERO, temp.lastIndexOf("("));
			// 现价
			String nowPrice = body.getElementsByClass("stock-current").first().text();
			// 涨跌
			String trend = body.getElementsByClass("stock-change").first().text()
					.split(" ")[CommonConstant.ONE];
			// 成交额
			Elements elements = body.getElementsByClass("separateTop");
			String turnover = elements.first().getElementsByTag("td").get(CommonConstant.THREE).text()
					.split("：")[CommonConstant.ONE];
			// 填充信息
			vo = StockCodeMap.get(stockCode);
			if (vo == null){
				vo = new StockPushVo(stockCode,stockName);
				StockCodeMap.put(stockCode,vo);
				StockNameCodeMap.put(stockName,stockCode);
			}
			vo.setStockName(stockName);
			vo.setNowPrice(nowPrice);
			vo.setTrend(trend);
			vo.setTurnover(turnover);
			//填充链接
			vo.fillLink();
			//日期
			vo.setRdid(DateUtil.getRdid());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 获取雪球热股
	 * @param url
	 * @return
	 */
	public static List<StockPushVo>  getHotByXueQiu(String url) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Cookie","device_id=34cff051773e8c372c1bcd3d177c0c13; s=bt12lj4g4l; bid=55f98a025015352661adabaed55fe6c2_lc43l8ar; Hm_lvt_1db88642e346389874251b5a1eded6e3=1675253209,1675253477,1675299587,1675386828; snbim_minify=true; xq_a_token=06c970814873215375f1cd02e4c8e64b740f6704; xqat=06c970814873215375f1cd02e4c8e64b740f6704; xq_r_token=9546eea976a2e2f78e2667bb2221518d5306c5b6; xq_id_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJ1aWQiOi0xLCJpc3MiOiJ1YyIsImV4cCI6MTY3NzM3MDg0NiwiY3RtIjoxNjc1NzY4MTQ0Nzg1LCJjaWQiOiJkOWQwbjRBWnVwIn0.YTmROb47LZSFJ5c3stxIF_ar0bVCsNmKZ0yOZyoAwPaGFlqK6J2EpmH_BMjs2fmrTDLJaLthmwYtj_cysm47fY7sMF6jRAgrh5Ze58NqThDYzXwWuek7IvrgCGfh8WWgjNzNCWGt5EDsbmySoJg1hI9kSakSl2rzUbcfhMDS-H16t52XcwdGRU_WBun8Tih82pzgGsHB1-6DMHQtweVSpgKf8vrDtv1GBDVUXE2tCgtm1k5dEoZldXLUciqjnRB5OP0cbxOmFTfUMKVWZT1DZAtMkQyr-9O2IEqvxgetIHOmydTWFHh0sBMazOQJ5-m6zpWzZw_EGBD68Syd8ZxAXg; u=571675768192753; Hm_lpvt_1db88642e346389874251b5a1eded6e3=1675768216");
		HttpEntity httpEntity = new HttpEntity(null, headers);
		ResponseEntity<JSONObject> entity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
		JSONObject body = entity.getBody();
		Map data = (Map) body.get("data");
		List<Map> items =  (ArrayList)data.get("items");
		//只包含
		return items.stream().map((e) -> {
			StockPushVo vo = new StockPushVo();
			vo.setStockName( (String) e.get("name"));
			vo.setStockCode( (String) e.get("symbol"));
			vo.setNowPrice( String.valueOf(e.get("current")));
			vo.setTrend( String.valueOf(e.get("percent"))+"%");
			vo.fillLink();
			return vo;
		}).collect(Collectors.toList());
	}

	/**
	 * 获取同花顺热股
	 * @param url
	 * @return
	 */
	public static List<StockPushVo>  getHotByTh(String url) {
		List<StockPushVo> res = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JSONObject> entity = restTemplate.getForEntity(url, JSONObject.class);
		JSONObject body = entity.getBody();


		Map data = (Map) body.get("data");
		List<Map> items =  (ArrayList)data.get("stock_list");
		for (int i = 0; i < CommonConstant.TEN; i++) {
			Map map = items.get(i);
			StockPushVo vo = new StockPushVo();
			String stockCode = CodeUtil.numToCode(String.valueOf(map.get("code")));
			StockPushVo temp = getOneBySinA(stockCode);
			vo.setStockCode(stockCode);
			vo.setStockName(String.valueOf(map.get("name")));
			vo.setNowPrice(temp.getNowPrice());
			vo.setTrend(temp.getTrend());
			vo.fillLink();
			res.add(vo);
		}
		return res;
	}

	/**
	 * 获取淘股吧热股
	 * @return
	 */
	public static Map<String,List<StockPushVo>>  getHotByTaoGu() {
		Document document = null;
		Map<String,List<StockPushVo>> res = new HashMap<>();
		List<StockPushVo> shList = new ArrayList<>();
		List<StockPushVo> szList = new ArrayList<>();
		try {
			document = Jsoup.connect(CrawConstant.HOT_TAOGU).get();
			// 获取主体，本质是个list
			Element body = document.getElementsByTag("body").first();
			// getElementsByClass根据类选择器，getElementsByTag根据标签选择器
			// 沪市
			Element sh = body.getElementsByClass("hot_hsnr_l left").first();
			//深市
			Element sz = body.getElementsByClass("hot_hsnr_l right b_rnone").first();

			Elements shCode = sh.getElementsByTag("input");
			for (Element element : shCode) {
				String stockCode = element.attr("value");
				StockPushVo vo = getOneBySinA(CodeUtil.toUp(stockCode));
				shList.add(vo);
			}
			Elements szCode = sz.getElementsByTag("input");
			for (Element element : szCode) {
				String stockCode = element.attr("value");
				StockPushVo vo = getOneBySinA(CodeUtil.toUp(stockCode));
				szList.add(vo);
			}
			res.put("sh",shList);
			res.put("sz",szList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 获取东方财富热股
	 */
	public static List<StockPushVo> getHotByDf(String url){
		List<StockPushVo> res = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		ResponseEntity<JSONObject> entity = restTemplate.getForEntity(url,JSONObject.class);
		JSONObject body = entity.getBody();
		Map data = (Map) body.get("data");
		ArrayList<Map> list = (ArrayList<Map>) data.get("diff");
		for (int i = 0; i < CommonConstant.TEN; i++) {
			Map map = list.get(i);
			StockPushVo vo = new StockPushVo();
			String code = String.valueOf(map.get("f12"));
			String stockCode = CodeUtil.numToCode(code);
			if (stockCode != null){
				vo.setStockCode(stockCode);
			}
			vo.setStockName(String.valueOf(map.get("f14")));
			vo.setNowPrice(String.valueOf(map.get("f2")));
			vo.setTrend(String.valueOf(map.get("f3"))+"%");
			vo.fillLink();
			res.add(vo);
		}
		return res;
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
	public static List<Map> resolution(ResponseEntity<String> entity) {
		JSONObject answerZero = baseResolution(entity);
		JSONArray txt = (JSONArray) answerZero.get("txt");
		JSONObject txtZero = (JSONObject) txt.get(0);
		JSONObject content = (JSONObject) txtZero.get("content");
		JSONArray components = (JSONArray) content.get("components");
		JSONObject componentsZero = (JSONObject) components.get(0);
		JSONObject componentsZeroData = (JSONObject) componentsZero.get("data");
		JSONArray datas = (JSONArray) componentsZeroData.get("datas");
		List<Map> mapList = datas.toJavaList(Map.class);
		// 转换并过滤非上交与深交的股票
		return mapList;
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
	public static Set<StockPushVo> getDayData(String question,String secondary_intent) {
		ResponseEntity<String> entity = getWenCai(question, secondary_intent,CommonConstant.ONE);
		Set<StockPushVo> res = new HashSet<>();
		//总数量
		int sum = resolutionNum(entity);
		//小于100：一页
		if (sum <= CommonConstant.One_Hundred){
			res.addAll(resolution(entity).stream().map(StockPushVo::of).filter(e -> e != null)
					.collect(Collectors.toList()));
			//大于100：多页
		}else {
			//总页数
			int pageSum = sum/CommonConstant.One_Hundred + CommonConstant.ONE;
			//第一页
			res.addAll(resolution(entity).stream().map(StockPushVo::of).filter(e -> e != null)
					.collect(Collectors.toList()));
			//从第二页开始遍历
			for (int i = 2; i <= pageSum; i++) {
				res.addAll(resolution(getWenCai(question, secondary_intent,i)).stream().map(StockPushVo::of).filter(e -> e != null)
						.collect(Collectors.toList()));
			}
		}
		log.info("结果长度：{}",res.size());
		return res;
	}
	/**
	 * 涨停数据
	 */
	public static Set<UpLimitVo> getUpData(String question,String secondary_intent) {
		ResponseEntity<String> entity = getWenCai(question, secondary_intent,CommonConstant.ONE);
		Set<UpLimitVo> res = new HashSet<>();
		//总数量
		int sum = resolutionNum(entity);
		//小于100：一页
		if (sum <= CommonConstant.One_Hundred){
			res.addAll(resolution(entity).stream().map(UpLimitVo::ofUp).filter(e -> e != null)
					.collect(Collectors.toList()));
			//大于100：多页
		}else {
			//总页数
			int pageSum = sum/CommonConstant.One_Hundred + CommonConstant.ONE;
			//第一页
			res.addAll(resolution(entity).stream().map(UpLimitVo::ofUp).filter(e -> e != null)
					.collect(Collectors.toList()));
			//从第二页开始遍历
			for (int i = 2; i <= pageSum; i++) {
				res.addAll(resolution(getWenCai(question, secondary_intent,i)).stream().map(UpLimitVo::ofUp).filter(e -> e != null)
						.collect(Collectors.toList()));
			}
		}
		log.info("结果长度：{}",res.size());
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
	 * 今日成交额(同花顺全A)
	 */
	public static String getTurnOver() {
		ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_TURNOVER, CrawConstant.ZHI_SHU,CommonConstant.ONE);
		JSONObject data = baseResolution(entity);
		String content = (String) data.get("text_answer");
		return content.split(CrawConstant.TURNOVER)[CommonConstant.ONE].replace(CrawConstant.PERIOD, "");
	}


}
