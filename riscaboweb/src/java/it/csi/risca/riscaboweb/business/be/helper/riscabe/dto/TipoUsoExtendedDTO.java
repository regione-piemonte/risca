package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TipoUsoExtendedDTO
 *
 * @author CSI PIEMONTE
 */
public class TipoUsoExtendedDTO extends TipoUsoDTO {
	
	@JsonProperty("ambito")
    private AmbitoDTO ambito;

	@JsonProperty("unita_misura")
    private UnitaMisuraDTO unitaMisura;
	
	@JsonProperty("accertamento_bilancio")
    private AccertamentoBilancioDTO accertamentoBilancio;
	
    /**
     * Gets ambito.
     *
     * @return AmbitoDTO ambito
     */
    public AmbitoDTO getAmbito() {
		return ambito;
	}

    /**
     * Sets ambito.
     *
     * @param AmbitoDTO ambito
     */
	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}
	
    /**
     * Gets unita_misura.
     *
     * @return UnitaMisuraDTO unita_misura
     */
	public UnitaMisuraDTO getUnitaMisura() {
		return unitaMisura;
	}

    /**
     * Sets unita_misura.
     *
     * @param UnitaMisuraDTO unita_misura
     */
	public void setUnitaMisura(UnitaMisuraDTO unitaMisura) {
		this.unitaMisura = unitaMisura;
	}

    /**
     * Gets ambito.
     *
     * @return ambito ambito
     */
	public AccertamentoBilancioDTO getAccertamentoBilancio() {
		return accertamentoBilancio;
	}

    /**
     * Sets accertamento_bilancio.
     *
     * @param AccertamentoBilancioDTO accertamento_bilancio
     */
	public void setAccertamentoBilancio(AccertamentoBilancioDTO accertamentoBilancio) {
		this.accertamentoBilancio = accertamentoBilancio;
	}

}
