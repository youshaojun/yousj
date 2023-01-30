package top.yousj.datasource.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.yousj.datasource.handler.DateFillMetaObjectHandler;
import top.yousj.datasource.injector.methods.InsertOnDuplicateKeyUpdate;
import top.yousj.datasource.injector.methods.ReplaceInto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yousj
 * @since 2023-01-30
 */
@Configuration(proxyBeanMethods = false)
public class MybatisPlusConfig {

	@Bean
	@ConditionalOnMissingBean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
		mybatisPlusInterceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
		mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
		return mybatisPlusInterceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public MetaObjectHandler fillMetaObjectHandler() {
		return new DateFillMetaObjectHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public DefaultSqlInjector defaultSqlInjector() {
		return new DefaultSqlInjector() {
			@Override
			public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
				List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
				List<AbstractMethod> customizeMethods = Stream.of(new ReplaceInto(), new InsertOnDuplicateKeyUpdate()).collect(Collectors.toList());
				methodList.addAll(customizeMethods);
				return methodList;
			}
		};
	}

}
