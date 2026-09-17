package mk.ukim.finki.dogshelter.service;

import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.Breed;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.repository.AdoptionRequestRepository;
import mk.ukim.finki.dogshelter.repository.DogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DogService {

    private final DogRepository dogRepository;
    private final AdoptionRequestRepository requestRepository;

    public DogService(DogRepository dogRepository, AdoptionRequestRepository requestRepository) {
        this.dogRepository = dogRepository;
        this.requestRepository = requestRepository;
    }

    public List<Dog> search(DogStatus status, Breed breed) {
        if (status != null && breed != null) {
            return dogRepository.findByStatusAndBreedOrderByNameAsc(status, breed);
        }
        if (status != null) {
            return dogRepository.findByStatusOrderByNameAsc(status);
        }
        if (breed != null) {
            return dogRepository.findByBreedOrderByNameAsc(breed);
        }
        return dogRepository.findAllByOrderByNameAsc();
    }

    public List<Dog> findAvailable() {
        return dogRepository.findByStatusOrderByNameAsc(DogStatus.AVAILABLE);
    }

    public Dog findById(Long id) {
        return dogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dog", id));
    }

    @Transactional
    public Dog save(Dog dog) {
        return dogRepository.save(dog);
    }

    @Transactional
    public void delete(Long id) {
        Dog dog = findById(id);
        requestRepository.deleteByDog(dog);
        dogRepository.delete(dog);
    }

    public long count() {
        return dogRepository.count();
    }

    public long countByStatus(DogStatus status) {
        return dogRepository.countByStatus(status);
    }
}
