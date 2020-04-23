package Gruppe11.RoskildeApp;

import Service.FirebaseInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class RoskildeAppApplication {

	public static void main(String[] args) {
		//SpringApplication.run(RoskildeAppApplication.class, args);
		FirebaseInit init = new FirebaseInit();
		init.init();
		while (true) {}
	}
}
