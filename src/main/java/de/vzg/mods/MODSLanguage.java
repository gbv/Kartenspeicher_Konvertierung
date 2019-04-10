package de.vzg.mods;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSLanguage extends MODSAutoOrderedElement implements MODSDisplayLabel {
    public static final String ELEMENT_NAME = "language";

    private static final List<String> ORDER = Stream
        .of("languageTerm", "scriptTerm").collect(Collectors.toList());

    public MODSLanguage(Element MODSElement) {
        super(MODSElement);
    }

    public MODSLanguage() {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected List<String> getElementOrder() {
        return ORDER;
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public void addLanguageTerm(MODSLanguageTerm languageTerm) {
        insertElement(languageTerm.getElement());
    }

    public List<MODSLanguageTerm> getLanguageTerms() {
        return getElements("mods:" + MODSLanguageTerm.ELEMENT_NAME, MODSLanguageTerm::new);
    }
}
