package com.back.common.craw;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class crawYearHigher {

    private static String url = "https://www.iwencai.com/unifiedwap/result?w=一年新高&querytype=stock";
    private static String head = "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36";
    public static void main(String[] args) throws IOException {
            getTwo();
    }

    public static void getOne() throws IOException  {
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select("hoverClass");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            String string = element.toString();
            System.out.println(string);
        }
    }

    public static void getTwo() throws IOException {
        Document document = Jsoup.connect(url).get();
        Element body = document.body();
        String string1 = body.toString();
        System.out.println(string1);
        Elements elements = document.select("iwc-table-body scroll-style2 big-mark");
        for (int i = 0; i < elements.size(); i++) {
            //一行
            Elements tr = elements.get(i).select("table > tbody > tr");
            for (int j = 0; j < tr.size(); j++){
                //一行的元素
                Elements td = tr.select("td -> div");
                for (int k = 0; k < td.size(); k++) {
                    String string = td.toString();
                    System.out.println(string);
                }
            }

        }
    }
}
