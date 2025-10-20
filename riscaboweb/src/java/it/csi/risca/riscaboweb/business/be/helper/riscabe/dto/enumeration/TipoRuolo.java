package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.enumeration;

public enum TipoRuolo {

	AMMINISTRATORE("AMMINISTRATORE"),

    GESTORE_BASE("GESTORE_BASE"),
	
	GESTORE_DATI("GESTORE_DATI"),
	
	CONSULTATORE("CONSULTATORE");

    private final String descrizione;

    TipoRuolo(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }
}
