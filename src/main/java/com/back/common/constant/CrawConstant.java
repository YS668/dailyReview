package com.back.common.constant;

public class CrawConstant {

    /** 上交所股票url*/
    public static String SH_URL = "http://www.sse.com.cn/js/common/ssesuggestdata.js?v=20231130";
    /** 深交所xlsx下载url*/
    public static String SZ_URL = "http://www.szse.cn/api/report/ShowReport?SHOWTYPE=xlsx&CATALOGID=1110&TABKEY=tab1&random=0.29676899312443084";
    /** 问财URL*/
    public static String WENCAI_URL = "http://www.iwencai.com/customized/chart/get-robot-data";
    /** 问财链接 */
    public static String WENCAI_LINK = "https://www.iwencai.com/unifiedwap/result?w=$&querytype=stock";
    /** 同花顺行业 */
    public static String TONGHUA_INDUSTRY = "http://q.10jqka.com.cn/thshy/detail/code/";
    /** 雪球个股链接 */
    public static String XUE_QIU_ONE = "https://xueqiu.com/S/";
    /** 新浪个股链接 */
    public static String SINA_ONE = "https://hq.sinajs.cn/list=";
    /** 新浪请求头refre */
    public static String HEADER_REFERER= "https://finance.sina.com.cn/realstock/company/$/nc.shtml";
    /** 淘股吧个股链接 */
    public static String TAO_GU_ONE = "https://www.taoguba.com.cn/quotes/";
    /** 同花顺个股链接 */
    public static String TONG_HU_ONE = "http://stockpage.10jqka.com.cn/";
    /** 东方财富个股链接 */
    public static String DONG_FANG_ONE ="https://so.eastmoney.com/web/s?keyword=";
    /** 上证指数 */
    public static String SH_INDEX_URL = "http://q.10jqka.com.cn/zs/detail/code/1A0001/";
    /** 深证成指*/
    public static String SZ_INDEX_URL = "http://q.10jqka.com.cn/zs/detail/code/399001/";
    /** 创业扳指 */
    public static String BUS_INDEX_URL = "http://q.10jqka.com.cn/zs/detail/code/399006/";
    /** 北向资金 */
    public static String NORTH_JME_URL = "https://data.eastmoney.com/hsgt/index.html";
    /** 同花顺龙虎榜 */
    public static String Long_Hu_URL= "http://data.10jqka.com.cn/market/longhu/";
    /** 文件路径 */
    public static String BASE_CD = System.getProperty("base.cd");
    /** 上交所js文件*/
    public static String SH_JS = BASE_CD+"sh.js";
    /** 深交所excel文件*/
    public static String SZ_EXCEL = BASE_CD+"SZStock.xlsx";
    /** hexin-v.js文件 */
    public static String HE_XIN_V = BASE_CD+"hexin-v.js";
    /** 深交所*/
    public static String SZ = "SZ";
    /** 上交所*/
    public static String SH = "SH";

    /*************************热股****************/
    /** 雪球1小时热榜 */
    public static String HOT_ONE_XUEQIU= "https://stock.xueqiu.com/v5/stock/hot_stock/list.json?size=10&_type=10&type=10";
    /** 雪球24小时热榜 */
    public static String HOT_TF_XUEQIU= "https://stock.xueqiu.com/v5/stock/hot_stock/list.json?size=10&_type=10&type=20";
    /** 淘股吧热榜 */
    public static String HOT_TAOGU= "https://www.taoguba.com.cn/stock/moreHotStock";
    /** 东方财富人气榜 */
    public static String HOT_DF_RENQI= "https://push2.eastmoney.com/api/qt/ulist.np/get?fltt=2&np=3&ut=a79f54e3d4c8d44e494efb8f748db291&invt=2&secids=1.601360,0.000977,1.601059,0.002547,0.002362,0.300114,0.002241,0.002882,0.300603,0.300317,0.300474,0.301358,1.601890,0.002400,0.002253,0.002717,0.002229,0.002328,0.301419,1.603019&fields=f1,f2,f3,f4,f12,f13,f14,f152,f15,f16";
    /** 东方财富飙升榜 */
    public static String HOT_DF_UP= "https://push2.eastmoney.com/api/qt/ulist.np/get?fltt=2&np=3&ut=a79f54e3d4c8d44e494efb8f748db291&invt=2&secids=1.688045,1.688661,0.300449,1.688368,1.688416,1.688699,1.688571,1.688173,1.688668,1.688383,1.688386,1.688391,1.603306,1.688150,1.688683,1.688070,1.688325,0.300940,0.300606,0.300818&fields=f1,f2,f3,f4,f12,f13,f14,f152,f15,f16";
    /** 同花顺1小时热榜 */
    public static String HOT_ONE_TH= "https://eq.10jqka.com.cn/open/api/hot_list/v1/hot_stock/a/hour/data.txt";
    /** 同花顺24小时热榜 */
    public static String HOT_TF_TH= "https://eq.10jqka.com.cn/open/api/hot_list/v1/hot_stock/a/day/data.txt";

