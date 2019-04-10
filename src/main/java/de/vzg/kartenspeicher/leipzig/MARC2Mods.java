package de.vzg.kartenspeicher.leipzig;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import de.vzg.mods.MODSAbstract;
import de.vzg.mods.MODSAccessCondition;
import de.vzg.mods.MODSCartographics;
import de.vzg.mods.MODSClassification;
import de.vzg.mods.MODSDocument;
import de.vzg.mods.MODSGenre;
import de.vzg.mods.MODSGeographic;
import de.vzg.mods.MODSLanguage;
import de.vzg.mods.MODSLanguageTerm;
import de.vzg.mods.MODSLocation;
import de.vzg.mods.MODSName;
import de.vzg.mods.MODSNote;
import de.vzg.mods.MODSOriginInfo;
import de.vzg.mods.MODSPhysicalDescription;
import de.vzg.mods.MODSPublisher;
import de.vzg.mods.MODSRoleTerm;
import de.vzg.mods.MODSShelfLocator;
import de.vzg.mods.MODSSubject;
import de.vzg.mods.MODSTitleInfo;
import de.vzg.mods.MODSTopic;
import de.vzg.mods.MODSURL;

public class MARC2Mods {

    public static final Namespace MARC_NAMESPACE = Namespace.getNamespace("marc", "http://www.loc.gov/MARC21/slim");

    public static final String TARGET = "/home/paschty/karten/";

    private static int current = 0;

    private static Map<String, List<String>> urlShelfMap = initMap();

    private final Element marcRecordElement;

    public MARC2Mods(Element marcRecordElement) {
        this.marcRecordElement = marcRecordElement;
    }

