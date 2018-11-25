import java.util.Date;

import org.junit.jupiter.api.Test;

import com.jfinal.ext.kit.DateTimeKit;

class DateTimeKitDemo {

	@Test
	void test() {
		Date date = DateTimeKit.getSomeDay(180);
		
		System.out.println(date);
	}

}
