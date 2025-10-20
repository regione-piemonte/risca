package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NazioniDTO {
	
	@JsonProperty("id_nazione")
    private Long idNazione;

    @JsonProperty("cod_istat_nazione")
    private String codIstatNazione;

    @JsonProperty("cod_belfiore_nazione")
    private String codBelfioreNazione;
    
    @JsonProperty("denom_nazione")
    private String denomNazione;

	@JsonProperty("data_inizio_validita")
    private Date dataInizioValidita;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita;
    
    @JsonProperty("dt_id_stato")
    private Long dtIdStato;
    
    @JsonProperty("dt_id_stato_prev")
    private Long dtIdStatoPrev;
    
    @JsonProperty("dt_id_stato_next")
    private Long dtIdStatoNext;
    
    @JsonProperty("id_origine")
    private Long idOrigine;
    
	@JsonProperty("cod_iso2")
    private String codIso2;

	public Long getIdNazione() {
		return idNazione;
	}

	public void setIdNazione(Long idNazione) {
		this.idNazione = idNazione;
	}

	public String getCodIstatNazione() {
		return codIstatNazione;
	}

	public void setCodIstatNazione(String codIstatNazione) {
		this.codIstatNazione = codIstatNazione;
	}

	public String getCodBelfioreNazione() {
		return codBelfioreNazione;
	}

	public void setCodBelfioreNazione(String codBelfioreNazione) {
		this.codBelfioreNazione = codBelfioreNazione;
	}

	public String getDenomNazione() {
		return denomNazione;
	}

	public void setDenomNazione(String denomNazione) {
		this.denomNazione = denomNazione;
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

	public Long getDtIdStato() {
		return dtIdStato;
	}

	public void setDtIdStato(Long dtIdStato) {
		this.dtIdStato = dtIdStato;
	}

	public Long getDtIdStatoPrev() {
		return dtIdStatoPrev;
	}

	public void setDtIdStatoPrev(Long dtIdStatoPrev) {
		this.dtIdStatoPrev = dtIdStatoPrev;
	}

	public Long getDtIdStatoNext() {
		return dtIdStatoNext;
	}

	public void setDtIdStatoNext(Long dtIdStatoNext) {
		this.dtIdStatoNext = dtIdStatoNext;
	}

	public Long getIdOrigine() {
		return idOrigine;
	}

	public void setIdOrigine(Long idOrigine) {
		this.idOrigine = idOrigine;
	}
	
    public String getCodIso2() {
		return codIso2;
	}

	public void setCodIso2(String codIso2) {
		this.codIso2 = codIso2;
	}
}
