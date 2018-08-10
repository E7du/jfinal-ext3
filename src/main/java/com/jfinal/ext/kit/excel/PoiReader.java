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

import java.io.File;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.Lists;
import com.jfinal.ext.kit.Reflect;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

public class PoiReader {

    public static List<List<List<String>>> readExcel(File file, Rule rule) {
        int start = rule.getStart();
        int end = rule.getEnd();
        List<List<List<String>>> result = Lists.newArrayList();
        Workbook wb = null;
        try {
            wb = WorkbookFactory.create(file);
        } catch (Exception e) {
            throw new PoiException(e);
        }
        
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            Sheet sheet = wb.getSheetAt(i);
            List<List<String>> sheetList = Lists.newArrayList();
            int rows = sheet.getLastRowNum();
            if (start <= sheet.getFirstRowNum()) {
                start = sheet.getFirstRowNum();
            }
            if (end >= rows) {
                end = rows;
            } else if (end <= 0) {
                end = rows + end;
            }
            for (int rowIndex = start; rowIndex <= end; rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                List<String> columns = Lists.newArrayList();
                int cellNum = row.getLastCellNum();
                for (int cellIndex = row.getFirstCellNum(); cellIndex < cellNum; cellIndex++) {
                    Cell cell = row.getCell(cellIndex);
                    if (null == cell) {
						continue;
					}
                    CellType cellType = cell.getCellTypeEnum();
                    String column = "";
                    if (CellType.NUMERIC.equals(cellType)) {
                    	column = String.valueOf(cell.getDateCellValue());
					} else if (CellType.STRING.equals(cellType)) {
                        column = cell.getStringCellValue();
					} else if (CellType.BOOLEAN.equals(cellType)) {
                        column = cell.getBooleanCellValue() + "";
					} else if (CellType.FORMULA.equals(cellType)) {
                        column = cell.getCellFormula();
					} else if (CellType.ERROR.equals(cellType)
							|| CellType.BLANK.equals(cellType)) {
						column = "";
					}
                    columns.add(column.trim());
                }

                List<Boolean> rowFilterFlagList = Lists.newArrayList();
                List<RowFilter> rowFilterList = Lists.newArrayList();
                for (int k = 0; k < rowFilterList.size(); k++) {
                    RowFilter rowFilter = rowFilterList.get(k);
                    rowFilterFlagList.add(rowFilter.doFilter(rowIndex, columns));
                }
                if (!rowFilterFlagList.contains(false)) {
                    sheetList.add(columns);
                }
            }
            result.add(sheetList);
        }
        return result;
    }

    public static List<List<String>> readSheet(File file, Rule Rule) {
        return readExcel(file, Rule).get(0);
    }

    public static List<Model<?>> processSheet(File file, Rule rule) {
        List<List<String>> srcList = readSheet(file, rule);
        List<Model<?>> results = Lists.newArrayList();
        for (int i = 0; i < srcList.size(); i++) {
            List<String> list = srcList.get(i);
            Model<?> model = fillModel(rule.getClazz(), list, rule);
            results.add(model);
        }
        return results;
    }

	public static Model<?> fillModel(Class<?> clazz, List<String> list, Rule rule) {
        Model<?> model = Reflect.on(clazz).create().get();
        String[] values = list.toArray(new String[]{});
        String message = "";
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            Rule.Cell cell = matchCell(rule, i);
            String name = cell.getAttribute();
            CellValidate cellValidate = cell.getValidate();
            boolean valid = true;
            if (null != cellValidate) {
                valid = cellValidate.validate(value);
                if (!valid) {
                    message = message + "value(" + value + ") is invalid in column " + cell.getIndex() + "</br>";
                }
            }
            if (valid) {
                Object convertedValue = value;
                CellConvert cellConvert = cell.getConvert();
                if (null != cellConvert) {
                    convertedValue = cellConvert.convert(value, model);
                }
                model.set(name, convertedValue);
            }
        }
        if (StrKit.notBlank(message)) {
            throw new PoiException(message);
        }
        return model;
    }

    public static Rule.Cell matchCell(Rule rule, int index) {
        List<Rule.Cell> cells = rule.getCells();
        for (int i = 0; i < cells.size(); i++) {
            Rule.Cell cell = cells.get(i);
            if (index + 1 == cell.getIndex()) return cell;
        }
        return null;
    }

}
