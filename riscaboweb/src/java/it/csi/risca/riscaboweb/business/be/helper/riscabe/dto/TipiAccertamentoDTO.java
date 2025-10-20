package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoAccertamentoDTO
 *
 * @author CSI PIEMONTE
 */

public class TipiAccertamentoDTO {

	@JsonProperty("id_tipo_accertamento")
    private Long idTipoAccertamento;

    @JsonProperty("cod_tipo_accertamento")
    private String codTipoAccertamento;
    
    @JsonProperty("des_tipo_accertamento")
    private String desTipoAccertamento;
	
    @JsonProperty("flg_automatico")
    private int flgAutomatico;

    @JsonProperty("flg_manuale")
    private int flgManuale ;
    
	public Long getIdTipoAccertamento() {
		return idTipoAccertamento;
	}

	public void setIdTipoAccertamento(Long idTipoAccertamento) {
		this.idTipoAccertamento = idTipoAccertamento;
	}

	public String getCodTipoAccertamento() {
		return codTipoAccertamento;
	}

	public void setCodTipoAccertamento(String codTipoAccertamento) {
		this.codTipoAccertamento = codTipoAccertamento;
	}

	public String getDesTipoAccertamento() {
		return desTipoAccertamento;
	}

	public void setDesTipoAccertamento(String desTipoAccertamento) {
		this.desTipoAccertamento = desTipoAccertamento;
	}

	public int getFlgAutomatico() {
		return flgAutomatico;
	}

	public void setFlgAutomatico(int flgAutomatico) {
		this.flgAutomatico = flgAutomatico;
	}

	public int getFlgManuale() {
		return flgManuale;
	}

	public void setFlgManuale(int flgManuale) {
		this.flgManuale = flgManuale;
	}


}
