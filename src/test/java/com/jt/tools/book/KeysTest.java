package com.jt.tools.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;

/**
 * since 2016/1/14.
 */
public class KeysTest {

    ObjectMapper objectMapper = new ObjectMapper();
    String userDir = System.getProperty("user.dir");
    String convertName = "convert.txt";
    String dest = "d:/test/books/";
    String source = "d:/books";
    String[] split = new String[]{".", " ", "(", ")", "[", "]", "%5B", "%5D", "%29", "%28"};


    @Test
    public void test012() throws Exception {
        System.out.println(URLDecoder.decode("%5B", "utf-8"));
        System.out.println(URLDecoder.decode("%5D", "utf-8"));
        System.out.println(URLDecoder.decode("%29", "utf-8"));
        System.out.println(URLDecoder.decode("%28", "utf-8"));

    }

    /**
     * 根据关键字文件, 生成目录
     */
    @Test
    public void test02() throws Exception {
        String s = FileUtils.readFileToString(new File(convertName));
        List<String> dirs = objectMapper.readValue(s, new TypeReference<List<String>>() {

        });
        for (String dir : dirs) {
            String tmp = dest + dir;
            File file = new File(tmp);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    @Test
    public void testa01() throws Exception {
        System.out.println(FileUtils.getUserDirectoryPath());
        System.out.println(userDir);
    }


    String s = "[Lucene.in.Action(2nd,2010.7)].Michael.McCandless.文字版.pdf";

    @Test
    public void test05() throws Exception {
        StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        TokenStream data = analyzer.tokenStream("data", new StringReader("[Lucene.in.Action(2nd," +
                "2010.7)].Michael.McCandless.文字版.pdf"));
        CharTermAttribute charTermAttribute = data.addAttribute(CharTermAttribute.class);

        while (data.incrementToken()) {
            System.out.println(new String(charTermAttribute.buffer()));
        }
    }

    @Test
    public void test06() throws Exception {
        List<Term> parse = ToAnalysis.parse(s);
        parse.forEach(s -> System.out.println(s.getName()));

    }

}
