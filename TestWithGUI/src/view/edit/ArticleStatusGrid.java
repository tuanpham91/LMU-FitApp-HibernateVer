package view.edit;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import model.FitArticle;

@Getter
public class ArticleStatusGrid extends GridPane {
	
	private CheckBox englVersion, exportFit, exportWeb; 
	private CheckBox completed, corrected, exportReady; 
	private CheckBox recurrent, running; 
	private DatePicker nextApplicationDeadline, applicationDeadline, recurrentCheckFrom; 
	private VBox left, middle, right; 
	//Tuan
	private Label applicationDeadlineLabel, nextDeadlineLabel, nextProcessDeadlineLabel;
	/**
	 *  Initialize Status Grid
	 */
	public ArticleStatusGrid(NumberBinding elementsWidthBinding) {
		
		super(); 
		
		this.prefWidthProperty().bind(elementsWidthBinding);
		
		getStylesheets().add("/view/ArticleEditPage.css"); 
		getStyleClass().add("article-fixed-fields"); 
		
		englVersion = new CheckBox("Englische Version"); 
		exportFit = new CheckBox("Export FiT"); 
		exportWeb = new CheckBox("Export WWW"); 
		
		left = new VBox(); 
		left.getChildren().addAll(englVersion, exportFit, exportWeb); 
		
		completed = new CheckBox("Abgeschlossen"); 
		corrected = new CheckBox("Korrigiert"); 
		exportReady = new CheckBox("Exportfertig"); 
		
		completed.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					corrected.setSelected(false);
				}
			}
		});
		corrected.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					completed.setSelected(true);
				} else {
					exportReady.setSelected(false);
				}
			}
		});
		exportReady.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					corrected.setSelected(true);
				}
			}
		});
		
		middle = new VBox(); 
		middle.getChildren().addAll(completed, corrected, exportReady); 
		
		running = new CheckBox("Laufend"); 
		recurrent = new CheckBox("Wiederkehrend"); 
		nextApplicationDeadline = new DatePicker(); 
		applicationDeadline = new DatePicker(); 
		recurrentCheckFrom = new DatePicker();
		
		// tuan
		applicationDeadlineLabel = new Label("Aktuelle Bewerbungsfrist: "); 
		nextDeadlineLabel = new Label("Wahrscheinlich nächste Frist: ");
		nextProcessDeadlineLabel = new Label ("Wieder bearbeiten ab:");
		// Tuan
		
		right = new VBox(); 
		right.getChildren().addAll(running, recurrent, applicationDeadlineLabel, applicationDeadline, nextDeadlineLabel, nextApplicationDeadline, nextProcessDeadlineLabel, recurrentCheckFrom); 
		
		this.add(left, 0, 0);
		this.add(middle, 1, 0);
		this.add(right, 2, 0);
		
	}
	
	/**
	 * Load all the attributes of an article (in most cases the current article) and show in on the window
	 * @param article
	 */
	public void setArticle(FitArticle article) {

		englVersion.setSelected(article.getEn());
		exportFit.setSelected(article.getExportFit());
		exportWeb.setSelected(article.getExportWeb());
		
		completed.setSelected(article.getCompleted());
		corrected.setSelected(article.getAuthorCorrected());
		exportReady.setSelected(article.getExportReady());
		
		running.setSelected(article.isApplicationAnytime());
		if (article.getRecurrent()) {
			recurrent.setSelected(true);
		} else {
			nextApplicationDeadline.setDisable(true);
		}
		
		nextApplicationDeadline.setValue(toLocalDate(article.getNextApplicationDeadline()));
		applicationDeadline.setValue(toLocalDate(article.getApplicationDeadline()));
		recurrentCheckFrom.setValue(toLocalDate(article.getRecurrentCheckFrom()));

	}
	/** 
	 * After copying , the new copy should be considered as unchecked, uncorrected and not export ready
	 * This function make sure the new Edit Window will show the status of the new Article
	 * 
	 */
	public void setCopied() {
		englVersion.setSelected(false);
		exportFit.setSelected(false);
		exportWeb.setSelected(false);
		
		completed.setSelected(false);
		corrected.setSelected(false);
		exportReady.setSelected(false);
		
		applicationDeadline.setValue(nextApplicationDeadline.getValue());
		nextApplicationDeadline.setValue(null);
		recurrentCheckFrom.setValue(null);
		
		if (!running.isSelected()) {
			// don't reset if the article is running (-> should still be running) 
			recurrent.setSelected(false);			
		}
	}
	
	/**
	 * A static method designed to translate  a Date in to LocalDate data type, for some functions its very helpfull
	 */
	private static LocalDate toLocalDate(Date d) {
		if(d == null) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyyMMdd");
		String formatted = simpleFormat.format(d);
		return LocalDate.parse(formatted, formatter);		
	}
	
}