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
package com.jfinal.ext.kit;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.jfinal.kit.HttpKit;

/**
 * 
 * @author Jobsz
 */
final public class HttpExtKit {

	/**
	 * Send Hex String request
	 */
	public static String postHexString(String url, Map<String, String> queryParas, byte[] data, Map<String, String> headers) {
		return HttpKit.post(url, queryParas, HexKit.byteToHexString(data), headers);
	}
	
	/**
	 * Send byte data by post request
	 */
	public static String post(String url, Map<String, String> queryParas, byte[] data, Map<String, String> headers) {
		return HttpKit.post(url, queryParas, (new String(data)), headers);
	}
	
	public static byte[] readHexByteData(HttpServletRequest request) {
		String ret = HttpKit.readData(request);
		return HexKit.HexStringToBytes(ret);
	}
	
	public static byte[] readByteData(HttpServletRequest request) {
		String ret = HttpKit.readData(request);
		byte[] data = null;
		try {
			data = ret.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			new RuntimeException(e);
		}
		return data;
	}
}
