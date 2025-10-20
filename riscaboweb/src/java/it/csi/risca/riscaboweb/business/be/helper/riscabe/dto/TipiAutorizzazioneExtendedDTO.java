package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipiAutorizzazioneExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class TipiAutorizzazioneExtendedDTO extends TipiAutorizzazioneDTO {
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;
	
    /**
     * Gets ambito.
     *
     * @return ambito ambito
     */
    public AmbitoDTO getAmbito() {
		return ambito;
	}

    /**
     * Sets ambito.
     *
     * @param ambito ambito
     */
	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}


}
