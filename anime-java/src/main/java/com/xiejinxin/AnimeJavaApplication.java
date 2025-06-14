package com.xiejinxin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//扫描mapper包
@MapperScan("com.xiejinxin.mapper")
@EnableScheduling
public class AnimeJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimeJavaApplication.class, args);
	}

}
