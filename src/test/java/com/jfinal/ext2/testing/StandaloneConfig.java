package com.jfinal.ext2.testing;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jfinal.ext.config.StandaloneDbConfig;
import com.jfinal.kit.HashKit;

import cn.zcq.model.Zcq;

public class StandaloneConfig {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void test() {
		StandaloneDbConfig.start();
		
		Zcq zcq = new Zcq();
		zcq.setId(0L);
		zcq.setAddr(HashKit.generateSalt(12));
//		
		zcq.save();
//		
//		Table table = TableMapping.me().getTable(Zcq.class);
//		System.out.println(table.getName());
	}
}
