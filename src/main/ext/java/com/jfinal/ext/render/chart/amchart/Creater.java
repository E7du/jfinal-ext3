/**
 * Copyright (c) 2011-2013, kidzhou 周磊 (zhouleib1412@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jfinal.ext.render.chart.amchart;

import java.util.List;

import com.jfinal.ext.kit.KeyLabel;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Log;

public class Creater {

    public static boolean isFormat = true;
    protected static final Log LOG = Log.getLog(Creater.class);

    private Creater() {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String createMultipleChart(GraphChart chart) {
        StringBuffer strXML = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>").append(newLine())
                .append("<chart>").append(newLine());
        strXML.append(newLine());
        strXML = appendSeries(strXML, chart.getSeriesNames());
        List value = chart.getValues();
        if (value != null && value.size() > 0) {
            if (value.get(0) instanceof String) {
                strXML = appendSimpleGraphs(strXML, value);
            } else if (value.get(0) instanceof List) {
                strXML = appendMultipleGraphs(strXML, value);
            }
        }
        strXML.append(newLine()).append("</chart>");
        return strXML.toString();
    }

    public static String createPieChart(PieChart chart) {
        StringBuffer strXML = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>").append(newLine());
        strXML.append("<pie>").append(newLine());
        List<KeyLabel> pies = chart.getPies();
        for (KeyLabel pie : pies) {
            String label = pie.getLabel();
            if (StrKit.isBlank(label)) {
                label = "0";
            }
            strXML.append(space(1)).append("<slice title='").append(pie.getKey()).append("'>").append(pie.getLabel())
                    .append("</slice>").append(newLine());
        }
        strXML.append("</pie>");
        return strXML.toString();
    }

    private static StringBuffer appendMultipleGraphs(StringBuffer strXML, List<List<String>> values) {
        if (values == null) {
            return strXML;
        }
        strXML.append(space(1)).append("<graphs>");
        for (int i = 0, size = values.size(); i < size; i++) {
            strXML.append(newLine()).append(space(2)).append("<graph gid ='").append(i + 1).append("'>");
            List<String> value = values.get(i);
            for (int j = 0; j < value.size(); j++) {
                String val = value.get(j);
                if (StrKit.isBlank(val)) {
                    val = "0";
                }
                strXML.append(newLine()).append(space(3)).append("<value xid='").append(j).append("'>").append(val)
                        .append("</value>");
            }
            strXML.append(newLine()).append(space(2)).append("</graph>");
        }
        strXML.append(newLine()).append(space(1)).append("</graphs>");
        return strXML;
    }

    /**
     * 生成x轴描述
     * 
     * @param strXML
     * @param seriesNames
     *            描述的list
     * @return
     */
    private static StringBuffer appendSeries(StringBuffer strXML, List<String> seriesNames) {
        if (seriesNames == null) {
            return strXML;
        }
        strXML.append(space(1)).append("<series>").append(newLine());
        for (int i = 0, size = seriesNames.size(); i < size; i++) {
            String str = seriesNames.get(i);
            strXML.append(space(2)).append("<value xid='").append(i).append("'>").append(str).append("</value>")
                    .append(newLine());
        }
        strXML.append(space(1)).append("</series>").append(newLine());
        return strXML;
    }

    /**
     * 生成报表图形元素
     * 
     * @param strXML
     * @param list
     * @return
     */
    private static StringBuffer appendSimpleGraphs(StringBuffer strXML, List<String> list) {
        if (list == null) {
            return strXML;
        }
        strXML.append(space(1)).append("<graphs>");
        strXML.append(newLine()).append(space(2)).append("<graph gid = '1' ").append(">");
        for (int i = 0, size = list.size(); i < size; i++) {
            String value = list.get(i);
            if (StrKit.isBlank(value)) {
                value = "0";
            }
            strXML.append(newLine()).append(space(3)).append("<value xid='").append(i).append("'>").append(value)
                    .append("</value>");
        }
        strXML.append(newLine()).append(space(2)).append("</graph>");
        strXML.append(newLine()).append(space(1)).append("</graphs>");
        return strXML;
    }

    /**
     * 生成缩进
     * 
     * @param level
     *            缩进的级别（1级4个空格）
     * @return
     * 
     */
    private static StringBuffer space(int level) {
        StringBuffer sb = new StringBuffer();
        if (!isFormat) {
            return sb;
        }
        while (level-- > 0) {
            sb.append("    ");
        }
        return sb;
    }

    /**
     * 生成换行符 Description: <br>
     * 
     * @return
     */
    private static StringBuffer newLine() {
        StringBuffer sb = new StringBuffer();
        if (!isFormat) {
            return sb;
        }
        return sb;
    }

}
