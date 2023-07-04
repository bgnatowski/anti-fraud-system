package pl.bgnat.antifraudsystem.bank.transaction.dto;

public enum TransactionRegion {
	EAP("EAP", "East Asia and Pacific"),
	ECA("ECA", "Europe and Central Asia"),
	HIC("HIC", "High-Income countries"),
	LAC("LAC", "Latin America and the Caribbean"),
	MENA("MENA", "The Middle East and North Africa"),
	SA("SA", "South Asia"),
	SSA("SSA", "Sub-Saharan Africa");

	private String code;
	private String description;

	TransactionRegion(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}
