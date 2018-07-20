package com.jfinal.ext.render.csv;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.core.Controller;

public class CSVController extends Controller {
    public void index() {
        String[] str1 = { "a1", "b1", "c1", "d1" };
        String[] str2 = { "a2", "b2", "c2", "d2" };
        String[] str3 = { "a3", "b3", "c3", "d3" };
        List<String[]> data = Lists.newArrayList();
        data.add(str1);
        data.add(str2);
        data.add(str3);
        List<String> header = Lists.newArrayList();
        header.add("列数1");
        header.add("列数2");
        header.add("列数3");
        List<String> columns = Lists.newArrayList();
        columns.add("id");
        columns.add("title");
        // List<Blog> blogs=Blog.dao.find("select * from blog");
        // List<Record> records=Db.find("select * from blog");
        render(CsvRender.me(header, data).fileName("csvTest.csv").clomuns(columns));
    }
}
