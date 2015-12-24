package jaex.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jaex.dto.JaexProduct;
import jaex.dto.JaexPurchase;
import jaex.dto.JaexPurchaseWithDetails;
import jaex.dto.JaexUser;
import jaex.service.JaexService;

public class JaexServiceImpl implements JaexService {

	private static final String METHOD_GET = "GET";

	private static final int RECENT_PURCHASES_NUMBER = 5;

	private static final String XTEAM_URL = "http://74.50.59.155:6000";
	private static final String API_URL_USERS = "/api/users";
	private static final String API_URL_PURCHASE_BY_USER_WITH_LIMIT = "/api/purchases/by_user/{0}?limit={1}";
	private static final String API_URL_PURCHASE_BY_PRODUCT = "/api/purchases/by_product/{0}";
	private static final String API_URL_PRODUCT = "/api/products/{0}";
	private static final String API_URL_USER = "/api/users/{0}";

	// HTTP GET request
	private String getFromUrl(String urlToAccess) throws Exception {

		HttpURLConnection con = (HttpURLConnection) new URL(urlToAccess).openConnection();
		con.setRequestMethod(METHOD_GET);
		// print("Response Code : " + con.getResponseCode());

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();

	}

	@Override
	public List<JaexUser> getUserList() throws JSONException, Exception {
		List<JaexUser> userList = new ArrayList<>();
		JSONArray jsonArray = this.getJSONArrayFromUrl(API_URL_USERS, JaexUser.FIELD_USERS);

		JaexUser jaexUser;
		for (int i = 0; i < jsonArray.length(); i++) {
			jaexUser = getUserFromJSON(jsonArray.getJSONObject(i));
			userList.add(jaexUser);
			print(jaexUser);
		}
		return userList;
	}

	private static JaexUser getUserFromJSON(JSONObject jsonObject) throws JSONException {
		String username = jsonObject.getString(JaexUser.FIELD_USERNAME);
		String email = jsonObject.getString(JaexUser.FIELD_EMAIL);
		return new JaexUser(username, email);
	}

	@Override
	public List<JaexPurchaseWithDetails> getRecentPurchasesForUser(JaexUser user) throws Exception {
		List<JaexPurchaseWithDetails> purchasesOfUser = new ArrayList<>();

		JSONArray fiveRecentPurchasesJSONArray = getJSONArrayFromUrl(API_URL_PURCHASE_BY_USER_WITH_LIMIT, JaexPurchase.FIELD_PURCHASES,
				user.getUsername(), String.valueOf(RECENT_PURCHASES_NUMBER));

		for (int indexOfPurchase = 0; indexOfPurchase < fiveRecentPurchasesJSONArray.length(); indexOfPurchase++) {
			JSONObject recentPurchaseJSON = fiveRecentPurchasesJSONArray.getJSONObject(indexOfPurchase);
			purchasesOfUser.add(getRecentPurchaseForUserFromJSON(user, recentPurchaseJSON));
		}
		Collections.sort(purchasesOfUser);
		return purchasesOfUser;
	}

	private JaexPurchaseWithDetails getRecentPurchaseForUserFromJSON(JaexUser user, JSONObject recentPurchaseJSON)
			throws JSONException, Exception {
		int id = getIntFromJSON(recentPurchaseJSON, JaexPurchase.FIELD_ID);
		Date dateOfPurchase = getDateFromJSON(recentPurchaseJSON, JaexPurchase.FIELD_DATE);
		String username = recentPurchaseJSON.getString(JaexUser.FIELD_USERNAME);
		JaexUser addedUser = (user != null ? user : new JaexUser(username, null));
		int productId = getIntFromJSON(recentPurchaseJSON, JaexPurchase.FIELD_PRODUCT_ID);

		String productIdAsString = String.valueOf(productId);
		JaexProduct product = getProductInfo(productIdAsString);
		
		Set<JaexUser> peopleWhoBoughtThisProduct = getOtherPeopleWhoBoughtThis(productIdAsString, user);

		JaexPurchaseWithDetails jaexPurchaseDisplayed = new JaexPurchaseWithDetails(id, dateOfPurchase, addedUser, product, peopleWhoBoughtThisProduct);
		return jaexPurchaseDisplayed;
	}

