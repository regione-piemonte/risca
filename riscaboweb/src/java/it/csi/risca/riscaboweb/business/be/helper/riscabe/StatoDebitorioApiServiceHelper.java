package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.GenericExceptionList;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AnnualitaSdDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.AttivitaStatoDebitorioDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RataSdDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.RicercaPagamentiDaVisionareDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.StatoDebitorioExtendedDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.UtRimbDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyInvioSpecialeDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.VerifyStatoDebitorioDTO;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class StatoDebitorioApiServiceHelper extends AbstractServiceHelper {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();
	
	private static final String STATI_DEBITORI = "/stati-debitori"; 
	private static final String VERIFY_STATI_DEBITORI = "/_verify_stato_debitorio";
    @Autowired
    private TipoRicercaMorositaApiServiceHelper tipoRicercaMorositaApiServiceHelper;
    
    @Autowired
    private TipoRicercaRimborsiApiServiceHelper tipoRicercaRimborsiApiServiceHelper;
    
	public StatoDebitorioApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response loadAllStatoDebitorioOrByIdRiscossione(Integer idRiscossione, String fruitore, Integer offset,
			Integer limit, String sort,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) throws GenericException {
		
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI, Constants.FRUITORE, fruitore, "idRiscossione", idRiscossione,
        		Constants.OFFSET,offset, Constants.LIMIT, limit, Constants.SORT, sort);
        List<StatoDebitorioExtendedDTO>  result = new ArrayList<>();
    	String paginazione = null;
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            handleResponseErrors(resp);
            if  (resp.getStatus() == 200) {
                GenericType<List<StatoDebitorioExtendedDTO>> dtoType = new GenericType<>() {};
                result = resp.readEntity(dtoType);
    			paginazione = resp.getHeaderString(Constants.PAGINATION_INFO);
            }
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
		return Response.ok(result).header(Constants.PAGINATION_INFO, paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	public StatoDebitorioExtendedDTO loadStatoDebitorioByIdStatoDebitorio(Integer idStatoDebitorio, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI +"/" + idStatoDebitorio, Constants.FRUITORE, fruitore);
		StatoDebitorioExtendedDTO result = new StatoDebitorioExtendedDTO();
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
            handleResponseErrors(resp);
            if  (resp.getStatus() == 200) {
                GenericType<StatoDebitorioExtendedDTO> dtoType = new GenericType<>() {};
                result = resp.readEntity(dtoType);
            }
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
		return result;

	}

	public VerifyStatoDebitorioDTO verifyStatoDebitorio(String modalita, StatoDebitorioExtendedDTO statoDebitorio,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList, ParseException {
		VerifyStatoDebitorioDTO result = new VerifyStatoDebitorioDTO();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+VERIFY_STATI_DEBITORI, "modalita", modalita);
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<StatoDebitorioExtendedDTO> entity = Entity.json(statoDebitorio);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			if (resp.getStatus() >= 400) {
				List<ErrorDTO> err = getErrors(resp);
				LOGGER.error("[StatoDebitorioApiServiceHelper::verifyStatoDebitorio] SERVER EXCEPTION : " + err);
				throw new GenericExceptionList(err);
			}
			GenericType<VerifyStatoDebitorioDTO> verifyStatoDebitorioType = new GenericType<>() {
			};
			result = resp.readEntity(verifyStatoDebitorioType);
        } catch (ProcessingException e) {
            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
            throw e;
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
		return result;
	}

	public StatoDebitorioExtendedDTO updateStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio,
			 String fruitore, String flgUpDataScadenza, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericExceptionList {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		StatoDebitorioExtendedDTO result = new StatoDebitorioExtendedDTO();

        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI, Constants.FRUITORE, fruitore,
        		"flgUpDataScadenza",flgUpDataScadenza);
        try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<StatoDebitorioExtendedDTO> entity = Entity.json(statoDebitorio);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
			if (resp.getStatus() >= 400) {
				List<ErrorDTO> err = getErrors(resp);
				LOGGER.error("[StatoDebitorioApiServiceHelper::updateStatoDebitorio] SERVER EXCEPTION : " + err);
				throw new GenericExceptionList(err);
			}
			GenericType<StatoDebitorioExtendedDTO> statoDebitorioExtendedDTO = new GenericType<>() {
			};
		 	result = resp.readEntity(statoDebitorioExtendedDTO);
		   } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
	}

	public StatoDebitorioExtendedDTO saveStatoDebitorio(StatoDebitorioExtendedDTO statoDebitorio, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericExceptionList {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        StatoDebitorioExtendedDTO result = new StatoDebitorioExtendedDTO();

        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI,Constants.FRUITORE, fruitore);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<StatoDebitorioExtendedDTO> entity = Entity.json(statoDebitorio);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			if (resp.getStatus() >= 400) {
				List<ErrorDTO> err = getErrors(resp);
				LOGGER.error("[StatoDebitorioApiServiceHelper::saveStatoDebitorio] SERVER EXCEPTION : " + err);
				throw new GenericExceptionList(err);
			}
			GenericType<StatoDebitorioExtendedDTO> statoDebitorioExtendedDTO = new GenericType<>() {
			};
			result = resp.readEntity(statoDebitorioExtendedDTO);
		 } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
	}

	public List<UtRimbDTO> loadStatoDebitorioByIdRimborso(Integer idRimborso, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<UtRimbDTO> result = new ArrayList<>();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI+"/idRimborso/"+idRimborso,Constants.FRUITORE, fruitore);
		try {
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
	         handleResponseErrors(resp);
			GenericType<List<UtRimbDTO>> ListUtRimbDTO = new GenericType<>() {};
			result = resp.readEntity(ListUtRimbDTO);
		 } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
	}

	public Response loadStatoDebitorioByNap(String nap, String fruitore, List<Integer> sdDaEscludere, Integer offset, Integer limit, String sort,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
        String targetUrl = Utils.buildTargetUrl(this.endpointBase+STATI_DEBITORI+"/nap/" + nap , Constants.FRUITORE, fruitore, Constants.OFFSET,offset, Constants.LIMIT, limit, Constants.SORT, sort);
		String paginazione = null;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<List<Integer>> entity = Entity.json(sdDaEscludere);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
	         handleResponseErrors(resp);
			GenericType<List<StatoDebitorioExtendedDTO>> ListStatoDebitorioExtendedDTO = new GenericType<>() {};
			result = resp.readEntity(ListStatoDebitorioExtendedDTO);
			paginazione = resp.getHeaderString(Constants.PAGINATION_INFO);
		 } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(result).header(Constants.PAGINATION_INFO, paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	public VerifyInvioSpecialeDTO verifyStatoDebitorioInvioSpeciale(Integer idRiscossione, Integer idStatoDebitorio,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		VerifyInvioSpecialeDTO result = new VerifyInvioSpecialeDTO();
		StatoDebitorioExtendedDTO statoDebitorio = new StatoDebitorioExtendedDTO();
		statoDebitorio.setAnnualitaSd(new ArrayList<AnnualitaSdDTO>());
		statoDebitorio.setRate(new ArrayList<RataSdDTO>());
		statoDebitorio.setIdRiscossione(Long.valueOf(idRiscossione));
		String baseUrl = this.endpointBase +"/_verify_stato_debitorio_invio_speciale/idRiscossione/"+idRiscossione;
        String targetUrl = Utils.buildTargetUrl(baseUrl,"idStatoDebitorio",idStatoDebitorio);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Response resp = getInvocationBuilder(targetUrl, map).get();
	        handleResponseErrors(resp);
			Integer countStatoDebitorioInvioSpeciale = resp.readEntity(Integer.class);
			if(countStatoDebitorioInvioSpeciale > 0 ) {
				result.setExist(true);
			}else {
				result.setExist(false);
			}

		 } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
	}

	public Response loadStatoDebitorioByCodUtenza(String codUtenza,	  String fruitore,List<Integer> sdDaEscludere, Integer offset, Integer limit,
			String sort,  HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		String baseUrl = this.endpointBase + "/stati-debitori/codUtenza/" + codUtenza;
        String targetUrl = Utils.buildTargetUrl(baseUrl, Constants.FRUITORE, fruitore,	Constants.OFFSET,offset, Constants.LIMIT, limit, Constants.SORT, sort);
		String paginazione = null;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<List<Integer>> entity = Entity.json(sdDaEscludere);
            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			handleResponseErrors(resp);
			GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<>() {
			};
			result = resp.readEntity(listStatoDebitorioExtendedDTO);
			paginazione = resp.getHeaderString(Constants.PAGINATION_INFO);
		    } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return Response.ok(result).header(Constants.PAGINATION_INFO, paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
				.build();
	}

	public List<StatoDebitorioExtendedDTO> updateAttivitaStatoDebitorio(List<StatoDebitorioExtendedDTO> statoDebitorio,
			String fruitore, String idAttivitaSD, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/stati_debitori/attivita");

		if (fruitore != null) {
			targetUrlBuilder.append("?").append("fruitore=").append(fruitore);
		}

		if (idAttivitaSD != null) {
			targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?").append("idAttivitaSD=")
					.append(idAttivitaSD);
		}

		String targetUrl = targetUrlBuilder.toString();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<List<StatoDebitorioExtendedDTO>> entity = Entity.json(statoDebitorio);
			Response resp = getInvocationBuilder(targetUrl, map).put(entity);

			handleResponseErrors(resp);
			GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<List<StatoDebitorioExtendedDTO>>() {
			};
			result = resp.readEntity(listStatoDebitorioExtendedDTO);
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName + "Errore nella chiamata :" + targetUrl, e),
					e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return result;
	}

	public List<StatoDebitorioExtendedDTO> updateAttivitaForAllSDByFilter(AttivitaStatoDebitorioDTO attivitaStatoDeb,
			String tipoRicercaMorosita, Integer anno, Integer flgRest, Integer flgAnn, String lim, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		String idAttivitaSD =  attivitaStatoDeb != null ? attivitaStatoDeb.getIdAttivitaStatoDeb().toString() : null;
		StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/stati_debitori/attivita");
		 if (fruitore != null) {
		     targetUrlBuilder.append("?")
		                    .append("fruitore=").append(fruitore);
		 }

		if (idAttivitaSD != null) {
		    targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
		                   .append("idAttivitaSD=").append(idAttivitaSD);
		}

		String targetUrl = targetUrlBuilder.toString();

		try {
			 Response respListStatoDebitorio  =  tipoRicercaMorositaApiServiceHelper.ricercaMorosita(fruitore, tipoRicercaMorosita, anno, flgRest, flgAnn, lim, null, null, null,  httpHeaders, httpRequest);		
			GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<List<StatoDebitorioExtendedDTO>>() {};
		    List<StatoDebitorioExtendedDTO> listStatoDebitorio = respListStatoDebitorio.readEntity(listStatoDebitorioExtendedDTO);
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<List<StatoDebitorioExtendedDTO>> entity = Entity.json(listStatoDebitorio);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	         handleResponseErrors(resp);
			result = resp.readEntity(listStatoDebitorioExtendedDTO);
	    	 } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName+ "Errore nella chiamata :" +targetUrl,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
		

	}

	public List<StatoDebitorioExtendedDTO> updateAttivitaForAllSDByFilterRimborsi(
			AttivitaStatoDebitorioDTO attivitaStatoDeb, String tipoRicercaRimborsi, Integer anno, String fruitore,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		String idAttivitaSD =  attivitaStatoDeb != null ? attivitaStatoDeb.getIdAttivitaStatoDeb().toString() : null;
		StringBuilder targetUrlBuilder = new StringBuilder(this.endpointBase + "/stati_debitori/attivita");
		 if (fruitore != null) {
		     targetUrlBuilder.append("?")
		                    .append("fruitore=").append(fruitore);
		 }
		if (idAttivitaSD != null) {
		    targetUrlBuilder.append(targetUrlBuilder.toString().contains("?") ? "&" : "?")
		                   .append("idAttivitaSD=").append(idAttivitaSD);
		}

		String targetUrl = targetUrlBuilder.toString();

		try {
			 Response respListStatoDebitorio  =  tipoRicercaRimborsiApiServiceHelper.ricercaRimborsi(fruitore, tipoRicercaRimborsi, anno, null, null, null,  httpHeaders, httpRequest);
			GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<List<StatoDebitorioExtendedDTO>>() {};
		    List<StatoDebitorioExtendedDTO> listStatoDebitorio = respListStatoDebitorio.readEntity(listStatoDebitorioExtendedDTO);
            MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
            Entity<List<StatoDebitorioExtendedDTO>> entity = Entity.json(listStatoDebitorio);
            Response resp = getInvocationBuilder(targetUrl, map).put(entity);
	         handleResponseErrors(resp);
			result = resp.readEntity(listStatoDebitorioExtendedDTO);
		    } catch (ProcessingException e) {
	            LOGGER.error(getClassFunctionErrorInfo(className, methodName+ "Errore nella chiamata :" +targetUrl,e) ,e);
	            throw e;
	        } finally {
	            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
	        }
		return result;
		
	}

	public BigDecimal sommaAllCanoneDovutoByNap(String nap, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		BigDecimal result = null;
		String baseUrl = this.endpointBase + "/stati-debitori/nap/" + nap + "/canone-dovuto";
		String targetUrl = Utils.buildTargetUrl(baseUrl);
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			handleResponseErrors(resp);
			GenericType<BigDecimal> entity = new GenericType<>() {
			};
			result = resp.readEntity(entity);
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return result;
	}

	public Response loadStatoDebitorioByNumPratica(String numPratica, String calcolaInteressi, String fruitore,
			List<Integer> sdDaEscludere, BigDecimal importoDa, BigDecimal importoA, String titolare, String flgPratica,
			Integer offset, Integer limit, String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws GenericException {
		   String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
	        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
			List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
			String baseUrl = this.endpointBase + "/stati-debitori/numPratica/" + numPratica;
	        String targetUrl = Utils.buildTargetUrl(baseUrl,"calcolaInteressi", calcolaInteressi, 
	        		Constants.FRUITORE, fruitore, "importoDa", importoDa, "importoA", importoA, "titolare", titolare,
	        		"flgPratica", flgPratica, Constants.OFFSET, offset, Constants.LIMIT, limit, Constants.SORT, sort);
			String paginazione = null;
			try {
				MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
				Entity<List<Integer>> entity = Entity.json(sdDaEscludere);
	            Response resp = getInvocationBuilder(targetUrl, map).post(entity);
				handleResponseErrors(resp);
				GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<>() {
				};
				result = resp.readEntity(listStatoDebitorioExtendedDTO);
				paginazione = resp.getHeaderString(Constants.PAGINATION_INFO);
			    } catch (ProcessingException e) {
		            LOGGER.error(getClassFunctionErrorInfo(className, methodName,e),e);
		            throw e;
		        } finally {
		            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		        }
			return Response.ok(result).header(Constants.PAGINATION_INFO, paginazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY)
					.build();
	}

	public Response loadStatiDebitoriPerPagamentiDaVisionare(
			RicercaPagamentiDaVisionareDTO ricercaPagamentiDaVisionareDTO, String fruitore, Integer offset,
			Integer limit, String sort, HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<StatoDebitorioExtendedDTO> result = new ArrayList<>();
		String baseUrl = this.endpointBase + "/stati-debitori/pagamenti-da-visionare";
		String targetUrl = Utils.buildTargetUrl(baseUrl, Constants.FRUITORE, fruitore, Constants.OFFSET, offset,
				Constants.LIMIT, limit, Constants.SORT, sort);
		String paginazione = null;
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Entity<RicercaPagamentiDaVisionareDTO> entity = Entity.json(ricercaPagamentiDaVisionareDTO);
			Response resp = getInvocationBuilder(targetUrl, map).post(entity);
			handleResponseErrors(resp);
			GenericType<List<StatoDebitorioExtendedDTO>> listStatoDebitorioExtendedDTO = new GenericType<>() {
			};
			result = resp.readEntity(listStatoDebitorioExtendedDTO);
			paginazione = resp.getHeaderString(Constants.PAGINATION_INFO);
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e), e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(Constants.PAGINATION_INFO, paginazione)
				.header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
}
