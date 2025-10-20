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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.activation.DataHandler;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.iride2.policy.entity.UseCase;
import it.csi.risca.riscabesrv.business.be.DocumentiAllegatiApi;
import it.csi.risca.riscabesrv.business.be.exception.BusinessException;
import it.csi.risca.riscabesrv.business.be.exception.GenericException;
import it.csi.risca.riscabesrv.business.be.helper.acta.backoffice.ActaBackOfficeServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.acta.navigationservice.ActaNavigationServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.acta.object.ActaObjectServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.acta.officialbookservice.ActaOfficialBookServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.acta.relationshipsservice.ActaRelationshipsServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.acta.repositoryservice.ActaRepositoryServiceHelper;
import it.csi.risca.riscabesrv.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscabesrv.business.be.impl.dao.AmbitiConfigDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ConfigurazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.MessaggiDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.ProfilazioneDAO;
import it.csi.risca.riscabesrv.business.be.impl.dao.RiscossioneDAO;
import it.csi.risca.riscabesrv.dto.AmbitoConfigDTO;
import it.csi.risca.riscabesrv.dto.ConfigurazioneDTO;
import it.csi.risca.riscabesrv.dto.ErrorDTO;
import it.csi.risca.riscabesrv.dto.MessaggiDTO;
import it.csi.risca.riscabesrv.dto.ParametriAcaris;
import it.csi.risca.riscabesrv.dto.ProfilazioneDTO;
import it.csi.risca.riscabesrv.dto.ProfiloOggAppDTO;
import it.csi.risca.riscabesrv.dto.RiscossioneDTO;
import it.csi.risca.riscabesrv.dto.enumeration.EnumRelationshipDirectionType;
import it.csi.risca.riscabesrv.dto.enumeration.EnumRelationshipObjectType;
import it.csi.risca.riscabesrv.identitadigitale.IdentitaDigitaleManager;
import it.csi.risca.riscabesrv.util.Constants;
import it.csi.risca.riscabesrv.util.Utils;
import it.csi.risca.riscabesrv.util.actabackoffice.ClientApplicationInfo;
import it.csi.risca.riscabesrv.util.actabackoffice.CodiceFiscaleType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdAOOType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdNodoType;
import it.csi.risca.riscabesrv.util.actabackoffice.IdStrutturaType;
import it.csi.risca.riscabesrv.util.actabackoffice.ObjectIdType;
import it.csi.risca.riscabesrv.util.actabackoffice.PrincipalExtResponseType;
import it.csi.risca.riscabesrv.util.actanavigation.AcarisFaultType;
import it.csi.risca.riscabesrv.util.actaobject.AcarisContentStreamType;
import it.csi.risca.riscabesrv.util.actaobject.AllegatiDTO;
import it.csi.risca.riscabesrv.util.actaobject.ClassificazioniDTO;
import it.csi.risca.riscabesrv.util.actaobject.PrincipalIdType;
import it.csi.risca.riscabesrv.util.actarelationships.RelationshipPropertiesType;
import it.csi.risca.riscabesrv.util.actarepository.AcarisRepositoryEntryType;
import it.doqui.acta.acaris.objectservice.AcarisException;
import it.doqui.acta.actasrv.dto.acaris.type.common.EnumPropertyFilter;
import it.doqui.acta.actasrv.dto.acaris.type.common.EnumQueryOperator;
import it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType;

/**
 * The type riscossione api service.
 *
 * @author CSI PIEMONTE
 */
