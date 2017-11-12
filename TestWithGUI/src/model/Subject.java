package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embeddable;
public enum Subject {
	
	THEMENOFFENES_PROGRAMM            ("Themenoffenes Programm",          "TP",  0x00000001),     // 0001 
	LIFE_SCIENCES                     ("Life Sciences",                   "LS",  0x00000002),     // 0010
	PHYSICAL_SCIENCES_AND_ENGINEERING ("Physical Sciences & Engineering", "PSE", 0x00000004),     // 0100
	SOCIAL_SCIENCES_AND_HUMANITIES    ("Social Sciences & Humanities",    "SSH", 0x00000008);     // 1000
	
	private final String description, akronym;
	
	private final int code;
	
	private final int orderWeight;
	
	private Subject(String description, String akronym, int code) {
		this.description = description;
		this.akronym = akronym;
		this.code = code;
		this.orderWeight = code;
	}

	public static List<Subject> getMinorSubjectAreas(int code) {
		List<Subject> areas = new ArrayList<>();
		for(Subject area : values()) {
			if ((code & area.getCode()) != 0) {
				areas.add(area);
			}
		}
		return areas;
	}
	
	
	public static int getCode(List<Subject> subjects) {
		int res = 0; 
		for (Subject s : subjects) {
			res += s.getCode(); 
		}
		return res; 
	}

	public String getDescription() {
		return description;
	}

	public String getAkronym() {
		return akronym;
	}

	public int getCode() {
		return code;
	}

	public int getOrderWeight() {
		return orderWeight;
	}	


}
