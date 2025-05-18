package ma.achraf.tp3;

import ma.achraf.tp3.entities.Patient;
import ma.achraf.tp3.repository.PatientRepository;
import ma.achraf.tp3.security.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import java.util.Date;

@SpringBootApplication
public class HopitalApplication implements CommandLineRunner {

    @Autowired
    private PatientRepository patientRepository;

    public static void main(String[] args) {
        SpringApplication.run(HopitalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
       // patientRepository.save(new Patient(null, "Achraf","Tichirra", new Date(), false,93));


        Patient patient=Patient.builder()
                .nom("Achrf")
                .prenom("Tichira")
                .dateNaissance(new Date())
                .score(56)
                .malade(true)
                .build();
    }

   // @Bean
    CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
        PasswordEncoder passwordEncoder = passwordEncoder();
        return args -> {

            UserDetails admin11 = jdbcUserDetailsManager.loadUserByUsername("admin");
            if(admin11==null)
                jdbcUserDetailsManager.createUser(
                    User.withUsername("admin").password(passwordEncoder.encode("1234")).roles("USER","ADMIN").build()
            );
            UserDetails user1 = jdbcUserDetailsManager.loadUserByUsername("achraf");
            if(user1==null)
                jdbcUserDetailsManager.createUser(
                        User.withUsername("achraf").password(passwordEncoder.encode("1234")).roles("USER").build()
                );
        };
    }
    @Bean
    CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
        return args -> {
            try {
                accountService.addNewRole("USER");
            } catch (RuntimeException e) {
                System.out.println("Role USER already exists.");
            }

            try {
                accountService.addNewRole("ADMIN");
            } catch (RuntimeException e) {
                System.out.println("Role ADMIN already exists.");
            }

            if (accountService.loadUserByUsername("achraf") == null)
                accountService.addNewUser("achraf", "1234", "1234", "achraf@gmail.com");



            if (accountService.loadUserByUsername("admin") == null)
                accountService.addNewUser("admin", "1234", "1234", "admin@gmail.com");

            accountService.addRoleToUser("achraf", "USER");

            accountService.addRoleToUser("admin", "USER");
            accountService.addRoleToUser("admin", "ADMIN");
        };
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
