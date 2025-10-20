package it.csi.risca.riscaboweb.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import it.csi.risca.riscaboweb.business.be.exception.ParameterValidationException;
import it.csi.risca.riscaboweb.util.Constants;
import it.csi.risca.riscaboweb.util.validation.ValidateParameters;
import it.csi.risca.riscaboweb.util.validation.ValidatedBigDecimal;
import it.csi.risca.riscaboweb.util.validation.ValidatedParam;

public class ValidationFilter implements Filter {


	
	private static Logger LOGGER = Logger.getLogger(Constants.LOGGER_NAME);
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
//		if (request instanceof HttpServletRequest) {
//			LOGGER.info("ValidationFilter:: doFilter: BEGIN");
//	        HttpServletRequest httpRequest = (HttpServletRequest) request;
//	        String requestURI = httpRequest.getRequestURI();
//			LOGGER.info("requestURI: "+requestURI);
//	        String pathTranslated = httpRequest.getPathTranslated();
//			LOGGER.info("pathTranslated: "+pathTranslated);
//			String pathInfo = httpRequest.getPathInfo();
//			LOGGER.info("pathInfo: "+pathInfo);
//			Class<?> resourceClassName = ResourceClassMapper.getResourceClassName(pathInfo);
//			LOGGER.info("resourceClassName: "+resourceClassName.getSimpleName());
//			
//			if(resourceClassName != null) {
//				Method[] methods = getMethodsForResourceClass(resourceClassName.getSimpleName());
//				for (Method method : methods) {
//					LOGGER.info("methods: "+methods.toString());
//					try {
//				        String methodAndClassPath = getPathFromMethodAndClass(method, resourceClassName);
//						LOGGER.info(methodAndClassPath);
//				        Map<String, String> map = extractPathParameters(methodAndClassPath, pathInfo);
//				        if(map != null) {
//							processPathParamAnnotations(httpRequest, method, methodAndClassPath);
//							processQueryParamAnnotations(httpRequest, method, response);
//				        }
//
//					} catch (ParameterValidationException e) {
//						sendErrorResponse(response, e.getMessage());
//						return; 
//					}
//				}
//			}
//
//		}
//		LOGGER.info("ValidationFilter:: doFilter: END");
//		chain.doFilter(request, response);
	}
