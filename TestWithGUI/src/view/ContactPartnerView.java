package view;

import controller.MsAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.ContactPartner;
import model.edit.ContactPerson;

public class ContactPartnerView extends Scene{

	private GridPane root;
	private Label infoLabel ;
	private TableView table = new TableView();
	private GridPane hbox = new GridPane();
	private VBox vbox = new VBox();
	private Button addButton = new Button("Add");
	private Button deleteButton = new Button("Delete");
	private Button exitButton = new Button("Exit");
	private TableColumn firstNameColumn = new TableColumn("Vorname");
	private TableColumn lastNameColumn = new TableColumn("Name");
	private TableColumn emailColumn = new TableColumn("Email");
	private TableColumn phoneColumn = new TableColumn("Telefon");
	private TableColumn departmentColumn = new TableColumn("Abteilung");
	
	
	public ContactPartnerView() {
		super(new Pane(), 800 , 600);
		root = new GridPane();
		root.setVgap(20);
		
		addButton.setMinWidth(100);
		deleteButton.setMinWidth(100);
		exitButton.setMinWidth(100);
			
		hbox.add(addButton, 0, 0);
		hbox.add(deleteButton, 1, 0);
		hbox.add(exitButton, 2, 0);

		hbox.setHgap(40);
		table.setPrefSize(780, 540);
		firstNameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.10));
		firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		
		lastNameColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.10));
		lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));

		
		emailColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.30));
		emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		
		phoneColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.15));
		phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
		
		departmentColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.35));
		departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
		
		MsAccess db = new MsAccess();
		ObservableList<ContactPartner> data = FXCollections.observableArrayList(db.getContactPersons());
		
				
		table.setItems(data);
		table.getColumns().addAll(firstNameColumn,lastNameColumn, emailColumn,phoneColumn, departmentColumn);
		
		
		vbox.getChildren().add(table);
		root.add(hbox, 0, 0);
		root.add(vbox, 0, 1);
		
		root.setPadding(new Insets(10,10,10,10));
		
		this.setRoot(root);
		
	}
	

}
