package de.vzg.mods;

import org.jdom2.Element;

public class MODSNote extends MODSElement implements MODSType, MODSText {

    public static final String ELEMENT_NAME = "note";

    public MODSNote(Element MODSElement) {
        super(MODSElement);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public MODSNote(String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    public MODSNote(String text, String type) {
        this(text);
        setType(type);
    }

}
