package co.uk.yapily.ecommerce.model;

import co.uk.yapily.ecommerce.exception.LabelException;

public enum Label {
    DRINK, FOOD, CLOTHES, LIMITED;

    public static Label fromString(String label) {
        for (Label l : Label.values()) {
            if (l.name().equalsIgnoreCase(label)) {
                return l;
            }
        }
        throw new LabelException("Invalid label: " + label);
    }
}