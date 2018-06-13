/**
 * 
 */
package com.jfinal.ext.plugin.druid;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * @author Jobsz
 */
public class JFDruidConfigTools {

	public static void main(String[] args) throws Exception {
        String password = "admin";//args[0];
        String cipherText = ConfigTools.encrypt(password);
        System.out.println("encrypt password:" + cipherText);
        System.out.println("decrypt password:" + ConfigTools.decrypt(cipherText));
	}
}
