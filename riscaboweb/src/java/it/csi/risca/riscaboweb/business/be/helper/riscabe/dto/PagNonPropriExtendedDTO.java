package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagNonPropriExtendedDTO extends PagNonPropriDTO {
	
	@JsonProperty("tipo_imp_non_propri")
    private TipoImpNonPropriDTO tipoImpNonPropri;

	public TipoImpNonPropriDTO getTipoImpNonPropri() {
		return tipoImpNonPropri;
	}

	public void setTipoImpNonPropri(TipoImpNonPropriDTO tipoImpNonPropri) {
		this.tipoImpNonPropri = tipoImpNonPropri;
	}
	
}
