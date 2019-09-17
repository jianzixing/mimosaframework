<img src="https://repository-images.githubusercontent.com/207211209/39fc7180-d94f-11e9-8866-d05f91f10f31" width="600" align="center"/>

## MimosaFramework简介
MimosaFramework是一组框架组合，主要功能是提供数据库读写的工具。

框架分为三部分，第一部分mimosa-core包含一些基本的工具类以及ModelObject的JSON操作库(使用FastJson为基础)，以及cglib和ognl表达式等开源库(方便独立使用)。
第二部分mimosa-orm是数据库读写操作的核心库(依赖mimosa-core)，用过API操作数据库，通过注解类自动创建数据库表，并且可以提供类似Mybatis的xml配置SQL的方式执行SQL语句。
第三部分mimosa-mvc是在spring-mvc基础上扩展的方便Ajax交互的工具(依赖mimosa-core)，可以简化后台Ajax交互复杂度。

总的来说MimosaFramework可以给我们提供一个方便快捷开发的半自动框架。MimosaFramework并不能帮我们做全部的事情，很多时候依旧需要开发者本身去处理一些事情。

框架的参考文档点击这里 [参考文档](https://mimosaframework.org) 访问查看MimosaFramework详细教程。

### Markdown

Markdown is a lightweight and easy-to-use syntax for styling your writing. It includes conventions for

```markdown
Syntax highlighted code block

# Header 1
## Header 2
### Header 3

- Bulleted
- List

1. Numbered
2. List

**Bold** and _Italic_ and `Code` text

[Link](url) and ![Image](src)
```

For more details see [GitHub Flavored Markdown](https://guides.github.com/features/mastering-markdown/).

### Jekyll Themes

```java
System.out.println("");
```

Your Pages site will use the layout and styles from the Jekyll theme you have selected in your [repository settings](https://github.com/yangankang/a/settings). The name of this theme is saved in the Jekyll `_config.yml` configuration file.

### Support or Contact

Having trouble with Pages? Check out our [documentation](https://help.github.com/categories/github-pages-basics/) or [contact support](https://github.com/contact) and we’ll help you sort it out.
