package org.springblade.common.config;

import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.swagger.EnableSwagger;
import org.springblade.core.swagger.SwaggerProperties;
import org.springblade.core.swagger.SwaggerUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

/**
 * Swagger配置类
 * @author Eugene-Forest
 * @date 2021/11/17
 **/
@Configuration
@EnableSwagger
@AllArgsConstructor
public class SwaggerConfiguration {

	/**
	 * 引入swagger配置类
	 */
	private final SwaggerProperties swaggerProperties;

	/**
	 * 引入Knife4j扩展类
	 */
	private final OpenApiExtensionResolver openApiExtensionResolver;

	/**
	 * 请求头部信息
	 */
	private final ApiKey clientInfo=new ApiKey("ClientInfo", "Authorization", "header");
	private final ApiKey bladeAuth= new ApiKey("BladeAuth", "Blade-Auth", "header");


	@Bean
	public Docket testDocket() {
		return docket("测试模块", Collections.singletonList(AppConstant.BASE_PACKAGES + ".test"));
	}


	/**
	 * @param groupName 模块名
	 * @param basePackages 模块包的根目录
	 */
	private Docket docket(String groupName, List<String> basePackages) {
		return new Docket(DocumentationType.SWAGGER_2)
			.groupName(groupName)
			.apiInfo(apiInfo())
			.select()
			.apis(SwaggerUtil.basePackages(basePackages))
			.paths(PathSelectors.any())
			.build()
			.securitySchemes(Lists.<SecurityScheme>newArrayList(
				clientInfo,bladeAuth))
			.extensions(openApiExtensionResolver.buildExtensions(groupName));
	}

	/**
	 * 获取文档主页的基本信息
	 * @return
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
			.title(swaggerProperties.getTitle())
			.description(swaggerProperties.getDescription())
			.license(swaggerProperties.getLicense())
			.licenseUrl(swaggerProperties.getLicenseUrl())
			.termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
			.contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
			.version(swaggerProperties.getVersion())
			.build();
	}

}
