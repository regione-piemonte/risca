/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The type OutputDati dto.
 *
 * @author CSI PIEMONTE
 */
public class OutputDatiDTO extends GestAttoreDTO {
	
    @Min(value = 1, message = "L' id_elabora deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_elabora supera il limite massimo consentito per Integer")
	@JsonProperty("id_elabora")
	private Long idElabora;
    
	private Long idRiscossione;
	
    @Digits(integer = 6, fraction = 0, message = "progressivo deve avere al massimo 6 cifre intere")
	@JsonProperty("progressivo")
	private Integer progressivo;

    @Min(value = 1, message = "L' id_output_foglio deve essere maggiore di 0")
    @Max(value = Integer.MAX_VALUE, message = "L' id_output_foglio supera il limite massimo consentito per Integer")
	@JsonProperty("id_output_foglio")
	private Long idOutputFoglio;

    @Size(max = 20, min = 0, message = "invalid  cod_tipo_passo_elabora")
	@JsonProperty("cod_tipo_passo_elabora")
	private String codTipoPassoElabora;

    @Size(max = 500, min = 0, message = "invalid  valore_colonna1")
	@JsonProperty("valore_colonna1")
	private String valoreColonna1;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna2")
	@JsonProperty("valore_colonna2")
	private String valoreColonna2;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna3")
	@JsonProperty("valore_colonna3")
	private String valoreColonna3;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna4")
	@JsonProperty("valore_colonna4")
	private String valoreColonna4;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna5")
	@JsonProperty("valore_colonna5")
	private String valoreColonna5;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna6")
	@JsonProperty("valore_colonna6")
	private String valoreColonna6;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna7")
	@JsonProperty("valore_colonna7")
	private String valoreColonna7;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna8")
	@JsonProperty("valore_colonna8")
	private String valoreColonna8;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna9")
	@JsonProperty("valore_colonna9")
	private String valoreColonna9;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna10")
	@JsonProperty("valore_colonna10")
	private String valoreColonna10;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna11")
	@JsonProperty("valore_colonna11")
	private String valoreColonna11;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna12")
	@JsonProperty("valore_colonna12")
	private String valoreColonna12;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna13")
	@JsonProperty("valore_colonna13")
	private String valoreColonna13;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna14")
	@JsonProperty("valore_colonna14")
	private String valoreColonna14;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna15")
	@JsonProperty("valore_colonna15")
	private String valoreColonna15;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna16")
	@JsonProperty("valore_colonna16")
	private String valoreColonna16;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna17")
	@JsonProperty("valore_colonna17")
	private String valoreColonna17;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna18")
	@JsonProperty("valore_colonna18")
	private String valoreColonna18;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna19")
	@JsonProperty("valore_colonna19")
	private String valoreColonna19;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna20")
	@JsonProperty("valore_colonna20")
	private String valoreColonna20;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna21")
	@JsonProperty("valore_colonna21")
	private String valoreColonna21;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna22")
	@JsonProperty("valore_colonna22")
	private String valoreColonna22;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna23")
	@JsonProperty("valore_colonna23")
	private String valoreColonna23;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna24")
	@JsonProperty("valore_colonna24")
	private String valoreColonna24;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna25")
	@JsonProperty("valore_colonna25")
	private String valoreColonna25;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna26")
	@JsonProperty("valore_colonna26")
	private String valoreColonna26;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna27")
	@JsonProperty("valore_colonna27")
	private String valoreColonna27;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna28")
	@JsonProperty("valore_colonna28")
	private String valoreColonna28;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna29")
	@JsonProperty("valore_colonna29")
	private String valoreColonna29;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna30")
	@JsonProperty("valore_colonna30")
	private String valoreColonna30;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna31")
	@JsonProperty("valore_colonna31")
	private String valoreColonna31;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna32")
	@JsonProperty("valore_colonna32")
	private String valoreColonna32;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna33")
	@JsonProperty("valore_colonna33")
	private String valoreColonna33;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna34")
	@JsonProperty("valore_colonna34")
	private String valoreColonna34;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna35")
	@JsonProperty("valore_colonna35")
	private String valoreColonna35;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna36")
	@JsonProperty("valore_colonna36")
	private String valoreColonna36;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna37")
	@JsonProperty("valore_colonna37")
	private String valoreColonna37;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna38")
	@JsonProperty("valore_colonna38")
	private String valoreColonna38;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna39")
	@JsonProperty("valore_colonna39")
	private String valoreColonna39;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna40")
	@JsonProperty("valore_colonna40")
	private String valoreColonna40;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna41")
	@JsonProperty("valore_colonna41")
	private String valoreColonna41;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna42")
	@JsonProperty("valore_colonna42")
	private String valoreColonna42;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna43")
	@JsonProperty("valore_colonna43")
	private String valoreColonna43;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna44")
	@JsonProperty("valore_colonna44")
	private String valoreColonna44;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna45")
	@JsonProperty("valore_colonna45")
	private String valoreColonna45;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna46")
	@JsonProperty("valore_colonna46")
	private String valoreColonna46;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna47")
	@JsonProperty("valore_colonna47")
	private String valoreColonna47;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna48")
	@JsonProperty("valore_colonna48")
	private String valoreColonna48;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna49")
	@JsonProperty("valore_colonna49")
	private String valoreColonna49;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna50")
	@JsonProperty("valore_colonna50")
	private String valoreColonna50;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna51")
	@JsonProperty("valore_colonna51")
	private String valoreColonna51;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna52")
	@JsonProperty("valore_colonna52")
	private String valoreColonna52;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna53")
	@JsonProperty("valore_colonna53")
	private String valoreColonna53;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna54")
	@JsonProperty("valore_colonna54")
	private String valoreColonna54;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna55")
	@JsonProperty("valore_colonna55")
	private String valoreColonna55;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna56")
	@JsonProperty("valore_colonna56")
	private String valoreColonna56;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna57")
	@JsonProperty("valore_colonna57")
	private String valoreColonna57;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna58")
	@JsonProperty("valore_colonna58")
	private String valoreColonna58;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna59")
	@JsonProperty("valore_colonna59")
	private String valoreColonna59;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna60")
	@JsonProperty("valore_colonna60")
	private String valoreColonna60;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna61")
	@JsonProperty("valore_colonna61")
	private String valoreColonna61;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna62")
	@JsonProperty("valore_colonna62")
	private String valoreColonna62;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna63")
	@JsonProperty("valore_colonna63")
	private String valoreColonna63;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna64")
	@JsonProperty("valore_colonna64")
	private String valoreColonna64;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna65")
	@JsonProperty("valore_colonna65")
	private String valoreColonna65;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna66")
	@JsonProperty("valore_colonna66")
	private String valoreColonna66;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna67")
	@JsonProperty("valore_colonna67")
	private String valoreColonna67;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna68")
	@JsonProperty("valore_colonna68")
	private String valoreColonna68;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna69")
	@JsonProperty("valore_colonna69")
	private String valoreColonna69;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna70")
	@JsonProperty("valore_colonna70")
	private String valoreColonna70;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna71")
	@JsonProperty("valore_colonna71")
	private String valoreColonna71;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna72")
	@JsonProperty("valore_colonna72")
	private String valoreColonna72;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna73")
	@JsonProperty("valore_colonna73")
	private String valoreColonna73;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna74")
	@JsonProperty("valore_colonna74")
	private String valoreColonna74;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna75")
	@JsonProperty("valore_colonna75")
	private String valoreColonna75;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna76")
	@JsonProperty("valore_colonna76")
	private String valoreColonna76;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna77")
	@JsonProperty("valore_colonna77")
	private String valoreColonna77;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna78")
	@JsonProperty("valore_colonna78")
	private String valoreColonna78;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna79")
	@JsonProperty("valore_colonna79")
	private String valoreColonna79;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna80")
	@JsonProperty("valore_colonna80")
	private String valoreColonna80;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna81")
	@JsonProperty("valore_colonna81")
	private String valoreColonna81;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna82")
	@JsonProperty("valore_colonna82")
	private String valoreColonna82;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna83")
	@JsonProperty("valore_colonna83")
	private String valoreColonna83;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna84")
	@JsonProperty("valore_colonna84")
	private String valoreColonna84;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna85")
	@JsonProperty("valore_colonna85")
	private String valoreColonna85;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna86")
	@JsonProperty("valore_colonna86")
	private String valoreColonna86;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna87")
	@JsonProperty("valore_colonna87")
	private String valoreColonna87;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna88")
	@JsonProperty("valore_colonna88")
	private String valoreColonna88;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna89")
	@JsonProperty("valore_colonna89")
	private String valoreColonna89;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna90")
	@JsonProperty("valore_colonna90")
	private String valoreColonna90;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna91")
	@JsonProperty("valore_colonna91")
	private String valoreColonna91;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna92")
	@JsonProperty("valore_colonna92")
	private String valoreColonna92;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna93")
	@JsonProperty("valore_colonna93")
	private String valoreColonna93;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna94")
	@JsonProperty("valore_colonna94")
	private String valoreColonna94;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna95")
	@JsonProperty("valore_colonna95")
	private String valoreColonna95;
    @Size(max = 250, min = 0, message = "invalid  valore_colonna96")
	@JsonProperty("valore_colonna96")
	private String valoreColonna96;

