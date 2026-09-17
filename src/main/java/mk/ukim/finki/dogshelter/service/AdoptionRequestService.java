package mk.ukim.finki.dogshelter.service;

import mk.ukim.finki.dogshelter.model.AdoptionRequest;
import mk.ukim.finki.dogshelter.model.Adopter;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.model.enums.RequestStatus;
import mk.ukim.finki.dogshelter.repository.AdoptionRequestRepository;
import mk.ukim.finki.dogshelter.repository.DogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdoptionRequestService {

    private final AdoptionRequestRepository requestRepository;
    private final DogRepository dogRepository;
    private final DogService dogService;
    private final AdopterService adopterService;

    public AdoptionRequestService(AdoptionRequestRepository requestRepository,
                                  DogRepository dogRepository,
                                  DogService dogService,
                                  AdopterService adopterService) {
        this.requestRepository = requestRepository;
        this.dogRepository = dogRepository;
        this.dogService = dogService;
        this.adopterService = adopterService;
    }

    public List<AdoptionRequest> search(RequestStatus status) {
        return status == null
                ? requestRepository.findAllByOrderBySubmittedAtDesc()
                : requestRepository.findByStatusOrderBySubmittedAtDesc(status);
    }

    public AdoptionRequest findById(Long id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoption request", id));
    }

    public long countByStatus(RequestStatus status) {
        return requestRepository.countByStatus(status);
    }

    @Transactional
    public AdoptionRequest create(Long dogId, Long adopterId, String message) {
        Dog dog = dogService.findById(dogId);
        Adopter adopter = adopterService.findById(adopterId);

        if (dog.getStatus() != DogStatus.AVAILABLE) {
            throw new AdoptionRuleViolationException(
                    dog.getName() + " is not available for adoption (current status: "
                            + dog.getStatus().getDisplayName() + ").");
        }
        if (requestRepository.existsByDogAndAdopterIdAndStatus(dog, adopterId, RequestStatus.PENDING)) {
            throw new AdoptionRuleViolationException(
                    adopter.getFullName() + " already has a pending request for " + dog.getName() + ".");
        }

        return requestRepository.save(new AdoptionRequest(dog, adopter, message));
    }

    @Transactional
    public AdoptionRequest update(Long id, Long dogId, Long adopterId, String message) {
        AdoptionRequest request = findById(id);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AdoptionRuleViolationException("Only pending requests can be edited.");
        }

        Dog dog = dogService.findById(dogId);
        if (!dog.getId().equals(request.getDog().getId()) && dog.getStatus() != DogStatus.AVAILABLE) {
            throw new AdoptionRuleViolationException(
                    dog.getName() + " is not available for adoption (current status: "
                            + dog.getStatus().getDisplayName() + ").");
        }

        request.setDog(dog);
        request.setAdopter(adopterService.findById(adopterId));
        request.setMessage(message);
        return requestRepository.save(request);
    }

    @Transactional
    public AdoptionRequest approve(Long id) {
        AdoptionRequest request = findById(id);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AdoptionRuleViolationException(
                    "This request was already " + request.getStatus().getDisplayName().toLowerCase() + ".");
        }

        Dog dog = request.getDog();
        LocalDateTime now = LocalDateTime.now();

        List<AdoptionRequest> competing = requestRepository.findByDogAndStatus(dog, RequestStatus.PENDING);
        for (AdoptionRequest other : competing) {
            if (!other.getId().equals(request.getId())) {
                other.setStatus(RequestStatus.REJECTED);
                other.setDecidedAt(now);
            }
        }
        requestRepository.saveAll(competing);

        request.setStatus(RequestStatus.APPROVED);
        request.setDecidedAt(now);

        dog.setStatus(DogStatus.ADOPTED);
        dogRepository.save(dog);

        return requestRepository.save(request);
    }

    @Transactional
    public AdoptionRequest reject(Long id) {
        AdoptionRequest request = findById(id);

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new AdoptionRuleViolationException(
                    "This request was already " + request.getStatus().getDisplayName().toLowerCase() + ".");
        }

        request.setStatus(RequestStatus.REJECTED);
        request.setDecidedAt(LocalDateTime.now());
        return requestRepository.save(request);
    }

    @Transactional
    public void delete(Long id) {
        requestRepository.delete(findById(id));
    }
}
