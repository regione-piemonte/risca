package it.csi.risca.riscaboweb.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ping")
@Produces({ "application/json" })
public interface PingApi  {
   
    @GET
    @Produces({ "application/json" })
    public Response ping( @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );

    @GET
    @Path("/spid")
    public Response testSPID( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/fake/fasi")
    public Response getFasiFake(@QueryParam(value = "protetto") Boolean protetto, @QueryParam(value = "incidenza") Boolean incidenza,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/pdf/{idIstanza}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response testPDF(@PathParam("idIstanza") Long idIstanza,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    @GET
    @Path("/login")
    public Response login( @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

    
//    @GET
//    @Path("/doc/{idIstanza}")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response compilaDoc(@PathParam("idIstanza") Long idIstanza,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

//    @GET
//    @Path("/json/{idIstanza}")
//    @Produces(MediaType.APPLICATION_OCTET_STREAM)
//    public Response testJsonIstanza(@PathParam("idIstanza") Long idIstanza,  @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
}