package de.vzg.mods;

import java.util.List;
import java.util.Optional;

import org.jdom2.Element;
import org.mycore.common.MCRConstants;

public class MODSName extends MODSElement implements MODSAuthority, XLink {

    public static final String ELEMENT_NAME = "name";

    public MODSName(Element MODSElement) {
        super(MODSElement);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public MODSName() {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    public MODSName(String displayForm){
        this();
        setFromCombinedName(displayForm);
    }


    private void setFromDisplayForm(Element element) {
        String displayForm = element.getChildTextTrim("displayForm", MCRConstants.MODS_NAMESPACE);
        if (displayForm != null) {
            setFromCombinedName(displayForm.replaceAll("\\s+", " "));
        }
    }

    private void setFromCombinedName(String nameFragment) {
        if (nameFragment.contains(",")) {
            String[] parts = nameFragment.split(",");
            addNameParts(parts[0].trim(), parts[1].trim());
        } else if (nameFragment.contains(" ")) {
            int pos = nameFragment.lastIndexOf(' ');
            addNameParts(nameFragment.substring(pos), nameFragment.substring(0, pos));
        } else {
            addNamePart(MODSNamePart.TYPE.family, nameFragment);
        }
    }


    private void addNameParts(String familyName, String foreName) {
        addNamePart(MODSNamePart.TYPE.family, familyName);
        addNamePart(MODSNamePart.TYPE.given, foreName);

    }

    public void addNameIdentifier(MODSNameIdentifier nameIdentifier) {
        insertElement(nameIdentifier.getElement());
    }

    public MODSNameIdentifier addNameIdentifier(String type, String text) {
        MODSNameIdentifier nameIdentifier = new MODSNameIdentifier(type, text);
        insertElement(nameIdentifier.getElement());
        return nameIdentifier;
    }

    public void setType(String type) {
        getElement().setAttribute("type", type);
    }

    public Optional<String> getDescription() {
        return getFirstElement("mods:description", Element::getText);
    }

    public void setDescription(String description) {
        setOrCreatSimpleTextElement("description", description);
    }

    public Optional<String> getDisplayForm() {
        return getFirstElement("mods:displayForm", Element::getText);
    }

    public void setDisplayForm(String displayForm) {
        setOrCreatSimpleTextElement("displayForm", displayForm);
    }

    public void addNamePart(MODSNamePart part) {
        insertElement(part.getElement());
    }

    public MODSNamePart addNamePart(MODSNamePart.TYPE type, String text) {
        final MODSNamePart newNamePart = new MODSNamePart(type, text);
        addNamePart(newNamePart);
        return newNamePart;
    }

    public List<MODSNamePart> getNameParts() {
        return getElements("mods:namePart", MODSNamePart::new);
    }

    public List<MODSNamePart> getNameParts(MODSNamePart.TYPE type) {
        return getElements("mods:namePart[@type='" + type.toString() + "']", MODSNamePart::new);
    }

    public List<MODSNameIdentifier> getNameIdentifier() {
        return getElements("mods:nameIdetifier", MODSNameIdentifier::new);
    }

    public List<MODSNameIdentifier> getNameIdentifier(String type) {
        return getElements("mods:nameIdetifier[@type='" + type + "']", MODSNameIdentifier::new);
    }

    public List<MODSRoleTerm> getRoles() {
        return getElements("mods:role/mods:roleTerm", MODSRoleTerm::new);
    }

    public void addRoleTerm(MODSRoleTerm roleTerm) {
        final Element rolesElement = getOrCreateElement("role");
        rolesElement.addContent(roleTerm.getElement());
    }

    public MODSRoleTerm addRoleTerm(String type, String authority, String text) {
        final MODSRoleTerm newRoleTerm = new MODSRoleTerm(type, authority, text);
        addRoleTerm(newRoleTerm);
        return newRoleTerm;
    }

}
