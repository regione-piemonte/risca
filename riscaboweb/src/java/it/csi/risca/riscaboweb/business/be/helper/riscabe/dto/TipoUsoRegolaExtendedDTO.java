package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TipoUsoRegolaExtendedDTO extends TipoUsoRegolaDTO{
	
	@JsonProperty("tipo_uso")
    private TipoUsoExtendedDTO tipoUso; 
	
	@JsonProperty("json_regola_obj")
    private JsonRegolaDTO jsonRegolaDTO;

	public TipoUsoExtendedDTO getTipoUso() {
		return tipoUso;
	}

	public void setTipoUso(TipoUsoExtendedDTO tipoUso) {
		this.tipoUso = tipoUso;
	}

	public JsonRegolaDTO getJsonRegolaDTO() {
		return jsonRegolaDTO;
	}

	public void setJsonRegolaDTO(JsonRegolaDTO jsonRegolaDTO) {
		this.jsonRegolaDTO = jsonRegolaDTO;
	}


	
	
	
}
