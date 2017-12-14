package com.sten.framework.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Created by linkai on 2017/4/27.
 */
@Service
@Configuration
@PropertySource(value = "classpath*:/globals.properties")
public class PathConfigurationUtil {

	@Value("${PATH.ESAPI_VALID_ROOT}")
	private String ESAPI_VALID_ROOT;

	@Value("${PATH.UPLOAD_FOLDER_ROOT}")
	private String UPLOAD_FOLDER_ROOT;

	public String getESAPI_VALID_ROOT() {
		return ESAPI_VALID_ROOT;
	}

	public void setESAPI_VALID_ROOT(String ESAPI_VALID_ROOT) {
		this.ESAPI_VALID_ROOT = ESAPI_VALID_ROOT;
	}

	public String getUPLOAD_FOLDER_ROOT() {
		return UPLOAD_FOLDER_ROOT;
	}

	public void setUPLOAD_FOLDER_ROOT(String UPLOAD_FOLDER_ROOT) {
		this.UPLOAD_FOLDER_ROOT = UPLOAD_FOLDER_ROOT;
	}

	// @Bean
	// public static PropertySourcesPlaceholderConfigurer propertyConfigInDev()
	// {
	// return new PropertySourcesPlaceholderConfigurer();
	// }

	// @Bean
	// public static PropertySourcesPlaceholderConfigurer
	// propertySourcesPlaceholderConfigurer() {
	// return new PropertySourcesPlaceholderConfigurer();
	// }
}
