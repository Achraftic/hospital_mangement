package ma.achraf.tp3.security.service;

import ma.achraf.tp3.security.entities.AppRole;
import ma.achraf.tp3.security.entities.AppUser;

public interface AccountService {
    AppUser addNewUser(String userName, String password ,String confirmPassword, String email);
    AppRole addNewRole(String role);
    void addRoleToUser(String userName, String role);
    void removeRoleFromUser(String userName, String role);
    AppUser loadUserByUsername(String userName);
}
