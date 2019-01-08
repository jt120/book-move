package com.jt.tools;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Created by he on 2016/3/14.
 */
public class Keywords {

    public static Set<String> load(String name) {
        URL resource = Resources.getResource(name);
        final Set<String> set = Sets.newHashSet();
        try {
            Resources.readLines(resource, Charsets.UTF_8, new LineProcessor<Set<String>>() {
                @Override
                public boolean processLine(String line) throws IOException {
                    set.add(line.toLowerCase());
                    return true;
                }

                @Override
                public Set<String> getResult() {
                    return set;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static Multiset<String> loadMulti(String name) {
        URL resource = Resources.getResource(name);
        final Multiset<String> set = HashMultiset.create();
        try {
            Resources.readLines(resource, Charsets.UTF_8, new LineProcessor<Multiset<String>>() {
                @Override
                public boolean processLine(String line) throws IOException {
                    final String[] split = line.split(",");
                    set.add(split[0], Integer.parseInt(split[1]));
                    return true;
                }

                @Override
                public Multiset<String> getResult() {
                    return set;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void main(String[] args) throws Exception {
        Set<String> load = load("");
        ArrayList<String> list = Lists.newArrayList(load);
        Collections.sort(list);
        Joiner joiner = Joiner.on("\r\n");
        String join = joiner.join(list);
        String property = System.getProperty("user.dir") + "/src/main/resources";
        String normalize = FilenameUtils.normalize(property);

        File file = new File(normalize, "keys.txt");
        FileUtils.write(file, join, Charsets.UTF_8, false);

    }
}
