package mk.ukim.finki.dogshelter.web;

import mk.ukim.finki.dogshelter.model.Shelter;
import mk.ukim.finki.dogshelter.service.ShelterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shelters")
public class ShelterController {

    private final ShelterService shelterService;

    public ShelterController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    @GetMapping
    public String list(Model model) {
        List<Shelter> shelters = shelterService.findAll();
        Map<Long, Long> dogCounts = new LinkedHashMap<>();
        for (Shelter shelter : shelters) {
            dogCounts.put(shelter.getId(), shelterService.countDogs(shelter));
        }
        model.addAttribute("shelters", shelters);
        model.addAttribute("dogCounts", dogCounts);
        return "shelters/list";
    }
}
