package de.vzg.mods;

import java.util.Optional;

import org.jdom2.Element;

public class MODSNamePart extends MODSElement implements MODSText {

    public static final String ELEMENT_NAME = "namePart";

    public MODSNamePart(Element MODSElement) {
        super(MODSElement);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public MODSNamePart(TYPE type, String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setType(type);
        setText(text);
    }

    public Optional<TYPE> getType() {
        return Optional.ofNullable(getElement()
            .getAttributeValue("type"))
            .map(TYPE::valueOf);
    }

    public void setType(TYPE type) {
        getElement().setAttribute("type", type.toString());
    }


    public enum TYPE {
        date,
        family,
        given,
        termsOfAddress
    }
}
