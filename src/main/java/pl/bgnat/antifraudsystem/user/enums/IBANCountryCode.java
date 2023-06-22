package pl.bgnat.antifraudsystem.user.enums;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum IBANCountryCode {
	CHINA("CN", "CHN", "156"),
	INDIA("IN", "IND", "356"),
	JAPAN("JP", "JPN", "392"),
	UNITED_STATES("US", "USA", "840"),
	BRAZIL("BR", "BRA", "076"),
	POLAND("PL", "POL", "616"),
	GERMANY("DE", "DEU", "276"),
	FRANCE("FR", "FRA", "250"),
	UNITED_KINGDOM("GB", "GBR", "826"),
	AUSTRALIA("AU", "AUS", "036"),
	CANADA("CA", "CAN", "124"),
	RUSSIA("RU", "RUS", "643"),
	SOUTH_KOREA("KR", "KOR", "410");

	private final String alpha2Code;
	private final String alpha3Code;
	private final String numericCode;

	private static final Map<Country, IBANCountryCode> lookup = new HashMap<>();

	public String getAlpha2Code() {
		return alpha2Code;
	}

	public String getAlpha3Code() {
		return alpha3Code;
	}

	public String getNumericCode() {
		return numericCode;
	}
}
