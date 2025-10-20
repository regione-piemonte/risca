/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscabesrv.dto.ambiente.TipoUsoDatoTecnicoDTO;

public class BollUtils {

	public static final String COD_TIPO_USO_PROD_BENI_SERVIZI = "PROD_BENI_SERVIZI";
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO = "GRANDE_IDROELETTRICO";
	public static final String COD_TIPO_USO_GRANDE_IDROELETTRICO_AGG = "GRANDE_IDRO_AGG";
	public static final String COD_DATO_TECNICO_CORPO_IDRICO = "corpo_idrico_captazione";

	public static final String COD_TIPO_PROVVEDIMENTO_IST_RINNOVO = "IST_RINNOVO";
	public static final String COD_TIPO_PROVVEDIMENTO_IST_SANATORIA = "IST_SANATORIA";
	public static final String COD_TIPO_PROVVEDIMENTO_AUT_PROVVISORIA = "AUT_PROVVISORIA";

	public static final String COD_TIPO_SPEDIZIONE_ACCERTA = "ACCERTA";
	public static final String COD_TIPO_SPEDIZIONE_ORDINARIA = "ORDINARIA";
	public static final String COD_TIPO_SPEDIZIONE_SPECIALE = "SPECIALE";
	public static final String COD_TIPO_SPEDIZIONE_GRANDEIDRO = "GRANDEIDRO";
	public static final String COD_TIPO_ACCERTAMENTO_AVB = "AVB";
	public static final String COD_TIPO_ACCERTAMENTO_SOP = "SOP";

	public static final String COD_TIPO_USO_MONET_ENERGIA_GRAT = "MONET_ENERGIA_GRAT";
	public static final String COD_TIPO_USO_GRANDE_IDRO_VAR = "GRANDE_IDRO_VAR";
	
	public static final String COD_TIPO_ELABORA_BS = "BS";
	public static final String COD_TIPO_ELABORA_BG = "BG";
	public static final String COD_TIPO_ELABORA_BO = "BO";
	
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BS = 56l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BG = 75l;
	private static final Long ID_OUTPUT_FOGLIO_DATI_TITOLARE_BO = 40l;
	
	public static final String ATTORE_BS = "riscabatchspec";
	public static final String ATTORE_BO = "riscabatchord";
	
