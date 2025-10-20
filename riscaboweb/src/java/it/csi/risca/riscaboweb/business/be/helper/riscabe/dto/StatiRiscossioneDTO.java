package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;


public class StatiRiscossioneDTO {

	@JsonProperty("id_stato_riscossione")
    private Long idStatoRiscossione;

    @JsonIgnore
    private Long idAmbito;

    @JsonProperty("cod_stato_riscossione")
    private String codStatoRiscossione;
    
    @JsonProperty("des_stato_riscossione")
    private String desStatoRiscossione;
    
	@JsonProperty("flg_default")
    private int flgDefault;

    @JsonProperty("ambito")
    private AmbitoDTO ambito;
    
    public Long getIdStatoRiscossione() {
		return idStatoRiscossione;
	}
    
	public Long getIdAmbito() {
		return idAmbito;
	}

	public void setIdAmbito(Long idAmbito) {
		this.idAmbito = idAmbito;
	}

	public String getCodStatoRiscossione() {
		return codStatoRiscossione;
	}

	public void setCodStatoRiscossione(String codStatoRiscossione) {
		this.codStatoRiscossione = codStatoRiscossione;
	}

	public String getDesStatoRiscossione() {
		return desStatoRiscossione;
	}

	public void setDesStatoRiscossione(String desStatoRiscossione) {
		this.desStatoRiscossione = desStatoRiscossione;
	}

	public void setIdStatoRiscossione(Long idStatoRiscossione) {
		this.idStatoRiscossione = idStatoRiscossione;
	}
	
    public int getFlgDefault() {
		return flgDefault;
	}

	public void setFlgDefault(int flgDefault) {
		this.flgDefault = flgDefault;
	}
	
	
    public AmbitoDTO getAmbito() {
		return ambito;
	}

	public void setAmbito(AmbitoDTO ambito) {
		this.ambito = ambito;
	}

	/**
     * @param o Object
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatiRiscossioneDTO that = (StatiRiscossioneDTO) o;
        return Objects.equals(idStatoRiscossione, that.idStatoRiscossione) && Objects.equals(idAmbito, that.idAmbito) && Objects.equals(codStatoRiscossione, that.codStatoRiscossione) && Objects.equals(desStatoRiscossione, that.desStatoRiscossione) && Objects.equals(flgDefault, that.flgDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStatoRiscossione, idAmbito, codStatoRiscossione, desStatoRiscossione, flgDefault);
    }

    /**
     * @return string
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatiRiscossioneDTO {");
        sb.append("         idStatoRiscossione:").append(idStatoRiscossione);
        sb.append(",        idAmbito:").append(idAmbito);
        sb.append(",        codStatoRiscossione:'").append(codStatoRiscossione).append("'");
        sb.append(",        desStatoRiscossione:'").append(desStatoRiscossione).append("'");
        sb.append(",        flgDefault:").append(flgDefault);
        sb.append("}");
        return sb.toString();
    }
}
