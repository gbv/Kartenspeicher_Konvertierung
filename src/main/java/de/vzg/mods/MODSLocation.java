package de.vzg.mods;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSLocation extends MODSAutoOrderedElement {

    public static final String ELEMENT_NAME = "location";

    private static final List<String> ORDER = Stream
        .of("physicalLocation", "shelfLocator", "url", "holdingSimple", "holdingExternal").collect(Collectors.toList());

    public MODSLocation(Element MODSElement) {
        super(MODSElement);
    }

    public MODSLocation() {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public void addShelfLocator(MODSShelfLocator shelfLocator) {
        this.insertElement(shelfLocator.getElement());
    }

    public void addUrl(MODSURL url) {
        this.insertElement(url.getElement());
    }

    public List<MODSShelfLocator> getShelfLocators() {
        return getElements("mods:shelfLocator", MODSShelfLocator::new);
    }

    public List<MODSURL> getURLs() {
        return getElements("mods:url", MODSURL::new);
    }

    @Override protected List<String> getElementOrder() {
        return ORDER;
    }
}
