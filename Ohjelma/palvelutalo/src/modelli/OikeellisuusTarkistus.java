package modelli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fi.jyu.mit.fxgui.Dialogs;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * @author Miia Laurila
 * @version 9.5.2017
 *
 *Luokka, jossa tarkistetaan annettujen tietojen oikeellisuus.
 */
public class OikeellisuusTarkistus{
	
	private Palvelutalo palvelutalo = new Palvelutalo();
	
	
	/**
	 * konstruktori
	 */
	public OikeellisuusTarkistus(){
		// ei tarvitse alustaa
	}

	
	/**
	 * Tarkastetaan, onko työntekijän tiedot oikein täytetty.
	 * @param nimi nimi tekstikenttä
	 * @param hetu hetu tekstikenttä
	 * @param tyonimike työnimike tekstikenttä
	 * @param katuosoite katuosoite teksitkenttä
	 * @param postinumero postinumero tekstikenttä
	 * @param postitoimipaikka postitoimipaikka tekstikenttä
	 * @param puhelinnumero puhelinnumero tekstikenttä
	 * @param id työntekijän id
	 * @return Jos ei ole virheitä true, jos on false.
	 */
	public Boolean tarkistus(TextField nimi, TextField hetu, TextField tyonimike, TextField katuosoite, TextField postinumero, TextField postitoimipaikka, TextField puhelinnumero, int id) {
		nimi.getStyleClass().remove("virhe");
		hetu.getStyleClass().remove("virhe");
		tyonimike.getStyleClass().remove("virhe");
		katuosoite.getStyleClass().remove("virhe");
		postinumero.getStyleClass().remove("virhe");
		postitoimipaikka.getStyleClass().remove("virhe");
		puhelinnumero.getStyleClass().remove("virhe");
		Boolean[] virheNimessa = nimenTarkistus(nimi);
		String[] virheHetussa = hetunTarkistus(hetu, id);
		Boolean virheTyonimikkeessa = tyonimikkeenTarkistus(tyonimike);
		Boolean virheKatuosoitteessa = katuosoitteenTarkistus(katuosoite);
		Boolean[] virhePostinumerossa = postinumeronTarkistus(postinumero);
		Boolean virhePostitoimpaik = postitoimipaikanTarkistus(postitoimipaikka);
		Boolean[] virhePuhnrossa = puhelinnumeronTarkistus(puhelinnumero);
		
		if(virheNimessa[0] || virheHetussa[0].equals("true") || virheTyonimikkeessa || virheKatuosoitteessa || virhePostinumerossa[0] || virhePostitoimpaik || virhePuhnrossa[0]){
			Dialogs.showMessageDialog("Täytä puuttuvat tiedot");
			return false;
		}
		else if(virheNimessa[1] || virheNimessa[2]){
			nimi.getStyleClass().add("virhe");
			Dialogs.showMessageDialog("Nimi virheellinen.");
			return false;
		}
		else if(virheHetussa[1].equals("false")){
			hetu.getStyleClass().add("virhe");
			if(hetu.getText().length()!=11 && hetu.getText().length()!= 0 && virheHetussa[3].equals("true")){
				Dialogs.showMessageDialog("Henkilötunnuksen loppuosa virheellinen.");
			}
			if(hetu.getText().length()==11 && virheHetussa[5].equals("true")){
                Dialogs.showMessageDialog("Henkilötunnuksen loppuosa virheellinen.");
            }
			if(hetu.getText().length()==11 && virheHetussa[3].equals("true") && virheHetussa[5].equals("false")){
				Dialogs.showMessageDialog("Henkilötunnuksen viimeinen merkki täytyisi olla "+virheHetussa[2]+".");
			}
			if(virheHetussa[3].equals("false") && hetu.getText().length()!=0){
				Dialogs.showMessageDialog("Henkilötunnuksen päivämäärä väärin");
			}
			return false;
		}
		else if(virheHetussa[4].equals("true")){
			hetu.getStyleClass().add("virhe");
			Dialogs.showMessageDialog("Henkilötunnuksella on jo olemassa toinen työntekijä.");
			return false;
		}
		else if(virhePostinumerossa[1] || virhePostinumerossa[2]){
			postinumero.getStyleClass().add("virhe");
			if(virhePostinumerossa[2]){
				Dialogs.showMessageDialog("Postinumero ei saa sisältää muuta kuin numeroita.");
			}
			else{
				Dialogs.showMessageDialog("Postinumeron täytyy olla viiden numeron mittainen.");
			}
			return false;
		}
		else if(virhePuhnrossa[1] || virhePuhnrossa[2] || virhePuhnrossa[3]){
			puhelinnumero.getStyleClass().add("virhe");
			Dialogs.showMessageDialog("Puhelinnumero on virheellinen.");
			return false;
		}
		else{
			return true;
		}
	}


