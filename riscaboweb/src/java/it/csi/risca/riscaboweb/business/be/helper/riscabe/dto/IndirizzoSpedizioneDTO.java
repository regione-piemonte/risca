package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * IndirizzoSpedizioneDTO
 *
 * @author CSI PIEMONTE
 */

public class IndirizzoSpedizioneDTO {
	
	@JsonProperty("id_recapito")
    private Long idRecapito;
	
	@JsonProperty("id_recapito_postel")
    private Long idRecapitoPostel;
	
	@JsonProperty("id_gruppo_soggetto")
    private Long idGruppoSoggetto;
    
    @JsonProperty("destinatario_postel")
    private String destinatarioPostel;
    
    @JsonProperty("presso_postel")
    private String pressoPostel;
    
    @JsonProperty("indirizzo_postel")
    private String indirizzoPostel;
    
    @JsonProperty("citta_postel")
    private String cittaPostel;
    
    @JsonProperty("provincia_postel")
    private String provinciaPostel;
    
    @JsonProperty("cap_postel")
    private String capPostel;
    
    @JsonProperty("frazione_postel")
    private String frazionePostel;
    
    @JsonProperty("nazione_postel")
    private String nazionePostel;
    
    @JsonProperty("ind_valido_postel")
    private Long indValidoPostel;
  
	
	public Long getIdRecapito() {
		return idRecapito;
	}

	public void setIdRecapito(Long idRecapito) {
		this.idRecapito = idRecapito;
	}

	public Long getIdRecapitoPostel() {
		return idRecapitoPostel;
	}

	public void setIdRecapitoPostel(Long idRecapitoPostel) {
		this.idRecapitoPostel = idRecapitoPostel;
	}

	public Long getIdGruppoSoggetto() {
		return idGruppoSoggetto;
	}

	public void setIdGruppoSoggetto(Long idGruppoSoggetto) {
		this.idGruppoSoggetto = idGruppoSoggetto;
	}

	public String getDestinatarioPostel() {
		return destinatarioPostel;
	}

	public void setDestinatarioPostel(String destinatarioPostel) {
		this.destinatarioPostel = destinatarioPostel;
	}

	public String getPressoPostel() {
		return pressoPostel;
	}

	public void setPressoPostel(String pressoPostel) {
		this.pressoPostel = pressoPostel;
	}

	public String getIndirizzoPostel() {
		return indirizzoPostel;
	}

	public void setIndirizzoPostel(String indirizzoPostel) {
		this.indirizzoPostel = indirizzoPostel;
	}

	public String getCittaPostel() {
		return cittaPostel;
	}

	public void setCittaPostel(String cittaPostel) {
		this.cittaPostel = cittaPostel;
	}

	public String getProvinciaPostel() {
		return provinciaPostel;
	}

	public void setProvinciaPostel(String provinciaPostel) {
		this.provinciaPostel = provinciaPostel;
	}

	public String getCapPostel() {
		return capPostel;
	}

	public void setCapPostel(String capPostel) {
		this.capPostel = capPostel;
	}

	public String getFrazionePostel() {
		return frazionePostel;
	}

	public void setFrazionePostel(String frazionePostel) {
		this.frazionePostel = frazionePostel;
	}

	public String getNazionePostel() {
		return nazionePostel;
	}

	public void setNazionePostel(String nazionePostel) {
		this.nazionePostel = nazionePostel;
	}

	public Long getIndValidoPostel() {
		return indValidoPostel;
	}

	public void setIndValidoPostel(Long indValidoPostel) {
		this.indValidoPostel = indValidoPostel;
	}

}
