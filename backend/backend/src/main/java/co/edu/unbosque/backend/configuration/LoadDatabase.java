package co.edu.unbosque.backend.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.backend.dto.UserDTO;
import co.edu.unbosque.backend.model.User;
import co.edu.unbosque.backend.service.UserService;

/**
 * Crea al iniciar, si no existen: admin / 1234567890 (ADMIN) y normaluser /
 * 1234567890 (USER). Se crean a traves de UserService, asi el username queda
 * cifrado con AES y la clave con BCrypt.
 */
@Configuration
public class LoadDatabase {

	private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Value("${app.admin.username:admin}")
	private String adminUsername;

	@Value("${app.admin.password:1234567890}")
	private String adminPassword;

	@Value("${app.user.username:normaluser}")
	private String userUsername;

	@Value("${app.user.password:1234567890}")
	private String userPassword;

	@Bean
	CommandLineRunner initDatabase(UserService userService) {
		return args -> {
			if (userService.findUsernameAlreadyTaken(adminUsername)) {
				log.info("Admin already exists,  skipping admin creating  ...");
			} else {
				userService.create(new UserDTO(adminUsername, adminPassword, User.Role.ADMIN));
				log.info("Preloading admin user");
			}

			if (userService.findUsernameAlreadyTaken(userUsername)) {
				log.info("User already exists,  skipping user creating  ...");
			} else {
				userService.create(new UserDTO(userUsername, userPassword, User.Role.USER));
				log.info("Preloading normal user");
			}
		};
	}

}
