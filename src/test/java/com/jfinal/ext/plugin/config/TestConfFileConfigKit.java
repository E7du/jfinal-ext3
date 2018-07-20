package com.jfinal.ext.plugin.config;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestConfFileConfigKit {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		ConfigPlugin confConfigPlugin = new ConfigPlugin(".*.conf").reload(false);
		confConfigPlugin.start();
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	public void testGetStr() throws InterruptedException {
		assertEquals("test", ConfigKit.getStr("name"));
		assertEquals(1, ConfigKit.getInt("age"));
	}

	@Test
	public void testZw() throws InterruptedException {
		assertEquals("中文内容", ConfigKit.getStr("zw"));
		assertEquals("xxx", ConfigKit.getStr("中"));
	}
}
