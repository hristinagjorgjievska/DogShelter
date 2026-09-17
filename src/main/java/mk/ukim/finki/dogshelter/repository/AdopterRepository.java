package mk.ukim.finki.dogshelter.repository;

import mk.ukim.finki.dogshelter.model.Adopter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdopterRepository extends JpaRepository<Adopter, Long> {

    List<Adopter> findAllByOrderByFullNameAsc();
}
