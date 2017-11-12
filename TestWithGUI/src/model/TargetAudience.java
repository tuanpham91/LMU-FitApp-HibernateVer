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

import javafx.beans.property.SimpleBooleanProperty;
@Embeddable
public class TargetAudience {
	@Transient
	private SimpleBooleanProperty targetAudienceYoungResearchers = new SimpleBooleanProperty();
	@Transient
	private SimpleBooleanProperty targetAudienceExperiencedResearchers = new SimpleBooleanProperty();
	
	static Map<String, String> DEFAULT_TEXT = new HashMap<>();
	static {
		DEFAULT_TEXT.put("targetAudienceYoungResearchers", "Wissenschaftlicher Nachwuchs");
		DEFAULT_TEXT.put("targetAudienceExperiencedResearchers", "Erfahrene Wissenschaftler");
	}
	
	static Map<String, String> DEFAULT_TEXT_SHORT = new HashMap<String, String>(); 
	static {
		DEFAULT_TEXT_SHORT.put("targetAudienceYoungResearchers", "Nachwuchs"); 
		DEFAULT_TEXT_SHORT.put("targetAudienceExperiencedResearchers", "Erfahren"); 
	}
	
	public TargetAudience() {
		
	}
	
	public TargetAudience(boolean young, boolean experienced) {
		this.targetAudienceYoungResearchers.set(young); 
		this.targetAudienceExperiencedResearchers.set(experienced); 
	}
	
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		try {
			boolean lb = false;
			for (Field s : this.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(s.getModifiers()))
					continue;
				if (lb)
					b.append("\n");
				SimpleBooleanProperty v = (SimpleBooleanProperty) s.get(this);
				if (Boolean.TRUE.equals(v.get())) {
					lb = true;
					b.append(DEFAULT_TEXT.get(s.getName()));
				}
			}
		} catch (Exception e) {
			b.append(e.getMessage());
		}
		return b.toString();
	}

	public String toShortString() {
		StringBuilder b = new StringBuilder();
		try {
			boolean lb = false;
			for (Field s : this.getClass().getDeclaredFields()) {
				if (Modifier.isStatic(s.getModifiers()))
					continue;
				if (lb)
					b.append("\n");
				SimpleBooleanProperty v = (SimpleBooleanProperty) s.get(this);
				if (Boolean.TRUE.equals(v.get())) {
					lb = true;
					b.append(DEFAULT_TEXT_SHORT.get(s.getName()));
				}
			}
		} catch (Exception e) {
			b.append(e.getMessage());
		}
		return b.toString();
	}

	public boolean isTargetAudienceYoungResearchers() {
		return targetAudienceYoungResearchers.get();
	}
	@Column(name = "b_target_audience_young_researchers")
	@Access(AccessType.PROPERTY)
	public boolean getTargetAudienceYoungResearchers() {
		return targetAudienceYoungResearchers.get();
	}
	public void setTargetAudienceYoungResearchers(boolean targetAudienceYoungResearchers) {
		this.targetAudienceYoungResearchers.set(targetAudienceYoungResearchers);
	}

	@Column(name="b_target_audience_experienced_researchers")
	@Access(AccessType.PROPERTY)
	public boolean getTargetAudienceExperiencedResearchers() {
		return targetAudienceExperiencedResearchers.get();
	}
	
	public boolean isTargetAudienceExperiencedResearchers() {
		return targetAudienceExperiencedResearchers.get();
	}

	public void setTargetAudienceExperiencedResearchers(boolean targetAudienceExperiencedResearchers) {
		this.targetAudienceExperiencedResearchers.set(targetAudienceExperiencedResearchers);
	}
	
	public SimpleBooleanProperty TargetAudienceExperienced() {
		return this.targetAudienceExperiencedResearchers; 
	}
	
	public SimpleBooleanProperty TargetAudienceYoung() {
		return this.targetAudienceYoungResearchers; 
	}
	
	public TargetAudience clone() {
		TargetAudience res = new TargetAudience(this.targetAudienceYoungResearchers.get(), this.targetAudienceExperiencedResearchers.get()); 
		return res; 
	}
	
	@Override
	public boolean equals(Object o) {
    	if (o == null) return false; 
    	if (o == this) return true; 
    	if (!(o instanceof TargetAudience)) return false; 
    	TargetAudience ta = (TargetAudience)o; 
    	if (ta.targetAudienceExperiencedResearchers.get() == this.targetAudienceExperiencedResearchers.get() && 
    			ta.targetAudienceYoungResearchers.get() == this.targetAudienceYoungResearchers.get()) return true; 
    	return false; 
	}

}
