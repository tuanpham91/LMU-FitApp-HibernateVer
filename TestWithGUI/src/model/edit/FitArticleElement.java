package model.edit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;
import model.FitArticle;
@Setter @Getter
@Entity 
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class FitArticleElement {
	/*
	 * DOC:
	 * GenerationType.TABLE—Hibernate will use an extra table in your database
		schema that holds the next numeric primary key value, one row for each entity
		class. This table will be read and updated accordingly, before INSERTs. The
		default table name is HIBERNATE_SEQUENCES with columns SEQUENCE_NAME and
		SEQUENCE_NEXT_HI_VALUE. (The internal implementation uses a more complex
		but efficient hi/lo generation algorithm; more on this later.)
		http://www.summa.com/blog/2011/07/29/setting-up-sequential-ids-using-jpa-tablegenerator
	 */
	@Id
	@Column(name="i_id")
	public int id; 
	@Column(name="i_order_nr")
	public int orderNr; 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn (name="i_fit_article")
	private FitArticle fitArticle;
	
	public FitArticleElement() {
		
	}
	
	public FitArticleElement(int id, int orderNr) {
		this.id = id; 
		this.orderNr = orderNr; 
	}
	
	public FitArticle getFitArticle() {
		return this.fitArticle;
	}
	public void setFitArticle(FitArticle ar) {
		this.fitArticle = ar;
	}
	public abstract FitArticleElement clone(); 

}
