package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CalcoloCanoneSingoloDTO {
	
	@JsonProperty("importo_canone")
    private BigDecimal importoCanone;
	
	@JsonProperty("canone_usi")
    private List<CanoneUsoDTO> canoneUsi;
	
	@JsonProperty("json_regola_mancante")
    private boolean jsonRegolaMancante;
	
	@JsonProperty("num_mesi")
    private int numMesi;

	public BigDecimal getImportoCanone() {
		return importoCanone;
	}

	public void setImportoCanone(BigDecimal importoCanone) {
		this.importoCanone = importoCanone;
	}

	public List<CanoneUsoDTO> getCanoneUsi() {
		return canoneUsi;
	}

	public void setCanoneUsi(List<CanoneUsoDTO> canoneUsi) {
		this.canoneUsi = canoneUsi;
	}

	public boolean isJsonRegolaMancante() {
		return jsonRegolaMancante;
	}

	public void setJsonRegolaMancante(boolean jsonRegolaMancante) {
		this.jsonRegolaMancante = jsonRegolaMancante;
	}

	public int getNumMesi() {
		return numMesi;
	}

	public void setNumMesi(int numMesi) {
		this.numMesi = numMesi;
	}

}
