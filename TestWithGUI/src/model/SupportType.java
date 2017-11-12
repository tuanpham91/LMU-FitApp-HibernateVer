package model;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
@Embeddable
public class SupportType {
	@Transient
	public BooleanProperty supportIndividualResearch = new SimpleBooleanProperty();
	@Transient
	public BooleanProperty supportResearchGroups = new SimpleBooleanProperty();
	@Transient
	public BooleanProperty supportScholarship = new SimpleBooleanProperty();
	@Transient
	public BooleanProperty supportPrice = new SimpleBooleanProperty();
	@Transient
	public BooleanProperty supportTransferSpinOff = new SimpleBooleanProperty();
	
	public static Map<String, String> DEFAULT_TEXTS = new HashMap<>();
	static {
		DEFAULT_TEXTS.put("supportIndividualResearch", "Individualförderung");
		DEFAULT_TEXTS.put("supportResearchGroups", "Verbundförderung");
		DEFAULT_TEXTS.put("supportScholarship", "Stipendium");
		DEFAULT_TEXTS.put("supportPrice", "Preis");
		DEFAULT_TEXTS.put("supportTransferSpinOff", "Transfer/Ausgründung");
	}
	
	public static Map<String, String> DEFAULT_TEXTS_SHORT = new HashMap<>(); 
	static {
		DEFAULT_TEXTS_SHORT.put("supportIndividualResearch", "Individualförderung");
		DEFAULT_TEXTS_SHORT.put("supportResearchGroups", "Verbundförderung");
		DEFAULT_TEXTS_SHORT.put("supportScholarship", "Stipendium");
		DEFAULT_TEXTS_SHORT.put("supportPrice", "Preis");
		DEFAULT_TEXTS_SHORT.put("supportTransferSpinOff", "Transfer/Ausgründung");		
	}
	
	public SupportType() {
		
	}
	
	public SupportType(boolean individual, boolean group, boolean scholarship, boolean price, boolean spinOff) {
		this.supportIndividualResearch.set(individual); 
		this.supportResearchGroups.set(group); 
		this.supportScholarship.set(scholarship); 
		this.supportPrice.set(price); 
		this.supportTransferSpinOff.set(spinOff); 
	}
	
	@Override
	public String toString() 
	{
		return toString("\n");
	}
	
	public String toString(String separator) {
		StringBuilder b = new StringBuilder();
		try {
			boolean lb = false;
			for (Field s : this.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(s.getModifiers()))
					continue;
				SimpleBooleanProperty v = (SimpleBooleanProperty) s.get(this);
				if (Boolean.TRUE.equals(v.get())) {
					if (lb)
						b.append(separator);
					lb = true;
					b.append(DEFAULT_TEXTS.get(s.getName()));
				}
			}
		} catch (Exception e) {
			b.append(e.getMessage());
		}
		return b.toString();
	}
	
	public String toShortString(String separator) {
		StringBuilder b = new StringBuilder();
		try {
			boolean lb = false;
			for (Field s : this.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(s.getModifiers()))
					continue;
				SimpleBooleanProperty v = (SimpleBooleanProperty) s.get(this);
				if (Boolean.TRUE.equals(v.get())) {
					if (lb)
						b.append(separator);
					lb = true;
					b.append(DEFAULT_TEXTS_SHORT.get(s.getName()));
				}
			}
		} catch (Exception e) {
			b.append(e.getMessage());
		}
		return b.toString();
	}
	
	@Override
	public boolean equals(Object o) {
    	if (o == null) return false; 
    	if (o == this) return true; 
    	if (!(o instanceof SupportType)) return false; 
    	SupportType st = (SupportType)o; 
    	if (st.isSupportIndividualResearch() == this.supportIndividualResearch.get() && 
    			st.isSupportPrice() == this.supportPrice.get() && 
    			st.isSupportResearchGroups() == this.supportResearchGroups.get() && 
    			st.isSupportScholarship() == this.supportScholarship.get() && 
    			st.isSupportTransferSpinOff() == this.supportTransferSpinOff.get()) return true; 
    	return false; 
	}
	
	public SupportType clone() {
		SupportType res = new SupportType(this.supportIndividualResearch.get(), this.supportResearchGroups.get(), 
				this.supportScholarship.get(), this.supportPrice.get(), this.supportTransferSpinOff.get()); 
		return res; 
	}
	@Column(name="b_support_individual_research")
	@Access(AccessType.PROPERTY)
	public boolean getSupportIndividualResearch() {
		return supportIndividualResearch.get();
	}

	public boolean isSupportIndividualResearch() {
		return supportIndividualResearch.get();
	}

	public void setSupportIndividualResearch(boolean supportIndividualResearch) {
		this.supportIndividualResearch.set(supportIndividualResearch);
	}
	
	@Column(name="b_support_research_groups")
	@Access(AccessType.PROPERTY)
	public boolean getSupportResearchGroups() {
		return supportResearchGroups.get();
	}

	public boolean isSupportResearchGroups() {
		return supportResearchGroups.get();
	}

	public void setSupportResearchGroups(boolean supportResearchGroups) {
		this.supportResearchGroups.set(supportResearchGroups);
	}

	@Column(name="b_support_scholarship")
	@Access(AccessType.PROPERTY)
	public boolean getSupportScholarship() {
		return supportScholarship.get();
	}
	
	public boolean isSupportScholarship() {
		return supportScholarship.get();
	}

	public void setSupportScholarship(boolean supportScholarship) {
		this.supportScholarship.set(supportScholarship);
	}
	
	@Column(name="b_support_price")
	@Access(AccessType.PROPERTY)
	public boolean getSupportPrice() {
		return supportPrice.get();
	}
	public boolean isSupportPrice() {
		return supportPrice.get();
	}

	public void setSupportPrice(boolean supportPrice) {
		this.supportPrice.set(supportPrice);
	}
	@Column(name="b_support_transfer_spin_off")
	@Access(AccessType.PROPERTY)
	public boolean getSupportTransferSpinOff() {
		return supportTransferSpinOff.get();
	}
	
	public boolean isSupportTransferSpinOff() {
		return supportTransferSpinOff.get();
	}

	public void setSupportTransferSpinOff(boolean supportTransferSpinOff) {
		this.supportTransferSpinOff.set(supportTransferSpinOff);
	}

}
