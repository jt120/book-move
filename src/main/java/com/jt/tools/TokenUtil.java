package com.jt.tools;

import com.google.common.base.Splitter;

import java.util.regex.Pattern;

/**
 * Created by he on 2016/3/14.
 */
public class TokenUtil {

    public static Pattern compile = Pattern.compile("[\\.\\s\\(\\)\\[\\]%\\-_\\+《》]");
    public static Splitter splitter = Splitter.on(compile).omitEmptyStrings().trimResults();
}
