package mk.ukim.finki.dogshelter.web;

import jakarta.validation.Valid;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.Breed;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.model.enums.Sex;
import mk.ukim.finki.dogshelter.service.DogService;
import mk.ukim.finki.dogshelter.service.ShelterService;
import mk.ukim.finki.dogshelter.web.form.DogForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dogs")
public class DogController {

    private final DogService dogService;
    private final ShelterService shelterService;

    public DogController(DogService dogService, ShelterService shelterService) {
        this.dogService = dogService;
        this.shelterService = shelterService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) DogStatus status,
                       @RequestParam(required = false) Breed breed,
                       Model model) {
        model.addAttribute("dogs", dogService.search(status, breed));
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedBreed", breed);
        model.addAttribute("statuses", DogStatus.values());
        model.addAttribute("breeds", Breed.values());
        model.addAttribute("totalCount", dogService.count());
        model.addAttribute("availableCount", dogService.countByStatus(DogStatus.AVAILABLE));
        model.addAttribute("underCareCount", dogService.countByStatus(DogStatus.UNDER_CARE));
        model.addAttribute("adoptedCount", dogService.countByStatus(DogStatus.ADOPTED));
        return "dogs/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("form", new DogForm());
        populateOptions(model);
        return "dogs/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") DogForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            populateOptions(model);
            return "dogs/form";
        }
        Dog dog = new Dog();
        form.applyTo(dog);
        dog.setShelter(shelterService.findById(form.getShelterId()));
        dogService.save(dog);
        redirect.addFlashAttribute("success", dog.getName() + " was added to the shelter.");
        return "redirect:/dogs";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("form", DogForm.from(dogService.findById(id)));
        model.addAttribute("dogId", id);
        populateOptions(model);
        return "dogs/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") DogForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("dogId", id);
            populateOptions(model);
            return "dogs/form";
        }
        Dog dog = dogService.findById(id);
        form.applyTo(dog);
        dog.setShelter(shelterService.findById(form.getShelterId()));
        dogService.save(dog);
        redirect.addFlashAttribute("success", dog.getName() + " was updated.");
        return "redirect:/dogs";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        String name = dogService.findById(id).getName();
        dogService.delete(id);
        redirect.addFlashAttribute("success", name + " was removed.");
        return "redirect:/dogs";
    }

    private void populateOptions(Model model) {
        model.addAttribute("breeds", Breed.values());
        model.addAttribute("sexes", Sex.values());
        model.addAttribute("statuses", DogStatus.values());
        model.addAttribute("shelters", shelterService.findAll());
    }
}
