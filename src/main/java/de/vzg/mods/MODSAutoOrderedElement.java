package de.vzg.mods;

import java.util.List;

import org.jdom2.Element;

public abstract class MODSAutoOrderedElement extends MODSElement {

    public MODSAutoOrderedElement(Element MODSElement) {
        super(MODSElement);
    }

    protected void insertElement(Element element) {
        int rankOfNewElement = getOrderRank(element.getName());
        List<Element> topLevelElements = getElement().getChildren();
        for (int pos = 0; pos < topLevelElements.size(); pos++) {
            if (getOrderRank(topLevelElements.get(pos).getName()) > rankOfNewElement) {
                getElement().addContent(pos, element);
                return;
            }
        }

        getElement().addContent(element);
    }

    private int getOrderRank(String elementName) {
        return getElementOrder().indexOf(elementName);
    }

    protected abstract List<String> getElementOrder();
}
