package view;

import controller.Ordering;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import lombok.Getter;

public class Sort extends Scene {
	
	private GridPane root; 
	
	private Label label1; 
	private Label label2; 
	private Label label3; 
	private Label label4; 
	
	@Getter
	private ComboBox<String> comboBox1, comboBox2, comboBox3, comboBox4; 
	@Getter
	private RadioButton ascending1, descending1, ascending2, descending2, ascending3, descending3, ascending4, descending4; 
	private ToggleGroup ordering1, ordering2, ordering3, ordering4; 
	
	@Getter
	private Button ok, cancel, reset; 
	/**
	 * Interface Class for Sorting Articless
	 */
	public Sort() {
		
		super(new Pane(), 450, 530); 
		
		root = new GridPane(); 
		
		label1 = new Label("Merkmal 1");
		GridPane.setMargin(label1, new Insets(20, 0, 0, 0));
		label2 = new Label("Merkmal 2"); 
		GridPane.setMargin(label2, new Insets(20, 0, 0, 0));
		label3 = new Label("Merkmal 3"); 
		GridPane.setMargin(label3, new Insets(20, 0, 0, 0));
		label4 = new Label("Merkmal 4"); 
		GridPane.setMargin(label4, new Insets(20, 0, 0, 0));
		
		comboBox1 = new ComboBox<>(); 
		comboBox1.setMaxWidth(1000);
		GridPane.setMargin(comboBox1, new Insets(0, 0, 0, 20));
		comboBox1.getItems().add(Ordering.NONE.getItemName()); 
		comboBox1.getItems().add(Ordering.APPLICATION_DEADLINE.getItemName()); 
		comboBox1.getItems().add(Ordering.PRIORITY.getItemName()); 
		comboBox1.getItems().add(Ordering.RUBRIC.getItemName()); 
		comboBox1.getItems().add(Ordering.SUBJECT.getItemName()); 
		ordering1 = new ToggleGroup(); 
		ascending1 = new RadioButton("Aufsteigend"); 
		ascending1.setToggleGroup(ordering1);
		ascending1.setSelected(true);
		descending1 = new RadioButton("Absteigend"); 
		descending1.setToggleGroup(ordering1);
		
		comboBox2 = new ComboBox<>(); 
		comboBox2.setMaxWidth(1000);
		GridPane.setMargin(comboBox2, new Insets(0, 0, 0, 20));
		comboBox2.getItems().add(Ordering.NONE.getItemName()); 
		comboBox2.getItems().add(Ordering.APPLICATION_DEADLINE.getItemName()); 
		comboBox2.getItems().add(Ordering.PRIORITY.getItemName()); 
		comboBox2.getItems().add(Ordering.RUBRIC.getItemName()); 
		comboBox2.getItems().add(Ordering.SUBJECT.getItemName()); 
		comboBox2.setDisable(true);
		ordering2 = new ToggleGroup(); 
		ascending2 = new RadioButton("Aufsteigend"); 
		ascending2.setToggleGroup(ordering2);
		ascending2.setSelected(true);
		ascending2.setDisable(true);
		descending2 = new RadioButton("Absteigend"); 
		descending2.setToggleGroup(ordering2);
		descending2.setDisable(true);
		
		comboBox3 = new ComboBox<>(); 
		comboBox3.setMaxWidth(1000);
		GridPane.setMargin(comboBox3, new Insets(0, 0, 0, 20));
		comboBox3.getItems().add(Ordering.NONE.getItemName()); 
		comboBox3.getItems().add(Ordering.APPLICATION_DEADLINE.getItemName()); 
		comboBox3.getItems().add(Ordering.PRIORITY.getItemName()); 
		comboBox3.getItems().add(Ordering.RUBRIC.getItemName()); 
		comboBox3.getItems().add(Ordering.SUBJECT.getItemName()); 
		comboBox3.setDisable(true);
		ordering3 = new ToggleGroup(); 
		ascending3 = new RadioButton("Aufsteigend"); 
		ascending3.setToggleGroup(ordering3);
		ascending3.setSelected(true);
		ascending3.setDisable(true);
		descending3 = new RadioButton("Absteigend"); 
		descending3.setToggleGroup(ordering3);
		descending3.setDisable(true);
		
		comboBox4 = new ComboBox<>(); 
		comboBox4.setMaxWidth(1000);
		GridPane.setMargin(comboBox4, new Insets(0, 0, 0, 20));
		comboBox4.getItems().add(Ordering.NONE.getItemName()); 
		comboBox4.getItems().add(Ordering.APPLICATION_DEADLINE.getItemName()); 
		comboBox4.getItems().add(Ordering.PRIORITY.getItemName()); 
		comboBox4.getItems().add(Ordering.RUBRIC.getItemName()); 
		comboBox4.getItems().add(Ordering.SUBJECT.getItemName()); 
		comboBox4.setDisable(true);
		ordering4 = new ToggleGroup(); 
		ascending4 = new RadioButton("Aufsteigend"); 
		ascending4.setToggleGroup(ordering4);
		ascending4.setSelected(true);
		ascending4.setDisable(true);
		descending4 = new RadioButton("Absteigend"); 
		descending4.setToggleGroup(ordering4);
		descending4.setDisable(true);
		
		comboBox1.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals(Ordering.NONE.getItemName())) {
					comboBox2.setValue(Ordering.NONE.getItemName());
					ascending2.setSelected(true);
					comboBox2.setDisable(true);
					ascending2.setDisable(true);
					descending2.setDisable(true);
					comboBox3.setValue(Ordering.NONE.getItemName());
					ascending3.setSelected(true);
					comboBox3.setDisable(true);
					ascending3.setDisable(true);
					descending3.setDisable(true);
					comboBox4.setValue(Ordering.NONE.getItemName());
					ascending4.setSelected(true);
					comboBox4.setDisable(true);
					ascending4.setDisable(true);
					descending4.setDisable(true);
				} else {
					comboBox2.setDisable(false);
					ascending2.setDisable(false);
					descending2.setDisable(false);
				}
			}
		});
		
		comboBox2.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals(Ordering.NONE.getItemName())) {
					comboBox3.setValue(Ordering.NONE.getItemName());
					ascending3.setSelected(true);
					comboBox3.setDisable(true);
					ascending3.setDisable(true);
					descending3.setDisable(true);
					comboBox4.setValue(Ordering.NONE.getItemName());
					ascending4.setSelected(true);
					comboBox4.setDisable(true);
					ascending4.setDisable(true);
					descending4.setDisable(true);
				} else {
					comboBox3.setDisable(false);
					ascending3.setDisable(false);
					descending3.setDisable(false);
				}
			}
		});
		
		comboBox3.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue.equals(Ordering.NONE.getItemName())) {
					comboBox4.setValue(Ordering.NONE.getItemName());
					ascending4.setSelected(true);
					comboBox4.setDisable(true);
					ascending4.setDisable(true);
					descending4.setDisable(true);
				} else {
					comboBox4.setDisable(false);
					ascending4.setDisable(false);
					descending4.setDisable(false);
				}
			}
		});
		
		ok = new Button("OK"); 
		ok.setMaxWidth(1000);
		GridPane.setMargin(ok, new Insets(30, 0, 0, 0));
		cancel = new Button("Abbrechen"); 
		cancel.setMaxWidth(1000);
		GridPane.setMargin(cancel, new Insets(30, 0, 0, 0));
		reset = new Button("Zurücksetzen"); 
		reset.setMaxWidth(1000);
