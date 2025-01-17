package kaynnistys;
import java.io.File;
import fxPalvelutalo.PalvelutaloGUIController;
import javafx.application.Platform;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import modelli.Palvelutalo;

/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka k�ynnistysn�kym�n tapahtumien hoitamiseksi
 */	
public class KaynnistysController{
	
	@FXML private TextField nimi;
	private static String rekisteri = "Chicago";
	private PalvelutaloGUIController obj = new PalvelutaloGUIController();
	private Stage aloitus;
	
	Palvelutalo palvelutalo = new Palvelutalo();
	
	
	/**
	 * Peruutetaan ohjelman avaaminen
	 */
	@FXML void handlePeru(){
		 Platform.exit();
	}
	
	
	/**
	 * Asetetaan oletusteksti
	 */
	@FXML private void initialize(){
		nimi.setText(rekisteri);
	}
	
	
	/**
	 * K�sitell��n, onko rekisteri olemassa.
	 */
	@FXML void handleJatka(){
		aloitus = (Stage) nimi.getScene().getWindow();
		rekisteri = nimi.getText();
		
		File f = new File("rekisterit");
		File[] list = f.listFiles();
		boolean onkoOlemassa = false;
		for(int i = 0; i<list.length; i++){
		    if(rekisteri.equals(list[i].getName())){
		        rekisteri = "rekisterit/"+rekisteri;
		        onkoOlemassa = true;
		    }
		}
		if(onkoOlemassa){
		    palvelutalo.lueTiedostosta(rekisteri, "tyontekijat", "lisakoulutukset");
			obj.handleSulje(aloitus, true);
		}
		else{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/virhe/Ilmoitus.fxml"));
			String otsikko = "Ilmoitus";
			obj.avaaNakyma(fxmlLoader, otsikko, aloitus, false, false);
		}
	}
	
	
	/**
	 * Palautetaan, mit� on kentt��n kirjoitettu.
	 * @return rekisteri palauttaa rekisterin kentt��n kirjoitetun nimen
	 */
	public static String getNimi(){
		return rekisteri;
	}
}
