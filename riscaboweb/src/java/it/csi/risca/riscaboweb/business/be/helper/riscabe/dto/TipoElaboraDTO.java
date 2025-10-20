package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * The type Tipo Elabora  dto.
 *
 * @author CSI PIEMONTE
 */
public class TipoElaboraDTO {
	
	@JsonProperty("id_tipo_elabora")
    private Long idTipoElabora;
	
	@JsonProperty("id_ambito")
    private Long idAmbito;
	
	@JsonProperty("id_funzionalita")
    private Long idFunzionalita;
	
    @JsonProperty("cod_tipo_elabora")
    private String codTipoElabora;

    @JsonProperty("des_tipo_elabora")
    private String desTipoElabora;
    
    @JsonProperty("ordina_tipo_elabora")
    private Long ordina_tipo_elabora;
    
    @JsonProperty("flg_default")
    private Integer flgDefault; 
    
    @JsonProperty("flg_visibile")
    private Integer flgVisibile; 
    
    @JsonProperty("data_fine_validita")
    private Date dataFineValidita ;

    @JsonProperty("data_inizio_validita")
    private Date dataInizioValidita ;

	public Long getIdTipoElabora() {
		return idTipoElabora;
	}

	public void setIdTipoElabora(Long idTipoElabora) {
		this.idTipoElabora = idTipoElabora;
	}

	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public Long getIdFunzionalita() {
		return idFunzionalita;
	}

	public void setIdFunzionalita(Long idFunzionalita) {
		this.idFunzionalita = idFunzionalita;
	}

	public String getCodTipoElabora() {
		return codTipoElabora;
	}

	public void setCodTipoElabora(String codTipoElabora) {
		this.codTipoElabora = codTipoElabora;
	}

	public String getDesTipoElabora() {
		return desTipoElabora;
	}

	public void setDesTipoElabora(String desTipoElabora) {
		this.desTipoElabora = desTipoElabora;
	}

	public Long getOrdina_tipo_elabora() {
		return ordina_tipo_elabora;
	}

	public void setOrdina_tipo_elabora(Long ordina_tipo_elabora) {
		this.ordina_tipo_elabora = ordina_tipo_elabora;
	}

	public Integer getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(Integer flgDefault) {
		this.flgDefault = flgDefault;
	}

	public Integer getFlgVisibile() {
		return flgVisibile;
	}

	public void setFlgVisibile(Integer flgVisibile) {
		this.flgVisibile = flgVisibile;
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
    
    
    
    
}
