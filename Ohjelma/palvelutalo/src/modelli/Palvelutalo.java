package modelli;

import java.io.File;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * Palvelutalo-luokka, joka huolehtii näkymässä tapahtuvien muutosten siirtymisestä työntekijät ja
 * lisäkoulutukset luokkiin.
 *
 * @author Miia Laurila
 * @version 6.6.2017
 */
public class Palvelutalo {
	
    private Lisakoulutukset lisakoulutukset = new Lisakoulutukset();

    private static Tyontekijat tyontekijat = new Tyontekijat(); 
    private static OikeellisuusTarkistus oikeellisuusTarkistus = new OikeellisuusTarkistus();
    
    
    /**
     * Palauttaa työntekijöiden lukumäärän rekisterissä
     * @return työntekijöiden määrän
     */
    public int getTyontekija() {
        return tyontekijat.getLkm();
    }
    
    
    /**
     * Palauttaa lisäkoulutuksien kokonaismäärän rekisterissä.
     * @return lisäkoulutuksien määrän
     */
    public int getLisakoulutus() {
    	return lisakoulutukset.getLkm();
    }
    
    
    /**
     * Lisää uuden työntekijän työntekijä taulukkoon ja sen lisäkoulutukset lisäkoulutus taulukkoon.
     * @param tyontekija lisättävä työntekijä
     * @param uudenLisakoulutukset lisättävät lisäkoulutukset
     * @return työntekijän lisäkoulutukset
     */
    public Lisakoulutus[] lisaa(Tyontekija tyontekija, Lisakoulutus[] uudenLisakoulutukset) {
    	Lisakoulutus lisakoulutusLista[] = lisakoulutukset.lisaa(uudenLisakoulutukset);
        tyontekijat.lisaa(tyontekija);
        return lisakoulutusLista;
    }


    /**
     * Palauttaa i:nnen työntekijän taulukosta.
     * @param i indeksi
     * @return i:nnen työntekijän
     */
    public Tyontekija annaTyontekija(int i) {
        return tyontekijat.anna(i);
    }
    
    
    /**
     * Palauttaa i:nnen lisäkoulutuksen taulukosta.
     * @param i indeksi
     * @return i:nnen lisäkoulutuksen.
     */
    public Lisakoulutus annaLisakoulutus(int i){
    	return lisakoulutukset.anna(i);
    }


    /**
     * Muokkaus-näkymässä lisättyjen lisäkoulutuksien lisääminen lisäkoulutus taulukkoon
     * @param tyontekijanLisakoulutukset lisäkoulutukset, jotka lisätään lisäkoulutus taulukkoon.
     * @return lisäkoulutukset, jotka on lisätty lisäkoulutus taulukkoon.
     */
	public Lisakoulutus[] lisaa(Lisakoulutus[] tyontekijanLisakoulutukset) {
		Lisakoulutus koulutusLista[] = lisakoulutukset.lisaa(tyontekijanLisakoulutukset);
        return koulutusLista;
	}
	
	
	/**
	 * Asetetaan työntekijälle id.
	 * @param tyontekija työntekijä, jolle id annetaan
	 */
	public void asetaTyontekijalleId(Tyontekija tyontekija){
		tyontekijat.laitaId(tyontekija);
	}
	
	
	/**
	 * Asetaan koulutuksille oma id ja työntekijän id.
	 * @param tyontekija työntekijä, jonka lisäkoulutuksia käsitellään
	 * @param tyontekijanLisakoulutukset työntekijän lisäkoulutukset.
	 */
	public void asetaKoulutuksilleId(Tyontekija tyontekija, Lisakoulutus[] tyontekijanLisakoulutukset){
		lisakoulutukset.laitaId(tyontekija, tyontekijanLisakoulutukset);
	}
	
	
	/**
	 * Asetetaan käytettävät tiedostot
	 * @param rekisteri rekisterin nimi
	 * @param tyontekija työntekijöitä käsittelevä tiedosto
	 * @param lisakoulutus lisäkoulutuksia käsittelevä tiedosto
	 */
	public void setTiedosto(String rekisteri, String tyontekija, String lisakoulutus){ 
	    File dir = new File(rekisteri);
        dir.mkdirs();
        tyontekijat.setTiedostonPerusNimi(rekisteri+"/"+tyontekija);
        lisakoulutukset.setTiedostonPerusNimi(rekisteri+"/"+lisakoulutus);
    }
	
	
    /**
     * Luetaan tiedostoissa olevat työntekijät ja niiden lisäkoulutukset
     * @param rekisteri rekisterin nimi
     * @param tyontekija työntekijöitä käsittelevä tiedosto
     * @param lisakoulutus lisäkoulutuksia käsitteleäv tiedosto
     */
    public void lueTiedostosta(String rekisteri, String tyontekija, String lisakoulutus) {
        tyontekijat = new Tyontekijat();
        lisakoulutukset = new Lisakoulutukset();
        setTiedosto(rekisteri, tyontekija, lisakoulutus);
        tyontekijat.lueTiedostosta();
        lisakoulutukset.lueTiedostosta();
    }

    
	/**
	 * Lisätään tiedostoon työntekijät
	 */
	public void lisaaTiedostoon() {
		tyontekijat.lisaaMuokkauksetTiedostoon();
	}

	
	/**
	 * Lisätään tiedostoon lisäkoulutukset.
	 */
	public void lisaaLisakoulutus() {
		lisakoulutukset.lisaaTiedostoon();
	}
	

