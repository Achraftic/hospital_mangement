package ma.achraf.tp3.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
// Entite JPA Patient
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Le nom est obligatoire")
    @Size(min = 3, max = 40, message = "Le nom doit avoir entre 3 et 40 caractères")
    private String nom;

    @NotEmpty(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 40, message = "Le prénom doit avoir entre 2 et 40 caractères")
    private String prenom;

    @NotNull(message = "La date de naissance est obligatoire")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateNaissance;

    private boolean malade;

    @DecimalMin(value = "50", message = "Le score doit être au minimum 50")
    private int score;
}
