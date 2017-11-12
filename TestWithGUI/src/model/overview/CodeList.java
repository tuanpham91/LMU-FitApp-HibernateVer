package model.overview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
@Setter @Getter
public class CodeList {
	
	private List<String> codes = new ArrayList<>(); 
	public CodeList() {
		
	}
	
	public void addCode(String nextCode) {
		this.codes.add(nextCode); 
	}
	

}