	/**
	 * Tarkastetaan, onko puhelinnumero kirjoitettu oikein.
	 * @param puhelinnumero puhelinnumero tekstikenttä
	 * @return jos on virhe true, jos ei false.
	 */
	private Boolean[] puhelinnumeronTarkistus(TextField puhelinnumero) {
		String puh = puhelinnumero.getText();
		Boolean[] virheet = {false, false, false, false};
		if(puh.length() == 0){
			puhelinnumero.getStyleClass().add("virhe");
			virheet[0] = true;
		}
		else{
			if(puh.charAt(0) == '+' && puh.length() != 13){
				virheet[1] = true;
			}
			for(int i = 1; i < puh.length() ; i ++){
				if(!Character.isDigit(puh.charAt(i))){
					virheet[2] = true;
				}
			}
			if((puh.length() != 10 || !Character.isDigit(puh.charAt(0))) && (puh.charAt(0) != '+' && puh.length() != 13)){
				virheet[3] = true;
			}
		}
		return virheet;
	}


	/**
	 * Tarkastetaan, onko postitoimipaikka kirjoitettu.
	 * @param postitoimipaikka postitoimipaikka tekstikenttä
	 * @return jos on virhe true, jos ei false.
	 */
	private Boolean postitoimipaikanTarkistus(TextField postitoimipaikka) {
		if(postitoimipaikka.getText().length() == 0){
			postitoimipaikka.getStyleClass().add("virhe");
			return true;
		}
		return false;
	}


	/**
	 * Tarkastetaan, onko postinumerossa virheitä.
	 * @param postinumero postinumeron tekstikenttä
	 * @return jos on virhe true, jos ei false.
	 */
	private Boolean[] postinumeronTarkistus(TextField postinumero) {
		String postinro = postinumero.getText();
		Boolean[] virheet = {false, false, false};
		if(postinro.length() == 0){
			postinumero.getStyleClass().add("virhe");
			virheet[0] = true;
		}
		if(postinro.length() != 5 && postinro.length() != 0){
			virheet[1] = true;
		}
		for(int i = 0; i < postinro.length(); i++){
			if(!Character.isDigit(postinro.charAt(i))){
				virheet[2] = true;
			}
		}
		return virheet;
	}


	/**
	 * Tarkastetaan, onko katuosoite kirjoitettu.
	 * @param katuosoite katuosoitteen tekstikenttä
	 * @return jos on virheitä true, jos ei false.
	 */
	private Boolean katuosoitteenTarkistus(TextField katuosoite) {
		if(katuosoite.getText().length() == 0){
			katuosoite.getStyleClass().add("virhe");
			return true;
		}
		return false;
	}


	/**
	 * Tarkistetaan, onko työnimike kirjoitettu
	 * @param tyonimike työnimikkeen tekstikenttä
	 * @return jon virheitä true, jos ei false.
	 */
	private Boolean tyonimikkeenTarkistus(TextField tyonimike) {
		if(tyonimike.getText().length() == 0){
			tyonimike.getStyleClass().add("virhe");
			return true;
		}
		return false;
	}


