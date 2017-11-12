package view;

import controller.Controller;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import lombok.Getter;

public class AlertCopy extends Alert {
	
	@Getter
	private CheckBox referencingCheckBox; 
	
	public AlertCopy() {
		super(AlertType.CONFIRMATION); 
		this.setDialogPane(new DialogPane() {
			@Override
			protected Node createDetailsButton() {
				CheckBox optOut = new CheckBox(); 
				optOut.setText("Mit Referenz zum ursprünglichen Artikel kopieren");
				referencingCheckBox = optOut; 
				return optOut; 
			}
		});
		this.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL); 
//		this.checkBox = new CheckBox("Mit Referenz zum ursprünglichen Artikel kopieren"); 
		this.setTitle("Artikel kopieren");
		this.setHeaderText("Von dem Artikel wird eine Kopie erstellt. Die Kopie wird erst gespeichert, wenn "
				+ "in der Bearbeitungs-Ansicht auf die Speichern-Schaltfläche gedrückt wird. ");
		
		this.getDialogPane().setExpandableContent(new Group());
		this.getDialogPane().setExpanded(true);
		
		this.getDialogPane().setPrefWidth(Controller.getGui().getOverviewTable().getWidth() / 2);
		this.getDialogPane().getChildren().stream()
				.filter(node -> node instanceof Label)
				.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
		
////		this.getDialogPane().getContent().getLayoutY(); 
//		this.getDialogPane().getChildren().add(checkBox); 
//		if (this.getDialogPane().getContent() == null) {
//			System.out.println("null");
//		}
//		checkBox.setLayoutY(this.getDialogPane().getContent().getLayoutY());
	}
	
}