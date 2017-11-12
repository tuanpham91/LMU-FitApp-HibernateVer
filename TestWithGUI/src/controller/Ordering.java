package controller;

public enum Ordering {
	
	NONE					("", "", ""), 
	APPLICATION_DEADLINE	("t_fit_article", "d_application_deadline", "Bewerbungsfrist"), 
	PRIORITY 				("t_fit_article", "i_priority", "Priorität"), 
	RUBRIC					("t_fit_article", "e_n_rubric", "Rubrik"), 
	SUBJECT					("t_fit_article", "e_n_main_subject_area", "Fachbereich");
	
	private String table; 
	private String colName; 
	private String itemName; 
	private boolean ascending; 
	
	private Ordering(String table, String colName, String itemName) {
		this.table = table; 
		this.colName = colName; 
		this.itemName = itemName; 
	}
	
	public void setAscending(boolean asc) {
		this.ascending = asc; 
	}
	
	public boolean getAscending() {
		return this.ascending; 
	}
	
	public String getColName() {
		if (ascending) {
			return this.colName; 
		} else {
			return this.colName; 
		}
	}
	
	public String getAsc() {
		if (ascending) {
			return " ASC"; 
		} else {
			return " DESC"; 
		}
	}
	
	public String getAscInv() {
		if (ascending) {
			return " DESC"; 
		} else {
			return " ASC"; 
		}
	}
	
	public String getTable() {
		return this.table; 
	}
	
	public String getItemName() {
		return this.itemName; 
	}
	
	public static Ordering fromString(String s) {
		if (s == null) {
			return Ordering.NONE; 
		}
		if (s.equals("Bewerbungsfrist")) {
			return Ordering.APPLICATION_DEADLINE; 
		} else if (s.equals("Priorität")) {
			return Ordering.PRIORITY; 
		} else if (s.equals("Rubrik")) {
			return Ordering.RUBRIC; 
		} else if (s.equals("Fachbereich")) {
			return Ordering.SUBJECT; 
		} else {
			return Ordering.NONE; 
		}
	}
	
}