	/**
	 * Tarkastetaan, onko nimi kirjoitettu ja onko siinä vähintään etu- ja sukunimi.
	 * @param nimiText nimen tekstikenttä
	 * @return jos on virheitä true, jos ei false.
	 */
	private Boolean[] nimenTarkistus(TextField nimiText) {
		Boolean[] virheet = {false, false, true};
		String nimi = nimiText.getText();
		if(nimi.length()==0){
			nimiText.getStyleClass().add("virhe");
			virheet[0] = true;
		}
		else if(nimi.length()<3){
			virheet[1] = true;
		}
		else{
			for(int i = 0; i<nimi.length(); i++){
				if(nimi.charAt(i) == ' '){
					virheet[2] = false;
				}
			}
		}
		return virheet;
	}


	/**
	 * Tarkastetaan hetun oikeellisuus ja onko se jo olemassa toisella työntekijällä.
	 * @param hetuText
	 * @param id
	 * @return palauttaa String taulukkona onko hetun muoto oikein ja mikä on viimeinen merkki ja onko päivämäärä oikein sekä onko jo olemassa.
	 */
	private String[] hetunTarkistus(TextField hetuText, int id) {
		String[] palautus = new String[6];
		palautus[0] = "false";
		String hetu = hetuText.getText();
		
		if(hetu.length()==0){
			hetuText.getStyleClass().add("virhe");
			palautus[0] = "true";
		}
		
		String[] oikeellisuus = hetunMuotoOikein(hetu);
		palautus[1] = oikeellisuus[0];
		palautus[2] = oikeellisuus[1];
		palautus[5] = oikeellisuus[2];
		
		palautus[3] = Boolean.toString(hetunPaivamaara(hetu));
		
		palautus[4] = onkoJoOlemassa(hetu, id);
		return palautus;
	}


	/**
	 * Tarkastaa onko jollain toisella työntekijällä jo kyseinen hetu.
	 * @param hetu tarkasteltava hetu
	 * @param id tarkasteltava työntekijän id.
	 * @return palauttaa "false", jos ei ole olemassa ja true jos on.
	 */
	private String onkoJoOlemassa(String hetu, int id) {
		String palautus = "false";
		Tyontekija tyontekija;
		int maara = palvelutalo.getTyontekija();
		for(int i = 0; i<maara ; i++){
			tyontekija = palvelutalo.annaTyontekija(i);
			if(tyontekija.getHetu().equals(hetu) && tyontekija.getId() != id){
				palautus = "true";
			}
		}
		return palautus;
	}


	/**
	 * Tarkastellaan, onko hetun päiväys kirjoitettu oikein.
	 * @param hetu tarkasteltava hetu.
	 * @return false jos hetun pvm virheellinen, true jos on oikein.
	 */
	private Boolean hetunPaivamaara(String hetu) {
		Boolean onko = false;
		if(hetu.length()>=6 && Character.isDigit(hetu.charAt(0)) && Character.isDigit(hetu.charAt(1)) && Character.isDigit(hetu.charAt(2)) && Character.isDigit(hetu.charAt(3)) && Character.isDigit(hetu.charAt(4)) && Character.isDigit(hetu.charAt(5))){
			int pv = Integer.parseInt(hetu.charAt(0)+""+hetu.charAt(1));
			int kk = Integer.parseInt(hetu.charAt(2)+""+hetu.charAt(3));
			int vv = Integer.parseInt(hetu.charAt(4)+""+hetu.charAt(5));
			
			if(vv >= 0 && vv <= 99){
				switch (kk) {
				case 1:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 2:
					if(vv % 4 == 0){
						if(pv > 0 && pv <= 29){
							onko = true;
						}
					}
					if(pv > 0 && pv <= 28){
						onko = true;
					}
					break;
				case 3:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 4:
					if(pv > 0 && pv <= 30){
						onko = true;
					}
					break;
				case 5:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 6:
					if(pv > 0 && pv <= 30){
						onko = true;
					}
					break;
				case 7:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 8:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 9:
					if(pv > 0 && pv <= 30){
						onko = true;
					}
					break;
				case 10:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				case 11:
					if(pv > 0 && pv <= 30){
						onko = true;
					}
					break;
				case 12:
					if(pv > 0 && pv <= 31){
						onko = true;
					}
					break;
				default:
					break;
				}
			}
		}
		return onko;
	}


