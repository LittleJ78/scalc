package local.myproject.scalc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Точка входа в приложение SCalc.
 *
 * @author Evgenii Mironov
 */
@SpringBootApplication
@EnableTransactionManagement
public class ScalcApplication {

	/**
	 * Запускает Spring Boot приложение.
	 *
	 * @param args аргументы командной строки
	 * @return результат не возвращается
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScalcApplication.class, args);
	}

	@Bean
	@Description("Spring Message Resolver")
	/**
	 * Создает источник сообщений для локализации.
	 *
	 * @param args параметры не передаются
	 * @return настроенный источник сообщений
	 */
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		return messageSource;
	}
}
