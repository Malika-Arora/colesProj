package com.coles.musicbrainz.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@SpringBootApplication(scanBasePackages = { "com.coles.musicbrainz" })
@EntityScan(basePackages = { "com.coles.musicbrainzd.models" })
public class MusicBrainzServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MusicBrainzServiceApplication.class, args);
	}
	@Bean
	public OpenAPI customOpenAPI(@Value("${application-description}") String appDesciption,
			@Value("${build.version}") String appVersion) {
		return new OpenAPI().info(new Info().title("MusicBrains service").version(appVersion).description(appDesciption)
				.termsOfService("http://swagger.io/terms/")
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

}
