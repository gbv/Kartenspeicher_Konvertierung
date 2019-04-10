package de.vzg.mods;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSOriginInfo extends MODSAutoOrderedElement {

    public static final List<String> ORDER = Stream
        .of("place", "publisher", "dateIssued", "dateCreated", "dateCaptured", "dateValid", "dateModified",
            "copyrightDate", "dateOther", "edition", "issuance", "frequency").collect(Collectors.toList());

    public static final String ELEMENT_NAME = "originInfo";

    public static final String EVENT_TYPE_ATTR = "eventType";

    public MODSOriginInfo(Element MODSElement) {
        super(MODSElement);
    }

    public MODSOriginInfo(String eventType) {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setEventType(eventType);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public Optional<String> getEventType() {
        return Optional.ofNullable(getElement().getAttributeValue(EVENT_TYPE_ATTR));
    }

    public void setEventType(String eventType) {
        getElement().setAttribute("eventType", eventType);
    }

    public Element addPlaceTerm(String type, String text) {
        final Element place = getOrCreateElement("place");

        final Element placeTerm = new Element("placeTerm", MODSUtil.MODS_NAMESPACE);
        place.addContent(placeTerm);

        if (type != null) {
            placeTerm.setAttribute("type", type);
        }
        placeTerm.setText(text);

        return placeTerm;
    }

    public List<Element> getPlaceTerms() {
        return getElements("mods:place/mods:placeTerm", MODSUtil::PASS);
    }

    public Element addDateIssued(String encoding, String text) {
        final Element dateIssued = new Element("dateIssued", MODSUtil.MODS_NAMESPACE);
        if (encoding != null) {
            dateIssued.setAttribute("encoding", encoding);
        }
        dateIssued.setText(text);
        insertElement(dateIssued);

        return dateIssued;
    }

    public void addPublisher(MODSPublisher publisher) {
        insertElement(publisher.getElement());
    }

    public List<MODSPublisher> getPublisher() {
        return getElements("mods:" + MODSPublisher.ELEM_NAME, MODSPublisher::new);
    }

    @Override
    protected List<String> getElementOrder() {
        return ORDER;
    }
}
