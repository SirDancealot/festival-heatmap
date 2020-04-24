package Gruppe11.RoskildeApp;

import Service.FirebaseInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoskildeAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoskildeAppApplication.class, args);
		new FirebaseInit().init();
	}
}
