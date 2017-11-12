package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;
/**
 * Info Entry of an article, not quite special thing here
 * @author tuan
 *
 */
public class EditInfoListEntry extends ArticleElementPane {
	
	private Label name, value; 
	@Getter
	private TextField nameText, valueText; 
	
	public EditInfoListEntry(String name, String value, NumberBinding elementsWidthBinding) {
		
		super(elementsWidthBinding); 
		
		this.label = new Label("Informationslistenelement"); 
		this.label.prefWidthProperty().bind(elementsWidthBinding);;
		this.setGraphic(label);
//		this.setText("Informationslistenelement");
		
		this.name = new Label("Name"); 
		this.value = new Label("Wert"); 
		nameText = new TextField(name); 
		valueText = new TextField(value); 
		
		container.getChildren().addAll(this.name, nameText, this.value, valueText); 
		
	}

}
