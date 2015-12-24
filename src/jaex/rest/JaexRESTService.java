package jaex.rest;

import java.util.List;

/**
 * @author Crunchify
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.json.JSONArray;

import jaex.dto.JaexPurchaseWithDetails;
import jaex.dto.JaexUser;
import jaex.service.JSONTransformService;
import jaex.service.JaexService;
import jaex.service.impl.JSONTransformServiceImpl;
import jaex.service.impl.JaexServiceImpl;

@Path("/recent_purchases")
public class JaexRESTService {

	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getRecentPurchases(@PathParam("username") String username) throws Exception {

		JaexService jaexService = new JaexServiceImpl();
		JSONTransformService jsonTransformer = new JSONTransformServiceImpl();

		JaexUser user = jaexService.getUser(username);
		List<JaexPurchaseWithDetails> recentPurchasesForUser = jaexService.getRecentPurchasesForUser(user);
		JSONArray returnedValues = jsonTransformer.getRecentPurchasesJSON(recentPurchasesForUser);

		return Response.status(200).entity(returnedValues.toString()).build();
	}

}
