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
package com.jfinal.ext2.testing;

import org.junit.Test;

import com.jfinal.ext.kit.HexKit;


public class Hex {

	@Test
	public void test() {
		String name = "朱丛启";
		byte[] data = name.getBytes();
		
		String hexStr = HexKit.byteToHexString(data);
		
		System.out.println(hexStr);
		
		byte[] _data = HexKit.HexStringToBytes(hexStr);
		name = new String(_data);
		
		System.out.println(name);
		
	}

}
