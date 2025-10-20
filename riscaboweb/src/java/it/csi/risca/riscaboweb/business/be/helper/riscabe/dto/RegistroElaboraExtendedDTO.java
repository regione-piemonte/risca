package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegistroElaboraExtendedDTO extends RegistroElaboraDTO {

	@JsonProperty("data_richiesta")
	private Date dataRichiesta;

	@JsonProperty("des_tipo_elabora")
	private String desTipoElabora;
	
	@JsonProperty("des_stato_elabora")
	private String desStatoElabora;

	@JsonProperty("des_passo_elabora")
	private String desPassoElabora;

	public Date getDataRichiesta() {
		return dataRichiesta;
	}

	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}

	public String getDesTipoElabora() {
		return desTipoElabora;
	}

	public String getDesStatoElabora() {
		return desStatoElabora;
	}

	public void setDesStatoElabora(String desStatoElabora) {
		this.desStatoElabora = desStatoElabora;
	}

	public void setDesTipoElabora(String desTipoElabora) {
		this.desTipoElabora = desTipoElabora;
	}

	public String getDesPassoElabora() {
		return desPassoElabora;
	}

	public void setDesPassoElabora(String desPassoElabora) {
		this.desPassoElabora = desPassoElabora;
	}

}
