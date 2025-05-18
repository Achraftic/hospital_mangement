package ma.achraf.tp3.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import ma.achraf.tp3.entities.Patient;
import ma.achraf.tp3.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {

    @Autowired
    private PatientRepository patientRepository;

    // chercher les patients
    @GetMapping("/home")
    public String index(Model model,
                        @RequestParam(name = "page" ,defaultValue = "0") int page,
                        @RequestParam(name = "size" ,defaultValue = "6") int size,
                        @RequestParam(name = "keyword" ,defaultValue = "") String keyword) {
        Page<Patient> pagePatient = patientRepository.findByNomContains(keyword, PageRequest.of(page, size));
        model.addAttribute("listPatients",pagePatient.getContent());
        model.addAttribute("pages",new int[pagePatient.getTotalPages()] ); //Pagination
        model.addAttribute("currentPage",page);
        model.addAttribute("keyword",keyword);
        return "patients";
    }

    // delete Patient
    @GetMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(Long id, String keyword, int page) {
        patientRepository.deleteById(id);
        return "redirect:/home?page="+page+"&keyword="+keyword;
    }
    @GetMapping("/")
    public String home() {
        return "redirect:/home";
    }

    @GetMapping("/patients")
    @ResponseBody
    public List<Patient> listPatients() {
        return patientRepository.findAll();
    }


    @GetMapping("/formPatient")
    @PreAuthorize("hasRole('ADMIN')")
    public String formPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatient";
    }

    // save Patient
    @PostMapping("/admin/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue ="") String keyword) {
        if (bindingResult.hasErrors()) return "formPatient";
        patientRepository.save(patient);
        return "redirect:/home?page="+page+"&keyword="+keyword;
    }

    // editPatient

    @GetMapping("/admin/editPatient")
    @PreAuthorize("hasRole('ADMIN')")
    public String editPatient(Model model, Long id, String keyword, int page) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient",patient);
        model.addAttribute("page",page);
        model.addAttribute("keyword",keyword);
        return "editPatient";
    }

}
