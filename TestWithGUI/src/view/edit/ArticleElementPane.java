package view.edit;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import lombok.Getter;
import view.FontAwesome;
import view.GuiContainer;

public class ArticleElementPane extends TitledPane {
	
	@Getter
	protected VBox container; 
	private ToolBar toolBar; 
	@Getter
	private Button allUp, allDown, up, down, delete; 
	@Getter
	protected Label label; 
	/**
	 * Interface Class for  Article Elements in Edit
	 */
	public ArticleElementPane(NumberBinding elementsWidthBinding) {
		
		super(); 
		
		this.prefWidthProperty().bind(elementsWidthBinding);
				
		toolBar = new ToolBar(); 
		container = new VBox(); 
		
		NumberBinding widthBinding = Bindings.multiply(container.widthProperty(), 0.95); 
		this.maxWidthProperty().bind(widthBinding);

		allUp = initializeButton(FontAwesome.angle_double_up, "ganz nach oben"); 
		allDown = initializeButton(FontAwesome.angle_double_down, "ganz nach unten"); 
//		up = initializeButton(FontAwesome.caret_square_o_up, "nach oben"); 
//		down = initializeButton(FontAwesome.caret_square_o_down, "nach unten");
		up = initializeButton(FontAwesome.angle_up, "nach oben"); 
		down = initializeButton(FontAwesome.angle_down, "nach unten");		
		delete = initializeButton(FontAwesome.trash, "löschen"); 
		toolBar.getItems().addAll(allUp, allDown, up, down, delete); 
		
		container.getChildren().add(toolBar); 
		
		this.setPadding(new Insets(5, 0, 0, 10));
		
		this.setContent(container); 
	}
	/**
	 * Return an Button with the same Font, style.
	 */
	private Button initializeButton(String iconId, String toolTip) {
	 	Button b = new Button(GuiContainer.ICONS.getString(iconId));
	 	Tooltip t = new Tooltip(toolTip);
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	b.setStyle(FontAwesome.FONT_STYLE_SMALL);
	 	Tooltip.install(b, t);
	 	b.setMaxHeight(1000);
	 	b.setMaxWidth(1000);
	 	return b;
	}

}
