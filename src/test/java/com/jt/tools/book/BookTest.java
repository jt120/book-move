package com.jt.tools.book;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * since 2016/1/8.
 */
public class BookTest {

    String[] subfix = {"pdf", "azw3", "epub", "mobi","rar","chm","txt","ppt","doc"};

    @Test
    public void test01() throws Exception {
        String s = "D:\\test";
        Collection<File> files = FileUtils.listFiles(new File(s), new String[]{"pdf"}, true);
        final Set<String> keywords = new HashSet<>();
        for (File file : files) {
            String baseName = FilenameUtils.getBaseName(file.getName());
            String[] split = baseName.split("[\\.\\s\\-_\\[\\],\\(\\):]");
        }

        System.out.println(keywords);
        FileUtils.write(new File("d:/test/keywords.txt"), keywords.toString(), false);
    }


    public Set<String> fileNames(String dir) {
        Collection<File> files = FileUtils.listFiles(new File(dir), new String[]{"pdf"}, true);
        Set<String> fileNames = new HashSet<>();
        for (File file : files) {
            String baseName = FilenameUtils.getBaseName(file.getName());
            fileNames.add(baseName);
        }
        return fileNames;
    }

    //用分词器
    @Test
    public void test02() throws Exception {
        Set<String> fileNames = fileNames("d:/test");
        Set<String> ret = new HashSet<>();
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        for (String fileName : fileNames) {
            ret.addAll(AnalyzerUtils.tokensFromAnalysis(analyzer, fileName));
        }
        System.out.println(ret);
    }

    @Test
    public void test022() throws Exception {
  Set<String> fileNames = fileNames("d:/test");
        Multiset<String> multiset = HashMultiset.create();
        for (String fileName : fileNames) {
            List<Term> parse = ToAnalysis.parse(fileName);
            for (Term term : parse) {
                multiset.add(term.getName());
            }
        }
        Set<String> print = Sets.newHashSet();
        for (String s : multiset) {
            if (print.contains(s)) {
                continue;
            }
            System.out.println(s + "\t" + multiset.count(s));
            print.add(s);

        }
    }

    @Test
    public void test23() throws Exception {
        long s = 1452391751880L;
        Date date = new Date(s);
        System.out.println(date);
    }

    @Test
    public void test03() throws Exception {
        String s = "Learning ROS for Robotics Programming" +
                " - Second Edition.pdf";
        List<Term> parse = ToAnalysis.parse(s);
        System.out.println(parse);
    }

    @Test
    public void test04() throws Exception {
        String str = "欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!";
        System.out.println(ToAnalysis.parse(str));
    }

    @Test
    public void testAn() throws Exception {
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_30);
        String s = "OReilly.Accumulo.2015.7.pdf";
        //TokenStream content = analyzer.tokenStream("content", new StringReader(s));
        Set<String> set = AnalyzerUtils.tokensFromAnalysis(analyzer, s);
        System.out.println(set);
    }

    static class AnalyzerUtils {
        public static Set<String> tokensFromAnalysis(Analyzer analyzer,
                                                     String text) throws IOException {
            TokenStream stream = //1
                    analyzer.tokenStream("contents", new StringReader(text)); //1
            Set<String> tokens = new HashSet<>();
            while (stream.incrementToken()) {
                CharTermAttribute charTermAttribute = stream.addAttribute(CharTermAttribute.class);
                tokens.add(charTermAttribute.toString());
            }
            return tokens;
        }
    }
}
