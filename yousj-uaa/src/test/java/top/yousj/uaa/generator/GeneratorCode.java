package top.yousj.uaa.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import top.yousj.datasource.mapper.ICustomizeBaseMapper;
import top.yousj.datasource.service.CustomizeServiceImpl;
import top.yousj.datasource.service.ICustomizeService;

import java.util.Collections;

public class GeneratorCode {

	public static void main(String[] args) {
		String url = "jdbc:mysql://127.0.0.1:3306/uaa";
		String username = "root";
		String password = "root";
		String dir = "yousj-uaa\\src\\main\\";
		FastAutoGenerator.create(url, username, password)
			.globalConfig(builder -> builder
				.author("yousj")
				.enableSwagger()
				// 指定代码输出目录
				.outputDir(dir + "java"))
			.packageConfig(builder -> builder
				// 设置父包名
				.parent("top.yousj.uaa")
				.entity("entity.po")
				// 设置mapperXml生成路径
				.pathInfo(Collections.singletonMap(OutputFile.xml, dir + "resources\\mapper")))
			.strategyConfig(builder -> builder
				// 设置需要生成的表名
				//.addInclude("uaa_user_data_source", "uaa_auth_url_config")
				.addInclude("uaa_test")
				.entityBuilder()
				//.idType(IdType.ASSIGN_ID)
				.enableLombok()
				.enableTableFieldAnnotation()
				.mapperBuilder()
				.superClass(ICustomizeBaseMapper.class)
				.serviceBuilder()
				.superServiceClass(ICustomizeService.class)
				.superServiceImplClass(CustomizeServiceImpl.class))
			// 禁止生成controller
			.templateConfig(builder -> builder.disable(TemplateType.CONTROLLER))
			// 使用Freemarker引擎模板，默认的是Velocity引擎模板
			.templateEngine(new FreemarkerTemplateEngine())
			.execute();
	}
}
