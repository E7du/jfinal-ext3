package com.jfinal.ext.render.excel.test;

import com.jfinal.ext.kit.excel.Reader;
import com.jfinal.ext.kit.excel.ReadRule;

import java.io.File;
import java.util.List;

public class TestPoiReader {
    public static void main(String[] args) {
        ReadRule readRule = new ReadRule();
        readRule.alignColumn("en_name", "cn_name");
        List<List<String>> list = Reader.read(new File("src/test/resources/data.xlsx"), readRule);
        System.out.println(list);
//        StringUtils.substringBetween()
    }
}
