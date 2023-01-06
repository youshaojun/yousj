package top.yousj.reload.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/***
 * @author yousj
 * @since 2023-01-05
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile({"dev", "test"})
public class MapperReloadService implements InitializingBean {

	private final SqlSessionFactory sqlSessionFactory;

	private volatile Configuration configuration;

	@Override
	public void afterPropertiesSet() {
		configuration = sqlSessionFactory.getConfiguration();
	}

	/**
	 * 重新加载sql.xml
	 *
	 * @param file 修改的xml资源
	 */
	public void reloadXml(File file) {
		log.info("需要重新加载的文件: {}", file);
		try {
			clearMap(getNamespace(file));
			clearSet(file.getAbsolutePath());
			XMLMapperBuilder xmlMapperBuilder =
				new XMLMapperBuilder(new FileInputStream(file), getTarConfiguration(), file.toString(),
					getTarConfiguration().getSqlFragments());
			xmlMapperBuilder.parse();
		} catch (Exception e) {
			log.error("ERROR: 重新加载[{}]失败", file.toString(), e);
			throw new RuntimeException("ERROR: 重新加载[" + file.toString() + "]失败", e);
		} finally {
			ErrorContext.instance().reset();
		}
		log.info("成功热部署文件列表: {}", file);
	}

	private Configuration getTarConfiguration() {
		return configuration;
	}

	/**
	 * 删除xml元素的节点缓存
	 *
	 * @param nameSpace xml中命名空间
	 */
	private void clearMap(String nameSpace) {
		log.info("清理Mybatis的namespace={}在mappedStatements、caches、resultMaps、parameterMaps、keyGenerators、sqlFragments中的缓存");
		Arrays.asList("mappedStatements", "caches", "resultMaps", "parameterMaps", "keyGenerators", "sqlFragments")
			.forEach(fieldName -> {
				Object value = getFieldValue(getTarConfiguration(), fieldName);
				if (value instanceof Map) {
					Map<?, ?> map = (Map) value;
					List<Object> list = map.keySet().stream().filter(o -> o.toString().startsWith(nameSpace + "."))
						.collect(Collectors.toList());
					log.info("需要清理的元素: {}", list);
					list.forEach(k -> map.remove((Object) k));
				}
			});
	}

	/**
	 * 清除文件记录缓存
	 *
	 * @param resource xml文件路径
	 */
	private void clearSet(String resource) {
		log.info("清理mybatis的资源{}在容器中的缓存", resource);
		Object value = getFieldValue(getTarConfiguration(), "loadedResources");
		if (value instanceof Set) {
			Set<?> set = (Set) value;
			set.remove(resource);
			set.remove("namespace:" + resource);
		}
	}

	/**
	 * 获取xml的namespace
	 *
	 * @param file xml资源
	 * @return java.lang.String
	 */
	private String getNamespace(File file) {
		try {
			XPathParser parser = new XPathParser(new FileInputStream(file), true, null, new XMLMapperEntityResolver());
			return parser.evalNode("/mapper").getStringAttribute("namespace");
		} catch (Exception e) {
			log.error("ERROR: 解析xml中namespace失败", e);
			throw new RuntimeException("ERROR: 解析xml中namespace失败", e);
		}
	}

	private Object getFieldValue(Object obj, String fieldName) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			return null;
		}
	}
}

