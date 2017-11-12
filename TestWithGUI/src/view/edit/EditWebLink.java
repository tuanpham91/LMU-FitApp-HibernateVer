package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

public class EditWebLink extends ArticleElementPane {
	
	private Label info, url; 
	@Getter
	private TextField infoText, urlText; 

	public EditWebLink(String info, String url, NumberBinding elementsWidthBinding) {
		
		super(elementsWidthBinding); 
		
		this.info = new Label("Information"); 
		this.url = new Label("URL"); 
		this.infoText = new TextField(info);
		this.urlText = new TextField(url); 
		
		this.label = new Label("Link");
		this.label.prefWidthProperty().bind(elementsWidthBinding);;
		this.setGraphic(label);
//		this.setText("Link");
		
		container.getChildren().addAll(this.info, infoText, this.url, urlText); 
		
	}
	
}