	private Set<JaexUser> getOtherPeopleWhoBoughtThis(String productId, JaexUser besidesThisUser) throws Exception {
		Set<JaexUser> peopleWhoBoughtThis = new HashSet<>();
		JSONArray jsonArrayOfPeopleWhoBoughtThis = getJSONArrayFromUrl(API_URL_PURCHASE_BY_PRODUCT,
				JaexPurchase.FIELD_PURCHASES, productId);

		for (int i = 0; i < jsonArrayOfPeopleWhoBoughtThis.length(); i++) {
			String username = jsonArrayOfPeopleWhoBoughtThis.getJSONObject(i).getString(JaexUser.FIELD_USERNAME);
			JaexUser personWhoBoughThis = getUser(username);
			if(!personWhoBoughThis.equals(besidesThisUser)) {
				peopleWhoBoughtThis.add(personWhoBoughThis);
			}
		}

		return peopleWhoBoughtThis;
	}

	@Override
	public JaexUser getUser(String username) throws Exception {
		Object[] urlParams = { username };
		JSONObject userAsJSONObj = getJSONObjFromUrl(MessageFormat.format(XTEAM_URL + API_URL_USER, urlParams)).getJSONObject(JaexUser.FIELD_USER);
		String email = userAsJSONObj.getString(JaexUser.FIELD_EMAIL);
		return new JaexUser(username, email);
	}

	private JaexProduct getProductInfo(String productId) throws Exception {
		JaexProduct jaexProduct = new JaexProduct(Integer.valueOf(productId));

		String url = MessageFormat.format(XTEAM_URL + API_URL_PRODUCT, productId);
		JSONObject productAsJSONObj = getJSONObjFromUrl(url).getJSONObject(JaexProduct.FIELD_PRODUCT);
		jaexProduct = getProductFromJSONObject(productAsJSONObj);

		return jaexProduct;
	}

	private JaexProduct getProductFromJSONObject(JSONObject jsonObject) throws JSONException {
		int id = getIntFromJSON(jsonObject, JaexProduct.FIELD_ID);
		String face = jsonObject.getString(JaexProduct.FIELD_FACE);
		long price = getLongFromJSON(jsonObject, JaexProduct.FIELD_PRICE);
		int size = getIntFromJSON(jsonObject, JaexProduct.FIELD_SIZE);
		return new JaexProduct(id, face, price, size);
	}

	private Date getDateFromJSON(JSONObject jsonObject, final String fieldDate) throws JSONException {
		final String dateAsString = jsonObject.getString(fieldDate);
		final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTime();
		Date date = dateTimeFormatter.parseDateTime(dateAsString).toDate();
		return date;
	}

	private Integer getIntFromJSON(JSONObject jsonObject, final String fieldId) throws JSONException {
		return Integer.valueOf(jsonObject.getInt(fieldId));
	}

	private Long getLongFromJSON(JSONObject jsonObject, final String fieldId) throws JSONException {
		return Long.valueOf(jsonObject.getLong(fieldId));
	}

	private JSONArray getJSONArrayFromUrl(String apiUrl, String jsonFieldname, Object... urlParams) throws Exception {
		String url = MessageFormat.format(XTEAM_URL + apiUrl, urlParams);
		final JSONObject jsonObject = getJSONObjFromUrl(url);
		return jsonObject.getJSONArray(jsonFieldname);
	}

	private JSONObject getJSONObjFromUrl(String url) throws JSONException, Exception {
		final JSONObject jsonObject = new JSONObject(getFromUrl(url));
		return jsonObject;
	}

	// TODO remove this
	private static void print(Object textToPrint) {
		System.out.println(textToPrint.toString());
	}

}
