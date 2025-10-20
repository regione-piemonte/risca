package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegioneDTO {
	
	@JsonProperty("id_regione")
    private Long idRegione;

    @JsonProperty("cod_regione")
    private String codRegione;

    @JsonProperty("denom_regione")
    private String denomRegione;
    
    @JsonProperty("id_nazione")
    private Long idNazione;

	@JsonProperty("data_inizio_validita")
    private Date dataInizioValidita;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita;

	@JsonProperty("nazione")
    private NazioniDTO nazione;
    
	public Long getIdRegione() {
		return idRegione;
	}

	public void setIdRegione(Long idRegione) {
		this.idRegione = idRegione;
	}

	public String getCodRegione() {
		return codRegione;
	}

	public void setCodRegione(String codRegione) {
		this.codRegione = codRegione;
	}

	public String getDenomRegione() {
		return denomRegione;
	}

	public void setDenomRegione(String denomRegione) {
		this.denomRegione = denomRegione;
	}

	public Long getIdNazione() {
		return idNazione;
	}

	public void setIdNazione(Long idNazione) {
		this.idNazione = idNazione;
	}

    public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}
	   
    public NazioniDTO getNazione() {
		return nazione;
	}

	public void setNazione(NazioniDTO nazione) {
		this.nazione = nazione;
	}


}
