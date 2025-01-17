package tulostus;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelli.Lisakoulutus;
import modelli.Palvelutalo;
import modelli.Tyontekija;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import fi.jyu.mit.fxgui.Dialogs;
import fxPalvelutalo.*;


/**
 * @author Miia Laurila
 * @version 11.5.2017
 *
 * Luokka, jossa hoidetaan tulostaminen.
 */	
public class TulostusController { 

	@FXML private TextArea teksti;
	
	private PalvelutaloGUIController obj = new PalvelutaloGUIController();
	private Palvelutalo palvelutalo = new Palvelutalo();
	private String haku;
	private String hakusana;
	private String nimenPaivays;
	
	
	/**
	 * Alustus
	 */
	@FXML private void initialize(){
		DateFormat muoto = new SimpleDateFormat("ddMMyyyyHHmmss");
		Date paivays = new Date();
		nimenPaivays = muoto.format(paivays);
		muoto = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String paiva = muoto.format(paivays);
		teksti.setEditable(false);
		int[] tulostettavatId = PalvelutaloGUIController.tyontekijat;
		haku = PalvelutaloGUIController.haunRajaus;
		hakusana = PalvelutaloGUIController.hakusana;
		if(hakusana == null){
		    hakusana = "";
		}
		if(hakusana.equals("")){
		    teksti.setText("Tulostettu: "+paiva+"\n"+"\n"+"Työntekijät"+"\n"+"\n"+ "---------------------------------------------"+"\n"+ Tulosta(tulostettavatId));
		}
		else{
			teksti.setText("Tulostettu: "+paiva+"\n"+"\n"+"Työntekijät"+"\n"+"hakusana: "+hakusana+"\n"+"hakuehto: "+haku+"\n"+ "---------------------------------------------"+"\n"+ Tulosta(tulostettavatId));
		}
	}
	
	
	/**
     * Poistu napin painallus.
     */
    @FXML void handlePoistu(){
        Stage stage = PalvelutaloGUIController.getTulostus();
        obj.handleSulje(stage, false);
    }
    
    
    /**
     * Muodostetaan tiedosto, jossa tiedot tulostuksesta.
     * @throws FileNotFoundException Jos tiedostoa ei löydy
     */
    @FXML void handleTulosta() throws FileNotFoundException{
        String tulosta = teksti.getText();
        String nimi = "tyontekijat"+"_"+hakusana+"_"+haku+"_"+nimenPaivays+".txt";
        if(hakusana == null){
            nimi = "tyontekijat"+"_"+nimenPaivays+".txt";
        }
        try(PrintWriter out = new PrintWriter(nimi)){
            out.println(tulosta);
        }
        Dialogs.showMessageDialog("Tiedot tulostettu tiedostoon "+ nimi +" onnistuneesti.");
        Stage stage = PalvelutaloGUIController.getTulostus();
        obj.handleSulje(stage, false);
    }


    /**
     * Muodostetaan tiedoista String-tyyppinen muuttuja.
     * @param tulostettavatId kaikkien työntekijöiden id:t, jotka tulostetaan
     * @return tulostettava
     */
	private String Tulosta(int[] tulostettavatId) {
		String tulosta = "";
		int lisakoulutuksienLkm = palvelutalo.getLisakoulutus();
		for(int i = 0; i<tulostettavatId.length; i++){
			String tyosopimus = "On";
			Tyontekija tyontekija = palvelutalo.annaTyontekijaIdnAvulla(tulostettavatId[i]);
			if(tyontekija.getTyosopimus() == false){
				tyosopimus = "Ei";
			}
			tulosta += "\n"+"Nimi:"+"\t"+"\t"+"\t"+tyontekija.getNimi()+"\n"+"Hetu: "+"\t"+"\t"+"\t"+tyontekija.getHetu()+"\n"+"Työnimike: "+"\t"+"\t"+tyontekija.getTyonimike()+"\n"+"Katuosoite: "+"\t"+"\t"+tyontekija.getKatuosoite()+"\n"+"Postinumero: "+"\t"+"\t"+tyontekija.getPostinumero()+"\n"+"Postitoimipaikka: "+"\t"+tyontekija.getPostitoimipaikka()+"\n"+"Puhelinnumero: "+"\t"+tyontekija.getPuhelinnumero()+"\n"+"Työsopimus: "+"\t"+"\t"+tyosopimus+"\n";
			tulosta += "\n"+"Lisäkoulutus: "+"\t"+" Suorituspäivämäärä: "+"\t"+" Voimassaolopäivämäärä: ";
			for(int j = 0; j<lisakoulutuksienLkm; j++){
				Lisakoulutus lisakoulutus = palvelutalo.annaLisakoulutus(j);
				if(tyontekija.getId() == lisakoulutus.getTyontekijanId()){
					String koulutus = lisakoulutus.getKoulutus().getValue();
					while(koulutus.length() <= 13){
						koulutus += " ";
					}
					koulutus += "\t";
					tulosta += "\n"+koulutus+" "+lisakoulutus.getSuoritusPvm().getValue()+"\t"+"\t"+"\t"+" "+lisakoulutus.getVoimassaolo().getValue();
				}
			}
			tulosta += "\n"+"\n"+"---------------------------------------------"+"\n";
		}
		return tulosta;
	}
}