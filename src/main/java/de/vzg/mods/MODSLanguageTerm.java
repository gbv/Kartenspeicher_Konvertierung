package de.vzg.mods;

import org.jdom2.Element;

public class MODSLanguageTerm extends MODSElement implements MODSType, MODSAuthority,MODSText {

    public static final String ELEMENT_NAME = "languageTerm";

    public MODSLanguageTerm(Element MODSElement) {
        super(MODSElement);
    }

    public MODSLanguageTerm(){
        this(new Element(ELEMENT_NAME,MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }
}
