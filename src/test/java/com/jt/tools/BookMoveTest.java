package com.jt.tools;

import junit.framework.TestCase;

public class BookMoveTest extends TestCase {

    public void testRemoveEmptyDir() throws Exception {
        BookMove.removeEmptyDir("D:\\a\\2");
    }
}