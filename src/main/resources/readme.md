说明
1. 数据连接
	参考 cfg.txt;
2. cfg.txt 组成
	2.1 测试模块
	2.2 生成模块
	2.3 包含内容
		jdbcUrl:数据库连接 jdbc
		user:数据库用户名
		password :数据库密码
		initsize : 连接池初始大小
		maxactive : 连接池最大连接数
		dbtype : 数据库类型
3. INFO: At least one JAR was scanned for TLDs yet contained no TLDs. Enable debug logging for this logger for a complete list of JARs that were scanned but no TLDs were found in them. Skipping unneeded JARs during scanning can improve startup time and JSP compilation time.
	在log4j.properties中加入
	Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager
4. errorpages文件夹放到 WEB-INF 中