// ATTENZIONE!!!! 
// Lo scope Resquest per il componente NON e' compatibile con l'eventuale gestione asincrona dei servizi
// Quindi se il componente usa il request scope non si possono invocare i suoi servizi 
// con parametro  asynch=true
@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DocumentiAllegatiApiServiceImpl extends BaseApiServiceImpl implements DocumentiAllegatiApi {

	private static final String IDENTITY = "identity";
	private static final String DATE_FORMAT = "yyyy-MM-dd";

//	
//    @Autowired
//    private DocumentiAllegatiDAO docAllegatiDAO;
    
    @Autowired
    private RiscossioneDAO  riscossioneDAO;
 
    @Autowired
    private MessaggiDAO  messaggiDAO;
    
    @Autowired
    private ProfilazioneDAO profilazioneDAO;
	
    @Autowired
    private IrideServiceHelper serviceHelper;
    
	@Autowired
	private AmbitiConfigDAO ambitiConfigDAO;
    
	@Autowired
	private ConfigurazioneDAO configurazioneDAO; 
	
	@Autowired
	private ActaRepositoryServiceHelper actaRepositoryServiceHelper;
	
	@Autowired
	private ActaBackOfficeServiceHelper actaBackOfficeServiceHelper;
	
	@Autowired
	private ActaObjectServiceHelper actaObjectServiceHelper;
	
	@Autowired
	private ActaNavigationServiceHelper actaNavigationServiceHelper;

	@Autowired
	private ActaRelationshipsServiceHelper actaRelationshipsServiceHelper;
	
	@Autowired
	private ActaOfficialBookServiceHelper actaOfficialBookServiceHelper; 
	
    protected it.csi.risca.riscabesrv.util.actarepository.ObjectIdType repositoryIdentificato = null;
    
	protected PrincipalIdType principalId = null;
	
	private ParametriAcaris parametriAcaris = new ParametriAcaris();
	
	protected String codiceUtenza = null;
	
	protected it.csi.risca.riscabesrv.util.actabackoffice.DecodificaExtType aoo  = null;

	protected it.csi.risca.riscabesrv.util.actabackoffice.DecodificaExtType struttura = null;

	protected it.csi.risca.riscabesrv.util.actabackoffice.DecodificaExtType nodo  = null;
    
	
	@Override
	public Response classificazioni(String idRiscossione, String dataInizioTitolarita,String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::classificazioni] BEGIN");
		 List<ClassificazioniDTO>  output= new ArrayList<ClassificazioniDTO>();
		try {
			init(idRiscossione, fruitore, securityContext, httpHeaders, httpRequest);
			it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
			repoIdObject.setValue(this.repositoryIdentificato.getValue());
			it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();
			propertyFilterType.setFilterType(EnumPropertyFilter.ALL);
			it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal = new it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType();
			principal.setValue(this.principalId.getValue());
			String objectId = this.getObjectId(repoIdObject, principal, propertyFilterType);
			String dbKeyFascicolo = this.getIdentificativoFascicolo(repoIdObject, principal, this.codiceUtenza,
					propertyFilterType, objectId, fruitore);
			output = this.getClassificazione(fruitore, dataInizioTitolarita, repoIdObject, principal, propertyFilterType, dbKeyFascicolo);


			
		} catch (it.doqui.acta.acaris.objectservice.AcarisException e) {
		    LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : " + e);

	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getMessage(): " + e.getMessage());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getFaultInfo().getPropertyName());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getFaultInfo().getObjectId());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getFaultInfo().getExceptionType());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getClassName(): " + e.getFaultInfo().getClassName());
           if(e.getFaultInfo().getErrorCode().equals("SERGEN-E001")) {
		   	     aoo = null;
		   	     struttura = null ;
		   	     nodo = null;
		   	     
		   	    try {
					init(idRiscossione, fruitore, securityContext, httpHeaders, httpRequest);
					it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
					repoIdObject.setValue(this.repositoryIdentificato.getValue());
					it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();
					propertyFilterType.setFilterType(EnumPropertyFilter.ALL);
					it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal = new it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType();
					principal.setValue(this.principalId.getValue());
					String objectId = this.getObjectId(repoIdObject, principal, propertyFilterType);
					String dbKeyFascicolo = this.getIdentificativoFascicolo(repoIdObject, principal, this.codiceUtenza,
							propertyFilterType, objectId, fruitore);
					output = this.getClassificazione(fruitore, dataInizioTitolarita, repoIdObject, principal, propertyFilterType, dbKeyFascicolo);

				}catch (GenericException e1) {
					ErrorDTO err = e1.getError();
		        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] classificazioni - Eccezione:" +e.getMessage());
			        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
				} catch (Exception e1) {
		        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] classificazioni - Eccezione", e);
			        return Response.serverError().entity(e1).status(400).build();
				}
		     	 
		     	 
		     	 
		     	 
	        }else {
			    LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : " + e);
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getMessage(): " + e.getMessage());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getFaultInfo().getPropertyName());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getFaultInfo().getObjectId());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getFaultInfo().getExceptionType());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisException] ERROR : acEx.getFaultInfo().getClassName(): " + e.getFaultInfo().getClassName());
		        return Response.serverError().entity(e).status(Integer.parseInt(e.getFaultInfo().getErrorCode())).build();
	        }
        }
		catch (GenericException e) {
			ErrorDTO err = e.getError();
        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] classificazioni - Eccezione:" +e.getMessage());
	        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] classificazioni - Eccezione", e);
	        return Response.serverError().entity(e).status(400).build();
		}
		 LOGGER.debug("[DocumentiAllegatiApiServiceImpl::classificazioni] END");

		return Response.ok(output)
        		.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	


	private  List<AllegatiDTO> getListAllegati(it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject,
			it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType,
			it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal ,
			it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType responseAllegati) throws GenericException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl:: getListAllegati ] BEGIN");
    	String objectIdDocumento  = null;
    	String idClassificazione    = null;
        List<AllegatiDTO> listAllegati = new ArrayList<>();
		if(responseAllegati.getObjects().length > 0) {
			it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] objSerie = responseAllegati.getObjects();
			if (objSerie != null && objSerie.length > 0) { 
				for (int i = 0; i < objSerie.length; ++i) {
					it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] prtSerie = objSerie[i].getProperties();
					for (it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType property : prtSerie) {
						if (property.getQueryName().getPropertyName().equals("objectIdDocumento") && property.getValue().getContentLength() > 0) {
							objectIdDocumento = property.getValue().getContent()[0];
							LOGGER.debug("getObjectIdBydbKeyClassificazionePrincipale - OjectId reperito tramite ricerca per parola chiave=" + objectIdDocumento);
			            }
			            if (property.getQueryName().getPropertyName().equals("objectIdClassificazione") &&  property.getValue().getContentLength() > 0) {
			            	idClassificazione = property.getValue().getContent()[0];
			            }
					}
					if(objectIdDocumento != null) {
						LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get Attributi Allegati ] BEGIN");
						it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType documentoSemplicePropertiesType= this.getTarget(Constants.DOC_SEMPLICE_PROPERTIES_TYPE);
						it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] criteriaObjectIdDocumento = this.getCriteria(new String[]{"E"}, new String[]{"objectId"},  new String[]{objectIdDocumento});
						it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType responseAttributiAllegati = null;
						try {
							LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get Attributi Allegati] repo Id --> "+repoIdObject.getValue());
							LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get Attributi Allegati] principal Id --> "+principal.getValue());
							LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get Attributi Allegati] Target --> "+documentoSemplicePropertiesType.getObject());

							responseAttributiAllegati = actaObjectServiceHelper.query(repoIdObject, principal , documentoSemplicePropertiesType, propertyFilterType, criteriaObjectIdDocumento,  null, null, null);
						} catch (Exception e) {
							  LOGGER.error("[DocumentiAllegatiApiServiceImpl::AttributiAllegati] ERROR  responseAttributiAllegati" , e);
						}

						if(responseAttributiAllegati != null) {
							it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] objSerieAttAllegati = responseAttributiAllegati.getObjects();

							if (objSerieAttAllegati != null && objSerieAttAllegati.length > 0) {
								for (int j = 0; j < objSerieAttAllegati.length; ++j) {
									it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] prtSerieAttAllegati = objSerieAttAllegati[j].getProperties();
									listAllegati.add(this.mapAllegatiDTO(prtSerieAttAllegati, idClassificazione));
								}
							}else{
								LOGGER.debug("objSerieAttAllegati - OjectId della serie NON reperito o nullo!!");	
							}	
						}

						LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get Attributi Allegati ] END");

					}
				}
			}else{ 
				LOGGER.debug("Ricerca Allegati - OjectId della serie NON reperito o nullo!!");	
    			ErrorDTO err = new ErrorDTO("404", "I019", "Non sono presenti allegati per il documento selezionato..", null, null);
            	throw new GenericException(err);
			}
		}else{ 
			LOGGER.debug("Ricerca Allegati - OjectId della serie NON reperito o nullo!!");	
			ErrorDTO err = new ErrorDTO("404", "I019", "Non sono presenti allegati per il documento selezionato..", null, null);
        	throw new GenericException(err);
		}
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl:: getListAllegati ] BEGIN");
		return listAllegati;
	}

	@Override
	public Response allegati(String dbKeyClassificazione, String idRiscossione,String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl:: Ricerca Allegati ] BEGIN");
        List<AllegatiDTO> listAllegati = new ArrayList<>();
		try {
			init(idRiscossione,fruitore, securityContext, httpHeaders, httpRequest);
			it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
			repoIdObject.setValue(this.repositoryIdentificato.getValue());
			it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();
			propertyFilterType.setFilterType(EnumPropertyFilter.ALL);
			it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal = new it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType();
			principal.setValue(this.principalId.getValue());
			it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType responseAllegati = this.ricercaAllegati(repoIdObject, principal, propertyFilterType, dbKeyClassificazione);
			listAllegati = getListAllegati(repoIdObject, propertyFilterType, principal, responseAllegati);

		
		} catch (it.doqui.acta.acaris.objectservice.AcarisException e) {
		    LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : " + e);

	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getMessage(): " + e.getMessage());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getFaultInfo().getPropertyName());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getFaultInfo().getObjectId());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getFaultInfo().getExceptionType());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getClassName(): " + e.getFaultInfo().getClassName());
           if(e.getFaultInfo().getErrorCode().equals("SERGEN-E001")) {
		   	     aoo = null;
		   	     struttura = null ;
		   	     nodo = null;
		   	    try {
					init(idRiscossione, fruitore, securityContext, httpHeaders, httpRequest);
					it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
					repoIdObject.setValue(this.repositoryIdentificato.getValue());
					it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();
					propertyFilterType.setFilterType(EnumPropertyFilter.ALL);
					it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal = new it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType();
					principal.setValue(this.principalId.getValue());
					it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType responseAllegati= this.ricercaAllegati(repoIdObject, principal, propertyFilterType, dbKeyClassificazione);
					listAllegati = getListAllegati(repoIdObject, propertyFilterType, principal, responseAllegati);
				} catch (AcarisException e2) {
					   LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : " + e);
				        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getMessage(): " + e.getMessage());
				        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
				        return Response.serverError().entity(e).status(Integer.parseInt(e.getFaultInfo().getErrorCode())).build();
				 }catch (GenericException e1) {
						ErrorDTO err = e1.getError();
			        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati] allegati - Eccezione:" +e1.getMessage());
				        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
					} catch (Exception e1) {
			        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati] allegati - Eccezione:" + e1);
				        return Response.serverError().entity(500).build();
					}
	        }else {
			    LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : " + e);
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getMessage(): " + e.getMessage());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getFaultInfo().getPropertyName());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getFaultInfo().getObjectId());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getFaultInfo().getExceptionType());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::Allegati::AcarisException] ERROR : acEx.getFaultInfo().getClassName(): " + e.getFaultInfo().getClassName());
		        return Response.serverError().entity(e).status(Integer.parseInt(e.getFaultInfo().getErrorCode())).build();
	        }
        }
		
		catch (GenericException e) {
			ErrorDTO err = e.getError();
        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] allegati - Eccezione:" +e.getMessage());
	        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] allegati - Eccezione:" + e);
	        return Response.serverError().entity(500).build();
		}
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl:: Ricerca Allegati  ] END");

        return Response.ok(listAllegati).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	
	@Override
	public Response actaContentStream(String idClassificazione,  String idRiscossione, String fruitore, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws IOException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::actaContentStream] BEGIN");
		AcarisContentStreamType output = new AcarisContentStreamType();
		try {
			init(idRiscossione,fruitore, securityContext, httpHeaders, httpRequest);
			String objectId = getObjectIdContentStream(idClassificazione);
			output = getDocumentoContentStream(objectId);
			 
		}
		catch (AcarisFaultType e) {
		    LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : " + e);
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getMessage(): " + e.getMessage());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getErrorCode(): " + e.getErrorCode());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getPropertyName(): " + e.getPropertyName());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getObjectId(): " + e.getObjectId());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getExceptionType(): " + e.getExceptionType());
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::actaContentStream] ERROR : acEx.getClassName(): " + e.getClassName());
           if(e.getErrorCode().equals("SERGEN-E001")) {
		   	     aoo = null;
		   	     struttura = null ;
		   	     nodo = null;
				try {
					init(idRiscossione,fruitore, securityContext, httpHeaders, httpRequest);
					String objectId = getObjectIdContentStream(idClassificazione);
					output = getDocumentoContentStream(objectId);
				} 	
				catch (GenericException e1) {
					ErrorDTO err = e1.getError();
			        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
				} catch (Exception e1) {
		        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] allegati - Eccezione:" + e1);
			        return Response.serverError().entity(500).build();
				}
					

	        } else {
			    LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : " + e);
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getMessage(): " + e.getMessage());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getErrorCode());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getPropertyName());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getObjectId());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getExceptionType());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni::AcarisFaultType] ERROR : acEx.getFaultInfo().getClassName(): " + e.getClassName());
		        return Response.serverError().entity(e).status(Integer.parseInt(e.getErrorCode())).build();
	        }
        }
		catch (GenericException e) {
			ErrorDTO err = e.getError();
	        return Response.serverError().entity(err).status(Integer.parseInt(err.getStatus())).build();
		} catch (Exception e) {
        	LOGGER.error("[DocumentiAllegatiApiServiceImpl::classificazioni] allegati - Eccezione:" + e);
	        return Response.serverError().entity(500).build();
		} 
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::actaContentStream] END");
       return Response.ok(output).build();
	}
	
	 private List<ClassificazioniDTO>  getClassificazione(String fruitore,  String dataInizioTitolaritaString, 
			 it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject,
			 it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal,
			 it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType,
	
				String dbKeyFascicolo) throws Exception {
     		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] BEGIN");
	         List<ClassificazioniDTO> listClassificazioni = new ArrayList<>();
			 
	         // parametri Object Service
	         
			 it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] criteria = this.getCriteria(new String[]{"E","E"}, new String[]{"dbKeyAggregazione","stato"}, new String[]{dbKeyFascicolo,"2"});
	         it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType target = this.getTarget("ClassificazionePrincipaleView");

	         // end parametri Object Service

	         // parametri navigation service 
	         it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repoIdNavigation = new  it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType();
	         repoIdNavigation.setValue(this.repositoryIdentificato.getValue());
	         it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalIdNavigation = new  it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType();
	         principalIdNavigation.setValue(this.principalId.getValue());

	         // end parametri navigation service 
	         
	         // parametri official book  service
	         it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repoIdOfficialbook = new  it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType();
	         repoIdOfficialbook.setValue(this.repositoryIdentificato.getValue());
	         it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalIdOfficialbook = new  it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType();
	         principalIdOfficialbook.setValue(this.principalId.getValue());
	         it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType propertyFilterTypeOfficialbook = new  it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType();
	         propertyFilterTypeOfficialbook.setFilterType(Constants.FILTER_TYPE_ALL);
	         it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType targetOfficialbook = new  it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType();
	         targetOfficialbook.setObject("RegistrazioneClassificazioniView");
	         // end parametri  official book  service

	         List<it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType> listResponse = new ArrayList< it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType>();
	         try {
        		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] repo Id --> "+repoIdObject.getValue());
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] principal Id --> "+principal.getValue());
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] Target --> "+target.getObject());
        		 it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterClassificazione = this.getFilter("ClassificazionePrincipaleView", new String[]{"dbKey", "objectId", "docConAllegati", "dataInizio","docAllegato"}); 
        		 propertyFilterClassificazione.setFilterType(EnumPropertyFilter.LIST);

	        		 boolean hasMoreItems = true;
	        	     Integer skip = 0;
	        	     while( hasMoreItems) {
	        	        it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType response = actaObjectServiceHelper.query(repoIdObject, principal, target, propertyFilterClassificazione, criteria,null, null, skip);
        	    	    hasMoreItems = response.isHasMoreItems();
                        if(hasMoreItems) {
		        	    		skip = skip + response.getObjectsLength();
		        	    }
                        listResponse.add(response);
	        	     }

	        	    if(listResponse.isEmpty()) {
	        	    	for (it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType pagingResponseType : listResponse) {
			 	    		if(pagingResponseType.getObjects().length == 0) { 
			 	    			ErrorDTO err = new ErrorDTO("404", "I021", "Non e' stato trovato alcun documento nel fascicolo associato alla pratica.", null, null);
			 	            	throw new GenericException(err);
			 	    		}
						}

	        	    }
					LOGGER.debug(" oggetti trovati " + listResponse.size());
					for (it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType response : listResponse) {
						it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] obj = response.getObjects();
						if (obj != null && obj.length > 0) {
							for (int i = 0; i < obj.length; ++i) {
								ClassificazioniDTO classificazioniDTO = new ClassificazioniDTO();
								it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] propertiesVoce = obj[i].getProperties();
								for (it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType propertyVoce : propertiesVoce) {
									if (propertyVoce.getQueryName().getPropertyName().equals("dbKey")
											&& propertyVoce.getValue().getContentLength() > 0) {
										String dbKey = propertyVoce.getValue().getContent()[0];
										classificazioniDTO.setDbkeyClassificazione(dbKey);
									}
									if (propertyVoce.getQueryName().getPropertyName().equals("objectId")
											&& propertyVoce.getValue().getContentLength() > 0) {
										String objectIdDocumento = propertyVoce.getValue().getContent()[0];
										classificazioniDTO.setIdClassificazione(objectIdDocumento);
									}
									if (propertyVoce.getQueryName().getPropertyName().equals("docConAllegati")
											&& propertyVoce.getValue().getContentLength() > 0) {
										String docConAllegati = propertyVoce.getValue().getContent()[0];

										classificazioniDTO.setDocConAllegati(docConAllegati.equals("S") ? true : false);
									}
								}
								classificazioniDTO = mapChildrenClassificazioni(repoIdNavigation, classificazioniDTO,principalIdNavigation);

								it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[] criteriaOfficialbook = getCriteriaOfficialbook(
										new String[] { "E", "E" },
										new String[] { "objectIdClassificazione", "idAooResponsabile" },
										new String[] { classificazioniDTO.getIdClassificazione(),
												String.valueOf(parametriAcaris.getIdAoo()) });

								classificazioniDTO = mapProtocolloClassificazioni(repoIdOfficialbook,
										classificazioniDTO, principalIdOfficialbook, targetOfficialbook,
										propertyFilterTypeOfficialbook, criteriaOfficialbook);

								listClassificazioni.add(classificazioniDTO);
							}
						}
					}
					// filter list per dataInizioTitolarita e il fruitore
					listClassificazioni = filtraClassificazioni(listClassificazioni, dataInizioTitolaritaString, fruitore);
					
	        	

		    	
	         }
	         catch (GenericException e) {
		        	LOGGER.error("getClassificazione - Eccezione", e);
		        	throw e;
		        }
	         if(listClassificazioni.size() == 0) { 
	    			ErrorDTO err = new ErrorDTO("404", "I034", "Il servizio non e' al momento attivo.", null, null);
	            	throw new GenericException(err);
	    		}
		     LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] BEGIN: Order list By data inserimento ");
	         listClassificazioni = listClassificazioni.stream().sorted(
	        		Comparator.comparing(ClassificazioniDTO::getDataInserimento, Comparator.nullsLast(Comparator.reverseOrder() ))
	        		 .thenComparing(Comparator.comparing(ClassificazioniDTO::getIdClassificazione).reversed())
	        		 ).collect(Collectors.toList());
	         

	  		 LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getClassificazione] END");
	  		
			return listClassificazioni;
		}
	 /**
	  * Filtra la lista di classificazioni in base a diverse condizioni.
	  *
	  * @param listClassificazioni La lista di classificazioni da filtrare.
	  * @param dataInizioTitolaritaString La stringa rappresentante la data iniziale della titolarita.
	  * @param Il fruitore da utilizzare per il filtro sulla visibilita ed is valido.
	  * @return Una nuova lista contenente gli oggetti che soddisfano le condizioni specificate.
	  */
	 public List<ClassificazioniDTO> filtraClassificazioni(List<ClassificazioniDTO> listClassificazioni, String dataInizioTitolaritaString, String fruitore) {
	     if (dataInizioTitolaritaString != null) {
	         Date dataInizioTitolarita = Utils.convertiStringInData(dataInizioTitolaritaString, DATE_FORMAT);
	         listClassificazioni = listClassificazioni.stream()
	             .filter(f -> {
	                 if (f.getDataInserimento() != null) {
	                     Date dataInserimento = Utils.convertiStringInData(f.getDataInserimento(), DATE_FORMAT);
	                     return dataInserimento != null && dataInserimento.compareTo(dataInizioTitolarita) >= 0;
	                 }
	                 return false;
	             })
	             .collect(Collectors.toList());
	     }

	     if (fruitore != null) {
	         listClassificazioni = listClassificazioni.stream()
	             .filter(f -> !f.getVisibilita().equals("I") && f.isValido())
	             .collect(Collectors.toList());
	     }

	     return listClassificazioni;
	 }

		 
		private ClassificazioniDTO mapProtocolloClassificazioni(
				it.csi.risca.riscabesrv.util.actaofficialbook.ObjectIdType repoIdOfficialbook,
				ClassificazioniDTO classificazioniDTO,
				it.csi.risca.riscabesrv.util.actaofficialbook.PrincipalIdType principalIdOfficialbook,
				it.csi.risca.riscabesrv.util.actaofficialbook.QueryableObjectType targetOfficialbook,
				it.csi.risca.riscabesrv.util.actaofficialbook.PropertyFilterType propertyFilterTypeOfficialbook,
				it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[] criteriaOfficialbook) {
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] BEGIN");
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] repo Id --> "+repoIdOfficialbook.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] principal Id --> "+principalIdOfficialbook.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] Target --> "+targetOfficialbook.getObject());
			LOGGER.debug(criteriaOfficialbook.length > 0 ? "[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] criteria key:valore --> " +  criteriaOfficialbook[0].getPropertyName()+" : " +criteriaOfficialbook[0].getValue() : "" );

			it.csi.risca.riscabesrv.util.actaofficialbook.PagingResponseType resp =  actaOfficialBookServiceHelper.query(repoIdOfficialbook, principalIdOfficialbook,targetOfficialbook,  propertyFilterTypeOfficialbook, criteriaOfficialbook, null, null, null);
	        if(resp != null)
			if(resp.getObjects() != null) {
				it.csi.risca.riscabesrv.util.actaofficialbook.ObjectResponseType[] obj = resp.getObjects();
//		        LOGGER.debug(" oggetti trovati " + obj.length);
		        if (obj != null && obj.length > 0) {
		        	String codiceProtocolloMittente  = null;
		        	String dataProtocolloMittente = null;
		    		String codice = null; 
		    		String dataProtocollo = null;
		            for (int i1 = 0; i1 < obj.length; ++i1) {
		                it.csi.risca.riscabesrv.util.actaofficialbook.PropertyType[] properties =  obj[i1].getProperties();
		                LOGGER.debug(" oggetti trovati " + obj.length);
		                for (it.csi.risca.riscabesrv.util.actaofficialbook.PropertyType propertyVoce : properties) {
		                    if (propertyVoce.getQueryName().getPropertyName().equals("dbKeyTipoRegistrazione") && propertyVoce.getValue().length > 0) {
		                   	 String dbKeyTipoRegistrazione = propertyVoce.getValue()[0];
		                   	 if (dbKeyTipoRegistrazione.equals("1")) {
		                       	 classificazioniDTO.setEntrataUscita("E");
		                   	 }
		                   	if (dbKeyTipoRegistrazione.equals("2")){
		                       	 classificazioniDTO.setEntrataUscita("U");
		                   	 }
		                   }
		                    if (propertyVoce.getQueryName().getPropertyName().equals("codiceProtocolloMittente") && propertyVoce.getValue().length > 0) {
		                      	  codiceProtocolloMittente = propertyVoce.getValue()[0];
		                      	 
		                      }
		                    if (propertyVoce.getQueryName().getPropertyName().equals("dataProtocolloMittente") && propertyVoce.getValue().length > 0) {
		                    	dataProtocolloMittente = propertyVoce.getValue()[0];
		                    	 
		                    }
		                    if (propertyVoce.getQueryName().getPropertyName().equals("codice") && propertyVoce.getValue().length > 0) {
		                    	codice = propertyVoce.getValue()[0];
		                    	 
		                    }
		                    if (propertyVoce.getQueryName().getPropertyName().equals("dataProtocollo") && propertyVoce.getValue().length > 0) {
		                    	dataProtocollo = propertyVoce.getValue()[0];
		                    	 
		                    }
		                 }	
		            }
		            if(codiceProtocolloMittente != null && dataProtocolloMittente != null) {
			            classificazioniDTO.setProtocolloMittente(codiceProtocolloMittente+"-"+dataProtocolloMittente);	
		            }

		            classificazioniDTO.setProtocolloRegionale(codice +"-"+dataProtocollo);
		        }
	        }else {
	        	LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] NON TROVA NESSUN RECORD ");
	        	LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] END");
	        }
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapProtocolloClassificazioni] END");
			return classificazioniDTO;
		}

		private ClassificazioniDTO mapChildrenClassificazioni(
				it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repoIdNavigation,
				ClassificazioniDTO classificazioniDTO,
				it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalIdNavigation) throws AcarisException, Exception {
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] BEGIN");
	        it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType folderId = new  it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType();
	        folderId.setValue(classificazioniDTO.getIdClassificazione()); 
	        it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType propertyFilterTypeNavigation = this.getFilterNavigation("DocumentoSemplicePropertiesType", new String[]{"rappresentazioneDigitale","dataDocCronica", "paroleChiave", "oggetto", "registrato", "definitivo"}); 
	        propertyFilterTypeNavigation.setFilterType("list");
	        
	    	LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] repo Id --> "+repoIdNavigation.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] principal Id --> "+principalIdNavigation.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] Target --> DocumentoSemplicePropertiesType");
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] folderId --> "+folderId.getValue());
	

	        it.csi.risca.riscabesrv.util.actanavigation.PagingResponseType responseGetChildren =   actaNavigationServiceHelper.getChildren(repoIdNavigation, folderId, principalIdNavigation, propertyFilterTypeNavigation, null, null);
	    
	        
	        if( responseGetChildren.getObjects().length > 0) {
	        it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] objGetChildren = responseGetChildren.getObjects();
	        LOGGER.debug(" oggetti trovati " + objGetChildren.length);
	        if (objGetChildren != null && objGetChildren.length > 0) {
	            for (int i1 = 0; i1 < objGetChildren.length; ++i1) {
	                it.csi.risca.riscabesrv.util.actanavigation.PropertyType[] properties =  objGetChildren[i1].getProperties();
	                LOGGER.debug(" oggetti trovati " + objGetChildren.length);
	            	String oggetto = null;

	                for (it.csi.risca.riscabesrv.util.actanavigation.PropertyType propertyVoce : properties) {

	                    if (propertyVoce.getQueryName().getPropertyName().equals("rappresentazioneDigitale") && propertyVoce.getValue().length > 0) {
	                   	 String rappresentazioneDigitale = propertyVoce.getValue()[0];
	                   	 classificazioniDTO.setRappresentazioneDigitale(rappresentazioneDigitale.equals("true") ? true: false);
	                    }		
	                    if (propertyVoce.getQueryName().getPropertyName().equals("oggetto") && propertyVoce.getValue().length > 0) {
	                     	  oggetto = propertyVoce.getValue()[0];
	                    }	
	                    if (propertyVoce.getQueryName().getPropertyName().equals("dataDocCronica") && propertyVoce.getValue().length > 0) {
	                    	String dataDocCronica = propertyVoce.getValue()[0];
	                    	if("".equals(dataDocCronica)) {
	                    		classificazioniDTO.setDataInserimento(null);
	                    	}else {
		                    	dataDocCronica = dataDocCronica.substring(0,10);
		    					classificazioniDTO.setDataInserimento(dataDocCronica);
		    					
	                    	}

	                    }
	                    if (propertyVoce.getQueryName().getPropertyName().equals("paroleChiave") && propertyVoce.getValue().length > 0) {
	                    	if(propertyVoce.getValue()[0] != null && !"".equals(propertyVoce.getValue()[0] )) {
	    		            	String paroleChiave = this.creaParoleChiave(propertyVoce.getValue()[0]);
	    		            	classificazioniDTO.setDescrizione(paroleChiave);
	    	            	}else {
	    	            		classificazioniDTO.setDescrizione(oggetto);
	    	            	}
	                    }	
	                    if (propertyVoce.getQueryName().getPropertyName().equals("registrato") && propertyVoce.getValue().length > 0) {
	                    	 String registrato = propertyVoce.getValue()[0];
	                        classificazioniDTO.setValido(registrato.equals("true") ? true: false);
	                    }	
	                    if (propertyVoce.getQueryName().getPropertyName().equals("definitivo") && propertyVoce.getValue().length > 0) {
	                    	if(!classificazioniDTO.isValido()){
		                    	 String definitivo = propertyVoce.getValue()[0];
		                      	 classificazioniDTO.setValido(definitivo.equals("true") ? true: false);
	                    	}
	                   }	
	                    if (propertyVoce.getQueryName().getPropertyName().equals("paroleChiave") && propertyVoce.getValue().length > 0) {
	                    	if(propertyVoce.getValue()[0] != null && !"".equals(propertyVoce.getValue()[0])) {
	                    		if(propertyVoce.getValue()[0].contains("#INTERNO#")) {
	    		            	    classificazioniDTO.setVisibilita("I");
	        	            	}else {
	          	            		classificazioniDTO.setVisibilita("E");
	        	            	  }
	                      }else {
      	            		classificazioniDTO.setVisibilita("E");
      	            	  }
	                    }	

	                }	
	    	        if(classificazioniDTO.getDescrizione() == null) {
	    	        	classificazioniDTO.setDescrizione(oggetto);
	    	        }
	            }
	        }
	        }else {
	        	LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] NON TROVA NESSUN RECORD ");
	        	LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] END");
	        }
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::mapChildrenClassificazioni] END");
			return classificazioniDTO;
		}

		public String getIdentificativoFascicolo(it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject, it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal, String codiceUtenza,
				it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType, String objectId, String fruitore) throws Exception, GenericException {
			   LOGGER.debug((Object)"getIdentificativoFascicolo - BEGIN");
		        String dbKeyFascicolo = "";

				it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType voceType = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType ();
		        voceType.setValue(objectId);
		        
		        try {
		        	List<it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[]> listcriteria = new ArrayList<>();
		        	
		            it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] statoAperto = this.getCriteria(new String[]{"E","E"}, new String[]{"codice","stato"}, new String[]{codiceUtenza,"1"});
		            it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] statoChiusoInCorrente = this.getCriteria(new String[]{"E","E"}, new String[]{"codice","stato"}, new String[]{codiceUtenza,"9"});
		            it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] statoInAttesaDiChiusura = this.getCriteria(new String[]{"E","E"}, new String[]{"codice","stato"}, new String[]{codiceUtenza,"15"});
		            listcriteria.add(statoAperto);
		            listcriteria.add(statoChiusoInCorrente);
		            listcriteria.add(statoInAttesaDiChiusura);
		            it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType target = this.getTarget("FascicoloRealeLiberoPropertiesType");
		            
					LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getIdentificativoFascicolo] repo Id --> "+repoIdObject.getValue());
					LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getIdentificativoFascicolo] principal Id --> "+principal.getValue());
					LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getIdentificativoFascicolo] Target --> "+target.getObject());	
					// Calcola la dimensione totale dell'array
					int totalSize = 0;
					for (QueryConditionType[] queryConditionType : listcriteria) {
					    it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType res = actaObjectServiceHelper.query(repoIdObject, principal, target, propertyFilterType, queryConditionType, this.getNavigationConditionInfoType(voceType), new Integer(0), new Integer(0));
					    if (res.getObjects() != null) {
					        totalSize += res.getObjects().length;
					        // Ho trovato il fascicolo in un certo stato --> posso uscire dal ciclo
					        break;
					    }
					}

					// Crea un nuovo array con la dimensione totale
					it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] obj = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[totalSize];

					// Indice per tenere traccia di dove iniziare a inserire gli elementi nell'array obj
					int currentIndex = 0;

					// Popola l'array obj con valori dalle chiamate a actaObjectServiceHelper.query()
					for (QueryConditionType[] queryConditionType : listcriteria) {
					    it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType res = actaObjectServiceHelper.query(repoIdObject, principal, target, propertyFilterType, queryConditionType, this.getNavigationConditionInfoType(voceType), new Integer(0), new Integer(0));
					    if (res.getObjects() != null) {
					        for (it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType object : res.getObjects()) {
					            obj[currentIndex] = object;
					            currentIndex++;
					        }
					    }
					    if (obj.length > 0) {
					    	// se ho trovato il fascicolo posso uscire dal ciclo
					    	break;
					    }
					}

