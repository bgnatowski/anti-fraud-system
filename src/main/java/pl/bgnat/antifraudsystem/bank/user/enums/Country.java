package pl.bgnat.antifraudsystem.bank.user.enums;

import lombok.RequiredArgsConstructor;
import pl.bgnat.antifraudsystem.bank.user.exceptions.IllegalCountryException;

import java.util.Currency;

@RequiredArgsConstructor
public enum Country {
	CHINA("China", "CNY", IBANCountryCode.CHINA),
	INDIA("India", "INR", IBANCountryCode.INDIA),
	JAPAN("Japan", "JPY", IBANCountryCode.JAPAN),
	UNITED_STATES("United States", "USD", IBANCountryCode.UNITED_STATES),
	BRAZIL("Brazil", "BRL", IBANCountryCode.BRAZIL),
	POLAND("Poland", "PLN", IBANCountryCode.POLAND),
	GERMANY("Germany", "EUR", IBANCountryCode.GERMANY),
	FRANCE("France", "EUR", IBANCountryCode.FRANCE),
	UNITED_KINGDOM("United Kingdom", "GBP", IBANCountryCode.UNITED_KINGDOM),
	AUSTRALIA("Australia", "AUD", IBANCountryCode.AUSTRALIA),
	CANADA("Canada", "CAD", IBANCountryCode.CANADA),
	RUSSIA("Russia", "RUB", IBANCountryCode.RUSSIA),
	SOUTH_KOREA("South Korea", "KRW", IBANCountryCode.SOUTH_KOREA);

	private final String countryName;
	private final String currencyCode;
	private final IBANCountryCode ibanCountryCode;

	public String getCountryName() {
		return countryName;
	}

	public Currency getCurrency() {
		return Currency.getInstance(currencyCode);
	}

	public IBANCountryCode getIbanCountryCode(){
		return ibanCountryCode;
	}

	public static final Country parse(String countryName) {
		try {
			return Country.valueOf(countryName);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new IllegalCountryException(countryName);
		}
	}
}
