package virhe;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import fxPalvelutalo.*;
import kaynnistys.*;
import modelli.Palvelutalo;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka virhe ikkunan tapahtumien hoitamiseksi
 */	
public class IlmoitusController {
	
	@FXML private Button ok;
	@FXML private Label palvelukeskus;
	
	private PalvelutaloGUIController obj = new PalvelutaloGUIController();
	private Palvelutalo palvelutalo = new Palvelutalo();
	private String nimi = KaynnistysController.getNimi();
	
	
	/**
	 * hakee nimen, joka on kirjoitettu ja lis�� tekstin.
	 */
	@FXML private void initialize(){
		palvelukeskus.setText(nimi+" nimist�");
	}

	
	/**
	 * K�sittelee peruuta-napin painalluksen.
	 */
	@FXML void handleSelva(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/kaynnistys/KaynnistysView.fxml"));
		String otsikko = PalvelutaloMain.getRekisteri();
		Stage virhe = (Stage) ok.getScene().getWindow();
		obj.avaaNakyma(fxmlLoader, otsikko, virhe, false, false);
	}
	
	@FXML void handleUusi(){
	    Stage virhe = (Stage) ok.getScene().getWindow();
	    nimi = "rekisterit/"+nimi;
	    palvelutalo.luoTiedosto(nimi, "tyontekijat", "lisakoulutukset");
	    obj.handleSulje(virhe, true);
	}
}
