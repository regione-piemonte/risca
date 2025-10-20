package it.csi.risca.riscaboweb.business.be.helper.riscabe.dto;

public class DetailDTO {

	private SoggettiExtendedDTO soggetto;

	public DetailDTO() {
		super();
	}

	public DetailDTO(SoggettiExtendedDTO soggetto) {
		super();
		this.soggetto = soggetto;
	}

	public SoggettiExtendedDTO getSoggetto() {
		return soggetto;
	}

	public void setSoggetto(SoggettiExtendedDTO soggetto) {
		this.soggetto = soggetto;
	}
	
}
