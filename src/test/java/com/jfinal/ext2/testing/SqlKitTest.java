package com.jfinal.ext2.testing;
import org.junit.Test;

import com.jfinal.ext.kit.SqlKit;

public class SqlKitTest {

	@Test
	public void test() {
		SqlKit kit = new SqlKit();
//		kit.select("a","b","c").from("user").where("user=1").and("name='2'").ascOrderBy("ii").limit("1,12");
//		System.out.println(kit.sql());
		
		kit = new SqlKit();
		
		kit.select(kit.column("id", "uid"),
				kit.column("username", "name")).from("bame as","asas a").where("a = b").ascOrderBy("ass").limit("12","1223");
		
		System.out.println(kit.sql());
		
		kit = new SqlKit();
		kit.update("user").set("name","'zcq","addr","北京'").where("name = s");
		System.out.println(kit.sql());
//		kit = new SqlKit();
//		kit.insert("user").values("name","zcq'","addr","北京'");
//		System.out.println(kit.sql());
		
		String baseDir = "/var/uploads";
//		if (baseDir.endsWith("/")) {
//			if (!baseDir.endsWith("uploads/")) {
//				baseDir += "uploads/";	
//			}
//		}else{
//			if (!baseDir.endsWith("uploads")) {
//				baseDir += "/uploads/";
//			}else{
//				baseDir += "/";
//			}
//		}
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		
		if (!baseDir.endsWith("uploads")) {
			baseDir += "uploads/";
		}
		
		System.out.println(baseDir);
		
	}

}
