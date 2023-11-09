package may.yuntian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 管理系统启动类入口
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020-08-08
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableScheduling //开启定时任务
public class AnlianApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AnlianApplication.class, args);
		System.out.println("云天开发平台 项目启动成功......");
	}
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(AnlianApplication.class);
	}
}
