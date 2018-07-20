package com.jfinal.ext.plugin.sqlinxml;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class TestSqlinxml {

    @Test
    public void test() throws InterruptedException {
        SqlInXmlPlugin plugin = new SqlInXmlPlugin();
        plugin.start();
        assertEquals("select * from blog", SqlInXmlKit.sql("blog.findBlog"));
        assertEquals("select * from user", SqlInXmlKit.sql("blog.findUser"));
    }

}
