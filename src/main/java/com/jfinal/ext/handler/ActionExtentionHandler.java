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
package com.jfinal.ext.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * @author Jobsz
 */
public class ActionExtentionHandler extends Handler {

	// 伪静态处理
	public static final String htmlExt = ".html";
	public static final String htmExt = ".htm";
	public static final String jsonExt = ".json";
	
	private int len = ActionExtentionHandler.htmlExt.length();
	private String actionExt = ActionExtentionHandler.htmlExt;
	
	public ActionExtentionHandler(){
		this(ActionExtentionHandler.htmExt);
	}
	
	public ActionExtentionHandler(String actionExt){
		this.actionExt = actionExt;
		this.len = actionExt.length();
	}
	
	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		if (target.endsWith(this.actionExt))
            target = target.substring(0, target.length() - this.len);
        next.handle(target, request, response, isHandled);
	}

}
