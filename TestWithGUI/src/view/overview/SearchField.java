package view.overview;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import view.ImageViewResizable;
/*
 * Class for search function in Overview
 * All the search criterium will be recorded here
 */
@Getter
public class SearchField extends StackPane {
	
	private TextField textField; 
	private ImageViewResizable imageView; 
	
	final Image lensIcon = new Image("resources/search-lens.png"); 
	final Image crossIcon = new Image("resources/cross.png"); 
	
	private BooleanProperty changed = new SimpleBooleanProperty(); 
	
	public SearchField() {
		
		this.textField = new TextField(); 
		this.imageView = new ImageViewResizable(); 
		this.imageView.setImage(lensIcon);
		this.changed.set(false);
		
		textField.paddingProperty().addListener(new ChangeListener<Insets>() {
			@Override
			public void changed(ObservableValue<? extends Insets> observable, Insets oldValue, Insets newValue) {
				Insets newInsets = new Insets(newValue.getTop(), newValue.getLeft()*3, newValue.getBottom(), newValue.getLeft());
				textField.setPadding(newInsets);
			}
		});
		
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				changed.set(true);
			}
		});
		
		changed.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					imageView.setImage(lensIcon);
				} else {
					imageView.setImage(crossIcon);
				}
			}
		});
		
		this.prefHeightProperty().bind(textField.heightProperty());
		
		StackPane.setAlignment(imageView, Pos.CENTER_RIGHT);
		
		this.getChildren().addAll(textField, imageView); 
		
	}
	
	public void setLens() {
		imageView.setImage(lensIcon);
	}
	
	public void setCross() {
		imageView.setImage(crossIcon);
	}
	
}
