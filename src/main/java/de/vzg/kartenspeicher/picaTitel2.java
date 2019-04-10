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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import de.vzg.mods.MODSName;
import de.vzg.mods.MODSNamePart;

/**
 * verwaltet einen pica+-Titel, organisiert die Inhalte zum Zugriff von außen
 * in diverse Strukturen;
 * erstellt für die Konvertierung Kartenspeicher
 * @author liess
 * 08.2017 erstellt für die Konvertierung der IKAR-Karten aus der 1.68
 * zum http://kartenspeicher.gbv.de mir-MODS
 * 25.10.17 neu: String display = (vorname + " " + name).trim();
 * 03.11.17 titelOriginal und titelXMLfaehig neu, titelXMLfaehig normalized 
 * 			und ohne eckige Klammern
 * 			massstab normalisiert herausfiltern
 */

public class picaTitel2 {

    private String titelOriginal;  // der gesamte Titel

    private String titelCleaned;

    private String picaXMLfaehig;

    private String delimiterZeilen;

    private String delimiterUnterfelder;

    private LinkedHashSet<String> zeilen0; // bib-Kats

    private LinkedHashSet<String> zeilen1; // lokale Kats

    private HashSet<String> katsVorkommen;

    private String exemplare; // 200er Kats

    private HashMap<String, ArrayList<String>> hashKats0;

    private HashMap<String, ArrayList<String>> hashKats1;

    private String verlag = "";

    private String verlagsort = "";

    private ArrayList<String> sprachen = new ArrayList<>();

    private String titelsprache = "de";

    private String haupttitel = "";

    private ArrayList<String> alternativtitel = new ArrayList<>();

    private ArrayList<String> topicsxxxgnd = new ArrayList<>();

    private ArrayList<String> notes = new ArrayList<>();

    private TreeSet<String> illustrationClassifications = new TreeSet<String>();

    private HashMap<String, String> classis = new HashMap<>();

    private String jahrCreated = "";

    private String jahrCreatedVon = "";

    private String jahrCreatedBis = "";

    private String jahrIssued = "";

    private String jahrIssuedVon = "";

    private String jahrIssuedBis = "";

    private LinkedHashSet<String> autorenZeilen;

    private ArrayList<MODSName> autos;

    private String seriesTitel = "";

    private ArrayList<String> relatedItemPPNs = new ArrayList<>();

    private String koordinatenABCD = "";

    private String koordinatenKonvertiert = "";

    private String massstab = "";

    private String filteredMassstab = "";

    private String ppn003;

    private char typ002;

    public picaTitel2(String titel) {
        super();
        if (titel.isEmpty())
            System.err.println("picaTitel: leerer Titel");
        else {
            this.titelOriginal = titel;
            this.delimiterZeilen = "\n";
            this.delimiterUnterfelder = "" + (char) 31;
            extractTitel();
        }
    }

    public picaTitel2(String titel, String delimiterZeilen, String delimiterUnterfelder) {
        super();
        if (titel.isEmpty())
            System.err.println("picaTitel: leerer Titel");
        else {
            this.titelOriginal = titel;
            this.delimiterZeilen = delimiterZeilen;
            this.delimiterUnterfelder = delimiterUnterfelder;
            extractTitel();
        }
    }

    private void extractTitel() {
        initFields();
        cleanTitel();
        setPicaXMLfaehig();
        setZeilen0_1_exemplare();
        setHashKats0();
        setHashKats1();
        setTyp002();
        setPPN003();
        setVerlage033A();
        setSprachen();
        setHaupttitel();
        setAlternativtitel();
        setClassis();
        filterJahreCreated_011ata();
        filterJahrIssued_011atn();
        setMassstab();
        filterMassstab_035E();
        extractAutoren();
        setKoordinatenABCD();
        setSeriesTitel(); // hier nur einmal 036L $a
        setKoordinatenKonvertiert();
        setSeriesTitel();
    }