	/**
	 * Tarkistetaan, onko hetun muoto oikein
	 * @param hetu tarkasteltava hetu
	 * @return taulukko, jossa onko oikein ja tarkistusmerkki.
	 */
	private String[] hetunMuotoOikein(String hetu) {
	    Boolean kirjaimia = false;
		Boolean oikeellisuus = false;
		char tarkistusmerkki = '0';
		String ppkkvvnnn = "";
		if(hetu.length() == 11) {
			for(int i = 0; i<=5; i++){
				if(Character.isLetter(hetu.charAt(i))){
				    kirjaimia = true;
					break;
				}
				ppkkvvnnn += hetu.charAt(i);
			}
			for(int j = 7; j<=9; j++){
				if(Character.isLetter(hetu.charAt(j))){
				    kirjaimia = true;
					break;
				}
				ppkkvvnnn += hetu.charAt(j);
			}
			int henktun = Integer.parseInt(ppkkvvnnn);
			int jakojaannos = henktun - (31*(henktun/31));
			switch (jakojaannos) {
				case 0:
					tarkistusmerkki = '0';
					break;
				case 1:
					tarkistusmerkki = '1';
					break;
				case 2:
					tarkistusmerkki = '2';
					break;
				case 3:
					tarkistusmerkki = '3';
					break;
				case 4:
					tarkistusmerkki = '4';
					break;
				case 5:
					tarkistusmerkki = '5';
					break;
				case 6:
					tarkistusmerkki = '6';
					break;
				case 7:
					tarkistusmerkki = '7';
					break;
				case 8:
					tarkistusmerkki = '8';
					break;
				case 9:
					tarkistusmerkki = '9';
					break;
				case 10:
					tarkistusmerkki = 'A';
					break;
				case 11:
					tarkistusmerkki = 'B';
					break;
				case 12:
					tarkistusmerkki = 'C';
					break;
				case 13:
					tarkistusmerkki = 'D';
					break;
				case 14:
					tarkistusmerkki = 'E';
					break;
				case 15:
					tarkistusmerkki = 'F';
					break;
				case 16:
					tarkistusmerkki = 'H';
					break;
				case 17:
					tarkistusmerkki = 'J';
					break;
				case 18:
					tarkistusmerkki = 'K';
					break;
				case 19:
					tarkistusmerkki = 'L';
					break;
				case 20:
					tarkistusmerkki = 'M';
					break;
				case 21:
					tarkistusmerkki = 'N';
					break;
				case 22:
					tarkistusmerkki = 'P';
					break;
				case 23:
					tarkistusmerkki = 'R';
					break;
				case 24:
					tarkistusmerkki = 'S';
					break;
				case 25:
					tarkistusmerkki = 'T';
					break;
				case 26:
					tarkistusmerkki = 'U';
					break;
				case 27:
					tarkistusmerkki = 'V';
					break;
				case 28:
					tarkistusmerkki = 'W';
					break;
				case 29:
					tarkistusmerkki = 'X';
					break;
				case 30:
					tarkistusmerkki = 'Y';
					break;
				default:
					break;
			}
			
			if(tarkistusmerkki == hetu.charAt(10) && (hetu.charAt(6) == '-' || hetu.charAt(6) == '+' || hetu.charAt(6) == 'A')){
				oikeellisuus = true;
			}
		}
		String[] palautus = new String[3];
		palautus[2] = Boolean.toString(kirjaimia);
		palautus[0] = Boolean.toString(oikeellisuus);
		palautus[1] = Character.toString(tarkistusmerkki);
		return palautus;
	}