	/**
	 * Poistetaan poistettavan työntekijän tiedot tiedostoista ja taulukoista.
	 * @param poistettava poistettavan työntekijän id.
	 */
	public void poista(int poistettava) {
		lisakoulutukset.poistaTiedostosta(poistettava);
		lisakoulutukset.poistaTaulukosta(poistettava);
		tyontekijat.poistaTiedostosta(poistettava);
		tyontekijat.poistaTaulukosta(poistettava);
	}

	
	/**
	 * Palautetaan työntekijä id:n avulla
	 * @param id työntekijän id
	 * @return työntekijä, jolla on haluttu id.
	 */
	public Tyontekija annaTyontekijaIdnAvulla(int id) {
		return tyontekijat.annaTyontekijaId(id);
	}

	
	/**
	 * Muokataan i:nnes lisäkoulutus
	 * @param i indeksi
	 * @param lisakoulutus muokattu lisäkoulutus
	 */
	public void muokkaaLisakoulutus(int i, Lisakoulutus lisakoulutus) {
		lisakoulutukset.muokkaaLisakoulutus(i, lisakoulutus);
	}

	
	/**
	 * Muokataan lisäkoulutus, jolla on tietty id.
	 * @param id lisäkoulutuksen id
	 * @param koulutus lisäkoulutuksen nimi
	 * @param suoritus lisäkoulutuksen suorituspäivämäärä
	 * @param voimassa lisäkoulutuksen voimassaolopäivämäärä
	 */
	public void muokkaaLisakoulutus(int id, String koulutus, String suoritus, String voimassa) {
		lisakoulutukset.muokkaaLisakoulutus(id, koulutus, suoritus, voimassa);
	}

	
	/**
	 * Tarkistetaan kenttien oikeellisuus
	 * @param nimi nimen tekstikenttä
	 * @param hetu hetun tekstikenttä
	 * @param tyonimike työnimikkeen tekstikenttä
	 * @param katuosoite katuosoitteen tekstikenttä
	 * @param postinumero postinumeron tekstikenttä
	 * @param postitoimipaikka postitoimipaikan tekstikenttä
	 * @param puhelinnumero puhelinnumeron tekstikenttä
	 * @param id työntekijän id
	 * @return jos ei ole virheitä true, jos on false
	 */
	public Boolean tarkistaKentat(TextField nimi, TextField hetu, TextField tyonimike, TextField katuosoite, TextField postinumero, TextField postitoimipaikka, TextField puhelinnumero, int id) {
		return oikeellisuusTarkistus.tarkistus(nimi, hetu, tyonimike, katuosoite, postinumero, postitoimipaikka, puhelinnumero, id);
	}


    /**
     * Tarkistetaan koulutuksen tiedot.
     * @param lisakoulutus koulutuksen nimi
     * @param suoritus suorituspvm
     * @param voimassa voimassapvm
     * @param plus onko painettu plus-napista
     * @return true, jos ei virheitä, false jos on.
     */
    public Boolean tarkistaLisakoulutus(ComboBox<String> lisakoulutus, TextField suoritus, TextField voimassa, Boolean plus) {
        return oikeellisuusTarkistus.tarkistaLisa(lisakoulutus, suoritus, voimassa, plus);
    }


    /**
     * Poistetaan yksittäinen lisäkoulutus taulukosta.
     * @param id lisäkoulutuksen id
     */
    public void poistaLisakoulutus(int id) {
        lisakoulutukset.poistaKoulutusTiedostosta(id);
        lisakoulutukset.poistaKoulutusTaulukosta(id);
    }


    /**
     * Luodaan uusi rekisteri.
     * @param rekisteri rekisterin nimi
     * @param tyontekija tyontekijat.dat
     * @param lisakoulutus lisakoulutukset.dat
     */
    public void luoTiedosto(String rekisteri, String tyontekija, String lisakoulutus) {
        File dir = new File(rekisteri);
        dir.mkdirs();
        tyontekijat.luoTiedosto(rekisteri+"/"+tyontekija);
        lisakoulutukset.luoTiedosto(rekisteri+"/"+lisakoulutus);
        setTiedosto(rekisteri, tyontekija, lisakoulutus);
    }
}