    @Min(value = 0, message = " flg_invio_pec_email deve essere 0 o 1 ")
    @Max(value = 1, message = " flg_invio_pec_email deve essere 0 o 1 ")
	@JsonProperty("flg_invio_pec_email")
	private int flgInvioPecEmail;

	public Long getIdRiscossione() {
		return idRiscossione;
	}

	public void setIdRiscossione(Long idRiscossione) {
		this.idRiscossione = idRiscossione;
	}

	public Long getIdElabora() {
		return idElabora;
	}

	public void setIdElabora(Long idElabora) {
		this.idElabora = idElabora;
	}

	public Integer getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(Integer progressivo) {
		this.progressivo = progressivo;
	}

	public Long getIdOutputFoglio() {
		return idOutputFoglio;
	}

	public void setIdOutputFoglio(Long idOutputFoglio) {
		this.idOutputFoglio = idOutputFoglio;
	}

	public String getCodTipoPassoElabora() {
		return codTipoPassoElabora;
	}

	public void setCodTipoPassoElabora(String codTipoPassoElabora) {
		this.codTipoPassoElabora = codTipoPassoElabora;
	}

	public String getValoreColonna1() {
		return valoreColonna1;
	}

	public void setValoreColonna1(String valoreColonna1) {
		this.valoreColonna1 = valoreColonna1;
	}

