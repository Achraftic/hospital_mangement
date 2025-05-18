package ma.achraf.tp3.security.repository;

import ma.achraf.tp3.security.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRoleRepository extends JpaRepository <AppRole, String> {
}
