package com.jfinal.ext.render.xls.demos;

import java.io.File;
import java.util.List;

import com.jfinal.ext.kit.xls.XlsReadRule;
import com.jfinal.ext.kit.xls.XlsReader;

public class TestPoiReader {
    public static void main(String[] args) {
        XlsReadRule xlsReadRule = new XlsReadRule();
        xlsReadRule.alignColumn("en_name", "cn_name");
        List<List<Object>> list = XlsReader.read(new File("src/test/resources/data.xlsx"), xlsReadRule);
        System.out.println(list);
//        StringUtils.substringBetween()
    }
}
