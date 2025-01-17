package fxPalvelutalo;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import fi.jyu.mit.fxgui.*;
import javafx.application.Platform;
import javafx.fxml.*;
import modelli.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 *Luokka pääikkunan tapahtumien hoitamiseksi
 */		
public class PalvelutaloGUIController implements Initializable {
	
	private ObservableList<Lisakoulutus> koulutusData = FXCollections.observableArrayList();
	private ObservableList<String> hakuehtoLista = FXCollections.observableArrayList("","Nimi", "Työnimike","Työsopimus","EA1","EA2", "LOVe", "Hygieniapassi", "Voimassaolo");
	
	@FXML private Label nimiLabel;
	@FXML private Label hetuLabel;
	@FXML private Label tyonimikeLabel;
	@FXML private Label katuosoiteLabel;
	@FXML private Label postinroLabel;
	@FXML private Label postitoimiLabel;
	@FXML private Label puhnroLabel;
	@FXML private Label tyosopimusLabel;
	@FXML private ListChooser<Tyontekija> chooserTyontekija;
	@FXML private TableView<Lisakoulutus> lisakoulutusTable;
	@FXML private TableColumn<Lisakoulutus, String> lisakoulutusColumn;
	@FXML private TableColumn <Lisakoulutus, String>suorituspvmColumn;
	@FXML private TableColumn <Lisakoulutus, String>voimassaoloColumn;
	@FXML private TextField haku;
	@FXML private ComboBox<String> hakuehto;
	@FXML private HBox button;
	
	private Palvelutalo palvelutalo = new Palvelutalo();
	
	private static Stage lisaaminen = new Stage();
	private static Stage muokkaaminen = new Stage();
	private static Stage tietoja = new Stage();
	private static Stage tulostus = new Stage();
	private static Stage paanakyma;
	private static FXMLLoader ldr;
	private static Parent root;
	
	/**
	 * ID, jonka avulla voidaan muokata haluttua työntekijää ja näyttää se listauksessa.
	 */
	public static int id = 0;
	
	/**
	 * Tulostettavat työntekijät.
	 */
	public static int[] tyontekijat;
	
	/**
	 * Haussa käytettävä rajaus.
	 */
	public static String haunRajaus;
	