    /**
     * Tarkistetaan lisäkoulutuksen tietojen oikeellisuus
     * @param lisakoulutus koulutuksen nimi
     * @param suoritus suorituspvm
     * @param voimassa voimassaolopvm
     * @param plus onko painettu plus-napista
     * @return true, jos ei virheitä ja false jos on.
     */
    public Boolean tarkistaLisa(ComboBox<String> lisakoulutus, TextField suoritus, TextField voimassa, Boolean plus) {
        Boolean palautus = true;
        String koulutus = lisakoulutus.getSelectionModel().getSelectedItem();
        String suori = suoritus.getText();
        String voima = voimassa.getText();
        if(koulutus == null){
            koulutus = "";
        }
        
        suoritus.getStyleClass().remove("virhe");
        voimassa.getStyleClass().remove("virhe");
        
        if(koulutus.equals("") && (!suori.equals("") || !voima.equals(""))){
            Dialogs.showMessageDialog("Valitse lisäkoulutus.");
            palautus = false;
        }
        else if(suori.equals("") && !koulutus.equals("")) {
            suoritus.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Lisää suorituspäivämäärä.");
            palautus = false;
        }
        else if(voima.equals("") && !koulutus.equals("")){
            voimassa.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Lisää voimassaolopäivämäärä.");
            palautus = false;
        }
        else if(koulutus.equals("") && suori.equals("") && voima.equals("")){
            if(plus){
                Dialogs.showMessageDialog("Valitse lisäkoulutus.");
                palautus = false;
            }
            else{
                palautus = true;
            }
        }
        else{
            palautus = tarkastaPaivamaarat(suori, suoritus, voima, voimassa);
        }
        return palautus;
    }


    /**
     * Tarkistetaan onko päivämäärät oikein.
     * @param suori suoritus pvm
     * @param suoritus suoritus tekstikenttä
     * @param voima voimassaolo pvm
     * @param voimassa voimassaolon tekstikenttä
     * @return true jos on oikein false jos ei.
     */
    private Boolean tarkastaPaivamaarat(String suori, TextField suoritus, String voima, TextField voimassa) {
        if(virheSuoritusPvm(suori)){
            suoritus.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Virhe suorituspäivämäärässä; sen täytyy olla muotoa pv.kk.vvvv");
            return false;
        }
        else if(suoritusPvmTulevaisuudessa(suori)){
            suoritus.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Suorituspäivämäärä on tulevaisuudessa.");
            return false;
        }
        else if(virheVoimassaPvm(voima)){
            voimassa.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Virhe voimassaolopäivämäärässä; sen täytyy olla muotoa pv.kk.vvvv tai Toistaiseksi");
            return false;
        }
        else if(voimassaEnnenSuoritus(suori, voima)){
            suoritus.getStyleClass().add("virhe");
            voimassa.getStyleClass().add("virhe");
            Dialogs.showMessageDialog("Suorituspäivämäärä on voimassaolopäivämäärän jälkeen");
            return false;
        }
        return true;
    }


    /**
     * Tarkastellaan, onko suoritus päivämäärä tulevaisuudessa.
     * @param suori suoritus päivämäärä
     * @return true jos on tulevaisuudessa ja false jos ei
     */
    private boolean suoritusPvmTulevaisuudessa(String suori) {
        Boolean onkoTulevaisuudessa = false;
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String tanaan = dateFormat.format(date);
        String[] tanaanSplit = tanaan.split("\\.");
        String[] suoritusSplit = suori.split("\\.");
        int pvT = Integer.parseInt(tanaanSplit[0]);
        int kkT = Integer.parseInt(tanaanSplit[1]);
        int vvvvT = Integer.parseInt(tanaanSplit[2]);
        int pvS = Integer.parseInt(suoritusSplit[0]);
        int kkS = Integer.parseInt(suoritusSplit[1]);
        int vvvvS = Integer.parseInt(suoritusSplit[2]);
        if((vvvvT < vvvvS) || (vvvvT == vvvvS && kkT < kkS) || (vvvvT == vvvvS && kkT == kkS && pvT < pvS)){
            onkoTulevaisuudessa = true;
        }
        return onkoTulevaisuudessa;
    }


