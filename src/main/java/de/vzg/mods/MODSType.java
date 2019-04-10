package de.vzg.mods;

import java.util.Optional;

public interface MODSType extends MODSElementWrapper {

    default Optional<String> getType() {
        return Optional.ofNullable(getElement().getAttributeValue("type"));
    }

    default void setType(String type) {
        getElement().setAttribute("type", type);
    }
}
