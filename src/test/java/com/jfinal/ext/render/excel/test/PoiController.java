package com.jfinal.ext.render.excel.test;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jfinal.core.Controller;
import com.jfinal.ext.render.excel.PoiRender;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.auth.AccessTokenBuilder;

public class PoiController extends Controller {
    String[] columns = new String[] { "ACC_NBR", "DEVID", "IMSI" };
    String[] headers = new String[] { "电话号码", "设备id", "imsi", "最后上线时间" };
    String[] headers2 = new String[] { "电话号码", "设备id", "imsi" };

    public void index(){
        renderText(AccessTokenBuilder.getAccessToken(getRequest()));
    }
    public void columns() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = getMap(i);
            data.add(map);
        }
        render(PoiRender.me(data).fileName("your_file_name.xls").headers(headers2).sheetName("ok").columns(columns).cellWidth(5000).headerRow(2));
    }

    public void map() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Map<String, Object> map = getMap(i);
            data.add(map);
        }
        render(PoiRender.me(data).columns(columns).sheetName("ok").headers(headers));
    }

    public void record() {
        List<Object> data = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            Record record = new Record();
            Map<String, Object> map = getMap(i);
            record.setColumns(map);
            data.add(record);
        }
        render(PoiRender.me(data).headers(headers2));
    }

    private Map<String, Object> getMap(int i) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("ACC_NBR", "ACC_NBR" + i);
        map.put("IMSI", "IMSI" + i);
        map.put("DEVID", "DEVID中文" + i);
        map.put("LASTTIME", "LASTTIME" + i);
        return map;
    }
}
