package mk.ukim.finki.dogshelter.web.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mk.ukim.finki.dogshelter.model.AdoptionRequest;

public class AdoptionRequestForm {

    @NotNull(message = "Pick a dog")
    private Long dogId;

    @NotNull(message = "Pick an adopter")
    private Long adopterId;

    @Size(max = 500)
    private String message;

    public static AdoptionRequestForm from(AdoptionRequest request) {
        AdoptionRequestForm form = new AdoptionRequestForm();
        form.setDogId(request.getDog().getId());
        form.setAdopterId(request.getAdopter().getId());
        form.setMessage(request.getMessage());
        return form;
    }

    public Long getDogId() {
        return dogId;
    }

    public void setDogId(Long dogId) {
        this.dogId = dogId;
    }

    public Long getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(Long adopterId) {
        this.adopterId = adopterId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
