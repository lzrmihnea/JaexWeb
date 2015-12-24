package jaex.dto;

import java.util.Formatter;

public class JaexProduct {
	
	public static final String FIELD_PRODUCT = "product";
	public static final String FIELD_ID = "id";
	public static final String FIELD_FACE = "face";
	public static final String FIELD_PRICE = "price";
	public static final String FIELD_SIZE = "size";

	private static final String JAEX_PRODUCT_TOSTRING_TEMPLATE = "Class%-25s productId%-10s face%-10s price%-10s size%-5s";
	private final Formatter formatter;

	private int id;
	private String face;
	private long price;
	private int size;

	public JaexProduct(int id) {
		super();
		this.id = id;
		formatter = new Formatter();
	}
	

	public JaexProduct(int id, String face, long price, int size) {
		super();
		this.id = id;
		this.face = face;
		this.price = price;
		this.size = size;
		formatter = new Formatter();
	}



	@Override
	public String toString() {
		return formatter.format(JAEX_PRODUCT_TOSTRING_TEMPLATE, inBrackets(this.getClass().getName()),
				inBrackets(String.valueOf(id)), inBrackets(face), inBrackets(String.valueOf(price)), inBrackets(String.valueOf(size))).toString();
	}

	private String inBrackets(final String textToPlaceInBrackets) {
		return "[" + textToPlaceInBrackets + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getFace() {
		return face;
	}


	public void setFace(String face) {
		this.face = face;
	}


	public long getPrice() {
		return price;
	}


	public void setPrice(long price) {
		this.price = price;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}
	
	

}
