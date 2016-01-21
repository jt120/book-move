package com.jt.tools;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 1. 读取key.txt文件, 去重, 小写, 输出Set
 * 2. list目录下的文件
 * 3. 对文件名进行拆分, 并与Set匹配, 如果匹配上, 移动到对应目录
 * since 2016/1/15.
 */
public class BookMove {

    private static String keywordsFile = "keys.txt";
    private static String sourceBookPath = "d:/books";
    private static String destBookPath = "d:/text/books/";
    private static String[] ext = {"epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar"};
    public static Pattern compile = Pattern.compile("[\\.\\s\\(\\)\\[\\]%\\-_\\+《》]");
    public static Splitter splitter = Splitter.on(compile).omitEmptyStrings().trimResults();

    public static Map<String, String> convertMap = Maps.newHashMap();

    static {
        convertMap.put("algorithms", "algorithm");
        convertMap.put("databases", "database");
        convertMap.put("patterns", "pattern");
    }

    public static List<String> readKeywords() throws Exception {
        URL resource = BookMove.class.getClassLoader().getResource(keywordsFile);
        List<String> keywords = FileUtils.readLines(new File(resource.toURI()));
        Set<String> tmp = Sets.newHashSet(keywords.iterator());//去重
        List<String> result = Lists.newArrayList();
        tmp.forEach((s) -> {
            result.add(s.trim().toLowerCase());
        });
        Collections.sort(result);
        return result;
    }

    public static void moveBook() throws Exception {
        Collection<File> files = FileUtils.listFiles(new File(sourceBookPath), ext, true);
        List<String> keywords = readKeywords();
        System.out.println("keywords " + keywords);
        files.forEach((f) -> {
            System.out.println("process file " + f.getAbsolutePath());
            Iterable<String> iter = splitter.split(f.getName());
            for (String str : iter) {
                String houxuan = convert(str);
                if (keywords.contains(houxuan)) {
                    System.out.println("match file " + str);
                    try {
                        String pathname = destBookPath + houxuan;
                        FileUtils.moveFileToDirectory(f, new File(pathname), true);
                        System.out.println("copy file to " + pathname);
                    } catch (IOException e) {
                        System.out.println("error" + e);
                    }
                    break;
                }
            }

        });
    }


    public static String convert(String orgName) {
        orgName = orgName.trim().toLowerCase();
        orgName = orgName.replace("5b", "");
        if (convertMap.containsKey(orgName)) {
            return convertMap.get(orgName);
        }

        return orgName;
    }

    public static void removeEmptyDir() {
        File file = new File(sourceBookPath);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                String[] list = f.list();
                if (list.length == 0) {
                    System.out.println(f.getAbsolutePath() + " is kong");
                    f.delete();
                }

            }
        }
    }

    public static void main(String[] args) throws Exception {
        moveBook();
        removeEmptyDir();
    }
}
