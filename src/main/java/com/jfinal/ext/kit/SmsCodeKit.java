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

final public class SmsCodeKit {
	
	public enum SMSCodeType{
		Numbers,
		CharAndNumbers,
	}
	
	/**
	 * 短信验证码,纯数字
	 * @param codeLen 验证码长度
	 */
	public static String smsCode(int codeLen){
		return smsCode(codeLen, SMSCodeType.Numbers);
	}
	
	/**
	 * sms验证码
	 * @param codeLen
	 * @param type
	 */
	public static String smsCode(int codeLen, SMSCodeType type){
		String randomCode = "";
		String strTable = type == SMSCodeType.Numbers ? "1234567890"
				: "1234567890abcdefghijkmnpqrstuvwxyz";
		int len = strTable.length();
		boolean bDone = true;
		do {
			randomCode = "";
			int count = 0;
			for (int i = 0; i < codeLen; i++) {
				double dblR = Math.random() * len;
				int intR = (int) Math.floor(dblR);
				char c = strTable.charAt(intR);
				if (('0' <= c) && (c <= '9')) {
					count++;
				}
				randomCode += strTable.charAt(intR);
			}
			if (count >= 2) {
				bDone = false;
			}
		} while (bDone);

		return randomCode.toUpperCase();
	}
}
