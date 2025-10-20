/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.BusinessLogic;
import it.csi.risca.riscabesrv.business.be.RiscossioneApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.ValidationException;
import it.csi.risca.riscabesrv.business.be.impl.dao.LockRiscossioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.ProvvedimentoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneRecapitoDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneSearchResultDTO;
import it.csi.risca.riscabesrv.dto.VerifyRiscossioneStatoDebDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.tracciamento.TracciamentoManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;

/**
 * The type riscossione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class RiscossioneApiServiceImpl extends BaseApiServiceImpl implements RiscossioneApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();
	
    @Autowired
    private RiscossioneDAO riscossioneDAO;
    
    @Autowired
    private TracciamentoManager tracciamentoManager;
	
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 
	
    @Autowired
    private LockRiscossioneDAO  lockRiscossioneDAO;
    
    @Autowired
    private BusinessLogic businessLogic;
    
    @Autowired
    private  InputValidationService inputValidationService;

    
	@Override
	@Transactional
	public Response saveRiscossione(RiscossioneDTO riscossione, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
	    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] BEGIN");
        LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] Parametro in input riscossione :\n " + riscossione);
		Identita identita = null;
        Long id = null;
		Long idAmbito = null;
		try {	
			setGestAttore(riscossione, fruitore, httpHeaders, httpRequest);
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, Long.parseLong(riscossione.getAmbito()),httpHeaders, Constants.POST_PUT_DEL_RISC); 
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] verificaIdentitaDigitale END");
		    businessLogic.validatorDTO(riscossione, null, null);
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] IdentitaDigitaleManager BEGIN");
			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] IdentitaDigitaleManager END");
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] getAmbitoByIdentitaOrFonte BEGIN");
			idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
			
			
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] getAmbitoByIdentitaOrFonte END");
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] saveRiscossione BEGIN");
			id = riscossioneDAO.saveRiscossione(riscossione, fruitore, idAmbito);
		    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] saveRiscossione END");
		}
		catch (ValidationException  ve) {
			LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
			Integer status = Response.Status.BAD_REQUEST.getStatusCode();
			ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
			   return Response.status(Response.Status.BAD_REQUEST)
	                   .entity(err)
	                   .build();
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}
		catch (Exception e) {
            LOGGER.error("[RiscossioneApiServiceImpl::saveRiscossione:: ERROR ]: "+e);
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
            return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			
		}
		try {
	           	LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione]  BEGIN save tracciamento");
 		
				riscossione.setIdRiscossione(id);

				tracciamentoManager.saveTracciamento(fruitore, riscossione, identita, riscossione.getIdRiscossione(), "JSON RISCOSSIONE",
						riscossione.getIdRiscossione() != null ? riscossione.getIdRiscossione().toString() : null, "RISCA_T_RISCOSSIONE",
						Constants.FLG_OPERAZIONE_INSERT, Constants.OPERAZIONE_INSERT, true, true, httpRequest);			
				
	           	LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione]  END save tracciamento");

			} catch (Exception e) {
	            LOGGER.error("[RiscossioneApiServiceImpl::saveRiscossione:: operazione insert LogAudit ERROR ]: "+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();

				
			}
		
			
	    LOGGER.debug("[RiscossioneApiServiceImpl::saveRiscossione] END");
        return Response.ok(riscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	private void setGestAttore(RiscossioneDTO riscossione, String fruitore,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception  {
		try {
			setGestAttoreInsUpd(riscossione, fruitore, httpRequest, httpHeaders);
			if(Utils.isNotEmpty(riscossione.getRiscossioneRecapito())) {
				for (RiscossioneRecapitoDTO riscossioneRecapitoDTO : riscossione.getRiscossioneRecapito()) {
					setGestAttoreInsUpd(riscossioneRecapitoDTO, fruitore, httpRequest, httpHeaders);
				}
			}
			if(Utils.isNotEmpty(riscossione.getProvvedimento())) {
				for (ProvvedimentoDTO provvedimentoDTO : riscossione.getProvvedimento()) {
					setGestAttoreInsUpd(provvedimentoDTO, fruitore, httpRequest, httpHeaders);
				}	
			}
		} catch (Exception e) {
            LOGGER.error("[RiscossioneApiServiceImpl::setGestAttore:: ERROR ]: ",e);
            throw e;
		}
	}

	@Override
	@Transactional
	public Response updateRiscossione(RiscossioneDTO riscossione, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] BEGIN");
		Identita identita = null;
		Long idRiscossione= null;
		Long idAmbito = null;
		RiscossioneDTO riscossioneOld = null;
		try {
			setGestAttore(riscossione, fruitore, httpHeaders, httpRequest);
		    businessLogic.validatorDTO(riscossione, null, null);
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, Long.parseLong(riscossione.getAmbito()),httpHeaders, Constants.POST_PUT_DEL_RISC); 
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] verificaIdentitaDigitale END");
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] IdentitaDigitaleManager BEGIN");
			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] IdentitaDigitaleManager END");
           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] BEGIN getRiscossione FOR CHECK FIELDS HAS BEEN MODIFIED BEFORE UPATE");
           	riscossioneOld = riscossioneDAO.getRiscossione(String.valueOf(riscossione.getIdRiscossione()));
           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] END getRiscossione FOR CHECK FIELDS HAS BEEN MODIFIED BEFORE UPATE");
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] getLockRiscossioneByIdRiscossione BEGIN");
            lockRiscossioneDAO.verifyLockRiscossione(riscossione.getIdRiscossione(), fruitore, riscossione.getGestAttoreUpd());
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] getLockRiscossioneByIdRiscossione END");
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] getAmbitoByIdentitaOrFonte BEGIN");
			idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] getAmbitoByIdentitaOrFonte END");

		   idRiscossione = riscossioneDAO.updateRiscossione(riscossione, idAmbito);
		}  catch (ValidationException  ve) {
				LOGGER.debug("BAD REQUEST: MESSAGE:"+ve.getMessage());
				Integer status = Response.Status.BAD_REQUEST.getStatusCode();
				ErrorDTO err = new ErrorDTO(status.toString(), Constants.E003,ve.getMessage(), null, null);
				   return Response.status(Response.Status.BAD_REQUEST)
		                   .entity(err)
		                   .build();
			}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}	
		catch (Exception e) {
            LOGGER.error("[RiscossioneApiServiceImpl::updateRiscossione:: ERROR ]: "+e);
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			
		} 
		try {
	           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione]  BEGIN save tracciamento");


				riscossione.setIdRiscossione(idRiscossione);

				tracciamentoManager.saveTracciamento(fruitore, riscossione, identita, riscossione.getIdRiscossione(), "JSON RISCOSSIONE",
						riscossione.getIdRiscossione() != null ? riscossione.getIdRiscossione().toString() : null, "RISCA_T_RISCOSSIONE",
						Constants.FLG_OPERAZIONE_UPDATE, Constants.OPERAZIONE_UPDATE, true, true, httpRequest);			
				

	           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione]  END save tracciamento");
			} catch (Exception e) {
	            LOGGER.error("[RiscossioneApiServiceImpl::updateRiscossione::operazione update LogAudit ERROR ]: "+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
        
        LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossione] BEGIN");
        return Response.ok(riscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getRiscossione(String codRiscossione, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossione] BEGIN");
        LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossione] Parametri in input codRiscossione [" + codRiscossione + "] ");
        RiscossioneDTO riscossione;
		try {
			
			 // Costruisci la mappa dei parametri
	        Map<String, Object> parametri = new HashMap<>();
	        parametri.put("codRiscossione", codRiscossione);

	        // Richiama il servizio di validazione dei parametri
	        inputValidationService.validaParametri(parametri);
	        
		    LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossione] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,
					httpHeaders, Constants.LOAD_RISC);  
		    LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossione] verificaIdentitaDigitale END");
			riscossione = riscossioneDAO.getRiscossione(codRiscossione);
			
	        
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}catch (Exception e) {
            LOGGER.error("[RiscossioneApiServiceImpl::getRiscossione:: ]: "+e);
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossione] END");
        return Response.ok(riscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getRiscossioniGruppo(String codGruppo, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossioniGruppo] BEGIN");
        List<RiscossioneDTO> listRiscossioniGruppo;
		try {
			listRiscossioniGruppo = riscossioneDAO.getRiscossioniGruppo(codGruppo);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
		}
       
        LOGGER.debug("[RiscossioneApiServiceImpl::getRiscossioniGruppo] END");
        return Response.ok(listRiscossioniGruppo).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	

	@Override
	public Response searchRiscossione(RiscossioneSearchDTO RiscossioneSearch, String fruitore, String modalitaRicerca , Integer offset,Integer limit, String sort,
			SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[RiscossioneApiServiceImpl::searchRiscossione] BEGIN");
		 String jsonString = null;
		 List<RiscossioneSearchResultDTO> riscossione = new ArrayList<RiscossioneSearchResultDTO>();
		 try {
			    LOGGER.debug("[RiscossioneApiServiceImpl::searchRiscossione] verificaIdentitaDigitale BEGIN");
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_RISC);   
			    LOGGER.debug("[RiscossioneApiServiceImpl::searchRiscossione] verificaIdentitaDigitale END");
			    
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			    Long idAmbito =	identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, RiscossioneSearch);
			    String competenzaTerritoriale = identitaDigitaleManager.getCompetenzaTerritoriale(identita);
			    LOGGER.info("[RiscossioneApiServiceImpl::searchRiscossione] competenzaTerritoriale: "+competenzaTerritoriale);
			    RiscossioneSearch.setCompetenzaTerritoriale(competenzaTerritoriale);
				riscossione = riscossioneDAO.searchRiscossioni(RiscossioneSearch, idAmbito,modalitaRicerca, offset, limit, sort);
			    Integer numberAllRiscossione = riscossioneDAO.countRiscossioni(RiscossioneSearch, idAmbito,modalitaRicerca);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllRiscossione);
			    paginationHeader.setTotalPages((numberAllRiscossione / limit) + ((numberAllRiscossione % limit) == 0 ? 0 : 1));
			    paginationHeader.setPage(offset);
			    paginationHeader.setPageSize(limit);
			    paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			    jsonString =  jsonPaginationHeader.toString();   
			    
		}
		catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
		} 
		catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[RiscossioneApiServiceImpl::searchRiscossione] END");
        return Response.ok(riscossione)
        		.header("PaginationInfo", jsonString)
        		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	
	@Override
	public Response loadDatoTecnico(Long idRiscossione, String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 LOGGER.debug("[RiscossioneApiServiceImpl::loadDatoTecnico] BEGIN");
		 String json_dt= null;
		try {
		    LOGGER.debug("[RiscossioneApiServiceImpl::loadDatoTecnico] verificaIdentitaDigitale BEGIN");
		    identitaDigitaleManager.verificaIdentitaDigitale(fruitore,null,httpHeaders, Constants.LOAD_RISC); 
		    LOGGER.debug("[RiscossioneApiServiceImpl::loadDatoTecnico] verificaIdentitaDigitale END");
		    json_dt = riscossioneDAO.loadDatoTecnico(idRiscossione);
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}
		catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		
		}

        
        return Response.ok(json_dt).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}





	
	@Override
	public Response verifyRiscossioniSTDebitori(Long idOggetto, String indTipoOggetto, String idTipoOper,
			Long idRiscossione, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        LOGGER.debug("[RiscossioneApiServiceImpl::verifyRiscossioniSTDebitori] BEGIN");
        LOGGER.debug("[RiscossioneApiServiceImpl::verifyRiscossioniSTDebitori] Parametri in input idOggetto :[" + idOggetto + "] "
        		+ " [indTipoOggetto] :  "+ indTipoOggetto+ "[idTipoOper]: " +idTipoOper+ "[idRiscossione]: "+idRiscossione);
		Identita identita = null;
		Long idAmbito = null;
		VerifyRiscossioneStatoDebDTO verifyRisc;
		try {
			 identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),null);
			 idAmbito =	identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, null, null);
			 verifyRisc = riscossioneDAO.verifyRiscossioniSTDebitori(idAmbito, idOggetto,
					indTipoOggetto, idTipoOper, idRiscossione);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
        LOGGER.debug("[RiscossioneApiServiceImpl::verifyRiscossioniSTDebitori] END");
        return Response.ok(verifyRisc).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getCodiciUtenzaByIdStatoDebitorio(Long idStatoDebitorio, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
        LOGGER.debug("[RiscossioneApiServiceImpl::getCodiciUtenzaByIdStatoDebitorio] BEGIN");
        List<String> listCodUtenza;
		try {
			listCodUtenza = riscossioneDAO.getCodiciUtenzaByIdStatoDebitorio(idStatoDebitorio);
		} catch (Exception e) {
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
	
		}
      
        LOGGER.debug("[RiscossioneApiServiceImpl::getCodiciUtenzaByIdStatoDebitorio] END");
        return Response.ok(listCodUtenza).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	private Boolean checkIfFieldRiscossioneHasBeenModified(RiscossioneDTO riscossioneOld, RiscossioneDTO riscossioneNew) {
		StringBuffer listaCampiAggiornati = new StringBuffer();
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "numero_pratica", riscossioneNew.getNumPratica(), riscossioneOld.getNumPratica());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "flg_prenotato", riscossioneNew.getFlgPrenotata(), riscossioneOld.getFlgPrenotata());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "nota", riscossioneNew.getNoteRiscossione(), riscossioneOld.getNoteRiscossione());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "stato_Risocssione", riscossioneNew.getStatiRiscossione().getCodStatoRiscossione(), riscossioneOld.getStatiRiscossione().getCodStatoRiscossione());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "procedimento", riscossioneNew.getTipoAutorizza().getCodTipoAutorizza(), riscossioneOld.getTipoAutorizza().getCodTipoAutorizza());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "Motivo_Prenotazione", riscossioneNew.getMotivoPrenotazione(), riscossioneOld.getMotivoPrenotazione());
		
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "DataFineSospCanone", riscossioneNew.getDataFineSospCanone(), riscossioneOld.getDataFineSospCanone());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "DataIniSospCanone", riscossioneNew.getDataIniSospCanone(), riscossioneOld.getDataIniSospCanone());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "DataIniConcessione", riscossioneNew.getDataIniConcessione(), riscossioneOld.getDataIniConcessione());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "DataScadConcessione", riscossioneNew.getDataScadConcessione(), riscossioneOld.getDataScadConcessione());
		Utils.checkIfFieldHasBeenModified(listaCampiAggiornati, "id_soggetto", riscossioneNew.getSoggetto().getIdSoggetto(), riscossioneOld.getSoggetto().getIdSoggetto());
		
		return listaCampiAggiornati.length() > 0 ? true : false;
	}
	
	private Boolean checkIProvvedimentoHasBeenModified(List<ProvvedimentoDTO> provvedimentoOld, List<ProvvedimentoDTO> provvedimentoNew) {
		boolean isAggiornati = false;
	     List<Long> listaIdProvvedimentoOld = provvedimentoOld.stream().map(p ->p.getIdProvvedimento())
				 .collect(Collectors.toList());
		 List<Long> listaIdProvvedimentoNew = provvedimentoNew.stream().map(p ->p.getIdProvvedimento())
				 .collect(Collectors.toList());
		 if(listaIdProvvedimentoOld.size() != listaIdProvvedimentoNew.size() )
			 isAggiornati= true;
		 else {
			 isAggiornati = !listaIdProvvedimentoOld.stream().anyMatch(listaIdProvvedimentoNew::contains);

		 }
		     
		return isAggiornati ;
	}

	@Override
	public Response updateRiscossioneStatoPratica(RiscossioneDTO riscossione, String fruitore,
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws Exception {
		LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] BEGIN");
		Identita identita = null;
		Long idRiscossione= null;
		Long idAmbito = null;
		try {
			setGestAttoreInsUpd(riscossione, fruitore, httpRequest, httpHeaders);
			
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] verificaIdentitaDigitale BEGIN");
			identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null,httpHeaders, Constants.POST_PUT_DEL_RISC); 
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] verificaIdentitaDigitale END");
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] IdentitaDigitaleManager BEGIN");
			identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] IdentitaDigitaleManager END");
           	
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] getAmbitoByIdentitaOrFonte BEGIN");
			idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
		    LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] getAmbitoByIdentitaOrFonte END");

		   idRiscossione = riscossioneDAO.updateRiscossioneStatoPratica(riscossione, idAmbito);
		}
		catch (BusinessException be) {
			return handleBusinessException(be.getHttpStatus(), be);
		}	
		catch (Exception e) {
            LOGGER.error("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica:: ERROR ]: "+e);
			ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
			return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			
		} 
		try {
	           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica]  BEGIN save tracciamento");
				riscossione.setIdRiscossione(idRiscossione);

				
				

	           	LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica]  END save tracciamento");
			} catch (Exception e) {
	            LOGGER.error("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica::operazione update LogAudit ERROR ]: "+e);
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}
        
        LOGGER.debug("[RiscossioneApiServiceImpl::updateRiscossioneStatoPratica] BEGIN");
        return Response.ok(riscossione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response searchRiscossioneDelegati(RiscossioneSearchDTO RiscossioneSearch, String fruitore,
			String modalitaRicerca, Integer offset, Integer limit, String sort,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 String jsonString = null;
		 List<RiscossioneSearchResultDTO> riscossione = new ArrayList<RiscossioneSearchResultDTO>();
		 try {
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_RISC);   
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			    Long idAmbito =	identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, RiscossioneSearch);
				riscossione = riscossioneDAO.searchRiscossioniDelegati(RiscossioneSearch, idAmbito,modalitaRicerca, offset, limit, sort);
			    Integer numberAllRiscossione = riscossioneDAO.countRiscossioniDelegati(RiscossioneSearch, idAmbito,modalitaRicerca);
				PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
				paginationHeader.setTotalElements(numberAllRiscossione);
			    paginationHeader.setTotalPages((numberAllRiscossione / limit) + ((numberAllRiscossione % limit) == 0 ? 0 : 1));
			    paginationHeader.setPage(offset);
			    paginationHeader.setPageSize(limit);
			    paginationHeader.setSort(sort);
				JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
			    jsonString =  jsonPaginationHeader.toString();   
			}
			catch (BusinessException be) {
					return handleBusinessException(be.getHttpStatus(), be);
			} 
			catch (Exception e) {
					ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
					return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
       return Response.ok(riscossione)
       		.header("PaginationInfo", jsonString)
       		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response countRiscossioniDelegati(RiscossioneSearchDTO RiscossioneSearch, String fruitore,
			String modalitaRicerca, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		Integer numberAllRiscossione = 0;
		 try {
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_RISC);   
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders),fruitore);
			    Long idAmbito =	identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, RiscossioneSearch);
			    numberAllRiscossione = riscossioneDAO.countRiscossioniDelegati(RiscossioneSearch, idAmbito,modalitaRicerca);
			}
			catch (BusinessException be) {
					return handleBusinessException(be.getHttpStatus(), be);
			} 
			catch (Exception e) {
					ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
					return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}finally {
				LOGGER.debug(getClassFunctionEndInfo(className, methodName));
			}
       return Response.ok(numberAllRiscossione)
       		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
}
