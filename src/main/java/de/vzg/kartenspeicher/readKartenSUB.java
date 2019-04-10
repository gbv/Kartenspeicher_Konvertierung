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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.vzg.mods.MODSElement;

/**
 * liest die pica+-Titel der SUB-Göttingen aus,
 * übergibt sie einzeln an picaTitel2-Objekte oder holt die 
 * aufbereiteten Einträge von dort
 * und übergibt an ein MODS-Objekt, 
 * das die einzelnen mods-Elemente bildet und file schreibt
 * @author liess
 * 08.2017 erstellt für die Konvertierung der IKAR-Karten aus der 1.68
 * zum http://kartenspeicher.gbv.de mir-MODS
 */
public class readKartenSUB {

	public static void main(String[] args) {
		String pfad = "C:/sab/MuseenKram/Kartenspeicher/KartenSUB/";
		String institute = "SUB_Göttingen";
		
		String quelldatei = "SUBKarten168-cbs_1064.pp";
		String zielPfad = pfad + "export/";
//		String quelldatei = "kartenTest.pp";
//		String zielPfad = pfad + "exporttest/";

		String splitterTitel = "001A 0";
		String delimiterZeilen = "\n";
		String delimiterUnterfelder = "" + (char)31;// default
		System.out.println(getDateTimestamp("dd.MM.yyyy kk:mm:ss"));
		// den ganzen pica+Abzug auslesen
		System.out.println("lese aus pica+-Abzug" + "\n--> " + pfad + quelldatei + "\n" 
						+ "und exportiere zu modsfile je Titel nach" + "\n--> " + zielPfad);
		String all = "";//FileKram.readFile(new File(pfad + quelldatei));
		String[] titels = all.split(splitterTitel);
		System.out.println("\nfinde: " + (titels.length-1) + " Titel");
		
		// alle Titel einzeln ///////////////////////////////
		for (int i = 1; i < titels.length; i++) {
			String picaTitel = splitterTitel + titels[i];
			// normalize für XML
//			picaTitel = java.text.Normalizer.normalize(picaTitel, java.text.Normalizer.Form.NFC);
			picaTitel2 pTitel = new picaTitel2(picaTitel, delimiterZeilen, delimiterUnterfelder);
			
//			if(true)
//				continue;
			// hole ppn
			String ppn = pTitel.getPPN003();
			
			// generiere mods mit filename
			String filename = ppn + ".xml";
			MODS m = new MODS( zielPfad, filename);
			m.setModsFile(zielPfad + filename);
			
			// hole und setze Typ --> immer "cartographic"
			// SUB-Titel haben nur K oder O
			// für alle:
			m.createCorporate(institute);
			m.createTyp("cartographic");
			m.createSdnb("910");
			m.createAccessConditionCC0();
			
			m.createGenre();
			
			m.createID(ppn);
			
			// hole und setze Sprachcodes und Titelsprache direkt in pTitel
			m.createSprachen(pTitel.getSprachen());
			
			String haupttitel = pTitel.getSingleUnterfeldKat0("021A", 'a');

			haupttitel = haupttitel.replace("@", "");
			String h = pTitel.getSingleUnterfeldKat0("021B", 'a');
			h = h.replace("@", "");
			if(!h.isEmpty())
				haupttitel = h + " enthalten in: " + haupttitel;
			String titelnummer = pTitel.getSingleUnterfeldKat0("031A", 'y');
			
			String untertitel1 = pTitel.getSingleUnterfeldKat0("021A", 'd');
			String untertitel2 = pTitel.getSingleUnterfeldKat0("021A", 'h');
			untertitel1 = untertitel1.replace("@", "");
			untertitel2 = untertitel2.replace("@", "");
			m.createHaupttitel(haupttitel, pTitel.getTitelsprache(), 
									titelnummer, untertitel1, untertitel2);
			
			m.createAlternativtitel(pTitel.getAlternativtitel());
			
			// alles für subject /////////////
			ArrayList<String> topicKats = new ArrayList<>();
			topicKats.add("041A");
			topicKats.add("041A/01");
			topicKats.add("041A/10");
			topicKats.add("041A/11");
			topicKats.add("044K");
			topicKats.add("044K/01");
			topicKats.add("044K/02");
			topicKats.add("044K/03");
			topicKats.add("044K/04");
			topicKats.add("044K/05");
			topicKats.add("044K/06");
			topicKats.add("044K/07");
			topicKats.add("044K/08");
			topicKats.add("044K/09");
			pTitel.setTopics(topicKats);
			
			m.createTopics(pTitel.getTopicsxxxgnd());
			m.createTopicsGND(pTitel.getTopicsxxxgnd());
			
			m.createMassstab(pTitel.getFilteredMassstab());
			m.createNotesScale(pTitel.getMassstab());
			
			m.createCoordinates(pTitel.getKoordinaten());
			
			m.createSubject();
			
			////////////
			ArrayList<String> noteKats = new ArrayList<>();
			noteKats.add("037A");
			noteKats.add("046R");
			noteKats.add("046S");
			noteKats.add("047C");
			pTitel.setNotes(noteKats);
			m.createNotesContent(pTitel.getNotes());
			
			// hole und setze extents
			// hier ist die Reihenfolge wichtig!
			ArrayList<String> extents = new ArrayList<>();
			// hole Umfang
			String umfang = pTitel.getSingleUnterfeldKat0("034D", 'a');
			extents.add(umfang);
			// hole Format
			String format = pTitel.getSingleUnterfeldKat0("034I", 'a');
			extents.add(format);
			
			// hole Illustrationsangaben
			String ill = pTitel.getSingleUnterfeldKat0("034M", 'a');
			// hole Herstellungsmethode aus Illustrationsangaben
			String illneu = pTitel.setIllustrationClassifications(ill);
			if(!illneu.isEmpty())
				if(!illneu.equals("ich"))
					extents.add(illneu);
			m.createExtents(extents);
			
			m.createClassification(pTitel.getIllustrationClassifications());

			m.createProjektion(pTitel.getSingleUnterfeldKat0("035F", 'a'));
			
			//	Sign. Druckausgabe	und url zum Digitalisat
			String loc = pTitel.getSingleUnterfeldKat0("009A", 'b');
			if(!loc.isEmpty())	
				loc += " - ";
			String b = pTitel.getSingleUnterfeldKat0("009A", 'c');
			if(!b.isEmpty()) 
				if(!loc.isEmpty())
					loc += b;
			String a = pTitel.getSingleUnterfeldKat0("009A", 'a');
			if(!a.isEmpty()) 
				if(!loc.isEmpty())
					loc = loc + " - " + a;
			String url = pTitel.getSingleUnterfeldKat0("009P/03", 'a');
			if(url.endsWith("PPN"))
				url = "";
			url = url.replace("www-gdz.sub.uni-goettingen.de/cgi-bin/digbib.cgi"
					, "resolver.sub.uni-goettingen.de/purl");
//			if(!url.isEmpty())
//				System.out.println(url + " --> zu ikarppn: " + ppn);
//009P/03  $S1 $0cgi $ahttp://www-gdz.sub.uni-goettingen.de/cgi-bin/digbib.cgi?PPN
//http://www-gdz.sub.uni-goettingen.de/cgi-bin/digbib.cgi?PPN341861871
//http://resolver.sub.uni-goettingen.de/purl?PPN344084353
			m.createLocation(loc, url);
			
			// OriginInfoPublication ///////////////////////////////
			m.createVerlag(pTitel.getVerlag(), pTitel.getVerlagsort());
			m.createDateIssued(pTitel.getJahrIssued()
					, pTitel.getJahrIssuedVon(), pTitel.getJahrIssuedBis());
			//	Digidate	011B $a	2002
			m.createDateCaptured(pTitel.getSingleUnterfeldKat0("011B", 'a'));
			String edition = pTitel.getSingleUnterfeldKat0("032B", 'a');
			edition += " " + pTitel.getSingleUnterfeldKat0("032@", 'a');
			edition = edition.trim();
			edition += " " + pTitel.getSingleUnterfeldKat0("032@", 'c');
			m.createEdition(edition.trim());
			m.createOriginInfoPublication();
			////////////////////////////////////////////////////////

			// OriginInfoCreation //////////////////////////////////
			// aus "011@", 'a'
			m.createDateCreated(pTitel.getJahrCreated()
									, pTitel.getJahrCreatedVon()
										,pTitel.getJahrCreatedBis() );
			m.createOriginInfoCreation();
			///////////////////////////////////////////////////////
			
			// personal /////////////////////////////
			pTitel.getAutos().stream().map(MODSElement::getElement).forEach(m.root::addContent);

			// relatedItem //////
			m.createSingleSeries(pTitel.getSeriesTitel());
			
			ArrayList<String> ppns = new ArrayList<>();
			String p = pTitel.getSingleUnterfeldKat0("036D", '9');
			if(!p.isEmpty())
				ppns.add(p);
			p = pTitel.getSingleUnterfeldKat0("036F", '9');
			if(!p.isEmpty())
				ppns.add(p);
			p = pTitel.getSingleUnterfeldKat0("036F/01", '9');
			if(!p.isEmpty())
				ppns.add(p);
			p = pTitel.getSingleUnterfeldKat0("036F/02", '9');
			if(!p.isEmpty())
				ppns.add(p);
			p = pTitel.getSingleUnterfeldKat0("039B", '9');
			if(!p.isEmpty())
				ppns.add(p);
			m.createRelatedItemsPPN(ppns);
			//////////////////////////

			m.createExtension(pTitel.getPicaXMLfaehig());
//			m.createExtension(pTitel.getTitelXMLfaehig());
			
//			m.printMods2Console();
			m.printMods2File();
		}
		System.out.println("fertig jetzt");
		System.out.println(getDateTimestamp("dd.MM.yyyy kk:mm:ss"));
	}

	public static String getDateTimestamp(String syntax){
		//yyyy-MM-dd'T'HH:mm:ss:SS");
		SimpleDateFormat dateFormat = new SimpleDateFormat(syntax);
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		return date;
	}
}
