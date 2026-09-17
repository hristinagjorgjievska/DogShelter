package mk.ukim.finki.dogshelter.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.dogshelter.model.Dog;
import mk.ukim.finki.dogshelter.model.enums.Breed;
import mk.ukim.finki.dogshelter.model.enums.DogStatus;
import mk.ukim.finki.dogshelter.model.enums.Sex;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class DogForm {

    @NotBlank(message = "Name is required")
    @Size(max = 60)
    private String name;

    @NotNull(message = "Breed is required")
    private Breed breed;

    @NotNull(message = "Sex is required")
    private Sex sex;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthDate;

    @Positive(message = "Weight must be positive")
    private Double weightKg;

    @NotNull(message = "Status is required")
    private DogStatus status = DogStatus.AVAILABLE;

    @Size(max = 500)
    private String description;

    @NotNull(message = "Shelter is required")
    private Long shelterId;

    public static DogForm from(Dog dog) {
        DogForm form = new DogForm();
        form.setName(dog.getName());
        form.setBreed(dog.getBreed());
        form.setSex(dog.getSex());
        form.setBirthDate(dog.getBirthDate());
        form.setWeightKg(dog.getWeightKg());
        form.setStatus(dog.getStatus());
        form.setDescription(dog.getDescription());
        form.setShelterId(dog.getShelter().getId());
        return form;
    }

    public void applyTo(Dog dog) {
        dog.setName(name);
        dog.setBreed(breed);
        dog.setSex(sex);
        dog.setBirthDate(birthDate);
        dog.setWeightKg(weightKg);
        dog.setStatus(status);
        dog.setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Breed getBreed() {
        return breed;
    }

    public void setBreed(Breed breed) {
        this.breed = breed;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public DogStatus getStatus() {
        return status;
    }

    public void setStatus(DogStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getShelterId() {
        return shelterId;
    }

    public void setShelterId(Long shelterId) {
        this.shelterId = shelterId;
    }
}
