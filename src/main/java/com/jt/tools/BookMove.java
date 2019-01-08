package com.jt.tools;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static Set<String> exts = Sets.newHashSet("epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar",
            "doc", "chm");

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

    public static void moveBook(String source, String dest, Set<String> exts, Set<String> keywords) throws Exception {
        Files.walk(Paths.get(source)).filter(p -> exts.contains(com.google.common.io.Files.getFileExtension(p
                .getFileName().toString())))
                .forEach(f -> {
                    log.info("process file {}", f);
                    moveDest(keywords, f, dest);
                });
    }

    private static String buildTargetPath(String root, String subject, String fileName) {
        return root + File.separator + subject + File.separator + fileName;
    }

    private static void moveDest(Set<String> keywords, Path sourceFile, String dest) {
        List<Term> parse = ToAnalysis.parse(com.google.common.io.Files.getNameWithoutExtension(sourceFile.getFileName()
                .toString()));
        boolean notRemove = true;
        for (Term term : parse) {
            final String convert = convert(term.getName());
            if (keywords.contains(convert)) {
                moveFile(sourceFile, buildTargetPath(dest, convert, sourceFile.getFileName().toString()));
                notRemove = false;
                break;
            }
        }

        if (notRemove) {
            moveFile(sourceFile, buildTargetPath(dest, "other", sourceFile.getFileName().toString()));
        }
    }

    private static void moveFile(Path sourceFile, String targetFile) {
        try {
            log.info("move file {} {}", sourceFile, targetFile);
            final Path target = Paths.get(targetFile);
            Files.createDirectories(target.getParent());
            Files.move(sourceFile, target);
        } catch (IOException e) {
            log.warn("move file fail", e);
        }
    }


    public static String convert(String orgName) {
        orgName = orgName.trim().toLowerCase().replace("5b", "");
        if (convertMap.containsKey(orgName)) {
            return convertMap.get(orgName);
        }
        return orgName;
    }

    public static void removeEmptyDir(String source) {
        final Path path = Paths.get(source);
        try {
            Files.walk(path).forEach(p -> {
                final File file = p.toFile();
                if (file.isDirectory() && file.list().length == 0) {
                    log.info("removeEmptyDir {}", file);
                    file.delete();
                }
            });
        } catch (IOException e) {
            log.warn("removeEmptyDir fail", e);
        }
    }

    public static Multiset<String> createKeywords(String rootPath, Set<String> exts) {
        Multiset<String> set = HashMultiset.create();
        try {
            Files.walk(Paths.get(rootPath)).filter(p -> exts.contains(com.google.common.io.Files.getFileExtension(p
                    .getFileName().toString())))
                    .forEach(f -> {
                        String fileName = com.google.common.io.Files.getNameWithoutExtension(f.getFileName().toString
                                ());
                        final String[] split = fileName.split("\\W+");
                        for (String s : split) {
                            if (s.matches("[a-zA-Z]{2,}")) {
                                set.add(s.toLowerCase());
                            }
                        }

                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return set;
    }

    public static void main(String[] args) throws Exception {

        //加载关键字
        Set<String> load = Keywords.load("keys.txt");
        //遍历文件, 针对匹配额关键字, 移动
        String sourceBookPath = "D:\\test";
        String destBookPath = "D:\\a\\2";
        Set<String> exts = Sets.newHashSet("epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar", "doc", "chm");
        //清楚空文件夹
        moveBook(sourceBookPath, destBookPath, exts, load);
        removeEmptyDir(sourceBookPath);
    }
}
