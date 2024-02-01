package Entities;

// TODO: Auto-generated Javadoc
/**
 * store transaction information.
 */
public class paymentInfo {
	
	/** The credit phrases. */
	public String[] creditPhrases;
	
	/** The cvv. */
	public String CVV;
	
	/** The Zerli coins. */
	public int ZerliCoins;
	
	/** The new user. */
	public boolean newUser;
	
	/** The expiration month. */
	public String expirationMonth;
	
	/** The expiration year. */
	public String expirationYear;
	
	/**
	 * Instantiates a new payment info.
	 *
	 * @param cn the cn
	 * @param cvv the cvv
	 * @param expMonth the exp month
	 * @param expYear the exp year
	 * @param zerlicoins the zerlicoins
	 * @param newUserDisc the new user disc
	 */
	public paymentInfo(String cn,String cvv,String expMonth,String expYear,String zerlicoins,String newUserDisc)
	{
		creditPhrases = new String[] {cn.substring(0, 4),cn.substring(4,8),cn.substring(8,12),cn.substring(12,16)};
		newUser = newUserDisc.equals("1") ? true : false;
		expirationMonth = expMonth;
		expirationYear = expYear;
		CVV = cvv;
		ZerliCoins = Integer.parseInt(zerlicoins);
	}
}
