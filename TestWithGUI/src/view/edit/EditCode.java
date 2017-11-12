package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import lombok.Getter;

public class EditCode extends ArticleElementPane {
	/**
	 * The Code at the end of each article
	 */
	@Getter
	private TextField editText; 
	@Getter 
	private Label label; 
	
	public EditCode(NumberBinding elementsWidthBinding) {
		super(elementsWidthBinding); 
		label = new Label("Code");
		label.prefWidthProperty().bind(elementsWidthBinding);
		this.setGraphic(label);
//		this.setText("Code");
		editText = new TextField(); 
		container.getChildren().add(editText); 
	}
	
	public void setEditText(String code) {
		editText.setText(code);
	}

}
