package com.jt.tools;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
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
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * 1. 读取key.txt文件, 去重, 小写, 输出Set
 * 2. list目录下的文件
 * 3. 对文件名进行拆分, 并与Set匹配, 如果匹配上, 移动到对应目录
 * since 2016/1/15.
 */
public class BookMove {

    public static Map<String, String> convertMap = Maps.newHashMap();
    public static ImmutableMap<String, String> renameMap = ImmutableMap.of(
            "%5B", "[",
            "%28", "(",
            "%29", ")",
    "%5D", "]"
    );

    static {
        convertMap.put("algorithms", "algorithm");
        convertMap.put("databases", "database");
        convertMap.put("patterns", "pattern");
        convertMap.put("systems", "system");
        convertMap.put("designing", "design");
        convertMap.put("mvn", "maven");
        convertMap.put("networks", "network");
        convertMap.put("Testing", "test");

    }
    public static void moveBook(String source, String dest, String[] exts, Set<String> keywords) throws Exception {
        Collection<File> files = FileUtils.listFiles(new File(source), exts, true);
        System.out.println("keywords " + keywords);
        final String destPath = buildPath(dest);
        files.forEach((f) -> {
            System.out.println("process file " + f.getAbsolutePath());
            moveDest(keywords, f, destPath);
        });
    }

    private static String buildPath(String dest) {
        if (!(dest.endsWith("/") || dest.endsWith("\\"))) {
            dest = dest + "/";
        }
        return dest;
    }

    private static void moveDest(Set<String> keywords, File source, String dest) {
        List<Term> parse = ToAnalysis.parse(source.getName());
        final boolean[] flag = new boolean[1];
        parse.forEach(s -> {
            String subject = convert(s.getName());
            if (keywords.contains(subject)) {
                System.out.println("match file " + source);
                String pathname = dest + subject;
                moveFile(source, pathname);
                flag[0] = true;
            }
        });
        if (!flag[0]) {
            moveFile(source,dest+  "other");
        }
    }


    private static boolean moveFile(File f, String pathname) {
        File destDir = new File(pathname);
        String name = f.getName();
        for (Map.Entry<String, String> entry : renameMap.entrySet()) {
            name = name.replaceAll(entry.getKey(), entry.getValue());
        }
        File targetFile = new File(destDir, name);
        if (targetFile.exists()) {
            return false;
        }

        try {
            FileUtils.moveFile(f, targetFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        System.out.println("move file to " + pathname);
        return true;
    }


    public static String convert(String orgName) {
        orgName = orgName.trim().toLowerCase().replace("5b", "");
        if (convertMap.containsKey(orgName)) {
            return convertMap.get(orgName);
        }
        return orgName;
    }

    public static void removeEmptyDir(String source) {
        File file = new File(source);
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

        //加载关键字
        Set<String> load = Keywords.load();
        //遍历文件, 针对匹配额关键字, 移动
        String sourceBookPath = "D:\\test\\books";
        String destBookPath = "D:\\test\\books2\\";
        String[] ext = {"epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar", "doc", "chm"};
        //清楚空文件夹
        moveBook(sourceBookPath, destBookPath, ext, load);
        removeEmptyDir(sourceBookPath);
    }
}
