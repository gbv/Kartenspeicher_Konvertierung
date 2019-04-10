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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author liess
 * von Sebastian
 * 08.2017 erstellt für die Konvertierung der IKAR-Karten aus der 1.68
 * zum http://kartenspeicher.gbv.de mir-MODS
 */
public class CoordinateConverter {

    private static final String data =
        "/E 011 00E 012 30N 050 30N 049 40\t\n"
            + "E 000 31E 023 33N 055 15N 047 39\t\n"
            + "E 003 27E 010 57N 053 32N 050 59\t\n"
            + "E 003 36E 020 10N 047 36N 036 12\t\n"
            + "E 003 45E 010 02N 053 55N 049 52\t\n"
            + "E 004 44E 005 03N 050 36N 050 21\t\n"
            + "E 004 47E 009 15N 050 32N 046 44\t\n"
            + "E 005 08E 010 05N 049 17N 046 06\t\n"
            + "E 005 29E 007 38N 052 06N 049 43\t\n"
            + "E 005 35E 007 30N 052 05N 051 10\t\n"
            + "E 006 22E 007 14N 053 49N 053 15\t\n"
            + "E 006 30E 008 20N 053 50N 053 00\t\n"
            + "E 006 30E 008 50N 053 30N 51 35\t\n"
            + "E 006 30E 011 30N 053 50051 20\t\n"
            + "E 006 40E 009 30N 050 21N 048 51\t\n"
            + "E 006 41E 011 04N 054 23N 053 10\t\n"
            + "E 006 43E 008 23N 050 04N 048 39\t\n"
            + "E 006 45E 008 30N 053 55N 053 05\t\n"
            + "E 006 52E 008 34N 050 42N 050 01\t\n"
            + "E 006 57E 008 49N 049 56N 048 47\t\n"
            + "E 007 20E 007 50N 047 40N 047 10\t\n"
            + "E 007 20E 012 40N 49 20N 046 40\t\n"
            + "E 007 25E 009 54N 051 30N 049 35\t\n"
            + "E 007 31E 007 41N 047 35N 047 31\t\n"
            + "E 007 35E 008 47N 053 37N 052 53\t\n"
            + "E 007 50E 010 10N 049 20N 047 20\t\n"
            + "E 007 51E 010 19N 049 16N 047 10\t\n"
            + "E 007 55E 008 16N 049 20N 049 05\t\n"
            + "E 007 55E 009 00N 047 55N 047 05\t\n"
            + "E 008 02E 014 07N 054 25N 051 10\t\n"
            + "E 008 05E 01159N 054 02N 050 51\t\n"
            + "E 008 09E 010 04N 051 21N 049 37\t\n"
            + "E 008 20E 009 26N 050 35N 049 55\t\n"
            + "E 008 20E 009 38N 052 09N 051 23\t\n"
            + "E 008 22E 008 34N 049 32N 049 25\t\n"
            + "E 008 30E 010 00N 050 40N 049 50\t\n"
            + "E 008 31E 010 32N 051 35N 050 29\t\n"
            + "E 008 35E 009 15N 051 35N 051 05\t\n"
            + "E 008 42E 009 49N 050 19N 049 10\t\n"
            + "E 008 47E 009 52N 047 57N 047 19\t\n"
            + "E 008 50E 011 30N 052 35N 051 20\t\n"
            + "E 008 54E 010 21N 053 54N 053 25\t\n"
            + "E 009 04E 014 10N 046 01N 043 06\t\n"
            + "E 009 17E 009 27N 052 07N 052 03\t\n"
            + "E 009 20E 011 30N 052 40N 041 35\t\n"
            + "E 009 33E 017 32N 049 06N 045 08\t\n"
            + "E 009 36E 010 06N 049 07N 048 50\t\n"
            + "E 009 40E 010 57N 055 13N 054 42\t\n"
            + "E 009 40E 010 57N 055 44N 055 13\t\n"
            + "E 009 42E 010 30N 049 10N 048 46\t\n"
            + "E 009 42E 010 49N 051 37N 051 05\t\n"
            + "E 009 45E 015 15N 052 30N 049 40\t\n"
            + "E 009 50E 012 24N 046 46N 045 43\t\n"
            + "E 009 52E 009 58N 051 33N 051 29\t\n"
            + "E 010 00E 014 10N 050 05N 046 45\t\n"
            + "E 010 10E 014 00N 050 00N 047 00\t\n"
            + "E 010 10E 015 10N 052 35N 049 50\t\n"
            + "E 010 12E 011 18N 049 46N 049 20\t\n"
            + "E 010 30E 014 00N 050 00N 047 00\t\n"
            + "E 010 36E 022 14N 055 42N 053 58\t\n"
            + "E 010 50E 011 26N 049 38N 049 13\t\n"
            + "E 010 50E 012 20N 048 25N 047 10\t\n"
            + "E 011 07E 015 25N 043 30N 040 06\t\n"
            + "E 011 59E 017 30N 051 14N 048 29\t\n"
            + "E 012 07E 012 38N 051 09N 051 29\t\n"
            + "E 012 10E 019 50N 052 30N 048 40\t\n"
            + "E 012 10E 020 00N 052 30N 048 40\t\n"
            + "E 012 15E 014 26N 051 06N 050 16\t\n"
            + "E 012 30E 013 15N 051 15N 050 50\t\n"
            + "E 013 22E 014 44N 050 09N 050 01\t\n"
            + "E 013 27E 014 08N 051 19N 050 47\t\n"
            + "E 013 40E 020 20N 052 20N 049 10\t\n"
            + "E 014 03E 014 37N 036 07N 035 45\t\n"
            + "E 014 20E 019 20N 052 00N 049 00\t\n"
            + "E 015 10E 018 41N 041 25N 037 48\t\n"
            + "E 015 20E 177 20N 083 00N 033 00\t\n"
            + "E 015 47E 018 28N 050 31N 048 34\t\n"
            + "E 016 04E 016 26N 050 52N 050 40\t\n"
            + "E 016 12E 023 20N 056 00N 053 00\t\n"
            + "E 016 20E 025 20N 044 00N 039 00\t\n"
            + "E 016 41E 036 52N 041 00-N 028 39\t\n"
            + "E 016 53E 023 03N 055 49N 052 56\t\n"
            + "E 018 12E 021 26N 045 57N 044 20\t\n"
            + "E 018°17E 027 13N 051 25N 047 00\t\n"
            + "E 020 57E 026 50N 057 40N 055 32\t\n"
            + "E 021 15E 022 50N 055 50N 054 20\t\n"
            + "E 024 40E 034 00N 056 53N 050 27\t\n"
            + "E 024 41E 043 09N 056 10N 039 26\t\n"
            + "E 026 00E 029 48/ N 041 25 -N 039 43\t\n"
            + "E 028 39E 032 21N 060 35N 058 58\t\n"
            + "E 059 25E061 16N 070 01N 069 31\t\n"
            + "E 119 00E 132 40N 036 00N 018 35\t\n"
            + "E 123 02W 070 39N 065 13N 022 35\t\n"
            + "E 125 20E 170 20N 062 00N 040 00\t\n"
            + "E 128 19-W 113 12N 075 24N 029 13\t\n"
            + "E 1737E 019 30N 053 13N 050 16\t\n"
            + "E 46 36E 56 13N 47 27N 36 23\t\n"
            + "W 0026 00E 044 17N 071 38N 028 59\t\n"
            + "W 006 46E 037 15N 050 17N 028 04\t\n"
            + "W 025 45E 044 38N 071 34N 029 22\t\n";

