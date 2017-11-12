package view.overview;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import lombok.Getter;
import view.GuiContainer;

public class OverviewTable extends VBox {
	
	@Getter
	private GuiContainer container; 
	
	@Getter
	private OverviewTableHeader header; 
	@Getter
	private ArticlesTable table; 
	@Getter 
	private Label count; 
	/*
	 * Initialize the Overview Table, note that GuiContainer is a class which contains every graphical elements of the programm
	 * Which can be called by the container object
	 */
	public OverviewTable(GuiContainer container) {
		
		super(); 
		
		this.container = container; 
		
		prefWidthProperty().bind(container.widthProperty());
		prefHeightProperty().bind(container.heightProperty());
				
		this.header = new OverviewTableHeader(this);
		
		this.table = new ArticlesTable(this); 
		VBox.setVgrow(table, Priority.ALWAYS);
		
		this.count = new Label("Insgesamt 0 Artikel"); 
		this.count.setMaxWidth(10000);
		this.count.setAlignment(Pos.CENTER);
		this.count.setPadding(new Insets(10, 10, 10, 10));
		this.count.setStyle("-fx-background-color: #cccccc; ");
		InnerShadow is = new InnerShadow(); 
		is.setOffsetX(1.0f);
		is.setOffsetY(1.0f);
		is.setColor(Color.GREY);
		this.count.setEffect(is);
		
		this.getChildren().add(header); 
		this.getChildren().add(table); 
		this.getChildren().add(count); 
		
	}
	
	public void setCount(int c) {
		this.count.setText("Insgesamt " + c + " Artikel");
	}

}
