/*******************************************************************************
 *  ========================LICENSE_START=================================
 *   
 *  Copyright (C) 2025 Regione Piemonte
 *   
 *  SPDX-FileCopyrightText: (C) Copyright 2025  Regione Piemonte
 *  SPDX-License-Identifier: EUPL-1.2
 *  =========================LICENSE_END==================================
 *******************************************************************************/
package it.csi.risca.riscabesrv.business.be;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RequestBody;

import it.csi.risca.riscabesrv.dto.RiscossioneSearchDTO;
import it.csi.risca.riscabesrv.dto.RpNonPremarcatiDTO;
import it.csi.risca.riscabesrv.dto.soris.FileSorisDTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.RuoloSorisFr7DTO;
import it.csi.risca.riscabesrv.dto.soris.Soris00CDTO;
import it.csi.risca.riscabesrv.dto.soris.Soris99CDTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr0DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr1DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr3DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisFr7DTO;
import it.csi.risca.riscabesrv.dto.soris.SorisTotDTO;

/**
 * The interface Soris api.
 *
 * @author CSI PIEMONTE
 */
@Path("/soris")
@Produces(MediaType.APPLICATION_JSON)
public interface SorisApi {

	@Path("/save-file")
	@POST
	Response saveFileSoris(@RequestBody FileSorisDTO fileSoris, @QueryParam("fruitore") String fruitore, @Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	@Path("/save-soris-tot")
	@POST
	Response saveSorisTot(@RequestBody SorisTotDTO sorisTot,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/soris-working-00c")
	@POST
	Response saveSorisWorking00C(@RequestBody Soris00CDTO soris00C,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@Path("/soris-working-fr0")
	@POST
	Response saveSorisWorkingFr0(@RequestBody SorisFr0DTO sorisFr0,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/soris-working-fr1")
	@POST
	Response saveSorisWorkingFr1(@RequestBody SorisFr1DTO sorisFr1,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/soris-working-fr3")
	@POST
	Response saveSorisWorkingFr3(@RequestBody SorisFr3DTO sorisFr3,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/soris-working-fr7")
	@POST
	Response saveSorisWorkingFr7(@RequestBody SorisFr7DTO sorisFr7,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/soris-working-99c")
	@POST
	Response saveSorisWorking99C(@RequestBody Soris99CDTO soris99C,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);

	@Path("/soris-working-fr1")
	@GET
	Response getSorisWorkingFr1(@Context SecurityContext securityContext,
			@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	
	@Path("/ruolo-soris-fr1")
	@POST
	Response inserisciRuoloSorisFr1(@RequestBody RuoloSorisFr1DTO ruoloSorisFr1,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/ruolo-soris-fr3")
	@POST
	Response inserisciRuoloSorisFr3(@RequestBody RuoloSorisFr3DTO ruoloSorisFr3,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	@Path("/ruolo-soris-fr3-stato-debitorio")
	@GET
	Response getEstrazioneDatiSorisPerInsertPagamenti(@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
			@QueryParam("idFileSoris") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idFileSoris,
			@Context SecurityContext securityContext,@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	
	
	@Path("/ruolo-soris-fr3")
	@PUT
	Response aggiornaRuoloSorisFr3(@QueryParam("fruitore") String fruitore, @RequestBody RuoloSorisFr3DTO ruoloSorisFr3,
			@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
			@Context HttpServletRequest httpRequest);
	
	  @GET
	  @Path("/file_soris/_count")
	  Response countFileSoris(@NotNull @QueryParam("nomeFileSoris") String nomeFileSoris, @QueryParam("fruitore") String fruitore,
	    	 @Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);

	  @Path("/ruolo-soris-fr7")
	  @POST
	  Response inserisciRuoloSorisFr7(@RequestBody RuoloSorisFr7DTO ruoloSorisFr7,
				@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders,
				@Context HttpServletRequest httpRequest); 
	  
	  
	  @Path("/ruolo-soris-fr7-stato-debitorio")
		@GET
		Response getEstrazioneDatiSorisPerInsertAnnullamenti(@QueryParam("idElabora") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idElabora,
				@QueryParam("idFileSoris") @Min(value = 1, message = "L'id deve essere maggiore di 0")  @Max(value = Integer.MAX_VALUE, message = "L'id deve essere minore o uguale a max value integer")  Long idFileSoris,
				@Context SecurityContext securityContext,@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
		
	  
	  @Path("/delete-working")
		@DELETE
		Response deleteWorkingSoris(@Context SecurityContext securityContext,@Context HttpHeaders httpHeaders, @Context HttpServletRequest httpRequest);
	  
}
