package mk.ukim.finki.dogshelter.service;

import mk.ukim.finki.dogshelter.model.Shelter;
import mk.ukim.finki.dogshelter.repository.DogRepository;
import mk.ukim.finki.dogshelter.repository.ShelterRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final DogRepository dogRepository;

    public ShelterService(ShelterRepository shelterRepository, DogRepository dogRepository) {
        this.shelterRepository = shelterRepository;
        this.dogRepository = dogRepository;
    }

    public List<Shelter> findAll() {
        return shelterRepository.findAllByOrderByNameAsc();
    }

    public Shelter findById(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelter", id));
    }

    public long countDogs(Shelter shelter) {
        return dogRepository.countByShelter(shelter);
    }
}
