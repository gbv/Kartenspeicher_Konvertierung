package de.vzg.mods;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public abstract class MODSElement implements MODSElementWrapper {

    private Element MODSElement;

    public MODSElement(Element MODSElement) {
        if (MODSElement.getNamespace() == null || !MODSElement.getNamespace().equals(MODSUtil.MODS_NAMESPACE)) {
            throw new IllegalArgumentException("The element is not in the MODS-Namespace!");
        }
        if (!MODSElement.getName().equals(getElementName())) {
            throw new IllegalArgumentException("Element is no " + getElementName());
        }
        this.MODSElement = MODSElement;
    }

    public Element getElement() {
        return MODSElement;
    }

    protected <T> Optional<T> getFirstElement(String xpath, Function<Element, T> constructor) {
        XPathExpression<Element> xPathExpression = XPathFactory.instance()
            .compile(xpath, Filters.element(), null, MODSUtil.MODS_NAMESPACE);
        final Element element = xPathExpression.evaluateFirst(getElement());
        return Optional.ofNullable(constructor.apply(element));
    }

    protected <T> List<T> getElements(String xpath, Function<Element, T> constructor) {
        XPathExpression<Element> xPathExpression = XPathFactory.instance()
            .compile(xpath, Filters.element(), null, MODSUtil.MODS_NAMESPACE);
        final List<Element> element = xPathExpression.evaluate(getElement());
        return element.stream().map(constructor).collect(Collectors.toList());
    }

    protected Element setOrCreatSimpleTextElement(String name, String description) {
        return getOrCreateElement(name).setText(description);
    }

    protected Element getOrCreateElement(String name) {
        return getFirstElement("mods:" + name, MODSUtil::PASS)
            .orElseGet(() -> {
                final Element newElement = new Element(name, MODSUtil.MODS_NAMESPACE);
                insertElement(newElement);
                return newElement;
            });
    }


    protected void validateNotNullEmpty(String text) {
        if (text == null) {
            throw new IllegalArgumentException("The String is null");
        }
        if (text.isEmpty()) {
            throw new IllegalArgumentException("The String is empty!");
        }
    }

    protected void insertElement(Element newElement) {
        getElement().addContent(newElement);
    }

    protected abstract String getElementName();

}
