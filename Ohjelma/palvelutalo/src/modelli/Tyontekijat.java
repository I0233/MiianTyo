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
 * Luokka, jossa muodostetaan työntekijöistä taulukko
 */
public class Tyontekijat {
    
    private String kokoNimi = "";

    private static int maxTyontekija   = 5;
    private static int              lkm           = 0;
    private static Tyontekija       alkiot[]      = new Tyontekija[maxTyontekija];
    private static String tiedostonPerusNimi = "tyontekijat";


    /**
     * Oletusmuodostaja
     */
    public Tyontekijat() {
        // Attribuuttien oma alustus riittää
    }


    /**
     * Lisätään rekisterin työntekijä taulukkoon uusi työntekijä
     * @param tyontekija lisättävä työntekijä
     */
    public void lisaa(Tyontekija tyontekija) {
		if (lkm >= alkiot.length){
        	maxTyontekija = lkm+1;
        	Tyontekija lisaaAlkio[] = new Tyontekija[maxTyontekija];
        	for(int k = 0; k<lisaaAlkio.length-1; k++){
        		lisaaAlkio[k] = alkiot[k];
        	}
        		lisaaAlkio[lisaaAlkio.length-1] = tyontekija;
        		alkiot = new Tyontekija[lisaaAlkio.length];
        		for(int i = 0; i<lisaaAlkio.length; i++){
        		
        			alkiot[i] = lisaaAlkio[i];
        		}
        		
        }
        alkiot[lkm] = tyontekija;
        lkm++;
    }


    /**
     * Palauttaa halutun työntekijän
     * @param i työntekijä, jonka id on i+1
     * @return työntekijä, jolla on haluttu id
     * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella  
     */
    public Tyontekija anna(int i) throws IndexOutOfBoundsException {
    	if(alkiot[i] == null){
    		throw new IndexOutOfBoundsException("Laiton " + i);
    	}
		return alkiot[i];
	}
    
    
    /**
     * Palauttaa työntekijöiden lukumäärän
     * @return työntekijöiden lukumäärä
     */
    public int getLkm() {
        return lkm;
    }


	/**
	 * Asetetaan id.
	 * @param tyontekija työntekijä, jolle asetetaan id.
	 */
	public void laitaId(Tyontekija tyontekija) {
		tyontekija.setId();
	}
	
	

    /**
     * Lukee jäsenistön tiedostosta. 
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
                Dialogs.showMessageDialog("Maksimikoko puuttuu");
            }

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Tyontekija tyontekija = new Tyontekija();
                tyontekija.poimiTyontekijanTiedot(rivi);
                lisaa(tyontekija);
            }
        } catch ( FileNotFoundException e ) {
            Dialogs.showMessageDialog("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            Dialogs.showMessageDialog("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
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
        return tiedostonPerusNimi+ ".dat";
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Lisätään muokkaukset tiedostoon.
     */
    public void lisaaMuokkauksetTiedostoon() {
    	try(PrintWriter output = new PrintWriter(new FileWriter("temp.dat",true))){
    		output.println(maxTyontekija);
    		for(int i = 0; i<alkiot.length; i++){
    			if(alkiot[i] == null){
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
	 * Poistetaan työntekijä tiedostosta
	 * @param poistettava poistettava työntekijä
	 */
	public void poistaTiedostosta(int poistettava) {
		lkm--;
    	try(PrintWriter output = new PrintWriter(new FileWriter("temp.dat",true))){
    		output.println(maxTyontekija);
    		for(int i = 0; i<alkiot.length; i++){
    			if(alkiot[i] == null){
    				break;
    			}
    			else if(alkiot[i].getId() == poistettava){
    				continue;
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
	 * Poistetaan työntekijä taulukosta.
	 * @param poistettava poistettava työntekijä
	 */
	public void poistaTaulukosta(int poistettava) {
		int indeksi = 0;
		Tyontekija apu[] = new Tyontekija[alkiot.length-1];
		for(Tyontekija i : alkiot){
		    if(i == null){
		        break;
		    }
			if(poistettava != i.getId()){
				apu[indeksi] = i;
				indeksi++;
			}
		}
		alkiot = new Tyontekija[apu.length];
		for(int j = 0; j<apu.length; j++){
			alkiot[j] = apu[j];
		}
	}


	/**
	 * Palautetaan työntekijä, jolla on haluttu id.
	 * @param id halutun työntekijän id
	 * @return työntekijä, jolla on haluttu id
	 */
	public Tyontekija annaTyontekijaId(int id) {
		Tyontekija haluttu = null;
		for(int i = 0; i<alkiot.length; i++){
		    if(alkiot[i] == null){
		        break;
		    }
			if(alkiot[i].getId() == id){
				haluttu = alkiot[i];
			}
		}
		return haluttu;
	}


    /**
     * Luodan uusi jasenrekisterin tyontekijat.dat
     * @param nimi tiedoston nimi
     */
    @SuppressWarnings("resource")
    public void luoTiedosto(String nimi) {
        try{
            PrintWriter writer = new PrintWriter(nimi+".dat");
            writer.println("1");
            writer.close();
        } catch (IOException e) {
           // do something
        }
    }
}