/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;

import fr.opensagres.xdocreport.core.XDocReportException;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.FileDownloadDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.GruppiSoggettoDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.IndirizzoSpedizioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ProvvedimentoDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RataSdDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RecapitiExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.SoggettiExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StampaRiscossioneDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoRiscossioneExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoUsoExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.DatiTecniciAmbienteDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ambiente.TipoUsoDatoTecnicoDTO;
import it.csi.risca.riscaboweb.util.TemplateUtil;

public class StampaRiscossioneApiServiceHelper  extends AbstractServiceHelper {

    @Autowired
    private RiscossioneApiServiceHelper riscossioneApiServiceHelper;
    

    @Autowired
    private SoggettiApiServiceHelper soggettiApiServiceHelper;
    
    @Autowired
    private StatoDebitorioApiServiceHelper statoDebitorioApiServiceHelper;
    
    @Autowired
    private TipiRiscossioneApiServiceHelper tipiRiscossioneApiServiceHelper;
    
	@Autowired
	private TipoUsoApiServiceHelper tipoUsoApiServiceHelper;
    
    private static final String SORT_BY_DT_SCADENZA = "-dataScadenza";
	
	private static final String  FILE_NAME ="Sintesi Pratica";
	
	private static final String  PATH_TEMPLATE ="/MOD_SINTESI_PRATICA_V1.docx";
    
    public StampaRiscossioneApiServiceHelper(String hostname, String endpointBase) {
        this.hostname = hostname;
        this.endpointBase = hostname + endpointBase;
    }


