package com.jfinal.ext.plugin.mongodb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.plugin.monogodb.Mongodb;
import com.jfinal.ext.plugin.monogodb.MongodbPlugin;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

public class TestMongodb {
	
    private static MongodbPlugin mongodbPlugin = new MongodbPlugin("log");

    @BeforeClass
    public static void init() {
        mongodbPlugin.start();
    }

    @Test
    public void page() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("age", "1");
        Map<String, Object> like = new HashMap<String, Object>();
        Map<String, Object> sort = new HashMap<String, Object>();
        Page<Record> page = Mongodb.paginate("sns", 1, 10, filter, like, sort);
        System.out.println(page.getList());
    }

    @Test
    public void testDelete() {
        Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("name", "bb");
        filter.put("age", "1");
        System.out.println(Mongodb.remove("sns", filter));
    }

    @Test
    public void testDeleteAll() {
        System.out.println(Mongodb.removeAll("sns"));
    }

    @Test
    public void testSave() {
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.set("name", "aa");
        records.add(record);
        Record record2 = new Record();
        record2.set("name", "bb");
        record2.set("age", "1");
        records.add(record2);
        System.out.println(Mongodb.save("sns", records));
    }

    @Test
    public void testUpdate() {
        Mongodb.removeAll("sns");
        List<Record> records = new ArrayList<Record>();
        Record record = new Record();
        record.set("name", "aa");
        records.add(record);
        Record record2 = new Record();
        record2.set("name", "bb");
        record2.set("age", "1");
        records.add(record2);
        Record record3 = new Record();
        record3.set("name", "qbb");
        record3.set("age", "1");
        records.add(record3);
        Mongodb.save("sns", records);
        Map<String, Object> src = new HashMap<String, Object>();
        src.put("age", "1");
        Map<String, Object> desc = new HashMap<String, Object>();
        desc.put("addr", "test");
        Mongodb.updateFirst("sns", src, desc);
        System.out.println(Mongodb.paginate("sns", 1, 10).getList());
    }
}
