package model.edit;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name = "fit_article_web_link")
public class WebLinkEntry extends FitArticleElement {
	@Transient
	private SimpleStringProperty url = new SimpleStringProperty(); 
	@Transient
	private SimpleStringProperty info = new SimpleStringProperty(); 
	@Column(name="i_order_nr")
	public int orderNr; 
	
	public WebLinkEntry() {
		
	}
	@Column (name ="s_url" )
	@Access(AccessType.PROPERTY)
	public String getUrl() {
		return this.url.get();
	}
	@Column (name ="s_info" )
	@Access(AccessType.PROPERTY)
	public String getInfo() {
		return this.info.get();
	}
	
	public void setUrl(String a) {
		this.url.set(a);
	}
	public void setInfo(String a) {
		this.info.set(a);
	}
	public SimpleStringProperty url() {
		return this.url;
	}
	public SimpleStringProperty info() {
		return this.info;
	}
	public WebLinkEntry( int orderNr, String url, String info) {
		this.orderNr = orderNr;
		this.url.set(url); 
		this.info.set(info);
	}
	
	public String getElementPreview() {
		String preview = "Link ";
		if(url == null) {
			preview += "???";
		}
		else {
			preview += url.length().get() > 150 ? url.get().substring(0, 147) + "..." : url.get();
		}
		return preview;
	}

	@Override
	public FitArticleElement clone() {
		WebLinkEntry res = new WebLinkEntry(orderNr, url.get(), info.get()); 
		return res;
	}
	
	public boolean equalContent(FitArticleElement element) {
		if (!(element instanceof WebLinkEntry)) return false; 
		if (((WebLinkEntry)element).getId() != this.id || 
				((WebLinkEntry)element).getOrderNr() != this.orderNr || 
				!((WebLinkEntry)element).getUrl().equals(this.url.get()) || 
				!((WebLinkEntry)element).getInfo().equals(this.info.get())) {
			return false; 
		}
		return true; 
	}
	
}