    private void setClassis() {
        classis.put(", Lith", "x001166x");
        classis.put(", Lithogr", "x001166x");
        classis.put("Lith ;", "x001166x");
        classis.put("Lith. ,", "x001166x");
        classis.put("Lith.;", "x001166x");
        classis.put("Lith,", "x001166x");
        classis.put("Lith", "x001166x");
        classis.put("Lithogr.", "x001166x");
        classis.put("Lithogr.;", "x001166x");
        classis.put("Lithogr", "x001166x");
        classis.put("Lithographie;", "x001166x");
        classis.put("Lithograpphie;", "x001166x");
        classis.put("Lithographie", "x001166x");
        classis.put("Farblithogr", "x001166x");
        classis.put("Handzeichnung", "x001235x");
        classis.put("Holzschnitt", "x001177x");
        classis.put("Holzschnitt ;", "x001177x");
        classis.put("kupferstich ,", "x001192x");
        classis.put("Kuperstich", "x001192x");
        classis.put("Kupferst.", "x001192x");
        classis.put("Kupferst.,", "x001192x");
        classis.put("Kupferstich", "x001192x");
        classis.put("Kupferstich ,", "x001192x");
        classis.put("Kupferstich ;", "x001192x");
        classis.put("Kupferstich,", "x001192x");
        classis.put("Kupferstich.", "x001192x");
        classis.put("Kupfestich", "x001192x");
        classis.put("Kupferst", "x001192x");
        classis.put("Radierung", "x001196x");
        classis.put("Stahlstich", "x001202x");
        classis.put("Zinkdruck", "ts005001");
        classis.put("Zinkogr", "ts005001");
        classis.put("Zinkographie", "ts005001");

        classis.put("einfarb.", "lb000025");
        classis.put("einfarbig", "lb000025");
        classis.put("farb.", "tr000029");
        classis.put("mehrfarb.", "tr000029");
        classis.put("mehrfarbig", "tr000029");
        classis.put("schw.-weiß", "lb000025");

        //		classis.put("Typometrie");
        //		classis.put("grenzkol");
    }

    private void initFields() {
        exemplare = "";
        zeilen0 = new LinkedHashSet<>();
        zeilen1 = new LinkedHashSet<>();
        katsVorkommen = new HashSet<>();
        hashKats0 = new HashMap<>();
        hashKats1 = new HashMap<>();
        //		this.setPicaXMLfaehig();
    }

    /**
     * setzt titelXMLfaehig,
     * entfernt eckige Klammer aus dem Titel und normalisiert
     */
    private void cleanTitel() {
        this.titelCleaned = titelOriginal.replace("[", "");
        this.titelCleaned = titelCleaned.replace("]", "");
        //		titel = titel.replace("", "");
        this.titelCleaned = java.text.Normalizer.normalize(this.titelCleaned, java.text.Normalizer.Form.NFC);
    }

    /**
     * @param kat
     * @return String pica+-Zeile ohne Kategorienummer, also die Unterfelder
     */
    private String getSingleZeilenrest(String kat) {
        ArrayList<String> all = this.getZeilenOhneKatnrKat0(kat);
        if (all != null)
            return all.get(0);
        else
            return "";
    }