//		GridPane.setMargin(reset, new Insets(30, 0, 0, 0));
		
		root.add(label1, 0, 0);
		root.add(comboBox1, 0, 1, 2, 2);
		root.add(ascending1, 2, 1);
		root.add(descending1, 2, 2);
		
		root.add(label2, 0, 3);
		root.add(comboBox2, 0, 4, 2, 2);
		root.add(ascending2, 2, 4);
		root.add(descending2, 2, 5);
		
		root.add(label3, 0, 6);
		root.add(comboBox3, 0, 7, 2, 2);
		root.add(ascending3, 2, 7);
		root.add(descending3, 2, 8);
		
		root.add(label4, 0, 9);
		root.add(comboBox4, 0, 10, 2, 2);
		root.add(ascending4, 2, 10);
		root.add(descending4, 2, 11);
		
		root.add(ok, 1, 12);
		root.add(cancel, 2, 12);
		root.add(reset, 2, 13);
		
		root.setHgap(10);
		root.setVgap(10);
		
		ColumnConstraints c1 = new ColumnConstraints(); 
		c1.setPercentWidth(40);
		ColumnConstraints c2 = new ColumnConstraints(); 
		c2.setPercentWidth(30);
		ColumnConstraints c3 = new ColumnConstraints(); 
		c3.setPercentWidth(30);
		
		root.getColumnConstraints().addAll(c1, c2, c3); 
		
		root.setPadding(new Insets(10, 10, 10, 10));
		
		this.setRoot(root);
		
	}
		/**
		 * Reset the Interface
		 */
	public void reset() {
		comboBox1.setValue(Ordering.NONE.getItemName());
		ascending1.setSelected(true);
	}
	
	public boolean isActive() {
		
		if (!comboBox1.getValue().equals(Ordering.NONE.getItemName()) || !comboBox2.getValue().equals(Ordering.NONE.getItemName()) 
				|| !comboBox3.getValue().equals(Ordering.NONE.getItemName()) || !comboBox4.getValue().equals(Ordering.NONE.getItemName())) {
			return true; 
		} else {
			return false; 
		}
		
	}

}
