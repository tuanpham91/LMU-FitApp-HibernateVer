package model.edit;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "fit_article_code")
@Setter @Getter
public class Code extends FitArticleElement {
	
	@Transient
	private StringProperty code = new SimpleStringProperty(); 
	@Column(name="i_order_nr")
	public int orderNr; 
	public Code() {
		
	}
	@Column(name="s_code")
	@Access(AccessType.PROPERTY)
	public String getCode() {
		return this.code.get();
	}
	public void setCode(String a) {
		this.code.set(a);
	}
	public StringProperty code() {
		return this.code;
	}
	public Code(int orderNr, String code) {
		this.orderNr = orderNr;
		this.code.set(code);
	}
	
	@Override
	public FitArticleElement clone() {
		
		Code res = new Code(this.orderNr, this.code.get()); 
		return res;
	}
	@Override
	public String toString() {
		return this.getCode().toString();
	}
	public boolean equalContent(FitArticleElement element) {
		System.out.println("Code clone - Code Zeile 57");
		if (!(element instanceof Code)) return false; 
		if (((Code)element).getId() != this.id || 
				((Code)element).getOrderNr() != this.orderNr || 
				!((Code)element).getCode().equals(this.code.get())) {
			return false; 
		}
		return true; 
	}

}