	/**
	 * Hakusana haussa.
	 */
	public static String hakusana;
	
	
	/**
	 * Lisätään esimerkki työntekijän tiedot ja lisäkoulutukset pääikkuna näkymään.
	 * Lisätään hakuehdot.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		chooserTyontekija.clear();
        hakuehto.setItems(hakuehtoLista);
		int i = palvelutalo.getTyontekija();
		for(int j = 0; j<i; j++){
			chooserTyontekija.add(palvelutalo.annaTyontekija(j).getNimi(), palvelutalo.annaTyontekija(j));
		}
        chooserTyontekija.addSelectionListener(e -> naytaTyontekija());
	}
	
	
	/**
     * Tallenna-napin toiminnallisuus.
     */
    @FXML void handleTallennaMuokkaus() {
        hae(id);
        haunRajaus = hakuehto.getSelectionModel().getSelectedItem();
        hakusana = haku.getText();
        if(hakusana.length() != 0 && haunRajaus == null){
            Dialogs.showMessageDialog("Valitse hakuehto.");
        }
        if(haunRajaus != null){
            kasitteleHaku();
        }
    }
    
    
    /**
     * Avataan työntekijän lisäämis-näkymä.
     */
    @FXML void handleLisaaTyontekija(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lisaaminen/UusiTyontekija.fxml"));
        String otsikko = "Lisää uusi työntekijä";
        avaaNakyma(fxmlLoader, otsikko, lisaaminen, true, true);
    }
    
    
    /**
     * Avataan työntekijän tietojen muokkaus-näkymä.
     */
    @FXML void handleMuokkaaTyontekija(){
        if(chooserTyontekija.getSelectedObject() == null){
            Dialogs.showMessageDialog("Valitse muokattava työntekijä");
        }
        else{
            Tyontekija valittuTyontekija = chooserTyontekija.getSelectedObject();
            id = valittuTyontekija.getId();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/muokkaaminen/MuokkaaTyontekijanTietoja.fxml"));
            String otsikko = "Muokkaa työntekijän tietoja";
            avaaNakyma(fxmlLoader, otsikko, muokkaaminen, true, true);
        }
    }
    
    
    /**
     * Työntekijän poistaminen.
     */
    @FXML void handlePoistaTyontekija() {
        if(chooserTyontekija.getSelectedObject() == null){
            Dialogs.showMessageDialog("Valitse poistettava työntekijä.");
        }
        else{
            int poistettava = chooserTyontekija.getSelectedObject().getId();
            palvelutalo.poista(poistettava);
            chooserTyontekija.clear();
            int i = palvelutalo.getTyontekija();
            for(int j = 0; j<i; j++){
                chooserTyontekija.add(palvelutalo.annaTyontekija(j).getNimi(), palvelutalo.annaTyontekija(j));
            }
            nimiLabel.setText("");
            hetuLabel.setText("");
            tyonimikeLabel.setText("");
            katuosoiteLabel.setText("");
            postinroLabel.setText("");
            postitoimiLabel.setText("");
            puhnroLabel.setText("");
            tyosopimusLabel.setText("");
            for (int k = 0; k<lisakoulutusTable.getItems().size(); k++) {
                lisakoulutusTable.getItems().clear();
            }
        }
    }
    
	
	/**
	 * Tulosta napin toiminnallisuus.
	 */
	@FXML void handleTulosta(){
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tulostus/Tulostus.fxml"));
		String otsikko = "Tulostus";
		tyontekijat = new int[chooserTyontekija.getItems().size()];
		for(int i = 0; i< chooserTyontekija.getItems().size(); i++){
			tyontekijat[i] = chooserTyontekija.getItems().get(i).getObject().getId();
		}
		avaaNakyma(fxmlLoader, otsikko, tulostus, false, true);
	}
    
    
    /**
     * Piilotetaan pääikkuna jonkun muun näkymän ollessa auki.
     */
    @FXML void handlePiilota(){
        paanakyma.setOpacity(0.0);
    }


    /**
     * Apua ikkunan avaaminen.
     */
    @FXML void handleApua(){
        Dialogs.showMessageDialog("Ei tartte auttaa");
    }
    
    
    /**
     * Tietoja näkymän avaaminen.
     */
    @FXML void handleTietoa(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/tietoja/TietojaView.fxml"));
        String otsikko = "Tietoja";
        avaaNakyma(fxmlLoader, otsikko, tietoja, false, true);
    }
    
    
    /**
     * Käsitellään lopetuskäsky.
     */
    @FXML private void handlePoistu() {
        Platform.exit();
    }
	
	
    /**
     * Lisää listaan työntekijän.
     * @param jnro työntekijän id, jota ollaan lisäämässä.
     */
    public void hae(int jnro) {
        chooserTyontekija.clear();
        int index = 0;
        for (int i = 0; i < palvelutalo.getTyontekija(); i++) {
            Tyontekija tyontekija = palvelutalo.annaTyontekija(i);
            if (tyontekija.getId() == jnro) index = i;
            chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
        }
        chooserTyontekija.setSelectedIndex(index);
    }
	
	
    /**
     * Näyttää valitun työntekijän tiedot näkymässä.
     */
	protected void naytaTyontekija() {
		Tyontekija valittuTyontekija = chooserTyontekija.getSelectedObject();
		nimiLabel.setText(valittuTyontekija.getNimi());
        hetuLabel.setText(valittuTyontekija.getHetu());
        tyonimikeLabel.setText(valittuTyontekija.getTyonimike());
        katuosoiteLabel.setText(valittuTyontekija.getKatuosoite());
        postinroLabel.setText(String.valueOf(valittuTyontekija.getPostinumero()));
        postitoimiLabel.setText(valittuTyontekija.getPostitoimipaikka());
        puhnroLabel.setText(valittuTyontekija.getPuhelinnumero());
        if(valittuTyontekija.getTyosopimus()==false){
        	tyosopimusLabel.setText("Ei");
        }
        else{
        	tyosopimusLabel.setText("On");
        }
        
        for ( int i = 0; i<lisakoulutusTable.getItems().size(); i++) {
            lisakoulutusTable.getItems().clear();
        }
        
        lisakoulutusColumn.setCellValueFactory(cellData -> cellData.getValue().getKoulutus());
        suorituspvmColumn.setCellValueFactory(cellData -> cellData.getValue().getSuoritusPvm());
        voimassaoloColumn.setCellValueFactory(cellData -> cellData.getValue().getVoimassaolo());
        lisakoulutusTable.setItems(haeKoulutusLista(valittuTyontekija.getId()));
	}
	

