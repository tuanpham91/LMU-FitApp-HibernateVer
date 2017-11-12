package view.overview;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.Getter;
import view.ImageViewResizable;
/*
 * Initialize the Header of Overview where all the Buttons, RadioButtons filter are
 * 
 */
@Getter
public class OverviewTableHeader extends GridPane {
	
	private Label fitLogo; 
	
	private Button standard; 
	private Button refresh; 

	private ToggleGroup filterToggleGroup; 

	private Label archive; 
	private RadioButton withArchive; 
	private RadioButton onlyArchive; 
	
	private Label nichtAbgeschlossen; 
	private RadioButton nichtAbgeschlossenAktuell; 
	private RadioButton nichtAbgeschlossenAlle; 

	private Label nichtKorrigiert; 
	private RadioButton nichtKorrigiertAktuell; 
	private RadioButton nichtKorrigiertAlle;
	
	private Label nichtExportfertig; 
	private RadioButton nichtExportfertigAktuell; 
	private RadioButton nichtExportfertigAlle;
	
	private Label exportFertigLabel; 
	private RadioButton exportFertigAktuell; 
	private RadioButton exportFertigAlle; 
	
	private Label edit; 
	private Button contactPartner; 
	private Button newArticle; 
	
	private Label export; 
	private Button exportIdml; 
	private Button resetExported; 
	private Button exportHtml; 
	
	private SearchField searchField; 
	private Button searchButton; 
	private Button searchResetButton; 
	
	private Button sort; 
	private Button sortResetButton; 
	
	private Label zoomLabel; 
	@Getter						// called in ArticlesTable.renderNextPage()
	private Slider zoom; 
	
	/*
	 * When initialize a new OverviewTableHeader, we should have a Table for it first
	 */
	public OverviewTableHeader(OverviewTable table) {
		
		super(); 
		
		this.prefWidthProperty().bind(table.widthProperty());
//		this.setMaxWidth(1200);
		NumberBinding maxHeightBinding = Bindings.multiply(0.1, table.heightProperty()); 
		this.maxHeightProperty().bind(maxHeightBinding);
		this.setMinHeight(140);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setHgap(20);
		this.setVgap(5);
				
		fitLogo = new Label(); 
		Image fitLogoImage = new Image("resources/fit-app-logo.jpg"); 
		ImageViewResizable fitLogoView = new ImageViewResizable();
		fitLogoView.setImage(fitLogoImage);
		fitLogo.setGraphic(fitLogoView);

		standard = new Button("Standard"); 
		standard.setMaxWidth(1000);
		standard.setMaxHeight(1000);
		standard.setMinWidth(0);
		refresh = new Button(); 
//		refresh.getStyleClass().add("button-refresh"); 
		Image refreshImage = new Image("resources/refresh.png"); 
		ImageView refreshView = new ImageView(); 
		refreshView.setPreserveRatio(true);
		refreshView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.10));
		refreshView.setImage(refreshImage);
		refresh.setGraphic(refreshView); 
		refresh.setMaxWidth(1000);
		refresh.setMinWidth(20);

		filterToggleGroup = new ToggleGroup(); 

		archive = new Label("Archiv"); 
		withArchive = new RadioButton("Mit Archiv"); 
//		withArchive.setPadding(Insets.EMPTY);
		withArchive.setToggleGroup(filterToggleGroup);
		onlyArchive = new RadioButton("Nur Archiv"); 
//		onlyArchive.setPadding(Insets.EMPTY);
		onlyArchive.setToggleGroup(filterToggleGroup);
		
		nichtAbgeschlossen = new Label("Nicht \nabgeschlossen"); 
		nichtAbgeschlossenAktuell = new RadioButton("aktuell"); 
		nichtAbgeschlossenAktuell.setToggleGroup(filterToggleGroup);
		nichtAbgeschlossenAlle = new RadioButton("alle");
		nichtAbgeschlossenAlle.setToggleGroup(filterToggleGroup);
		
		nichtKorrigiert = new Label("Abgeschlossen, \nnicht korrigiert"); 
		nichtKorrigiertAktuell = new RadioButton("aktuell"); 
		nichtKorrigiertAktuell.setToggleGroup(filterToggleGroup);
		nichtKorrigiertAlle = new RadioButton("alle"); 
		nichtKorrigiertAlle.setToggleGroup(filterToggleGroup);
		
		nichtExportfertig = new Label("Korrigiert, \nnicht exportfertig"); 
		nichtExportfertigAktuell = new RadioButton("aktuell"); 
		nichtExportfertigAktuell.setToggleGroup(filterToggleGroup);
		nichtExportfertigAlle = new RadioButton("alle"); 
		nichtExportfertigAlle.setToggleGroup(filterToggleGroup);
		
		exportFertigLabel = new Label("Exportfertig"); 
		exportFertigAktuell = new RadioButton("aktuell"); 
		exportFertigAktuell.setToggleGroup(filterToggleGroup);
		exportFertigAlle = new RadioButton("alle"); 
		exportFertigAlle.setToggleGroup(filterToggleGroup);
				
		edit = new Label("Bearbeiten"); 
		edit.setMaxWidth(1000);
		edit.setAlignment(Pos.CENTER);
		newArticle = new Button(); 
		Image newArticleImage = new Image("resources/new_article.png"); 
		ImageView newArticleView = new ImageView(); 
		newArticleView.setPreserveRatio(true);
