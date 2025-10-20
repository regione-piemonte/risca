package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.tributi;

import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * DatiTecniciAmbienteDTO
 *
 * @author CSI PIEMONTE
 */
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DatiTecniciTributiDTO {


	
	@JsonProperty("usi")
    private Map<String, TipoUsoDatoTecnicoTributiDTO> usi;


	public Map<String, TipoUsoDatoTecnicoTributiDTO> getUsi() {
		return usi;
	}

	public void setUsi(Map<String, TipoUsoDatoTecnicoTributiDTO> usi) {
		this.usi = usi;
	}

	
	@JsonIgnore
	public String toJsonString() {
	    ObjectMapper mapper = new ObjectMapper();
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    try {
	        return ow.writeValueAsString(this);
	    } catch (JsonProcessingException e) {
	        return null;
	    }
	}
	/**
	 * To json obj json object.
	 *
	 * @return JSONObject json object
	 */
	@JsonIgnore
	public JSONObject toJsonObj() {
	    String sJson = this.toJsonString();
	    JSONObject obj = new JSONObject(sJson);

	    return obj;
	}
	
	
}
