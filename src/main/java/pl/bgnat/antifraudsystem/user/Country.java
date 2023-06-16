package pl.bgnat.antifraudsystem.user;

import static pl.bgnat.antifraudsystem.user.IBANCountryCode.*;

enum Country {
	NIGERIA("Nigeria", NG),
	SOUTH_AFRICA("South Africa", ZA),
	EGYPT("Egypt", EG),
	KENYA("Kenya", KE),
	MOROCCO("Morocco", MA),
	ETHIOPIA("Ethiopia", ET),
	GHANA("Ghana", GH),
	ALGERIA("Algeria", DZ),
	TUNISIA("Tunisia", TN),
	SUDAN("Sudan", SD),
	CHINA("China", CN),
	INDIA("India", IN),
	JAPAN("Japan", JP),
	SOUTH_KOREA("South Korea", KR),
	INDONESIA("Indonesia", ID),
	SAUDI_ARABIA("Saudi Arabia", SA),
	TURKEY("Turkey", TR),
	IRAN("Iran", IR),
	THAILAND("Thailand", TH),
	PHILIPPINES("Philippines", PH),
	GERMANY("Germany", DE),
	FRANCE("France", FR),
	SPAIN("Spain", ES),
	ITALY("Italy", IT),
	UNITED_KINGDOM("United Kingdom", GB),
	POLAND("Poland", PL),
	RUSSIA("Russia", RU),
	NETHERLANDS("Netherlands", NL),
	SWEDEN("Sweden", SE),
	AUSTRIA("Austria", AT),
	UNITED_STATES("United States", US),
	CANADA("Canada", CA),
	MEXICO("Mexico", MX),
	GUATEMALA("Guatemala", GT),
	COSTA_RICA("Costa Rica", CR),
	PANAMA("Panama", PA),
	HAITI("Haiti", HT),
	JAMAICA("Jamaica", JM),
	DOMINICAN_REPUBLIC("Dominican Republic", DO),
	CUBA("Cuba", CU),
	BRAZIL("Brazil", BR),
	ARGENTINA("Argentina", AR),
	COLOMBIA("Colombia", CO),
	PERU("Peru", PE),
	VENEZUELA("Venezuela", VE),
	CHILE("Chile", CL),
	ECUADOR("Ecuador", EC),
	BOLIVIA("Bolivia", BO),
	URUGUAY("Uruguay", UY),
	PARAGUAY("Paraguay", PY),
	AUSTRALIA("Australia", AU),
	NEW_ZEALAND("New Zealand", NZ),
	FIJI("Fiji", FJ),
	PAPUA_NEW_GUINEA("Papua New Guinea", PG),
	SOLOMON_ISLANDS("Solomon Islands", SB),
	SAMOA("Samoa", WS),
	VANUATU("Vanuatu", VU),
	KIRIBATI("Kiribati", KI),
	NAURU("Nauru", NR),
	TONGA("Tonga", TO);

	private final String countryName;
	private final IBANCountryCode countryCode;

	Country(String countryName, IBANCountryCode countryCode) {
		this.countryName = countryName;
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public IBANCountryCode getCountryCode() {
		return countryCode;
	}

}
