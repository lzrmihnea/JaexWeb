package jaex.service;

import java.util.List;

import org.json.JSONException;

import jaex.dto.JaexPurchaseWithDetails;
import jaex.dto.JaexUser;

public interface JaexService {
	
	List<JaexUser> getUserList() throws JSONException, Exception;

	List<JaexPurchaseWithDetails> getRecentPurchasesForUser(JaexUser user) throws Exception;

	JaexUser getUser(String username) throws Exception;

	String getRecentPurchasesJSONForUser(String username) throws Exception;

}
