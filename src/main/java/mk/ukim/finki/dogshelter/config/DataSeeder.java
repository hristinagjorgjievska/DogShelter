package mk.ukim.finki.dogshelter.config;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@ConditionalOnProperty(name = "shelter.seed-data", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final ShelterRepository shelterRepository;
    private final DogRepository dogRepository;
    private final AdopterRepository adopterRepository;
    private final AdoptionRequestRepository requestRepository;

    public DataSeeder(ShelterRepository shelterRepository,
                      DogRepository dogRepository,
                      AdopterRepository adopterRepository,
                      AdoptionRequestRepository requestRepository) {
        this.shelterRepository = shelterRepository;
        this.dogRepository = dogRepository;
        this.adopterRepository = adopterRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (shelterRepository.count() > 0) {
            log.info("Database already contains data, skipping seed.");
            return;
        }
        log.info("Seeding sample shelters, dogs, adopters and adoption requests.");

        Shelter skopje = shelterRepository.save(
                new Shelter("Vardar Valley Shelter", "Skopje", "ul. Vodnjanska 17", "+389 2 3111 222", 120));
        Shelter bitola = shelterRepository.save(
                new Shelter("Pelister Paws", "Bitola", "ul. Širok Sokak 45", "+389 47 222 333", 60));
        Shelter ohrid = shelterRepository.save(
                new Shelter("Lakeside Rescue", "Ohrid", "Kej Maršal Tito 8", "+389 46 250 100", 40));

        LocalDate today = LocalDate.now();
        Dog rex = new Dog("Rex", Breed.GERMAN_SHEPHERD, Sex.MALE, today.minusYears(4).minusMonths(2), 34.0,
                DogStatus.ADOPTED, "Loyal and well trained, great with kids.", skopje);
        Dog luna = new Dog("Luna", Breed.LABRADOR_RETRIEVER, Sex.FEMALE, today.minusYears(2).minusMonths(5), 27.5,
                DogStatus.AVAILABLE, "Playful, loves water and long walks.", skopje);
        Dog max = new Dog("Max", Breed.BEAGLE, Sex.MALE, today.minusYears(3), 12.0,
                DogStatus.AVAILABLE, "Curious nose, needs a fenced yard.", skopje);
        Dog bella = new Dog("Bella", Breed.GOLDEN_RETRIEVER, Sex.FEMALE, today.minusYears(1).minusMonths(8), 24.0,
                DogStatus.AVAILABLE, "Gentle and calm, an ideal first dog.", bitola);
        Dog sharko = new Dog("Šarko", Breed.SHARPLANINEC, Sex.MALE, today.minusYears(5).minusMonths(1), 48.0,
                DogStatus.AVAILABLE, "Mountain guardian, needs space and an experienced owner.", bitola);
        Dog milo = new Dog("Milo", Breed.FRENCH_BULLDOG, Sex.MALE, today.minusMonths(9), 9.5,
                DogStatus.UNDER_CARE, "Recovering from surgery, available soon.", skopje);
        Dog daisy = new Dog("Daisy", Breed.DACHSHUND, Sex.FEMALE, today.minusYears(6), 7.8,
                DogStatus.AVAILABLE, "Senior lady who loves naps and sunny spots.", ohrid);
        Dog rocky = new Dog("Rocky", Breed.SIBERIAN_HUSKY, Sex.MALE, today.minusYears(2), 22.0,
                DogStatus.AVAILABLE, "High energy, perfect for runners and hikers.", ohrid);
        Dog zoe = new Dog("Zoe", Breed.BORDER_COLLIE, Sex.FEMALE, today.minusYears(1).minusMonths(3), 16.0,
                DogStatus.AVAILABLE, "Smart and eager, learns a trick a day.", bitola);
        Dog aro = new Dog("Aro", Breed.MIXED, Sex.MALE, today.minusYears(3).minusMonths(7), 19.0,
                DogStatus.AVAILABLE, "Friendly street rescue, gets along with cats.", skopje);
        Dog lea = new Dog("Lea", Breed.COCKER_SPANIEL, Sex.FEMALE, today.minusYears(4), 13.5,
                DogStatus.UNDER_CARE, "Being treated for an ear infection.", ohrid);
        Dog toby = new Dog("Toby", Breed.YORKSHIRE_TERRIER, Sex.MALE, today.minusYears(7).minusMonths(4), 3.2,
                DogStatus.AVAILABLE, "Tiny but bossy, apartment friendly.", skopje);
        dogRepository.saveAll(List.of(rex, luna, max, bella, sharko, milo, daisy, rocky, zoe, aro, lea, toby));

        Adopter ana = new Adopter("Ana Petrovska", "ana.petrovska@example.com", "+389 70 111 222", "Skopje");
        Adopter marko = new Adopter("Marko Stojanov", "marko.stojanov@example.com", "+389 71 333 444", "Skopje");
        Adopter elena = new Adopter("Elena Nikolova", "elena.nikolova@example.com", "+389 72 555 666", "Bitola");
        Adopter filip = new Adopter("Filip Dimitrov", "filip.dimitrov@example.com", "+389 75 777 888", "Ohrid");
        Adopter sara = new Adopter("Sara Trajkova", "sara.trajkova@example.com", "+389 76 999 000", "Kumanovo");
        Adopter nikola = new Adopter("Nikola Ristov", "nikola.ristov@example.com", "+389 78 123 456", "Tetovo");
        adopterRepository.saveAll(List.of(ana, marko, elena, filip, sara, nikola));

        LocalDateTime now = LocalDateTime.now();
        requestRepository.saveAll(List.of(
                request(luna, ana, "We have a big garden and two kids who adore labradors.",
                        RequestStatus.PENDING, now.minusDays(3), null),
                request(luna, marko, "Looking for a running partner for the Vodno trails.",
                        RequestStatus.PENDING, now.minusDays(2), null),
                request(rex, elena, "Experienced with shepherds, retired police dog handler.",
                        RequestStatus.APPROVED, now.minusDays(10), now.minusDays(8)),
                request(max, filip, "Small apartment, but I work from home.",
                        RequestStatus.REJECTED, now.minusDays(6), now.minusDays(5)),
                request(bella, sara, "First-time owner, already completed a training course.",
                        RequestStatus.PENDING, now.minusDays(1), null),
                request(sharko, nikola, "We live on a farm near Mavrovo with plenty of space.",
                        RequestStatus.PENDING, now.minusHours(5), null)
        ));

        log.info("Seed data loaded.");
    }

    private static AdoptionRequest request(Dog dog, Adopter adopter, String message, RequestStatus status,
                                           LocalDateTime submittedAt, LocalDateTime decidedAt) {
        AdoptionRequest request = new AdoptionRequest(dog, adopter, message);
        request.setStatus(status);
        request.setSubmittedAt(submittedAt);
        request.setDecidedAt(decidedAt);
        return request;
    }
}
