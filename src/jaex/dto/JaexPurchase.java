package jaex.dto;

import java.util.Date;
import java.util.Formatter;

public class JaexPurchase {
	
	public static final String FIELD_PURCHASES = "purchases";
	public static final String FIELD_ID = "id";
	public static final String FIELD_PRODUCT_ID = "productId";
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_DATE = "date";
	
	private static final String JAEX_PURCHASE_TOSTRING_TEMPLATE1 = "Class%-25s productId%-10s user%-20s date%-10s";
	private Formatter formatter;
	
	private int id;
	private int productId;
	private JaexUser user;
	private Date date;
	
	public JaexPurchase(int id, int productId, Date date) {
		super();
		this.id = id;
		this.productId = productId;
		this.date = date;
		formatter = new Formatter();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public JaexUser getUser() {
		return user;
	}

	public void setUser(JaexUser user) {
		this.user = user;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return formatter.format(JAEX_PURCHASE_TOSTRING_TEMPLATE1, inBrackets(this.getClass().getName()),
				inBrackets(String.valueOf(productId)), inBrackets(user.getUsername()), inBrackets(date.toString())).toString();
	}

	private String inBrackets(final String textToPlaceInBrackets) {
		return "[" + textToPlaceInBrackets + "]";
	}

}