	/**
     * Haetaan jokaisen työntekijän henkilökohtaiset lisäkoulutukset.
	 * @param idTyontekija  työntekijän id
	 * @return työntekijän lisäkoulutukset taulukkoon
     */
    public ObservableList<Lisakoulutus> haeKoulutusLista(int idTyontekija) {
    	int j = palvelutalo.getLisakoulutus();
    	for(int i = 0 ; i<j; i++){
    		if(palvelutalo.annaLisakoulutus(i).getTyontekijanId() == idTyontekija){
    			koulutusData.add(palvelutalo.annaLisakoulutus(i));
    		}
    	}
    	return koulutusData;
	}
    
    
    /**
     * Muutetaan lisäkoulutuksien päivämäärä näkymä
     * @param localPaivamaara päivämäärä, jonka formaattia halutaan muokata.
     * @return päivämäärä String -tyyppisenä
     */
	public static String muutaPaivamaara(LocalDate localPaivamaara){
		java.util.Date paivamaara = java.sql.Date.valueOf(localPaivamaara);
    	SimpleDateFormat sdf=new SimpleDateFormat("dd.MM.yyyy");
        String strPaivamaara = sdf.format(paivamaara);
    	return strPaivamaara;
    }
	
	
	/**
	 * Haun käsittely.
	 */
	private void kasitteleHaku() {
		int maara = palvelutalo.getTyontekija();
		int lisaMaara = palvelutalo.getLisakoulutus();
		if(haunRajaus.length() == 0 && hakusana.length()==0){
			hae(id);
		}
		if(haunRajaus.length() == 0 && hakusana.length() != 0){
			Dialogs.showMessageDialog("Valitse hakuehto.");
		}
		if((haunRajaus.equals("Nimi")|| haunRajaus.equals("Työnimike") || haunRajaus.equals("Työsopimus"))&& hakusana.length()==0){
			Dialogs.showMessageDialog("Lisää hakusana.");
		}
		if(haunRajaus.equals("Nimi") && !hakusana.equals("")){
			chooserTyontekija.clear();
			kasitteleNimi(maara);
		}
		if(haunRajaus.equals("Työnimike") && !hakusana.equals("")){
			chooserTyontekija.clear();
			kasitteleTyonimike(maara);
		}
		if(haunRajaus.equals("Työsopimus") && !hakusana.equals("")){
			chooserTyontekija.clear();
			hakusana = hakusana.toLowerCase();
			if(hakusana.equals("on")){
				kasitteleTyosopimus(maara, true);
			}
			else if(hakusana.equals("ei")){
				kasitteleTyosopimus(maara, false);
			}
			else{
				Dialogs.showMessageDialog("Hakusana virheellinen.");
			}
			chooserTyontekija.setSelectedIndex(0);
		}
		if(haunRajaus.equals("EA1")){
			chooserTyontekija.clear();
			kasitteleLisakoulutukset(lisaMaara, "EA1");
		}
		if(haunRajaus.equals("EA2")){
			chooserTyontekija.clear();
			kasitteleLisakoulutukset(lisaMaara, "EA2");
		}
		if(haunRajaus.equals("LOVe")){
			chooserTyontekija.clear();
			kasitteleLisakoulutukset(lisaMaara, "LOVe");
		}
		if(haunRajaus.equals("Hygieniapassi")){
			chooserTyontekija.clear();
			kasitteleLisakoulutukset(lisaMaara, "Hygieniapassi");
		}
		if(haunRajaus.equals("Voimassaolo")){
			kasitteleVoimassaolo(lisaMaara);
		}
	}


