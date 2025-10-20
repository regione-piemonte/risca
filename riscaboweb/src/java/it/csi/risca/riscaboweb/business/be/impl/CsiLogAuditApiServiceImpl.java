package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.CsiLogAuditApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.CsiLogAuditApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.CsiLogAuditDTO;

/**
 * The type Csi Log Audit Api Service Impl.
 *
 * @author CSI PIEMONTE
 */

@Component
public class CsiLogAuditApiServiceImpl extends AbstractApiServiceImpl implements CsiLogAuditApi {

	private final String className = this.getClass().getSimpleName();

	@Autowired
	private CsiLogAuditApiServiceHelper csiLogAuditApiServiceHelper;

	@Override
	public Response saveCsiLogAudit(CsiLogAuditDTO csiLogAudit, 
			javax.ws.rs.core.HttpHeaders httpHeaders, HttpServletRequest httpRequest) {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		CsiLogAuditDTO csiLogAuditDTO = new CsiLogAuditDTO();
		try {
			csiLogAuditDTO = csiLogAuditApiServiceHelper
					.saveCsiLogAudit((getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest)), csiLogAudit);
		} catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}

		return Response.ok(csiLogAuditDTO).status(201).header(HttpHeaders.CONTENT_ENCODING, "identity").build();

	}

}
