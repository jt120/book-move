package com.jt.tools;

import com.google.common.collect.Maps;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 1. 读取key.txt文件, 去重, 小写, 输出Set
 * 2. list目录下的文件
 * 3. 对文件名进行拆分, 并与Set匹配, 如果匹配上, 移动到对应目录
 * since 2016/1/15.
 */
public class BookMove {

    private static final Logger log = LoggerFactory.getLogger(BookMove.class);

    public static Map<String, String> convertMap = Maps.newHashMap();

    static {
        convertMap.put("algorithms", "algorithm");
        convertMap.put("databases", "database");
        convertMap.put("patterns", "pattern");
        convertMap.put("systems", "system");
        convertMap.put("designing", "design");
        convertMap.put("mvn", "maven");
        convertMap.put("networks", "network");
        convertMap.put("Testing", "test");
        convertMap.put("nodejs", "node");
        convertMap.put("clojurescript", "clojure");
        convertMap.put("cocos2d", "cocos");
        convertMap.put("database", "db");

    }

    public static void moveBook(String source, String dest, String[] exts, Set<String> keywords) throws Exception {
        Collection<File> files = FileUtils.listFiles(new File(source), exts, true);
        System.out.println("keywords " + keywords);
        final String destPath = buildPath(dest);
        files.forEach((f) -> {
            System.out.println("process file " + f.getAbsolutePath());
            Book book = new Book(f);
            moveDest(keywords, book, destPath);
        });
    }

    private static String buildPath(String dest) {
        if (!(dest.endsWith("/") || dest.endsWith("\\"))) {
            dest = dest + "/";
        }
        return dest;
    }

    private static void moveDest(Set<String> keywords, Book book, String dest) {
        List<Term> parse = ToAnalysis.parse(book.getFile().getName());

        parse.forEach(s -> {
            String subject = convert(s.getName());
            //String subject = s.getName();
            if (keywords.contains(subject)) {
                book.addKeyword(subject);

            }
        });
        book.move(dest + book.getFirstKeyword());
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
        String sourceBookPath = "D:\\test\\book\\";
        String destBookPath = "D:\\test\\book2\\";
        String[] ext = {"epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar", "doc", "chm"};
        //清楚空文件夹
        moveBook(sourceBookPath, destBookPath, ext, load);
        removeEmptyDir(sourceBookPath);
    }
}
