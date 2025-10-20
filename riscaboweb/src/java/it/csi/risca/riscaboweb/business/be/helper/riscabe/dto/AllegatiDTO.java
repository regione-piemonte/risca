package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type Ambito dto.
 *
 * @author CSI PIEMONTE
 */

public class AllegatiDTO {

	@JsonProperty("id_classificazione")
    private String idClassificazione;
	
	@JsonProperty("descrizione")
    private String descrizione;
	
	@JsonProperty("doc_con_allegati")
    private boolean docConAllegati;
	
	@JsonProperty("rappresentazione_digitale")
    private boolean rappresentazioneDigitale;
    
    
	public String getIdClassificazione() {
		return idClassificazione;
	}
	public void setIdClassificazione(String idClassificazione) {
		this.idClassificazione = idClassificazione;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public boolean isDocConAllegati() {
		return docConAllegati;
	}
	public void setDocConAllegati(boolean docConAllegati) {
		this.docConAllegati = docConAllegati;
	}
	public boolean isRappresentazioneDigitale() {
		return rappresentazioneDigitale;
	}
	public void setRappresentazioneDigitale(boolean rappresentazioneDigitale) {
		this.rappresentazioneDigitale = rappresentazioneDigitale;
	}
    
    

}
