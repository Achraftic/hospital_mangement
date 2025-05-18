package ma.achraf.tp3.security.service;

import lombok.AllArgsConstructor;
import ma.achraf.tp3.security.entities.AppRole;
import ma.achraf.tp3.security.entities.AppUser;
import ma.achraf.tp3.security.repository.AppRoleRepository;
import ma.achraf.tp3.security.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private AppUserRepository appUserRepository;
    private AppRoleRepository appRoleRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public AppUser addNewUser(String userName, String password, String confirmPassword, String email) {
        AppUser appUser = appUserRepository.findByUserName(userName);
        if (appUser != null) throw new RuntimeException("User already exist !");
        if (!password.equals(confirmPassword)) throw new RuntimeException("Passwords do not match !");
        appUser = AppUser.builder()
                .userId(UUID.randomUUID().toString())
                .userName(userName)
                .password(passwordEncoder.encode(password))
                .email(email)
                .build();
        AppUser savedAppUser = appUserRepository.save(appUser);
        return savedAppUser;
    }

    @Override
    public AppRole addNewRole(String role) {
        AppRole appRole= appRoleRepository.findById(role).orElse(null);
        if (appRole != null) throw new RuntimeException("Role already exist !");
        appRole=AppRole.builder()
                .role(role)
                .build();
        return appRoleRepository.save(appRole);
    }

    @Override
    public void addRoleToUser(String userName, String role) {
        AppUser appUser = appUserRepository.findByUserName(userName);
        if (appUser == null) throw new RuntimeException("User already exist !");
        AppRole appRole = appRoleRepository.findById(role).orElse(null);
        if (appRole == null) throw new RuntimeException("Role already exist !");
        appUser.getRoles().add(appRole);


    }

    @Override
    public void removeRoleFromUser(String userName, String role) {
        AppUser appUser = appUserRepository.findByUserName(userName);
        AppRole appRole = appRoleRepository.findById(role).get();
        appUser.getRoles().remove(appRole);

    }

    @Override
    public AppUser loadUserByUsername(String username) {
        return appUserRepository.findByUserName(username);
    }
}