    private static final String REG_EXP_STRING = "[ \\/-]*([EW])[ ]?([0-9]?[0-9]?[0-9]?[0-9][ °]?[0-9][0-9])"
        + "[ \\/-]*([EW])[ ]?([0-9]?[0-9]?[0-9][ °]?[0-9][0-9])"
        + "[ \\/-]*([NS])[ ]?([0-9]?[0-9]?[0-9][ °]?[0-9][0-9])"
        + "[ \\/-]*([NS])[ ]?([0-9]?[0-9]?[0-9][ °]?[0-9][0-9])";

    private static final Pattern PATTERN = Pattern.compile(REG_EXP_STRING);

    private static final int WESTLICHSTER = 0;
    private static final int NOERDLICHSTER = 2;
    private static final int OESTLICHSTER = 1;
    private static final int SUEDLICHSTER = 3;

    public static String convertCoordinate(String coordinate) {
        Matcher matcher = PATTERN.matcher(coordinate);

        if (!matcher.matches() || matcher.groupCount() != 8) {
            invalidInfo(coordinate);
            return null; // need 4 coordinates with 4 directions
        }
        double[] vals = new double[4];

        for (int i = 0; i < 4; i++) {
            char direction = matcher.group(i * 2 + 1).charAt(0);
            String numbersAsString = matcher.group(i * 2 + 2);
            String[] numbers = numbersAsString.split("[° ]");
            String firstNumber;
            String secondNumber;
            if (numbers.length == 2) {
                firstNumber = numbers[0];
                secondNumber = numbers[1];
            } else if (numbers.length == 1) {
                if (numbers[0].length() == 5) {
                    firstNumber = numbers[0].substring(0, 3);
                    secondNumber = numbers[0].substring(3, 5);
                } else {
                    invalidInfo(coordinate);
                    return null;
                }
            } else {
                invalidInfo(coordinate);
                return null;
            }
            int deg1 = Integer.parseInt(firstNumber, 10);
            int deg2 = Integer.parseInt(secondNumber, 10);

            double result = (direction == 'E' || direction == 'N') ?
                ((double) deg1) + (deg2 / 60.0) :
                -((double) deg1) + (deg2 / 60.0);

            vals[i] = result;
        }
        return String.format(Locale.ROOT, "%f %f, %f %f, %f %f, %f %f ",
            vals[WESTLICHSTER], vals[NOERDLICHSTER],
            vals[OESTLICHSTER], vals[NOERDLICHSTER],
            vals[OESTLICHSTER], vals[SUEDLICHSTER],
            vals[WESTLICHSTER], vals[SUEDLICHSTER]
            );
    }
    public static void invalidInfo(String coord) {
        System.err.println("Coordinate is invalid: " + coord);
    }
    public static void main(String[] args) {
        Stream.of(data.split("\t\n"))
            .map(CoordinateConverter::convertCoordinate)
            .forEach(System.out::println);
    }
}
