package co.uk.yapily.ecommerce.model;

public enum Label {
    DRINK, FOOD, CLOTHES, LIMITED;

    public static Label fromString(String label) {
        for (Label l : Label.values()) {
            if (l.name().equalsIgnoreCase(label)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Invalid label: " + label);
    }
}