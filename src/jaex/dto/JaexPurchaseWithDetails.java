package jaex.dto;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class JaexPurchaseWithDetails implements Comparable<JaexPurchaseWithDetails> {
	
	public static final String FIELD_PURCHASES = "purchases";
	public static final String FIELD_ID = "id";
	public static final String FIELD_PRODUCT_ID = "productId";
	public static final String FIELD_USERNAME = "username";
	public static final String FIELD_DATE = "date";
	
	public static final String FIELD_RECENT = "recent";
	
	private static final String JAEX_PURCHASE_TOSTRING_TEMPLATE = "Class{0} productId{1} user{2} date{3}";
//	private static final String JAEX_PURCHASE_TOSTRING_TEMPLATE1 = "Class%-25s productId%-10s user%-20s date%-10s";
	
	private int id;
	private JaexProduct product;
	private JaexUser user;
	private Date date;
	
	private Set<JaexUser> peopleWhoAlsoBoughtThis = new HashSet<>();

	public JaexPurchaseWithDetails() {
		
	}
	
	public JaexPurchaseWithDetails(int id, Date date, JaexUser user, JaexProduct product, Set<JaexUser> otherPeopleWhoBoughtThis) {
		this.setId(id);
		this.setDate(date);
		this.setUser(user);
		this.setProduct(product);
		this.setPeopleWhoAlsoBoughtThis(otherPeopleWhoBoughtThis);
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public JaexProduct getProduct() {
		return product;
	}

	public void setProduct(JaexProduct product) {
		this.product = product;
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
		return MessageFormat.format(JAEX_PURCHASE_TOSTRING_TEMPLATE, inBrackets(this.getClass().getName()),
				inBrackets(String.valueOf(product)), inBrackets(user.getUsername()), inBrackets(date.toString())).toString();
	}

	private String inBrackets(final String textToPlaceInBrackets) {
		return "[" + textToPlaceInBrackets + "]";
	}

	public Set<JaexUser> getPeopleWhoAlsoBoughtThis() {
		return peopleWhoAlsoBoughtThis;
	}

	public void setPeopleWhoAlsoBoughtThis(Set<JaexUser> peopleWhoAlsoBoughtThis) {
		this.peopleWhoAlsoBoughtThis = peopleWhoAlsoBoughtThis;
	}

//	Sorting is done in descending order. Greatest size goes first.
	@Override
	public int compareTo(JaexPurchaseWithDetails o2) {
		int thisNbOfBuyers = this.getPeopleWhoAlsoBoughtThis().size();
		int otherProdNbOfBuyers = o2.getPeopleWhoAlsoBoughtThis().size();
		if(thisNbOfBuyers == otherProdNbOfBuyers)
            return 0;
        return thisNbOfBuyers < otherProdNbOfBuyers ? 1 : -1;
	}

}
