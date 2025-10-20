package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/tipi-modalita-pagamenti")
@Produces(MediaType.APPLICATION_JSON)
public interface TipiModalitaPagApi {

    /**
     * load All Tipi modalita Pagamenti.
     * @param securityContext SecurityContext
     * @param httpHeaders     HttpHeaders
     * @param httpRequest     HttpServletRequest
     * @return Response response
     */
    @GET
    Response loadAllTipiModalitaPagamenti(@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);


}
