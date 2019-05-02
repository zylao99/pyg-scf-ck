package com.pyg.fm;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

/**
 * Created by on 2018/8/28.
 */
public class MyFreemarker {

    /**
     * 需求：freemarker入门案例
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test01() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("hello.ftl");

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("name","张三");
        maps.put("message","习总来视察");

        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\fisrt.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }

    /**
     * 需求：模板页面获取对象数据
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test02() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("person.ftl");

        //创建对象
        Person p = new Person();
        p.setId(100000);
        p.setUsername("张无机");
        p.setSex("男");
        p.setAge("1");
        p.setAddress("冰火弹");

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("p",p);


        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\person.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }

    /**
     * 需求：freemarker入门案例
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test03() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("assain.ftl");

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("name","张三");
        maps.put("message","习总来视察");


        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\assain.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }


    /**
     * 需求：freemarker入门案例
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test04() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("ifelse.ftl");

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("flag",3);



        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\ifelse.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }

    /**
     * 需求：freemarker #lsit指令
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test05() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("list.ftl");

        //创建集合，封装数据
        List<Person> pList = new ArrayList<>();

        //创建对象
        Person p1 = new Person();
        p1.setId(1);
        p1.setUsername("赵敏");
        p1.setSex("男");
        p1.setAge("90");
        p1.setAddress("大都");

        Person p2 = new Person();
        p2.setId(2);
        p2.setUsername("老周");
        p2.setSex("女");
        p2.setAge("900");
        p2.setAddress("峨眉山");


        Person p3 = new Person();
        p3.setId(3);
        p3.setUsername("灭绝师太");
        p3.setSex("女");
        p3.setAge("9000");
        p3.setAddress("大都");

        pList.add(p1);
        pList.add(p2);
        pList.add(p3);

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("pList",pList);



        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\list.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }


    /**
     * 需求：freemarker eval内建函数
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test06() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("date.ftl");


        Map maps = new HashMap();
        maps.put("today",new Date());

        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\date.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }

    /**
     * 需求：freemarker 时间内建函数
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     */
    @Test
    public void test07() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("eval.ftl");



        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\eval.html"));

        //生成静态页面
        template.process(null,out);

        out.close();



    }

    /**
     * 需求：freemarker NULL值处理 NULL不处理，将会报错
     * 生成静态页面三要素：
     * 1，模板 （模板扩展名：ftl）
     * 2,准备数据 （在测试中模拟数据）
     * 3,freemarker生成静态页面API
     * 模板数据：
     * 通过模板取值表达式获取值
     * 语法：${}
     * 括号:${key} key就是map的key
     * 处理null值：
     * 1，内建函数
     * 语法：${name?default("默认值")}
     * 2，！
     * 语法：${name!"默认值"}
     * 3，判断
     * 语法：
     * <#if name??>
     *     ${name}
     * </#if>
     */
    @Test
    public void test08() throws Exception{
        //创建freemarker核心配置对象
        Configuration cf = new Configuration(Configuration.getVersion());
        //设置模板所在路径
        cf.setDirectoryForTemplateLoading(new File("C:\\workspace\\heima85\\pyg-html-utils" +
                "\\src\\main\\resources\\template"));
        //设置模板编码
        cf.setDefaultEncoding("utf-8");

        //读取指定路径下的模板文件
        Template template = cf.getTemplate("null.ftl");

        //创建map对象，封装数据 （模拟数据）
        Map maps = new HashMap();
        maps.put("name",null);
        maps.put("message","习总来视察");

        //创建输出流对象，把生成的静态页面写入磁盘
        Writer out = new FileWriter(new File("C:\\workspace\\heima85\\" +
                "pyg-html-utils\\src\\main\\resources\\out\\null.html"));

        //生成静态页面
        template.process(maps,out);

        out.close();



    }
}
