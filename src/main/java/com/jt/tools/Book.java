package com.jt.tools;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * since 2016/3/25.
 */
public class Book {

    public static final Logger log = LoggerFactory.getLogger(Book.class);

    private File file;
    private Set<String> keywords = Sets.newHashSet();

    public static ImmutableMap<String, String> renameMap = ImmutableMap.of(
            "%5B", "[",
            "%28", "(",
            "%29", ")",
            "%5D", "]"
    );


    public Book(File file) {
        this.file = file;
    }

    public void addKeyword(String keyword) {
        this.keywords.add(keyword);
    }

    public String getFirstKeyword() {
        if (this.keywords.isEmpty()) {
            return "other";
        } else {
            return this.keywords.iterator().next();
        }
    }

    public File getFile() {
        return file;
    }

    public boolean move(String dest) {
        File destDir = new File(dest);
        String name = getFile().getName();
        for (Map.Entry<String, String> entry : renameMap.entrySet()) {
            name = name.replaceAll(entry.getKey(), entry.getValue());
        }
        File targetFile = new File(destDir, name);
        if (targetFile.exists()) {
            return false;
        }

        try {
            FileUtils.moveFile(getFile(), targetFile);
        } catch (IOException e) {
            log.warn("move fail", e);
            return false;
        }
        log.info("move file {} to {}", getFile().getAbsolutePath(), targetFile.getAbsolutePath());
        return true;
    }
}
