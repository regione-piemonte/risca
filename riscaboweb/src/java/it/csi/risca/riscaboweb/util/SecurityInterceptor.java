package it.csi.risca.riscaboweb.util;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import it.csi.risca.riscaboweb.dto.UserInfo;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

/**
 * This interceptor verify the access permissions for a user 
 * based on username and passowrd provided in request
 * */
@Provider
@ServerInterceptor
public class SecurityInterceptor implements PreProcessInterceptor
{

	private static final ServerResponse ACCESS_DENIED = new ServerResponse("Accesso negato a questa risorsa", 401, new Headers<Object>());;
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Nessuno puo' accedere a questa risorsa", 403, new Headers<Object>());;

	@Context
	private HttpServletRequest servletReq;
	
	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethodInvoker methodInvoked) throws Failure, WebApplicationException
	{
		Method method = methodInvoked.getMethod();
		
		//Access allowed for all 
		if(method.isAnnotationPresent(PermitAll.class))
		{
			return null;
		}
		//Access denied for all 
		if(method.isAnnotationPresent(DenyAll.class))
		{
			return ACCESS_FORBIDDEN;
		}

		UserInfo userInfo = (UserInfo) servletReq.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);
		String ruoloInArrivo = userInfo.getRuolo();
		

	    //Verify user access
		if(method.isAnnotationPresent(RolesAllowed.class))
		{
			RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
			Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
			
			//Is user valid?
			if(!isUserAllowed(rolesSet, ruoloInArrivo))
			{
				return ACCESS_DENIED;
			}
		}
		
		//Return null to continue request processing
		return null;
	}

	private boolean isUserAllowed(final Set<String> rolesSet, String ruoloInArrivo) 
	{
		boolean isAllowed = false;
		
		//Verify user role
		if(rolesSet.contains(ruoloInArrivo))
		{
			isAllowed = true;
		}
		return isAllowed;
	}

	
}
