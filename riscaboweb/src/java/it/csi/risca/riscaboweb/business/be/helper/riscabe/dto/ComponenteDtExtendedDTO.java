package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ComponenteDtExtendedDTO extends ComponenteDtDTO {
    
	@JsonProperty("tipo_componente_dt")
    private TipoComponenteDtDTO tipoComponenteDt;

	public TipoComponenteDtDTO getTipoComponenteDt() {
		return tipoComponenteDt;
	}

	public void setTipoComponenteDt(TipoComponenteDtDTO tipoComponenteDt) {
		this.tipoComponenteDt = tipoComponenteDt;
	}

}
