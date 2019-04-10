package de.vzg.mods;

import org.jdom2.Element;

public class MODSShelfLocator extends MODSElement implements MODSText {

    public static final String ELEMENT_NAME = "shelfLocator";


    public MODSShelfLocator(Element MODSElement) {
        super(MODSElement);
    }

    public MODSShelfLocator(String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }


}