	public String getValoreColonna2() {
		return valoreColonna2;
	}

	public void setValoreColonna2(String valoreColonna2) {
		this.valoreColonna2 = valoreColonna2;
	}

	public String getValoreColonna3() {
		return valoreColonna3;
	}

	public void setValoreColonna3(String valoreColonna3) {
		this.valoreColonna3 = valoreColonna3;
	}

	public String getValoreColonna4() {
		return valoreColonna4;
	}

	public void setValoreColonna4(String valoreColonna4) {
		this.valoreColonna4 = valoreColonna4;
	}

	public String getValoreColonna5() {
		return valoreColonna5;
	}

	public void setValoreColonna5(String valoreColonna5) {
		this.valoreColonna5 = valoreColonna5;
	}

	public String getValoreColonna6() {
		return valoreColonna6;
	}

	public void setValoreColonna6(String valoreColonna6) {
		this.valoreColonna6 = valoreColonna6;
	}

	public String getValoreColonna7() {
		return valoreColonna7;
	}

	public void setValoreColonna7(String valoreColonna7) {
		this.valoreColonna7 = valoreColonna7;
	}

	public String getValoreColonna8() {
		return valoreColonna8;
	}

	public void setValoreColonna8(String valoreColonna8) {
		this.valoreColonna8 = valoreColonna8;
	}

	public String getValoreColonna9() {
		return valoreColonna9;
	}

	public void setValoreColonna9(String valoreColonna9) {
		this.valoreColonna9 = valoreColonna9;
	}

	public String getValoreColonna10() {
		return valoreColonna10;
	}

	public void setValoreColonna10(String valoreColonna10) {
		this.valoreColonna10 = valoreColonna10;
	}

