package com.jt.tools;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.da.DanishAnalyzer;

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
    private static String sourceBookPath = "D:\\text\\books";
    private static String destBookPath = "d:/test/books/";
    private static String[] ext = {"epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar","doc","chm"};
    public static Pattern compile = Pattern.compile("[\\.\\s\\(\\)\\[\\]%\\-_\\+《》]");
    public static Splitter splitter = Splitter.on(compile).omitEmptyStrings().trimResults();

    public static Map<String, String> convertMap = Maps.newHashMap();

    static {
        convertMap.put("algorithms", "algorithm");
        convertMap.put("databases", "database");
        convertMap.put("patterns", "pattern");
        convertMap.put("systems", "system");
        convertMap.put("designing", "design");

    }

    public static List<String> readKeywords() throws Exception {
        URL resource = Resources.getResource(keywordsFile);
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
            boolean moved = false;
            for (String str : iter) {
                String subject = convert(str);
                if (keywords.contains(subject)) {
                    System.out.println("match file " + str);
                    try {
                        String pathname = destBookPath + subject;
                        if (moveFile(f, pathname)) break;
                    } catch (IOException e) {
                        System.out.println("error" + e);
                    }
                    moved = true;
                    break;
                }
            }
            if (!moved) {
                try {
                    moveFile(f, destBookPath+"other");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    private static boolean moveFile(File f, String pathname) throws IOException {
        File destDir = new File(pathname);
        File targetFile = new File(destDir, f.getName());
        if (targetFile.exists()) {
            return true;
        }
        FileUtils.moveFileToDirectory(f, destDir, true);
        System.out.println("move file to " + pathname);
        return false;
    }


    public static String convert(String orgName) {
        orgName = orgName.trim().toLowerCase().replace("5b", "");
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
