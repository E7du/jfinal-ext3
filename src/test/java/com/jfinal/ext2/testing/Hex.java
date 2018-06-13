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
