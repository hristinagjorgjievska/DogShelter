package mk.ukim.finki.dogshelter.service;

import mk.ukim.finki.dogshelter.model.AdoptionRequest;
import mk.ukim.finki.dogshelter.model.Adopter;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.Shelter;
import mk.ukim.finki.dogshelter.model.enums.Breed;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.model.enums.RequestStatus;
import mk.ukim.finki.dogshelter.model.enums.Sex;
import mk.ukim.finki.dogshelter.repository.AdopterRepository;
import mk.ukim.finki.dogshelter.repository.AdoptionRequestRepository;
import mk.ukim.finki.dogshelter.repository.DogRepository;
import mk.ukim.finki.dogshelter.repository.ShelterRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AdoptionRequestServiceTest {

    @Autowired
    private AdoptionRequestService requestService;
    @Autowired
    private ShelterRepository shelterRepository;
    @Autowired
    private DogRepository dogRepository;
    @Autowired
    private AdopterRepository adopterRepository;
    @Autowired
    private AdoptionRequestRepository requestRepository;

    @Test
    void approvingARequestAdoptsTheDogAndRejectsCompetingRequests() {
        Shelter shelter = shelterRepository.save(new Shelter("Test Shelter", "Skopje", null, null, 10));
        Dog dog = dogRepository.save(new Dog("Rex", Breed.BEAGLE, Sex.MALE, LocalDate.of(2022, 1, 1), 12.0,
                DogStatus.AVAILABLE, null, shelter));
        Adopter ana = adopterRepository.save(new Adopter("Ana", "ana.approve@test.mk", null, "Skopje"));
        Adopter marko = adopterRepository.save(new Adopter("Marko", "marko.approve@test.mk", null, "Bitola"));

        AdoptionRequest winner = requestService.create(dog.getId(), ana.getId(), "First!");
        AdoptionRequest competitor = requestService.create(dog.getId(), marko.getId(), "Me too");

        requestService.approve(winner.getId());

        assertEquals(DogStatus.ADOPTED, dogRepository.findById(dog.getId()).orElseThrow().getStatus());
        assertEquals(RequestStatus.APPROVED, requestRepository.findById(winner.getId()).orElseThrow().getStatus());
        assertEquals(RequestStatus.REJECTED, requestRepository.findById(competitor.getId()).orElseThrow().getStatus());
    }

    @Test
    void cannotRequestADogThatIsNotAvailable() {
        Shelter shelter = shelterRepository.save(new Shelter("Test Shelter 2", "Bitola", null, null, 10));
        Dog adopted = dogRepository.save(new Dog("Luna", Breed.POODLE, Sex.FEMALE, LocalDate.of(2021, 6, 1), 8.0,
                DogStatus.ADOPTED, null, shelter));
        Adopter filip = adopterRepository.save(new Adopter("Filip", "filip.unavailable@test.mk", null, "Ohrid"));

        assertThrows(AdoptionRuleViolationException.class,
                () -> requestService.create(adopted.getId(), filip.getId(), "Please"));
    }

    @Test
    void sameAdopterCannotHaveTwoPendingRequestsForTheSameDog() {
        Shelter shelter = shelterRepository.save(new Shelter("Test Shelter 3", "Ohrid", null, null, 10));
        Dog dog = dogRepository.save(new Dog("Max", Breed.MIXED, Sex.MALE, LocalDate.of(2023, 3, 3), 15.0,
                DogStatus.AVAILABLE, null, shelter));
        Adopter sara = adopterRepository.save(new Adopter("Sara", "sara.duplicate@test.mk", null, "Skopje"));

        requestService.create(dog.getId(), sara.getId(), "First request");

        assertThrows(AdoptionRuleViolationException.class,
                () -> requestService.create(dog.getId(), sara.getId(), "Second request"));
    }
}
