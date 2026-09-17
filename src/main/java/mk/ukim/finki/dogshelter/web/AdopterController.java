package mk.ukim.finki.dogshelter.web;

import mk.ukim.finki.dogshelter.service.AdopterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/adopters")
public class AdopterController {

    private final AdopterService adopterService;

    public AdopterController(AdopterService adopterService) {
        this.adopterService = adopterService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("adopters", adopterService.findAll());
        return "adopters/list";
    }
}
