package com.back.common.craw;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.back.entity.vo.StockPushVo;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 爬取股票信息
 */
@Slf4j
public class CrawUtil {
    public static void main(String[] args) {
        System.out.println(getOne("SH600546").toString());
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
        StockPushVo res = new StockPushVo(stockCode);
        log.info("开始爬取个股信息：{}",stockCode);
        try {
            document = Jsoup.connect(url).get();
            //获取主体，本质是个list
            Elements body = document.getElementsByTag("body");
            //getElementsByClass根据类选择器，getElementsByTag根据标签选择器
            //股票名称
            String stockName = body.first().getElementsByClass("stock-name").first().text();
            //现价
            String curr = body.first().getElementsByClass("stock-current").first().text();
            //涨跌
            String trend =body.first().getElementsByClass("stock-change").first().text().split(" ")[1];
            //成交额
            Elements elements = body.first().getElementsByClass("separateTop");
            String turnover = elements.first().getElementsByTag("td").get(3).text().split("：")[1];
            //tr,每一行的信息
            res.setStockName(stockName);
            res.setNowPrice(curr);
            res.setTrend(trend);
            res.setTurnover(turnover);
            res.setXueQiuLink(url);
            log.info("个股爬取结果：{}",res.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 爬取问财信息
     * @param question
     *        条件
     * @return
     */
    public String get(String question) {
        RestTemplate restTemplate = new RestTemplate();
        // 封装请求内容
        Map<String, Object> params = new HashMap<>();
        String url = "http://www.iwencai.com/customized/chart/get-robot-data";
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
        params.put("secondary_intent", "stock");
        params.put("source", "Ths_iwencai_Xuangu");
        params.put("version", "2.0");
        HttpHeaders headers = new HttpHeaders();
        String heXinV = JavaScriptProvider.getHeXinV();
        log.info("开始爬取复盘信息：条件：{},hexin-V:{}",question,heXinV);
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
        ResponseEntity<String> entity = restTemplate.postForEntity(url, httpEntity, String.class);
        // 取出核心信息
        JSONObject json = JSONObject.parseObject(entity.getBody());
        JSONObject data = (JSONObject) json.get("data");
        JSONArray answer = (JSONArray) data.get("answer");
        JSONObject zero = (JSONObject) answer.get(0);
        JSONArray txt = (JSONArray) zero.get("txt");
        JSONObject txtZero = (JSONObject) txt.get(0);
        JSONObject content = (JSONObject) txtZero.get("content");
        JSONArray components = (JSONArray) content.get("components");
        JSONObject componentsZero = (JSONObject) components.get(0);
        JSONObject componentsZeroData = (JSONObject) componentsZero.get("data");
        JSONArray datas = (JSONArray) componentsZeroData.get("datas");
        List<Map> list = datas.toJavaList(Map.class);
        System.out.println(list.get(0).toString());
        System.out.println(list.size());
        return datas.toString();
    }
}
