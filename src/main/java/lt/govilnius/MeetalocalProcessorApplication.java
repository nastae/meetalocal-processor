package lt.govilnius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MeetalocalProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeetalocalProcessorApplication.class, args);
	}

}
