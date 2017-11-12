package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.SimpleBooleanProperty;
import lombok.Getter;
import model.FitArticle;
import model.Rubric;
import model.Subject;
import model.SupportType;
import model.TargetAudience;
import model.edit.Code;
import model.edit.ContactPerson;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.TextEntry.TextEntryFormat;
import model.edit.WebLinkEntry;
import view.GuiContainer;

/**
 * Singleton to organize editing articles, saving articles and deleting them. 
 * <p>
 * The {@link #INSTANCE} holds a reference to the article that can be edited. 
 * This article is set by {@link #setArticle(int)}. Saving and deleting is 
 * conducted by {@link #saveArticle()} and {@link #deleteArticle()}. A new 
 * article is saved when the flag {@link #isNewArticle()} is set. The ids for 
 * the respective tuples in the data base is retrieved from {@link #nextIdArticle nextId*}. 
 */
public enum Edit {
	
	INSTANCE; 
	
	private static final Logger log = Controller.log;
	
	@Getter
	private FitArticle article = new FitArticle(); 
	@Getter 
	private FitArticle articleOld = new FitArticle(); 
	@Getter
	private boolean newArticle; 
	private MsAccess db = new MsAccess();
	
	/**
	 * Sets the {@link #newArticle} flag, i.e. the currently editable article is regarded 
	 * as new article and if saved, new tuples are inserted in the data base. 
	 */
	public void markAsNewArticle() {
		newArticle = true; 
	}
	
	/**
	 * Sets a new article to be editable. The contents are retrieved directly from the data base. 
	 * {@link #articleOld} is set as a copy of the editable article, in order to check 
	 * what has changed in case of saving the edited article. 
	 * @param id The id of the article. 
	 */
	public void setArticle(int id) {
		System.out.println("set article " + id + " to be editted");		
		newArticle = false; 
		article = db.getArticleById(id);
		articleOld = article.copy();
	}
	
	/**
	 * Sets a new article without loading any contents from the data base. 
	 */
	public void setNewArticle() {
		article = new FitArticle(); 
		article.setMinorSubjectCode(0);
		newArticle = true; 
	}
	
	/**
	 * Saves the currently editable article. If {@link #newArticle} is set, the id of the 
	 * article is set to {@link #nextIdArticle} and the sql command is retrieved from 
	 * {@link QueryFactory#getInsertQueryFitArticle(FitArticle)}, otherwise from 
	 * {@link QueryFactory#getUpdateQueryFitArticle(FitArticle, FitArticle)}. The 
	 * latter returns null if no changes are made (no differences between {@link #article} 
	 * and {@link #articleOld}). In this case no transactions are conducted and false is returned. 
	 * @return Returns true if the article is saved, false otherwise. 
	 */
	public boolean saveArticle() {
		int newID = 0;
		if (newArticle) {
			db.saveArticle(article);
			if (article.isParentArticlePresent()){
				FitArticle parent = db.getArticleById(article.getParentArticle());
				newID = db.getMaxId();
				parent.setChildArticle(newID);
				db.saveArticle(parent);
			}
		} else {
			db.saveArticle(article);

		}
		
		return true;
	}
	
	public void saveContactPartner() {
		// TODO 
	}
	
	/**
	 * Deletes all tuples related to the currently set article. 
	 */
	public void deleteArticle() {
		db.deleteArticle(INSTANCE.article);
		
	}
	
	/**
	 * Method to check if changes to the article are made (with regard to {@link #articleOld}. 
	 * @return
	 */
	
	// TODO
	public boolean isChanged() {
		if (newArticle) {
			return true; 
		} 		
		return true;
	}	
	
	public void markAsEditing(boolean b) {
		if (!newArticle) {
			this.INSTANCE.article.setBeingUpdated(b);
			db.saveArticle(this.INSTANCE.article);
		}
	}
}
