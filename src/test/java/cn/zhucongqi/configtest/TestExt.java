/*
 * <strong>File   ：</strong>TestExt.java <br/>
 * <strong>Project：</strong>JFinal-ext2 <br/>
 * <strong>Date   ：</strong>2018年4月13日 下午5:27:46 <br/>
 * 
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
		u.setId(101L);
		u.setName("zcq");
		u.setAddr(HashKit.generateSalt(12));
//		
		u.save();
    }

}
