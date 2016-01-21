package com.jt.tools.book;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.jt.tools.BookMove;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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


    /**
     * 生成关键字文件
     * @throws Exception
     */
    @Test
    public void test01() throws Exception {
        String file = "%5BZooKeeper%282013.11%29%5D.Flavio.Junqueira.文字版.pdf";
        List<String> strings = BookMove.splitter.splitToList(file);
        System.out.println(strings);
        for (String s : strings) {
            System.out.println(BookMove.convert(s));
        }
    }



    @Test
    public void test012() throws Exception {
        System.out.println(URLDecoder.decode("%5B", "utf-8"));
        System.out.println(URLDecoder.decode("%5D", "utf-8"));
        System.out.println(URLDecoder.decode("%29", "utf-8"));
        System.out.println(URLDecoder.decode("%28", "utf-8"));

    }

    /**
     * 根据关键字文件, 生成目录
     * @throws Exception
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

}
