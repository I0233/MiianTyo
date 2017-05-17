package fxPalvelutalo;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 * P��ohjelma, joka k�ynnist�� ty�ntekij�rekisterin
 */
public class PalvelutaloMain extends Application {
	
	static String rekisteri = "Ty�ntekij�rekisteri";
	
	/**
	 * Lataa k�ytt�liittym�n ensimm�isen ikkunan.
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
		    FXMLLoader ldr = new FXMLLoader(getClass().getResource("/kaynnistys/KaynnistysView.fxml"));
		    Pane root = (Pane)ldr.load();
		    final Scene scene = new Scene(root);
		    scene.getStylesheets().add(getClass().getResource("palvelutalo.css").toExternalForm());
		    primaryStage.setScene(scene);
			primaryStage.setTitle(getRekisteri());
		    primaryStage.show();
		} catch(Exception e) {
		    e.printStackTrace();
		}
	}
	
	
	/**
	 * K�ynnist�� k�ytt�liittym�n
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	
	/**
	 * Palauttaa rekisterin otsikon
	 * @return ty�ntekij�rekisterin otsikon.
	 */
	public static String getRekisteri(){
		return rekisteri;
	}
}