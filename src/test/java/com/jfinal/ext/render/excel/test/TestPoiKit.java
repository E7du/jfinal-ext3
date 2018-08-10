package com.jfinal.ext.render.excel.test;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.excel.PoiWriter;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by kid on 14-12-4.
 */
public class TestPoiKit {
    public static void main(String[] args) throws IOException {
        List<Record> data = Lists.newArrayList();
        for (int i = 0; i < 65535+1; i++) {
            Record record = new Record();
            record.set("姓名", "朱丛启走起" + i);
            data.add(record);
        }
        Workbook workbook = PoiWriter.data(data).sheetNames("data")
                .columns(new String[]{"姓名"}).headers(new String[]{"姓名"}).write();
        String pathname = PathKit.getRootClassPath() + "/excel1.xls";
        System.out.println(pathname);
        workbook.write(new FileOutputStream(new File(pathname)));
    }
}
