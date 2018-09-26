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
package com.jfinal.ext.kit;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;

import com.jfinal.kit.StrKit;

public final class TypeKit {
	
	private static List<SimpleDateFormat> DATE_FORMAT_LIST = new ArrayList<SimpleDateFormat>(4);

    static {
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));
        DATE_FORMAT_LIST.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    public static Object convert(String value, Class<?> type, String format) {
        if (StrKit.notBlank(value)) {
            if (String.class.equals(type)) {
                return value;
            }
            
            if (Integer.class.equals(type) || int.class.equals(type)) {
                return Integer.parseInt(value);
            }
            
            if (Double.class.equals(type) || double.class.equals(type)) {
                return Double.parseDouble(value);
            }
            
            if (Float.class.equals(type) || float.class.equals(type)) {
				return Float.parseFloat(value);
			}
            
            if (Boolean.class.equals(type) || boolean.class.equals(type)) {
                String valueLower = value.toLowerCase();
                if (valueLower.equals("true") || valueLower.equals("false")) {
                    return Boolean.parseBoolean(value.toLowerCase());
                }
                Integer integer = Integer.parseInt(value);
                if (integer == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            
            if (Long.class.equals(type) || long.class.equals(type)) {
                return Long.parseLong(value);
            }
            
            if (BigInteger.class.equals(type)) {
				return (new BigInteger(value));
			}
            
            if (byte[].class.equals(type)) {
				return value.getBytes();
			}
            
            if (Short.class.equals(type)) {
				return Short.parseShort(value);
			}
            
            if (Byte.class.equals(type)) {
				return Byte.parseByte(value);
			}
            
            if (Date.class.equals(type) 
            		|| java.sql.Date.class.equals(type)
            		|| java.sql.Time.class.equals(type)
            		|| java.sql.Timestamp.class.equals(type)) {
            	Date date = null;
                if (value.contains("-") || value.contains("/") || value.contains(":")) {
                    date = getSimpleDateFormatDate(value, format);
                } else {
                    Double d = Double.parseDouble(value);
                    date = HSSFDateUtil.getJavaDate(d, true);
                }
                if (java.sql.Date.class.equals(type)) {
                	return new java.sql.Date(date.getTime());
				}
                if (java.sql.Time.class.equals(type)) {
					return new java.sql.Time(date.getTime());
				}
                if (java.sql.Timestamp.class.equals(type)) {
					return new java.sql.Timestamp(date.getTime());
				}
                return date;
            }
            
            if (BigDecimal.class.equals(type)) {
                return new BigDecimal(value);
            }
        }
        return null;
    }
    
    private static Date getSimpleDateFormatDate(String value, String format) {
        if (StrKit.notBlank(value)) {
            Date date = null;
            if (StrKit.notBlank(format)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
                try {
                    date = simpleDateFormat.parse(value);
                    return date;
                } catch (ParseException e) {
                }
            }
            for (SimpleDateFormat dateFormat : DATE_FORMAT_LIST) {
                try {
                    date = dateFormat.parse(value);
                } catch (ParseException e) {
                }
                if (date != null) {
                    break;
                }
            }
            return date;
        }
        return null;
    }

    public static String formatFloat(String value) {
        if (value.contains(".")) {
            if (TypeKit.isNumeric(value)) {
                try {
                    BigDecimal decimal = new BigDecimal(value);
                    BigDecimal setScale = decimal.setScale(10, BigDecimal.ROUND_HALF_DOWN).stripTrailingZeros();
                    return setScale.toPlainString();
                } catch (Exception e) {
                }
            }
        }
        return value;
    }

	public static boolean isInteger(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isDouble(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isFloat(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isLong(Object obj) {
		return TypeKit.isNumeric(obj);
	}
	
	public static boolean isNumeric(Object obj) {
		if (null == obj) {
			return false;
		}
        try {
            new BigDecimal(obj.toString());
        } catch (Exception e) {
            return false;
        }
        return true;
//		Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
//        Matcher isNum = pattern.matcher(obj.toString());
//        if (!isNum.matches()) {
//            return false;
//        }
//        return true;
	}
	
	public static boolean isString(Object obj) {
		return !TypeKit.isNumeric(obj);
	}
	
	public static boolean isBoolean(Object obj) {
		if (null == obj) {
			return false;
		}
		String val = obj.toString().toUpperCase();
		if (Boolean.TRUE.toString().toUpperCase().equals(val)
				|| Boolean.FALSE.toString().toUpperCase().equals(val)) {
			return true;
		}
		return false;
	}
	
	public static final class Type {
		
		/**
		 * Java String <br/>
		 * sql: varchar, char, enum, set, text, tinytext, mediumtext, longtext
		 */
		public static final Class<?> STRING = java.lang.String.class;
		
		/**
		 * Java Integer <br/>
		 * sql: int, integer, tinyint, smallint, mediumint
		 */
		public static final Class<?> INTEGER = java.lang.Integer.class;
		
		/**
		 * Java Byte <br/>
		 */
		public static final Class<?> BYTE = java.lang.Byte.class;
		
		/**
		 * Java Short <br/>
		 */
		public static final Class<?> SHORT = java.lang.Short.class;
		
		/**
		 * Java Long <br/>
		 * sql: bigint
		 */
		public static final Class<?> LONG = java.lang.Long.class;
		
		/**
		 * Java Double <br/>
		 * sql: real, double
		 */
		public static final Class<?> DOUBLE = java.lang.Double.class;
		
		/**
		 * Java Float <br/>
		 * sql: float
		 */
		public static final Class<?> FLOAT = java.lang.Float.class;
		
		/**
		 * Java Boolean <br/>
		 * sql: bit
		 */
		public static final Class<?> BOOL = java.lang.Boolean.class; 
		
		/**
		 * Java Date <br/>
		 * java.sql.Date, java.sql.Time, java.sql.Timestamp all extends java.util.Date so getDate can return the three types data
		 */
		public static final Class<?> DATE = java.util.Date.class;
		
		/**
		 * Java BigDecimal <br/>
		 * sql: decimal, numeric
		 */
		public static final Class<?> BIGDECIMAL = java.math.BigDecimal.class; 
		
		/**
		 * Java BigInteger <br/>
		 * sql: unsigned bigint
		 */
		public static final Class<?> BIGINTEGER = java.math.BigInteger.class; 
		
		/**
		 * binary, varbinary, tinyblob, blob, mediumblob, longblob <br/>
		 * qjd project: print_info.content varbinary(61800);
		 */
		public static final Class<?> BYTES = byte[].class;
		
		/**
		 * Sql Date <br/>
		 * sql: date, year
		 */
		public static final Class<?> SQL_DATE = java.sql.Date.class;
		
		/**
		 * sql: time
		 */
		public static final Class<?> SQL_TIME = java.sql.Time.class;
		
		/**
		 * sql: timestamp, datetime
		 */
		public static final Class<?> SQL_TIMESTAMP = java.sql.Timestamp.class;
 	}
}
