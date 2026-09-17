package mk.ukim.finki.dogshelter.service;

import mk.ukim.finki.dogshelter.model.Adopter;
import mk.ukim.finki.dogshelter.repository.AdopterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdopterService {

    private final AdopterRepository adopterRepository;

    public AdopterService(AdopterRepository adopterRepository) {
        this.adopterRepository = adopterRepository;
    }

    public List<Adopter> findAll() {
        return adopterRepository.findAllByOrderByFullNameAsc();
    }

    public Adopter findById(Long id) {
        return adopterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adopter", id));
    }
}
