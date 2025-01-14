package modelli;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import fi.jyu.mit.fxgui.Dialogs;

/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka, jossa muodostetaan lisakoulutuksista taulukko.
 */
public class Lisakoulutukset{
    
    private String kokoNimi = "";
    
	private static int maxLisakoulutuksia   = 1;
    private static int              lkm           = 0;
    private static Lisakoulutus      alkiot[]      = new Lisakoulutus[maxLisakoulutuksia];
    private static String tiedostonPerusNimi = "lisakoulutukset";
	
    
    /**
     * lisäkoulutuksien alustus, mutta sitä ei tarvita.
     */
	public Lisakoulutukset(){
		//ei tarvitse mitään
	}
	
	
	/**
	 * @param i indeksi.
	 * @return palauttaa kyseisen lisäkoulutuksen.
	 * @throws IndexOutOfBoundsException virhe ilmoitus, jos indeksi on laiton.
	 */
	public Lisakoulutus anna(int i) throws IndexOutOfBoundsException {
    	if(alkiot[i] == null){
    		throw new IndexOutOfBoundsException("Laiton " + i);
    	}
		return alkiot[i];
	}
	
	
	/**
	 * Antaa lisäkoulutuksien lukumäärän.
	 * @return lisäkoulutuksien lukumäärän
	 */
	public int getLkm(){
		return lkm;
	}
	
	
	/**
	 * Lisää lisäkoulutukset taulukkoon.
	 * @param lisakoulutukset taulukko lisättävistä lisäkoulutuksista
	 * @return alkiot koko lisäkoulutus taulukon, jossa on kaikki rekisterissä olevien
	 */
	public Lisakoulutus[] lisaa(Lisakoulutus[] lisakoulutukset) {
		int uusienMaara = 0;
		int indeksi = 0;
    	for(int i = 0; i<lisakoulutukset.length; i++){
    		if(lisakoulutukset[i] != null){
    			uusienMaara++;
    		}
    	}
        if (lkm >= alkiot.length-uusienMaara){
        	int mahtuu = alkiot.length-lkm;
        	maxLisakoulutuksia = maxLisakoulutuksia + uusienMaara - mahtuu;
        	int lisays = uusienMaara - mahtuu;
        	Lisakoulutus[] lisaaAlkio = new Lisakoulutus[alkiot.length + uusienMaara - mahtuu];
        	for(int k = 0; k<alkiot.length; k++){
        		lisaaAlkio[k] = alkiot[k];
        	}
        	if(mahtuu >= 1){
        		lisaaAlkio[lkm] = lisakoulutukset[0];
        		indeksi++;
        		lkm++;
        		if(mahtuu >= 2){
        			lisaaAlkio[lkm] = lisakoulutukset[1];
        			indeksi++;
        			lkm++;
        			if(mahtuu == 3){
        				lisaaAlkio[lkm] = lisakoulutukset[2];
        				indeksi++;
        				lkm++;
        			}
        		}
        	}
        	switch (lisays) {
			case 1: lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
				break;
			case 2: lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
    			lisaaAlkio[lkm] = lisakoulutukset[indeksi];
    			indeksi++;
    			lkm++;
    			break;
			case 3: lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
    			lisaaAlkio[lkm] = lisakoulutukset[indeksi];
    			indeksi++;
    			lkm++;
    			lisaaAlkio[lkm] = lisakoulutukset[indeksi];
    			indeksi++;
    			lkm++;
    			break;
			case 4: lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
				lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
				lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
				lisaaAlkio[lkm] = lisakoulutukset[indeksi];
				indeksi++;
				lkm++;
				break;
			default:
				break;
			}
        	alkiot = new Lisakoulutus[lisaaAlkio.length];
        	for(int i = 0; i<lisaaAlkio.length; i++){
        		alkiot[i] = lisaaAlkio[i];
        	}
        }
        else{
	        for(int i = 0; i<lisakoulutukset.length; i++){
	        	if(lisakoulutukset[i] == null){
	        		break;
	        	}
        		alkiot[lkm] = lisakoulutukset[i];
        		lkm++;
	        }
        }
		return alkiot;
    }