//		            it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] obj = response.getObjects();
		            if(obj.length == 0) { 
		            	if(fruitore != null && fruitore.equals(Constants.RISCA_FO)) {
		            		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I034);
			    			ErrorDTO err = new ErrorDTO("404",  messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()), null, null);
			            	throw new GenericException(err);	
		            	}
		            	else
		            	{
		            		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I020);
			    			ErrorDTO err = new ErrorDTO("404",  messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()), null, null);
			            	throw new GenericException(err);	
		            	}

		    		}
		    		if(obj.length > 1) { 
		    			if(fruitore != null && fruitore.equals(Constants.RISCA_FO)) {
		            		MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.I034);
			    			ErrorDTO err = new ErrorDTO("404",  messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()), null, null);
			            	throw new GenericException(err);	
		    			}else {
		    				MessaggiDTO messaggiDTO = messaggiDAO.loadMessaggiByCodMessaggio(Constants.E071);
			    			ErrorDTO err = new ErrorDTO("404",  messaggiDTO.getCodMessaggio(), Utils.removeAccentedCharacters(messaggiDTO.getDesTestoMessaggio()), null, null);
			            	throw new GenericException(err);	
		    			}

		    		}
		    		

		            LOGGER.debug(" oggetti trovati " + obj.length);
		            if (obj != null && obj.length > 0) {
		                for (int i = 0; i < obj.length; ++i) {
		                    it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] propertiesVoce;
		                    LOGGER.debug(" oggetti trovati " + obj.length);
		                    for (it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType propertyVoce : propertiesVoce = obj[i].getProperties()) {
		                        if (propertyVoce.getQueryName().getPropertyName().equals("dbKey")){
			                        dbKeyFascicolo = propertyVoce.getValue().getContent()[0];
		                        }

		                    }
		                }
		            }
		        }
		        catch (GenericException e) {
		        	LOGGER.error("getIdentificativoFascicolo - Eccezione", e);
		        	throw e;
		        }
		        LOGGER.debug((Object)"getIdentificativoFascicolo - END");
		        return dbKeyFascicolo;
		    }
		private String getObjectId(it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repoIdObject ,
				it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principalIdType, it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType) throws GenericException, AcarisException {
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectId] BEGIN");
			it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType queryObjectType= this.getTarget(Constants.SERIE_FSC_PROPERTIES_TYPE);
			
			it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] criteria = this.getCriteria(new String[]{"E"}, new String[]{"paroleChiave"},  new String[]{parametriAcaris.getParoleChiaveSF()});
	     

			it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType pagingResponseType = null;
			try {
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectId] repo Id --> "+repoIdObject.getValue());
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectId] principal Id --> "+principalIdType.getValue());
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectId] Target --> "+queryObjectType.getObject());
				pagingResponseType = actaObjectServiceHelper.query(repoIdObject, principalIdType, queryObjectType, propertyFilterType, criteria,  null, null, null);
			} catch (Exception e) {
				  LOGGER.error("[DocumentiAllegatiApiServiceImpl::getObjectId] ERROR " , e);
				  throw e;
			}
			if(pagingResponseType.getObjects().length == 0) { 
			
				ErrorDTO err = new ErrorDTO("404", "E071", "Attenzione: Vi sono delle incongruenze nella configurazione su DoQui ACTA", null, null);
	        	throw new GenericException(err);
			}
			if(pagingResponseType.getObjects().length > 1) {
				ErrorDTO err = new ErrorDTO("409", "E071", "Attenzione: Vi sono delle incongruenze nella configurazione su DoQui ACTA", null, null);
	        	throw new GenericException(err);
			}
			it.doqui.acta.actasrv.dto.acaris.type.common.ObjectResponseType[] objSerie = pagingResponseType.getObjects();
	    	String objectIdDocumento  = null;
			if (objSerie != null && objSerie.length > 0) {
				for (int i = 0; i < objSerie.length; ++i) {
					it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] prtSerie = objSerie[i].getProperties();
					for (it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType property : prtSerie) {
						if (property.getQueryName().getPropertyName().equals("objectId") && property.getValue().getContentLength() > 0) {
							objectIdDocumento = property.getValue().getContent()[0];
							LOGGER.debug("getObjectId - ObjectId reperito tramite ricerca per parola chiave=" + objectIdDocumento);
			            }

					}
				}
			}else{
				LOGGER.debug("getObjectId - ObjectId della serie NON reperito o nullo!!");	
			}
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectId] END");
			return objectIdDocumento;
		}

    private it.doqui.acta.actasrv.dto.acaris.type.common.NavigationConditionInfoType getNavigationConditionInfoType(it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType nodo) {
    	LOGGER.debug((Object)"getNavigationConditionInfoType - BEGIN");
        it.doqui.acta.actasrv.dto.acaris.type.common.NavigationConditionInfoType info = new it.doqui.acta.actasrv.dto.acaris.type.common.NavigationConditionInfoType();
        info.setLimitToChildren(new Boolean(true));
        info.setParentNodeId(nodo);
        LOGGER.debug((Object)"getNavigationConditionInfoType - END");
        return info;
    }
	private AcarisContentStreamType getDocumentoContentStream(String objectId) throws GenericException, IOException, AcarisException {

		String typeID = EnumRelationshipObjectType.DOCUMENT_COMPOSITION_PROPERTIES_TYPE.getDescrizione();
		String direction = EnumRelationshipDirectionType.SOURCE.getDescrizione();
		it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType objectIdType = new it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType(objectId);
		it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType repositoryId = new it.csi.risca.riscabesrv.util.actarelationships.ObjectIdType(this.repositoryIdentificato.getValue());
		it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType principalId = new it.csi.risca.riscabesrv.util.actarelationships.PrincipalIdType(this.principalId.getValue());
		it.csi.risca.riscabesrv.util.actarelationships.PropertyFilterType filter = new  it.csi.risca.riscabesrv.util.actarelationships.PropertyFilterType(Constants.FILTER_TYPE_ALL, null);

		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getDocumentoContentStream::getObjectRelationships] repo Id --> "+repositoryId.getValue());
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getDocumentoContentStream::getObjectRelationships] principal Id --> "+principalId.getValue());
		 RelationshipPropertiesType[] ret = null;
		
		 AcarisContentStreamType  output = new AcarisContentStreamType();
		 try {
				ret = actaRelationshipsServiceHelper.getObjectRelationships(repositoryId, principalId, objectIdType,
						typeID, direction, filter);

				it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repository = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
				repository.setValue(this.repositoryIdentificato.getValue());
				it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principal = new it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType();
				principal.setValue(this.principalId.getValue());

				String nomeFile = "";
				Integer dimensioneFile = null;
				byte[] streamMTOM = null;
				byte[] stream = null;
				String mimeType = null;
				String nodeUID = null;
	            
	   		 if (ret != null && ret.length > 0) {
	             for (int i = 0; i < ret.length; ++i) {
	            	 it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType documentIdFisico = new it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType();
	            		            	
	            	 documentIdFisico.setValue(ret[i].getTargetId().getValue());
	            		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getDocumentoContentStream::actaObjectServiceHelper.getContentStream] repo Id --> "+repository.getValue());
	            		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getDocumentoContentStream::actaObjectServiceHelper.getContentStream] principal Id --> "+principal.getValue());

	                 it.doqui.acta.actasrv.dto.acaris.type.common.AcarisContentStreamType[] retCS = actaObjectServiceHelper.getContentStream(repository, documentIdFisico, principal, it.doqui.acta.actasrv.dto.acaris.type.common.EnumStreamId.PRIMARY);
	                 if (retCS != null) {
		                 LOGGER.debug("contentStream " + retCS.length);
		                 for (int iCS = 0; iCS < retCS.length; ++iCS) {
						     if (iCS == 0) {
						         nomeFile = retCS[0].getFilename();
						     }
						     LOGGER.debug("filename " + retCS[0].getFilename());
						     streamMTOM = this.getContentAsByteArray(retCS[0].getStreamMTOM());
						     if(streamMTOM != null)
						        dimensioneFile = new Integer(streamMTOM.length);

						     stream =retCS[0].getStream();
						     mimeType = retCS[0].getMimeType().value();
						     nodeUID  = retCS[0].getNodeUID();
						 }
	                  }
	                 else { 
	                	ErrorDTO err = new ErrorDTO("404", "E072", "Non e' stato possibile scaricare il file associato al documento.", null, null);
	     	            throw new GenericException(err);
	                 }
	             }
	         }
	   		 else { 
	    		ErrorDTO err = new ErrorDTO("404", "I022", "Non e' stato trovato alcun file associato al documento.", null, null);
	            throw new GenericException(err);
	   		 }
	   		 output = this.mapToAcarisContentStreamType(nomeFile, dimensioneFile, mimeType,  nodeUID , streamMTOM, stream);
		 }       
		 catch (GenericException ex) {
			 	LOGGER.debug((Object)("ex.getMessage() " + ex.getMessage()));
	            throw ex;//ex.printStackTrace();
	            
	        }
		 catch (AcarisException e) {
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : " + e);

		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getMessage(): " + e.getMessage());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getFaultInfo().getErrorCode(): " + e.getFaultInfo().getErrorCode());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getFaultInfo().getPropertyName(): " + e.getFaultInfo().getPropertyName());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getFaultInfo().getObjectId(): " + e.getFaultInfo().getObjectId());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getFaultInfo().getExceptionType(): " + e.getFaultInfo().getExceptionType());
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : acEx.getFaultInfo().getClassName(): " + e.getFaultInfo().getClassName());
	            throw e;
		}
		 
		return output;
	}
	
    private AcarisContentStreamType mapToAcarisContentStreamType(String nomeFile, Integer fileDimensione, String mimeType, String nodeUID , byte[] streamMTOM, byte[] stream) {
    	AcarisContentStreamType dto = new AcarisContentStreamType(); 
    	dto.setFilename(nomeFile);		
    	dto.setLength(fileDimensione);
    	dto.setMimeType(mimeType);
    	dto.setNodeUID(nodeUID);
    	dto.setStream(stream);
    	dto.setStreamMTOM(streamMTOM);

        return dto;
    }
    
    public static byte[] getContentAsByteArray(DataHandler handler) throws IOException {
        byte[] bytes = null;
//        final InputStream in = handler.getInputStream();
//        byte[] byteArray=org.apache.commons.io.IOUtils.toByteArray(in);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        handler.writeTo(bos);
        bos.flush();
        bos.close();
        bytes = bos.toByteArray();
        LOGGER.debug("BYTES ARRAY:" + bytes);
        return bytes;
    }
	private String getObjectIdContentStream(String idClassificazione) throws  Exception, AcarisFaultType{
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] BEGIN");
	 it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType folderId = new it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType(idClassificazione);
	 it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType repositoryId = new it.csi.risca.riscabesrv.util.actanavigation.ObjectIdType(this.repositoryIdentificato.getValue());
	 it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType principalId = new it.csi.risca.riscabesrv.util.actanavigation.PrincipalIdType(this.principalId.getValue());
	 it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter = new  it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType(Constants.FILTER_TYPE_ALL, null);
