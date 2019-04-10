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

/**
 * verwaltet eine Person mit seinen Eigenschaften in MODS<br>
 * für das mapping Kartenspeicher IKAR 10.2017
 * @author liess
 * 08.2017 erstellt für die Konvertierung der IKAR-Karten aus der 1.68
 * zum http://kartenspeicher.gbv.de mir-MODS
 */
public class AuthorMods {
	
		private int nr = 0;
		private String description = "";
		private String displayForm = "";
		private String family = "";
		private String given = "";
		private String nameIdentifier_gnd = "";
		private String namePart_date = "";
		private String namePart_termsOfAddress = "";
		private String roleTerm = "";

		public AuthorMods() {
			super();
		}
		
		public void setDescription(String description) {
			this.description = description;
		}
		public void setDisplayForm(String displayForm) {
			this.displayForm = displayForm;
		}
		public void setFamily(String family) {
			this.family = family;
		}
		public void setGiven(String given) {
			this.given = given;
		}
		public void setNameIdentifier_gnd(String nameIdentifier_gnd) {
			this.nameIdentifier_gnd = nameIdentifier_gnd;
		}
		public void setNamePart_date(String namePart_date) {
			this.namePart_date = namePart_date;
		}
		public void setNamePart_termsOfAddress(String namePart_termsOfAddress) {
			this.namePart_termsOfAddress = namePart_termsOfAddress;
		}
		public void setNr(int nr) {
			this.nr = nr;
		}
		public void setRoleTerm(String roleTerm) {
			this.roleTerm = roleTerm;
		}
		
		public int getNr() {
			return nr;
		}
		public String getDescription() {
			return description;
		}
		public String getDisplayForm() {
			return displayForm;
		}
		public String getFamily() {
			return family;
		}
		public String getGiven() {
			return given;
		}
		public String getNameIdentifier_gnd() {
			return nameIdentifier_gnd;
		}
		public String getNamePart_date() {
			return namePart_date;
		}
		public String getNamePart_termsOfAddress() {
			return namePart_termsOfAddress;
		}
		public String getRoleTerm() {
			return roleTerm;
		}
		public boolean isEmpty() {
			if((getDisplayForm() + getFamily() + getGiven()
					 + namePart_date
					+ description + nameIdentifier_gnd).isEmpty())
				return true;
			else
				return false;
		}
	}

