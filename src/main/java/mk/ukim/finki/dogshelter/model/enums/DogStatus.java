package mk.ukim.finki.dogshelter.model.enums;

public enum DogStatus {

    AVAILABLE("Available"),
    UNDER_CARE("Under care"),
    ADOPTED("Adopted");

    private final String displayName;

    DogStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
