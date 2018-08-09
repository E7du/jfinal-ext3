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
package cn.zhucongqi.configtest;

import com.jfinal.ext.config.StandaloneAppConfig;
import com.jfinal.kit.HashKit;
import com.test.api.model.User;

/**
 * 
 * 注释内容<br/>
 * 
 * @author yadong
 * 
 * @version 
 */
public class TestExt {

    public static void main(String args[]) {

        StandaloneAppConfig.start();
        
    	
		User u = new User();
		u.setId(1011);
		u.setName("zc😝q");
		u.setAddr(HashKit.generateSalt(12));
//		
		u.save();
    }

}
