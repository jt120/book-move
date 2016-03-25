package com.jt.tools;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * since 2016/2/1.
 */
public class BookRename {
 public static ImmutableMap<String, String> renameMap = ImmutableMap.of(
            "%5B", "[",
            "%28", "(",
            "%29", ")",
    "%5D", "]"
    );
    private static String sourceBookPath = "D:\\test\\books";
    private static String[] ext = {"epub", "mobi", "pdf", "pptx", "ppt", "azw3", "zip", "rar","doc","chm"};
    public static Pattern compile = Pattern.compile("[\\.\\s\\(\\)\\[\\]%\\-_\\+《》]");
    public static void main(String[] args) throws Exception {
         Collection<File> files = FileUtils.listFiles(new File(sourceBookPath), ext, true);
        for (File file : files) {
            String name = file.getName();
            for (Map.Entry<String, String> entry : renameMap.entrySet()) {
                name = name.replaceAll(entry.getKey(), entry.getValue());
            }
            file.renameTo(new File(file.getParent(), name));
        }
    }
}
