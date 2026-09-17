package mk.ukim.finki.dogshelter.repository;

import mk.ukim.finki.dogshelter.model.Shelter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelterRepository extends JpaRepository<Shelter, Long> {

    List<Shelter> findAllByOrderByNameAsc();
}
