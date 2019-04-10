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

package de.vzg.kartenspeicher;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.Namespace;
import org.jdom2.ProcessingInstruction;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * @author liess 
 * 08.2017 erstellt für die Konvertierung der IKAR-Karten aus der 1.68
 * zum http://kartenspeicher.gbv.de mir-MODS
 *
 */
public class MODS {
	
	Document doc;
	Content content;
	Element root;
	Element genre;
	Namespace nsMods;
	Namespace nsXlink;
	Namespace nsXsi;
	Namespace nsXml;
	XMLOutputter out;
	File modsFile;
	
	Element name;
	Element subject;
	Element cartographics;
	Element physicalDescription;
	Element originInfoPublication;
	Element originInfoCreation;
	boolean hatOriginInfoPublication;
	boolean hatOriginInfoCreation;
	
	public MODS() {
		super();
		initVariablen();
	}
	public MODS(String pfad, String filename) {
		super();
		this.modsFile = new File(pfad + filename);
		this.out = new XMLOutputter();
		this.out.setFormat(Format.getPrettyFormat());

		setNamespaces();
		setRoot();
		initVariablen();

	}
	private void initVariablen() {
		name = new Element("name", nsMods);
		subject = new Element("subject", nsMods);
		cartographics = new Element("cartographics", nsMods);
		physicalDescription = new Element("physicalDescription", nsMods);
		originInfoCreation = new Element("originInfo", nsMods);
		originInfoCreation.setAttribute("eventType", "creation");
		originInfoPublication = new Element("originInfo", nsMods);
		originInfoPublication.setAttribute("eventType", "publication");

		hatOriginInfoPublication = false;
		hatOriginInfoCreation = false;
	}
	private void setNamespaces() {
		//	Namespaces URIs
		String nsModsURL = "http://www.loc.gov/mods/v3";
		String nsXlinkURL = "http://www.w3.org/1999/xlink";
		String nsXsiURL = "http://www.w3.org/2001/XMLSchema-instance";
		String nsXmlURL = "http://www.w3.org/XML/1998/namespace";
		nsXlink = Namespace.getNamespace("xlink", nsXlinkURL);
		nsXsi = Namespace.NO_NAMESPACE.getNamespace("xsi", nsXsiURL);
		nsXml = Namespace.getNamespace("xml", nsXmlURL);
		nsMods = Namespace.getNamespace("mods", nsModsURL);
	}
	private void setRoot() {
		// root mit Namespaces setzen
		root = new Element("mods", nsMods);
		root.addNamespaceDeclaration(nsMods);
		root.addNamespaceDeclaration(nsXlink);
		root.addNamespaceDeclaration(nsXsi);
		this.doc = new Document();
		this.doc.setRootElement(root);
//		java.util.List<Content> list = doc.getContent();
	}
	/**
	 * @param f
	 * Format.getPrettyFormat() (default) oder 
	 * Format.getCompactFormat() oder 
	 * Format.getRawFormat()
	 */
	public void setOutputter(Format f) {
		this.out.setFormat(f);
	
	}
	public void setOut(XMLOutputter out) {
		this.out = out;
	}
	public void setRoot(Element root) {
		this.root = root;
	}
//	private List<Content> getContent() {
//		return this.doc.getContent();
//	}
	public void printMods2File() {
		List<Content> allcontent = this.doc.getContent();
		for (Content content : allcontent) {
			removeEmptyNodes(content);
		}
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getPrettyFormat());
		try {
			PrintStream ps = new PrintStream(this.modsFile);
			out.output(this.doc, ps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void printMods2Console() {
		List<Content> allcontent = this.doc.getContent();
		for (Content content : allcontent) {
			removeEmptyNodes(content);
		}
		XMLOutputter out = new XMLOutputter();
		out.setFormat(Format.getPrettyFormat());
		try {
			PrintStream ps2 = new PrintStream(System.out);
			out.output(this.doc, ps2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String getModsFilename() {
		return this.getModsFile().getName();
	}
	public String getModsFilepath() {
		return this.getModsFile().getPath();
	}
	public String getSprachCode(String sprache) {
	//		Rfc4646, eigentlich nur iso 639.1
			String rfc = "";
			switch (sprache.toLowerCase()) {
				case "dan": rfc = "da"; 	break;
				case "dut": rfc = "nl"; 	break;
				case "eng": rfc = "en"; 	break;
				case "fre": rfc = "fr"; 	break;
				case "ger": rfc = "de"; 	break;
				case "ita": rfc = "it"; 	break;
				case "lat": rfc = "la"; 	break;
				case "rus": rfc = "ru"; 	break;
				case "spa": rfc = "es"; 	break;
				default:	break;
			}
			return rfc;
		}
	public void setModsFile(String filename) {
		this.modsFile = new File(filename);
	}
	public List<Content> getContent(Element e) {
		return e.getContent();
	}
	public Document getDoc() {
		return this.doc;
	}
	public File getModsFile() {
		return this.modsFile;
	}
	public void createGenre() {
		genre = new Element("genre", nsMods);
		genre.setAttribute("type", "intern");
		genre.setAttribute("authorityURI", "http://www.mycore.org/classifications/mir_genres");
		genre.setAttribute("valueURI", "http://www.mycore.org/classifications/mir_genres#map");
		root.addContent(genre);
	}
	public void createPersonal(ArrayList<AuthorMods> autos) {
		int anzahl = autos.size();
		for (int i = 0; i < anzahl; i++) {
			AuthorMods a = autos.get(i);
			if(!a.isEmpty()) {
				Element auto = new Element("name", nsMods);
				auto.setAttribute("type", "personal");
				if(!a.getDisplayForm().isEmpty()) {
					Element displayForm = new Element("displayForm", nsMods);
					displayForm.addContent(a.getDisplayForm());
					auto.addContent(displayForm);
				}
				if(!a.getRoleTerm().isEmpty()) {
					Element role = new Element("role", nsMods);
					Element roleTerm = new Element("roleTerm", nsMods);
					roleTerm.setAttribute("authority", "marcrelator");
					roleTerm.setAttribute("type", "code");
					roleTerm.addContent(a.getRoleTerm());
					role.addContent(roleTerm);
					auto.addContent(role);
				}
				if(!a.getNamePart_termsOfAddress().isEmpty()) {
					Element namePart1 = new Element("namePart", nsMods);
					namePart1.setAttribute("type", "termsOfAddress");
					namePart1.addContent(a.getNamePart_termsOfAddress());
					auto.addContent(namePart1);
				}
				if(!a.getFamily().isEmpty()) {
					Element namePart2 = new Element("namePart", nsMods);
					namePart2.setAttribute("type", "family");
					namePart2.addContent(a.getFamily());
					auto.addContent(namePart2);
				}
				if(!a.getGiven().isEmpty()) {
					Element namePart3 = new Element("namePart", nsMods);
					namePart3.setAttribute("type", "given");
					namePart3.addContent(a.getGiven());
					auto.addContent(namePart3);
				}
				if(!a.getNamePart_date().isEmpty()) {
					Element namePart4 = new Element("namePart", nsMods);
					namePart4.setAttribute("type", "date");
					namePart4.addContent(a.getNamePart_date());
					auto.addContent(namePart4);
				}
				if(!a.getDescription().isEmpty()) {
					Element description = new Element("description", nsMods);
					description.addContent(a.getDescription());
					auto.addContent(description);
				}
				if(!a.getNameIdentifier_gnd().isEmpty()) {
					Element nameIdentifier = new Element("nameIdentifier", nsMods);
					nameIdentifier.setAttribute("type", "gnd");
					nameIdentifier.addContent(a.getNameIdentifier_gnd());
					auto.addContent(nameIdentifier);
				}
				root.addContent(auto);
			}
		}
	}
	public void createCoordinates(String ko){
		if(ko != null) {
			Element co = new Element ("coordinates", nsMods);
			co.addContent(ko);
			this.cartographics.addContent(co);
		}
	}
	public void createSubject() {
		this.subject.addContent(cartographics);
		root.addContent(this.subject);
	}	
	public void createPhysicalDescription() {
		root.addContent(this.physicalDescription);
	}
	public void createOriginInfoPublication() {
		if(hatOriginInfoPublication)
			root.addContent(this.originInfoPublication);
	}
	public void createOriginInfoCreation() {
		if(hatOriginInfoCreation)
			root.addContent(this.originInfoCreation);
	}
	public void createDateCreated(String jahr, String jahrvon, String jahrbis) {
		if(!jahrvon.isEmpty()) {
			if(jahrvon.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateCreated", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.setAttribute("point", "start");
				e.addContent(jahrvon);
				this.originInfoCreation.addContent(e);
				hatOriginInfoCreation = true;
			}
		}	
		if(!jahrbis.isEmpty()) {
			if(jahrbis.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateCreated", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.setAttribute("point", "end");
				e.addContent(jahrbis);
				this.originInfoCreation.addContent(e);
				hatOriginInfoCreation = true;
			}
		}	
		if(!jahr.isEmpty()) {
			if(jahr.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateCreated", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.addContent(jahr);
				this.originInfoCreation.addContent(e);
				hatOriginInfoCreation = true;
			}
		}	
	}
	public void createSingleSeries(String seriesTitel) {
		if(!seriesTitel.isEmpty()) {
			Element rel = new Element("relatedItem", nsMods);
			rel.setAttribute("type", "series");
			rel.setAttribute("href", "mir_mods_00000000", nsXlink);
			Element typeOfResource = new Element("typeOfResource", nsMods);
			typeOfResource.addContent("text");
			rel.addContent(typeOfResource);
			Element genre = new Element("genre", nsMods);
			genre.setAttribute("usage", "primary");
			genre.addContent("series");
			rel.addContent(genre);
			Element titleInfo = new Element("titleInfo", nsMods);
			titleInfo.setAttribute("lang", "de", nsXml);
			rel.addContent(titleInfo);
			Element title = new Element("title", nsMods);
			title.addContent(seriesTitel);
			titleInfo.addContent(title);
			root.addContent(rel);
		}
	}
	public void createRelatedItemsPPN(ArrayList<String> ppns) {
		if(!ppns.isEmpty()) {
			Element rel = new Element("relatedItem", nsMods);
			rel.setAttribute("type", "host");
			boolean hatPPN = false;
			for (String p : ppns) {
				if(!p.isEmpty()) {
					hatPPN = true;
					Element id = new Element("identifier", nsMods);
					id.setAttribute("type", "uri");
					id.addContent("http://uri.gbv.de/document/ikar:ppn:"+p);
					rel.addContent(id);
				}
			}
			if(hatPPN)
				root.addContent(rel);
		}
	}
	public void createDateIssued(String jahr, String jahrvon, String jahrbis) {
		if(!jahrvon.isEmpty()) {
			if(jahrvon.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateIssued", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.setAttribute("point", "start");
				e.addContent(jahrvon);
				this.originInfoPublication.addContent(e);
				hatOriginInfoPublication = true;
			}
		}	
		if(!jahrbis.isEmpty()) {
			if(jahrbis.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateIssued", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.setAttribute("point", "end");
				e.addContent(jahrbis);
				this.originInfoPublication.addContent(e);
				hatOriginInfoPublication = true;
			}
		}	
		if(!jahr.isEmpty()) {
			if(jahr.matches("[1-9][0-9]{0,3}")) {
				Element e = new Element("dateIssued", nsMods);
				e.setAttribute("encoding", "w3cdtf");
				e.addContent(jahr);
				this.originInfoPublication.addContent(e);
				hatOriginInfoPublication = true;
			}
		}	
	}
	public void createDateCaptured(String date) {
		if(date.matches("[1-9][0-9]{0,3}")) {
			Element e = new Element("dateCaptured", nsMods);
			e.setAttribute("encoding", "w3cdtf");
			e.addContent(date);
			this.originInfoPublication.addContent(e);
		}
	}
	public void createEdition(String edition) {
		if(!edition.isEmpty()) {
			Element e = new Element("edition", nsMods);
			e.addContent(edition);
			this.originInfoPublication.addContent(e);
			hatOriginInfoPublication = true;
		}
	}
	public void createVerlag(String verlagsname, String verlagsort) {
		if(!verlagsort.isEmpty()) {
			Element place = new Element("place", nsMods);
			Element pt = new Element("placeTerm", nsMods);
			pt.setAttribute("type", "text");
			pt.addContent(verlagsort);
			place.addContent(pt);
			this.originInfoPublication.addContent(place);
			hatOriginInfoPublication = true;
		}
		if(!verlagsname.isEmpty()) {
			Element pub = new Element("publisher", nsMods);
			pub.addContent(verlagsname);
			this.originInfoPublication.addContent(pub);
			hatOriginInfoPublication = true;
		}
		
	}
	public void createMassstab(String massstab) {
		if(!massstab.isEmpty()) {
			Element m = new Element("scale", nsMods);
			m.addContent(massstab);
			this.cartographics.addContent(m);
		}
	}
	public void createExtension(String pica) {
		if(!pica.isEmpty()) {
			Element ex = new Element("extension", nsMods);
			ex.addContent(pica);
			this.root.addContent(ex);
		}
	}
	public void createSdnb(String sdnb) {
		if(!sdnb.isEmpty()) {
			Element ex = new Element("classification", nsMods);
			ex.setAttribute("authority", "sdnb");
			ex.setAttribute("displayLabel", "sdnb");
			ex.addContent(sdnb);
			this.root.addContent(ex);
		}
	}
	public void createAccessCondition() {
		Element ex = new Element("accessCondition", nsMods);
		ex.setAttribute("type", "use and reproduction");
		ex.setAttribute("href", "http://www.mycore.org/classifications/mir_licenses#rights_reserved", nsXlink);
		this.root.addContent(ex);
	}
	public void createAccessConditionCC0() {
		Element ex = new Element("accessCondition", nsMods);
		ex.setAttribute("type", "use and reproduction");
		ex.setAttribute("href", "http://www.mycore.org/classifications/mir_licenses#cc_zero_1.0", nsXlink);
		ex.setAttribute("type", "simple", nsXlink);
		this.root.addContent(ex);
	}
	public void createID(String ppn) {
		if(!ppn.isEmpty()) {
			Element p = new Element("identifier", nsMods);
			p.setAttribute("type", "uri");
			p.addContent("http://uri.gbv.de/document/ikar:ppn:" + ppn);
			root.addContent(p);
		}
	}
	public void createTopicsGND(ArrayList<String> topics) {
		for (String t : topics) {
			String gnd = "";
			if(t.contains("xxx")) {
				gnd = t.substring(t.indexOf("xxx")+3);
				t = t.substring(0, t.indexOf("xxx"));
				if(!gnd.isEmpty()) {
					Element topic = new Element("topic", nsMods);
					topic.setAttribute("authorityURI", "http://d-nb.info/gnd/");
					topic.setAttribute("valueURI", "http://d-nb.info/" + gnd);
					topic.setAttribute("authority", "gnd");
					if(!t.isEmpty())
						topic.setText(t);
				}
			}
		}
	}
	public void createTopics(ArrayList<String> topics) {
		for (String t : topics) {
			if(!t.contains("xxx")) {
				if(!t.isEmpty()) {
					Element topic = new Element("topic", nsMods);
					topic.setText(t);
					this.subject.addContent(topic);
				}
			}
		}
	}
	public void createNotesContent(ArrayList<String> notes) {
		for (String note : notes) {
			if(!note.isEmpty()) {
				Element n = new Element("note", nsMods);
				n.setAttribute("type", "content");
				n.addContent(note);
				root.addContent(n);
			}
		}
	}
	public void createNotesScale(String note) {
		if(!note.isEmpty()) {
			Element n = new Element("note", nsMods);
			n.setAttribute("type", "scale");
			n.addContent(note);
			root.addContent(n);
		}
	}
	public void createTyp(String typ) {
		if(!typ.isEmpty()) {
			Element p = new Element("typeOfResource", nsMods);
			p.addContent(typ);
			root.addContent(p);
		}
	}
	public void createSimpleModsElement(String name, String value) {
		if(!value.isEmpty()) {
			Element e = new Element(name, nsMods);
			e.addContent(value);
			root.addContent(e);
		}
	}
	public void createSprachen(ArrayList<String> array) {
		if(!array.isEmpty()) {
			Element e2 = new Element("language", nsMods);
			for (String s : array) {
				String c = this.getSprachCode(s);
				if(!c.isEmpty()) {
					Element e = new Element("languageTerm", nsMods);
					e.addContent(c);
					e.setAttribute("authority", "rfc4646");
					e.setAttribute("type", "code");
					e2.addContent(e);
				}
			}
			root.addContent(e2);
		}
	}
	public void createExtents(ArrayList<String> extents) {
		if(!extents.isEmpty()) {
			boolean hatPhysicalDescription = false;
			Element e2 = new Element("physicalDescription", nsMods);
			for (String s : extents) {
				if(s.startsWith(","))
					s = s.substring(1).trim();
				if(!s.isEmpty()) {
					hatPhysicalDescription = true;
					Element e = new Element("extent", nsMods);
					if(s.contains("cm")) {
						s = s.replace(" cm", "").trim();
						e.setAttribute("unit", "cm");
					}
					e.addContent(s);
					e2.addContent(e);
				}
			}
			if(hatPhysicalDescription)
				root.addContent(e2);
		}
	}
	public void createProjektion(String projektion) {
		String pr = projektion.replace("Projektion", "").trim();
		if(!pr.trim().isEmpty()) {
			Element p = new Element("classification", nsMods);
			p.setAttribute("displayLabel", "projection");
			p.setAttribute("authorityURI", "http://kartenspeicher.gbv.de/classifications/projection");
			p.setAttribute("valueURI", "http://kartenspeicher.gbv.de/classifications/projection#" + pr);
			root.addContent(p);  
		}
	}
	public void createHaupttitel(String haupttitel, String titelsprache, String titelnummer, String untertitel1, String untertitel2) {
		if(!haupttitel.isEmpty()) {

			Element titleInfo = new Element("titleInfo", nsMods);
			titleInfo.setAttribute("lang", titelsprache, nsXml);
			Element title = new Element("title", nsMods);
			title.addContent(haupttitel);
			titleInfo.addContent(title);
	
			Element nr = new Element("partNumber", nsMods);
			if(!titelnummer.isEmpty()) {
				nr.addContent(titelnummer);
				titleInfo.addContent(nr);
			}
			Element u1 = new Element("subTitle", nsMods);
			if(!untertitel1.isEmpty()) {
				u1.addContent(untertitel1);
				titleInfo.addContent(u1);
			}
			Element u2 = new Element("subTitle", nsMods);
			if(!untertitel2.isEmpty()) {
				u2.addContent(untertitel2);
				titleInfo.addContent(u2);
			}
			root.addContent(titleInfo);
		}
	}
	public void createAlternativtitel(ArrayList<String> alternativtitel) {
		if(!alternativtitel.isEmpty()) {
			Element o = new Element("titleInfo", nsMods);
			o.setAttribute("type", "alternative");
			o.setAttribute("lang", "de", nsXml);
			for (String s : alternativtitel) {
				if(!s.isEmpty()) {
					Element h = new Element("title", nsMods);
					h.addContent(s);
					o.addContent(h);
				}
			}
			root.addContent(o);
		}
	}
	public void createLocation(String location, String url) {
		if(!(location + url).isEmpty()  ) {
			Element o = new Element("location", nsMods);
			if(!location.isEmpty()) {
				Element lo = new Element("shelfLocator", nsMods);
				lo.addContent(location);
				o.addContent(lo);
			}
			if(!url.isEmpty()) {
				Element u = new Element("url", nsMods);
				u.addContent(url);
				o.addContent(u);
			}
			root.addContent(o);
		}
	}
	public void createCorporate(String institute) {
		if(!institute.isEmpty()) {
			Element o = new Element("name", nsMods);
			o.setAttribute("type", "corporate");
			o.setAttribute("authorityURI", "http://www.mycore.org/classifications/mir_institutes");
			o.setAttribute("valueURI", "http://www.mycore.org/classifications/mir_institutes#" + institute);
			
			Element role = new Element("role", nsMods);
			
			Element roleterm = new Element("roleTerm", nsMods);
			roleterm.setAttribute("authority", "marcrelator");
			roleterm.setAttribute("type", "code");
			roleterm.addContent("his");
			
			role.addContent(roleterm);
			o.addContent(role);
			root.addContent(o);
		}
	}
	public void createClassification(TreeSet<String> illustrationClassifications) {
		for (String c : illustrationClassifications) {
			if(!c.isEmpty()) {
				Element e = new Element("classification", nsMods);
				e.setAttribute("displayLabel", "illustration");
				e.setAttribute("authorityURI", "http://kartenspeicher.gbv.de/classifications/x001148x");
				e.setAttribute("valueURI", "http://kartenspeicher.gbv.de/classifications/x001148x#" + c);
				root.addContent(e);
			}
		}
	}
	/**
	 * @param node
	 * @return true if the content is empty and can be removed
	 */
	public static boolean removeEmptyNodes(Content node) {
	    if (node instanceof Element) {
	        List<Content> content = ((Element) node).getContent();
	        List<Content> elementsToRemove =
	content.stream().filter(s->removeEmptyNodes(s))
	                .collect(Collectors.toList());
	        elementsToRemove.forEach(((Element) node)::removeContent);
	        return content.size() == 0 && ((Element)
	node).getAttributes().size() == 0;
	    } else if (node instanceof Text) {
	        return ((Text) node).getTextTrim().equals("");
	    } else if (node instanceof ProcessingInstruction || node instanceof
	EntityRef) {
	        return false;
	    }
	    return true;
	}
}
