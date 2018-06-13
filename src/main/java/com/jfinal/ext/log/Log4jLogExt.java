/**
 * Copyright (c) 2015-2016, BruceZCQ (zcq@zhucongqi.cn).
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
package com.jfinal.ext.log;

import org.apache.log4j.Level;

import com.jfinal.log.Log4jLog;
import com.jfinal.log.Logger;

/**
 * Log4jLogExt
 * @author BruceZCQ
 */
public class Log4jLogExt extends Logger {

	private org.apache.log4j.Logger log;
	private static final String callerFQCN = Log4jLog.class.getName();
	
	Log4jLogExt(Class<?> clazz) {
		log = org.apache.log4j.Logger.getLogger(clazz);
	}
	
	Log4jLogExt(String name) {
		log = org.apache.log4j.Logger.getLogger(name);
	}
	
	public static Log4jLogExt getLogger(Class<?> clazz) {
		return new Log4jLogExt(clazz);
	}
	
	public static Log4jLogExt getLogger(String name) {
		return new Log4jLogExt(name);
	}
	
	public void info(String message) {
		log.log(callerFQCN, Level.INFO, message, null);
	}
	
	public void info(String message, Throwable t) {
		log.log(callerFQCN, Level.INFO, message, t);
	}
	
	public void debug(String message) {
		log.log(callerFQCN, Level.DEBUG, message, null);
	}
	
	public void debug(String message, Throwable t) {
		log.log(callerFQCN, Level.DEBUG, message, t);
	}
	
	public void warn(String message) {
		log.log(callerFQCN, Level.WARN, message, null);
	}
	
	public void warn(String message, Throwable t) {
		log.log(callerFQCN, Level.WARN, message, t);
	}
	
	public void error(String message) {
		log.log(callerFQCN, Level.ERROR, message, null);
	}
	
	public void error(String message, Throwable t) {
		log.log(callerFQCN, Level.ERROR, message, t);
	}
	
	public void fatal(String message) {
		log.log(callerFQCN, Level.FATAL, message, null);
	}
	
	public void fatal(String message, Throwable t) {
		log.log(callerFQCN, Level.FATAL, message, t);
	}
	
	public boolean isDebugEnabled() {
		return log.isDebugEnabled();
	}
	
	public boolean isInfoEnabled() {
		return log.isInfoEnabled();
	}
	
	public boolean isWarnEnabled() {
		return log.isEnabledFor(Level.WARN);
	}
	
	public boolean isErrorEnabled() {
		return log.isEnabledFor(Level.ERROR);
	}
	
	public boolean isFatalEnabled() {
		return log.isEnabledFor(Level.FATAL);
	}

}
