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

import java.util.Random;
import java.util.UUID;

import com.jfinal.kit.HashKit;

/**
 * @author Jobsz
 *
 */
final public class RandomKit {

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
