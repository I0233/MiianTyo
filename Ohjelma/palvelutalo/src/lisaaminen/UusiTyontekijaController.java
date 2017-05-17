package lisaaminen;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import modelli.*;
import fxPalvelutalo.*;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka uuden ty�ntekij�n luomisen tapahtumien hoitamiseksi
 */	
public class UusiTyontekijaController{
	
	private ObservableList<String> tyosopimusLista = FXCollections.observableArrayList("On", "Ei");
	private ObservableList<String> lisakoulutusLista = FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi");
	
	@FXML private ComboBox<String> tyosopimus;
	@FXML private ComboBox<String> lisakoulutus1;
	@FXML private Pane plus;
	@FXML private VBox koulutus1;
	@FXML private Button nappi;
	@FXML private TextField uudenNimi;
	@FXML private TextField uudenHetu;
	@FXML private TextField uudenTyonimike;
	@FXML private TextField uudenKatuosoite;
	@FXML private TextField uudenPostinumero;
	@FXML private TextField uudenPostitoimipaikka;
	@FXML private TextField uudenPuhelinnumero;
	@FXML private TextField uudenSuoritus1;
	@FXML private TextField uudenVoimassa1;
	
	@FXML GridPane koulutus_1;
	@FXML VBox vBox;

	private Lisakoulutus lisakoulutukset[] = new Lisakoulutus[4];
    private PalvelutaloGUIController obj = new PalvelutaloGUIController();
    
