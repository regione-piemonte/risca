package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.TipiElaborazioneApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.TipiElaborazioneApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.TipoElaboraExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Tipi Elaborazione api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class TipiElaborazioneApiServiceImpl extends AbstractApiServiceImpl implements TipiElaborazioneApi {

	private static final String IDENTITY = "identity";
	private final String className = this.getClass().getSimpleName();

	@Autowired
	TipiElaborazioneApiServiceHelper tipiElaborazioneApiServiceHelper;

	@Override
	public Response loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile(String idAmbitoS, String idFunzionalitaS,
			String flgVisibleS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {

		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<TipoElaboraExtendedDTO> listTipoElaborazione = new ArrayList<>();
		try {
			Integer  idAmbito = ValidationFilter.validateParameter("idAmbito", idAmbitoS, 0, Integer.MAX_VALUE);
			Integer  idFunzionalita = ValidationFilter.validateParameter("idFunzionalita", idFunzionalitaS, 0, Integer.MAX_VALUE);
			Integer  flgVisible = ValidationFilter.validateParameter("flgVisible", flgVisibleS, 0, 1);

			listTipoElaborazione = tipiElaborazioneApiServiceHelper
					.loadTipiElaborazioneByIdAmbitoAndFunzionalitaAndFlgVisibile(
							getMultivaluedMapFromHttpHeaders(httpHeaders, httpRequest), idAmbito, idFunzionalita,
							flgVisible);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
			return handleException(e, className, methodName);
		} catch (ProcessingException e) {
			return handleException(e, className, methodName);
		} finally {
			LOGGER.debug(getClassFunctionEndInfo(className, methodName));
		}
		return Response.ok(listTipoElaborazione).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

}