	public String getValoreColonna11() {
		return valoreColonna11;
	}

	public void setValoreColonna11(String valoreColonna11) {
		this.valoreColonna11 = valoreColonna11;
	}

	public String getValoreColonna12() {
		return valoreColonna12;
	}

	public void setValoreColonna12(String valoreColonna12) {
		this.valoreColonna12 = valoreColonna12;
	}

	public String getValoreColonna13() {
		return valoreColonna13;
	}

	public void setValoreColonna13(String valoreColonna13) {
		this.valoreColonna13 = valoreColonna13;
	}

	public String getValoreColonna14() {
		return valoreColonna14;
	}

	public void setValoreColonna14(String valoreColonna14) {
		this.valoreColonna14 = valoreColonna14;
	}

	public String getValoreColonna15() {
		return valoreColonna15;
	}

	public void setValoreColonna15(String valoreColonna15) {
		this.valoreColonna15 = valoreColonna15;
	}

	public String getValoreColonna16() {
		return valoreColonna16;
	}

	public void setValoreColonna16(String valoreColonna16) {
		this.valoreColonna16 = valoreColonna16;
	}

	public String getValoreColonna17() {
		return valoreColonna17;
	}

	public void setValoreColonna17(String valoreColonna17) {
		this.valoreColonna17 = valoreColonna17;
	}

	public String getValoreColonna18() {
		return valoreColonna18;
	}

	public void setValoreColonna18(String valoreColonna18) {
		this.valoreColonna18 = valoreColonna18;
	}

	public String getValoreColonna19() {
		return valoreColonna19;
	}

	public void setValoreColonna19(String valoreColonna19) {
		this.valoreColonna19 = valoreColonna19;
	}

	public String getValoreColonna20() {
		return valoreColonna20;
	}

	public void setValoreColonna20(String valoreColonna20) {
		this.valoreColonna20 = valoreColonna20;
	}

	public String getValoreColonna21() {
		return valoreColonna21;
	}

	public void setValoreColonna21(String valoreColonna21) {
		this.valoreColonna21 = valoreColonna21;
	}

	public String getValoreColonna22() {
		return valoreColonna22;
	}

	public void setValoreColonna22(String valoreColonna22) {
		this.valoreColonna22 = valoreColonna22;
	}

	public String getValoreColonna23() {
		return valoreColonna23;
	}

	public void setValoreColonna23(String valoreColonna23) {
		this.valoreColonna23 = valoreColonna23;
	}

	public String getValoreColonna24() {
		return valoreColonna24;
	}

	public void setValoreColonna24(String valoreColonna24) {
		this.valoreColonna24 = valoreColonna24;
	}

	public String getValoreColonna25() {
		return valoreColonna25;
	}

	public void setValoreColonna25(String valoreColonna25) {
		this.valoreColonna25 = valoreColonna25;
	}

	public String getValoreColonna26() {
		return valoreColonna26;
	}

	public void setValoreColonna26(String valoreColonna26) {
		this.valoreColonna26 = valoreColonna26;
	}

	public String getValoreColonna27() {
		return valoreColonna27;
	}

	public void setValoreColonna27(String valoreColonna27) {
		this.valoreColonna27 = valoreColonna27;
	}

	public String getValoreColonna28() {
		return valoreColonna28;
	}

	public void setValoreColonna28(String valoreColonna28) {
		this.valoreColonna28 = valoreColonna28;
	}

	public String getValoreColonna29() {
		return valoreColonna29;
	}

	public void setValoreColonna29(String valoreColonna29) {
		this.valoreColonna29 = valoreColonna29;
	}

	public String getValoreColonna30() {
		return valoreColonna30;
	}

	public void setValoreColonna30(String valoreColonna30) {
		this.valoreColonna30 = valoreColonna30;
	}

	public String getValoreColonna31() {
		return valoreColonna31;
	}

	public void setValoreColonna31(String valoreColonna31) {
		this.valoreColonna31 = valoreColonna31;
	}

	public String getValoreColonna32() {
		return valoreColonna32;
	}

