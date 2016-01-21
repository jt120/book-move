package com.jt.tools.book;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.net.URL;

/**
 * since 2016/1/11.
 */
public class JsoupTest {

    @Test
    public void test01() throws Exception {
        String url = "http://search.jd.com/Search?keyword=OReilly&enc=utf-8&book=y&wq=OReilly";
        //Connection connect = Jsoup.connect(url);
        //Connection.Response execute = connect.execute();
        Document parse = Jsoup.parse(new URL(url), 5000);
        Elements elements = parse.select(".p-name em");
        for (Element element : elements) {
            System.out.println(element.html());
        }
    }

    @Test
    public void test02() throws Exception {
        String page2 = "http://search.jd.com/Search?keyword=OReilly&enc=utf-8&book=y&wq=OReilly#keyword=OReilly&enc" +
                "=utf-8&qrst=1&rt=1&stop=1&book=y&vt=2&page=3&click=0";
        String page3 = "http://search.jd.com/Search?keyword=OReilly&enc=utf-8&book=y&wq=OReilly#keyword=OReilly&enc" +
                "=utf-8&qrst=1&rt=1&stop=1&book=y&vt=2&page=5&click=0";
        String pageLast = "http://search.jd.com/Search?keyword=OReilly&enc=utf-8&book=y&wq=OReilly#keyword=OReilly" +
                "&enc=utf-8&qrst=1&rt=1&stop=1&book=y&vt=2&page=37&click=0";
    }

    @Test
    public void test03() throws Exception {
        Connection connect = Jsoup.connect("http://www.jb51.net/article/16829.htm");
        Connection.Response execute = connect.execute();
        String body = execute.body();
        System.out.println(body);
        Document parse = Jsoup.parse(body);
        Elements elements = parse.select("[src$=.gif]");
        for (Element element : elements) {
            System.out.println(element.attr("src"));
        }
    }
}
