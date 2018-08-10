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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.ss.usermodel.Workbook;

import com.google.common.collect.Maps;
import com.jfinal.log.Log;
import com.jfinal.render.Render;
import com.jfinal.render.RenderException;

public class JxlsRender extends Render {

    private final Log LOG = Log.getLog(getClass());
    
    private static final String CONTENT_TYPE = "application/vnd.ms-excel;charset=" + getEncoding();
    public static JxlsRender me(String templetFile) {
        return new JxlsRender(templetFile);
    }
    private Map<String, Object> beans = Maps.newHashMap();
    private String filename = "file1.xls";

    private String templetFile;

    public JxlsRender(String templetFile) {
        this.templetFile = templetFile;
    }

    public JxlsRender beans(Map<String, Object> beans) {
        this.beans = beans;
        return this;
    }

    private void buildBean() {
        Enumeration<String> attrs = request.getAttributeNames();
        while (attrs.hasMoreElements()) {
            String key = attrs.nextElement();
            Object value = request.getAttribute(key);
            beans.put(key, value);
        }
    }

    public JxlsRender filename(String filename) {
        this.filename = filename;
        return this;
    }

    @Override
    public void render() {
        if (beans.isEmpty()) {
            buildBean();
        }
        response.setContentType(CONTENT_TYPE);
        response.setHeader("Content-Disposition", "attachment;Filename=" + filename);
        OutputStream os = null;
        InputStream is = null;
        try {
        	os = response.getOutputStream();
            is = new BufferedInputStream(new FileInputStream(templetFile));
            XLSTransformer transformer = new XLSTransformer();
            Workbook workBook = transformer.transformXLS(is, beans);
            workBook.write(os);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RenderException(e);
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (is != null) {
                	is.close();
                }
            } catch (IOException e) {
            	e.printStackTrace();
                LOG.error(e.getMessage(), e);
            }

        }
    }

    public JxlsRender templetFile(String templetFile) {
        this.templetFile = templetFile;
        return this;
    }

}
