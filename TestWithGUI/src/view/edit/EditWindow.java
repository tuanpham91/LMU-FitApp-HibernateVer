package view.edit;

import controller.Edit;
import controller.export.FitHtmlExport;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.Accordion;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Getter;
import model.FitArticle;
import view.GuiContainer;

public class EditWindow extends VBox {
	
	@Getter
	private GuiContainer container; 
	
	@Getter
	private EditorToolbox toolbox; 
	private HBox articleEditBox; 
	private ScrollPane editAccordionScroll, editPreviewScroll; 
	@Getter
	private WebView editPreview; 
	@Getter
	private Accordion editAccordion; 
	
	@Getter
	private TitledPane sectionGeneralInfo, sectionArticleStatus, sectionArticleElements, sectionSources, sectionComment; 
	
	@Getter
	private GeneralInfoGrid generalInfoGrid; 	
	@Getter
	private ArticleStatusGrid articleStatusGrid; 
	@Getter 
	private Sources sources; 
	@Getter
	private ArticleElements articleElements; 
	@Getter 
	private TextArea editComment; 
	/**
	 * This class contains all needed subclass for the Edit Window, which are : ArticleStatusGrid,  ToolBox..
	 * @param container
	 */
	public EditWindow(GuiContainer container) {
		
		super(); 
		
		NumberBinding widthBinding = Bindings.multiply(this.widthProperty(), 0.49); 

		this.container = container; 
	
		this.toolbox = new EditorToolbox(this); 
		
		articleEditBox = new HBox(); 

		editPreview = new WebView(); 
		editPreview.prefWidthProperty().bind(widthBinding);
		editPreviewScroll = new ScrollPane(); 
		editPreviewScroll.setContent(editPreview);
		
		editAccordion = new Accordion(); 
		editAccordion.prefWidthProperty().bind(widthBinding);
		editAccordionScroll = new ScrollPane(); 
		editAccordionScroll.prefWidthProperty().bind(widthBinding);
		editAccordionScroll.setContent(editAccordion);
		editAccordionScroll.getContent().setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				double deltaY = event.getDeltaY()*3; 
				double width = editAccordionScroll.getContent().getBoundsInLocal().getWidth(); 
				double vvalue = editAccordionScroll.getVvalue(); 
				editAccordionScroll.setVvalue(vvalue + -deltaY/width);
			}
		});
		
		NumberBinding elementsWidthBinding = Bindings.add(Bindings.multiply(this.widthProperty(), 0.49), -25); 
		
		sectionGeneralInfo = new TitledPane(); 
		sectionGeneralInfo.setText("Allgemein");
		sectionGeneralInfo.prefWidthProperty().bind(widthBinding);
		sectionGeneralInfo.setMaxWidth(10000);
		sectionGeneralInfo.prefWidthProperty().bind(widthBinding);
		generalInfoGrid = new GeneralInfoGrid(elementsWidthBinding); 
		sectionGeneralInfo.setContent(generalInfoGrid);
		
		sectionArticleStatus = new TitledPane(); 
		sectionArticleStatus.setText("Status");
		sectionArticleStatus.prefWidthProperty().bind(widthBinding);
		sectionArticleStatus.setMaxWidth(10000);
		articleStatusGrid = new ArticleStatusGrid(elementsWidthBinding); 
		sectionArticleStatus.setContent(articleStatusGrid);
		
		sectionArticleElements = new TitledPane(); 
		sectionArticleElements.setText("Inhalte");
		sectionArticleElements.prefWidthProperty().bind(widthBinding);
		sectionArticleElements.setMaxWidth(10000);
		articleElements = new ArticleElements(elementsWidthBinding); 
		sectionArticleElements.setContent(articleElements);
		
		sectionSources = new TitledPane(); 
		sectionSources.setText("Quellen");
		sectionSources.prefWidthProperty().bind(widthBinding);
		sectionSources.setMaxWidth(10000);
		sources = new Sources(elementsWidthBinding); 
		sectionSources.setContent(sources);
		
		sectionComment = new TitledPane(); 
		sectionComment.setText("Kommentar");
		sectionComment.prefWidthProperty().bind(widthBinding);
		sectionComment.setMaxWidth(10000);
		editComment = new TextArea(); 
		editComment.prefWidthProperty().bind(elementsWidthBinding);
		editComment.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (newValue == null || newValue.length() == 0) {
					sectionComment.setTextFill(Color.BLACK);
					sectionComment.setText("Kommentar");
				} else if (newValue.length() > 0) {
					sectionComment.setTextFill(Color.RED);
					sectionComment.setText("! Kommentar");
				}
			}
		});
		editComment.setWrapText(true);
//		editComment
		sectionComment.setContent(editComment);
		
		// TODO 
//		editAccordion.getPanes().addAll(sectionGeneralInfo, sectionArticleStatus, sectionArticleElements, sectionSources, sectionComment); 
		editAccordion.getPanes().addAll(sectionGeneralInfo, sectionArticleStatus, sectionArticleElements, sectionComment); 
		
		articleEditBox.getChildren().addAll(editAccordionScroll, editPreview); 
		HBox.setHgrow(editPreviewScroll, Priority.ALWAYS);
		HBox.setHgrow(editAccordionScroll, Priority.ALWAYS);
		
		VBox.setVgrow(articleEditBox, Priority.ALWAYS);
		this.getChildren().addAll(toolbox, articleEditBox); 
		
	}
	
	/**
	 * Renders the article preview in the edit view. 
	 * @param article Article to render. 
	 */
	public void refreshPreview(FitArticle article) {
		String preview = FitHtmlExport.export(article); 
		WebEngine engine = editPreview.getEngine();
		String previewStyle = FitHtmlExport.class.getResource("FitHtmlExportStyle.css").toString();
		engine.setUserStyleSheetLocation(previewStyle);
		engine.loadContent(preview);
	}
	
	/**
	 * setup, load a article and shows it accordingly in EditWoindow
	 * @param article
	 */
	public void setArticle(FitArticle article) {
		NumberBinding elementsWidthBinding = Bindings.add(Bindings.multiply(this.widthProperty(), 0.49), -25); 
		generalInfoGrid = new GeneralInfoGrid(elementsWidthBinding); 
		articleStatusGrid = new ArticleStatusGrid(elementsWidthBinding); 
		articleElements = new ArticleElements(elementsWidthBinding); 
		generalInfoGrid.setArticle(Edit.INSTANCE.getArticle());
		articleStatusGrid.setArticle(Edit.INSTANCE.getArticle());
		articleElements.setArticle(Edit.INSTANCE.getArticle()); 
		editComment.setText(Edit.INSTANCE.getArticle().getComment());	
		sectionGeneralInfo.setContent(generalInfoGrid);
		sectionArticleStatus.setContent(articleStatusGrid);
		sectionArticleElements.setContent(articleElements);
		sectionComment.setContent(editComment);
		refreshPreview(Edit.INSTANCE.getArticle());
	}
	
	public void expandElements() {
		editAccordion.setExpandedPane(sectionArticleElements);
	}
	
	public void jumpToBottom() {
		editAccordionScroll.setVvalue(editAccordionScroll.getVmax());
	}

}