	/**
	 * Käsitellään haku, jossa haetaan voimassaolon mukaan.
	 * @param maara kuinka monta työntekijää on rekisterissä.
	 */
	private void kasitteleVoimassaolo(int maara) {
		chooserTyontekija.clear();
		for(int i = 0; i< maara ; i++){
			String toistaiseksi = "toistaiseksi";
			hakusana = hakusana.toLowerCase();
			Lisakoulutus lisakoulutus = palvelutalo.annaLisakoulutus(i);
			String voimassa = lisakoulutus.getVoimassaolo().getValue();
			if(toistaiseksi.contains(hakusana)){
				toistaiseksi = "Toistaiseksi";
				if(toistaiseksi.equals(voimassa)){
					Tyontekija tyontekija = palvelutalo.annaTyontekijaIdnAvulla(lisakoulutus.getTyontekijanId());
					if(!chooserTyontekija.getItems().contains(tyontekija)){
						chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
					}
				}
			}
			else{
				String[] voimassaSplit = voimassa.split("\\.");
				int[] voim = {Integer.parseInt(voimassaSplit[0]), Integer.parseInt(voimassaSplit[1]), Integer.parseInt(voimassaSplit[2])};
				String[] haettu = hakusana.split("\\.");
				if(haettu.length == 3){
					if(haettu[2].length() != 4 || !haettu[2].matches("^-?\\d+$") || haettu[1].length() > 2 || !haettu[1].matches("^-?\\d+$") || haettu[0].length() > 2 || !haettu[0].matches("^-?\\d+$")){
						Dialogs.showMessageDialog("Virheellinen hakusana.");
						break;
					}
					int[] hae = {Integer.parseInt(haettu[2]), Integer.parseInt(haettu[1]), Integer.parseInt(haettu[0])};
					hakuehdot(hae, voim, lisakoulutus);
				}
				else if(haettu.length == 2){
					if(haettu[1].length() != 4 || !haettu[1].matches("^-?\\d+$") || haettu[0].length() > 2 || !haettu[0].matches("^-?\\d+$")){
						Dialogs.showMessageDialog("Virheellinen hakusana.");
						break;
					}
					int[] hae = {Integer.parseInt(haettu[1]), Integer.parseInt(haettu[0]), 31};
					hakuehdot(hae, voim, lisakoulutus);
				}
				else if(haettu.length == 1){
					if(haettu[0].length() != 4 || !haettu[0].matches("^-?\\d+$")){
						Dialogs.showMessageDialog("Virheellinen hakusana.");
						break;
					}
					int[] hae = {Integer.parseInt(haettu[0]), 12, 31};
					hakuehdot(hae, voim, lisakoulutus);
				}
				else{
					Dialogs.showMessageDialog("Virhe haettaessa.");
					break;
				}
			}
		}
		if(chooserTyontekija.getItems().isEmpty()){
			Dialogs.showMessageDialog("Haulla ei löytynyt työntekijöitä.");
		}
		chooserTyontekija.setSelectedIndex(0);
	}


	/**
	 * Tarkastellaan, onko hakuehtona annettu voimassaolo myöhemmin kuin työntekijän lisäkoulutuksen voimassaolo.
	 * @param hae hakuehtona annettu voimassaolo.
	 * @param voim työntekijän lisäkoulutuksen voimassaolo.
	 * @param lisakoulutus käsiteltävä lisäkoulutus.
	 */
	private void hakuehdot(int[] hae, int[] voim, Lisakoulutus lisakoulutus) {
		if(hae[0] > voim[2]){
			lisaaTyontekija(lisakoulutus);
		}
		if(hae[0] == voim[2] && hae[1] > voim[1]){
			lisaaTyontekija(lisakoulutus);
		}
		if(hae[0] == voim[2] && hae[1] == voim[1] && hae[2] >= voim[0]){
			lisaaTyontekija(lisakoulutus);
		}
	}


