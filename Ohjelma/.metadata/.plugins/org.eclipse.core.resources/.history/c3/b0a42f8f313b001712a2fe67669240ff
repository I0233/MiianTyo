package muokkaaminen;

import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import modelli.*;
import lisaaminen.*;
import fxPalvelutalo.*;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka työntekijän tietojen muokkaamisen tapahtumien hoitamiseksi
 */	
public class MuokkaaTyontekijanTietojaController { 
    
	private ObservableList<String> tyosopimusLista = FXCollections.observableArrayList("On", "Ei");
	private ObservableList<String> lisakoulutusLista = FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi");
	
	@FXML private ComboBox<String> tyosopimus;
	@FXML private ComboBox<String> lisakoulutus1;
	@FXML private GridPane koulutus_1;
	@FXML private Pane plus;
	@FXML private VBox vBox;
	@FXML private VBox koulutus1;
	@FXML private Button nappi;
	@FXML private TextField nimi;
	@FXML private TextField hetu;
	@FXML private TextField tyonimike;
	@FXML private TextField katuosoite;
	@FXML private TextField postinumero;
	@FXML private TextField postitoimipaikka;
	@FXML private TextField puhelinnumero;
	@FXML private TextField suoritus1;
	@FXML private TextField voimassa1;
	
	private TextField suoritus4 = new TextField();
	private TextField voimassa4 = new TextField();
	private ComboBox<String> lisakoulutus4= new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	private Node kysymysboksi;
	private VBox koulutus2;
	private Tyontekija tyontekija;
	private Lisakoulutus[] tyontekijanLisakoulutukset = new Lisakoulutus[4];
	private int tyontekijanAlkuperaisetLisakoulutukset = 0;
	private String valittu;
	private String toinenValittu;
	private String kolmasValittu;
	private VBox koulutus;
	
	TextField suoritus2 = new TextField();
    TextField voimassa2 = new TextField();
    ComboBox<String> lisakoulutus2 = new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	TextField suoritus3 = new TextField();
    TextField voimassa3 = new TextField();
    ComboBox<String> lisakoulutus3 = new ComboBox<String>(FXCollections.observableArrayList("","EA1", "EA2", "LOVe", "Hygieniapassi"));
	int tyontekijanLisakoulutustenMaara = 0;
	Palvelutalo palvelutalo = new Palvelutalo();
	
