package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProvinciaDTO {
	
	@JsonProperty("id_provincia")
    private Long idProvincia;

    @JsonProperty("cod_provincia")
    private String codProvincia;

    @JsonProperty("denom_provincia")
    private String denomProvincia;
    
    @JsonProperty("sigla_provincia")
    private String siglaProvincia;
    
    @JsonProperty("id_regione")
    private Long idRegione;

	@JsonProperty("data_inizio_validita")
    private Date dataInizioValidita;
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita;
	
	public Long getIdProvincia() {
		return idProvincia;
	}

	public void setIdProvincia(Long idProvincia) {
		this.idProvincia = idProvincia;
	}

	public String getCodProvincia() {
		return codProvincia;
	}

	public void setCodProvincia(String codProvincia) {
		this.codProvincia = codProvincia;
	}

	public String getDenomProvincia() {
		return denomProvincia;
	}

	public void setDenomProvincia(String denomProvincia) {
		this.denomProvincia = denomProvincia;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public void setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
	}

	public Long getIdRegione() {
		return idRegione;
	}

	public void setIdRegione(Long idRegione) {
		this.idRegione = idRegione;
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

}
