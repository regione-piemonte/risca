package it.csi.risca.riscaboweb.business.be.helper;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import it.csi.risca.riscaboweb.business.be.exception.GenericException;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorDTO;
import it.csi.risca.riscaboweb.business.be.helper.riscabe.dto.ErrorObjectDTO;
import it.csi.risca.riscaboweb.business.be.impl.AbstractApiServiceImpl;
import it.csi.risca.riscaboweb.util.Constants;

public abstract class AbstractServiceHelper extends AbstractApiServiceImpl{

    public static final Logger LOGGER = Logger.getLogger(Constants.COMPONENT_NAME + ".service");
    
    protected String hostname = null;
    protected String hostnameNodo = null;
    protected String endpointBase = null;

    protected String headerAttoreGestione = null;

    public String getHostnameNodo() {
		return hostnameNodo;
	}

	public void setHostnameNodo(String hostnameNodo) {
		this.hostnameNodo = hostnameNodo;
	}

	public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getEndpointBase() {
        return endpointBase;
    }

    public void setEndpointBase(String endpointBase) {
        this.endpointBase = endpointBase;
    }

    public String getHeaderAttoreGestione() { return headerAttoreGestione; }

    public void setHeaderAttoreGestione(String headerAttoreGestione) { this.headerAttoreGestione = headerAttoreGestione; }

    protected String getClassFunctionBeginInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "BEGIN");
    }

    protected String getClassFunctionEndInfo(String className, String methodName) {
        return getClassFunctionDebugString(className, methodName, "END");
    }

    protected String getClassFunctionErrorInfo(String className, String methodName, Object error) {
        return getClassFunctionDebugString(className, methodName, "ERROR : " + error);
    }

    protected String getClassFunctionDebugString(String className, String methodName, String info) {
        String functionIdentity = "[" + className + "::" + methodName + "] ";
        return functionIdentity + info;
    }

    public static Invocation.Builder getInvocationBuilder(String targetUrl, MultivaluedMap<String, Object> requestHeaders) {
        Client client =  ClientBuilder.newBuilder().build();
        WebTarget target = client.target(targetUrl);
        return requestHeaders!=null ? target.request().headers(requestHeaders) : target.request();
    }
    
//    public static Invocation.Builder getInvocationBuilderUser(String targetUrl, MultivaluedMap<String, Object> requestHeaders, Identita identita) {
//        Client client =  ClientBuilder.newBuilder().build();
//        WebTarget target = client.target(targetUrl);
//        return requestHeaders!=null ? target.request().headers(requestHeaders) : target.request();
//    }

    public static ErrorDTO getError(Response response) {
        ErrorDTO err = new ErrorDTO();
        GenericType<ErrorDTO> errGenericType = new GenericType<ErrorDTO>() {};
        try {
            err = response.readEntity(errGenericType);
        }
        catch (Exception e) {
            err.setStatus(String.valueOf(response.getStatus()));
            err.setCode("E005");
            err.setTitle("Errore inatteso dal server");

        }
        return err;
    }

    public static ErrorObjectDTO getErrorObjectDTO(Response response) {
    	ErrorObjectDTO err = new ErrorObjectDTO();
        GenericType<ErrorObjectDTO> errGenericType = new GenericType<ErrorObjectDTO>() {};
        try {
            err = response.readEntity(errGenericType);
        }
        catch (Exception e) {
            return null;
        }
        return err;
    }
    
    public static List<ErrorDTO> getErrors(Response response) {
        List<ErrorDTO> err = new ArrayList<>();
        ErrorDTO singleError = new ErrorDTO();
        GenericType<List<ErrorDTO>> errGenericType = new GenericType<List<ErrorDTO>>() {};
        try {
            err = response.readEntity(errGenericType);
        }
        catch (Exception e) {
        	singleError.setStatus(String.valueOf(response.getStatus()));
        	singleError.setCode("E005");
        	singleError.setTitle("Errore inatteso dal server");
        	err.add(singleError);
        }
        return err;
    }
    public void handleResponseErrors(Response response) throws GenericException {
        if (response.getStatus() >= 400) {
            ErrorDTO err = getError(response);
            LOGGER.debug("SERVER EXCEPTION : " + err);
            throw new GenericException(err);
        }
    }
    
}