    private static Map<String, List<String>> initMap() {
        ConcurrentHashMap<String, List<String>> result = new ConcurrentHashMap<>();
        String[] headers = new String[] { "Dateiname", "Signatur", "Titel", "Link", "Größe", "Datum" };
        try (Reader r = new InputStreamReader(
            MARC2Mods.class.getClassLoader().getResourceAsStream("csv/iflzkw_files.tsv"))) {
            Iterable<CSVRecord> records = CSVFormat.newFormat('\t')
                .withHeader(headers)
                .withFirstRecordAsHeader()
                .withIgnoreSurroundingSpaces()
                .withTrim()
                .parse(r);
            StreamSupport.stream(records.spliterator(), false).forEach(record -> {
                final String signatur = record.get(headers[1]);
                final String link = record.get(headers[3]);
                result.computeIfAbsent(signatur, (s) -> new ArrayList<>()).add(link);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        //final MCRXSLTransformer transformer = new MCRXSLTransformer("xsl/MARC21slim2MODS3-6.xsl");
        //transformer.setTransformerFactory("net.sf.saxon.TransformerFactoryImpl");

        try (InputStream is = MARC2Mods.class.getClassLoader().getResourceAsStream("xml/iflzkw.xml")) {
            SAXBuilder saxBuilder = new SAXBuilder();
            final Document document = saxBuilder.build(is);
            XPathExpression<Element> xPathExpression = XPathFactory.instance()
                .compile("/marc:collection/marc:record", Filters.element(), null, MARC_NAMESPACE);

            final XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.setFormat(Format.getPrettyFormat());
            StringBuilder commands = new StringBuilder();
            for (Element el : xPathExpression.evaluate(document)) {
                try (OutputStream os = Files
                    .newOutputStream(Paths.get(TARGET).resolve(current + ".xml"))) {
                    xmlOutputter.output(new MARC2Mods(el).getMods().getElement(), os);
                    System.out.println("Verarbeite: " + current);
                }
                commands.append("load mods document from file " + TARGET).append(current).append(".xml");

                if (Files.exists(Paths.get(TARGET + current))) {
                    commands.append(" with files from directory " + TARGET).append(current).append("/");
                }
                commands.append(" for project iflzkw")
                    .append(System.lineSeparator());
                current++;/*
                if (current == 200)
                    break;*/
            }

            Files.write(Paths.get(TARGET + "commands.txt"), commands.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    private List<String> getURLSForShelf(String shelf) {
        return urlShelfMap.getOrDefault(shelf, new ArrayList<String>());
    }

    private Optional<String> getFirstDataSubfield(String datafield, String subfield) {
        XPathExpression<Element> xPathExpression = XPathFactory.instance()
            .compile("marc:datafield[@tag='" + datafield + "']/marc:subfield[@code='" + subfield + "']",
                Filters.element(),
                null, MARC_NAMESPACE);

        return Optional.ofNullable(xPathExpression.evaluateFirst(this.marcRecordElement)).map(Element::getTextTrim);
    }

    private List<Map<String, String>> getFields(String datafield) {
        XPathExpression<Element> xPathExpression = XPathFactory.instance()
            .compile("marc:datafield[@tag='" + datafield + "']",
                Filters.element(),
                null, MARC_NAMESPACE);

        return xPathExpression.evaluate(this.marcRecordElement).stream()
            .map(el -> {
                HashMap<String, String> entries = new HashMap<>();
                el.getChildren().stream().filter(subfield -> subfield.getName().equals("subfield"))
                    .forEach(field -> entries.put(field.getAttributeValue("code"), field.getText()));
                return entries;
            }).filter(hm -> !hm.isEmpty()).collect(Collectors.toList());
    }

    private MODSDocument getMods() {
        final MODSDocument modsDocument = new MODSDocument();

        getFirstDataSubfield("245", "a").ifPresent(title -> {
            final MODSTitleInfo titleInfo = new MODSTitleInfo(title);
            modsDocument.addElement(titleInfo);

            getFirstDataSubfield("245", "b").ifPresent(titleInfo::setSubTitle);
        });

        getFirstDataSubfield("245", "c").ifPresent(verantw->{
            final MODSName modsName = new MODSName();
            modsName.setType("corporate");
            modsName.setXLinkType("simple");
            modsName.setDisplayForm(verantw);
            modsName.addRoleTerm("code", "marcrelator", "rpy");
            modsDocument.addElement(modsName);
        });

        final MODSOriginInfo originInfo = new MODSOriginInfo("publication");
        getFirstDataSubfield("260", "a").ifPresent(originPlace -> {
            if (originPlace.startsWith("[") && originPlace.endsWith("]")) {
                originInfo.addPlaceTerm("text", originPlace.substring(1, originPlace.length() - 1));
            } else {
                originInfo.addPlaceTerm("text", originPlace);
            }
        });

        getFirstDataSubfield("260", "b").ifPresent(publisher -> {
            originInfo.addPublisher(new MODSPublisher(publisher));
        });

        getFirstDataSubfield("260", "c").ifPresent(dateIssued -> {
            String cleanedDate = dateIssued;
            if (dateIssued.startsWith("[") && dateIssued.endsWith("]")) { // alle daten ohne klammern
                cleanedDate = dateIssued.substring(1, dateIssued.length() - 1);
            }
            cleanedDate = cleanedDate.trim();
            if (cleanedDate.endsWith("?")) {
                cleanedDate = cleanedDate.substring(0, cleanedDate.length() - 1);
            }

            if (cleanedDate.matches("[0-9]+")) { // 1993 oder 2019
                originInfo.addDateIssued("w3cdtf", cleanedDate);
            } else if (cleanedDate.matches("[0-9]+-[0-9]+")) { // 1993-2019 oder 2019-2032
                //publicationInfo.addDateIssued(null, cleanedDate);
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("-")[0]).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("-")[1]).setAttribute("point", "end");
            } else if (cleanedDate.matches("[0-9]+/[0-9]+")) { // 1993-2019 oder 2019-2032
                //publicationInfo.addDateIssued(null, cleanedDate);
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("/")[0]).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("/")[1]).setAttribute("point", "end");
            } else if (cleanedDate.matches("ca. [0-9]+-[0-9]+")) { // ca. 1990-1993 oder ca. 2013-2018
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("ca. ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("-")[0]).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate.split("-")[1]).setAttribute("point", "end");
            } else if (cleanedDate.matches("ca. [0-9]+")) { // ca. 1990 oder ca. 2013
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("ca. ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "end");
            } else if (cleanedDate.matches("um [0-9]+")) { // um 1990 oder um 2013
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("um ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "end");
            } else if (cleanedDate.matches("nach [0-9]+")) { // nach 1990 oder nach 2013
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("nach ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "start");
            } else if (cleanedDate.matches("vor [0-9]+")) { // vor 1990 oder vor 2013
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("vor ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate).setAttribute("point", "end");
            } else if (cleanedDate
                .matches("zwischen [0-9]+ u. [0-9]+")) { // zwischen 1764 u. 1767 oder zwischen 1993 u. 2019
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("zwischen ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate.split(" u. ")[0]).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate.split(" u. ")[1]).setAttribute("point", "end");
            } else if (cleanedDate
                .matches("zwischen [0-9]+ und [0-9]+")) { // zwischen 1764 und 1767 oder zwischen 1993 und 2019
                originInfo.addDateIssued(null, cleanedDate);
                cleanedDate = cleanedDate.substring("zwischen ".length());
                originInfo.addDateIssued("w3cdtf", cleanedDate.split(" und ")[0]).setAttribute("point", "start");
                originInfo.addDateIssued("w3cdtf", cleanedDate.split(" und ")[1]).setAttribute("point", "end");
            } else {
                System.err.println("WEIRED DATE: " + cleanedDate);
                originInfo.addDateIssued(null, cleanedDate);
            }
        });

        if (originInfo.getElement().getChildren().size() > 0) {
            modsDocument.addElement(originInfo);
        }

        getFirstDataSubfield("500", "a").ifPresent(note -> {
            Stream.of(note.split(";")).forEach(subNote -> modsDocument.addElement(new MODSNote(subNote, "content")));
        });

        getFirstDataSubfield("520", "a").ifPresent(_abstract -> {
            modsDocument.addElement(new MODSAbstract(_abstract));
        });

        MODSSubject subject = new MODSSubject();
        MODSCartographics cartographics = new MODSCartographics();

        getFirstDataSubfield("225", "c").ifPresent(coords -> {
            String regexp = "([EN]) ([0-9]+)[°]([0-9]+)'([0-9]+)\"-([EN]) ([0-9]+)[°]([0-9]+)'([0-9]+)\"\\/([EN]) ([0-9]+)[°]([0-9]+)'([0-9]+)\"-([EN]) ([0-9]+)[°]([0-9]+)'([0-9]+)\"";

            Matcher matcher = Pattern.compile(regexp).matcher(coords);
            while (matcher.find()) {
                int grps = matcher.groupCount();
                if (grps == 16) {
                    String[] coordinates = new String[4];
                    int grpSize = 4;
                    for (int coordGrp = 0; coordGrp < 4; coordGrp++) {
                        String fromDirection = matcher.group((coordGrp * grpSize) + 1);
                        String fromDegreeStr = matcher.group((coordGrp * grpSize) + 2);
                        String fromMinutesStr = matcher.group((coordGrp * grpSize) + 3);
                        String fromSecondsStr = matcher.group((coordGrp * grpSize) + 4);

                        int fromDegree = Integer.parseInt(fromDegreeStr);
                        int fromMinutes = Integer.parseInt(fromMinutesStr);
                        int fromSeconds = Integer.parseInt(fromSecondsStr);

                        coordinates[coordGrp] =
                            (fromDirection.equals("E") || fromDirection.equals("N") ? 1 : -1) * (fromDegree + (
                                fromMinutes
                                    / 60.0) + (fromSeconds / 3600.0)) + "";
                    }

                    final int LEFT = 0;
                    final int RIGHT = 1;
                    final int TOP = 3;
                    final int BOTTOM = 2;

                    final String points =
                        String.format("%s %s, %s %s, %s %s, %s %s, %s %s", coordinates[LEFT], coordinates[TOP],
                            coordinates[RIGHT], coordinates[TOP], coordinates[RIGHT], coordinates[BOTTOM],
                            coordinates[LEFT], coordinates[BOTTOM],
                            coordinates[LEFT], coordinates[TOP]);

                    cartographics.addCoordinates(points);
                }
            }
        });

        getFirstDataSubfield("225", "a").ifPresent(cartographics::setScale);

        getFields("650").forEach(fields -> {
            Optional.ofNullable(fields.get("a")).ifPresent(topic -> {
                subject.addTopic(new MODSTopic(topic));
            });
        });

        MODSPhysicalDescription modsPhysicalDescription = new MODSPhysicalDescription();

        getFirstDataSubfield("300", "a").ifPresent(modsPhysicalDescription::addExtent);

        getFirstDataSubfield("300", "b").ifPresent(modsPhysicalDescription::addExtent);

        getFirstDataSubfield("300", "c").ifPresent(modsPhysicalDescription::addExtent);

        getFields("700").forEach(entry -> {
            Optional.ofNullable(entry.get("a")).ifPresent(name -> {
                MODSName modsName = new MODSName(name);
                modsName.setType("personal");
                modsName.setXLinkType("simple");

                modsName.addRoleTerm(new MODSRoleTerm("code", "marcrelator", "ctg"));

                modsDocument.addElement(modsName);
            });
        });

        final MODSLocation modsLocation = new MODSLocation();
        getFirstDataSubfield("852", "m").ifPresent(shelf -> {
            modsLocation.addShelfLocator(new MODSShelfLocator(shelf));
            final Predicate<String> isEmpty = String::isEmpty;
            getURLSForShelf(shelf).stream().filter(isEmpty.negate()).forEach(url -> {
                String fileName = url.substring(url.lastIndexOf("/") + 1)
                    .replaceAll("[^a-zA-Z0-9_.]", ""), originalFileName = fileName;

                try {
                    URL url1 = new URL(url.replace("http:", "https:"));
                    try (InputStream is = url1.openStream()) {
                        Path filePath = Paths.get(TARGET).resolve(current + "/");
                        Files.createDirectories(filePath);
                        int count = 0;
                        while (Files.exists(filePath.resolve(fileName))) {
                            count++;
                            final int liod = originalFileName.lastIndexOf(".");
                            fileName = liod == -1 ? originalFileName + "_" + count :
                                originalFileName.substring(0, liod) + "_" + count + originalFileName
                                    .substring(liod);
                        }
                        Files.copy(is, filePath.resolve(fileName));
                    } catch (IOException e) {
                        throw new RuntimeException("Error while downloading file!", e);
                    }
                } catch (MalformedURLException e) {
                    throw new RuntimeException("Error while downloading file!", e);
                }
            });

        });

        /*getFirstDataSubfield("852", "t").ifPresent(ppn -> {

        });*/

        getFields("856").forEach(subFields -> {
            if ("permalink".equals(subFields.get("3"))) {
                final String url = subFields.get("a");
                modsLocation.addUrl(new MODSURL(url));
            }
        });

        getFields("562").forEach(subFields -> {
            final String note = String.join(" ", subFields.values());
            final MODSNote numberingNote = new MODSNote(note, "numbering");
            modsDocument.addElement(numberingNote);
        });

        getFirstDataSubfield("651", "a").ifPresent(geographics -> {
            subject.addGeographic(new MODSGeographic(geographics));
        });

        getFirstDataSubfield("041", "a").ifPresent(language -> {
            final MODSLanguage modsLanguage = new MODSLanguage();
            final MODSLanguageTerm languageTerm = new MODSLanguageTerm();
            modsLanguage.addLanguageTerm(languageTerm);
            languageTerm.setAuthority("rfc5646");
            languageTerm.setType("code");

            switch (language.trim()) {
                case "spa":
                    languageTerm.setText("es");
                    break;
                case "ger":
                    languageTerm.setText("de");
                    break;
                case "fre":
                    languageTerm.setText("fr");
                    break;
                case "por":
                    languageTerm.setText("pt");
                    break;
                case "eng":
                    languageTerm.setText("en");
                    break;
                case "ita":
                    languageTerm.setText("it");
                    break;
                case "rus":
                    languageTerm.setText("ru");
                    break;
                case "lat":
                    languageTerm.setText("la");
                    break;
                case "cat":
                    languageTerm.setText("ca");
                    break;
                case "dut":
                    languageTerm.setText("nl");
                    break;
                case "pol":
                    languageTerm.setText("pl");
                    break;
                case "swe":
                    languageTerm.setText("sv");
                    break;
                default:
                    System.out.println("Unknown language: " + language);
                    return;
            }

            modsDocument.addElement(modsLanguage);
        });

        if (cartographics.getElement().getChildren().size() > 0) {
            subject.addCartographics(cartographics);
        }

        if (subject.getElement().getChildren().size() > 0) {
            modsDocument.addElement(subject);
        }

        if (modsPhysicalDescription.getElement().getChildren().size() > 0) {
            modsDocument.addElement(modsPhysicalDescription);
        }

        if (modsLocation.getElement().getChildren().size() > 0) {
            modsDocument.addElement(modsLocation);
        }

        modsDocument.setTypeOfResource("cartographic");
        MODSGenre genre = new MODSGenre();
        genre.setType("intern");
        genre.setAuthorityURI("http://www.mycore.org/classifications/mir_genres");
        genre.setValueURI("http://www.mycore.org/classifications/mir_genres#map");
        modsDocument.addElement(genre);

        MODSAccessCondition modsAccessCondition = new MODSAccessCondition();
        modsAccessCondition.setType("use and reproduction");
        modsAccessCondition.setXlinkHref("http://www.mycore.org/classifications/mir_licenses#cc_zero_1.0");
        modsAccessCondition.setXLinkType("simple");
        modsDocument.addElement(modsAccessCondition);

        final MODSName modsName = new MODSName();
        modsName.setType("corporate");
        modsName.setAuthorityURI("http://www.mycore.org/classifications/mir_institutes");
        modsName.setValueURI("http://www.mycore.org/classifications/mir_institutes#ifl");
        modsName.addRoleTerm("code", "marcrelator", "his");
        modsDocument.addElement(modsName);

        final MODSClassification classification = new MODSClassification();
        classification.setDisplayLabel("sdnb");
        classification.setAuthority("sdnb");
        classification.setText("910");
        modsDocument.addElement(classification);

        return modsDocument;
    }

}
