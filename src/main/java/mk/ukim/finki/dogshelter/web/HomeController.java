package mk.ukim.finki.dogshelter.web;

import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.model.enums.RequestStatus;
import mk.ukim.finki.dogshelter.service.AdoptionRequestService;
import mk.ukim.finki.dogshelter.service.DogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DogService dogService;
    private final AdoptionRequestService requestService;

    public HomeController(DogService dogService, AdoptionRequestService requestService) {
        this.dogService = dogService;
        this.requestService = requestService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("totalDogs", dogService.count());
        model.addAttribute("availableDogs", dogService.countByStatus(DogStatus.AVAILABLE));
        model.addAttribute("adoptedDogs", dogService.countByStatus(DogStatus.ADOPTED));
        model.addAttribute("pendingRequests", requestService.countByStatus(RequestStatus.PENDING));
        model.addAttribute("availableList", dogService.findAvailable());
        model.addAttribute("recentRequests", requestService.search(null).stream().limit(5).toList());
        return "index";
    }
}