    /*************************指数****************/
    /** 上证50 */
    public static String SZ_50_INDEX= "SH000016";
    /** 沪深300 */
    public static String HS_300_INDEX= "sh000300";
    /** 科创50 */
    public static String KC_50_INDEX= "SH000688";
    /** 中证500 */
    public static String ZZ_500_INDEX= "SH000905";
    /** 中证1000 */
    public static String ZZ_1000_INDEX= "SH000852";
    /** 国证2000 */
    public static String GZ_2000_INDEX= "SZ399303";
    /** 今日成交额从高到低 */
    public static String TURNOVER_SORT = "今日成交额从大到小排名前20";
    /** 今日行业板块涨幅从高到低 */
    public static String INDUSTRY_PLATE_UP_SORT = "今日行业板块涨幅从大到小前";
    /** 今日行业板块跌幅从低到高 */
    public static String INDUSTRY_PLATE_DOWN_SORT = "今日行业板块跌幅从小到大前";
    /** 今日概念板块涨幅从高到低 */
    public static String CONCEPT_PLATE_UP_SORT = "今日概念板块涨跌幅从大到小前";
    /** 今日概念板块跌幅从低到高 */
    public static String CONCEPT_PLATE_DOWN_SORT = "今日概念板块跌幅从小到大前";

    /*************************复盘数据条件*****************/
    public static String QUESTION_HISTORY_HIGH = "今日创历史新高，非ST，非4和8开头";
    public static String QUESTION_YEAR_HIGH = "今日创一年新高，非ST，非4和8开头";
    public static String QUESTION_YEAR_LOW = "今日创一年新低，非ST，非4和8开头";
    public static String QUESTION_DOWN_LIMIT = "今日跌停，非ST，非4和8开头";
    public static String QUESTION_DOWN_FIVE = "今日跌幅大于5%，非ST，非4和8开头";
    public static String QUESTION_UP_LIMIT = "今日涨停，非ST，非4和8开头";
    public static String QUESTION_NO_ONE_UP = "今日非一字涨停，非ST，非4和8开头";
    public static String QUESTION_UP_FIVE = "今日涨幅大于5%，非ST，非4和8开头";
    public static String QUESTION_UP_ALL = "上涨个股";
    public static String NINE_TWENTY_FIVE = "9点25分";
    public static String TEN = "10点";
    public static String ELEVEN = "11点";
    public static String THIRTEENT = "13点";
    public static String FOURTEEN = "14点";
    public static final String FOURTEENTHE = "14点30分";
    public static String QUESTION_DAY = "今天";
    public static String QUESTION_SZ_INDEX = "上证指数";
    public static String SZ_INDEX_code = "000001";
    public static String QUESTION_BUS_INDEX = "创业板指";
    public static String BUS_INDEX_CODE = "399006";
    public static String QUESTION_TURNOVER = "同花顺全A";
    public static String QUESTION_PLATE_FIVE = "今日板块涨幅前五";
    public static String QUESTION_HIGHEST = "市场最高标";


    public static String STOCK_NAME = "股票简称";
    public static String STOCK_CODE = "股票代码";
    public static String NOW_PRICE = "最新价";
    public static String NOW_TREND = "最新涨跌幅";
    public static String TURNOVER = "成交额";
    public static String PERIOD = "。";
    public static String STOCK = "stock";
    public static String ZHI_SHU = "zhishu";
    public static String YI = "亿";


    public static String ZHI_SHU_NAME = "指数简称";
    public static String ZHI_SHU_TREND = "指数@涨跌幅:前复权";
    public static String ZHI_SHU_TRUNOVER = "指数@成交额";


    /**涨停相关*/
    //a股市值(不含限售股)
    public static  String VALUE = "a股市值(不含限售股)";
    //几天几板[日期rdid]
    public static String DAY_NUM = "几天几板";
    //连续涨停天数[日期rdid]
    public static String DAY = "连续涨停天数";
    //涨停原因类别[日期rdid]
    public static String REASON = "涨停原因类别";
    //涨停封单量[日期rdid]
    public static String UP_NUM = "涨停封单量";
    //涨停封单额[日期rdid]
    public static String UP_VALUE = "涨停封单额";
    //涨停封单量占流通a股比
    public static String CIRCULATION_PERCENTAGE = "涨停封单量占流通a股比";
    //涨停封单量占成交量比
    public static String ALL_PERCENTAGE = "涨停封单量占成交量比";
    //涨停开板次数[日期rdid]
    public static String OPEN_NUM = "涨停开板次数";
    //涨停类型[日期rdid]
    public static String UP_TYPE = "涨停类型";
    //首次涨停时间[日期rdid]
    public static String FIRST_UP = "首次涨停时间";
    //最终涨停时间[日期rdid]
    public static String LAST_UP = "最终涨停时间";

    public static String REVIEW = "review";
    public static String NORTH = "north";
    public static String UP = "up";


}
