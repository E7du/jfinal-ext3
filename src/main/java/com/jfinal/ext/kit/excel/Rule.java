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
package com.jfinal.ext.kit.excel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Lists;
import com.jfinal.ext.interceptor.excel.PostExcelProcessor;
import com.jfinal.ext.interceptor.excel.PostListProcessor;
import com.jfinal.ext.interceptor.excel.PreExcelProcessor;
import com.jfinal.ext.interceptor.excel.PreListProcessor;

@XmlRootElement
public class Rule {
	
	/**
	 * Data From
	 */
	private int start;

	/**
	 * Data End
	 */
	private int end;

	private PreExcelProcessor preExcelProcessor;

	private PostExcelProcessor postExcelProcessor;

	private PreListProcessor preListProcessor;

	private PostListProcessor postListProcessor;
    
	/**
	 * Data Convert Model's Class
	 */
    private Class<?> clazz;

    private List<Cell> cells = Lists.newArrayList();

    public int getStart() {
        return start;
    }

    public void setStart(int value) {
        this.start = value;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int value) {
        this.end = value;
    }

    public PreExcelProcessor getPreExcelProcessor() {
        return preExcelProcessor;
    }

    public void setPreExcelProcessor(PreExcelProcessor value) {
        this.preExcelProcessor = value;
    }

    public PostExcelProcessor getPostExcelProcessor() {
        return postExcelProcessor;
    }

    public void setPostExcelProcessor(PostExcelProcessor value) {
        this.postExcelProcessor = value;
    }

    public PreListProcessor getPreListProcessor() {
        return preListProcessor;
    }

    public void setPreListProcessor(PreListProcessor value) {
        this.preListProcessor = value;
    }

    public PostListProcessor getPostListProcessor() {
        return postListProcessor;
    }

    public void setPostListProcessor(PostListProcessor value) {
        this.postListProcessor = value;
    }
    
    public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	@XmlElementWrapper(name = "cells")
    @XmlElement(name = "cell")
    public List<Cell> getCells() {
        return cells;
    }

    public void setCells(List<Cell> cells) {
        this.cells = cells;
    }

    public Rule addCell(Cell cell) {
        cells.add(cell);
        return this;
    }

    public Rule addCell(int index, String attrName) {
        cells.add(Cell.create(index, attrName));
        return this;
    }

    public Rule addCell(int index, String attribute, CellConvert convert, CellValidate validate) {
        cells.add(Cell.create(index, attribute, convert, validate));
        return this;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Cell {

        protected int index;

        protected String attr;

        protected CellConvert convert;

        protected CellValidate validate;

        public static Cell create(int index, String attr) {
            Cell cell = new Cell();
            cell.setIndex(index);
            cell.setAttr(attr);
            return cell;
        }

        public static Cell create(int index, String attr, CellConvert convert, CellValidate validate) {
            Cell cell = create(index, attr);
            cell.setConvert(convert);
            cell.setValidate(validate);
            return cell;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int value) {
            this.index = value;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public CellConvert getConvert() {
            return convert;
        }

        public void setConvert(CellConvert value) {
            this.convert = value;
        }

        public CellValidate getValidate() {
            return validate;
        }

        public void setValidate(CellValidate value) {
            this.validate = value;
        }

        @Override
		public String toString() {
			return "Cell [index=" + index + ", attr=" + attr + ", convert=" + convert + ", validate="
					+ validate + "]";
		}
    }

//    @Override
//	public String toString() {
//		return "Rule [name=" + name + ", sheetNo=" + sheetNo + ", start=" + start + ", end=" + end + ", rowFilter="
//				+ rowFilter + ", preExcelProcessor=" + preExcelProcessor + ", postExcelProcessor=" + postExcelProcessor
//				+ ", preListProcessor=" + preListProcessor + ", postListProcessor=" + postListProcessor + ", clazz="
//				+ clazz + ", cells=" + cells + "]";
//	}
}
