package top.yousj.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"top.yousj"})
public class YousjUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(YousjUaaApplication.class, args);
	}

}
