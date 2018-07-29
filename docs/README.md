### 介绍
jfinal-ext3，源自jfinal-ext，jfinal-ext2，基于jfinal3.x，扩展了很多特性。

### 新特性
- v3.1.1final,v3的最后一个版本。优化、修复 bug。
- v3.1.0 优化ModelExt,find(),findOne()从数据库和redis取数逻辑：syncToRedis 为 true 时，先用数据库获取包含 primarykey的数据结合，在用这个数据去 redis 中获取全量。【注意：syncToRedis的取值，不能在 save 或 find 见取值不一致，也就是syncToRedis不可以中间修改取值。以避免 redis 或数据库数据不一致的情况出现。】新加入dataCount()，用于获取数据数量。新加入findPrimarykeyOnly()，用于获取 primarykeys。
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
