package model;

public enum Rubric {
	
	HORIZONT_2020         (true, "Horizont 2020"),
	OTHER_EU_PROGRAMM     (true, "Sonstige EU-Programme"),
	OTHER_INSTITUTION_INT (true, "Weitere Förderinstitutionen (int)"),
	
	LMU ("LMU"),
	BMBF ("BMBF"),
	DFG ("DFG"),
	BMWI ("Weitere Förderinstitutionen"),
	VW_STIFTUNG ("Weitere Förderinstitutionen"),
	OTHER_INSTITUTION ("Weitere Förderinstitutionen");
	
	private final boolean international;
	
	private final String akronym;
	
	private Rubric(boolean international, String akronym) {
		this.international = international;
		this.akronym = akronym;
	}
	
	private Rubric(String akronym) {
		this(false, akronym);
	}

	public boolean isInternational() {
		return international;
	}

	public String getAkronym() {
		return akronym;
	}

}
