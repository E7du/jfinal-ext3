package com.jfinal.ext.plugin.sqlinxml;

import static org.junit.Assert.assertEquals;

public class TestSqlinxml {

//    @Test
    public void test() throws InterruptedException {
        SqlInXmlPlugin plugin = new SqlInXmlPlugin();
        plugin.start();
        assertEquals("select * from blog", SqlKit.sql("blog.findBlog"));
        assertEquals("select * from user", SqlKit.sql("blog.findUser"));
    }

}
