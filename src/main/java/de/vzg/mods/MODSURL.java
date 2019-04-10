package de.vzg.mods;

import org.jdom2.Element;

public class MODSURL extends MODSElement implements MODSText {

    public static final String ELEM_NAME = "url";

    public MODSURL(Element MODSElement) {
        super(MODSElement);
    }

    public MODSURL(String text) {
        super(new Element(ELEM_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEM_NAME;
    }
}
