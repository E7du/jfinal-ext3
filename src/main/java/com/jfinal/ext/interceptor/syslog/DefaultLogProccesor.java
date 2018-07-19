/*
 * Copyright 2018 Jobsz (zcq@zhucongqi.cn)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.jfinal.ext.interceptor.syslog;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;
import java.util.Set;

public class DefaultLogProccesor implements LogProcessor {
	
    @Override
    public void process(SysLog sysLog) {
        Map map = null;
        try {
            map = BeanUtils.describe(sysLog);
            map.remove("class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Record record = new Record();
        record.setColumns(map);
        System.out.println(record);
//        Db.save("syslog", record);
    }

    @Override
    public String getUsername(Controller c) {
        return c.getSessionAttr("username");
    }

    @Override
    public String formatMessage(String title, Map<String, String> message) {
        String result = title;
        if (message.isEmpty()) {
            return result;
        }
        result += ", ";
        Set<Map.Entry<String, String>> entrySet = message.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            String key = entry.getKey();
            String value = entry.getValue();
            result += key + ":" + value;
        }
        return result;
    }

}