	public FileDownloadDTO stampaRiscossione(Integer idRiscossione,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] BEGIN");
		try {
			
			LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] getRiscossione");
			RiscossioneDTO riscossioneDTO = riscossioneApiServiceHelper.getRiscossione(String.valueOf(idRiscossione), null,  httpHeaders, httpRequest);
			LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] loadDatoTecnico");
            String datoTecnico = riscossioneApiServiceHelper.loadDatoTecnico(idRiscossione, null,  httpHeaders, httpRequest);
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] getProvvedimento");
            List<ProvvedimentoDTO> listProvvedimento = riscossioneDTO.getProvvedimento();
			LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] loadAllStatoDebitorioOrByIdRiscossione");
			Response resp = statoDebitorioApiServiceHelper.loadAllStatoDebitorioOrByIdRiscossione(idRiscossione, null , Integer.valueOf(0), Integer.valueOf(Integer.MAX_VALUE), SORT_BY_DT_SCADENZA,  httpHeaders, httpRequest);
			List<StatoDebitorioExtendedDTO> ListStatoDebitorio = resp.readEntity(new GenericType<List<StatoDebitorioExtendedDTO>>() {});
			LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] createBytePdfPratica");
			byte[] pdfBytes =  createBytePdfPratica(riscossioneDTO, datoTecnico, listProvvedimento, ListStatoDebitorio,  httpHeaders, httpRequest );

			FileDownloadDTO fileDownloadDTO = new FileDownloadDTO();
			fileDownloadDTO.setFileName(FILE_NAME);
			fileDownloadDTO.setStream(pdfBytes);
			fileDownloadDTO.setMimeType("application/pdf");
			return fileDownloadDTO;
        } 
		catch (GenericException e) {
            LOGGER.error("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " ,e);
            throw new GenericException(e);
	    }catch (ProcessingException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
        } catch (JsonParseException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} catch (JsonMappingException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} catch (IOException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} catch (DocumentException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} catch (XDocReportException e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} catch (Exception e) {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] SERVER EXCEPTION : " , e);
            throw new GenericException(e);
		} finally {
            LOGGER.debug("[StampaPraticaApiServiceHelper::stampaPratica] END");
        }
	}



	private byte[] createBytePdfPratica(RiscossioneDTO riscossioneDTO, String datoTecnico,
			List<ProvvedimentoDTO> listProvvedimento, List<StatoDebitorioExtendedDTO> listStatoDebitorio,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws Exception {
			LOGGER.debug("[StampaPraticaApiServiceHelper::createPdfPratica] BEGIN");
			byte[] pdfBytes = null;
			try {
				StampaRiscossioneDTO stampaRiscossioneDTO = mapDatiStampaRiscossione(riscossioneDTO, datoTecnico, listProvvedimento,listStatoDebitorio, riscossioneDTO.getTipoAutorizza().getAmbito().getIdAmbito(),  httpHeaders, httpRequest );
				ObjectMapper mapper = new ObjectMapper();
				String stampaRiscossioneJson = mapper.writeValueAsString(stampaRiscossioneDTO);
				pdfBytes = TemplateUtil.getCompiledTemplatePDF(stampaRiscossioneJson, PATH_TEMPLATE);
				LOGGER.debug("[StampaPraticaApiServiceHelper::createPdfPratica] End");
			   return pdfBytes;
            }
			catch (JsonProcessingException e) {
				LOGGER.error("[TemplateUtil::createPdfPratica] ERRORE :", e);
				throw new Exception(e);
			}
			catch (GenericException e) {
				LOGGER.error("[TemplateUtil::createPdfPratica] ERRORE :", e);
				throw new Exception(e);
			}
			catch (Exception e) {
				LOGGER.error("[TemplateUtil::createPdfPratica] ERRORE :", e);
				throw new Exception(e);
			}
		}


	public static DatiTecniciAmbienteDTO creaDatiTecniciFromJsonDt(String jsonDt, String campoDaLeggere) {
		LOGGER.debug("[StampaPraticaApiServiceHelper::creaDatiTecniciFromJsonDt] END");
		String campo = "";
		DatiTecniciAmbienteDTO datiTecniciAmbiente = null;
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);;
		final JSONObject obj = new JSONObject(jsonDt);
		final JSONObject geodata = obj.getJSONObject("riscossione");
		for (String key : obj.getJSONObject("riscossione").keySet()) {
			if (key.equals(campoDaLeggere))
				campo = geodata.getJSONObject(campoDaLeggere).toString();
		}
		try {
			datiTecniciAmbiente = mapper.readValue(campo, DatiTecniciAmbienteDTO.class);
		} catch (IOException e) {
			LOGGER.error("[StampaPraticaApiServiceHelper::creaDatiTecniciFromJsonDt] Errore nella lettura dati json dt", e);
		}

		return datiTecniciAmbiente;
	}

	private StampaRiscossioneDTO  mapDatiStampaRiscossione( RiscossioneDTO riscossioneDTO, String datoTecnico,
			List<ProvvedimentoDTO> listProvvedimento, List<StatoDebitorioExtendedDTO> statoDebitorio,
		  Long idAmbito,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException, ParseException {
		LOGGER.debug("[StampaPraticaApiServiceHelper::mapDatiStampaRiscossione] BEGIN");
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
		StampaRiscossioneDTO stampaRiscossioneDTO = new StampaRiscossioneDTO();
        String dataIniConcessione =riscossioneDTO.getDataIniConcessione() != null ? outputFormat.format(inputFormat.parse(riscossioneDTO.getDataIniConcessione())) :"";
        String dataScadConcessione = riscossioneDTO.getDataScadConcessione() != null  ? outputFormat.format(inputFormat.parse(riscossioneDTO.getDataScadConcessione())) :"";
        String dataIniSospCanone = riscossioneDTO.getDataIniSospCanone() != null ? outputFormat.format(inputFormat.parse(riscossioneDTO.getDataIniSospCanone())) :"";
        String dataFineSospCanone = riscossioneDTO.getDataFineSospCanone() != null ? outputFormat.format(inputFormat.parse(riscossioneDTO.getDataFineSospCanone())) :"";
        riscossioneDTO.setDataFineSospCanone(dataFineSospCanone);
        riscossioneDTO.setDataIniConcessione(dataIniConcessione);
        riscossioneDTO.setDataIniSospCanone(dataIniSospCanone);
        riscossioneDTO.setDataScadConcessione(dataScadConcessione);
		stampaRiscossioneDTO.setRiscossioneDTO(riscossioneDTO);
        String dataNascitaSoggetto =riscossioneDTO.getSoggetto().getDataNascitaSoggetto() != null ? outputFormat.format(inputFormat.parse(riscossioneDTO.getSoggetto().getDataNascitaSoggetto())) :"";
        SoggettiExtendedDTO soggetto = riscossioneDTO.getSoggetto();
        soggetto.setDataNascitaSoggetto(dataNascitaSoggetto);
		stampaRiscossioneDTO.setSoggetto(soggetto);
		for (ProvvedimentoDTO provvedimento : listProvvedimento) {
            String dataScadenzaEmasIso = provvedimento.getDataProvvedimento() != null ? outputFormat.format(inputFormat.parse(provvedimento.getDataProvvedimento())) :"";
            provvedimento.setDataProvvedimento(dataScadenzaEmasIso);
		}
		stampaRiscossioneDTO.setProvvedimento(listProvvedimento);

		RecapitiExtendedDTO recapitiExtendedDTO = riscossioneDTO.getSoggetto().getRecapiti().stream().filter(r -> r.getTipoRecapito().getCodTipoRecapito().equals("P")).collect(Collectors.toList()).get(0);
		stampaRiscossioneDTO.setRecapito(recapitiExtendedDTO);
		TipoRiscossioneExtendedDTO tipoRiscossioneExtendedDTO =	tipiRiscossioneApiServiceHelper.getTipoRiscossioneByIdTipoRiscossioneAndIdAmbito(String.valueOf(riscossioneDTO.getIdTipoRiscossione()), idAmbito.intValue(),
				  httpHeaders,  httpRequest);
		stampaRiscossioneDTO.setTipoRiscossioneExtendedDTO(tipoRiscossioneExtendedDTO);
		if(recapitiExtendedDTO.getIndirizziSpedizione() != null) {
			List<IndirizzoSpedizioneDTO> listIndirizzoSpedizioneDTO = recapitiExtendedDTO.getIndirizziSpedizione().stream().filter(i -> i.getIdGruppoSoggetto() == null).collect(Collectors.toList());
            if(!listIndirizzoSpedizioneDTO.isEmpty()) {
				stampaRiscossioneDTO.setIndirizzoSpedizione(listIndirizzoSpedizioneDTO.get(0));
			}
		}
		if(riscossioneDTO.getGruppoSoggetto() != null) {
			//filtro list componenti togliendo capo gruppo
			List<GruppiSoggettoDTO> listComponentiGruppo = riscossioneDTO.getGruppoSoggetto().getComponentiGruppo().stream().filter(g -> g.getFlgCapoGruppo() == 0).collect(Collectors.toList());
			List<SoggettiExtendedDTO> listsoggettoComponenteGruppo = new ArrayList<>();
			if(!listComponentiGruppo.isEmpty()) {
//				recupero i soggetti interi 
				for (GruppiSoggettoDTO gruppiSoggettoDTO : listComponentiGruppo) {
					SoggettiExtendedDTO soggettoComponenteGruppo =	soggettiApiServiceHelper.loadSoggettoById(gruppiSoggettoDTO.getIdSoggetto().intValue(), null,  httpHeaders, httpRequest);
					listsoggettoComponenteGruppo.add(soggettoComponenteGruppo);
				}
				stampaRiscossioneDTO.setComponentiGruppo(listsoggettoComponenteGruppo);
				stampaRiscossioneDTO.setIsComponentiGruppo("SI");;
			}else {
				stampaRiscossioneDTO.setIsComponentiGruppo("NO");
			}
			
			stampaRiscossioneDTO.setGruppoSoggetto(riscossioneDTO.getGruppoSoggetto());
//			set is gruppo per visualizzare il gruppo all'interno del template
			stampaRiscossioneDTO.setIs_gruppo("SI");
		}else {
			stampaRiscossioneDTO.setIs_gruppo("NO");
		}

		if(!statoDebitorio.isEmpty()) {
			List<StatoDebitorioExtendedDTO> listStatoDebitorioConIvioSp = statoDebitorio.stream().filter(r -> r.getFlgInvioSpeciale() == 1).collect(Collectors.toList());
	        if(!listStatoDebitorioConIvioSp.isEmpty())
	        	stampaRiscossioneDTO.setInvioSpeciale("SI");
	        else
	        	stampaRiscossioneDTO.setInvioSpeciale("NO");
	        
	        for (StatoDebitorioExtendedDTO statoDebitorioExtendedDTO : statoDebitorio) {
	    		statoDebitorioExtendedDTO.getRate().removeIf(s -> s.getIdRataSdPadre() != null);
		        for (RataSdDTO rata : statoDebitorioExtendedDTO.getRate()) {
		        	if(rata.getInteressiMaturati() == null)
		        		rata.setInteressiMaturati(BigDecimal.ZERO);
		        	
                    String dataScadenzaPagamento = rata.getDataScadenzaPagamento() != null ? outputFormat.format(inputFormat.parse(rata.getDataScadenzaPagamento())) :"";
                    rata.setDataScadenzaPagamento(dataScadenzaPagamento);
		        	
		        }
                String dataPagamento = statoDebitorioExtendedDTO.getDataPagamento() != null ? outputFormat.format(inputFormat.parse(statoDebitorioExtendedDTO.getDataPagamento())) :"";
                statoDebitorioExtendedDTO.setDataPagamento(dataPagamento);
	        }
		}
		stampaRiscossioneDTO.setStatoDebitorio(statoDebitorio);
		if( StringUtils.isNotBlank(datoTecnico)) {
			DatiTecniciAmbienteDTO  datiTecniciAmbienteDTO = creaDatiTecniciFromJsonDt(datoTecnico, "dati_tecnici");
			if (datiTecniciAmbienteDTO != null) {
				for (Entry<String, TipoUsoDatoTecnicoDTO> entry : datiTecniciAmbienteDTO.getUsi().entrySet()) {

					TipoUsoDatoTecnicoDTO tipoUsoDatoTecnicoDTO = entry.getValue();
					if (tipoUsoDatoTecnicoDTO.getTipoUsoSpecifico() != null) {
						for (int i = 0; i < tipoUsoDatoTecnicoDTO.getTipoUsoSpecifico().length; i++) {
							String usoSpecifico = tipoUsoDatoTecnicoDTO.getTipoUsoSpecifico()[i];
							TipoUsoExtendedDTO tipoUso = new TipoUsoExtendedDTO();
							try {
								tipoUso = tipoUsoApiServiceHelper.loadTipoUsoByIdTipoUsoOrCodTipoUso(
										getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), usoSpecifico);
							} catch (GenericException e) {
								throw e;
							}
							if (tipoUso != null) {
								tipoUsoDatoTecnicoDTO.getTipoUsoSpecifico()[i] = tipoUso.getDesTipouso();
							}

						}
						String dataScadenzaEmasIso = StringUtils
								.isNotBlank(tipoUsoDatoTecnicoDTO.getDataScadenzaEmasIso())
										? outputFormat.format(
												inputFormat.parse(tipoUsoDatoTecnicoDTO.getDataScadenzaEmasIso()))
										: "";
						tipoUsoDatoTecnicoDTO.setDataScadenzaEmasIso(dataScadenzaEmasIso);
					}

				}
			}
			 
			stampaRiscossioneDTO.setDatiTecnici(datiTecniciAmbienteDTO);
		}
		LOGGER.debug("[StampaPraticaApiServiceHelper::mapDatiStampaRiscossione] END");
		return stampaRiscossioneDTO;
	}
}
