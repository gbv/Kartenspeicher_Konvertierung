package de.vzg.mods;

import java.util.Optional;

public interface MODSAuthority extends MODSElementWrapper {

    default Optional<String> getAuthority() {
        return Optional.ofNullable(getElement().getAttributeValue("authority"));
    }

    default void setAuthority(String authority) {
        getElement().setAttribute("authority", authority);
    }

    default Optional<String> getAuthorityURI() {
        return Optional.ofNullable(getElement().getAttributeValue("authorityURI"));
    }

    default void setAuthorityURI(String authorityURI) {
        getElement().setAttribute("authorityURI", authorityURI);
    }

    default Optional<String> getValueURI() {
        return Optional.ofNullable(getElement().getAttributeValue("valueURI"));
    }

    default void setValueURI(String valueURI) {
        getElement().setAttribute("valueURI", valueURI);
    }
}