	/**
	 * Lisätään työntekijä näkymään listaukseen, jos sitä ei ole jo.
	 * @param lisakoulutus käsiteltävä lisäkoulutus.
	 */
	private void lisaaTyontekija(Lisakoulutus lisakoulutus) {
		Boolean onkoJo = false;
		Tyontekija tyontekija = palvelutalo.annaTyontekijaIdnAvulla(lisakoulutus.getTyontekijanId());
		if(chooserTyontekija.getItems().size() > 0){
			for(int j = 0; j<chooserTyontekija.getItems().size(); j++){
				if(chooserTyontekija.getItems().get(j).getObject().equals(tyontekija)){
					onkoJo = true;
				}
			}
			if(!onkoJo){
				chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
			}
		}
		else{
			chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
		}
	}


	/**
	 * Käsitellään haku, jossa haetaan jonkun lisäkoulutuksen mukaan.
	 * @param maara työntekijöiden määrä työntekijärekisterissä
	 * @param koulutus haussa käytetty rajaus.
	 */
	private void kasitteleLisakoulutukset(int maara, String koulutus) {
		for(int i = 0; i < maara ; i++){
			Lisakoulutus lisakoulutus = palvelutalo.annaLisakoulutus(i);
			if(lisakoulutus.getKoulutus().getValue().equals(koulutus)){
				Tyontekija tyontekija = palvelutalo.annaTyontekijaIdnAvulla(lisakoulutus.getTyontekijanId());
				chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
			}
		}
		chooserTyontekija.setSelectedIndex(0);
	}


	/**
	 * Käsitellään haku, jossa haetaan työnimikkeen mukaan.
	 * @param maara työntekijöiden määrä työntekijärekisterissä
	 */
	private void kasitteleTyonimike(int maara) {
		hakusana = hakusana.toLowerCase();
		for(int i = 0; i < maara ; i++){
			Boolean naytetaanko = true;
			Tyontekija tyontekija = palvelutalo.annaTyontekija(i);
			String tyonimike = tyontekija.getTyonimike();
			tyonimike = tyonimike.toLowerCase();
			for(int j = 0; j< hakusana.length(); j++){
				if(tyonimike.charAt(j) != hakusana.charAt(j)){
					naytetaanko = false;
				}
			}
			if(naytetaanko){
				chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
			}
		}
		if(chooserTyontekija.getItems().isEmpty()){
			Dialogs.showMessageDialog("Haulla ei löytynyt työntekijöitä.");
		}
		chooserTyontekija.setSelectedIndex(0);
	}


	/**
	 * Käsitellään haku, jossa haetaan nimen mukaan.
	 * @param maara työntekijöiden määrä työntekijärekisterissä
	 */
	private void kasitteleNimi(int maara) {
		hakusana = hakusana.toLowerCase();
		for(int i = 0; i < maara ; i++){
			Boolean naytetaanko = true;
			Tyontekija tyontekija = palvelutalo.annaTyontekija(i);
			String nimi = tyontekija.getNimi();
			nimi = nimi.toLowerCase();
			for(int j = 0; j< hakusana.length(); j++){
				if(nimi.charAt(j) != hakusana.charAt(j)){
					naytetaanko = false;
				}
			}
			if(naytetaanko){
				chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
			}
		}
		if(chooserTyontekija.getItems().isEmpty()){
			Dialogs.showMessageDialog("Haulla ei löytynyt työntekijöitä.");
		}
		chooserTyontekija.setSelectedIndex(0);
	}


