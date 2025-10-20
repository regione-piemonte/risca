package it.csi.risca.riscaboweb.business.be.helper.riscabe;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.AbstractServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.DettaglioPagSearchResultDTO;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.Utils;

public class DettaglioPagSearchResultApiServiceHelper extends AbstractServiceHelper {

	private final String className = this.getClass().getSimpleName();
	private static final String IDENTITY = "identity";
	
	private static final String DETTAGLIO_PAG_SEARCH_RESULT = "/dettaglio-pag-search-result/idPagamento/";

	public DettaglioPagSearchResultApiServiceHelper(String hostname, String endpointBase) {
		this.hostname = hostname;
		this.endpointBase = hostname + endpointBase;
	}

	public Response getDettaglioPagSearchResultByIdPagamento(String fruitore, Integer idPagamento,
			 HttpHeaders httpHeaders, HttpServletRequest httpRequest)
			throws GenericException {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		String targetUrl = Utils.buildTargetUrl(this.endpointBase + DETTAGLIO_PAG_SEARCH_RESULT + idPagamento,
				Constants.FRUITORE, fruitore);
		List<DettaglioPagSearchResultDTO> result = new ArrayList<>();
		try {
			MultivaluedMap<String, Object> map = getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest);
			Response resp = getInvocationBuilder(targetUrl, map).get();
			handleResponseErrors(resp);
			if (resp.getStatus() == 200) {
				GenericType<List<DettaglioPagSearchResultDTO>> dtoType = new GenericType<>() {};
				result = resp.readEntity(dtoType);
			}
		} catch (ProcessingException e) {
			LOGGER.error(getClassFunctionErrorInfo(className, methodName, e),e);
			throw e;
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(result).header(HttpHeaders.CONTENT_ENCODING,IDENTITY).build();
	}

}
