package top.yousj.swagger.config;

import io.swagger.annotations.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Response;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import top.yousj.commons.enums.ResultCode;
import top.yousj.swagger.entity.SwaggerGroups;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yousj
 * @since 2022-12-28
 */
@Configuration(proxyBeanMethods = false)
public class SwaggerDocket {

	@Bean
	public Docket defaultDocket() {
		SwaggerGroups.SwaggerGroup swaggerGroup = new SwaggerGroups.SwaggerGroup();
		swaggerGroup.setGroupName("a.全部");
		return SwaggerDocket.of(swaggerGroup);
	}

	public static Docket of(SwaggerGroups.SwaggerGroup swaggerGroup) {
		List<Response> responses = Arrays.stream(ResultCode.values())
			.map(e -> new Response(String.valueOf(e.getCode()), e.getValue(), false, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()))
			.collect(Collectors.toList());
		HttpMethod[] httpMethods = HttpMethod.values();
		Docket docket = new Docket(DocumentationType.OAS_30);
		for (HttpMethod httpMethod : httpMethods) {
			docket.globalResponses(httpMethod, responses);
		}
		return docket
			.apiInfo(new ApiInfoBuilder()
				.title(swaggerGroup.getTitle())
				.description(swaggerGroup.getDescription())
				.termsOfServiceUrl(swaggerGroup.getUrl())
				.version(swaggerGroup.getVersion())
				.contact(new Contact(swaggerGroup.getContactName(), swaggerGroup.getUrl(), swaggerGroup.getContactEmail()))
				.build())
			.select()
			.apis(Objects.isNull(swaggerGroup.getBasePackage()) ?
				RequestHandlerSelectors.withClassAnnotation(Api.class) :
				RequestHandlerSelectors.basePackage(swaggerGroup.getBasePackage()))
			.paths(PathSelectors.any()).build()
			.forCodeGeneration(true)
			.groupName(swaggerGroup.getGroupName());
	}

}
