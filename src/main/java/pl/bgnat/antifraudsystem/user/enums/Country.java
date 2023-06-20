package pl.bgnat.antifraudsystem.user.enums;

public enum Country {
	NIGERIA("Nigeria", IBANCountryCode.NG),
	SOUTH_AFRICA("South Africa", IBANCountryCode.ZA),
	EGYPT("Egypt", IBANCountryCode.EG),
	KENYA("Kenya", IBANCountryCode.KE),
	MOROCCO("Morocco", IBANCountryCode.MA),
	ETHIOPIA("Ethiopia", IBANCountryCode.ET),
	GHANA("Ghana", IBANCountryCode.GH),
	ALGERIA("Algeria", IBANCountryCode.DZ),
	TUNISIA("Tunisia", IBANCountryCode.TN),
	SUDAN("Sudan", IBANCountryCode.SD),
	CHINA("China", IBANCountryCode.CN),
	INDIA("India", IBANCountryCode.IN),
	JAPAN("Japan", IBANCountryCode.JP),
	SOUTH_KOREA("South Korea", IBANCountryCode.KR),
	INDONESIA("Indonesia", IBANCountryCode.ID),
	SAUDI_ARABIA("Saudi Arabia", IBANCountryCode.SA),
	TURKEY("Turkey", IBANCountryCode.TR),
	IRAN("Iran", IBANCountryCode.IR),
	THAILAND("Thailand", IBANCountryCode.TH),
	PHILIPPINES("Philippines", IBANCountryCode.PH),
	GERMANY("Germany", IBANCountryCode.DE),
	FRANCE("France", IBANCountryCode.FR),
	SPAIN("Spain", IBANCountryCode.ES),
	ITALY("Italy", IBANCountryCode.IT),
	UNITED_KINGDOM("United Kingdom", IBANCountryCode.GB),
	POLAND("Poland", IBANCountryCode.PL),
	RUSSIA("Russia", IBANCountryCode.RU),
	NETHERLANDS("Netherlands", IBANCountryCode.NL),
	SWEDEN("Sweden", IBANCountryCode.SE),
	AUSTRIA("Austria", IBANCountryCode.AT),
	UNITED_STATES("United States", IBANCountryCode.US),
	CANADA("Canada", IBANCountryCode.CA),
	MEXICO("Mexico", IBANCountryCode.MX),
	GUATEMALA("Guatemala", IBANCountryCode.GT),
	COSTA_RICA("Costa Rica", IBANCountryCode.CR),
	PANAMA("Panama", IBANCountryCode.PA),
	HAITI("Haiti", IBANCountryCode.HT),
	JAMAICA("Jamaica", IBANCountryCode.JM),
	DOMINICAN_REPUBLIC("Dominican Republic", IBANCountryCode.DO),
	CUBA("Cuba", IBANCountryCode.CU),
	BRAZIL("Brazil", IBANCountryCode.BR),
	ARGENTINA("Argentina", IBANCountryCode.AR),
	COLOMBIA("Colombia", IBANCountryCode.CO),
	PERU("Peru", IBANCountryCode.PE),
	VENEZUELA("Venezuela", IBANCountryCode.VE),
	CHILE("Chile", IBANCountryCode.CL),
	ECUADOR("Ecuador", IBANCountryCode.EC),
	BOLIVIA("Bolivia", IBANCountryCode.BO),
	URUGUAY("Uruguay", IBANCountryCode.UY),
	PARAGUAY("Paraguay", IBANCountryCode.PY),
	AUSTRALIA("Australia", IBANCountryCode.AU),
	NEW_ZEALAND("New Zealand", IBANCountryCode.NZ),
	FIJI("Fiji", IBANCountryCode.FJ),
	PAPUA_NEW_GUINEA("Papua New Guinea", IBANCountryCode.PG),
	SOLOMON_ISLANDS("Solomon Islands", IBANCountryCode.SB),
	SAMOA("Samoa", IBANCountryCode.WS),
	VANUATU("Vanuatu", IBANCountryCode.VU),
	KIRIBATI("Kiribati", IBANCountryCode.KI),
	NAURU("Nauru", IBANCountryCode.NR),
	TONGA("Tonga", IBANCountryCode.TO);

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
