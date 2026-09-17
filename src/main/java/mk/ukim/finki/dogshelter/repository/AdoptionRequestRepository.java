package mk.ukim.finki.dogshelter.repository;

import mk.ukim.finki.dogshelter.model.AdoptionRequest;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequest, Long> {

    List<AdoptionRequest> findAllByOrderBySubmittedAtDesc();

    List<AdoptionRequest> findByStatusOrderBySubmittedAtDesc(RequestStatus status);

    List<AdoptionRequest> findByDogAndStatus(Dog dog, RequestStatus status);

    boolean existsByDogAndAdopterIdAndStatus(Dog dog, Long adopterId, RequestStatus status);

    long countByStatus(RequestStatus status);

    void deleteByDog(Dog dog);
}
