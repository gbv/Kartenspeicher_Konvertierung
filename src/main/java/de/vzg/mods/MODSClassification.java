package de.vzg.mods;

import org.jdom2.Element;

public class MODSClassification extends MODSElement implements MODSAuthority, MODSText, MODSDisplayLabel {

    public static final String ELEMENT_NAME = "classification";

    public MODSClassification(Element MODSElement) {
        super(MODSElement);
    }

    public MODSClassification() {
        super(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }
}
