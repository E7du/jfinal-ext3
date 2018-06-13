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
package com.jfinal.ext.kit;

import java.util.Random;
import java.util.UUID;

import com.jfinal.kit.HashKit;

/**
 * @author BruceZCQ
 *
 */
final public class RandomKit {

	public enum SMSCodeType{
		Numbers,
		CharAndNumbers,
	}
	
	/**
	 * 短信验证码,纯数字
	 * @param codeLen 验证码长度
	 * @return
	 */
	public static String smsAuthCode(int codeLen){
		return smsAuthCode(codeLen, SMSCodeType.Numbers);
	}
	
	/**
	 * sms验证码
	 * @param codeLen
	 * @param type
	 * @return
	 */
	public static String smsAuthCode(int codeLen, SMSCodeType type){
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
	
	/**
	 * 随机范围内的数
	 * @param min
	 * @param max
	 * @return
	 */
	public static int random(int min, int max){
		Random random = new Random();
		return random.nextInt(max)%(max-min+1) + min;
	}
	
	/**
	 * 随机字符串：UUID方式
	 * @return
	 */
	public static String randomStr(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 *  随机字符串再 md5：UUID方式
	 * @return
	 */
	public static String randomMD5Str(){
		return HashKit.md5(randomStr());
	}
}