	public static DatiTecniciAmbienteDTO getDatiTecniciFromJsonDt(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		final JSONObject obj = new JSONObject(json);
		final JSONObject geodata = obj.getJSONObject("riscossione");
		String datiTecnici = "";
		for (String key : obj.getJSONObject("riscossione").keySet()) {
			if (key.equals("dati_tecnici"))
				datiTecnici = geodata.getJSONObject("dati_tecnici").toString();
		}
		DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici, DatiTecniciAmbienteDTO.class);
		return datiTecniciAmbiente;
	}

	public static DatiTecniciAmbienteDTO getDatiTecniciFromJsonDtUsi(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		final JSONObject datiTecnici = new JSONObject(json);
		DatiTecniciAmbienteDTO datiTecniciAmbiente = mapper.readValue(datiTecnici.toString(),
				DatiTecniciAmbienteDTO.class);
		return datiTecniciAmbiente;
	}

	public static String estraiDataScadenzaEmasIso(String json) {
		String dataScadenzaEmasIso = "";
		try {
			DatiTecniciAmbienteDTO datiTecnici = getDatiTecniciFromJsonDt(json);
			if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_PROD_BENI_SERVIZI)) {
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_PROD_BENI_SERVIZI);
				dataScadenzaEmasIso = uso.getDataScadenzaEmasIso();
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return dataScadenzaEmasIso;
	}

	public static String estraiCorpoIdrico(String json) {
		String corpoIdrico = "";
		try {
			DatiTecniciAmbienteDTO datiTecnici = getDatiTecniciFromJsonDt(json);
			corpoIdrico = datiTecnici.getDatiGenerali().getCorpoIdricoCaptazione();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return corpoIdrico;
	}

	public static String estraiComune(String json) {
		String comune = "";
		try {
			DatiTecniciAmbienteDTO datiTecnici = getDatiTecniciFromJsonDt(json);
			comune = datiTecnici.getDatiGenerali().getComune();
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return comune;
	}

	public static BigDecimal estraiQuantita(String json, Long idTipoUso) throws BusinessException {
		BigDecimal qtaAcqua = null;
		try {
			DatiTecniciAmbienteDTO datiTecniciAmbiente = getDatiTecniciFromJsonDt(json);
			Map<String, TipoUsoDatoTecnicoDTO> usi = datiTecniciAmbiente.getUsi();
			for (TipoUsoDatoTecnicoDTO tipoUso : usi.values()) {
				if (tipoUso.getIdTipoUsoLegge().equals(idTipoUso)) {
					qtaAcqua = tipoUso.getQuantita();
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return qtaAcqua;
	}

	public static BigDecimal estraiQuantitaDaUsi(String json, Long idTipoUso) throws BusinessException {
		BigDecimal qtaAcqua = null;
		try {
			DatiTecniciAmbienteDTO datiTecniciAmbiente = getDatiTecniciFromJsonDtUsi(json);
			Map<String, TipoUsoDatoTecnicoDTO> usi = datiTecniciAmbiente.getUsi();
			for (TipoUsoDatoTecnicoDTO tipoUso : usi.values()) {
				if (tipoUso.getIdTipoUsoLegge().equals(idTipoUso)) {
					qtaAcqua = tipoUso.getQuantita();
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return qtaAcqua;
	}

	public static BigDecimal estraiPercFaldaProfondaDaUsi(String json, Long idTipoUso) {
		BigDecimal percFaldaProf = null;
		try {
			DatiTecniciAmbienteDTO datiTecniciAmbiente = getDatiTecniciFromJsonDtUsi(json);
			Map<String, TipoUsoDatoTecnicoDTO> usi = datiTecniciAmbiente.getUsi();
			for (TipoUsoDatoTecnicoDTO tipoUso : usi.values()) {
				if (tipoUso.getIdTipoUsoLegge() == idTipoUso) {
					percFaldaProf = tipoUso.getPercFaldaProfonda();
					if (percFaldaProf.equals(BigDecimal.valueOf(-1l))) {
						percFaldaProf = BigDecimal.ZERO;
					}
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return percFaldaProf;
	}

	public static Integer estraiPercQuotaVar(String json) {
		Integer percQuotaVar = null;
		try {
			DatiTecniciAmbienteDTO datiTecnici = getDatiTecniciFromJsonDtUsi(json);
			if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_GRANDE_IDRO_VAR)) {
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_GRANDE_IDRO_VAR);
				percQuotaVar = uso.getPercQuotaVar() != null ? uso.getPercQuotaVar() : null;
			} else if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_MONET_ENERGIA_GRAT)) {
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_MONET_ENERGIA_GRAT);
				percQuotaVar = uso.getPercQuotaVar() != null ? uso.getPercQuotaVar() : null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return percQuotaVar;
	}

	public static BigDecimal estraiPrezzoMedOraPond(String json) {
		BigDecimal prezzoMedOraPond = null;
		try {
			DatiTecniciAmbienteDTO datiTecnici = getDatiTecniciFromJsonDtUsi(json);
			if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_GRANDE_IDRO_VAR)) {
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_GRANDE_IDRO_VAR);
				prezzoMedOraPond = uso.getPrezzoMedOraPond() != null ? uso.getPrezzoMedOraPond() : null;
			} else if (datiTecnici.getUsi().containsKey(COD_TIPO_USO_MONET_ENERGIA_GRAT)) {
				TipoUsoDatoTecnicoDTO uso = datiTecnici.getUsi().get(COD_TIPO_USO_MONET_ENERGIA_GRAT);
				prezzoMedOraPond = uso.getPrezzoMedOraPond() != null ? uso.getPrezzoMedOraPond() : null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prezzoMedOraPond;
	}

	public static double round(double value, int places) {
		// double scale = Math.pow(10, places);
		// return Math.round(value * scale) / scale;
		BigDecimal bd = new BigDecimal(Double.toString(value));
		return bd.setScale(places, RoundingMode.HALF_UP).doubleValue();
	}

	public static BigDecimal getCanoneUnitarioFromRegola(String jsonRegola) {
		final JSONObject obj = new JSONObject(jsonRegola);
		String canoneUnitario = obj.get("canone_unitario").toString();
		return new BigDecimal(canoneUnitario);
	}

	public static Long getIdFoglioTitolare(String tipoElab) {
		switch (tipoElab) {
		case COD_TIPO_ELABORA_BS:
			return ID_OUTPUT_FOGLIO_DATI_TITOLARE_BS;
		case COD_TIPO_ELABORA_BG:
			return ID_OUTPUT_FOGLIO_DATI_TITOLARE_BG;
		case COD_TIPO_ELABORA_BO:
			return ID_OUTPUT_FOGLIO_DATI_TITOLARE_BO;
		default:
			return null;
		}
	}
	
	public static String getAttore(String tipoElab) {
		switch (tipoElab) {
		case COD_TIPO_ELABORA_BS:
			return ATTORE_BS;
		case COD_TIPO_ELABORA_BG:
			return ATTORE_BS;
		case COD_TIPO_ELABORA_BO:
			return ATTORE_BO;
		default:
			return null;
		}
	}
}
