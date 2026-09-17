package mk.ukim.finki.dogshelter.repository;

import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.Shelter;
import mk.ukim.finki.dogshelter.model.enums.Breed;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DogRepository extends JpaRepository<Dog, Long> {

    List<Dog> findAllByOrderByNameAsc();

    List<Dog> findByStatusOrderByNameAsc(DogStatus status);

    List<Dog> findByBreedOrderByNameAsc(Breed breed);

    List<Dog> findByStatusAndBreedOrderByNameAsc(DogStatus status, Breed breed);

    long countByStatus(DogStatus status);

    long countByShelter(Shelter shelter);
}
