package pl.bgnat.antifraudsystem.user.enums;

import pl.bgnat.antifraudsystem.user.exceptions.IllegalCountryException;

import java.util.Currency;
import java.util.Locale;

import static pl.bgnat.antifraudsystem.user.enums.IBANCountryCode.*;

public enum Country {
	CHINA("China", "CNY"),
	INDIA("India", "INR"),
	JAPAN("Japan", "JPY"),
	UNITED_STATES("United States", "USD"),
	BRAZIL("Brazil", "BRL"),
	POLAND("Poland", "PLN"),
	GERMANY("Germany", "EUR"),
	FRANCE("France", "EUR"),
	UNITED_KINGDOM("United Kingdom", "GBP"),
	AUSTRALIA("Australia", "AUD"),
	CANADA("Canada", "CAD"),
	RUSSIA("Russia", "RUB"),
	SOUTH_KOREA("South Korea", "KRW");

	private final String countryName;
	private final String currencyCode;

	Country(String countryName, String currencyCode) {
		this.countryName = countryName;
		this.currencyCode = currencyCode;
	}

	public String getCountryName() {
		return countryName;
	}
	public Currency getCurrency() {
		return Currency.getInstance(currencyCode);
	}
	public static final Country parse(String countryName){
		try {
			return Country.valueOf(countryName);
		} catch (IllegalArgumentException | NullPointerException e) {
			throw new IllegalCountryException(countryName);
		}
	}
}
