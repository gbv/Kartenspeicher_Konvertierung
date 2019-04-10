package de.vzg.mods;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSTitleInfo extends MODSAutoOrderedElement {

    public static final String ELEMENT_NAME = "titleInfo";

    public static final String SUB_ELEM_TITLE = "title";

    private static final String SUB_ELEM_SUB_TITLE = "subTitle";

    private static final String SUB_ELEM_PART_NUMBER = "partNumber";

    private static final String SUB_ELEM_PART_NAME = "partName";

    private static final String SUB_ELEM_PART_NON_SORT = "nonSort";

    public static final List<String> ORDER = Stream
        .of(SUB_ELEM_TITLE, SUB_ELEM_SUB_TITLE, SUB_ELEM_PART_NUMBER, SUB_ELEM_PART_NAME, SUB_ELEM_PART_NON_SORT)
        .collect(Collectors.toList());

    public MODSTitleInfo(Element MODSElement) {
        super(MODSElement);
    }

    public MODSTitleInfo(String title) {
        super(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
        setTitle(title);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    public Optional<String> getTitle() {
        return getFirstElement("mods:" + SUB_ELEM_TITLE, MODSUtil::PASS).map(Element::getTextTrim);
    }

    public void setTitle(String title) {
        getOrCreateElement(SUB_ELEM_TITLE).setText(title);
    }

    public Optional<String> getSubTitle() {
        return getFirstElement("mods:" + SUB_ELEM_SUB_TITLE, MODSUtil::PASS).map(Element::getTextTrim);
    }

    public void setSubTitle(String subTitle) {
        getOrCreateElement(SUB_ELEM_SUB_TITLE).setText(subTitle);
    }

    public Optional<String> getPartNumber() {
        return getFirstElement("mods:" + SUB_ELEM_PART_NUMBER, MODSUtil::PASS).map(Element::getTextTrim);
    }

    public void setPartNumber(String partNumber) {
        getOrCreateElement(SUB_ELEM_PART_NUMBER).setText(partNumber);
    }

    public Optional<String> getPartName() {
        return getFirstElement("mods:" + SUB_ELEM_PART_NAME, MODSUtil::PASS).map(Element::getTextTrim);
    }

    public void setPartName(String partName) {
        getOrCreateElement(SUB_ELEM_PART_NAME).setText(partName);
    }

    public Optional<String> getNonSort() {
        return getFirstElement("mods:" + SUB_ELEM_PART_NON_SORT, MODSUtil::PASS).map(Element::getTextTrim);
    }

    public void setNonSort(String nonSort) {
        getOrCreateElement(SUB_ELEM_PART_NON_SORT).setText(nonSort);
    }

    @Override
    protected List<String> getElementOrder() {
        return ORDER;
    }
}
