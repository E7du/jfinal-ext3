package com.jfinal.ext.kit;

import com.jfinal.ext.kit.excel.PoiReader;
import com.jfinal.ext.kit.excel.Rule;

import java.io.File;
import java.util.List;

public class TestPoiImporter {
    public static void main(String[] args) {
        Rule rule = new Rule();
        rule.addCell(0,"en_name");
        rule.addCell(1,"cn_name");
        List<List<String>> list = PoiReader.readSheet(new File("src/test/resources/data.xlsx"), rule);
        System.out.println(list);
//        StringUtils.substringBetween()
    }
}
