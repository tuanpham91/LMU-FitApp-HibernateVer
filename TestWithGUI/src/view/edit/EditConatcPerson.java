package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.Label;

public class EditConatcPerson extends ArticleElementPane {
	
	private Label labelNa; 
	/**
	 * Contact Person, At the moment is yet to be implemented
	 */
	public EditConatcPerson(NumberBinding elementsWidthBinding) {
		super(elementsWidthBinding); 
		labelNa = new Label("Noch nicht aktiv"); 
		label = new Label("Ansprechpartner"); 
		this.label.prefWidthProperty().bind(elementsWidthBinding);;
//		this.setText("Ansprechpartner");
		this.setGraphic(label);
		container.getChildren().add(labelNa); 
	}

}