//		newArticleView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.25));
		newArticleView.fitHeightProperty().bind(Bindings.min(30, Bindings.multiply(this.heightProperty(), 0.25)));
		newArticleView.setImage(newArticleImage);
		newArticle.setGraphic(newArticleView); 
		newArticle.setPadding(new Insets(10, 10, 10, 10));
		newArticle.setMaxWidth(1000);
		newArticle.setMinWidth(50);
		Tooltip t = new Tooltip(); 
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	t.setText("Neuer Artikel");
	 	newArticle.setTooltip(t);
		contactPartner = new Button(); 
		contactPartner.setMaxWidth(1000);
		contactPartner.setMinWidth(50);
		Image addContactImage = new Image("resources/add_contact.png"); 
		ImageView addContactView = new ImageView(); 
		addContactView.setPreserveRatio(true);
//		addContactView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.25));
		addContactView.fitHeightProperty().bind(Bindings.min(30, Bindings.multiply(this.heightProperty(), 0.25)));
		addContactView.setImage(addContactImage);
		contactPartner.setGraphic(addContactView); 
		contactPartner.setPadding(new Insets(10, 10, 10, 10));
		export = new Label("Export"); 
		export.setMaxWidth(1000);
		export.setAlignment(Pos.CENTER);
		exportIdml = new Button(); 
		exportIdml.setMaxWidth(1000);
		exportIdml.setMinWidth(50);
		Image exportIdmlImage = new Image("resources/ico_fit.png"); 
		ImageView exportIdmlView = new ImageView(); 
		exportIdmlView.setPreserveRatio(true);
//		exportIdmlView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.25));
		exportIdmlView.fitHeightProperty().bind(Bindings.min(30, Bindings.multiply(this.heightProperty(), 0.25)));
		exportIdmlView.setImage(exportIdmlImage);
		exportIdml.setGraphic(exportIdmlView); 
		exportIdml.setPadding(new Insets(10, 10, 10, 10));
		resetExported = new Button(); 
		Image resetExportImage = new Image("resources/export_icon.png"); 
		ImageView resetExportView = new ImageView(); 
		resetExportView.setPreserveRatio(true);
		resetExportView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.10));
		resetExportView.setImage(resetExportImage);
		resetExported.setGraphic(resetExportView); 
		resetExported.setMaxWidth(1000);
		exportHtml = new Button(); 
		exportHtml.setMaxWidth(1000);
		exportHtml.setMinWidth(50);
		Image exportHtmlImage = new Image("resources/web-color.png"); 
		ImageView exportHtmlView = new ImageView(); 
		exportHtmlView.setPreserveRatio(true);
//		exportHtmlView.fitHeightProperty().bind(Bindings.multiply(this.heightProperty(), 0.25));
		exportHtmlView.fitHeightProperty().bind(Bindings.min(30, Bindings.multiply(this.heightProperty(), 0.25)));
		exportHtmlView.setImage(exportHtmlImage);
		exportHtml.setGraphic(exportHtmlView); 
		exportHtml.setPadding(new Insets(10, 10, 10, 10));
		
		searchField = new SearchField(); 
//		searchButton = new Button(GuiContainer.ICONS.getString(FontAwesome.search));
//	 	Tooltip t = new Tooltip("Suche");
//	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
//	 	searchButton.setStyle(FontAwesome.FONT_STYLE_SMALL);
//	 	// b.setTooltip(t);
//	 	Tooltip.install(searchButton, t);
	 	searchButton = new Button("Erweitert"); 
	 	t = new Tooltip("Erweiterte Suche"); 
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	Tooltip.install(searchButton, t);
	 	searchButton.setMaxHeight(1000);
	 	searchButton.setMaxWidth(1000);
	 	
	 	searchResetButton = new Button(); 
	 	searchResetButton.setMaxWidth(1000);
	 	searchResetButton.maxHeightProperty().bind(searchButton.heightProperty());
		ImageViewResizable imageViewAdvancedSearch = new ImageViewResizable(); 
		Image crossIcon = new Image("resources/cross.png"); 
		imageViewAdvancedSearch.setImage(crossIcon);
		searchResetButton.setGraphic(imageViewAdvancedSearch);
		searchResetButton.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	 	t = new Tooltip("Erweiterte Suche zurücksetzen"); 
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	Tooltip.install(searchResetButton, t);
	 	
		sort = new Button("Sortieren"); 
		sort.setMaxWidth(1000);
	 	t = new Tooltip("Sortierung festlegen"); 
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
		Tooltip.install(sort, t);
	 	
		sortResetButton = new Button(); 
		sortResetButton.setMaxWidth(1000);
		sortResetButton.maxHeightProperty().bind(sort.heightProperty());
		ImageViewResizable imageViewSort = new ImageViewResizable(); 
		imageViewSort.setImage(crossIcon);
		sortResetButton.setGraphic(imageViewSort);
		sortResetButton.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
	 	t = new Tooltip("Sortierung zurücksetzen"); 
	 	t.setStyle("-fx-font-family: Sans-serif; -fx-font-size: 15px; -fx-text-fill: black; -fx-background-color: white; -fx-padding: 0.667em 0.75em 0.667em 0.75em;");
	 	Tooltip.install(sortResetButton, t);
		
		zoom = new Slider(); 
		zoom.setMin(0.7);
		zoom.setMax(1.3);
		zoom.setValue(1);
		zoom.setMajorTickUnit(0.1);
		zoom.setMinorTickCount(3);
		zoom.setShowTickMarks(true);
		zoom.setBlockIncrement(0.1);
