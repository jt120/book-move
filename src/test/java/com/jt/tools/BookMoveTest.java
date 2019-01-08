package com.jt.tools;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.google.common.collect.Sets;
import junit.framework.TestCase;
import org.ansj.app.keyword.KeyWordComputer;
import org.junit.Test;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BookMoveTest extends TestCase {

    public void testRemoveEmptyDir() throws Exception {
        BookMove.removeEmptyDir("D:\\a\\2");
    }

    @Test
    public void testKeys() throws Exception {

        final Multiset<String> keywords = BookMove.createKeywords("D:\\a\\2", BookMove.exts);
        List<Word> wordList = new ArrayList<>();
        for (String word : keywords.elementSet()) {
            wordList.add(new Word(word, keywords.count(word)));
        }

        Collections.sort(wordList);

        for (Word word : wordList) {
            System.out.println(word);
        }
    }

    static class Word implements Comparable<Word> {
        private String name;
        private int count;

        Word(String name, int count) {
            this.name = name;
            this.count = count;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public int compareTo(Word o) {
            return this.getCount() - o.getCount();
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(name).append(",").append(count);
            return sb.toString();
        }
    }

    @Test
    public void test02() throws Exception {
        String fileName = "OReilly.The.Hardware.Startup.2015.5.pdf";
        final String[] split = fileName.split("\\W+");
        for (String s : split) {
            System.out.println(s);
        }
    }


    @Test
    public void testkw() throws Exception {
        final Multiset<String> multiset = Keywords.loadMulti("keywords.txt");
        Multiset<String> ret = HashMultiset.create();
        for (String word : multiset.elementSet()) {
            for (String tmp : multiset.elementSet()) {
                if (Math.abs(word.length() - tmp.length()) == 1) {

                }
            }
        }

    }
}