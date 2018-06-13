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

import com.jfinal.kit.StrKit;

/**
 * @author Jobsz
 */
final public class HexKit {

	public static byte[] HexStringToBytes(String hexString) {
		if (StrKit.isBlank(hexString)) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] data = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			data[i] = (byte) (c2b(hexChars[pos]) << 4 | c2b(hexChars[pos + 1]));
		}
		return data;
	}

	private static byte c2b(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	public static String byteToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
}
