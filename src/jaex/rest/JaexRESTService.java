package jaex.rest;

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

import jaex.service.JaexService;
import jaex.service.impl.JaexServiceImpl;

@Path("/recent_purchases")
public class JaexRESTService {

//	private static final int TIME_TO_LIVE_IN_SECONDS = 6000;
//	private static final int TIMER_INTERVAL_IN_SECONDS = 50;
//	private static final int MAX_ITEMS = 20;
//
//	private JaexInMemoryCache<String, String> cache = new JaexInMemoryCache<String, String>(TIME_TO_LIVE_IN_SECONDS,
//			TIMER_INTERVAL_IN_SECONDS, MAX_ITEMS);

	// TODO
	// x all functionalities in service
	// x put them together for final functionality
	// x build JSON
	// x create API functionality
	// - chaching

	@Path("{username}")
	@GET
	@Produces("application/json")
	public Response getRecentPurchases(@PathParam("username") String username, @Context Request req) throws Exception {

		JaexService jaexService = new JaexServiceImpl();
		
		CacheControl cacheControl = new CacheControl();
		cacheControl.setMaxAge(86400);
		Response.ResponseBuilder responseBuilder = null;

		// Calculate the ETag on last modified date of user resource
		EntityTag etag = new EntityTag(username);

		// Verify if it matched with etag available in http request
		responseBuilder = req.evaluatePreconditions(etag);
		
		 //If ETag matches the rb will be non-null;
        //Use the rb to return the response without any further processing
        if (responseBuilder != null)
        {
        	System.out.println("cache found!");
            return responseBuilder.cacheControl(cacheControl).tag(etag).build();
        }
        
        System.out.println("cache NOT found");
        
        String returnedValues = jaexService.getRecentPurchasesJSONForUser(username);
        //If rb is null then either it is first time request; or resource is modified
        //Get the updated representation and return with Etag attached to it
        responseBuilder = Response.ok(returnedValues).cacheControl(cacheControl).tag(etag);
        return responseBuilder.build();

		// asdasdasda

//		String cachedEntryForUsername = cache.get(username);
//		if (cachedEntryForUsername != null) {
//			System.out.println("Got something from the cache!");
//			return Response.status(200).entity(cachedEntryForUsername).build();
//		}
//
//		System.out.println("Cache is empty");
////		JaexService jaexService = new JaexServiceImpl();
////		JSONTransformService jsonTransformer = new JSONTransformServiceImpl();
//
//		JaexUser user = jaexService.getUser(username);
//		List<JaexPurchaseWithDetails> recentPurchasesForUser = jaexService.getRecentPurchasesForUser(user);
//		String returnedValues = jsonTransformer.getRecentPurchasesJSON(recentPurchasesForUser).toString();
//
//		cache.put(username, returnedValues);
//
//		return Response.status(200).entity(returnedValues).build();
	}

}
