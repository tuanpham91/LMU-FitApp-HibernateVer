package model.edit;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name="t_fit_article_text")
@Getter @Setter
public class TextEntry extends FitArticleElement {
	@Enumerated (EnumType.STRING)
	@Column(name="e_n_format")
	private TextEntryFormat format;
	@Transient
	private SimpleStringProperty content = new SimpleStringProperty(); 
	@Column(name="i_order_nr")
	public int orderNr; 
	public TextEntry(){
		
	}
	public TextEntry( int orderNr, TextEntryFormat format, String content) {
		this.orderNr = orderNr;
		this.format = format; 
		this.content.set(content); 
	}
	
	
	@Column (name ="s_content" )
	@Access(AccessType.PROPERTY)
	public String getContent() {
		return this.content.get();
	}
	
	
	public void setContent(String a){
		this.content.set(a);
	}
	public SimpleStringProperty content() {
		return this.content;
	}
	public enum TextEntryFormat 
	{
		HEADING_3,
		HEADING_4,
		HEADING_5,
		HEADING_6,
		TEXT_BB,
		ENUMERATION
	}

	@Override
	public TextEntry clone() {
		TextEntry res = new TextEntry(this.orderNr, this.format, this.content.get()); 
		return res;
	}
	
	public boolean equalContent(FitArticleElement element) {
		if (!(element instanceof TextEntry)) return false; 
		if (((TextEntry)element).getId() != this.id || 
				((TextEntry)element).getOrderNr() != this.orderNr || 
				!((TextEntry)element).getFormat().equals(this.format) || 
				!((TextEntry)element).getContent().equals(this.content.get())) {
			return false; 
		}
		return true; 
	}
	
}
