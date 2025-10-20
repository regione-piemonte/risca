package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * SoggettiDTO
 *
 * @author CSI PIEMONTE
 */

public class SoggettiDTO {
	
	@JsonProperty("id_soggetto")
    private Long idSoggetto;
	
	@JsonProperty("id_ambito")
    private Long idAmbito;
	
	@JsonIgnore
    private Long idTipoSoggetto;

    @JsonProperty("cf_soggetto")
    private String cfSoggetto;
    
	@JsonIgnore	
    private Long idTipoNaturaGiuridica;
	
	@JsonProperty("id_fonte_origine")
    private Long idFonteOrigine;
	
	@JsonIgnore
    private Long idFonte;
    
    @JsonProperty("nome")
    private String nome;
    
    @JsonProperty("cognome")
    private String cognome;
    
    @JsonProperty("den_soggetto")
    private String denSoggetto;
    
    @JsonProperty("partita_iva_soggetto")
    private String partitaIvaSoggetto;
    
    @JsonProperty("data_nascita_soggetto")
    private String dataNascitaSoggetto;
    
    @JsonIgnore
    private Integer idComuneNascita;
    
    @JsonIgnore
    private Integer idStatoNascita;
    
    @JsonProperty("citta_estera_nascita")
    private String cittaEsteraNascita;
  
	public Long getIdSoggetto() {
		return idSoggetto;
	}

	public void setIdSoggetto(Long idSoggetto) {
		this.idSoggetto = idSoggetto;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdTipoSoggetto() {
		return idTipoSoggetto;
	}

	public void setIdTipoSoggetto(Long idTipoSoggetto) {
		this.idTipoSoggetto = idTipoSoggetto;
	}

	public String getCfSoggetto() {
		return cfSoggetto;
	}

	public void setCfSoggetto(String cfSoggetto) {
		this.cfSoggetto = cfSoggetto;
	}

	public Long getIdTipoNaturaGiuridica() {
		return idTipoNaturaGiuridica;
	}

	public void setIdTipoNaturaGiuridica(Long idTipoNaturaGiuridica) {
		this.idTipoNaturaGiuridica = idTipoNaturaGiuridica;
	}

	public Long getIdFonteOrigine() {
		return idFonteOrigine;
	}

	public void setIdFonteOrigine(Long idFonteOrigine) {
		this.idFonteOrigine = idFonteOrigine;
	}

	public Long getIdFonte() {
		return idFonte;
	}

	public void setIdFonte(Long idFonte) {
		this.idFonte = idFonte;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getDenSoggetto() {
		return denSoggetto;
	}

	public void setDenSoggetto(String denSoggetto) {
		this.denSoggetto = denSoggetto;
	}

	public String getPartitaIvaSoggetto() {
		return partitaIvaSoggetto;
	}

	public void setPartitaIvaSoggetto(String partitaIvaSoggetto) {
		this.partitaIvaSoggetto = partitaIvaSoggetto;
	}

	public String getDataNascitaSoggetto() {
		return dataNascitaSoggetto;
	}

	public void setDataNascitaSoggetto(String dataNascitaSoggetto) {
		this.dataNascitaSoggetto = dataNascitaSoggetto;
	}

	public Integer getIdComuneNascita() {
		return idComuneNascita;
	}

	public void setIdComuneNascita(Integer idComuneNascita) {
		this.idComuneNascita = idComuneNascita;
	}

	public Integer getIdStatoNascita() {
		return idStatoNascita;
	}

	public void setIdStatoNascita(Integer idStatoNascita) {
		this.idStatoNascita = idStatoNascita;
	}

	public String getCittaEsteraNascita() {
		return cittaEsteraNascita;
	}

	public void setCittaEsteraNascita(String cittaEsteraNascita) {
		this.cittaEsteraNascita = cittaEsteraNascita;
	}


}