//		zoom.valueProperty().addListener(new ChangeListener<Number>() {
//			@Override
//			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//				Node n = table.getTable().getTabPane().getSelectionModel().getSelectedItem().getContent(); 
//				if (n instanceof WebView) {
//					((WebView) n).setZoom(newValue.doubleValue());
//				}
//			}
//		});
		
		zoomLabel = new Label("Zoom"); 
		zoomLabel.setMaxWidth(1000);
		zoomLabel.setAlignment(Pos.CENTER);
		
		this.add(fitLogo, 0, 0, 1, 4);
		this.add(standard, 1, 0, 1, 3);
		this.add(refresh, 1, 3);
		this.add(archive, 2, 0, 1, 2);
		this.add(nichtAbgeschlossen, 3, 0, 1, 2);
		this.add(nichtKorrigiert, 4, 0, 1, 2);
		this.add(nichtExportfertig, 5, 0, 4, 2);
		this.add(exportFertigLabel, 9, 0, 4, 2);
		
		this.addColumn(2, withArchive, onlyArchive);
		this.addColumn(3, nichtAbgeschlossenAktuell, nichtAbgeschlossenAlle);
		this.addColumn(4, nichtKorrigiertAktuell, nichtKorrigiertAlle);
		this.add(nichtExportfertigAktuell, 5, 2, 4, 1);
		this.add(nichtExportfertigAlle, 5, 3, 4, 1);
//		this.addColumn(5, nichtExportfertigAktuell, nichtExportfertigAlle);
		this.add(exportFertigAktuell, 9, 2, 4, 1);
		this.add(exportFertigAlle, 9, 3, 4, 1);
//		this.addColumn(7, exportFertigAktuell, exportFertigAlle);
		this.add(edit, 13, 0, 2, 2);
		this.add(newArticle, 13, 2, 1, 2);
		this.add(contactPartner, 14, 2, 1, 2);
		this.add(export, 15, 0, 2, 2);
		this.add(exportIdml, 15, 2, 1, 2);
		this.add(resetExported, 15, 4, 2, 1);
		this.add(exportHtml, 16, 2, 1, 2);
//		this.addColumn(4, newArticle, contactPartner);
//		this.addColumn(5, export, exportHtml, exportIdml);
		
		this.add(searchField, 1, 4, 4, 1);
		this.add(searchResetButton, 5, 4, 4, 1);
		this.add(searchButton, 5, 4, 4, 1);
		this.add(sortResetButton, 9, 4, 4, 1);
		this.add(sort, 9, 4, 4, 1);
		
		this.add(zoomLabel, 17, 0, 1, 2);
		this.add(zoom, 17, 2, 1, 2);
		
		defineColumnconstraint(6);
		defineColumnconstraint(12);
		defineColumnconstraint(12);
		defineColumnconstraint(12);
		defineColumnconstraint(12);
		defineColumnconstraint(4);
		defineColumnconstraint(4);
		defineColumnconstraint(4);
		defineColumnconstraint(1);
		defineColumnconstraint(4);
		defineColumnconstraint(4);
		defineColumnconstraint(4);
		defineColumnconstraint(1);
		defineColumnconstraint(5);
		defineColumnconstraint(5);
		defineColumnconstraint(5);
		defineColumnconstraint(5);
		defineColumnconstraint(13);

		this.getStylesheets().add("/view/Layout.css"); 

	}
	
	private void defineColumnconstraint(double widthPercent) {
		ColumnConstraints col = new ColumnConstraints(); 
		col.setHgrow(Priority.ALWAYS);
		col.setFillWidth(true);
		col.setPercentWidth(widthPercent);
		this.getColumnConstraints().add(col); 
	}
	
	public void showSearchReset() {
		GridPane.setColumnSpan(searchButton, 3);
	}
	
	public void hideSearchReset() {
		GridPane.setColumnSpan(searchButton, 4);
	}
	
	public void showSortReset() {
		GridPane.setColumnSpan(sort, 3);
	}
	
	public void hideSortReset() {
		GridPane.setColumnSpan(sort, 4);
	}
	
	// TODO 
	// font size dynamically, with min size & max size (?) 

}
