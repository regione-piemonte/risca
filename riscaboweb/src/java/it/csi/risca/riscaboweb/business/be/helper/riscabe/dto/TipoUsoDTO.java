package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoUsoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipoUsoDTO {
	
	@JsonProperty("id_tipo_uso")
    private Long idTipoUso;
	
    @JsonProperty("id_ambito")
    private Long idAmbito;

    @JsonProperty("id_unita_misura")
    private Long idUnitaMisura;
    
    @JsonProperty("id_accerta_bilancio")
    private Long idAccertaBilancio;

    @JsonProperty("cod_tipo_uso")
    private String codTipouso;
    
    @JsonProperty("des_tipo_uso")
    private String desTipouso;
    
	@JsonProperty("id_tipo_uso_padre")
    private String idTipoUsoPadre;

	@JsonProperty("flg_uso_principale")
    private String flgUsoPrincipale;
	
	@JsonProperty("ordina_tipo_uso")
    private Long ordinaTipoUso;
	
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;
    
    @JsonProperty("flg_default")
    private int flgDefault;
    
	public Long getIdTipoUso() {
		return idTipoUso;
	}

	public void setIdTipoUso(Long idTipoUso) {
		this.idTipoUso = idTipoUso;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdUnitaMisura() {
		return idUnitaMisura;
	}

	public void setIdUnitaMisura(Long idUnitaMisura) {
		this.idUnitaMisura = idUnitaMisura;
	}

	public Long getIdAccertaBilancio() {
		return idAccertaBilancio;
	}

	public void setIdAccertaBilancio(Long idAccertaBilancio) {
		this.idAccertaBilancio = idAccertaBilancio;
	}

	public String getCodTipouso() {
		return codTipouso;
	}

	public void setCodTipouso(String codTipouso) {
		this.codTipouso = codTipouso;
	}

	public String getDesTipouso() {
		return desTipouso;
	}

	public void setDesTipouso(String desTipouso) {
		this.desTipouso = desTipouso;
	}

	public String getIdTipoUsoPadre() {
		return idTipoUsoPadre;
	}

	public void setIdTipoUsoPadre(String idTipoUsoPadre) {
		this.idTipoUsoPadre = idTipoUsoPadre;
	}

	public String getFlgUsoPrincipale() {
		return flgUsoPrincipale;
	}

	public void setFlgUsoPrincipale(String flgUsoPrincipale) {
		this.flgUsoPrincipale = flgUsoPrincipale;
	}

	public Long getOrdinaTipoUso() {
		return ordinaTipoUso;
	}

	public void setOrdinaTipoUso(Long ordinaTipoUso) {
		this.ordinaTipoUso = ordinaTipoUso;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Date getDataInizioValidita() {
		return dataInizioValidita;
	}

	public void setDataInizioValidita(Date dataInizioValidita) {
		this.dataInizioValidita = dataInizioValidita;
	}

	public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}
	

}
