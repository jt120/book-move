package com.jt.tools;

import com.google.common.base.Charsets;
import com.google.common.collect.Sets;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.Set;

/**
 * Created by he on 2016/3/14.
 */
public class Keywords {

    public static Set<String> load() {
        URL resource = Resources.getResource("keys.txt");
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
}
