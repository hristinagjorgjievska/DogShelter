package mk.ukim.finki.dogshelter.web;

import jakarta.validation.Valid;
import mk.ukim.finki.dogshelter.model.AdoptionRequest;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.RequestStatus;
import mk.ukim.finki.dogshelter.service.AdopterService;
import mk.ukim.finki.dogshelter.service.AdoptionRequestService;
import mk.ukim.finki.dogshelter.service.AdoptionRuleViolationException;
import mk.ukim.finki.dogshelter.service.DogService;
import mk.ukim.finki.dogshelter.web.form.AdoptionRequestForm;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/requests")
public class AdoptionRequestController {

    private final AdoptionRequestService requestService;
    private final DogService dogService;
    private final AdopterService adopterService;

    public AdoptionRequestController(AdoptionRequestService requestService,
                                     DogService dogService,
                                     AdopterService adopterService) {
        this.requestService = requestService;
        this.dogService = dogService;
        this.adopterService = adopterService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) RequestStatus status, Model model) {
        model.addAttribute("requests", requestService.search(status));
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", RequestStatus.values());
        return "requests/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long dogId, Model model) {
        AdoptionRequestForm form = new AdoptionRequestForm();
        form.setDogId(dogId);
        model.addAttribute("form", form);
        populateOptions(model, null);
        return "requests/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("form") AdoptionRequestForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            populateOptions(model, null);
            return "requests/form";
        }
        try {
            requestService.create(form.getDogId(), form.getAdopterId(), form.getMessage());
        } catch (AdoptionRuleViolationException e) {
            model.addAttribute("error", e.getMessage());
            populateOptions(model, null);
            return "requests/form";
        }
        redirect.addFlashAttribute("success", "Adoption request submitted.");
        return "redirect:/requests";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        AdoptionRequest request = requestService.findById(id);
        model.addAttribute("form", AdoptionRequestForm.from(request));
        model.addAttribute("requestId", id);
        populateOptions(model, request.getDog());
        return "requests/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("form") AdoptionRequestForm form,
                         BindingResult binding,
                         Model model,
                         RedirectAttributes redirect) {
        Dog currentDog = requestService.findById(id).getDog();
        if (binding.hasErrors()) {
            model.addAttribute("requestId", id);
            populateOptions(model, currentDog);
            return "requests/form";
        }
        try {
            requestService.update(id, form.getDogId(), form.getAdopterId(), form.getMessage());
        } catch (AdoptionRuleViolationException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("requestId", id);
            populateOptions(model, currentDog);
            return "requests/form";
        }
        redirect.addFlashAttribute("success", "Adoption request updated.");
        return "redirect:/requests";
    }

    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            AdoptionRequest request = requestService.approve(id);
            redirect.addFlashAttribute("success",
                    request.getDog().getName() + " was adopted by " + request.getAdopter().getFullName()
                            + ". Other pending requests for this dog were closed.");
        } catch (AdoptionRuleViolationException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/requests";
    }

    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            AdoptionRequest request = requestService.reject(id);
            redirect.addFlashAttribute("success",
                    "Request from " + request.getAdopter().getFullName() + " for "
                            + request.getDog().getName() + " was rejected.");
        } catch (AdoptionRuleViolationException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/requests";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {
        requestService.delete(id);
        redirect.addFlashAttribute("success", "Adoption request deleted.");
        return "redirect:/requests";
    }

    private void populateOptions(Model model, Dog currentDog) {
        List<Dog> dogs = new ArrayList<>(dogService.findAvailable());
        if (currentDog != null && dogs.stream().noneMatch(d -> d.getId().equals(currentDog.getId()))) {
            dogs.add(0, currentDog);
        }
        model.addAttribute("dogs", dogs);
        model.addAttribute("adopters", adopterService.findAll());
    }
}
