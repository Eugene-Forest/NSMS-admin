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
import org.springframework.context.annotation.Profile;
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
	 * 定义请求需要的请求头
	 */
	private final ApiKey clientInfo=new ApiKey("ClientInfo", "Authorization", "header");
	private final ApiKey bladeAuth= new ApiKey("BladeAuth", "Blade-Auth", "header");
	private final ApiKey bladeTenant=new ApiKey("TenantId", "Tenant-Id", "header");



	@Bean
	@Profile({"dev","test"})
	public Docket testDocket() {
		return docket("测试模块", Collections.singletonList(AppConstant.BASE_PACKAGES + ".test"));
	}

	@Bean
	@Profile({"test"})
	public Docket testDocket2() {
		return docket("测试模块2", Collections.singletonList(AppConstant.BASE_PACKAGES + ".test"));
	}

	@Bean
	@Profile({"!prod"})
	public Docket testDocket3() {
		return docket("测试模块3_非生产环境", Collections.singletonList(AppConstant.BASE_PACKAGES + ".test"));
	}

	/**
	 * 模块的基本配置
	 *
	 * @param groupName 模块名
	 * @param basePackages 模块包的根目录
	 */
	private Docket docket(String groupName, List<String> basePackages) {
		// 接口文档的类型默认为 swagger version 2
		return new Docket(DocumentationType.SWAGGER_2)
			// 模块名定义
			.groupName(groupName)
			// 接口文档基本信息配置
			.apiInfo(apiInfo())
			// 扫描模块包的根目录下的接口
			.select()
			.apis(SwaggerUtil.basePackages(basePackages))
			.paths(PathSelectors.any())
			//构建
			.build()
			//添加请求需要的头部信息
			.securitySchemes(Lists.<SecurityScheme>newArrayList(clientInfo,bladeAuth,bladeTenant))
			// 使用Knife4j扩展
			.extensions(openApiExtensionResolver.buildExtensions(groupName));
	}

	/**
	 * swagger 文档主页的基本信息的配置，相关配置可以在项目运行配置文件中修改
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
