package ma.achraf.tp3.security.repository;

import ma.achraf.tp3.security.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {

    // Methode qui permet de chercher un utilisateur
    AppUser findByUserName(String userName);
}
