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
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscabesrv.business.be.TipoRicercaMorositaApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaMorositaDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaMorositaDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TipoRicercaMorositaApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaMorositaApiServiceImpl extends BaseApiServiceImpl implements TipoRicercaMorositaApi {

	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipoRicercaMorositaDAO tipoRicercaMorositaDAO;
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 

	
	@Override
	public Response loadAllTipoRicercaMorosita(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::loadAllTipoRicercaMorosita] BEGIN");
	        List<TipoRicercaMorositaDTO> tipoRicercaMorositaList = new ArrayList<TipoRicercaMorositaDTO>();
			try {
				tipoRicercaMorositaList = tipoRicercaMorositaDAO.loadAllTipoRicercaMorosita();
		       
			} catch (DAOException e) {
				LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::loadAllTipoRicercaMorosita] END");
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}

	        LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::loadAllTipoRicercaMorosita] END");
	        return Response.ok(tipoRicercaMorositaList).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response ricercaMorosita(String fruitore, String tipoRicercaMorosita, Integer anno, Integer flgRest,
			Integer flgAnn, String lim, Integer offset, Integer limit, String sort, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] BEGIN");
		 String jsonString = null;
		 List<StatoDebitorioExtendedDTO> listMorosita = new ArrayList<StatoDebitorioExtendedDTO>();
		 try {
			    LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] verificaIdentitaDigitale BEGIN");
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);   
			    LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] verificaIdentitaDigitale END");
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
				LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] getAmbitoByIdentitaOrFonte BEGIN");
				Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] getAmbitoByIdentitaOrFonte END");
			    
			    listMorosita = tipoRicercaMorositaDAO.ricercaMorosita(tipoRicercaMorosita,idAmbito, anno, flgRest,flgAnn, lim,
			    		offset, limit, sort);
			    if(offset != null && limit != null) {
				    Integer numberAllRicercaMorosita = tipoRicercaMorositaDAO.countRicercaMorosita(tipoRicercaMorosita,idAmbito, anno, flgRest,flgAnn, lim);
					PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
					paginationHeader.setTotalElements(numberAllRicercaMorosita);
				    paginationHeader.setTotalPages((numberAllRicercaMorosita / limit) + ((numberAllRicercaMorosita % limit) == 0 ? 0 : 1));
				    paginationHeader.setPage(offset);
				    paginationHeader.setPageSize(limit);
				    paginationHeader.setSort(sort);
					JSONObject jsonPaginationHeader = new JSONObject(paginationHeader.getMap());
				    jsonString =  jsonPaginationHeader.toString();   
			    }

			    
		}
		catch (BusinessException be) {
				return handleBusinessException(be.getHttpStatus(), be);
		} 
		catch (Exception e) {
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		}
		LOGGER.debug("[TipoRicercaMorositaApiServiceImpl::ricercaMorosita] END");
       return Response.ok(listMorosita)
       		.header("PaginationInfo", jsonString)
       		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
