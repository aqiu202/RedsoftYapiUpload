### RedsoftYapiUpload
一个快速生成接口文档的IntelliJ IDEA的Yapi上传小插件（基于[YapiIdeaUploadPlugin](https://github.com/diwand/YapiIdeaUploadPlugin)改造）
#### 目的
开发人员重复的向Yapi录入接口信息，费时费力，效率低下。该插件可以根据代码的注释和接口信息自动生成接口文档并上传到Yapi平台上
#### 支持语言
Java
#### 特性介绍
* 完美兼容JavaDoc和Swagger两种方式进行接口描述
* 友好的配置面板
* 完美支持YApi支持的Js-schema和Json5两种描述语言，并可以通过配置自动切换
* 完美支持任意泛型
* 完美支持SpringMVC的所有注解，支持单接口多路径配置等
* 支持WebFlux返回值的解析
* 兼容对Jackson注解的解析
* 支持对返回值的全局字段格式配置（驼峰、下划线等）
* 支持对某些字段/类的自动过滤
#### 支持IntelliJ IDEA的版本
2017+
#### 支持Yapi版本
1.5+
#### 上传方式
* 选中方法名上传对应的单个接口
* 选中类名上传该类下的所有接口
* 选中多个类/包（或者整个项目目录）批量上传选中的所有接口
#### 支持接口
HTTP接口
#### 使用方式
详情查看[使用文档](https://github.com/aqiu202/RedsoftYapiUpload/wiki/%E4%BD%BF%E7%94%A8%E6%8C%87%E5%8D%97)
