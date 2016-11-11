package com.protostar.billnstock.until.data;

public class NumberToRupees {

	protected String amountInWords;
	protected String words = "RUPEES ";
	final static String units[] = { "", "ONE ", "TWO ", "THREE ", "FOUR ",
			"FIVE ", "SIX ", "SEVEN ", "EIGHT ", "NINE ", "TEN ", "ELEVEN ",
			"TWELVE ", "THIRTEEN ", "FOURTEEN ", "FIFTEEN ", "SIXTEEN ",
			"SEVENTEEN ", "EIGHTEEN ", "NINETEEN " };
	String tens[] = { "", "TEN ", "TWENTY ", "THIRTY ", "FORTY ", "FIFTY ",
			"SIXTY ", "SEVENTY ", "EIGHTY ", "NINETY " };

	public NumberToRupees(int amount) {
		String tword;
		if (amount > (10000000 - 1)) {
			int crore = amount / 10000000;
			tword = threeDigits(crore);
			if (tword.length() > 1)
				words += tword + "CRORE ";

			amount -= crore * 10000000;
		}

		if (amount > (100000 - 1)) {
			int lakh = amount / 100000;
			tword = threeDigits(lakh);
			if (tword.length() > 1)
				words += tword + "LAKH ";

			amount -= lakh * 100000;
		}
		int thousands = amount / 1000;
		tword = threeDigits(thousands);
		if (tword.length() > 1)
			words += tword + "THOUSAND ";
		amount -= thousands * 1000;

		words += threeDigits(amount) + "ONLY ";
		this.amountInWords = words;

	}

	public String threeDigits(int digits) {
		String digWord = "";
		int hnd = digits / 100;
		if (hnd > 0)
			digWord += units[hnd] + "HUNDRED ";
		int ten = digits - hnd * 100;
		if (ten < 20)
			digWord += units[ten];
		else {
			int tenth = ten / 10;
			digWord += tens[tenth];
			int last = ten - tenth * 10;
			digWord += units[last];

		}
		return digWord;
	}

	/**
	 * Get the value of amountInWords
	 *
	 * @return the value of amountInWords
	 */
	public String getAmountInWords() {
		return amountInWords;
	}

	/**
	 * Set the value of amountInWords
	 *
	 * @param amountInWords
	 *            new value of amountInWords
	 */
	public void setAmountInWords(String amountInWords) {
		this.amountInWords = amountInWords;
	}

}