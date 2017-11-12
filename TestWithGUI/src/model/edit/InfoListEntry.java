package model.edit;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "t_fit_article_info_text")
public class InfoListEntry extends FitArticleElement {
	
	
	private SimpleStringProperty name = new SimpleStringProperty(); 

	private SimpleStringProperty value= new SimpleStringProperty(); 

	@Column(name="i_order_nr")
	public int orderNr; 
	public InfoListEntry(){
		
	}
	public InfoListEntry(int orderNr, String name, String value) {
		this.orderNr = orderNr;
		this.name.set(name); 
		this.value.set(value);; 
	}

	
	@Column(name ="s_name")
	@Access(AccessType.PROPERTY)
	public String getName() {
		return this.name.get();
	}
	
	@Column(name ="s_value")
	@Access(AccessType.PROPERTY)
	public String getValue() {
		return this.value.get();
	}
	
	public void setName(String a){
		this.name.set(a);
		
	}
	public void setValue(String a){
		this.value.set(a);
	}
	
	public SimpleStringProperty name() {
		return this.name;
	}
	
	public SimpleStringProperty value() {
		return this.value;
	}
	@Override
	public FitArticleElement clone() {
		InfoListEntry res = new InfoListEntry( orderNr, name.get(), value.get()); 
		return res;
	}
	
	public boolean equalContent(FitArticleElement element) {
		if (!(element instanceof InfoListEntry)) return false; 
		if (((InfoListEntry)element).getId() != this.id || 
				
				!((InfoListEntry)element).getName().equals(this.name.get()) || 
				!((InfoListEntry)element).getValue().equals(this.value.get())) {
			return false; 
		}
		return true; 
	}

}
