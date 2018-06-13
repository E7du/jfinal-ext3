/**
 * 
 */
package com.jfinal.ext.testing;

import java.io.File;
import java.util.List;

import com.jfinal.ext.kit.ClassSearcher;

/**
 * @author BruceZCQ
 *
 */
public class ClassSearcherTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassSearcher search = new ClassSearcher(ClassSearcher.class);
		search.libDir(".."+File.separator);
		List<?> list = search.search();
		System.out.println(list);
	}

}
