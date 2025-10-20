package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoComponenteDtDTO {

	@JsonProperty("id_tipo_componente_dt")
    private Long idTipoComponenteDt;

    @JsonProperty("cod_tipo_componente_dt")
    private String codTipoComponenteDt;

    @JsonProperty("des_tipo_componente_dt")
    private String desTipoComponenteDt;

	public Long getIdTipoComponenteDt() {
		return idTipoComponenteDt;
	}

	public void setIdTipoComponenteDt(Long idTipoComponenteDt) {
		this.idTipoComponenteDt = idTipoComponenteDt;
	}

	public String getCodTipoComponenteDt() {
		return codTipoComponenteDt;
	}

	public void setCodTipoComponenteDt(String codTipoComponenteDt) {
		this.codTipoComponenteDt = codTipoComponenteDt;
	}

	public String getDesTipoComponenteDt() {
		return desTipoComponenteDt;
	}

	public void setDesTipoComponenteDt(String desTipoComponenteDt) {
		this.desTipoComponenteDt = desTipoComponenteDt;
	}


}
