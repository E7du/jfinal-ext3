# jfinal-ext3

#### Maven dependency

![maven](https://img.shields.io/maven-central/v/com.jfinal/jfinal-ext3.svg)

```java
<dependency>
    <groupId>com.jfinal</groupId>
    <artifactId>jfinal-ext3</artifactId>
    <version>${jfinal-ext3.version}</version>
</dependency>
```

#### 特性清单
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

[更多查看文档docs](https://e7du.github.io/jfinal-ext3/)
