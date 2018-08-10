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
package com.jfinal.ext.interceptor.excel;

import java.util.List;

import com.google.common.collect.Lists;
import com.jfinal.aop.Invocation;
import com.jfinal.aop.PrototypeInterceptor;
import com.jfinal.core.Controller;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext.kit.excel.PoiReader;
import com.jfinal.ext.kit.excel.RowFilter;
import com.jfinal.ext.kit.excel.Rule;
import com.jfinal.plugin.activerecord.Model;

public abstract class ExcelUploadInterceptor<M extends Model<?>> extends PrototypeInterceptor {

    private Rule rule;

    public abstract Rule configRule();

    public abstract void callback(Model<?> model);

    public void doIntercept(Invocation ai) {
        rule = configRule();
        Controller controller = ai.getController();
        List<Model<?>> list = PoiReader.processSheet(controller.getFile().getFile(), rule);
        execPreListProcessor(list);
        for (Model<?> model : list) {
            execPreExcelProcessor(model);
            callback(model);
            execPostExcelProcessor(model);
        }
        execPostListProcessor(list);
        ai.invoke();
    }

    private void execPreListProcessor(List<Model<?>> list) {
        PreListProcessor preListProcessor = rule.getPreListProcessor();
        if (null != preListProcessor) {
        	preListProcessor.process(list);
        }
    }

    private void execPostListProcessor(List<Model<?>> list) {
    	PostListProcessor postListProcessor = rule.getPostListProcessor();
        if (null != postListProcessor) {
        	postListProcessor.process(list);
        }
    }

    private void execPreExcelProcessor(Model<?> model) {
    	PreExcelProcessor preExcelProcessor = rule.getPreExcelProcessor();
        if (null != preExcelProcessor) {
            preExcelProcessor.process(model);
        }
    }

    private void execPostExcelProcessor(Model<?> model) {
    	PostExcelProcessor postExcelProcessor = rule.getPostExcelProcessor();
        if (null != postExcelProcessor) {
            postExcelProcessor.process(model);
        }
    }
    
    @SuppressWarnings("unused")
	private List<RowFilter> getRowFilterList(String rowFilter) {
        List<RowFilter> rowFilterList = Lists.newArrayList();
        String[] rowFilters = rowFilter.split(",");
        if (rowFilters == null)
            return rowFilterList;
        for (String filter : rowFilters) {
            rowFilterList.add((RowFilter) Reflect.on(filter).create().get());
        }
        return rowFilterList;
    }

}
