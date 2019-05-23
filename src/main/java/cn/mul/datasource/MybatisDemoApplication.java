package cn.mul.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
@MapperScan("cn.mul.datasource.dao")
@SpringBootApplication
public class MybatisDemoApplication {

	private static Logger logger= LoggerFactory.getLogger(MybatisDemoApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MybatisDemoApplication.class);
		Environment env = app.run(args).getEnvironment();
		try {
			logger.info("\n----------------------------------------------------------\n\t" +
							"Application '{}' is running! Access URLs:\n\t" +
							"Local: \t\thttp://localhost:{}\n\t" +
							"External: \thttp://{}:{}\n\t" +
							"SwaggerUI: \thttp://localhost:{}/swagger-ui.html\n" +
							"----------------------------------------------------------",
					env.getProperty("spring.application.name"),
					env.getProperty("server.port"),
					InetAddress.getLocalHost().getHostAddress(),
					env.getProperty("server.port"),
					env.getProperty("server.port"));
		} catch (UnknownHostException e) {
			logger.error(e.getMessage(),e);
		}
	}

}
