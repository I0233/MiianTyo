package modelli;


/**
 * @author Miia Laurila
 * @version 6.4.2017
 *
 * Luokka työntekijän tietojen asettamiseksi.
 */	
public class Tyontekija {
 
	private int id;
	private String nimi;
	private String hetu;
	private String tyonimike;
	private String katuosoite;
	private int postinumero;
	private String postitoimipaikka;
	private String puhelinnumero;
	private Boolean tyosopimus;
	
	private static int tamanId = 1;
	
	/**
	 * Konstruktori
	 */
	public Tyontekija() {
	    // ei tarvitse alustaa
	}
	
	
	/**
	 * @return id työntekijän id.
	 * 
	 * Saadaan työntekijän id
	 */
	public int getId() {
		return id;
	}


	/**
	 * Asetetaan haluttu id
	 * @param i haluttu id
	 * @return id
	 */
	public int setId(int i){
		id = i;
		tamanId = i;
		tamanId++;
		return id;
	}

	
	/**
	 * Asetetaan työntekijälle id
	 * @return työntekijän id
	 */
	public int setId() {
		id = tamanId;
		tamanId++;
		return id;
	}
	
	
	/**
	 * Saadaan työntekijän nimi
	 * @return nimi
	 */
	public String getNimi() {
		return nimi;
	}
	
	
	/**
	 * Asetetaan työntekijälle nimi
	 * @param nimi työntekijän nimi
	 */
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	
	
	/**
	 * Saadaan työntekijän hetu
	 * @return hetu työntekijän hetu
	 */
	public String getHetu() {
		return hetu;
	}
	
	
	/**
	 * Asetetaan työntekijälle hetu
	 * @param hetu työntekijän hetu
	 */
	public void setHetu(String hetu) {
		this.hetu = hetu;
	}
	
	
	/**
	 * Saadaan työntekijän työnimike
	 * @return tyonimike työntekijän työnimike
	 */
	public String getTyonimike() {
		return tyonimike;
	}
	
	
	/**
	 * Asetetaan työntekijälle työnimike
	 * @param tyonimike työntekijän nimike
	 */
	public void setTyonimike(String tyonimike) {
		this.tyonimike = tyonimike;
	}
	
	
	/**
	 * Saadaan työntekijän katuosoite
	 * @return katuosoite työntekijän katuosoite
	 */
	public String getKatuosoite() {
		return katuosoite;
	}
	
	
	/**
	 * Asetetaan työntekijälle katuosoite
	 * @param katuosoite työntekijän katuosoite
	 */
	public void setKatuosoite(String katuosoite) {
		this.katuosoite = katuosoite;
	}
	
	
	/**
	 * Saadaan työntekijän postinumero
	 * @return postinumero työntekijän postinumero
	 */
	public int getPostinumero() {
		return postinumero;
	}
	
	
	/**
	 * Asetetaan työntekijälle postinumero
	 * @param postinumero työntekijän postinumero
	 */
	public void setPostinumero(int postinumero) {
		this.postinumero = postinumero;
	}
	
	
	/**
	 * Saadaan työntekijän postitoimipaikka.
	 * @return postitoimipaikka työntekijän postitoimipaikka
	 */
	public String getPostitoimipaikka() {
		return postitoimipaikka;
	}
	
	
	/**
	 * Asetetaan työntekijälle postitoimipaikka
	 * @param postitoimipaikka työntekijän postitoimipaikka
	 */
	public void setPostitoimipaikka(String postitoimipaikka) {
		this.postitoimipaikka = postitoimipaikka;
	}
	
	
	/**
	 * Saadaan työntekijän puhelinnumero
	 * @return puhelinnumero työntekijä puhelinnumero
	 */
	public String getPuhelinnumero() {
		return puhelinnumero;
	}
	
	
	/**
	 * Asetetaan työntekijälle puhelinnumero
	 * @param puhelinnumero työntekijän puhelinnumero
	 */
	public void setPuhelinnumero(String puhelinnumero) {
		this.puhelinnumero = puhelinnumero;
	}
	
	
	/**
	 * Saadaan työntekijän työsopimuksen voimassaolo (true/false)
	 * @return tyosopimus onko voimassa olevaa työsopimusta työntekijällä
	 */
	public Boolean getTyosopimus() {
		return tyosopimus;
	}
	
	
	/**
	 * Asetetaan työntekijän työsompimus voimassa olevaksi (true) tai ei (false)
	 * @param tyosopimus onko voimassa olevaa työsopimusta työntekijällä
	 */
	public void setTyosopimus(Boolean tyosopimus) {
		this.tyosopimus = tyosopimus;
	}


	/**
	 * Annetaan työntekijälle tiedot tiedostosta.
	 * @param rivi rivi, josta poimitaan työntekijän tiedot
	 */
	public void poimiTyontekijanTiedot(String rivi) {
		String[] splitattu = rivi.split("[|]");
		Boolean tyosopimuksenVoimassaolo = false;
		if(splitattu[8].equals("true")){
			tyosopimuksenVoimassaolo = true;
		}
		this.id = setId(Integer.parseInt(splitattu[0]));
		this.nimi = splitattu[1];
		this.hetu = splitattu[2];
		this.tyonimike = splitattu[3];
		this.katuosoite = splitattu[4];
		this.postinumero = Integer.parseInt(splitattu[5]);
		this.postitoimipaikka = splitattu[6];
		this.puhelinnumero = splitattu[7];
		this.tyosopimus = tyosopimuksenVoimassaolo;
	}


	/**
	 * Muodostetaan työntekijän tiedoista tulostettava rivi
	 * @return tulostettava rivi
	 */
	public String tulostuu() {
		String tulostus = this.id+"|"+this.nimi+"|"+this.hetu+"|"+this.tyonimike+"|"+this.katuosoite+"|"
				+this.postinumero+"|"+this.postitoimipaikka+"|"+this.puhelinnumero+"|"+this.tyosopimus+"|";
		return tulostus;
	}
}
