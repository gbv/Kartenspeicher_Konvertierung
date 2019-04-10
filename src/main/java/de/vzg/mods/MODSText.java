package de.vzg.mods;

public interface MODSText extends MODSElementWrapper {

    default String getText() {
        return getElement().getText();
    }

    default void setText(String text) {
        getElement().setText(text);
    }
}
