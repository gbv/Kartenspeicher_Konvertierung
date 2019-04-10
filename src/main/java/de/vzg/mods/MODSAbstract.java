package de.vzg.mods;

import org.jdom2.Element;

public class MODSAbstract extends MODSElement implements MODSText {

    public static final String ELEMENT_NAME = "abstract";

    public MODSAbstract(Element MODSElement) {
        super(MODSElement);
    }

    public MODSAbstract(String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }
}


