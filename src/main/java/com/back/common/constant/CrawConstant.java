package com.back.common.constant;

public class CrawConstant {

    /** 上交所股票url*/
    public static String SH_URL = "http://www.sse.com.cn/js/common/ssesuggestdata.js?v=20231130";
    /** 深交所xlsx下载url*/
    public static String SZ_URL = "http://www.szse.cn/api/report/ShowReport?SHOWTYPE=xlsx&CATALOGID=1110&TABKEY=tab1&random=0.29676899312443084";
    /** 问财URL*/
    public static String WENCAI_URL = "http://www.iwencai.com/customized/chart/get-robot-data";
    /** 雪球个股链接 */
    public static String XUE_QIU_ONE = "https://xueqiu.com/S/";
    /** 新浪个股链接 */
    public static String SINA_ONE = "https://hq.sinajs.cn/list=";
    /** 新浪请求头refre */
    public static String HEADER_REFERER= "https://finance.sina.com.cn/realstock/company/$/nc.shtml";
    /** 淘股吧个股链接 */
    public static String TAO_GU_ONE = "https://www.taoguba.com.cn/quotes/";
    /** 同花顺个股链接 */
    public static String TONG_HU_ONE = "http://www.iwencai.com/unifiedwap/result&w=";
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
