package it.csi.risca.riscaboweb.business.be.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.risca.riscaboweb.business.be.PagamentoApi;
import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.PagamentoApiServiceHelper;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.PagamentoDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.PagamentoExtendedDTO;
import it.csi.risca.riscaboweb.filter.ValidationFilter;

/**
 * The type Pagamento api service.
 *
 * @author CSI PIEMONTE
 */
@Component
public class PagamentoApiServiceImpl  extends AbstractApiServiceImpl implements PagamentoApi {

	private static final String IDENTITY = "identity";
	 private final String className = this.getClass().getSimpleName();

	@Autowired
	private PagamentoApiServiceHelper pagamentoHelper;

	/**
	 * @param securityContext SecurityContext
	 * @param httpHeaders     HttpHeaders
	 * @param httpRequest     HttpServletRequest
	 * @return Response
	 */


	@Override
	public Response getPagamentoWithMaxDataOpVal(String fruitore, String idStatoDebitorioS,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		PagamentoDTO pagamento = new PagamentoDTO();
		try {
			Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
			pagamento = pagamentoHelper.getPagamentoWithMaxDataOpVal(fruitore, idStatoDebitorio, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();		
	}

	@Override
	public Response getPagamentoByIdRiscossione(String fruitore,String idRiscossioneS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 List<PagamentoExtendedDTO> pagamento = new ArrayList<>();
		try {
			Integer  idRiscossione = ValidationFilter.validateParameter("idRiscossione", idRiscossioneS, 0, Integer.MAX_VALUE);
			pagamento = pagamentoHelper.getPagamentoByIdRiscossione(fruitore, idRiscossione, httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response savePagamento(PagamentoExtendedDTO pagamentoExtendedDTO,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			pagamento = pagamentoHelper.savePagamento(pagamentoExtendedDTO, fruitore,  httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}
	

	@Override
	public Response updatePagamento(PagamentoExtendedDTO pagamentoExtendedDTO,String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			pagamento = pagamentoHelper.updatePagamento(pagamentoExtendedDTO, fruitore,  httpHeaders, httpRequest);
        } catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response deleteByIdPagamento(String idPagamentoS,String fruitore,  HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 try {
			Integer  idPagamento = ValidationFilter.validateParameter("idPagamento", idPagamentoS, 0, Integer.MAX_VALUE);
			idPagamentoS = pagamentoHelper.deleteByIdPagamento(idPagamento,fruitore,  httpHeaders, httpRequest);
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(idPagamentoS).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentiByIdStatoDebitorio(String fruitore, String idStatoDebitorioS, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		  String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		List<PagamentoExtendedDTO>   listPagamenti = new ArrayList<>();
		 try {
				Integer  idStatoDebitorio = ValidationFilter.validateParameter("idStatoDebitorio", idStatoDebitorioS, 0, Integer.MAX_VALUE);
	        	listPagamenti = pagamentoHelper.getPagamentiByIdStatoDebitorio(fruitore, idStatoDebitorio,  httpHeaders, httpRequest);
			} catch (ParameterValidationException e) {
	        	return handleException(e, className, methodName);   

	        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(listPagamenti).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentoByIdPagamento(String idPagamentoS, String fruitore, 
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		 PagamentoExtendedDTO pagamento = new PagamentoExtendedDTO();
		try {
			Integer  idPagamento = ValidationFilter.validateParameter("idPagamento", idPagamentoS, 0, Integer.MAX_VALUE);

			pagamento = pagamentoHelper.getPagamentoByIdPagamento(idPagamento, fruitore,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
        return Response.ok(pagamento).header(HttpHeaders.CONTENT_ENCODING, IDENTITY).build();
	}

	@Override
	public Response getPagamentiDaVisionare(String fruitore, String offsets,
			 String limits, String sort,
			 HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		 String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
		 LOGGER.debug(getClassFunctionBeginInfo(className, methodName));
		try {
			Integer  limit = ValidationFilter.validateParameter("limits", limits, 0,Integer.MAX_VALUE);
			Integer  offset = ValidationFilter.validateParameter("offset", offsets, 0, Integer.MAX_VALUE);
			return  pagamentoHelper.getPagamentiDaVisionare(fruitore ,offset, limit, sort,  httpHeaders, httpRequest);
		} catch (ParameterValidationException e) {
        	return handleException(e, className, methodName);   

        }  catch (GenericException e) {
        	return handleException(e, className, methodName);   
        } catch (ProcessingException e) {
        	return handleException(e, className, methodName);   
        } finally {
        	LOGGER.debug(getClassFunctionEndInfo(className, methodName));
        }
	}

}
