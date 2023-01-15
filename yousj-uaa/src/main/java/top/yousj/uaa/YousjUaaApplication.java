package top.yousj.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("top.yousj.uaa.mapper")
@SpringBootApplication(scanBasePackages = {"top.yousj"})
public class YousjUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YousjUaaApplication.class, args);
	}

}
