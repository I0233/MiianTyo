package tietoja;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import fxPalvelutalo.*;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka n‰ytt‰m‰‰n tietoja hoitamiseksi
 */	
public class TietojaController { 

	private PalvelutaloGUIController obj = new PalvelutaloGUIController();
	
	/**
	 * Ok napin painallus.
	 */
	@FXML void handleOk(){
		Stage stage = PalvelutaloGUIController.getTietoja();
		obj.handleSulje(stage, false);
	}
}