	/**
	 * Käsitellään haku, jossa haetaan työsopimuksen mukaan.
	 * @param maara työntekijöiden määrä työntekijärekisterissä
	 * @param onkoTyosopimus onko vai ei voimassaolevaa työsopimusta.
	 */
	private void kasitteleTyosopimus(int maara, boolean onkoTyosopimus) {
		for(int i = 0; i<maara ; i++){
			Tyontekija tyontekija = palvelutalo.annaTyontekija(i);
			if(tyontekija.getTyosopimus() == onkoTyosopimus){
				chooserTyontekija.add(tyontekija.getNimi(), tyontekija);
			}
		}
	}
	
	
	/**
	 * Avataan haluttu näkymä.
	 * @param fxmlLoader käytettävä fxml
	 * @param otsikko näkymän otsikko
	 * @param stage näkymä, joka avataan
	 * @param tallennetaanko täytyykö tallentaa
	 * @param piilotaPaaikkuna piilotetaanko pääikkuna
	 */
	public void avaaNakyma(FXMLLoader fxmlLoader, String otsikko, Stage stage, Boolean tallennetaanko, Boolean piilotaPaaikkuna){
		try {
			Parent root1 = (Parent)fxmlLoader.load();
			stage.setScene(new Scene(root1));
			stage.setTitle(otsikko);
			if(tallennetaanko){
				stage.setOnCloseRequest((event) -> {
					event.consume();
					handleSulje(stage, false);
				});
			}
			stage.show();
			if(piilotaPaaikkuna){
				handlePiilota();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

    
    /**
     * Suljetaan ikkuna.
     * @param stage Suljettava näkymä
     * @param paaikkunanAvaus avataanko pääikkuna.
     */
    public void handleSulje(Stage stage, boolean paaikkunanAvaus){
		if(paaikkunanAvaus){
			handleAvaaPaaikkuna();
		}
		else{
			paanakyma.setOpacity(1.0);
		}
	    stage.close();
	}
    
    
    /**
     * Suljetaan lisäys-ikkuna.
     */
    public void handleSuljeJaLisaa() {
    	Stage stage = getLisaaminen();
	    paanakyma.setOpacity(1.0);
	    stage.close();
	}
    
    
    /**
     * Suljetaan muokkaus-ikkuna.
     * @param stage muokkaus-ikkuna
     */
    public void handleSuljeJaMuokkaa(Stage stage) {
    	paanakyma.setOpacity(1.0);
	    stage.close();
    }
    
    
    /**
     * Pääikkunan avaaminen, kun poistutaan muista näkymistä.
     */
	public void handleAvaaPaaikkuna(){
		try {
	        ldr = new FXMLLoader(getClass().getResource("PalvelutaloGUIView.fxml"));
	        root = (Parent)ldr.load();
	        ldr.setRoot(this);
	        ldr.setController(this);
	        final Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("palvelutalo.css").toExternalForm());
	        Stage stage = new Stage();
	        stage.setScene(scene);
	        stage.setTitle("Palvelutalo");
	        stage.show();
	        paanakyma = stage;
	    } catch(Exception e) {
	        e.printStackTrace();
		}
	}

     
     /**
      * Asetetaan staattiseksi lisaamis-ikkuna.
      * @return lisaaminen lisaamis-ikkunan näkymä
      */
     public static Stage getLisaaminen(){
    	 return lisaaminen;
     }
     
     
     /**
      * Asetetaan staattiseksi muokkaus-ikkuna.
      * @return muokkaaminen muokkaus-ikkunan näkymä
      */
     public static Stage getMuokkaaminen(){
    	 return muokkaaminen;
     }
     
     
     /**
      * Asetetaan staattiseksi tietoja-ikkuna
      * @return tietoja tietoja-ikkunan näkymä
      */
     public static Stage getTietoja(){
    	 return tietoja;
     }
     
     
     /**
      * Asetetaan staattiseksi tulostus-ikkuna
     * @return tulostusöikkunan näkymä.
     */
    public static Stage getTulostus(){
    	 return tulostus;
     }
}
