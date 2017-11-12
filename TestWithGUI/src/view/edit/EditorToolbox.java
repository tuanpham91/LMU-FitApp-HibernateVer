package view.edit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import lombok.Getter;
import view.FontAwesome;
import view.GuiContainer;

public class EditorToolbox extends ToolBar {
	
	@Getter
	private Button delete, save, back, addHeadline, addInfoListElement, addEditBBTextArea, addContactPartner, addWebLink, addCode, expand, collapse; 
	private VBox neuesElementContainer, expCollContainer; 
	private Label neuesElement, expandCollapse; 
	private HBox neuesElementButtons, expCollButtons; 
	
	/**
	 * GUI class for Toolbox in EditPage
	 * 
	 * @param editWindow
	 */
	public EditorToolbox(EditWindow editWindow) {
		
		super(); 
		
		back = initializeButton(FontAwesome.backward, "Zurück zur Übersicht"); 
		save = initializeButton(FontAwesome.save, "Speichern"); 
		delete = initializeButton(FontAwesome.trash, "Löschen");
		
		neuesElement = new Label("Neues Element:"); 
		neuesElementButtons = new HBox(); 
		addHeadline        = initializeSmallButtons(FontAwesome.header, "Header H3-H5"); 
		addEditBBTextArea  = initializeSmallButtons(FontAwesome.file_code_o, "Formatierter Text"); 
		addInfoListElement = initializeSmallButtons(FontAwesome.th_list, "Informationsliste"); 
		addContactPartner  = initializeSmallButtons(FontAwesome.user, "Ansprechsperson"); 
		addWebLink         = initializeSmallButtons(FontAwesome.anchor, "Link"); 
		addCode            = initializeSmallButtons(FontAwesome.comment_o, "Code"); 
		neuesElementButtons.getChildren().addAll(addHeadline, addEditBBTextArea, addInfoListElement, addContactPartner, addWebLink, addCode); 
		neuesElementContainer = new VBox(); 
		neuesElementContainer.getChildren().addAll(neuesElement, neuesElementButtons); 
		neuesElementContainer.setAlignment(Pos.CENTER);
		neuesElementContainer.setPadding(new Insets(0, 0, 0, 50));
		
		expandCollapse = new Label("Ein-/Ausklappen:"); 
		expCollButtons = new HBox(); 
		collapse = initializeSmallButtons(FontAwesome.caret_square_o_up, "Einklappen"); 
		expand = initializeSmallButtons(FontAwesome.caret_square_o_down, "Ausklappen"); 
		expCollButtons.getChildren().addAll(collapse, expand); 
		expCollButtons.setAlignment(Pos.CENTER);
		expCollContainer = new VBox(); 
		expCollContainer.getChildren().addAll(expandCollapse, expCollButtons); 
		expCollContainer.setAlignment(Pos.CENTER);
		expCollContainer.setPadding(new Insets(0, 0, 0, 50));
		
		
		
		this.prefWidthProperty().bind(editWindow.widthProperty());
//		this.setMaxWidth(1150);
//		NumberBinding prefHeightBinding = Bindings.multiply(0.1, editWindow.heightProperty()); 
//		this.prefHeightProperty().bind(prefHeightBinding);
		this.prefHeightProperty().bind(save.heightProperty());
		this.setMaxHeight(100);
//		this.setPadding(new Insets(10, 10, 10, 10));
		
		this.getItems().addAll(back, save, delete, neuesElementContainer, expCollContainer); 
		
	}
	/**
	 * Setting of an Button
	 * @param iconId
	 * @param toolTip
	 * @return
	 */
	private Button initializeButton(String iconId, String toolTip) {
	 	Button b = new Button(GuiContainer.ICONS.getString(iconId));
	 	Tooltip t = new Tooltip(toolTip);
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	b.setStyle(FontAwesome.FONT_STYLE);
	 	// b.setTooltip(t);
	 	Tooltip.install(b, t);
	 	b.setMaxHeight(1000);
	 	b.setMaxWidth(1000);
	 	return b;
	}
	
	private Button initializeSmallButtons(String iconId, String toolTip) {
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
