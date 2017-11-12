package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import lombok.Getter;
import model.edit.TextEntry.TextEntryFormat;

/**
 * Gui Class for header of edit page
 * @author tuan
 *
 */
public class EditHeader extends ArticleElementPane {
	
	@Getter
	private TextField header; 
	private HBox toggleContainer; 
	@Getter
	private RadioButton h3, h4, h5, h6; 
	private ToggleGroup toggleGroup; 
	
	public EditHeader(String headerText, TextEntryFormat format, NumberBinding elementsWidthBinding) {
		
		super(elementsWidthBinding); 
		
		this.label = new Label("Überschrift"); 
		this.label.prefWidthProperty().bind(elementsWidthBinding);;
//		this.setText("Überschirft");
		this.setGraphic(label);
		
		toggleGroup = new ToggleGroup(); 
		header = new TextField(headerText); 
		h3 = new RadioButton("H3"); 
		h3.setToggleGroup(toggleGroup);
		h4 = new RadioButton("H4"); 
		h4.setToggleGroup(toggleGroup);
		h5 = new RadioButton("H5"); 
		h5.setToggleGroup(toggleGroup);
		h6 = new RadioButton("H6"); 
		h6.setToggleGroup(toggleGroup);
		
		switch (format) {
		case HEADING_3: 
			h3.setSelected(true);
			break; 
		case HEADING_4: 
			h4.setSelected(true);
			break; 
		case HEADING_5: 
			h5.setSelected(true);
			break; 
		case HEADING_6: 
			h6.setSelected(true);
			break; 
		default:
			break; 
		}
		
		toggleContainer = new HBox(); 
		toggleContainer.getChildren().addAll(h3, h4, h5, h6); 
		toggleContainer.setSpacing(10);
		
		container.getChildren().addAll(header, toggleContainer); 
		
	}
	
}
