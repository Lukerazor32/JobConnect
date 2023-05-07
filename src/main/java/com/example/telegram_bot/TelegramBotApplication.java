package com.example.telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class TelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

//	@GetMapping("/auth")
//	public String oauth2Callback(@RequestParam("code") String authorizationCode) {
//		// Обработка полученного authorizationCode и выполнение запроса на обмен кода на токены
//		// ...
//		System.out.println(authorizationCode);
//		return "Авторизация прошла успешно!";
//	}

}
