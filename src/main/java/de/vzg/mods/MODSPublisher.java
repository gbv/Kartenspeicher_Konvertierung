package de.vzg.mods;

import org.jdom2.Element;

public class MODSPublisher extends MODSElement implements MODSText{

    public static final String ELEM_NAME = "publisher";

    public MODSPublisher(Element MODSElement) {
        super(MODSElement);
    }

    public MODSPublisher(String text) {
        this(new Element(ELEM_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEM_NAME;
    }
}
