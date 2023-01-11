package com.back.common.craw;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * 执行js脚本，获取hexin-V
 */
public class JavaScriptProvider {

    private static JSMethods loadJs(Class<JSMethods> clazz) throws FileNotFoundException, ScriptException {
        //创建一个脚本引擎管理器
        ScriptEngineManager manager = new ScriptEngineManager();
        //获取一个指定的名称的脚本管理器
        ScriptEngine engine = manager.getEngineByName("js");
        //获取js文件所在的目录的路径
        String jsName = Thread.currentThread().getClass().getResource("/").getPath() + "/hexin-v.js";
        engine.eval(new FileReader(jsName));
        //从脚本引擎中返回一个给定接口的实现
        Invocable invocable = (Invocable) engine;
        return invocable.getInterface(clazz);
    }

    public static String getHeXinV() {
        String str =null;
        try {
            JavaScriptProvider jsProvider = new JavaScriptProvider();
            JSMethods jsMethods = jsProvider.loadJs(JSMethods.class);
            str = jsMethods.v();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ScriptException e) {
            e.printStackTrace();
        }finally {
            return str;
        }
    }

}