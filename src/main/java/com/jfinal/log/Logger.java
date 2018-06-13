/**
 * Copyright (c) 2018, Jobsz (zcq@zhucongqi.cn).
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
package com.jfinal.log;

import com.jfinal.ext.log.ILogFactoryExt;
import com.jfinal.ext.log.JdkLogFactoryExt;

/**
 * The five logging levels used by Log are (in order):
 * 1. DEBUG (the least serious)
 * 2. INFO
 * 3. WARN
 * 4. ERROR
 * 5. FATAL (the most serious)
 */
public abstract class Logger {
	
	private static ILogFactoryExt factory;
	
	static {
		init();
	}
	
	static void init() {
		if (factory == null) {
			try {
				Class.forName("org.apache.log4j.Logger");
				Class<?> log4jLogFactoryExtClass = Class.forName("com.jfinal.ext2.log.Log4jLogFactoryExt");
				factory = (ILogFactoryExt)log4jLogFactoryExtClass.newInstance();
			} catch (Exception e) {
				factory = new JdkLogFactoryExt();
			}
		}
	}
	
	public static void setLoggerFactory(ILogFactoryExt loggerFactory) {
		if (loggerFactory != null)
			Logger.factory = loggerFactory;
	}
	
	public static Logger getLogger(Class<?> clazz) {
		return factory.getLogger(clazz);
	}
	
	public static Logger getLogger(String name) {
		return factory.getLogger(name);
	}
	
	public abstract void debug(String message);
	
	public abstract void debug(String message, Throwable t);
	
	public abstract void info(String message);
	
	public abstract void info(String message, Throwable t);
	
	public abstract void warn(String message);
	
	public abstract void warn(String message, Throwable t);
	
	public abstract void error(String message);
	
	public abstract void error(String message, Throwable t);
	
	public abstract void fatal(String message);
	
	public abstract void fatal(String message, Throwable t);
	
	public abstract boolean isDebugEnabled();

	public abstract boolean isInfoEnabled();

	public abstract boolean isWarnEnabled();

	public abstract boolean isErrorEnabled();
	
	public abstract boolean isFatalEnabled();
}

