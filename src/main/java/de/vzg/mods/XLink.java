package de.vzg.mods;

import java.util.Optional;

public interface XLink extends MODSElementWrapper {

    default Optional<String> getXlinkHref() {
        return Optional.ofNullable(this.getElement().getAttributeValue("href", MODSUtil.XLINK_NAMESPACE));
    }

    default void setXlinkHref(String href) {
        this.getElement().setAttribute("href", href, MODSUtil.XLINK_NAMESPACE);
    }

    default Optional<String> getXLinkType() {
        return Optional.ofNullable(getElement().getAttributeValue("type", MODSUtil.XLINK_NAMESPACE));
    }

    default void setXLinkType(String type) {
        getElement().setAttribute("type", type, MODSUtil.XLINK_NAMESPACE);
    }

}
