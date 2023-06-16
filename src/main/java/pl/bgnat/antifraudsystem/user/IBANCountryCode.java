package pl.bgnat.antifraudsystem.user;

import java.util.HashMap;
import java.util.Map;

enum IBANCountryCode {
	// Africa
	NG("Nigeria", "NG", "NGA", "566"),
	ZA("South Africa", "ZA", "ZAF", "710"),
	EG("Egypt", "EG", "EGY", "818"),
	KE("Kenya", "KE", "KEN", "404"),
	MA("Morocco", "MA", "MAR", "504"),
	ET("Ethiopia", "ET", "ETH", "231"),
	GH("Ghana", "GH", "GHA", "288"),
	DZ("Algeria", "DZ", "DZA", "012"),
	TN("Tunisia", "TN", "TUN", "788"),
	SD("Sudan", "SD", "SDN", "729"),

	// Asia
	CN("China", "CN", "CHN", "156"),
	IN("India", "IN", "IND", "356"),
	JP("Japan", "JP", "JPN", "392"),
	KR("South Korea", "KR", "KOR", "410"),
	ID("Indonesia", "ID", "IDN", "360"),
	SA("Saudi Arabia", "SA", "SAU", "682"),
	TR("Turkey", "TR", "TUR", "792"),
	IR("Iran", "IR", "IRN", "364"),
	TH("Thailand", "TH", "THA", "764"),
	PH("Philippines", "PH", "PHL", "608"),

	// Europe
	DE("Germany", "DE", "DEU", "276"),
	FR("France", "FR", "FRA", "250"),
	ES("Spain", "ES", "ESP", "724"),
	IT("Italy", "IT", "ITA", "380"),
	GB("United Kingdom", "GB", "GBR", "826"),
	PL("Poland", "PL", "POL", "616"),
	RU("Russia", "RU", "RUS", "643"),
	NL("Netherlands", "NL", "NLD", "528"),
	SE("Sweden", "SE", "SWE", "752"),
	AT("Austria", "AT", "AUT", "040"),

	// North America
	US("United States", "US", "USA", "840"),
	CA("Canada", "CA", "CAN", "124"),
	MX("Mexico", "MX", "MEX", "484"),
	GT("Guatemala", "GT", "GTM", "320"),
	CR("Costa Rica", "CR", "CRI", "188"),
	PA("Panama", "PA", "PAN", "591"),
	HT("Haiti", "HT", "HTI", "332"),
	JM("Jamaica", "JM", "JAM", "388"),
	DO("Dominican Republic", "DO", "DOM", "214"),
	CU("Cuba", "CU", "CUB", "192"),

	// South America
	BR("Brazil", "BR", "BRA", "076"),
	AR("Argentina", "AR", "ARG", "032"),
	CO("Colombia", "CO", "COL", "170"),
	PE("Peru", "PE", "PER", "604"),
	VE("Venezuela", "VE", "VEN", "862"),
	CL("Chile", "CL", "CHL", "152"),
	EC("Ecuador", "EC", "ECU", "218"),
	BO("Bolivia", "BO", "BOL", "068"),
	UY("Uruguay", "UY", "URY", "858"),
	PY("Paraguay", "PY", "PRY", "600"),

	// Oceania
	AU("Australia", "AU", "AUS", "036"),
	NZ("New Zealand", "NZ", "NZL", "554"),
	FJ("Fiji", "FJ", "FJI", "242"),
	PG("Papua New Guinea", "PG", "PNG", "598"),
	SB("Solomon Islands", "SB", "SLB", "090"),
	WS("Samoa", "WS", "WSM", "882"),
	VU("Vanuatu", "VU", "VUT", "548"),
	KI("Kiribati", "KI", "KIR", "296"),
	NR("Nauru", "NR", "NRU", "520"),
	TO("Tonga", "TO", "TON", "776");

	private final String country;
	private final String alpha2Code;
	private final String alpha3Code;
	private final String numericCode;

	private static final Map<String, IBANCountryCode> lookup = new HashMap<>();

	static {
		for (IBANCountryCode code : IBANCountryCode.values()) {
			lookup.put(code.getCountry(), code);
		}
	}

	IBANCountryCode(String country, String alpha2Code, String alpha3Code, String numericCode) {
		this.country = country;
		this.alpha2Code = alpha2Code;
		this.alpha3Code = alpha3Code;
		this.numericCode = numericCode;
	}

	public String getCountry() {
		return country;
	}

	public String getAlpha2Code() {
		return alpha2Code;
	}

	public String getAlpha3Code() {
		return alpha3Code;
	}

	public String getNumericCode() {
		return numericCode;
	}

	public static IBANCountryCode of(String country) {
		return lookup.get(country);
	}
}