	/**
	 * Asetetaan lisäkoulutuksille Id:t
	 * @param KasiteltavaTyontekija jonka lisäkoulutuksia käsitellään
	 * @param lisakoulutukset työntekijän lisäkoulutukset.
	 */
	public void laitaId(Tyontekija KasiteltavaTyontekija, Lisakoulutus[] lisakoulutukset) {
		for(int i = 0; i<lisakoulutukset.length; i++){
			for(int j = 0; j<alkiot.length; j++){
				if(lisakoulutukset[i]==null){
					break;
				}
				if(lisakoulutukset[i].equals(alkiot[j])){
				    int id = 1;
				    if(alkiot.length>1){
				        id = alkiot[j-1].getId()+1;
				    }
					alkiot[j].setId(id);
					alkiot[j].setTyontekijanId(KasiteltavaTyontekija.getId());
				}
			}
		}
	}


	/**
     * Lukee työntekijät tiedostosta. 
     * @param tied tiedoston perusnimi
     */
    public void lueTiedostosta(String tied) {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            if ( kokoNimi == null ) {
                Dialogs.showMessageDialog("Tiedoston nimi puuttuu.");
            }
            String rivi = fi.readLine();
            if ( rivi == null ) {
                Dialogs.showMessageDialog("Maksimikoko puuttuu.");
            }

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Lisakoulutus[] lisakoulutus = poimiLisakoulutukset(rivi);
                lisaa(lisakoulutus);
                
            }
        } catch ( FileNotFoundException e ) {
            Dialogs.showMessageDialog("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            Dialogs.showMessageDialog("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /**
     * Poimitaan lisäkoulutuksen tiedot teidoston riviltä.
     * @param rivi tiedoston rivi
     * @return poimittu lisäkoulutus.
     */
	private Lisakoulutus[] poimiLisakoulutukset(String rivi) {
		Lisakoulutus koulutus = new Lisakoulutus();
		koulutus.poimiLisakoulutus(rivi);
		Lisakoulutus[] lisakoulutus = {koulutus};
		return lisakoulutus;
	}


	/**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     */
    public void lueTiedostosta() {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Asettaa tiedoston perusnimen.
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi){
        tiedostonPerusNimi = nimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


	/**
	 * Lisätään tiedostoon uudet lisäkoulutukset.
	 */
	public void lisaaTiedostoon() {
		try(PrintWriter output = new PrintWriter(new FileWriter("temp.dat",true))){
			output.println(alkiot.length);
    		for(int i = 0; i<alkiot.length; i++){
    			if(alkiot[i]==null){
    				break;
    			}
    			output.println(alkiot[i].tulostuu());
    		}
    	} catch ( FileNotFoundException e ) {
    	    Dialogs.showMessageDialog("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            Dialogs.showMessageDialog("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    	File alkuperainenTiedosto = new File(getTiedostonNimi());
        alkuperainenTiedosto.delete();
        File muokattu = new File("temp.dat");
        muokattu.renameTo(alkuperainenTiedosto);
	}
	

	/**
	 * Muokataan olemassa oleva lisäkoulutus.
	 * @param i indeksi
	 * @param lisakoulutus muokattu lisäkoulutus.
	 */
	public void muokkaaLisakoulutus(int i, Lisakoulutus lisakoulutus) {
		alkiot[i] = lisakoulutus;
	}
	
	
	/**
	 * muokataan olemassa oleva lisäkoulutus.
	 * @param id lisäkoulutuksen id
	 * @param koulutus lisäkoulutuksen nimi
	 * @param suoritus lisäkoulutuksen suorituspäivämäärä
	 * @param voimassa lisäkoulutuksen voimassaolopäivämäärä
	 */
	public void muokkaaLisakoulutus(int id, String koulutus, String suoritus, String voimassa) {
		for(int i = 0; i<alkiot.length; i++){
			if(alkiot[i].getId() == id){
				alkiot[i].setKoulutus(koulutus);
				alkiot[i].setSuoritusPvm(suoritus);
				alkiot[i].setVoimassaolo(voimassa);
			}
		}
	}

	
	/**
	 * Poistetaan poistettavan työntekijän lisäkoulutukset tiedostosta.
	 * @param poistettava työntekijä, joka poistetaan
	 */
	public void poistaTiedostosta(int poistettava) {
    	try(PrintWriter output = new PrintWriter(new FileWriter("temp.dat",true))){
    		output.println(maxLisakoulutuksia);
    		for(int i = 0; i<alkiot.length; i++){
    			if(alkiot[i] == null){
    				break;
    			}
    			else if(alkiot[i].getTyontekijanId() == poistettava){
    				lkm--;
    			}
    			else{
    				output.println(alkiot[i].tulostuu());
    			}
    		}
    	} catch ( FileNotFoundException e ) {
    	    Dialogs.showMessageDialog("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            Dialogs.showMessageDialog("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    	
    	File alkuperainenTiedosto = new File(getTiedostonNimi());
        alkuperainenTiedosto.delete();
        File muokattu = new File("temp.dat");
        muokattu.renameTo(alkuperainenTiedosto);	
	}

	
	/**
	 * Poistetaan poistettavan työntekijän lisäkoulutukset taulukosta.
	 * @param poistettava työntekijä, joka poistetaan
	 */
	public void poistaTaulukosta(int poistettava) {
		int poistettavienMaara = 0;
		for(Lisakoulutus alkio : alkiot){
			if(alkio != null){
				if(alkio.getTyontekijanId() == poistettava){
					poistettavienMaara++;
				}
			}
		}
		Lisakoulutus[] apu = new Lisakoulutus[alkiot.length-poistettavienMaara];
		int indeksi = 0;
		for(Lisakoulutus i : alkiot){
			if(i != null){
				if(poistettava != i.getTyontekijanId()){
					apu[indeksi] = i;
					indeksi++;
				}
			}
		}
		alkiot = new Lisakoulutus[apu.length];
		for(int j = 0; j<apu.length; j++){
			alkiot[j] = apu[j];
		}
	}


    /**
     * poistetaan lisäkoulutus taulukosta.
     * @param id lisäkoulutuksen id
     */
    public void poistaKoulutusTaulukosta(int id) {
        Lisakoulutus[] apu = new Lisakoulutus[alkiot.length-1];
        int indeksi = 0;
        for(Lisakoulutus i : alkiot){
            if(id != i.getId()){
                apu[indeksi] = i;
                indeksi++;
            }
        }
        alkiot = new Lisakoulutus[apu.length];
        for(int j = 0; j<apu.length; j++){
            alkiot[j] = apu[j];
        }
    }


    /**
     * poistetaan lisäkoulutus tiedostosta.
     * @param id lisäkoulutuksen id
     */
    public void poistaKoulutusTiedostosta(int id) {
        try(PrintWriter output = new PrintWriter(new FileWriter("temp.dat",true))){
            output.println(maxLisakoulutuksia);
            for(int i = 0; i<alkiot.length; i++){
                if(alkiot[i] == null){
                    break;
                }
                else if(alkiot[i].getId() == id){
                    lkm--;
                }
                else{
                    output.println(alkiot[i].tulostuu());
                }
            }
        } catch ( FileNotFoundException e ) {
            Dialogs.showMessageDialog("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            Dialogs.showMessageDialog("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
        File alkuperainenTiedosto = new File(getTiedostonNimi());
        alkuperainenTiedosto.delete();
        File muokattu = new File("temp.dat");
        muokattu.renameTo(alkuperainenTiedosto);
    }


    /**
     * Luodan uusi jasenrekisterin lisakoulutukset.dat
     * @param nimi tiedoston nimi
     */
    @SuppressWarnings("resource")
    public void luoTiedosto(String nimi) {
        try{
            PrintWriter writer = new PrintWriter(nimi+".dat");
            writer.println("4");
            writer.close();
        } catch (IOException e) {
           // do something
        }
    }
	
}