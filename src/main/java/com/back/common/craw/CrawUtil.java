package com.back.common.craw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.back.common.constant.CommonConstant;
import com.back.common.constant.CrawConstant;
import com.back.entity.vo.StockPushVo;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 爬取股票信息工具类
 */
@Component
@Slf4j
public class CrawUtil {

    /** <股票代码,股票信息>*/
    public static Map<String, StockPushVo> StockCodeMap = new HashMap<>();
    /** <股票名称,股票信息>*/
    public static Map<String, StockPushVo> StockNameMap = new HashMap<>();
    private static final JavaScriptProvider<ShJS> shPr = new JavaScriptProvider<>();

    /**
     * 初始化股票map缓存
     */
    @PostConstruct
    public void initMap(){
        crawShData();
        getShData().forEach(e -> {
            StockCodeMap.put(e.getStockCode(),e);
            //去除空格
            StockNameMap.put(e.getStockName().replace(" ",""),e);
        });
        getSzData().forEach(e -> {
            StockCodeMap.put(e.getStockCode(),e);
            //去除空格
            StockNameMap.put(e.getStockName().replace(" ",""),e);
        });
        log.info("初始股票数据缓存成功");
    }

    /**
     * 爬取上交所股票，保存为js脚本
     */
    public void crawShData() {
        log.info("开始爬取上交所股票");
        RestTemplate restTemplate = new RestTemplate();
        // 处理乱码问题
        restTemplate.getMessageConverters().set(CommonConstant.ONE, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<String> entity = restTemplate.getForEntity(CrawConstant.SH_URL, String.class);
        String res = entity.getBody();
        try {
            FileOutputStream fos = new FileOutputStream(CrawConstant.SH_JS);
            fos.write(res.getBytes());
            log.info("爬取上交所股票结束");
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行js脚本获取上交所股票
     * @return
     */
    public static List<StockPushVo> getShData() {
        log.info("开始执行上交所股票js文件");
        List<StockPushVo> data =null;
        try {
            JavaScriptProvider jsProvider = new JavaScriptProvider();
            ShJS shJS = shPr.loadJs(CrawConstant.SH_JS,ShJS.class);
            //转换数据类型并过滤
            data = shJS.get_data().stream().map(StockPushVo::valueofSh).filter(vo->vo.getStockCode().startsWith("SH6")).collect(Collectors.toList());
            log.info("执行上交所股票js文件结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }finally {
            return data;
        }
    }

    /**
     * 从excel获取深交所股票，poi
     * @return
     */
    public static List<StockPushVo> getSzData() {
        List<StockPushVo> list = new ArrayList<>();
        try {
            log.info("开始操作深交所股票excel文件");
            //利用poi操作excel.xlsx文件
            InputStream inputStream = new FileInputStream(new File(CrawConstant.SZ_EXCEL));
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(CommonConstant.ZERO);
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i < lastRow; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row != null){
                    StockPushVo vo = new StockPushVo();
                    vo.setStockCode(CrawConstant.SZ+row.getCell(CommonConstant.FOUR).getStringCellValue());
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
     * 爬取雪球个股信息
     * @param stockCode
     *        股票代码
     * @return
     */
    public static StockPushVo getOne(String stockCode) {
        //例如：https://xueqiu.com/S/SH600546
        String url = "https://xueqiu.com/S/"+stockCode;
        Document document = null;
        List<String> list = new ArrayList<>();
        StockPushVo vo = null;
        log.info("开始爬取个股信息：股票编码{}",stockCode);
        try {
            document = Jsoup.connect(url).get();
            //获取主体，本质是个list
            Elements body = document.getElementsByTag("body");
            //getElementsByClass根据类选择器，getElementsByTag根据标签选择器
            //股票名称
            String stockName = body.first().getElementsByClass("stock-name").first().text();
            //现价
            String nowPrice = body.first().getElementsByClass("stock-current").first().text();
            //涨跌
            String trend =body.first().getElementsByClass("stock-change").first()
                    .text().split(" ")[CommonConstant.ONE];
            //成交额
            Elements elements = body.first().getElementsByClass("separateTop");
            String turnover = elements.first().getElementsByTag("td")
                    .get(CommonConstant.THREE).text().split("：")[CommonConstant.ONE];
            //填充信息
            vo = StockCodeMap.get(stockCode);
            vo.setStockName(stockName);
            vo.setNowPrice(nowPrice);
            vo.setTrend(trend);
            vo.setTurnover(turnover);
            vo.setXueQiuLink(url);
            log.info("个股爬取结果：{}",vo.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vo;
    }

    /**
     * 爬取问财信息
     * @param question
     *        条件
     * @return
     */
    public static ResponseEntity<String> getWenCai(String question,String secondary_intent) {
        RestTemplate restTemplate = new RestTemplate();
        log.info("开始爬取问财信息，条件：{}",question);
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
        params.put("page", 1);
        params.put("perpage", 200);
        params.put("query_area", "");
        params.put("question", question);
        params.put("rsh", "Ths_iwencai_Xuangu_z2chzlngxpcdgidg71ddgqenmcv2x4jy");
        params.put("secondary_intent", secondary_intent);
        params.put("source", "Ths_iwencai_Xuangu");
        params.put("version", "2.0");
        HttpHeaders headers = new HttpHeaders();
        String heXinV = JavaScriptProvider.getHeXinV();
        log.info("本次问财hexin-V:{}",heXinV);
        headers.set("hexin-v", heXinV);
        headers.set("Cookie", "other_uid=Ths_iwencai_Xuangu_z2chzlngxpcdgidg71ddgqenmcv2x4jy; ta_random_userid=hxo4x7vhm6; cid=2078830176e1a4273353b8af5f0cef731673344264; PHPSESSID=01611bc000b9c08efa02f2adf7e42cde; cid=2078830176e1a4273353b8af5f0cef731673344264; ComputerID=2078830176e1a4273353b8af5f0cef731673344264; WafStatus=0; v=" + heXinV);
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
     * 解析指数涨跌值
     * @param entity
     * @return
     */
    public static JSONObject ResolutionIndex(ResponseEntity<String> entity){
        JSONObject json = JSONObject.parseObject(entity.getBody());
        log.info("爬取结果：{}",json.toString());
        JSONObject data = (JSONObject) json.get("data");
        JSONObject parser_data = (JSONObject) data.get("parser_data");
        JSONObject market_data = (JSONObject) parser_data.get("market_data");
        return market_data;
    }

    /**
     * 基础解析
     * @param entity
     * @return
     */
    public static JSONObject baseResolution(ResponseEntity<String> entity){
        JSONObject json = JSONObject.parseObject(entity.getBody());
        log.info("爬取结果：{}",json.toString());
        JSONObject data = (JSONObject) json.get("data");
        JSONArray answer = (JSONArray) data.get("answer");
        JSONObject answerZero = (JSONObject) answer.get(0);
        return answerZero;
    }

    /**
     * 解析出个股
     * @param entity
     * @return
     */
    public static List<StockPushVo> resolution(ResponseEntity<String> entity){
        JSONObject answerZero = baseResolution(entity);
        JSONArray txt = (JSONArray) answerZero.get("txt");
        JSONObject txtZero = (JSONObject) txt.get(0);
        JSONObject content = (JSONObject) txtZero.get("content");
        JSONArray components = (JSONArray) content.get("components");
        JSONObject componentsZero = (JSONObject) components.get(0);
        JSONObject componentsZeroData = (JSONObject) componentsZero.get("data");
        JSONArray datas = (JSONArray) componentsZeroData.get("datas");
        List<Map> map = datas.toJavaList(Map.class);
        List<StockPushVo> res = map.stream().map(StockPushVo::reviewData).collect(Collectors.toList());
        return res;
    }

    /**
     * 解析出数量
     * @param entity
     * @return
     */
    public static int resolutionNum(ResponseEntity<String> entity){
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
     * 今日创历史新高
     */
    public static List<StockPushVo> getHistoryHigh(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_HISTORY_HIGH,CrawConstant.STOCK);
        return resolution(entity);
    }

    /**
     * 今日创一年新高
     */
    public static List<StockPushVo> getYearHigh(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_YEAR_HIGH,CrawConstant.STOCK);
        return resolution(entity);
    }

    /**
     * 今日创一年新低
     */
    public static List<StockPushVo> getYearLow(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_YEAR_LOW,CrawConstant.STOCK);
        return resolution(entity);

    }

    /**
     * 今日跌停
     */
    public static List<StockPushVo> getDownLimit(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_DOWN_LIMIT,CrawConstant.STOCK);
        return resolution(entity);
    }

    /**
     * 今日跌幅超5%
     */
    public static Integer getDownFive(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_DOWN_FIVE,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 今日涨停
     */
    public static List<StockPushVo> getUpLimit(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_UP_LIMIT,CrawConstant.STOCK);
        return resolution(entity);
    }

    /**
     * 今日非一字涨停
     */
    public static List<StockPushVo> getNoOneUp(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_NO_ONE_UP,CrawConstant.STOCK);
        return resolution(entity);
    }

    /**
     * 今日涨幅大于5%
     */
    public static Integer getUpFive(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_UP_FIVE,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 09:25上涨家数
     */
    public static int getUpAllToNineTwentyFive(){
        String question = CrawConstant.QUESTION_DAY+CrawConstant.NINE_TWENTY_FIVE+CrawConstant.QUESTION_UP_ALL;
        ResponseEntity<String> entity = getWenCai(question,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 10:00上涨家数
     */
    public static int getUpAllToTen(){
        String question = CrawConstant.QUESTION_DAY+CrawConstant.TEN+CrawConstant.QUESTION_UP_ALL;
        ResponseEntity<String> entity = getWenCai(question,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 11:30上涨家数
     */
    public static int getUpAllToElevenThirty(){
        String question = CrawConstant.QUESTION_DAY+CrawConstant.ELEVEN_THIRTY+CrawConstant.QUESTION_UP_ALL;
        ResponseEntity<String> entity = getWenCai(question,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 14:00上涨家数
     */
    public static int getUpAllToFourteen(){
        String question = CrawConstant.QUESTION_DAY+CrawConstant.FOURTEEN+CrawConstant.QUESTION_UP_ALL;
        ResponseEntity<String> entity = getWenCai(question,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 今日上涨家数(15点)
     */
    public static int getUpAllToDay(){
        String question = CrawConstant.QUESTION_DAY+CrawConstant.QUESTION_UP_ALL;
        ResponseEntity<String> entity = getWenCai(question,CrawConstant.STOCK);
        return resolutionNum(entity);
    }

    /**
     * 今日上证指数涨跌
     */
    public static String getSzIndex(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_SZ_INDEX,CrawConstant.ZHI_SHU);
        JSONObject market_data = ResolutionIndex(entity);
        JSONObject o = (JSONObject) market_data.get(CrawConstant.SZ_INDEX_code);
        return String.valueOf(o.get("rise_fall_rate"));
    }

    /**
     * 今日创业扳指涨跌
     */
    public static String getBusIndex(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_BUS_INDEX,CrawConstant.ZHI_SHU);
        JSONObject market_data = ResolutionIndex(entity);
        JSONObject o = (JSONObject) market_data.get(CrawConstant.BUS_INDEX_CODE);
        return String.valueOf(o.get("rise_fall_rate"));
    }

    /**
     * 今日成交额
     */
    public static String getTurnOver(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_TURNOVER,CrawConstant.ZHI_SHU);
        JSONObject data = baseResolution(entity);
        String content = (String) data.get("text_answer");
        return content.split(CrawConstant.TURNOVER)[CommonConstant.ONE].replace(CrawConstant.PERIOD,"");
    }

    /**
     * 今日板块涨幅前五
     */
    public static void getPlateFive(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_PLATE_FIVE,CrawConstant.ZHI_SHU);
        // 取出核心信息
        JSONObject json = JSONObject.parseObject(entity.getBody());
        JSONObject data = (JSONObject) json.get("data");
    }

    /**
     * 今日市场最高标
     */
    public static void getHighest(){
        ResponseEntity<String> entity = getWenCai(CrawConstant.QUESTION_HIGHEST,CrawConstant.STOCK);
        // 取出核心信息
        JSONObject json = JSONObject.parseObject(entity.getBody());
        JSONObject data = (JSONObject) json.get("data");

    }

}