	public void setValoreColonna32(String valoreColonna32) {
		this.valoreColonna32 = valoreColonna32;
	}

	public String getValoreColonna33() {
		return valoreColonna33;
	}

	public void setValoreColonna33(String valoreColonna33) {
		this.valoreColonna33 = valoreColonna33;
	}

	public String getValoreColonna34() {
		return valoreColonna34;
	}

	public void setValoreColonna34(String valoreColonna34) {
		this.valoreColonna34 = valoreColonna34;
	}

	public String getValoreColonna35() {
		return valoreColonna35;
	}

	public void setValoreColonna35(String valoreColonna35) {
		this.valoreColonna35 = valoreColonna35;
	}

	public String getValoreColonna36() {
		return valoreColonna36;
	}

	public void setValoreColonna36(String valoreColonna36) {
		this.valoreColonna36 = valoreColonna36;
	}

	public String getValoreColonna37() {
		return valoreColonna37;
	}

	public void setValoreColonna37(String valoreColonna37) {
		this.valoreColonna37 = valoreColonna37;
	}

	public String getValoreColonna38() {
		return valoreColonna38;
	}

	public void setValoreColonna38(String valoreColonna38) {
		this.valoreColonna38 = valoreColonna38;
	}

	public String getValoreColonna39() {
		return valoreColonna39;
	}

	public void setValoreColonna39(String valoreColonna39) {
		this.valoreColonna39 = valoreColonna39;
	}

	public String getValoreColonna40() {
		return valoreColonna40;
	}

	public void setValoreColonna40(String valoreColonna40) {
		this.valoreColonna40 = valoreColonna40;
	}

	public String getValoreColonna41() {
		return valoreColonna41;
	}

	public void setValoreColonna41(String valoreColonna41) {
		this.valoreColonna41 = valoreColonna41;
	}

	public String getValoreColonna42() {
		return valoreColonna42;
	}

	public void setValoreColonna42(String valoreColonna42) {
		this.valoreColonna42 = valoreColonna42;
	}

	public String getValoreColonna43() {
		return valoreColonna43;
	}

	public void setValoreColonna43(String valoreColonna43) {
		this.valoreColonna43 = valoreColonna43;
	}

	public String getValoreColonna44() {
		return valoreColonna44;
	}

	public void setValoreColonna44(String valoreColonna44) {
		this.valoreColonna44 = valoreColonna44;
	}

	public String getValoreColonna45() {
		return valoreColonna45;
	}

	public void setValoreColonna45(String valoreColonna45) {
		this.valoreColonna45 = valoreColonna45;
	}

	public String getValoreColonna46() {
		return valoreColonna46;
	}

	public void setValoreColonna46(String valoreColonna46) {
		this.valoreColonna46 = valoreColonna46;
	}

	public String getValoreColonna47() {
		return valoreColonna47;
	}

	public void setValoreColonna47(String valoreColonna47) {
		this.valoreColonna47 = valoreColonna47;
	}

	public String getValoreColonna48() {
		return valoreColonna48;
	}

	public void setValoreColonna48(String valoreColonna48) {
		this.valoreColonna48 = valoreColonna48;
	}

	public String getValoreColonna49() {
		return valoreColonna49;
	}

	public void setValoreColonna49(String valoreColonna49) {
		this.valoreColonna49 = valoreColonna49;
	}

	public String getValoreColonna50() {
		return valoreColonna50;
	}

	public void setValoreColonna50(String valoreColonna50) {
		this.valoreColonna50 = valoreColonna50;
	}

	public String getValoreColonna51() {
		return valoreColonna51;
	}

	public void setValoreColonna51(String valoreColonna51) {
		this.valoreColonna51 = valoreColonna51;
	}

	public String getValoreColonna52() {
		return valoreColonna52;
	}

	public void setValoreColonna52(String valoreColonna52) {
		this.valoreColonna52 = valoreColonna52;
	}

	public String getValoreColonna53() {
		return valoreColonna53;
	}

	public void setValoreColonna53(String valoreColonna53) {
		this.valoreColonna53 = valoreColonna53;
	}

	public String getValoreColonna54() {
		return valoreColonna54;
	}

