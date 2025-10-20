package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegioneExtendedDTO extends RegioneDTO {

	@JsonProperty("nazione")
    private NazioniDTO nazione;
	
	public NazioniDTO getNazione() {
		return nazione;
	}

	public void setNazione(NazioniDTO nazione) {
		this.nazione = nazione;
	}

	@Override
	public String toString() {
		return "RegioneExtendedDTO [nazione=" + nazione + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nazione == null) ? 0 : nazione.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegioneExtendedDTO other = (RegioneExtendedDTO) obj;
		if (nazione == null) {
			if (other.nazione != null)
				return false;
		} else if (!nazione.equals(other.nazione))
			return false;
		return true;
	}
}
