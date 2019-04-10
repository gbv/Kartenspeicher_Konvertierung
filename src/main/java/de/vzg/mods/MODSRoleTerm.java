package de.vzg.mods;

import org.jdom2.Element;

public class MODSRoleTerm extends MODSElement implements MODSAuthority, MODSType, MODSText {

    public static final String ELEMENT_NAME = "roleTerm";


    public MODSRoleTerm(Element MODSElement) {
        super(MODSElement);
    }

    @Override protected String getElementName() {
        return ELEMENT_NAME;
    }

    public MODSRoleTerm(String type, String authority, String text) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setAuthority(authority);
        setText(text);
        setType(type);
    }

}
