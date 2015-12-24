package jaex.service;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jaex.dto.JaexPurchaseWithDetails;

public interface JSONTransformService {
	
	JSONArray getRecentPurchasesJSON(List<JaexPurchaseWithDetails> recentPurchasesForUser) throws JSONException;

}
