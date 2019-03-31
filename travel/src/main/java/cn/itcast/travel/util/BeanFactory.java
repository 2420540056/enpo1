package cn.itcast.travel.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;

/**
 * 生成javabean的一个工厂类
 */
public class BeanFactory {
    /**
     * 根据传入的id，动态生成id对应的实现类对象
     * @param id
     * @return
     */
    public static Object getBean(String id) {

        try {
            // 得到文件路径
            URL url = BeanFactory.class.getClassLoader().getResource("applicationContext.xml");
            String path = url.getPath();
            // 创建文件
            File file = new File(path);
            // 解析xml文件
            Document document = Jsoup.parse(file, "UTF-8");
            // 根据传入的id获取对应的bean标签
            Element beanEle = document.getElementById(id);
            // 获取bean标签上的class 属性值
            String className = beanEle.attr("class");
            // 加载类进内存
            Class<?> clazz = Class.forName(className);
            // 生成对象
            Object obj = clazz.newInstance();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;

    }

    public static void main(String[] args) {
        getBean("aaa");
    }
}