	public void setValoreColonna54(String valoreColonna54) {
		this.valoreColonna54 = valoreColonna54;
	}

	public String getValoreColonna55() {
		return valoreColonna55;
	}

	public void setValoreColonna55(String valoreColonna55) {
		this.valoreColonna55 = valoreColonna55;
	}

	public String getValoreColonna56() {
		return valoreColonna56;
	}

	public void setValoreColonna56(String valoreColonna56) {
		this.valoreColonna56 = valoreColonna56;
	}

	public String getValoreColonna57() {
		return valoreColonna57;
	}

	public void setValoreColonna57(String valoreColonna57) {
		this.valoreColonna57 = valoreColonna57;
	}

	public String getValoreColonna58() {
		return valoreColonna58;
	}

	public void setValoreColonna58(String valoreColonna58) {
		this.valoreColonna58 = valoreColonna58;
	}

	public String getValoreColonna59() {
		return valoreColonna59;
	}

	public void setValoreColonna59(String valoreColonna59) {
		this.valoreColonna59 = valoreColonna59;
	}

	public String getValoreColonna60() {
		return valoreColonna60;
	}

	public void setValoreColonna60(String valoreColonna60) {
		this.valoreColonna60 = valoreColonna60;
	}

	public String getValoreColonna61() {
		return valoreColonna61;
	}

	public void setValoreColonna61(String valoreColonna61) {
		this.valoreColonna61 = valoreColonna61;
	}

	public String getValoreColonna62() {
		return valoreColonna62;
	}

	public void setValoreColonna62(String valoreColonna62) {
		this.valoreColonna62 = valoreColonna62;
	}

	public String getValoreColonna63() {
		return valoreColonna63;
	}

	public void setValoreColonna63(String valoreColonna63) {
		this.valoreColonna63 = valoreColonna63;
	}

	public String getValoreColonna64() {
		return valoreColonna64;
	}

	public void setValoreColonna64(String valoreColonna64) {
		this.valoreColonna64 = valoreColonna64;
	}

	public String getValoreColonna65() {
		return valoreColonna65;
	}

	public void setValoreColonna65(String valoreColonna65) {
		this.valoreColonna65 = valoreColonna65;
	}

	public String getValoreColonna66() {
		return valoreColonna66;
	}

	public void setValoreColonna66(String valoreColonna66) {
		this.valoreColonna66 = valoreColonna66;
	}

	public String getValoreColonna67() {
		return valoreColonna67;
	}

	public void setValoreColonna67(String valoreColonna67) {
		this.valoreColonna67 = valoreColonna67;
	}

	public String getValoreColonna68() {
		return valoreColonna68;
	}

	public void setValoreColonna68(String valoreColonna68) {
		this.valoreColonna68 = valoreColonna68;
	}

	public String getValoreColonna69() {
		return valoreColonna69;
	}

	public void setValoreColonna69(String valoreColonna69) {
		this.valoreColonna69 = valoreColonna69;
	}

	public String getValoreColonna70() {
		return valoreColonna70;
	}

	public void setValoreColonna70(String valoreColonna70) {
		this.valoreColonna70 = valoreColonna70;
	}

	public String getValoreColonna71() {
		return valoreColonna71;
	}

	public void setValoreColonna71(String valoreColonna71) {
		this.valoreColonna71 = valoreColonna71;
	}

	public String getValoreColonna72() {
		return valoreColonna72;
	}

	public void setValoreColonna72(String valoreColonna72) {
		this.valoreColonna72 = valoreColonna72;
	}

	public String getValoreColonna73() {
		return valoreColonna73;
	}

	public void setValoreColonna73(String valoreColonna73) {
		this.valoreColonna73 = valoreColonna73;
	}

	public String getValoreColonna74() {
		return valoreColonna74;
	}

	public void setValoreColonna74(String valoreColonna74) {
		this.valoreColonna74 = valoreColonna74;
	}

	public String getValoreColonna75() {
		return valoreColonna75;
	}

	public void setValoreColonna75(String valoreColonna75) {
		this.valoreColonna75 = valoreColonna75;
	}

	public String getValoreColonna76() {
		return valoreColonna76;
	}

