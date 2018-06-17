package cn.zhucongqi.configtest;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.config.JFinalConfigExt;
import com.jfinal.ext.config.JFinalExt;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.template.Engine;

/**
 * 
 */

/**
 * @author Jobsz
 *
 */
public class TestConfig extends JFinalConfigExt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig cfg = new TestConfig();
		cfg.configConstant(JFinal.me().getConstants());
		System.out.println(JFinalExt.DOWNLOAD_PATH);
		System.out.println(JFinalExt.UPLOAD_PATH);
		System.out.println(JFinalExt.DEV_MODE);
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
	public void configTablesMapping(String configName, ActiveRecordPlugin arp) {
		
	}

	@Override
	public void configMoreInterceptors(Interceptors me) {
		
	}

	@Override
	public void configMoreHandlers(Handlers me) {
		
	}

	@Override
	public void afterJFinalStarted() {
		
	}

	@Override
	public void configEngineMore(Engine me) {
		me.setBaseTemplatePath("sql.txt");
	}

}
