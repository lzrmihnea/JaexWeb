package jaex.service.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jaex.dto.JaexProduct;
import jaex.dto.JaexPurchaseWithDetails;
import jaex.dto.JaexUser;
import jaex.service.JSONTransformService;

public class JSONTransformServiceImpl implements JSONTransformService {

	@Override
	public JSONArray getRecentPurchasesJSON(List<JaexPurchaseWithDetails> recentPurchasesForUser) throws JSONException {
		JSONArray recentPurchasesJSON = new JSONArray();
		
		for (JaexPurchaseWithDetails purchase : recentPurchasesForUser) {
			JSONObject purchaseJSON = new JSONObject();
			purchaseJSON.put(JaexProduct.FIELD_ID, purchase.getProduct().getId());
			purchaseJSON.put(JaexProduct.FIELD_FACE, purchase.getProduct().getFace());
			purchaseJSON.put(JaexProduct.FIELD_PRICE, purchase.getProduct().getPrice());
			purchaseJSON.put(JaexProduct.FIELD_SIZE, purchase.getProduct().getSize());
			
			JSONArray jsonArray = new JSONArray( );
			for (JaexUser personWhoBoughtThis : purchase.getPeopleWhoAlsoBoughtThis()) {
				jsonArray.put(personWhoBoughtThis.getUsername());
			}
			purchaseJSON.put(JaexPurchaseWithDetails.FIELD_RECENT, jsonArray);
			recentPurchasesJSON.put(purchaseJSON);
		}
		
		return recentPurchasesJSON;
	}

}
