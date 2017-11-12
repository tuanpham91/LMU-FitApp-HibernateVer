package view.edit;

import java.util.Iterator;
import java.util.List;

import javafx.beans.binding.NumberBinding;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import lombok.Getter;
import model.FitArticle;
import model.edit.Code;
import model.edit.ContactPerson;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.WebLinkEntry;

public class ArticleElements extends VBox {
	
	@Getter
	private NumberBinding elementsWidthBinding; 
	
	public ArticleElements(NumberBinding elementsWidthBinding) {
		super(); 
		this.elementsWidthBinding = elementsWidthBinding; 
		this.prefWidthProperty().bind(elementsWidthBinding);
	}
	/**
	 * Loading all the attributes of the current Article and show it accordingly 
	 * @param article is the current Article
	 */
	public void setArticle(FitArticle article) {
		
		this.getChildren().clear();

		Iterator<FitArticleElement> elementsIterator = article.getElements().iterator(); 
		while (elementsIterator.hasNext()) {
			DragAndDropLabel dragLabel = new DragAndDropLabel(); 
			this.getChildren().add(dragLabel); 
			FitArticleElement e = elementsIterator.next(); 
			if (e instanceof Code) {
				EditCode editCode = new EditCode(elementsWidthBinding); 
				editCode.setEditText(((Code) e).getCode());
				this.getChildren().add(editCode); 
			} else if (e instanceof TextEntry) {
				ArticleElementPane editElement; 
				switch (((TextEntry) e).getFormat()) {
				case HEADING_3: 
				case HEADING_4: 
				case HEADING_5: 
				case HEADING_6: 
					editElement = new EditHeader(((TextEntry) e).getContent(), ((TextEntry) e).getFormat(), elementsWidthBinding); 
					break; 
				case TEXT_BB: 
					editElement = new EditText(((TextEntry) e).getContent(), elementsWidthBinding); 
					break; 
				default: 
					editElement = new ArticleElementPane(elementsWidthBinding); 
					break; 
				}
				this.getChildren().add(editElement); 
				
			} else if (e instanceof InfoListEntry) {
				EditInfoListEntry editInfo = new EditInfoListEntry(((InfoListEntry) e).getName(), ((InfoListEntry) e).getValue(), elementsWidthBinding); 
				this.getChildren().add(editInfo); 
			} else if (e instanceof WebLinkEntry) {
				EditWebLink editLink = new EditWebLink(((WebLinkEntry) e).getInfo(), ((WebLinkEntry) e).url().get(), elementsWidthBinding); 
				this.getChildren().add(editLink); 
			} else if (e instanceof ContactPerson) {
				EditConatcPerson editContact = new EditConatcPerson(elementsWidthBinding); 
				this.getChildren().add(editContact); 
				// TODO 
			}
		}
		
		DragAndDropLabel dragLabel = new DragAndDropLabel(); 
		this.getChildren().add(dragLabel); 
		
	}
	
	public DragAndDropLabel addDragLabel() {
		DragAndDropLabel dragLabel = new DragAndDropLabel(); 
		this.getChildren().add(dragLabel); 		
		return dragLabel; 
	}
	
	public void swapChildren(int i, int j) {
		// indices acc to list indices (starting with 0)
//		System.out.println("swapping " + i + " with " + j);
		List<Node> children = this.getChildren(); 
		Node oldI = children.get(i); 
		Node oldJ = children.get(j); 
		if (i < j) {
			children.remove(i); 
			children.remove(j-1); 
			children.add(i, oldJ);
			children.add(j, oldI); 
		} else {
			children.remove(i); 
			children.remove(j); 
			children.add(j, oldI);
			children.add(i, oldJ);
		}
	}
	
	public class DragAndDropLabel extends Label {
		
		public DragAndDropLabel() {
			super(); 
			this.setPrefWidth(1000);
			this.setMaxWidth(1000);
			this.setPrefHeight(5);
		}
		
//		public void setIndex(int newIndex) {
//			index = newIndex; 
//		}
//		
//		public int getIndex() {
//			return index; 
//		}
		
	}

}