    /**
     * Tarkistetaan, onko voimassaolo ennen suoritusta.
     * @param suori suoritus pvm
     * @param voima voimassa pvm
     * @return true, jos on ja false jos ei.
     */
    private boolean voimassaEnnenSuoritus(String suori, String voima) {
        Boolean onko = false;
        if(!voima.toLowerCase().equals("toistaiseksi")){
            String[] pvmVoima = voima.split("\\.");
            String[] pvmSuori = suori.split("\\.");
            Boolean voim = paivamaaranOikeellisuus(pvmVoima);
            Boolean suor = paivamaaranOikeellisuus(pvmSuori);
            if(!voim && !suor){
                int pvV = Integer.parseInt(pvmVoima[0]);
                int kkV = Integer.parseInt(pvmVoima[1]);
                int vvvvV = Integer.parseInt(pvmVoima[2]);
                int pvS = Integer.parseInt(pvmSuori[0]);
                int kkS = Integer.parseInt(pvmSuori[1]);
                int vvvvS = Integer.parseInt(pvmSuori[2]);
                if((vvvvV < vvvvS) || (vvvvV == vvvvS && kkV < kkS) || (vvvvV == vvvvS && kkV == kkS && pvV < pvS)){
                    onko = true;
                }
            }
        }
        
        return onko;
    }


    /**
     * Tarkistetaan, onko voimassaolo päivämäärä virheellinen
     * @param voima voimassaolon pvm
     * @return true, jos on virheellinen ja false jos ei.
     */
    private boolean virheVoimassaPvm(String voima) {
        Boolean onkoVirheita = false;
        String[] pvm = voima.split("\\.");
        if(voima.toLowerCase().equals("toistaiseksi")){
            onkoVirheita = false;
        }
        else if(pvm.length != 3){
            onkoVirheita = true;
        }
        else{
            onkoVirheita = paivamaaranOikeellisuus(pvm);
        }
        return onkoVirheita;
    }


    /**
     * Tarkistetaan päivämäärän oikeellisuus
     * @param pvm tarasteltava päivämäärä
     * @return jos on virheitä true jos ei false.
     */
    private Boolean paivamaaranOikeellisuus(String[] pvm) {
        Boolean onkoVirheita = true;
        if(pvm[0].matches("^-?\\d+$") && pvm[1].matches("^-?\\d+$") && pvm[2].matches("^-?\\d+$")){
            int pv = Integer.parseInt(pvm[0]);
            int kk = Integer.parseInt(pvm[1]);
            int vvvv = Integer.parseInt(pvm[2]);
            if(vvvv>=1950){
                switch (kk) {
                case 1: if(pv > 0 && pv <= 31){
                        onkoVirheita = false;
                    }
                    break;
                case 2: if(vvvv % 4 == 0){
                            if(pv > 0 && pv <= 29){
                                onkoVirheita = false;
                            }
                        }
                        else{
                            if(pv > 0 && pv <= 28){
                                onkoVirheita = false;
                            }
                        }
                    break;
                case 3: if(pv > 0 && pv <= 31){
                            onkoVirheita = false;
                        }
                    break;
                case 4:if(pv > 0 && pv <= 30){
                            onkoVirheita = false;
                        } 
                    break;
                case 5:if(pv > 0 && pv <= 31){
                        onkoVirheita = false;
                    }
                    break;
                case 6:if(pv > 0 && pv <= 30){
                        onkoVirheita = false;
                    }
                    break;
                case 7:if(pv > 0 && pv <= 31){
                       onkoVirheita = false;
                    } 
                    break;
                case 8:if(pv > 0 && pv <= 31){
                        onkoVirheita = false;
                    }
                    break;
                case 9:if(pv > 0 && pv <= 30){
                        onkoVirheita = false;
                    }
                    break;
                case 10:if(pv > 0 && pv <= 31){
                        onkoVirheita = false;
                    }
                    break;
                case 11:if(pv > 0 && pv <= 30){
                        onkoVirheita = false;
                    }
                    break;
                case 12:if(pv > 0 && pv <= 31){
                        onkoVirheita = false;
                    }
                break;
                
                default:
                    break;
                }
            }
        }
        return onkoVirheita;
    }


    /**
     * Tarkastellaan, onko suoritus päivämäärässä virheellisyyksiä
     * @param suori suorituspäivämäärä
     * @return jos on virhe true jos ei false.
     */
    private boolean virheSuoritusPvm(String suori) {
        Boolean onkoVirheita = false;
        String[] pvm = suori.split("\\.");
        if(pvm.length != 3){
            onkoVirheita = true;
        }
        else{
            onkoVirheita = paivamaaranOikeellisuus(pvm);
        }
        return onkoVirheita;
    }
	
	
}