package de.vzg.mods;

import org.jdom2.Element;

public class MODSTopic extends MODSElement implements MODSAuthority, MODSText {

    public static final String ELEM_NAME = "topic";

    public MODSTopic(Element MODSElement) {
        super(MODSElement);
    }

    public MODSTopic(String text){
        this(new Element(ELEM_NAME, MODSUtil.MODS_NAMESPACE));
        setText(text);
    }

    @Override
    protected String getElementName() {
        return ELEM_NAME;
    }
}
