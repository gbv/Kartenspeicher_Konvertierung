/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.vzg.mods;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSSubject extends MODSAutoOrderedElement {

    private static final List<String> ORDER = Stream
        .of("topic",
            "geographic",
            "temporal",
            "titleInfo",
            "name",
            "genre",
            "hierarchicalGeographic",
            "cartographics",
            "geographicCode",
            "occupation").collect(Collectors.toList());

    protected static final String ELEMENT_NAME = "subject";

    public MODSSubject(Element MODSElement) {
        super(MODSElement);
        if (!MODSElement.getName().equals(ELEMENT_NAME)) {
            throw new IllegalArgumentException("Element is no " + ELEMENT_NAME);
        }
    }

    public MODSSubject() {
        super(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected List<String> getElementOrder() {
        return ORDER;
    }

    public List<MODSCartographics> getCartographics() {
        return getElements("mods:" + MODSCartographics.ELEMENT_NAME, MODSCartographics::new);
    }

    public List<MODSTopic> getTopics() {
        return getElements("mods:" + MODSTopic.ELEM_NAME, MODSTopic::new);
    }

    public List<MODSGeographic> getGeographics() {
        return getElements("mods:" + MODSGeographic.ELEM_NAME, MODSGeographic::new);
    }

    public void addTopic(MODSTopic topic) {
        insertElement(topic.getElement());
    }

    public void addCartographics(MODSCartographics cartographics) {
        insertElement(cartographics.getElement());
    }

    public void addGeographic(MODSGeographic geographic) {
        insertElement(geographic.getElement());
    }

}
