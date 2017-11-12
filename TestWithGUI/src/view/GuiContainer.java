package view;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.Getter;
import view.edit.EditWindow;
import view.edit.EditorToolbox;
import view.overview.OverviewTable;

public class GuiContainer extends Scene {
	
	public final static ResourceBundle ICONS = ResourceBundle.getBundle("view.fontawsome");
	
	private StackPane root; 
	
	@Getter
	private OverviewTable overviewTable; 
	@Getter
	private EditWindow editWindow; 
	@Getter 
	Sort sortingWindow; 
	@Getter 
	Search searchWindow; 
	@Getter
	ContactPartnerView contactView;
	@Getter
	private Stage sortingStage, searchStage, contactPartnerStage; 
	/**GuiContainer contains all until now available scenes, graphical classes.
	 * 
	 */
	public GuiContainer() {
				
		super(new StackPane(), Screen.getPrimary().getVisualBounds().getMaxX(), 700); 
				
		InputStream font = EditorToolbox.class.getResourceAsStream("/resources/fontawesome-webfont.ttf");
		Font.loadFont(font, 10);
		
		root = new StackPane(); 
		this.setRoot(this.root);
		
		this.overviewTable = new OverviewTable(this); 
		this.editWindow = new EditWindow(this); 
		this.root.getChildren().add(overviewTable); 
		this.root.getChildren().add(editWindow); 
			
		this.sortingWindow = new Sort(); 
		this.sortingStage = new Stage(); 
		this.sortingStage.setTitle("Sortieren");
		this.sortingStage.setScene(sortingWindow);
		
		this.searchWindow = new Search(); 
		this.searchStage = new Stage(); 
		this.searchStage.setTitle("Erweiterte Suche");
		this.searchStage.setScene(searchWindow);
		
		this.contactView = new ContactPartnerView();
		this.contactPartnerStage = new Stage();
		this.contactPartnerStage.setTitle("Kontaktpartner");
		this.contactPartnerStage.setScene(contactView);
		
		this.overviewTable.setVisible(true);
		this.editWindow.setVisible(false);
		
		
	}
	/**
	 * Switch between scenes : Overview and Edit
	 */
	public void showEditWindow() {
//		Controller.getRefreshThread().refresh = true; 
		editWindow.getEditAccordion().setExpandedPane(null);
		overviewTable.setVisible(false);
		editWindow.setVisible(true);
	}
	public void showContact() {
		this.contactPartnerStage.show();
		this.contactPartnerStage.toFront();
	}
	public void showTable() {
//		Controller.getRefreshThread().refresh = false; 
		editWindow.setVisible(false);
		overviewTable.setVisible(true);
	}
	
	public static void showCopyFailure() {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setHeaderText("Fehler Kopieren");
		alert.setContentText("Von diesem Artikel konnte keine Kopie mit Referenz zum ursprünglichen Artikel erstellt werden, da dieser Artikel bereits kopiert wurde. ");
		alert.showAndWait(); 
	}
	/** Chooser for
	 * Directory Windows for Export functions
	 * @return
	 */
	public File getExportDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Html Export");
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        return selectedDirectory;
	}
	
	/**
	 * Alerting users to put in Application Deadline
	 */
	public void missingApplicationDeadline() {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setHeaderText("Fehlende Bewerbungsfrist");
		alert.setContentText("Der Artikel konnte nicht gespeichert werden. Bitte noch eine Bewerbungsfrist angeben");
		alert.showAndWait(); 
	}
	/**
	 * Alerting users, in case the article is recurrent, to put in the next Deadline
	 */
	public void missingRecurrentFrom() {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setHeaderText("Fehlende nächste Bewerbungsfrist");
		alert.setContentText("Geben Sie die nächste Bewerbungsfrist ein!");
		alert.showAndWait(); 
	}
	/**
	 * Confirm deleting the article
	 * @return
	 */
	public boolean deleteConfirmation() {
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.setTitle("Artikel löschen");
		alert.setContentText("Soll der Artikel unwiederruflich gelöscht werden?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		return false;
	}
	/**
	 * Alerting users that no changes were made in the current session
	 */
	public static void noChangesMade() {
		Alert alert = new Alert(AlertType.INFORMATION); 
		alert.setHeaderText("Keine Änderungen");
		alert.setContentText("Es wurden keine Änderungen vorgenommen");
		alert.showAndWait(); 
	}
	/**
	 *  Alerting erros when saving a article
	 */
	public static void saveError() {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setHeaderText("Fehler Datenbank");
		alert.setContentText("Der Artikel konnte nicht gespeichert werden. Bitte nochmal probieren. ");
		alert.showAndWait(); 
	}
	
	public static void deleteError() {
		Alert alert = new Alert(AlertType.ERROR); 
		alert.setHeaderText("Fehler Datenbank");
		alert.setContentText("Der Artikel konnte nicht gelöscht werden. Bitte nochmal probieren. ");
		alert.showAndWait(); 
	}	
	
	/**
	 * Noticing users that the made changes weren't saved. Need to click the save Button.
	 * @return
	 */
	public boolean discardChanges() {
		Alert alert = new Alert(AlertType.CONFIRMATION); 
		alert.setHeaderText("Änderungen verwerfen?");
		alert.setContentText("Soll zurück zur Übersicht gewechselt werden? In diesem Fall werden die Änderungen verworfen.");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			return true;
		}
		return false;	
	}
	
	public void showSort() {
		sortingStage.show();
		sortingStage.toFront();
	}
	public void closeContact() {
		this.contactPartnerStage.close();
	}
	public void closeSort() {
		sortingStage.close();
	}
	
	public void showSearch() {
		searchStage.show();
		searchStage.toFront();
	}
	
	public void closeSearch() {
		searchStage.close();
	}
	
}
