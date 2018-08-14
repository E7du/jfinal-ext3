### 介绍
jfinal-ext3，源自jfinal-ext，jfinal-ext2，基于jfinal3.x，扩展了很多特性。

![maven](https://img.shields.io/maven-central/v/com.jfinal/jfinal-ext3.svg)

### 新特性
- v4.0.1：修改ModelToRedis的存储方式，由Hash模式变为Generic模式。
- v4.0.0 ：加入 Xls 读写，XlsRender等。
 
 ```java
	void readXls() {

		XlsReadRule xlsReadRule = new XlsReadRule();
		xlsReadRule.setStart(1);
		xlsReadRule.setEnd(6);
		xlsReadRule.setClazz(User.class);

		Column id = Column.create("id");
		Column name = Column.create("name");
		Column addr = Column.create("addr");
		xlsReadRule.alignColumn(id, name, addr);

		String destFileName = "src/test/resources/users.xls";

		List<User> ret = XlsReader.readToModel(User.class, new File(destFileName), xlsReadRule);
		User u = ret.get(0);
		String json = JsonKit.toJson(ret);
		System.out.println(json + "id=" + u.getId() + ";name=" + u.getName() + ";addr=" + u.getAddr());

	}    
    

	void writeXls() {
		
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < 5; i++) {
			User u = new User();
			u.setId(i);
			u.setName("name"+i);
			u.setAddr("addr"+i);
			users.add(u);
		}
		
		 //XlsWriter.data(users).headerRow(1).header("Id","Name", "Addr").column("id","name", "addr").writeToFile("src/test/resources/users.xls");
		 
		 Column id = Column.header("编号", "id");
		 Column name = Column.header("姓名", "name");
		 Column addr = Column.header("地址", "addr");
		 XlsWriter.data(users).columns(id, name, addr).writeToFile("src/test/resources/users.xls");
	}
    
 ```
    
- v3.1.1final,v3的最后一个版本。优化、修复 bug。
- v3.1.0 优化ModelExt,fetch(),fetchOne()从数据库和redis取数逻辑：syncToRedis 为 true 时，先用数据库获取包含 primarykey的数据结合，在用这个数据去 redis 中获取全量。【注意：syncToRedis的取值，不能在 save 或 find 见取值不一致，也就是syncToRedis不可以中间修改取值。以避免 redis 或数据库数据不一致的情况出现。】新加入dataCount()，用于获取数据数量。新加入fetchPrimarykeyOnly()，用于获取 primarykeys。
- v3.0.9 优化 loadPropertyFile
- v3.0.8: 无需conf/jf-app-cfg.conf，使用code级配置。参考getLazyProp。

```java
/**
	 * Lazy Config Prop
	 * 
	 * <pre>
	 * Properties prop = new Properties();
	 * !// config db
	 * prop.setProperty("db.ds","mysql");
	 * prop.setProperty("db.mysql.active","true");
	 * prop.setProperty("db.mysql.url","localhost/zcq");
	 * prop.setProperty("db.mysql.user","root");
	 * prop.setProperty("db.mysql.password.pkey","MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJbIzkTcjrlDwB3vdc6gKwJ5gAMGRazWOrOeuMCxI2Lb7n/d4DoBySUdM+7HT6Gkfbfz6BM1o/2Gp0PkhkEHAx8CAwEAAQ==");
	 * prop.setProperty("db.mysql.password","TVL59a5MXxM3EB7Ylzf8idFijFX97+ZRxZG+2PpkR4pPCQ5TLtZAok88IGW05CxRvC56ekO++yWhepAEL118lw==");
	 * prop.setProperty("db.mysql.initsize","10");
	 * prop.setProperty("db.mysql.maxactive","100");
	 * prop.setProperty("db.showsql","true");
	 * !// config redis
	 * prop.setProperty("redis.cs","mainCache");
	 * prop.setProperty("redis.mainCache.active","true");
	 * prop.setProperty("redis.mainCache.host","localhost");
	 * prop.setProperty("redis.mainCache.port","6379");
	 * prop.setProperty("redis.mainCache.password","");
	 * prop.setProperty("redis.mainCache.tables","user,hello");
	 * !// config generator
	 * prop.setProperty("ge.dict","true");
	 * prop.setProperty("ge.model.dao","true");
	 * prop.setProperty("ge.mappingkit.classname","TableMappingKit");
	 * prop.setProperty("ge.base.model.outdir","./src/main/java/cn/zhucongqi/api/base/model");
	 * prop.setProperty("ge.base.model.package","cn.zhucongqi.api.base.model");
	 * prop.setProperty("ge.model.outdir","./src/main/java/cn/zhucongqi/api/model");
	 * prop.setProperty("ge.model.package","cn.zhucongqi.api.model");
	 * !// config app
	 * prop.setProperty("app.dev","true");
	 * prop.setProperty("app.post","true");
	 * prop.setProperty("app.name","jf-app");
	 * </pre>
	 */
	public abstract Properties getLazyProp();
```

### 使用

```java
<dependency>
    <groupId>com.jfinal</groupId>
    <artifactId>jfinal-ext3</artifactId>
    <version>${new_version}</version>
</dependency>
```

### 特性说明

#### Kits
- ArrayKit: String 数组的交集、并集、差集；
- BatchSaveKit：批量 Save 工具；
- ClassSearcher：class 查找工具；
- DateTimeKit：时间日期工具；
- DDLKit：数据表生成工具；
- EncodingKit：编码工具；
- FastJsonKit：基于 FastJson 的工具包，如要引入 fastjson 的 jar；
- HexKit：Hex 与字符串互转工具；
- HttpExtKit：Http 工具，扩展 postHex 的方法；
- JaxbKit：Xml 解析工具；
- JFinalKit：获取 jfinal 配置信息工具；
- KeyLabel：键标（值）工具；
- MobileValidateKit：手机号校验工具，支持13开头，147开头，15开头，17开头，18开头的各种手机号；
- PageViewKit：Web 项目或者 view 的工具，可以将 view 放到 WEB-INF以下；

```java
|-- 
|   |-- META-INF
|   |-- WEB-INF
 		|-- classes  
		|-- errorpages
   		|	|-- 403.jsp
		|	|-- 404.jsp
   	   	|	`-- 500.jsp
       	|-- lib
        `-- pageviews
        	|-- login
        	| 	|-- login.jsp
        		`-- ..jsp
        	|-- admin
        	|	|-- ..
        	|	`-- ..
        	`-- ..
        	
   <pre>
   web.xml:
   	<error-page>
		<error-code>403</error-code>
		<location>/WEB-INF/errorpages/403.jsp</location>
	</error-page>
	<error-page>
		<error-code>404</error-code>
		<location>/WEB-INF/errorpages/404.jsp</location>
	</error-page>
	<error-page>
		<error-code>500</error-code>
		<location>/WEB-INF/errorpages/500.jsp</location>
	</error-page>
   </pre>
```
- Prop：扩展com.jfinal.kit.Prop，可读写com.jfinal.kit.Prop的 properties 属性，方便用 Properties 实体直接实例化 Prop；
- RandomKit：随机数据生成工具；
- RecordKit：Record 工具；
- Reflect，ReflectException：反射工具；
- SerializableKit：序列化工具；
- ServletKit：获取 HTTPRequest 的 IP，URL工具；
- SmsCodeKit：短信验证码生成工具，纯数字或数字、字母组合；
- SqlKit：Sql 生成工具；
- SqlpKit：Sql 解析工具；
- TypeKit：类型管理工具，检查Object 是否为数字、是否为 boolean 等；
- UploadPathKit：获取上传日期目录工具；

##### 配置说明
- 主要就是conf/jf-app-cfg.conf（以下简称”配置文件“）的配置说明
- 配置文件可以很方便的配置：多数据源、redis、modelGenerator
- 数据源配置：
	- db.ds：数据源配置属性，数据格式为：以英文,分割的多数据源简称，如db.ds=mysql,oracle,other。这里的数据源简称可以任意起名，与其他关联属性配合即可。以下以db.ds=mysql举例；
	- db.mysql.active：数据源是否激活属性，取值true或false。true表示对应的数据源激活，false表示对应的数据源未激活；
	- db.mysql.url：数据源URL属性。根据不同的数据库对应的URL不一样；
	- db.mysql.user：数据源用户名；
	- db.mysql.password：数据源密码；
	- db.mysql.password.pkey：数据源加密publickey。通过 `java -cp druid-xx.jar com.alibaba.druid.filter.config.ConfigTools your_password`可以获取publickey；(详看[这里](https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter))
	- db.mysql.initsize：数据源初始连接池；
	- db.mysql.maxsize：数据源最大活跃连接数；
	- db.showsql：时候答应SQL，true打印，false不打印，此属性对所有数据库将全部生效。
- redis配置
	- redis.cs：redis的多实例配置，数据格式与db.ds类似，以英文,分割的多缓存实例简称，如redis.cs=mainCache,slaveCache,other。这里的缓存实例简称可以任意起名，与其他关联属性配合即可。以下以redis.cs=mainCache举例；
	- redis.mainCache.active：redis实例时候激活，取值true或false。true表示对应redis实例激活，false表示对应redis实例未激活；
	- redis.mainCache.host：redis实例的host，默认为localhost；
	- redis.mainCache.port：redis实例的端口，默认为6379；
	- redis.mainCache.password：redis实例的密码；
	- redis.mainCache.tables：redis映射的数据库表，以英文,分割的数据库表（注意大小写）（下面会重点讲解redis.mainCache.tables的使用）.
- modelGenerator配置（以下会详细讲解如何使用Ge）
	- ge.dict：是否生成数据词典，取值true或false。true表示生成，false表示不生成；
	- ge.model.dao：是否生成model的dao实例，取值true或false。true表示生成，false表示不生成；
	- ge.mappingkit.classname：mappingkit的classname，可自定义，如MappingKit。jfinal-ext3不根据不同的数据源，生成多个ge.mappingkit.classname对应的mappingkit文件。假设ge.mappingkit.classname=MappingKit，db.ds=mysql，则生成的mappingkit文件为MYSQLMappingKit.java。
	- ge.base.model.outdir：生成的BaseModel的存放位置，如：./src/cn/zcqq/base/model；
	- ge.base.model.package：生成的BaseModel的包名，如：cn.zcqq.base.model；
	- ge.model.outdir：生成的Model的存放位置，如：./src/cn/zcqq/model；
	- ge.model.package：生成Model的包名，如：cn.zcqq.model。
- 其他配置
	- app.dev：是否为debug模式，取值true：debug模式，false：生产模式；
	- app.post：所有的请求是否都使用POST拦截，取值true：POST拦截，false：依据Controller定义；
	- app.name： 应用名字配置。

### redis配置详解（redis.mainCache.tables）

- jfinal-ext3：使用ModelExt扩展了Model；
- 使用jfinal-ext3的Ge生产的BaseModel默认extends ModelExt，以此使用ModelExt的扩展功能；
	- ModelExt的扩展功能
		- 使用syncToRedis来将db操作同步到redis；
		- save，delete，update，find操作，透明同步到redis；
		- 结合ModelRedisPlugin和jf-app-cfg.conf，把redis.*.tables的数据库表与对应的redis实例透明映射；
		- 使用者可以在使用中修改使用shotCacheName来修改redis实例；注意：一旦手动设置shotCacheName，那么syncToRedis将自动开启；
		- 具体使用可查看[Test](https://github.com/E7du/jfinal-ext3/blob/master/src/test/java/cn/zhucongqi/redis/RedisCache.java)。

### Ge的使用

- 配置好jf-app-cfg.conf的db部分；
- 右击项目Debug As->Debug Configurations，双击左侧的Java Application，在右侧的Main class下写入com.jfinal.ext.ge.Ge，然后点击右下角的Debug按钮即可。 
