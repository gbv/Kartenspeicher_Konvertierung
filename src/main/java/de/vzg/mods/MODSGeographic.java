package de.vzg.mods;

import org.jdom2.Element;

public class MODSGeographic extends MODSElement implements MODSText, MODSAuthority {

    public static final String ELEM_NAME = "geographic";

    public MODSGeographic(Element MODSElement) {
        super(MODSElement);
    }

    public MODSGeographic(String text) {
        super(new Element(ELEM_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEM_NAME;
    }
}
