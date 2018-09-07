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
package com.jfinal.ext.render.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.jfinal.ext.kit.excel.ExcelWriter;
import com.jfinal.ext.plugin.activerecord.ModelExt;
import com.jfinal.log.Log;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class ExcelRender extends Render {

    private final Log LOG = Log.getLog(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
    private List<? extends ModelExt<?>> data;
    private String fileName = "file1.xls";

    public ExcelRender(List<? extends ModelExt<?>> data) {
        this.data = data;
    }

    public static ExcelRender me(List<? extends ModelExt<?>> data) {
        return new ExcelRender(data);
    }

    @Override
    public void render() {
        response.reset();
        response.setHeader("Content-Disposition", "attachment;Filename=" + this.fileName);
        response.setContentType(CONTENT_TYPE);
        OutputStream os = null;
        try {
            os = response.getOutputStream();
            ExcelWriter writer = new ExcelWriter(os, ExcelTypeEnum.XLSX);
			writer.writeModel(this.data);
			writer.finish();
        } catch (Exception e) {
        	e.printStackTrace();
            LOG.error(e.getMessage(), e);
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
                LOG.error(e.getMessage(), e);
                throw new RenderException(e);
            }
        }
    }

    public ExcelRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