	private PalvelutaloGUIController obj = new PalvelutaloGUIController();
	private UusiTyontekijaController uusi = new UusiTyontekijaController();

	
	/**
	 * Asetetaan vaihtoehdot ComboBoxeihin ja työntekijän tiedot.
	 */
	@FXML private void initialize() {
		asetaCombot();
		asetaValittuTyontekija();
		maaritaLisakoulutuksienMaara();
	}
	
	
	/**
     * Tallennetaan muokkaus.
     */
    @FXML void handleTallennaMuokkaus() {
        Boolean virheetonLisa1 = false;
        Boolean virheetonLisa2 = false;
        Boolean virheetonLisa3 = false;
        Boolean virheetonLisa4 = false;
        Boolean virheeton = palvelutalo.tarkistaKentat(nimi, hetu, tyonimike, katuosoite, postinumero, postitoimipaikka, puhelinnumero, tyontekija.getId());
        if(virheeton){
            virheetonLisa1 = palvelutalo.tarkistaLisakoulutus(lisakoulutus1, suoritus1, voimassa1, false);
            if(virheetonLisa1){
                virheetonLisa2 = palvelutalo.tarkistaLisakoulutus(lisakoulutus2, suoritus2, voimassa2, false);
                if(virheetonLisa2) {
                    virheetonLisa3 = palvelutalo.tarkistaLisakoulutus(lisakoulutus3, suoritus3, voimassa3, false);
                    if(virheetonLisa3){
                        virheetonLisa4 = palvelutalo.tarkistaLisakoulutus(lisakoulutus4, suoritus4, voimassa4, false);
                    }
                }
            }
        }
        if(virheeton && virheetonLisa1 && virheetonLisa2 && virheetonLisa3 && virheetonLisa4){
            Stage stage = PalvelutaloGUIController.getMuokkaaminen();
            muokkaaTyontekijanTiedot();
            muutaAlkuperaisetLisakoulutukset();
            saaUudetLisakoulutukset();
            palvelutalo.lisaaTiedostoon();
            palvelutalo.lisaaLisakoulutus();
            obj.handleSuljeJaMuokkaa(stage);
        }
    }
    
    
    /**
     * Peruutetaan.
     */
    @FXML void handlePeruutaMuokkaus(){
        Stage stage = PalvelutaloGUIController.getMuokkaaminen();
        obj.handleSulje(stage, false);
    }
    
    
    /**
     * Plus-nappi
     */
    @FXML void handleLisaaKoulutus(){
        Boolean luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus1, suoritus1, voimassa1, true);
        if(luodaankoUusi){
            lisaaLisakoulutus();
        }
    }
	
	
	/**
	 * Määritetään, kuinka monta lisäkoulutusta on kyseisellä työntekijällä
	 */
	private void maaritaLisakoulutuksienMaara() {
		int k = 0;
		int j = palvelutalo.getLisakoulutus();
		for(int i = 0; i<j; i++) {
			if(palvelutalo.annaLisakoulutus(i)!=null){
				if(palvelutalo.annaLisakoulutus(i).getTyontekijanId() == tyontekija.getId()){
					tyontekijanLisakoulutukset[k] = palvelutalo.annaLisakoulutus(i);
					tyontekijanLisakoulutukset[k].setId(palvelutalo.annaLisakoulutus(i).getId());
					k++;
					tyontekijanLisakoulutustenMaara++;
					tyontekijanAlkuperaisetLisakoulutukset++;
				}
			}
		}
		asetaLisakoulutukset();
	}

	
	/**
	 * Asetetaan työntekijän lisäkoulutukset näkymään näkymässä.
	 */
	private void asetaLisakoulutukset() {
		if(tyontekijanLisakoulutustenMaara >= 1){
			lisakoulutus1.setValue(tyontekijanLisakoulutukset[0].getKoulutus().getValue());
			suoritus1.setText(tyontekijanLisakoulutukset[0].getSuoritusPvm().getValue());
			voimassa1.setText(tyontekijanLisakoulutukset[0].getVoimassaolo().getValue());
			if(tyontekijanLisakoulutustenMaara>=2){
				kysymysboksi = koulutus1.getChildren().get(0);
				lisakoulutus2.setValue(tyontekijanLisakoulutukset[1].getKoulutus().getValue());
				suoritus2.setText(tyontekijanLisakoulutukset[1].getSuoritusPvm().getValue());
				voimassa2.setText(tyontekijanLisakoulutukset[1].getVoimassaolo().getValue());
				koulutus = asetaKoulutus(lisakoulutus2, true, suoritus2, voimassa2);
				vBox.getChildren().clear();
				vBox.getChildren().addAll(kysymysboksi, koulutus);
				if(tyontekijanLisakoulutustenMaara>=3){
		    		vBox.getChildren().clear();
		    		lisakoulutus3.setValue(tyontekijanLisakoulutukset[2].getKoulutus().getValue());
					suoritus3.setText(tyontekijanLisakoulutukset[2].getSuoritusPvm().getValue());
					voimassa3.setText(tyontekijanLisakoulutukset[2].getVoimassaolo().getValue());
		    		koulutus2 = asetaKoulutus(lisakoulutus3, true, suoritus3, voimassa3);
		    		kysymysboksi = koulutus.getChildren().get(0);
		    		vBox.getChildren().addAll(koulutus_1, kysymysboksi, koulutus2);
		    		if(tyontekijanLisakoulutustenMaara==4){
		        		vBox.getChildren().clear();
		        		lisakoulutus4.setValue(tyontekijanLisakoulutukset[3].getKoulutus().getValue());
		    			suoritus4.setText(tyontekijanLisakoulutukset[3].getSuoritusPvm().getValue());
		    			voimassa4.setText(tyontekijanLisakoulutukset[3].getVoimassaolo().getValue());
		            	VBox koulutus3 = asetaKoulutus(lisakoulutus4, false, suoritus4, voimassa4);
		            	Node kysymysboksi2 = koulutus2.getChildren().get(0);
		                vBox.getChildren().addAll(koulutus_1, kysymysboksi, kysymysboksi2, koulutus3);
		    		}
				}
			}
		}
	}

	
	/**
	 * Asetetaan työntekijän tiedot näkymään
	 */
	private void asetaValittuTyontekija() {
		tyontekija = palvelutalo.annaTyontekijaIdnAvulla(PalvelutaloGUIController.id);
		nimi.setText(tyontekija.getNimi());
		hetu.setText(tyontekija.getHetu());
		tyonimike.setText(tyontekija.getTyonimike());
		katuosoite.setText(tyontekija.getKatuosoite());
		postinumero.setText(String.valueOf(tyontekija.getPostinumero()));
		postitoimipaikka.setText(tyontekija.getPostitoimipaikka());
		puhelinnumero.setText(tyontekija.getPuhelinnumero());
		if(tyontekija.getTyosopimus()==true){
			tyosopimus.setValue("On");
		}
		else{
			tyosopimus.setValue("Ei");
		}
	}

	
	/**
	 * Asetetaan comboBoxit näyttämään tiedot
	 */
	private void asetaCombot() {
		tyosopimus.setItems(tyosopimusLista);
        lisakoulutus1.setItems(lisakoulutusLista);
	}
	
	
	/**
	 * Muodostetaan lisäkoulutuv VBox.
	 * @param lisakoulutus lisäkoulutus
	 * @param plusNappi lisätäänkö plus-nappi
	 * @param suori lisäkoulutuksen suorituspäivämäärä
	 * @param voima lisäkoulutuksen voimassaolopäivämäärä
	 * @return koulutus Boksi, joka sisältää koulutus boksin ja mahdollisen plus-napin
	 */
	private VBox asetaKoulutus(ComboBox<String> lisakoulutus, boolean plusNappi, TextField suori, TextField voima) {
		Label lisa = new Label("Lisäkoulutus: ");
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
		Pane napinPaikka = new Pane();
		nappi.setLayoutX(238);
	    nappi.setLayoutY(0);
		napinPaikka.getChildren().add(nappi);
		VBox koulutusVbox = new VBox(2);
		if(plusNappi == true){
			koulutusVbox.getChildren().addAll(uusiK, napinPaikka);
		}
		else{
			koulutusVbox.getChildren().addAll(uusiK);
		}
		return koulutusVbox;
	}
	

	/**
	 * Kerätään uudet lisäkoulutukset.
	 */
	private void saaUudetLisakoulutukset() {
		Lisakoulutus[] lisakoulutukset = new Lisakoulutus[4];
		if(tyontekijanAlkuperaisetLisakoulutukset < 4){
			if(!suoritus4.getText().trim().isEmpty()){
				uusi.luoLisakoulutus(lisakoulutus4.getSelectionModel().getSelectedItem(), suoritus4.getText(), voimassa4.getText(), 3, lisakoulutukset);
			}
			if(tyontekijanAlkuperaisetLisakoulutukset < 3){
				if(!suoritus3.getText().trim().isEmpty()){
					uusi.luoLisakoulutus(lisakoulutus3.getSelectionModel().getSelectedItem(), suoritus3.getText(), voimassa3.getText(), 2, lisakoulutukset);
				}
				if(tyontekijanAlkuperaisetLisakoulutukset < 2){
					if(!suoritus2.getText().trim().isEmpty()){
						uusi.luoLisakoulutus(lisakoulutus2.getSelectionModel().getSelectedItem(), suoritus2.getText(), voimassa2.getText(), 1, lisakoulutukset);
					}
					if(tyontekijanAlkuperaisetLisakoulutukset == 0){
						if(!suoritus1.getText().trim().isEmpty()){
							uusi.luoLisakoulutus(lisakoulutus1.getSelectionModel().getSelectedItem(), suoritus1.getText(), voimassa1.getText(), 0, lisakoulutukset);
						}
					}
				}
			}
		}
		int maara = 0;
		for(Lisakoulutus lisakoulutus : lisakoulutukset){
			if(lisakoulutus != null){
				maara++;
			}
		}
		Lisakoulutus[] apu = new Lisakoulutus[maara];
		int j = 0;
		for(int i = 0; i<lisakoulutukset.length; i++){
			if(lisakoulutukset[i] != null){
				apu[j] = lisakoulutukset[i];
				j++;
			}
		}
		if(apu.length > 0){
		    palvelutalo.lisaa(apu);
	        palvelutalo.asetaKoulutuksilleId(tyontekija, apu);
		}
	}


	/**
	 * Muutetaan työntekijän alkuperäiset lisäkoulutukset.
	 */
	private void muutaAlkuperaisetLisakoulutukset() {
		if(tyontekijanAlkuperaisetLisakoulutukset>0){
			String valittuKoulutus = lisakoulutus1.getSelectionModel().getSelectedItem();
			String suoritus = suoritus1.getText();
			String voimassa = voimassa1.getText();
			if(valittuKoulutus.equals("") && suoritus.equals("") && voimassa.equals("")){
			    palvelutalo.poistaLisakoulutus(tyontekijanLisakoulutukset[0].getId());
			}
			else{
			    palvelutalo.muokkaaLisakoulutus(tyontekijanLisakoulutukset[0].getId(), valittuKoulutus, suoritus, voimassa);
			}
			if(tyontekijanAlkuperaisetLisakoulutukset>1){
			    valittuKoulutus = lisakoulutus2.getSelectionModel().getSelectedItem();
				suoritus = suoritus2.getText();
				voimassa = voimassa2.getText();
				if(valittuKoulutus.equals("") && suoritus.equals("") && voimassa.equals("")){
	                palvelutalo.poistaLisakoulutus(tyontekijanLisakoulutukset[1].getId());
	            }
	            else{
	                palvelutalo.muokkaaLisakoulutus(tyontekijanLisakoulutukset[1].getId(), valittuKoulutus, suoritus, voimassa);
	            }
				if(tyontekijanAlkuperaisetLisakoulutukset>2){
				    valittuKoulutus = lisakoulutus3.getSelectionModel().getSelectedItem();
					suoritus = suoritus3.getText();
					voimassa = voimassa3.getText();
					if(valittuKoulutus.equals("") && suoritus.equals("") && voimassa.equals("")){
	                    palvelutalo.poistaLisakoulutus(tyontekijanLisakoulutukset[2].getId());
	                }
	                else{
	                    palvelutalo.muokkaaLisakoulutus(tyontekijanLisakoulutukset[2].getId(), valittuKoulutus, suoritus, voimassa);
	                }
					if(tyontekijanAlkuperaisetLisakoulutukset>3){
						valittuKoulutus = lisakoulutus4.getSelectionModel().getSelectedItem();
						suoritus = suoritus4.getText();
						voimassa = voimassa4.getText();
						if(valittuKoulutus.equals("") && suoritus.equals("") && voimassa.equals("")){
		                    palvelutalo.poistaLisakoulutus(tyontekijanLisakoulutukset[3].getId());
		                }
		                else{
		                    palvelutalo.muokkaaLisakoulutus(tyontekijanLisakoulutukset[3].getId(), valittuKoulutus, suoritus, voimassa);
		                }
					}
				}
			}
		}
	}

	
	/**
	 * Muutetaan työntekijän muokatut tiedot.
	 */
	private void muokkaaTyontekijanTiedot() {
			tyontekija.setNimi(nimi.getText());
			tyontekija.setHetu(hetu.getText());
			tyontekija.setTyonimike(tyonimike.getText());
			tyontekija.setKatuosoite(katuosoite.getText());
			tyontekija.setPostinumero(Integer.valueOf(postinumero.getText()));
			tyontekija.setPostitoimipaikka(postitoimipaikka.getText());
			tyontekija.setPuhelinnumero(puhelinnumero.getText());
			if(tyosopimus.getSelectionModel().getSelectedItem().equals("On")){
				tyontekija.setTyosopimus(true);
			}
			else{
				tyontekija.setTyosopimus(false);
			}
	}

	
	/**
	 * Tehdään lisää lisäkoulutus-bokseja
	 */
	private void lisaaLisakoulutus() {
		valittu = lisakoulutus1.getValue();
		if(tyontekijanLisakoulutustenMaara<=1){
			luoToinen();
		}
		if(tyontekijanLisakoulutustenMaara == 2){
        	uusiKoulutus3();
        	}
		if(tyontekijanLisakoulutustenMaara == 3){
			toinenValittu = lisakoulutus2.getValue();
			luoNeljas();
		}
	}
	
	
	/**
	 * Kolmannen lisäkoulutus boksin lisääminen sekä siihen liitetyn plus-napin toiminnallisuus
	 */
	private void uusiKoulutus3(){
		luoKolmas();
		nappi.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent event) {
			    Boolean luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus3, suoritus3, voimassa3, true);
			    if(luodaankoUusi){
			        luoNeljas();
			    }
			}
		 });
	}
	
	
	/**
	 * Toisen lisäkoulutus boksin lisääminen
	 */
	private void luoToinen() {
		tyontekijanLisakoulutustenMaara=0;
		ObservableList<String> lista = lisakoulutus2.getItems();
		kysymysboksi = koulutus1.getChildren().get(0);
		lista.remove(valittu);
		lisakoulutus2 = new ComboBox<String>(lista);
		koulutus = asetaKoulutus(lisakoulutus2, true, suoritus2, voimassa2);
		vBox.getChildren().clear();
		vBox.getChildren().addAll(kysymysboksi, koulutus);
		tyontekijanLisakoulutustenMaara++;
		nappi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(tyontekijanLisakoulutustenMaara == 1){
            	    Boolean luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus2, suoritus2, voimassa2, true);
            		if(luodaankoUusi){
            		    luoKolmas();
                        tyontekijanLisakoulutustenMaara++;
            		}
            	}
            	else{
            	    Boolean luodaankoUusi = palvelutalo.tarkistaLisakoulutus(lisakoulutus3, suoritus3, voimassa3, true);
            	    if(luodaankoUusi){
            	        luoNeljas();
            	    }
            	}
            }
		}); 
	}
	
	
	/**
	 * Kolmannen lisäkoulutus boksin luominen
	 */
	void luoKolmas(){
		toinenValittu = lisakoulutus2.getValue();
		ObservableList<String> lista2 = lisakoulutus3.getItems();
		lista2.remove(toinenValittu);
		lista2.remove(valittu);
		lisakoulutus3 = new ComboBox<String>(lista2);
		vBox.getChildren().clear();
		koulutus2 = asetaKoulutus(lisakoulutus3, true, suoritus3, voimassa3);
		kysymysboksi = koulutus.getChildren().get(0);
		vBox.getChildren().addAll(koulutus_1, kysymysboksi, koulutus2);
	}
	
	
	/**
	 * neljännen lisäkoulutus boksin luominen
	 */
	void luoNeljas() {
		kolmasValittu = lisakoulutus3.getValue();
		ObservableList<String> lista3 = lisakoulutus4.getItems();
		lista3.remove(valittu);
		lista3.remove(toinenValittu);
		lista3.remove(kolmasValittu);
		vBox.getChildren().clear();
    	VBox koulutus3 = asetaKoulutus(lisakoulutus4, false, suoritus4, voimassa4);
    	Node kysymysboksi2 = koulutus2.getChildren().get(0);
        vBox.getChildren().addAll(koulutus_1, kysymysboksi, kysymysboksi2, koulutus3);
	}
}
