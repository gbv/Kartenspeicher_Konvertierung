package de.vzg.mods;

import org.jdom2.Element;

public class MODSNameIdentifier extends MODSElement implements MODSText, MODSType {

    public static final String TYPE_ATTR = "type";

    public static final String ELEMENT_NAME = "nameIdentifier";

    public MODSNameIdentifier(Element MODSElement) {
        super(MODSElement);
        if (!MODSElement.getName().equals(ELEMENT_NAME)) {
            throw new IllegalArgumentException("Element is no " + ELEMENT_NAME);
        }
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public MODSNameIdentifier(String type, String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setType(type);
        setText(text);
    }

}
