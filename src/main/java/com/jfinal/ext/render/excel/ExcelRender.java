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

import com.jfinal.ext.kit.excel.Writer;
import com.jfinal.log.Log;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class ExcelRender extends Render {

    private final Log LOG = Log.getLog(getClass());
    private final static String CONTENT_TYPE = "application/msexcel;charset=" + getEncoding();
    private List<?>[] data;
    private String[][] headers;
    private String[] sheetNames = new String[]{"Sheet"};
    private int cellWidth;
    private String[] columns = new String[]{};
    private String fileName = "file1.xls";
    private int headerRow;

    public ExcelRender(List<?>[] data) {
        this.data = data;
    }

    public static ExcelRender me(List<?>... data) {
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
            Writer.data(data).sheetNames(sheetNames).headerRow(headerRow).headers(headers).columns(columns)
                    .cellWidth(cellWidth).write().write(os);
        } catch (Exception e) {
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

        }
    }

    public ExcelRender headers(String[]... headers) {
        this.headers = headers;
        return this;
    }

    public ExcelRender headerRow(int headerRow) {
        this.headerRow = headerRow;
        return this;
    }

    public ExcelRender columns(String... columns) {
        this.columns = columns;
        return this;
    }

    public ExcelRender sheetName(String... sheetName) {
        this.sheetNames = sheetName;
        return this;
    }

    public ExcelRender cellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
        return this;
    }

    public ExcelRender fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }
}
