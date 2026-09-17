package mk.ukim.finki.dogshelter.model.enums;

public enum Sex {

    MALE("Male"),
    FEMALE("Female");

    private final String displayName;

    Sex(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