//
//	private String getPathFromMethodAndClass(Method method, Class<?> resourceClass) {
//		LOGGER.info("getPathFromMethodAndClass BEGIN");
//	    Path classPathAnnotation = resourceClass.getAnnotation(Path.class);
//	    Path methodPathAnnotation = method.getAnnotation(Path.class);
//	    String classPath = (classPathAnnotation != null) ? classPathAnnotation.value() : "";
//	    String methodPath = (methodPathAnnotation != null) ? methodPathAnnotation.value() : "";
//		LOGGER.info("getPathFromMethodAndClass END");
//	    return classPath.startsWith("/") ? classPath + methodPath :  methodPath;
//	}
//
//
//	private void processPathParamAnnotations(HttpServletRequest request, Method method, String pathInfo) {
//		LOGGER.info(" processPathParamAnnotations pathInfo: "+pathInfo);
//	    ValidateParameters validateParametersAnnotation = method.getAnnotation(ValidateParameters.class);
//	    if (validateParametersAnnotation != null) {
//	        ValidatedParam[] validatedParams = validateParametersAnnotation.value();
//	        Parameter[] parameters = method.getParameters();
//	        for (ValidatedParam validatedParam : validatedParams) {
//	            // Cerca un parametro con lo stesso nome nella lista dei parametri del metodo
//	            Optional<Parameter> matchingParameter = Arrays.stream(parameters)
//	                    .filter(parameter -> parameter.isAnnotationPresent(PathParam.class) &&
//	                            parameter.getAnnotation(PathParam.class).value().equals(validatedParam.name()))
//	                    .findFirst();
//
//	            if (matchingParameter.isPresent()) {
//	                Parameter parameter = matchingParameter.get();
//	                PathParam pathParamAnnotation = parameter.getAnnotation(PathParam.class);
//	                String paramName = pathParamAnnotation.value();
//	        		LOGGER.info("processPathParamAnnotations paramName "+paramName);
//	                String paramValue =  getPathParamValue(request, pathInfo, paramName);
//	        		LOGGER.info("processPathParamAnnotations paramValue "+paramValue);
//	            	if(paramValue != null)
//	            		validateParameter(validatedParam.name(),paramValue,
//	                        validatedParam.minValue(), validatedParam.maxValue());
//	            } else {
//	                // Parametro non trovato
//	                throw new ParameterValidationException("Parametro con nome '" + validatedParam.name() +
//	                        "' non trovato nel metodo.");
//	            }
//	        }
//	    }
//		LOGGER.info("processPathParamAnnotations  ");
//	}
//
//	public static Map<String, String> extractPathParameters(String templatePath, String actualPath) {
//		LOGGER.info("extractPathParameters BEGIN : ");
//	    Map<String, String> pathParameters = new HashMap<>();
//	    String[] templateSegments = templatePath.split("/");
//	    String[] actualSegments = actualPath.split("/");
//	    
//	    if (templateSegments.length != actualSegments.length) {
//	        return null;
//	    }
//
//	    // Copia degli array originali per l'elaborazione
//	    String[] templateSegmentsTest = Arrays.copyOf(templateSegments, templateSegments.length);
//	    String[] actualSegmentsTest = Arrays.copyOf(actualSegments, actualSegments.length);
//
//	    // Rimuovi le parti che iniziano con "{"
//	    for (int i = 0; i < templateSegmentsTest.length; i++) {
//	        if (templateSegmentsTest[i].startsWith("{")) {
//	            templateSegmentsTest[i] = "";
//	            actualSegmentsTest[i] = "";
//	        }
//	    }
//
//	    // Controlla se gli array rimanenti sono uguali
//	    if (Arrays.equals(templateSegmentsTest, actualSegmentsTest)) {
//	        // Recupera i parametri della stringa di percorso
//	        for (int i = 0; i < templateSegments.length; i++) {
//	            String templateSegment = templateSegments[i];
//	            if (templateSegment.startsWith("{") && templateSegment.endsWith("}")) {
//	                String paramName = templateSegment.substring(1, templateSegment.length() - 1);
//	    			LOGGER.info("extractPathParameters paramName:  "+paramName);
//	                String paramValue = actualSegments[i];
//	    			LOGGER.info("extractPathParameters paramValue:  "+paramValue);
//	                pathParameters.put(paramName, paramValue);
//	            }
//	        }
//			LOGGER.info("extractPathParameters END  ");
//	        return pathParameters;
//	    } else {
//			LOGGER.info("extractPathParameters END null  ");
//	        return null;
//	    }
//
//	}
//
//	private String getPathParamValue(HttpServletRequest request, String pathMethod,String paramName ) {
//	    String uriPath = request.getPathInfo();
//		LOGGER.info("getPathParamValue uriPath "+uriPath);
//	    Map<String, String> map = extractPathParameters(pathMethod, uriPath);
//	    if(map != null) {
//			LOGGER.info("getPathParamValue VALUE "+map.get(paramName));
//	    	return map.get(paramName);
//	    }
//		return null;
//	}
//
//
//	private void processQueryParamAnnotations(HttpServletRequest request, Method method, ServletResponse response) {
//		Parameter[] parameters = method.getParameters();
//		for (Parameter parameter : parameters) {
//			QueryParam queryParamAnnotation = parameter.getAnnotation(QueryParam.class);
//			if (queryParamAnnotation != null) {
//				if (parameter.isAnnotationPresent(ValidatedParam.class)) {
//					ValidatedParam validatedParam = parameter.getAnnotation(ValidatedParam.class);
//					String paramName = parameter.getName();
//					String paramValue = request.getParameter(paramName);
//					if(paramValue != null)
//						validateParameter(paramName, paramValue,validatedParam != null ? validatedParam.minValue() : 0,validatedParam != null ? validatedParam.maxValue(): Integer.MAX_VALUE);
//
//				}
//				if (parameter.isAnnotationPresent(ValidatedBigDecimal.class)) {
//					ValidatedBigDecimal validatedParam = parameter.getAnnotation(ValidatedBigDecimal.class);
//					String paramName = parameter.getName();
//					String paramValue = request.getParameter(paramName);
//					if(paramValue != null)
//						validateParameterBigdecimal(paramName, paramValue, validatedParam.minValue(), validatedParam.maxValue());
//				}
//
//			}
//		}
//	}
//
//	private void validateParameterBigdecimal(String paramName, String paramValue, double minValue, double maxValue) {
//		try {
//			BigDecimal decimalValue = new BigDecimal(paramValue);
//			// Esegui la validazione rispetto ai valori minValue e maxValue
//			if (decimalValue.compareTo(new BigDecimal(minValue)) < 0 || decimalValue.compareTo(new BigDecimal(maxValue)) > 0) {
//			    throw new ParameterValidationException(
//			            paramName + " deve essere compreso tra " + minValue + " e " + maxValue);
//			}
//		} catch (NumberFormatException e) {
//			throw new ParameterValidationException(paramName + " deve essere un numero decimale valido");
//		}
//		
//	}
//
//	private Method[] getMethodsForResourceClass(String resourceClassName) {
//		try {
//			Class<?> resourceClass = Class.forName("it.csi.risca.riscaboweb.business.be." + resourceClassName);
//			return resourceClass.getMethods();
//		} catch (ClassNotFoundException e) {
//			LOGGER.info("Class not found per API :"+resourceClassName);
//		}
//		return new Method[0];
//	}
//
	public static Integer validateParameter(String paramName, String paramValue, int minValue, int maxValue) {
		Integer intValue;
		try {
			// Converte il valore del parametro in un numero intero
			 intValue = Integer.parseInt(paramValue);

			// Esegui la validazione rispetto ai valori minValue e maxValue
			if (intValue < minValue || intValue > maxValue) {
				throw new ParameterValidationException(
						paramName + " deve essere compreso tra " + minValue + " e " + maxValue);
			}

		} catch (NumberFormatException e) {
			throw new ParameterValidationException(paramName + " deve essere un numero intero valido");
		}
		return intValue;
	}
//
//	private void sendErrorResponse(ServletResponse response, String errorMessage) throws IOException {
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//		httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//		httpResponse.setContentType("application/json");
//		JSONObject errorObject = new JSONObject();
//		errorObject.put("error", errorMessage);
//		try (PrintWriter writer = httpResponse.getWriter()) {
//			writer.print(errorObject.toString());
//		}
//	}

	@Override
	public void destroy() {
	}
}
