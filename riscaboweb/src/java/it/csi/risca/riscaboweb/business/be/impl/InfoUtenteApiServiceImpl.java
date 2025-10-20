package it.csi.risca.riscaboweb.business.be.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.iride2.iridefed.entity.Ruolo;
import it.csi.iride2.policy.entity.Application;
import it.csi.iride2.policy.entity.Identita;
import it.csi.risca.riscaboweb.business.be.InfoUtenteApi;
import it.csi.risca.riscaboweb.business.be.helper.iride.IrideServiceHelper;
import it.csi.risca.riscaboweb.dto.UserInfo;
import it.csi.risca.riscaboweb.filter.IrideIdAdapterFilter;

@Component
public class InfoUtenteApiServiceImpl extends AbstractApiServiceImpl implements InfoUtenteApi {
	
	
    /**
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response
     */

    @Autowired
    IrideServiceHelper serviceHelper;
	

	@Override
	public Response getInfoUtente( HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
        UserInfo userInfo = (UserInfo) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.USERINFO_SESSIONATTR);

        
        Application cod = new Application();
        Identita identita = new Identita();

        identita = (Identita) httpRequest.getSession().getAttribute(IrideIdAdapterFilter.IRIDE_ID_SESSIONATTR);

        cod.setId("RISCA");
        
        Ruolo[] ruoli = serviceHelper.getRuoli(identita, cod);

        if(ruoli != null) {
            LOGGER.debug("[InfoUtenteApiServiceImpl::getInfoUtente] ruoli: " + ruoli);
            LOGGER.debug("[InfoUtenteApiServiceImpl::getInfoUtente] ruoli: " + ruoli[0].getCodiceRuolo());
            userInfo.setRuolo(ruoli[0].getCodiceRuolo());
        }

        return Response.ok(userInfo).build();
	}
}
