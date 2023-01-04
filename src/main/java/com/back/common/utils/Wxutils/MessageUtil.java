package com.back.common.utils.Wxutils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 消息工具类，转换xml文件
 */
public class MessageUtil {

    /**
     * xml转换成map
     * @return
     */
    public static Map<String,String> xmlToMap(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        SAXReader reader = new SAXReader();
        InputStream in = null;
        try {
            in = request.getInputStream();
            Document doc = reader.read(in);
            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element element : list) {
                map.put(element.getName(),element.getText());
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (DocumentException e){
            e.printStackTrace();
        }finally {
            try {
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        return map;
    }
}
