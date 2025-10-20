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
import it.csi.risca.riscabesrv.business.be.TipoRicercaRimborsiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.DAOException;
import it.csi.risca.riscabesrv.business.be.impl.dao.TipoRicercaRimborsiDAO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.PaginationHeaderDTO;
import it.csi.risca.riscabesrv.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscabesrv.dto.TipoRicercaRimborsoDTO;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;

/**
 * The type TipoRicercaRimborsiApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipoRicercaRimborsiApiServiceImpl extends BaseApiServiceImpl implements TipoRicercaRimborsiApi {

	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipoRicercaRimborsiDAO tipoRicercaRimborsiDAO;
    
    @Autowired
    private IdentitaDigitaleManager identitaDigitaleManager; 


	@Override
	public Response loadAllTipoRicercaRimborsi(SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
	        LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::loadAllTipoRicercaRimborsi] BEGIN");
	        List<TipoRicercaRimborsoDTO> tipoRicercaMorositaList = new ArrayList<TipoRicercaRimborsoDTO>();
			try {
				tipoRicercaMorositaList = tipoRicercaRimborsiDAO.loadAllTipoRicercaRimborsi();
		       
			} catch (DAOException e) {
				LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::loadAllTipoRicercaRimborsi] END");
				ErrorDTO err = new ErrorDTO("500", "E005", "Errore inaspettato nella gestione della richiesta. Riprova a breve.", null, null);
				return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
			}

	        LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::loadAllTipoRicercaRimborsi] END");
	        return Response.ok(tipoRicercaMorositaList).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


	@Override
	public Response ricercaRimborsi(String fruitore, String tipoRicercaRimborsi, Integer anno, Integer offset,
			Integer limit, String sort, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] BEGIN");
		 String jsonString = null;
		 List<StatoDebitorioExtendedDTO> listRimborsi = new ArrayList<StatoDebitorioExtendedDTO>();
		 try {
			    LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] verificaIdentitaDigitale BEGIN");
			    identitaDigitaleManager.verificaIdentitaDigitale(fruitore, null, httpHeaders, Constants.LOAD_STDEBI);   
			    LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] verificaIdentitaDigitale END");
				Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), fruitore);
				LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] getAmbitoByIdentitaOrFonte BEGIN");
				Long idAmbito = identitaDigitaleManager.getAmbitoByIdentitaOrFonte(identita, fruitore, null);
				LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] getAmbitoByIdentitaOrFonte END");
			    listRimborsi = tipoRicercaRimborsiDAO.ricercaRimborsi(tipoRicercaRimborsi, idAmbito, anno, offset, limit, sort);
			    if(offset != null && limit != null) {
				    Integer numberRicercaRimborsi = tipoRicercaRimborsiDAO.countRicercaRimborsi(tipoRicercaRimborsi,idAmbito, anno);
					PaginationHeaderDTO paginationHeader = new PaginationHeaderDTO();
					paginationHeader.setTotalElements(numberRicercaRimborsi);
				    paginationHeader.setTotalPages((numberRicercaRimborsi / limit) + ((numberRicercaRimborsi % limit) == 0 ? 0 : 1));
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
		LOGGER.debug("[TipoRicercaRimborsiApiServiceImpl::ricercaRimborsi] END");
       return Response.ok(listRimborsi)
       		.header("PaginationInfo", jsonString)
       		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
