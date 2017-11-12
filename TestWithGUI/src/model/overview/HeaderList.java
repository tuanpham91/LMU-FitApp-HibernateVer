package model.overview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import lombok.Getter;
import lombok.Setter;
@Embeddable
@Getter @Setter
public class HeaderList {
	private String heading1; 
	private String heading2;
	private String heading3; 
	private String heading4; 
	private String heading5; 
	private Set<String> heading6 = new HashSet<String>();
	private String heading7; 
	public boolean loaded;
	public HeaderList() {
		loaded = false;
	}
	
	public void addHeading6(String heading6) {
		this.heading6.add(heading6); 
	}
	
	public void removeHeading6(int index) {
		this.heading6.remove(index); 
	}
	
	public boolean search(String substring) {
		if (heading1 != null && heading1.toLowerCase().contains(substring)) {
			return true; 
		}
		if (heading2 != null && heading2.toLowerCase().contains(substring)) {
			return true; 
		}
		if (heading3 != null && heading3.toLowerCase().contains(substring)) {
			return true; 
		}
		if (heading4 != null && heading4.toLowerCase().contains(substring)) {
			return true; 
		}
		if (heading5 != null && heading5.toLowerCase().contains(substring)) {
			return true; 
		}
		Iterator<String> HeaderIterator = this.heading6.iterator();
		while (HeaderIterator.hasNext()) {
    		if (HeaderIterator.next().contains(substring)) {
    			return true;
    		}
    	}
		if (heading7 != null && heading7.toLowerCase().contains(substring)) {
			return true; 
		}
		return false; 
	}
	
}
