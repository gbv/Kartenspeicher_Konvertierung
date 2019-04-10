package de.vzg.mods;

import java.util.Optional;

public interface MODSDisplayLabel extends MODSElementWrapper {

    default Optional<String> getDisplayLabel() {
        return Optional.ofNullable(getElement().getAttributeValue("displayLabel"));
    }

    default void setDisplayLabel(String displayLabel) {
        getElement().setAttribute("displayLabel", displayLabel);
    }


}
