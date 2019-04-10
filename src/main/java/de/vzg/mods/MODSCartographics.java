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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jdom2.Element;

public class MODSCartographics extends MODSAutoOrderedElement {

    protected static final String ELEMENT_NAME = "cartographics";

    private static final List<String> ORDER = Stream
        .of("scale",
            "projection",
    "coordinates").collect(Collectors.toList());

    public MODSCartographics(Element MODSElement) {
        super(MODSElement);
    }

    @Override
    protected String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    protected List<String> getElementOrder() {
        return ORDER;
    }

    public MODSCartographics() {
        this(new Element(ELEMENT_NAME, MODSUtil.MODS_NAMESPACE));
    }

    public List<MODSCoordinates> getCoordinates() {
        return getElements("mods:coordinates", MODSCoordinates::new);
    }

    public void addCoordinates(String text) {
        insertElement(new MODSCoordinates(text).getElement());
    }

    public void setScale(String scale){
        getOrCreateElement("scale").setText(scale);
    }

    public Optional<String> getScale(){
        return getFirstElement("mods:scale", MODSUtil::PASS).map(Element::getText);
    }

}
