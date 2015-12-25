package jaex.rest;

import java.text.MessageFormat;

/**
 * @author Crunchify
 */

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import jaex.service.impl.JaexServiceImpl;

@Path("/recent_purchases")
public class JaexRESTService {

	private static final String CACHE_KEY_PATTERN = "RECENT_PURCHASES_OF_{0}";
	private static final int CACHE_MAX_AGE = 86400; // 1 day

	private JaexServiceImpl jaexService;
	
	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getRecentPurchases(@PathParam("username") String username, @Context Request req) throws Exception {

		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(CACHE_MAX_AGE);
		Response.ResponseBuilder responseBuilder = null;
		EntityTag etag = new EntityTag(MessageFormat.format(CACHE_KEY_PATTERN, username));
		
		responseBuilder = req.evaluatePreconditions(etag);
        if (responseBuilder != null)
        {
            return responseBuilder.cacheControl(cacheControl).tag(etag).build();
        }
        
        jaexService = new JaexServiceImpl();
		String returnedValues = jaexService.getRecentPurchasesJSONForUser(username);
        responseBuilder = Response.ok(returnedValues).cacheControl(cacheControl).tag(etag);
        return responseBuilder.build();
	}

}