	public void setValoreColonna76(String valoreColonna76) {
		this.valoreColonna76 = valoreColonna76;
	}

	public String getValoreColonna77() {
		return valoreColonna77;
	}

	public void setValoreColonna77(String valoreColonna77) {
		this.valoreColonna77 = valoreColonna77;
	}

	public String getValoreColonna78() {
		return valoreColonna78;
	}

	public void setValoreColonna78(String valoreColonna78) {
		this.valoreColonna78 = valoreColonna78;
	}

	public String getValoreColonna79() {
		return valoreColonna79;
	}

	public void setValoreColonna79(String valoreColonna79) {
		this.valoreColonna79 = valoreColonna79;
	}

	public String getValoreColonna80() {
		return valoreColonna80;
	}

	public void setValoreColonna80(String valoreColonna80) {
		this.valoreColonna80 = valoreColonna80;
	}

	public String getValoreColonna81() {
		return valoreColonna81;
	}

	public void setValoreColonna81(String valoreColonna81) {
		this.valoreColonna81 = valoreColonna81;
	}

	public String getValoreColonna82() {
		return valoreColonna82;
	}

	public void setValoreColonna82(String valoreColonna82) {
		this.valoreColonna82 = valoreColonna82;
	}

	public String getValoreColonna83() {
		return valoreColonna83;
	}

	public void setValoreColonna83(String valoreColonna83) {
		this.valoreColonna83 = valoreColonna83;
	}

	public String getValoreColonna84() {
		return valoreColonna84;
	}

	public void setValoreColonna84(String valoreColonna84) {
		this.valoreColonna84 = valoreColonna84;
	}

	public String getValoreColonna85() {
		return valoreColonna85;
	}

	public void setValoreColonna85(String valoreColonna85) {
		this.valoreColonna85 = valoreColonna85;
	}

	public String getValoreColonna86() {
		return valoreColonna86;
	}

	public void setValoreColonna86(String valoreColonna86) {
		this.valoreColonna86 = valoreColonna86;
	}

	public String getValoreColonna87() {
		return valoreColonna87;
	}

	public void setValoreColonna87(String valoreColonna87) {
		this.valoreColonna87 = valoreColonna87;
	}

	public String getValoreColonna88() {
		return valoreColonna88;
	}

	public void setValoreColonna88(String valoreColonna88) {
		this.valoreColonna88 = valoreColonna88;
	}

	public String getValoreColonna89() {
		return valoreColonna89;
	}

	public void setValoreColonna89(String valoreColonna89) {
		this.valoreColonna89 = valoreColonna89;
	}

	public String getValoreColonna90() {
		return valoreColonna90;
	}

	public void setValoreColonna90(String valoreColonna90) {
		this.valoreColonna90 = valoreColonna90;
	}

	public String getValoreColonna91() {
		return valoreColonna91;
	}

	public void setValoreColonna91(String valoreColonna91) {
		this.valoreColonna91 = valoreColonna91;
	}

	public String getValoreColonna92() {
		return valoreColonna92;
	}

	public void setValoreColonna92(String valoreColonna92) {
		this.valoreColonna92 = valoreColonna92;
	}

	public String getValoreColonna93() {
		return valoreColonna93;
	}

	public void setValoreColonna93(String valoreColonna93) {
		this.valoreColonna93 = valoreColonna93;
	}

	public String getValoreColonna94() {
		return valoreColonna94;
	}

	public void setValoreColonna94(String valoreColonna94) {
		this.valoreColonna94 = valoreColonna94;
	}

	public String getValoreColonna95() {
		return valoreColonna95;
	}

	public void setValoreColonna95(String valoreColonna95) {
		this.valoreColonna95 = valoreColonna95;
	}

	public String getValoreColonna96() {
		return valoreColonna96;
	}

	public void setValoreColonna96(String valoreColonna96) {
		this.valoreColonna96 = valoreColonna96;
	}

	public int getFlgInvioPecEmail() {
		return flgInvioPecEmail;
	}

	public void setFlgInvioPecEmail(int flgInvioPecEmail) {
		this.flgInvioPecEmail = flgInvioPecEmail;
	}

}