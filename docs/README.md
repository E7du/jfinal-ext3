### 介绍
jfinal-ext3，源自jfinal-ext，jfinal-ext2，基于jfinal3.x，扩展了很多特性。

### 使用

```java
<dependency>
    <groupId>com.jfinal</groupId>
    <artifactId>jfinal-ext3</artifactId>
    <version>3.0.6</version>
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
	- db.showsql：时候答应SQL，true打印，false不打印。
- redis配置
	- redis.cs：redis的多实例配置，数据格式与db.ds类似，以英文,分割的多缓存实例简称，如redis.cs=mainCache,slaveCache,other。这里的缓存实例简称可以任意起名，与其他关联属性配合即可。以下以redis.cs=mainCache举例；
	- redis.mainCache.active：redis实例时候激活，取值true或false。true表示对应redis实例激活，false表示对应redis实例未激活；
	- redis.mainCache.host：redis实例的host，默认为localhost；
	- redis.mainCache.port：redis实例的端口，默认为6379；
	- redis.mainCache.password：redis实例的密码；
	- redis.mainCache.tables：redis映射的数据库表，以英文,分割的数据库表（注意大小写）（下面会重点讲解redis.mainCache.tables的使用）；

