package mk.ukim.finki.dogshelter.model.enums;

public enum Breed {

    SHARPLANINEC("Sharplaninec", Size.LARGE),
    LABRADOR_RETRIEVER("Labrador Retriever", Size.LARGE),
    GERMAN_SHEPHERD("German Shepherd", Size.LARGE),
    GOLDEN_RETRIEVER("Golden Retriever", Size.LARGE),
    ROTTWEILER("Rottweiler", Size.LARGE),
    CANE_CORSO("Cane Corso", Size.LARGE),
    SIBERIAN_HUSKY("Siberian Husky", Size.MEDIUM),
    BORDER_COLLIE("Border Collie", Size.MEDIUM),
    BEAGLE("Beagle", Size.MEDIUM),
    COCKER_SPANIEL("Cocker Spaniel", Size.MEDIUM),
    FRENCH_BULLDOG("French Bulldog", Size.SMALL),
    DACHSHUND("Dachshund", Size.SMALL),
    YORKSHIRE_TERRIER("Yorkshire Terrier", Size.SMALL),
    POODLE("Poodle", Size.SMALL),
    MALTESE("Maltese", Size.SMALL),
    MIXED("Mixed breed", Size.MEDIUM);

    public enum Size {
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large");

        private final String label;

        Size(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private final String displayName;
    private final Size typicalSize;

    Breed(String displayName, Size typicalSize) {
        this.displayName = displayName;
        this.typicalSize = typicalSize;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Size getTypicalSize() {
        return typicalSize;
    }
}