    private void setAutorenZeilen() {
        this.autorenZeilen = new LinkedHashSet<>();
        autorenZeilen.add(this.getSingleZeilenrest("028A"));
        autorenZeilen.add(this.getSingleZeilenrest("028B/01"));
        autorenZeilen.add(this.getSingleZeilenrest("028B/02"));
        autorenZeilen.add(this.getSingleZeilenrest("028B/03"));
        autorenZeilen.add(this.getSingleZeilenrest("028B/04"));
        autorenZeilen.add(this.getSingleZeilenrest("028C"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/01"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/02"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/03"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/04"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/05"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/06"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/07"));
        autorenZeilen.add(this.getSingleZeilenrest("028C/08"));
        ArrayList<String> _C09 = this.getZeilenOhneKatnrKat0("028C/09");
        if (_C09 != null)
            for (String string : _C09) {
                autorenZeilen.add(string);
            }
    }

    public LinkedHashSet<String> getAutorenZeilen() {
        return this.autorenZeilen;
    }

    private void extractAutoren() {
        this.setAutorenZeilen();
        this.autos = new ArrayList<>();
        int nr = 0;
        for (String zeile : this.getAutorenZeilen()) {
            MODSName autor = new MODSName();
            String name = "";
            String vorname = "";
            String p = "";
            String gnd = "";
            String e = "";
            String f = "";
            String c = "";

            String vor9 = zeile;
            int index$9 = zeile.indexOf(delimiterUnterfelder + "9");
            if (index$9 >= 0) {
                vor9 = zeile.substring(0, index$9);
            }
            String[] unterfelder = zeile.split(delimiterUnterfelder);
            for (String u : unterfelder) {
                if (u.startsWith("E"))
                    e = u.substring(1);
                else if (u.startsWith("F"))
                    f = u.substring(1);
                else if (u.startsWith("0"))
                    gnd = u.substring(5);
                else if (u.startsWith("c"))
                    c = u.substring(1);
                else if (u.startsWith("P"))
                    p = u.substring(1);
            }
            String[] us = vor9.split(delimiterUnterfelder);
            for (String u : us) {
                if (u.startsWith("a"))
                    name = u.substring(1);
                else if (u.startsWith("d"))
                    vorname = u.substring(1);
                if (vorname.equals("..."))
                    vorname = "";
            }
            if (vorname.contains("/")) {
                name = (vorname.substring(vorname.indexOf('/') + 1) + " " + name).trim();
                vorname = (vorname.substring(0, vorname.indexOf('/'))).trim();
            }
            String display = (vorname + " " + name).trim();
            //			String display = name;
            //			if(!vorname.isEmpty())
            //				if(!display.isEmpty())
            //					display = display + ", " + vorname;
            //				else
            //					display = vorname;
            String date = e;
            if (!f.isEmpty())
                if (!date.isEmpty())
                    date = e + " - " + f;

            //			Cartographer [ctg]
            //			A person, family, or organization responsible for creating a map,
            //			atlas, globe, or other cartographic work

            if(!display.isEmpty()){
                autor.setDisplayForm(display);
            }
            if(!name.isEmpty()){
                autor.addNamePart(MODSNamePart.TYPE.family, name);
            }
            if(!vorname.isEmpty()){
                autor.addNamePart(MODSNamePart.TYPE.given, vorname);
            }
            if(!c.isEmpty()){
                autor.addNamePart(MODSNamePart.TYPE.termsOfAddress, c);
            }
            if(!date.isEmpty()){
                autor.addNamePart(MODSNamePart.TYPE.date, date);
            }
            if(!p.isEmpty()){
                autor.setDescription(p);
            }
            if(!gnd.isEmpty()){
                autor.addNameIdentifier("gnd", gnd);
            }

            if (autor.getElement().getChildren().size()>0) {
                autor.addRoleTerm("code", "marcrelator","ctg");
                nr++;
                this.autos.add(autor);
            }
        }
    }

    /**
     * liest Titelzeilen aus und füllt die collections
     * zeilen, zeilen0, zeilen1 und
     * String exemplare (hier müssen doppelte Kats bleiben)
     * ignoriert hier die Zeilen, die nicht mit 0, 1 oder 2 beginnen und die ohne Leerzeichen
     *
     */
    private void setZeilen0_1_exemplare() {
        if (!titelCleaned.isEmpty()) {
            String[] zs = titelCleaned.split(delimiterZeilen);
            for (int i = 0; i < zs.length; i++) {
                String zeile = zs[i].trim();
                if (!zeile.isEmpty()) {
                    char first = zeile.charAt(0);
                    if (first != '0' && first != '1' && first != '2')
                        continue;
                    if (zeile.contains(" ")) {
                        if (first == '0') {
                            zeilen0.add(zeile);
                        } else if (first == '1') {
                            zeilen1.add(zeile);
                        } else if (first == '2') {
                            exemplare += zeile + "\n";
                        }
                    }
                }
            }
        }
    }

    /**
     * liest zeilen0 aus und füllt die collections
     * katsVorkommen (sammelt alle vorkommenden KatNummern)
     * und hashKats0 (katnummer, array (zeileOhneKatnummer) )
     */
    private void setHashKats0() {
        for (String zeile : zeilen0) {
            String katnummer = zeile.substring(0, zeile.indexOf(' '));
            String zeileOhneKatnummer = zeile.substring(katnummer.length() + 1);
            this.katsVorkommen.add(katnummer);
            ArrayList<String> array = new ArrayList<>();
            if (hashKats0.containsKey(katnummer))
                array = hashKats0.get(katnummer);
            array.add(zeileOhneKatnummer);
            hashKats0.put(katnummer, array);
        }
    }

    /**
     * @param kat0
     * @return
     */
    public ArrayList<String> getZeilenOhneKatnrKat0(String kat0) {
        return hashKats0.get(kat0);

    }

    public ArrayList<String> getZeilenOhneKatnrKat1(String kat1) {
        return hashKats0.get(kat1);
    }

    /**
     * liest zeilen1 aus und füllt die collections
     * katsVorkommen und hashKats1
     */
    private void setHashKats1() { // ok
        for (String zeile : zeilen1) {
            String katnummer = zeile.substring(0, zeile.indexOf(' '));
            String zeileOhneKatnummer = zeile.substring(katnummer.length() + 1);
            this.katsVorkommen.add(katnummer);
            ArrayList<String> array = new ArrayList<>();
            if (hashKats1.containsKey(katnummer))
                array = hashKats1.get(katnummer);
            array.add(zeileOhneKatnummer);
            hashKats1.put(katnummer, array);
        }
    }

    public LinkedHashSet<String> getZeilen0() {
        return zeilen0;
    }

    public LinkedHashSet<String> getZeilen1() {
        return zeilen1;
    }

    public HashSet<String> getKatsVorkommen() {
        return katsVorkommen;
    }

    public int getAnzKat0Zeilen() {
        return zeilen0.size();
    }

    public int getAnzKat1Zeilen() {
        return zeilen1.size();
    }

    public String getExemplare() {
        return exemplare;
    }

    public String getTitelCleaned() {
        return this.titelCleaned;
        //		return this.titel;
    }

    public ArrayList<MODSName> getAutos() {
        return autos;
    }

    public ArrayList<String> getsplittedUnterfelder(String zeilenRest) {
        ArrayList<String> list = new ArrayList<>();
        String[] unterfelder = zeilenRest.split(delimiterUnterfelder);
        for (String u : unterfelder) {
            if (!u.isEmpty())
                list.add(u);
        }
        return list;
    }

    public String getsplittedUnterfeld(String zeilenRest, char u) {
        String[] unterfelder = zeilenRest.split(delimiterUnterfelder);
        for (String s : unterfelder) {
            if (!s.isEmpty())
                if (s.startsWith("" + u))
                    return s.substring(1);
        }
        return "";
    }

    public void setTitel(String titel) {
        this.titelCleaned = titel;
    }

    public HashMap<String, ArrayList<String>> getHashKats0() {
        return hashKats0;
    }

    public String getSingleUnterfeldKat0(String kat0, char ufeld) {
        String s = "";
        // alle Zeilen dieser kat
        ArrayList<String> array = this.hashKats0.get(kat0);
        if (array != null) {
            if (array.size() > 1) {
                System.err.println("FEHLER: picaTitel2.getSingleUnterfeldKat0: mehrere Einträge dieser Kategorie: "
                    + kat0 + " bei id " + this.ppn003);
            }
            // je zeile
            for (String zeile : array) {
                // alle Unterfelder dieser zeile
                ArrayList<String> splittedUs = getsplittedUnterfelder(zeile);
                // je unterfeld
                for (String unterfeld : splittedUs) {
                    char u = unterfeld.charAt(0);
                    if (u == ufeld) {
                        s = unterfeld.substring(1);
                    }
                }
            }
        }
        return s.trim();
    }

    public ArrayList<String> getUnterfelderKat0(String kat0, char ufeld) {
        ArrayList<String> us = new ArrayList<>();
        ArrayList<String> array = this.getZeilenOhneKatnrKat0(kat0);
        if (array != null) {
            for (String zeilenrest : array) {
                ArrayList<String> splittedUs = getsplittedUnterfelder(zeilenrest);
                for (String unterfeld : splittedUs) {
                    char u = unterfeld.charAt(0);
                    if (u == ufeld) {
                        us.add(unterfeld.substring(1));
                    }
                }
            }
        }
        return us;
    }

    private void setSprachen() {
        this.sprachen = this.getUnterfelderKat0("010@", 'a');
        if (!sprachen.isEmpty())
            this.titelsprache = sprachen.get(0);
    }

    public String setIllustrationClassifications(String ill) {
        String rest = ill;
        for (String c : this.classis.keySet()) {
            if (rest.contains(c)) {
                rest = rest.replace(c, "").trim();
                this.illustrationClassifications.add(classis.get(c));
            }
        }
        return rest;
    }

    public TreeSet<String> getIllustrationClassifications() {
        return illustrationClassifications;
    }

    public ArrayList<String> getSprachen() {
        return this.sprachen;
    }

    public HashMap<String, ArrayList<String>> getHashKats1() {
        return hashKats1;
    }

    public String getDelimiterUnterfelder() {
        return delimiterUnterfelder;
    }

    public void setDelimiterUnterfelder(String delimiterUnterfelder) {
        this.delimiterUnterfelder = delimiterUnterfelder;
    }

    public char getTyp002() {
        return typ002;
    }

    public String getJahrCreatedVon() {
        return jahrCreatedVon;
    }

    public String getJahrCreatedBis() {
        return jahrCreatedBis;
    }

    public String getJahrIssued() {
        return jahrIssued;
    }

    public String getJahrIssuedVon() {
        return jahrIssuedVon;
    }

    public String getJahrIssuedBis() {
        return jahrIssuedBis;
    }

    private void setTyp002() {
        ArrayList<String> a = this.getUnterfelderKat0("002@", '0');
        String s = a.get(0);
        char t = s.charAt(0);
        this.typ002 = t;
    }

    private void setPPN003() {
        String ppn = this.getSingleUnterfeldKat0("003@", '0');
        this.ppn003 = ppn;
    }

    private void filterJahrIssued_011atn() {
        String jahr = this.getSingleUnterfeldKat0("011@", 'n');
        String rest = "";
        if (jahr.contains("erschienen ")) {
            rest = jahr.substring(jahr.indexOf("erschienen ") + 11);
        } else if (jahr.contains("gedr. ")) {
            rest = jahr.substring(jahr.indexOf("gedr. ") + 6);
        }
        rest = rest.replace("[", "");
        rest = rest.replace("]", "");
        if (rest.startsWith("zwischen ")) {
            this.jahrIssuedVon = rest.substring(9, 13);
            this.jahrIssuedBis = rest.substring(rest.indexOf("u.") + 3);
        } else {
            this.jahrIssued = rest.trim();
        }
        //			1561 [erschienen 1585]
        //			gedr. 1482
        //			gedr. [zwischen 1482 u. 1491]
        //			[gedr. 1486]
        //			[gedr. 1482]
        //			[gedr. zwischen 1482 u. 1491]

    }

    private void filterMassstab_035E() {
        //		String massstab = this.getSingleUnterfeldKat0("035E", 'a');
        //		[Ca. 1:63.000]
        //		[Ca. 1:630.000 - 1:820.000]
        //		[Ca. 1:940.000. Beigef. Kt.: ca. 1:2.500.000]
        //		[Gleitender Maßstab]
        //		[Maßstabberechnung nicht möglich]
        //		[Nicht maßstäbig)
        //		[Nicht maßstäbig]
        //		[Versch. Maßstäbe]
        //		[Versch. Maßßstäbe]
        //		[Verschiedene Maßstäbe]
        //		[Vertikalmaßstab Ca. 1:70.000]
        if (!this.massstab.isEmpty()) {
            if (massstab.equals("1:"))
                massstab = "";
            //			if(this.getPPN003().equals("000254843"))
            //				System.err.println(massstab);
            filteredMassstab = massstab.replace("Ca.", "").trim();
            //			massstab = massstab.replace("", "");
            if (filteredMassstab.contains("1:")) {

                filteredMassstab = filteredMassstab.substring(filteredMassstab.indexOf("1:"));
                if (filteredMassstab.contains("i.e."))
                    filteredMassstab = filteredMassstab.substring(filteredMassstab.indexOf("i.e.")).trim();
                if (filteredMassstab.contains("i. e."))
                    filteredMassstab = filteredMassstab.substring(filteredMassstab.indexOf("i. e.")).trim();
                if (filteredMassstab.contains(" - "))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf(" - ")).trim();
                if (filteredMassstab.contains("6 Inche"))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf("6 Inche"));
                if (filteredMassstab.contains("6 inche"))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf("6 inche"));
                if (filteredMassstab.contains("#"))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf("#"));
                if (filteredMassstab.contains(". 1."))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf(". 1."));
                if (filteredMassstab.contains(". 2."))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf(". 2."));
                if (filteredMassstab.contains(" u. "))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf(" u. "));
                if (filteredMassstab.contains("-"))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf("-"));
                if (filteredMassstab.contains(". Beigef"))
                    filteredMassstab = filteredMassstab.substring(0, filteredMassstab.indexOf(". Beigef"));
                if (filteredMassstab.endsWith("/3"))
                    filteredMassstab = filteredMassstab.replace("/3", "");

                filteredMassstab = filteredMassstab.replace(".", "");
                filteredMassstab = filteredMassstab.replace(" ", "");
                filteredMassstab = filteredMassstab.replace("ie", "");
                //				if(!filteredMassstab.matches("1:[0-9]{0,10}")) {
                //					System.out.println(this.ppn003 + ": "+massstab +" ---> " + filteredMassstab);
                //				}
            }
        }
    }

    /**
     * @param jahr
     * @return TreeSet mit jahr, jahrVon, jahrBis
     */
    private void filterJahreCreated_011ata() {
        String jahr = this.getSingleUnterfeldKat0("011@", 'a');
        if (jahr.matches("[1-9][0-9]{0,3}")) {
            Integer i = new Integer(jahr);
            i.intValue();
            this.jahrCreated = jahr;
        } else if (jahr.equals("15XX")) {
            this.jahrCreatedVon = "1500";
            this.jahrCreatedBis = "1599";
        } else if (jahr.equals("16XX")) {
            this.jahrCreatedVon = "1600";
            this.jahrCreatedBis = "1699";
        } else if (jahr.equals("17XX")) {
            this.jahrCreatedVon = "1700";
            this.jahrCreatedBis = "1799";
        } else if (jahr.equals("18XX")) {
            this.jahrCreatedVon = "1800";
            this.jahrCreatedBis = "1800";
        } else if (jahr.equals("19XX")) {
            this.jahrCreatedVon = "1900";
            this.jahrCreatedBis = "1999";
        } else if (jahr.equals("XXXX")) {
        } else if (jahr.equals("[1744]")) {
            this.jahrCreated = "1744";
        } else if (jahr.equals("[1742]")) {
            this.jahrCreated = "1742";
        } else if (jahr.equals("o668")) {
            this.jahrCreated = "1668";
        } else if (jahr.equals("1796/97")) {
            this.jahrCreatedVon = "1796";
            this.jahrCreatedBis = "1797";
        } else if (jahr.equals("s.a. zwischen 1682 u. 1724")) {
            this.jahrCreatedVon = "1682";
            this.jahrCreatedBis = "1724";
        } else if (!jahr.isEmpty()) {
            System.err.println("picaTitel2:setJahre() klappt nicht für: " + jahr);
        }
    }

    private void setAlternativtitel() {
        String a = this.getSingleUnterfeldKat0("021A", 'f');
        a = a.replace("@", "");
        String b = this.getSingleUnterfeldKat0("027A", 'a');
        b = b.replace("@", "");
        String c = this.getSingleUnterfeldKat0("046A", 'a');
        c = c.replace("@", "");
        String d = this.getSingleUnterfeldKat0("046C", 'a');
        d = d.replace("@", "");
        if (!a.isEmpty())
            this.alternativtitel.add(a);
        if (!b.isEmpty())
            this.alternativtitel.add(b);
        if (!c.isEmpty())
            this.alternativtitel.add(c);
        if (!d.isEmpty())
            this.alternativtitel.add(d);
    }

    private void setKoordinatenABCD() {
        String koA = this.getSingleUnterfeldKat0("035G", 'a');
        String koB = this.getSingleUnterfeldKat0("035G", 'b');
        String koC = this.getSingleUnterfeldKat0("035G", 'c');
        String koD = this.getSingleUnterfeldKat0("035G", 'd');
        this.koordinatenABCD = koA + koB + koC + koD;
    }

    private void setKoordinatenKonvertiert() {
        if (!this.koordinatenABCD.isEmpty())
            this.koordinatenKonvertiert =
                CoordinateConverter.convertCoordinate(this.koordinatenABCD);
    }

    private void setPicaXMLfaehig() {
        this.picaXMLfaehig = this.titelOriginal.replace("" + (char) 30, " ");
        this.picaXMLfaehig = picaXMLfaehig.replace("" + (char) 29, " ");
        this.picaXMLfaehig = picaXMLfaehig.replace(delimiterUnterfelder, "$");
    }

    /**
     * lese nur 1 verlag aus, die restlichen sind falsche eingaben, nur orte
     */
    private void setVerlage033A() {
        ArrayList<String> vsZeilen = this.getZeilenOhneKatnrKat0("033A");
        if (vsZeilen != null)
            for (String zeile : vsZeilen) {
                this.verlag = this.getsplittedUnterfeld(zeile, 'n');
                if (!this.verlag.isEmpty()) {
                    this.verlagsort = this.getsplittedUnterfeld(zeile, 'p');
                    return;
                }
            }
    }

    public String getVerlag() {
        return verlag;
    }

    public String getVerlagsort() {
        return verlagsort;
    }

    public String getPPN003() {
        return this.ppn003;
    }

    private void setMassstab() {
        this.massstab = this.getSingleUnterfeldKat0("035E", 'a');
    }

    public String getMassstab() {
        return this.massstab;
    }

    public String getHaupttitel() {
        return haupttitel;
    }

    public void setHaupttitel() {
        this.haupttitel = this.getSingleUnterfeldKat0("021A", 'a');
        String h = this.getSingleUnterfeldKat0("021B", 'a');
        if (!h.isEmpty())
            this.haupttitel = h + " enthalten in: " + haupttitel;
    }

    public String getJahrCreated() {
        return jahrCreated;
    }

    public void setTopics(ArrayList<String> topicKats) {
        for (String kat : topicKats) {
            ArrayList<String> zeilen = this.getZeilenOhneKatnrKat0(kat);
            if (zeilen != null)
                for (String s : zeilen) {
                    String a = "";
                    String gnd = "";
                    ArrayList<String> splittedZeile = this.getsplittedUnterfelder(s);
                    for (String u : splittedZeile) {
                        if (u.startsWith("a")) {
                            a = u.substring(1);
                        }
                        if (u.startsWith("0")) {
                            gnd = u.substring(1);
                        }
                    }
                    if (!gnd.isEmpty())
                        a += "xxx" + gnd;
                    this.topicsxxxgnd.add(a);
                }
        }
    }

    public String getKoordinaten() {
        return this.koordinatenKonvertiert;
    }

    public String getSeriesTitel() {
        return seriesTitel;
    }

    public void setSeriesTitel() {
        this.seriesTitel = this.getSingleUnterfeldKat0("036L", 'a');
    }

    public ArrayList<String> getRelatedItemPPNs() {
        return relatedItemPPNs;
    }

    public void setRelatedItemPPNs(ArrayList<String> relatedItemPPNs) {
        this.relatedItemPPNs = relatedItemPPNs;
    }

    public ArrayList<String> getTopicsxxxgnd() {
        return this.topicsxxxgnd;
    }

    public ArrayList<String> getNotes() {
        return this.notes;
    }

    public void setNotes(ArrayList<String> noteKats) {
        // für jede Kat
        for (String kat0 : noteKats) {
            // alle Zeilen dieser kat
            ArrayList<String> array = this.hashKats0.get(kat0);
            // jede Zeile
            if (array != null)
                for (String z : array) {
                    String a = this.getsplittedUnterfeld(z, 'a');
                    this.notes.add(a);
                }
        }
    }

    public ArrayList<String> getAlternativtitel() {
        return this.alternativtitel;
    }

    public String getFilteredMassstab() {
        return filteredMassstab;
    }

    //	public void setUntertitel1(String untertitel1) {
    //		Untertitel1 = untertitel1;
    //	}
    //	public String getUntertitel2() {
    //		return Untertitel2;
    //	}
    //	public void setUntertitel2(String untertitel2) {
    //		Untertitel2 = untertitel2;
    //	}
    //	public String getAlternativtitel1() {
    //		return alternativtitel1;
    //	}
    //	public void setAlternativtitel1(String alternativtitel1) {
    //		this.alternativtitel1 = alternativtitel1;
    //	}
    //	public void setAlternativtitel2(String alternativtitel2) {
    //		this.alternativtitel2 = alternativtitel2;
    //	}
    //	public String getAlternativtitel2() {
    //		return alternativtitel2;
    //	}
    //	public void setAlternativtitel3(String alternativtitel3) {
    //		this.alternativtitel3 = alternativtitel3;
    //	}
    //	public String getAlternativtitel3() {
    //		return alternativtitel3;
    //	}
    //	public void setAlternativtitel4(String alternativtitel4) {
    //		this.alternativtitel4 = alternativtitel4;
    //	}
    //	public String getAlternativtitel4() {
    //		return alternativtitel4;
    //	}
    public String getTitelsprache() {
        return this.titelsprache;
    }

    public String getPica() {
        return this.titelCleaned;
    }

    public String getPicaXMLfaehig() {
        return this.picaXMLfaehig;
    }

    public String getSprachCode(String sprache) {
        //		Rfc4646, eigentlich nur iso 639.1
        String rfc = "";
        switch (sprache.toLowerCase()) {
            case "dan":
                rfc = "da";
                break;
            case "dut":
                rfc = "nl";
                break;
            case "eng":
                rfc = "en";
                break;
            case "fre":
                rfc = "fr";
                break;
            case "ger":
                rfc = "de";
                break;
            case "ita":
                rfc = "it";
                break;
            case "lat":
                rfc = "la";
                break;
            case "rus":
                rfc = "ru";
                break;
            case "spa":
                rfc = "es";
                break;
            default:
                break;
        }
        return rfc;
    }
}
