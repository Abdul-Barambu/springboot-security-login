package com.abdul.SpringSecurityLogin;

import com.abdul.SpringSecurityLogin.entities.Role;
import com.abdul.SpringSecurityLogin.entities.User;
import com.abdul.SpringSecurityLogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringSecurityLoginApplication implements CommandLineRunner {  //create admin login, impl command line runner

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityLoginApplication.class, args);
	}

//	call the method created in user repo FINDBYROLE and pass the role using the user class and then check if it is there, if not create admin
	public void run(String... args){
		User adminAccount = userRepository.findByRole(Role.ADMIN);
		if(adminAccount == null){
			User user = new User();
			user.setEmail("admin@gmail.com");
			user.setFirstName("Admin");
			user.setLastName("Admin");
			user.setRole(Role.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin123"));

			userRepository.save(user);
		}
	}
}
