package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipiModalitaPagApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiModalitaPagApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoModalitaPagDTO;

/**
 * The type TipiModalitaPagApiServiceImpl.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiModalitaPagApiServiceImpl extends AbstractApiServiceImpl implements TipiModalitaPagApi {

	private final String className = this.getClass().getSimpleName();
	
	private static final String IDENTITY = "identity";
	
    @Autowired
    private TipiModalitaPagApiServiceHelper tipiModalitaPagApiServiceHelper;

	@Override
	public Response loadAllTipiModalitaPagamenti(HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
        List<TipoModalitaPagDTO> listModalitaPag = new ArrayList<>();
		try {
			listModalitaPag = tipiModalitaPagApiServiceHelper.loadAllTipiModalitaPagamenti(httpHeaders,httpRequest);
        } catch (GenericException e) {
            return handleException(e, className, methodName);
        } catch (ProcessingException e) {
            return handleException(e, className, methodName);
        } finally {
            LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listModalitaPag).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}


}
