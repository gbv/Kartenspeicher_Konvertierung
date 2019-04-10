package de.vzg.mods;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSDocument extends MODSAutoOrderedElement {

    public static final String ELEMENT_NAME = "mods";

    private static final List<String> ORDER = Stream
        .of("typeOfResource",
            "titleInfo",
            "name",
            "genre",
            "originInfo",
            "language",
            "abstract",
            "note",
            "physicalDescription",
            "subject",
            "classification",
            "relatedItem",
            "identifier",
            "location",
            "accessCondition").collect(Collectors.toList());

    public MODSDocument(Element MODSElement) {
        super(MODSElement);
    }

    public MODSDocument() {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public void addElement(MODSTitleInfo titleInfo) {
        this.insertElement(titleInfo.getElement());
    }

    public void addElement(MODSOriginInfo originInfo) {
        this.insertElement(originInfo.getElement());
    }

    public void addElement(MODSName name) {
        this.insertElement(name.getElement());
    }

    public void addElement(MODSNote note) {
        this.insertElement(note.getElement());
    }

    public void addElement(MODSAbstract modsAbstract) {
        this.insertElement(modsAbstract.getElement());
    }

    public void addElement(MODSSubject subject) {
        this.insertElement(subject.getElement());
    }

    public void addElement(MODSPhysicalDescription modsPhysicalDescription) {
        insertElement(modsPhysicalDescription.getElement());
    }

    public void addElement(MODSGenre genre) {
        insertElement(genre.getElement());
    }

    public void addElement(MODSAccessCondition accessCondition) {
        insertElement(accessCondition.getElement());
    }

    public void addElement(MODSClassification classification) {
        insertElement(classification.getElement());
    }

    public Optional<String> getTypeOfResource() {
        return getFirstElement("mods:typeOfResource", MODSUtil::PASS).map(Element::getText);
    }

    public void setTypeOfResource(String typeOfResource) {
        getOrCreateElement("typeOfResource").setText(typeOfResource);
    }

    public List<MODSName> getNames() {
        return getElements("mods:" + MODSName.ELEMENT_NAME, MODSName::new);
    }

    public List<MODSName> getNames(String type) {
        return getElements("mods:" + MODSName.ELEMENT_NAME + "[@type='personal']", MODSName::new);
    }

    public List<MODSTitleInfo> getTitleInfo() {
        return getElements("mods:" + MODSTitleInfo.ELEMENT_NAME, MODSTitleInfo::new);
    }

    public List<MODSOriginInfo> getOriginInfo() {
        return getElements("mods:" + MODSOriginInfo.ELEMENT_NAME, MODSOriginInfo::new);
    }

    public Optional<MODSOriginInfo> getOriginInfo(String eventType) {
        return getFirstElement("mods:" + MODSOriginInfo.ELEMENT_NAME + "[@eventType='" + eventType + "']",
            MODSOriginInfo::new);
    }

    public List<MODSSubject> getSubjects() {
        return getElements("mods:" + MODSSubject.ELEMENT_NAME, MODSSubject::new);
    }

    public List<MODSClassification> getClassifications() {
        return getElements("mods:" + MODSClassification.ELEMENT_NAME, MODSClassification::new);
    }

    protected List<String> getElementOrder() {
        return ORDER;
    }

    public void addElement(MODSLocation modsLocation) {
        insertElement(modsLocation.getElement());

    }

    public void addElement(MODSLanguage modsLanguage) {
        insertElement(modsLanguage.getElement());
    }

    public List<MODSLanguage> getLanguages(){
        return getElements("mods:"+MODSLanguage.ELEMENT_NAME,MODSLanguage::new);
    }
}