    Palvelutalo palvelutalo = new Palvelutalo();
    TextField uudenSuoritus2 = new TextField();
    TextField uudenVoimassa2 = new TextField();
	TextField uudenSuoritus3 = new TextField();
	TextField uudenVoimassa3 = new TextField();
	TextField uudenSuoritus4 = new TextField();
	TextField uudenVoimassa4 = new TextField();
	ComboBox<String> lisakoulutus2 = new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	ComboBox<String> lisakoulutus3 = new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	ComboBox<String> lisakoulutus4= new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	int maara = 0;
	Node kysymysboksi;
	VBox koulutus2;
	String toinenValittu;
	
	
	/**
     * Asetetaan ComboBoxeihin vaihtoehdot.
     */
    @FXML private void initialize() {
        tyosopimus.setValue("On");
        tyosopimus.setItems(tyosopimusLista);
        
        lisakoulutus1.setItems(lisakoulutusLista);
    }
    
	
	/**
	 * K�sitell��n uuden j�senen napin painallus.
	 */
	@FXML void handleLisaaUusiTyontekija(){
	    Boolean virheetonLisa1 = false;
        Boolean virheetonLisa2 = false;
        Boolean virheetonLisa3 = false;
        Boolean virheetonLisa4 = false;
		Boolean virheeton = palvelutalo.tarkistaKentat(uudenNimi, uudenHetu, uudenTyonimike, uudenKatuosoite, uudenPostinumero, uudenPostitoimipaikka, uudenPuhelinnumero, 0);
		if(virheeton){
		    virheetonLisa1 = palvelutalo.tarkistaLisakoulutus(lisakoulutus1, uudenSuoritus1, uudenVoimassa1, false);
		    if(virheetonLisa1){
		        virheetonLisa2 = palvelutalo.tarkistaLisakoulutus(lisakoulutus2, uudenSuoritus2, uudenVoimassa2, false);
		        if(virheetonLisa2){
		            virheetonLisa3 = palvelutalo.tarkistaLisakoulutus(lisakoulutus3, uudenSuoritus3, uudenVoimassa3, false);
		            if(virheetonLisa3){
		                virheetonLisa4 = palvelutalo.tarkistaLisakoulutus(lisakoulutus4, uudenSuoritus4, uudenVoimassa4, false);
		            }
		        }
		    }
		}
		if(virheeton && virheetonLisa1 && virheetonLisa2 && virheetonLisa3 && virheetonLisa4){
			Tyontekija uusiTyontekija = saaTiedot();
			saaLisakoulutukset();
			palvelutalo.lisaa(uusiTyontekija, lisakoulutukset);
			palvelutalo.asetaTyontekijalleId(uusiTyontekija);
			palvelutalo.asetaKoulutuksilleId(uusiTyontekija, lisakoulutukset);
			palvelutalo.lisaaTiedostoon();
			palvelutalo.lisaaLisakoulutus();
			PalvelutaloGUIController.id = uusiTyontekija.getId();
			obj.handleSuljeJaLisaa();
		}
	}
	
	
	/**
     * K�sitell��n peruuttaminen.
     */
    @FXML void handlePeruutaLisaaminen(){
        Stage stage = PalvelutaloGUIController.getLisaaminen();
        obj.handleSulje(stage, false);
    }
    
    
    /**
     * Plus nappi.
     * @throws IOException 
     */
    @FXML void handleLisaaKoulutus(){
        Boolean luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus1, uudenSuoritus1, uudenVoimassa1, true);
        if(luodaankoUusi){
            uusikoulutus();
        }
    }


	/**
	 * Ker�t��n uuden ty�ntekij�n tiedot.
	 * @return uusiTyontekija uusi Ty�ntekij�
	 */
	private Tyontekija saaTiedot() {
		Boolean onkoTyosopimus = false;
		String valittuTyosopimus = tyosopimus.getSelectionModel().getSelectedItem();
		if(valittuTyosopimus.equals("On")){
			onkoTyosopimus = true;
		}
		Tyontekija uusiTyontekija = new Tyontekija();
		uusiTyontekija.setNimi(uudenNimi.getText());
		uusiTyontekija.setHetu(uudenHetu.getText());
		uusiTyontekija.setTyonimike(uudenTyonimike.getText());
		uusiTyontekija.setKatuosoite(uudenKatuosoite.getText());
		uusiTyontekija.setPostinumero(Integer.parseInt(uudenPostinumero.getText()));
		uusiTyontekija.setPostitoimipaikka(uudenPostitoimipaikka.getText());
		uusiTyontekija.setPuhelinnumero(uudenPuhelinnumero.getText());
		uusiTyontekija.setTyosopimus(onkoTyosopimus);
		return uusiTyontekija;
	}
	
	
	/**
	 * Ker�t��n ty�ntekij�n lis�koulutukset.
	 */
	public void saaLisakoulutukset(){
        if(!uudenSuoritus1.getText().trim().isEmpty()){
			luoLisakoulutus(lisakoulutus1.getSelectionModel().getSelectedItem(), uudenSuoritus1.getText(), uudenVoimassa1.getText(), 0, lisakoulutukset);
		}
		if(!uudenSuoritus2.getText().trim().isEmpty()){
			luoLisakoulutus(lisakoulutus2.getSelectionModel().getSelectedItem(), uudenSuoritus2.getText(), uudenVoimassa2.getText(), 1, lisakoulutukset);
		}
		if(!uudenSuoritus3.getText().trim().isEmpty()){
			luoLisakoulutus(lisakoulutus3.getSelectionModel().getSelectedItem(), uudenSuoritus3.getText(), uudenVoimassa3.getText(), 2, lisakoulutukset);
		}
		if(!uudenSuoritus4.getText().trim().isEmpty()){
		    luoLisakoulutus(lisakoulutus4.getSelectionModel().getSelectedItem(), uudenSuoritus4.getText(), uudenVoimassa4.getText(), 3, lisakoulutukset);
		}
	}
	
	
	/**
	 * Luodaan uusi lis�koulutus ty�ntekij�lle.
	 * @param koulutus valittu lis�koulutus
	 * @param suoritus suoritusp�iv�m��r�
	 * @param voimassa voimassaolop�iv�m��r�
	 * @param i monesko ty�ntekij�n lis�koulutus
	 * @param lisa lista, johon ty�ntekij�n lis�koulutukset listatataan.
	 */
    public void luoLisakoulutus(String koulutus, String suoritus, String voimassa, int i, Lisakoulutus[] lisa) {
    	String[] pvKkVvvv1 = suoritus.split("\\.");
		if(voimassa.toLowerCase().equals("toistaiseksi")){
			lisa[i] = new Lisakoulutus();
			lisa[i].setKoulutus(koulutus);
			lisa[i].setSuoritusPvm(PalvelutaloGUIController.muutaPaivamaara(LocalDate.of(Integer.parseInt(pvKkVvvv1[2]) , Integer.parseInt(pvKkVvvv1[1]) , Integer.parseInt(pvKkVvvv1[0]))));
			lisa[i].setVoimassaolo("Toistaiseksi");
		}
		else{
			String[] pvKkVvvv2 = voimassa.split("\\.");
			lisa[i] = new Lisakoulutus();
			lisa[i].setKoulutus(koulutus);
            lisa[i].setSuoritusPvm(PalvelutaloGUIController.muutaPaivamaara(LocalDate.of(Integer.parseInt(pvKkVvvv1[2]) , Integer.parseInt(pvKkVvvv1[1]) , Integer.parseInt(pvKkVvvv1[0]))));
            lisa[i].setVoimassaolo(PalvelutaloGUIController.muutaPaivamaara(LocalDate.of(Integer.parseInt(pvKkVvvv2[2]) , Integer.parseInt(pvKkVvvv2[1]) , Integer.parseInt(pvKkVvvv2[0]))));
		}
	}
	
	
	/**
	 * Muodotetaan boksi, johon saa lis�tty� uuden koulutuksen
	 */
	private void uusikoulutus() {
		String valittu = lisakoulutus1.getValue();
		ObservableList<String> lista = lisakoulutus2.getItems();
		kysymysboksi = koulutus1.getChildren().get(0);
		lista.remove(valittu);
		VBox koulutus = lisaaKoulutus(lisakoulutus2, true, uudenSuoritus2, uudenVoimassa2);
		vBox.getChildren().clear();
		vBox.getChildren().addAll(kysymysboksi, koulutus);
		nappi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Boolean luodaankoUusi = false;
            	if(maara == 1){
            	    luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus2, uudenSuoritus2, uudenVoimassa2, true);
            	    if(luodaankoUusi){
            	        toinenValittu = lisakoulutus2.getValue();
                        ObservableList<String> lista2 = lisakoulutus3.getItems();
                        lista2.remove(toinenValittu);
                        lista2.remove(valittu);
                        vBox.getChildren().clear();
                        koulutus2 = lisaaKoulutus(lisakoulutus3, true, uudenSuoritus3, uudenVoimassa3);
                        kysymysboksi = koulutus.getChildren().get(0);
                        vBox.getChildren().addAll(koulutus_1, kysymysboksi, koulutus2);
            	    }
            	}
            	else if(maara == 2){
            	    luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus3, uudenSuoritus3, uudenVoimassa3, true);
            	    if(luodaankoUusi){
            	        String kolmasValittu = lisakoulutus3.getValue();
                        ObservableList<String> lista3 = lisakoulutus4.getItems();
                        lista3.remove(valittu);
                        lista3.remove(toinenValittu);
                        lista3.remove(kolmasValittu);
                        vBox.getChildren().clear();
                        VBox koulutus3 = lisaaKoulutus(lisakoulutus4, false, uudenSuoritus4, uudenVoimassa4);
                        Node kysymysboksi2 = koulutus2.getChildren().get(0);
                        vBox.getChildren().addAll(koulutus_1, kysymysboksi, kysymysboksi2, koulutus3);
            	    }
            	}
            }
        });
	}
	
	
	/**
	 * lis�t��n uusi lis�koulutuss boksi.
	 * @param lisakoulutus lis�koulutus valinta
	 * @param plusNappi lis�t��nk� lis�koulutus boksin alle plus-nappi
	 * @param suori kentt�, johon kirjoitetaan suoritusp�iv�m��r�
	 * @param voima kentt�, johon kirjoitetaan voimassaolop�iv�m��r�
	 * @return koulutus VBox, joka sis�lt�� koulutus boksin ja mahdollisesti puls-napin
	 */
	VBox lisaaKoulutus(ComboBox<String> lisakoulutus, boolean plusNappi, TextField suori, TextField voima) {
		Label lisa = new Label("Lis�koulutus: ");
		Label suor = new Label("Suorituspvm: ");
		Label voim = new Label("Voimassaolo: ");
		GridPane uusiK = new GridPane();
		uusiK.getStylesheets().add("fxPalvelutalo/palvelutalo.css");
		uusiK.getStyleClass().add("vbox");
		suori.getStyleClass().add("tekstit");
		voima.getStyleClass().add("tekstit");
		lisakoulutus.getStyleClass().add("tekstit");
		GridPane.setMargin(suori, new Insets(5, 0, 0, 0));
		GridPane.setMargin(voima, new Insets(5, 0, 0, 0));
		uusiK.add(lisa, 1, 1);
		uusiK.add(lisakoulutus, 2, 1);
		uusiK.add(suor, 1, 2);
		uusiK.add(suori, 2, 2);
		uusiK.add(voim, 1, 3);
		uusiK.add(voima, 2, 3);
		maara++;
		Pane napinPaikka = new Pane();
		nappi.setLayoutX(238);
	    nappi.setLayoutY(0);
		napinPaikka.getChildren().add(nappi);
		VBox koulutus = new VBox(2);
		if(plusNappi == true){
			koulutus.getChildren().addAll(uusiK, napinPaikka);
		}
		else{
			koulutus.getChildren().addAll(uusiK);
		}
		return koulutus;
	}
}
