package cn.zhucongqi.prop;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.config.JFinalConfigExt;
import com.jfinal.template.Engine;

class ApiConfigPropDemo  extends JFinalConfigExt {

	@Test
	void test() {
		ApiConfigPropDemo d = new ApiConfigPropDemo();
		d.loadPropertyFile();
		d.dd();
	}
	
	public void dd() {
		
		assertEquals(this.prop.getBoolean("a"), true);
		assertEquals(this.prop.get("b"), "b_string");
	}

	@Override
	public Properties getLazyProp() {
		Properties p = new Properties();
		
		p.setProperty("a", "true");
		p.setProperty("b", "b_string");
		return p;
	}

	@Override
	public void configMoreConstants(Constants me) {
		
	}

	@Override
	public void configMoreRoutes(Routes me) {
		
	}

	@Override
	public void configMorePlugins(Plugins me) {
		
	}

	@Override
	public void configMoreInterceptors(Interceptors me) {
		
	}

	@Override
	public void configMoreHandlers(Handlers me) {
		
	}

	@Override
	public void configEngineMore(Engine e) {
		
	}

	@Override
	public void afterJFinalStarted() {
		
	}

	
}
