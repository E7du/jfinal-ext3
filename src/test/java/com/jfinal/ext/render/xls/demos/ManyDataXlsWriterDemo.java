package com.jfinal.ext.render.xls.demos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.xls.XlsWriter;
import com.jfinal.plugin.activerecord.Record;

/**
 * Created by kid on 14-12-4.
 */
public class ManyDataXlsWriterDemo {
    public static void main(String[] args) throws IOException {
        List<Record> data = Lists.newArrayList();
        for (int i = 0; i < 65535+2; i++) {
            Record record = new Record();
            record.set("姓名", "朱丛启走起" + i);
            data.add(record);
        }
        Workbook workbook = XlsWriter.data(data).sheetNames("数据数据")
                .columns(new String[]{"姓名"}).headers(new String[]{"姓名"}).write();
        String pathname = "src/test/resources/many_data_excel.xls";
        System.out.println(pathname);
        workbook.write(new FileOutputStream(new File(pathname)));
    }
}