//	 it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter = this.getFilterNavigation("DocumentoSemplicePropertiesType", new String[]{"objectId"}); 
//	 filter.setFilterType(idClassificazione);
	 LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] repo Id --> "+repositoryId.getValue());
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] principal Id --> "+principalId.getValue());
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream folderId --> "+folderId.getValue());

//		DocumentoSemplicePropertiesType
		
	 try {
		it.csi.risca.riscabesrv.util.actanavigation.PagingResponseType reponseObjectId = actaNavigationServiceHelper.getChildren(repositoryId,  folderId,  principalId,  filter,  null,  null) ;
		 it.csi.risca.riscabesrv.util.actanavigation.ObjectResponseType[] objSerie = reponseObjectId.getObjects();
		 String objectId = null;
		 if (objSerie != null && objSerie.length > 0) {
			for (int i = 0; i < objSerie.length; ++i) {
				it.csi.risca.riscabesrv.util.actanavigation.PropertyType[] prtSerie = objSerie[i].getProperties();
				for (it.csi.risca.riscabesrv.util.actanavigation.PropertyType property : prtSerie) {
					if (property.getQueryName().getClassName().equals("DocumentoSemplicePropertiesType"))
						if (property.getQueryName().getPropertyName().equals("objectId") && property.getValue().length > 0) {
							objectId = property.getValue()[0];
							LOGGER.debug("getObjectIdContentStream - OjectId reperito tramite ricerca per parola chiave=" + objectId);
			            }
		            
				}
			}
		 }else{
			LOGGER.debug("Ricerca Allegati - OjectId della serie NON reperito o nullo!!");	
		 }
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] END");
		 return objectId;
	} catch ( AcarisFaultType e) {
		 LOGGER.error("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] ERROR : " + e);
		 throw e;
	} catch (Exception e) {
		 LOGGER.error("[DocumentiAllegatiApiServiceImpl::getObjectIdContentStream] ERROR : " + e);
		 throw e;
	}
	
	}
	
	private void verifyRepositoryId() throws Exception {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::RECUPERARE  REPOSITORY ID  da ACTA:: BEGIN");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		if (this.repositoryIdentificato == null) {
			List<ConfigurazioneDTO> listConfigRepoId = configurazioneDAO.loadConfigByKey("RISCA_ACTA.repository_id");
			if (!listConfigRepoId.isEmpty()) {
				String ConfigurazioneDTO = listConfigRepoId.get(0).getValore();
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::AGGIORNARE REPOSITORY ID IN MEMORIA] ");
				this.repositoryIdentificato = new it.csi.risca.riscabesrv.util.actarepository.ObjectIdType(ConfigurazioneDTO);
			}
			List<ConfigurazioneDTO> listConfigDTCreazioneRepoId = configurazioneDAO.loadConfigByKey("RISCA_ACTA.data_creazione_repository_id");
			if (!listConfigDTCreazioneRepoId.isEmpty()) {
				Date now = cal.getTime();
				ConfigurazioneDTO configurazioneDTO = listConfigDTCreazioneRepoId.get(0);
				configurazioneDTO.setValore(formatter.format(now));
				configurazioneDAO.updatetConfigurazioneDTO(configurazioneDTO);
			}


		} else {
			ConfigurazioneDTO configRepositoryId = new ConfigurazioneDTO();
			ConfigurazioneDTO configDTcreazioneRepositoryId = new ConfigurazioneDTO();
			ConfigurazioneDTO configTimerPerLancio = new ConfigurazioneDTO();
			try {
				Map<String, String> mapConfigurazioneActa = configurazioneDAO
						.loadConfigByKeyList(Arrays.asList("RISCA_ACTA.orario_get_repository_id",
								"RISCA_ACTA.data_creazione_repository_id", "RISCA_ACTA.repository_id"));

				for (Map.Entry<String, String> config : mapConfigurazioneActa.entrySet()) {
					if (config.getKey().equals("RISCA_ACTA.orario_get_repository_id")) {
						configTimerPerLancio.setChiave(config.getKey());
						configTimerPerLancio.setValore(config.getValue());
					}

					if (config.getKey().equals("RISCA_ACTA.data_creazione_repository_id")) {
						configDTcreazioneRepositoryId.setChiave(config.getKey());
						configDTcreazioneRepositoryId.setValore(config.getValue());
					}

					if (config.getKey().equals("RISCA_ACTA.repository_id")) {
						configRepositoryId.setChiave(config.getKey());
						configRepositoryId.setValore(config.getValue());
					}
				}

			} catch (Exception e) {
		        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId] ERROR : " + e);
				throw new GenericException(e);
			}
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::VERIFICARE IL TIMER PER AGGIORNARE REPOSITORY ] ");
			Date dateinsertRepoId = formatter.parse(configDTcreazioneRepositoryId.getValore());
			LocalDateTime ultimaDateinsertRepoId = Instant.ofEpochMilli(dateinsertRepoId.getTime())
		      .atZone(ZoneId.systemDefault())
		      .toLocalDateTime();
			LocalDateTime dateAttuale = LocalDateTime.now();
			long diffInMinutes = ChronoUnit.MINUTES.between(ultimaDateinsertRepoId, dateAttuale);
			SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm:ss");
			Date dataLancioChiamata = formatter2.parse(configTimerPerLancio.getValore());
			long sommaLancioInMinuti = 0l;
			int oraLancioChiamata = dataLancioChiamata.getHours();
			if (oraLancioChiamata > 0) {
			     sommaLancioInMinuti = (long) oraLancioChiamata * 60;
			}

			int minutiLancioChiamata = dataLancioChiamata.getMinutes();
			if (minutiLancioChiamata > 0) {
				sommaLancioInMinuti = sommaLancioInMinuti + minutiLancioChiamata;
			}
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::CONFRONTARE LE DUE DATE PER AGGIORNAMENTO DEL REPOSITORY ID] ");
			if (diffInMinutes > sommaLancioInMinuti) {
				this.repositoryIdentificato = this.getRepository(parametriAcaris.getRepositoryName());
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::AGGIORNARE REPOSITORY ID SU BD] ");
				ConfigurazioneDTO configurazioneRepositoryId = configurazioneDAO.loadConfigByKey("RISCA_ACTA.repository_id").get(0);
				configurazioneRepositoryId.setValore(this.repositoryIdentificato.getValue());
				configurazioneDAO.updatetConfigurazioneDTO(configurazioneRepositoryId);
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::AGGIORNARE LA DATA DI CREAZIONE REPOSITORY ID  SU DB] ");
                Date now = cal.getTime();
				ConfigurazioneDTO configDataCreazioneRepoId = configurazioneDAO.loadConfigByKey("RISCA_ACTA.data_creazione_repository_id").get(0);
				configDataCreazioneRepoId.setValore(formatter.format(now));
				configurazioneDAO.updatetConfigurazioneDTO(configDataCreazioneRepoId);
			}else {
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::AGGIORNARE REPOSITORY ID IN MEMORIA] ");
				this.repositoryIdentificato = new it.csi.risca.riscabesrv.util.actarepository.ObjectIdType(configRepositoryId.getValore());
			}

		}

		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init::verifyRepositoryId]::RECUPERARE  REPOSITORY ID  da ACTA] END");
	}

	public void init(String idRiscossione,String fruitore, SecurityContext securityContext, HttpHeaders httpHeaders,
				HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::init] BEGIN");
		try {
			this.verificaIdentitaAndAmbito(idRiscossione, fruitore, httpHeaders, httpRequest);
			this.getConfigurazioneActaFromDBRisca();
			
			this.verifyRepositoryId();
			this.principalId = verifyPrincipalId();
			
		} catch (GenericException e) {
			 ErrorDTO err = e.getError();
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init] ERROR : " + err);
	    	throw new GenericException(err);
	
		}catch (Exception e) {
	        LOGGER.error("[DocumentiAllegatiApiServiceImpl::init] ERROR : " + e);

		}
	
	}
	
	private PrincipalIdType verifyPrincipalId() throws GenericException, ParseException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::INIT::verifyPrincipalId:: PRINCIPAL ID] BEGIN");
	     if( aoo != null  && struttura != null &&  nodo != null) {
	 		long idAoo = this.parametriAcaris.getIdAoo();
			long idNodo = this.parametriAcaris.getIdNodo();
			long idStruttura = this.parametriAcaris.getIdStruttura();
	    	 if( idAoo == aoo.getIdentificatore() && idStruttura == struttura.getIdentificatore() && idNodo == nodo.getIdentificatore()  ) {
	    		 LOGGER.debug("[DocumentiAllegatiApiServiceImpl::INIT::verifyPrincipalId:: PRINCIPAL ID] recupero principal id dalla memoria");
	    		 return principalId;
	    	 }
	     }
		return getPrincipalId();
		
	}
	
	private PrincipalIdType getPrincipalId() {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::INIT::getPrincipalId:: PRINCIPAL ID] BEGIN");
         PrincipalExtResponseType[] arrPrincipal =  this.getPrincipalExt(null, null, null);
	     
		 PrincipalIdType principalIdType = new PrincipalIdType();
	     for (int i = 0; i < arrPrincipal.length; i++) {
			 principalIdType.setValue(arrPrincipal[i].getPrincipalId().getValue());
			 aoo = arrPrincipal[i].getUtente().getAoo();
			 struttura = arrPrincipal[i].getUtente().getStruttura();
			 nodo = arrPrincipal[i].getUtente().getNodo();
			 
			 LOGGER.debug("[DocumentiAllegatiApiServiceImpl::get principal Id ] principal Id: "+principalIdType.getValue());
		 }
	     LOGGER.debug("[DocumentiAllegatiApiServiceImpl::INIT::getPrincipalId] END");
		 return  principalIdType;
	}


	public void verificaIdentitaAndAmbito(String idRiscossione,String fruitore, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaAndAmbito] BEGIN");
		RiscossioneDTO riscossione = null;
		try {
			riscossione = riscossioneDAO.getRiscossione(idRiscossione);
		} catch (SQLException e) {
			  LOGGER.error("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] ERROR " , e);
		}
		Long idAmbitoPratica = null;
		if(riscossione != null) {
			this.codiceUtenza = riscossione.getCodRiscossione();
			if(riscossione.getStatiRiscossione() != null)
			  idAmbitoPratica = riscossione.getStatiRiscossione().getIdAmbito();
		}
		Identita identita = IdentitaDigitaleManager.getIdentitaDigitale(IdentitaDigitaleManager.getKeyFromHeader(httpHeaders), null);

		if (identita != null && fruitore == null) {
			verificaIdentitaDigitale(identita);
			Long idAmbitoUtente = getIdAmbitoFromIride(identita);
			if (idAmbitoUtente != null) {
				LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaAndAmbito] idAmbito:" + idAmbitoUtente);
				if (!idAmbitoUtente.equals(idAmbitoPratica)) {
					ErrorDTO err = new ErrorDTO("401", "U004 ", "L'ambito dell'utente e l'ambito della pratica non corrispondono.", null, null);
	            	throw new GenericException(err);
				}
			}
			
		}

		Map<String, String> detailsConfig = new HashMap<String, String>();
			parametriAcaris.setIdAmbitoDTO(idAmbitoPratica);
    		try {
				for(AmbitoConfigDTO ambitiConfig : CollectionUtils.emptyIfNull(ambitiConfigDAO.loadAmbitiConfigByIdOrCodAmbito(idAmbitoPratica.toString()))) {
					if(ambitiConfig.getChiave().equals(Constants.ACTA_PAROLE_CHIAVE_SF)) {
						parametriAcaris.setParoleChiaveSF(ambitiConfig.getValore());
						
						detailsConfig.put(ambitiConfig.getChiave(), ambitiConfig.getValore());
					}
					if(ambitiConfig.getChiave().equals(Constants.ACTA_ID_AOO) ) {
						parametriAcaris.setIdAoo(Long.parseLong(ambitiConfig.getValore()));
						detailsConfig.put(ambitiConfig.getChiave(), ambitiConfig.getValore());
					}
					if(ambitiConfig.getChiave().equals(Constants.ACTA_ID_NODO) ) {
						parametriAcaris.setIdNodo(Long.parseLong( ambitiConfig.getValore()));
						detailsConfig.put(ambitiConfig.getChiave(), ambitiConfig.getValore());
						
					}
					if(ambitiConfig.getChiave().equals(Constants.ACTA_ID_STRUTTURA) ) {
						parametriAcaris.setIdStruttura(Long.parseLong( ambitiConfig.getValore()));
						detailsConfig.put(ambitiConfig.getChiave(), ambitiConfig.getValore());
						
					}
							

				}
			} catch (NumberFormatException e) {
				  LOGGER.error("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] ERROR " , e);
			} catch (SQLException e) {
				  LOGGER.error("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] ERROR " , e);
			}
    		if(detailsConfig.isEmpty()) {
    			
    			ErrorDTO err = new ErrorDTO("404", "E068", "Verificare la configurazione dell'identita ACTA.", null, null);
            	throw new GenericException(err);
    		}else {
    			if(detailsConfig.size() > 4) {
        			ErrorDTO err = new ErrorDTO("401", "E068", "Verificare la configurazione dell'identita ACTA.", null, null);
                	throw new GenericException(err);
    			}

    		}
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaAndAmbito] END");
	}
	
	public void getConfigurazioneActaFromDBRisca() throws GenericException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getConfigurazioneActaFromDBRisca] BEGIN");
		Map<String, String> mapConfigurazioneActa = null;
		try {
			mapConfigurazioneActa = configurazioneDAO.loadConfigByKeyList(Arrays.asList(Constants.ACTA_CLIENT_APPLICATION_INFO,
					Constants.ACTA_CODICE_FISCALE, Constants.ACTA_REPOSITORY_NAME));
		} catch (Exception e) {
        	throw new GenericException(e);
		}


		for (Map.Entry<String, String> config : mapConfigurazioneActa.entrySet()) {
			if(config.getKey().equals(Constants.ACTA_REPOSITORY_NAME))
				parametriAcaris.setRepositoryName(config.getValue());
			if(config.getKey().equals(Constants.ACTA_CODICE_FISCALE))
				parametriAcaris.setCodiceFiscale(config.getValue());
			if(config.getKey().equals(Constants.ACTA_CLIENT_APPLICATION_INFO))
				parametriAcaris.setAppKey(config.getValue());
			
		}
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getConfigurazioneActaFromDBRisca] END");
	}
	
    public it.csi.risca.riscabesrv.util.actarepository.ObjectIdType getRepository(String repositoryName) throws Exception {
    	LOGGER.debug((Object)"[AcarisProtocolloClient::getRepository] BEGIN");
    	LOGGER.debug("getRepository");
//        if (this.repositoryIdentificato == null) {
            AcarisRepositoryEntryType[] elencoRepository = null;
            LOGGER.debug("prima della chiamata");
            elencoRepository = actaRepositoryServiceHelper.getRepositories();
            LOGGER.debug("dopo la chiamata");
            for (AcarisRepositoryEntryType repository : elencoRepository) {
                if (repository.getRepositoryName().equalsIgnoreCase(repositoryName)){
                    this.repositoryIdentificato = repository.getRepositoryId();
                }
            }
//        }
        LOGGER.debug((Object)"getRepository - END");
        return this.repositoryIdentificato;
    }
    
    public PrincipalExtResponseType[] getPrincipalExt(Long idAoo, Long idStruttura, Long idNodo)  {
    	LOGGER.debug((Object)"getPrincipalExt - BEGIN");
		ObjectIdType repoId = new ObjectIdType();
		repoId.setValue(this.repositoryIdentificato.getValue());
		CodiceFiscaleType idUtente = new CodiceFiscaleType();
		idUtente.setValue(this.parametriAcaris.getCodiceFiscale());
		ClientApplicationInfo clientApplicationInfo = this.getClientApplicationInfo(this.parametriAcaris.getAppKey());
        IdAOOType _idAOO = null;
        if(idAoo != null) {
        	_idAOO = new IdAOOType();
        	_idAOO.setValue(idAoo);
        }
        IdStrutturaType _idStruttura = null;
        if(idStruttura != null) {
        	_idStruttura = new IdStrutturaType();
            _idStruttura.setValue(idStruttura);
        }
        IdNodoType _idNodo = null;
        if(idNodo != null) {
        	_idNodo = new IdNodoType();
            _idNodo.setValue(idNodo);
        }


        try {
        	   PrincipalExtResponseType[] arrPrincipal  = actaBackOfficeServiceHelper.getPrincipalExt(repoId, idUtente, _idAOO, _idStruttura, _idNodo, clientApplicationInfo);
        	return arrPrincipal;
        	
        }
//        catch (it.doqui.acta.acaris.objectservice.AcarisException acEx) {
//            System.err.println("acEx.getMessage(): " + acEx.getMessage());
//            System.err.println("acEx.getFaultInfo().getErrorCode(): " + acEx.getFaultInfo().getErrorCode());
//            System.err.println("acEx.getFaultInfo().getPropertyName(): " + acEx.getFaultInfo().getPropertyName());
//            System.err.println("acEx.getFaultInfo().getObjectId(): " + acEx.getFaultInfo().getObjectId());
//            System.err.println("acEx.getFaultInfo().getExceptionType(): " + acEx.getFaultInfo().getExceptionType());
//            System.err.println("acEx.getFaultInfo().getClassName(): " + acEx.getFaultInfo().getClassName());
//		} 
        catch (Exception e) {
			  LOGGER.error("[DocumentiAllegatiApiServiceImpl::getPrincipalExt] ERROR " , e);
		}
		return null;
    }
    
	private it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType ricercaAllegati (it.doqui.acta.actasrv.dto.acaris.type.common.ObjectIdType repositoryId, it.doqui.acta.actasrv.dto.acaris.type.common.PrincipalIdType principalIdType, it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType propertyFilterType, String dbKeyClassificazione) throws Exception, AcarisException {
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] BEGIN");
		it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType queryObjectType= this.getTarget(Constants.ELENCO_ALLEGATI_A_CLASSIFICAZIONE_PRINCIPAL_VIEW);
		it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] criteria = this.getCriteria(new String[]{"E"}, new String[]{"dbKeyClassificazionePrincipale"},  new String[]{dbKeyClassificazione});
        it.doqui.acta.actasrv.dto.acaris.type.common.PagingResponseType responseAllegati = null;
		try {
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] repo Id --> "+repositoryId.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] principal Id --> "+principalIdType.getValue());
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] Target --> "+queryObjectType.getObject());
			responseAllegati = actaObjectServiceHelper.query(repositoryId, principalIdType, queryObjectType, propertyFilterType, criteria,  null, null, null);
		} catch (Exception e) {
			  LOGGER.error("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] ERROR " , e);
			  throw e;
		}
		LOGGER.debug("[DocumentiAllegatiApiServiceImpl::ricercaAllegati] END");
		return responseAllegati;
	}
    private ClientApplicationInfo getClientApplicationInfo(String appKey) {
    	LOGGER.debug((Object)"getClientApplicationInfo - BEGIN");
        ClientApplicationInfo cai = new ClientApplicationInfo();
        cai.setAppKey(appKey);
        LOGGER.debug((Object)"getClientApplicationInfo - END");
        return cai;
    }
	private it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] getCriteria(String[] queryOperators, String[] propertyName, String[] value){

		LOGGER.debug("getCriteria - BEGIN");
		
		it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[] criteria = null;
		if(	(queryOperators != null && queryOperators.length > 0) && 
			(propertyName != null && propertyName.length > 0) && 
			(value != null && value.length > 0) &&
			(queryOperators.length == propertyName.length && queryOperators.length == value.length)){
			List<it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType> criteri = new ArrayList<it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType>();
			it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType criterio = null;
			for (int i = 0; i < propertyName.length; i++) {
				criterio = new it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType();				
				criterio.setOperator(EnumQueryOperator.EQUALS);
				criterio.setPropertyName(propertyName[i]);
				criterio.setValue(value[i]);
				criteri.add(criterio);
				
			}
			criteria =  criteri.toArray(new it.doqui.acta.actasrv.dto.acaris.type.common.QueryConditionType[0]);
		}
		LOGGER.debug("getCriteria - END");
		return criteria;
	}
	private it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[] getCriteriaOfficialbook(String[] queryOperators, String[] propertyName, String[] value){

		LOGGER.debug("getCriteria - BEGIN");
		
		it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[] criteria = null;
		if(	(queryOperators != null && queryOperators.length > 0) && 
			(propertyName != null && propertyName.length > 0) && 
			(value != null && value.length > 0) &&
			(queryOperators.length == propertyName.length && queryOperators.length == value.length)){
			List<it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType> criteri = new ArrayList<it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType>();
			it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType criterio = null;
			for (int i = 0; i < propertyName.length; i++) {
				criterio = new it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType();				
				criterio.setOperator(this.getQueryOperator(queryOperators[i]));
				criterio.setPropertyName(propertyName[i]);
				criterio.setValue(value[i]);
				criteri.add(criterio);
				
			}
			criteria =  criteri.toArray(new it.csi.risca.riscabesrv.util.actaofficialbook.QueryConditionType[0]);
		}
		LOGGER.debug("getCriteria - END");
		return criteria;
	}
	
    private String getQueryOperator(String queryOperator) {
        if (queryOperator.equals("E")) {
            return "equals";
        }
      
        return null;
    }
    private it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType getTarget(String className) {
    	LOGGER.debug((Object)"getTarget - BEGIN");
        it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType obj = new  it.doqui.acta.actasrv.dto.acaris.type.common.QueryableObjectType();
        obj.setObject(className);
    	LOGGER.debug((Object)"crea Target ----> "+obj.getObject() );
        LOGGER.debug((Object)"getTarget - END");
        return obj;
    }
    
	private it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType getFilterNavigation(String classesName, String[] propertiesName){
		LOGGER.debug("getFilter - BEGIN");
		it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType filter = new it.csi.risca.riscabesrv.util.actanavigation.PropertyFilterType();

		if (propertiesName != null) {
			it.csi.risca.riscabesrv.util.actanavigation.QueryNameType[] names = null;
			List<it.csi.risca.riscabesrv.util.actanavigation.QueryNameType> list = new ArrayList<it.csi.risca.riscabesrv.util.actanavigation.QueryNameType>();
			
			for (int i = 0; i < propertiesName.length; i++) {
				it.csi.risca.riscabesrv.util.actanavigation.QueryNameType obj = new it.csi.risca.riscabesrv.util.actanavigation.QueryNameType();
				obj.setClassName(classesName);
				obj.setPropertyName(propertiesName[i]);
				list.add(obj);
			}
			
			names = list.toArray(new it.csi.risca.riscabesrv.util.actanavigation.QueryNameType[0]);
			filter.setPropertyList(names);
			
		
		}
		
		LOGGER.debug("getFilter - END");
		return filter;
	}
	private it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType getFilterClassificazione(String classesName, String[] propertiesName){
		LOGGER.debug("getFilter - BEGIN");
		it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType filter = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();

		if (propertiesName != null) {
			it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType[] names = null;
			List<it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType> list = new ArrayList<it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType>();
			
			for (int i = 0; i < propertiesName.length; i++) {
				it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType obj = new it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType();
				obj.setClassName(classesName);
				obj.setPropertyName(propertiesName[i]);
				list.add(obj);
			}
			
			names = list.toArray(new it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType[0]);
			filter.setPropertyList(names);
			
		
		}
		
		LOGGER.debug("getFilter - END");
		return filter;
	}
	private it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType getFilter(String classesName, String[] propertiesName){
		LOGGER.debug("getFilter - BEGIN");
		it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType filter = new it.doqui.acta.actasrv.dto.acaris.type.common.PropertyFilterType();
		filter.setFilterType(EnumPropertyFilter.ALL);
		if (propertiesName != null) {
			it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType[] names = null;
			List<it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType> list = new ArrayList<it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType>();
			
			for (int i = 0; i < propertiesName.length; i++) {
				it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType obj = new it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType();
				obj.setClassName(classesName);
				obj.setPropertyName(propertiesName[i]);
				list.add(obj);
			}
			
			names = list.toArray(new it.doqui.acta.actasrv.dto.acaris.type.common.QueryNameType[0]);
			filter.setPropertyList(names);
			
		}
		
		LOGGER.debug("getFilter - END");
		return filter;
	}
	private String creaParoleChiave(String paroleChiave) {
		String result = null;
		if(paroleChiave != null) {
			if(paroleChiave.startsWith("#TITOLO=") && paroleChiave.endsWith("#INTERNO#") ) {
				int indexInizio = paroleChiave.indexOf("=") + 1;
				int indexFine = paroleChiave.indexOf("#",8);
				result = paroleChiave.substring(indexInizio, indexFine);
			}
			if(paroleChiave.startsWith("#TITOLO=")) {
				int indexInizio = paroleChiave.indexOf("=") + 1;
				int indexFine = paroleChiave.indexOf("#",8);
				result = paroleChiave.substring(indexInizio, indexFine);
			}
			if(paroleChiave.startsWith("#INTERNO#TITOLO=")) {
				int indexInizio = paroleChiave.indexOf("=") + 1;
				int indexFine = paroleChiave.indexOf("#",16);
				result = paroleChiave.substring(indexInizio, indexFine);
			}

		}
	
		return result;
	}
	private AllegatiDTO mapAllegatiDTO(it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType[] properties, String idClassificazioni) {
		AllegatiDTO dto = new AllegatiDTO();
		String oggetto= null;
        if(idClassificazioni != null) {
            dto.setIdClassificazione(idClassificazioni);
        }
        for (it.doqui.acta.actasrv.dto.acaris.type.common.PropertyType property : properties) {
      
            if (property.getQueryName().getPropertyName().equals("docConAllegati") && property.getValue().getContentLength() > 0) {
            	String docConAllegati = property.getValue().getContent()[0];
                dto.setDocConAllegati(docConAllegati.equals("S") ? true: false);
            }

            if (property.getQueryName().getPropertyName().equals("rappresentazioneDigitale") &&  property.getValue().getContentLength() > 0) {
              	 if ( property.getValue().getContent()[0].equals("true")) {
            	      	dto.setRappresentazioneDigitale(true);
                 } else {
                   	dto.setRappresentazioneDigitale(false);
                 }
            
            }
            if (property.getQueryName().getPropertyName().equals("oggetto") && property.getValue().getContentLength() > 0) {
            	oggetto =  property.getValue().getContent()[0];
            }

            if (property.getQueryName().getPropertyName().equals("paroleChiave") && property.getValue().getContentLength() > 0) {
            	if( property.getValue().getContent()[0] != null && !"".equals( property.getValue().getContent()[0])) {
	            	String paroleChiave = this.creaParoleChiave( property.getValue().getContent()[0]);
	            	dto.setDescrizione(paroleChiave);
            	}else {
            		dto.setDescrizione(oggetto);
            	}

            }
           
        }
	        if(dto.getDescrizione() == null) {
	    		dto.setDescrizione(oggetto);
	        }
	        return dto;
	}


	
	
	
	private void verificaIdentitaDigitale(Identita identita) {
	     LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaDigitale] BEGIN");
		String ruolo = "";
		Application cod = new Application();
		cod.setId("RISCA");
		Ruolo[] ruoli = serviceHelper.getRuoli(identita, cod);
		if (ruoli != null) {
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaDigitale] ruoli trovati: " + ruoli.length);
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaDigitale] ruolo: " + ruoli[0].getCodiceRuolo());
			ruolo = ruoli[0].getCodiceRuolo();
		}else {
			throw new BusinessException(400, "U003", "Verifica identita digitale fallita");
		}
		ProfilazioneDTO profilazione;
		try {
			profilazione = profilazioneDAO.loadProfilazione(ruolo);
			List<ProfiloOggAppDTO> listProfiloOggettoApp = profilazione.getProfiloOggettoApp().stream().filter(
					p -> p.getFlgAttivo() == true && p.getOggettoApp().getCodOggettoApp().equals("POST_PUT_DEL_RISC"))
					.collect(Collectors.toList());
		     LOGGER.debug("[DocumentiAllegatiApiServiceImpl::verificaIdentitaDigitale] END");
			if (listProfiloOggettoApp.isEmpty()) {
				throw new BusinessException(400, "U003", "Verifica identita digitale fallita");
			}
		
		} catch (Exception e) {
			LOGGER.error("[DocumentiAllegatiApiServiceImpl::verificaIdentitaDigitale] (Exception) ", e);
		}


	}
	
	private Long getIdAmbitoFromIride(Identita identita) {
		Application cod = new Application();
		cod.setId("RISCA");
		UseCase use = new UseCase();
		use.setAppId(cod);
		use.setId("UC_SIPRA");
		Long idAmbito = null;
		if (identita != null) {
			idAmbito = serviceHelper.getInfoPersonaInUseCase(identita, use);
			
			LOGGER.debug("[DocumentiAllegatiApiServiceImpl::getIdAmbitoFromIride] idAmbito:"+ idAmbito);
		}
		return idAmbito;

	}


}