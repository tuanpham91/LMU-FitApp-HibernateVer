package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import controller.Service.Filter;
import controller.export.Export;
import controller.export.FitHtmlExport;
import javafx.application.HostServices;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.Toggle;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logging.MyLogger;
import lombok.Getter;
import model.FitArticle;
import model.Rubric;
import model.Subject;
import model.edit.Code;
import model.edit.ContactPerson;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.TextEntry.TextEntryFormat;
import model.edit.WebLinkEntry;
import view.GuiContainer;
import view.edit.ArticleElementPane;
import view.edit.ArticleElements;
import view.edit.ArticleElements.DragAndDropLabel;
import view.edit.ArticleStatusGrid;
import view.edit.EditCode;
import view.edit.EditConatcPerson;
import view.edit.EditHeader;
import view.edit.EditInfoListEntry;
import view.edit.EditText;
import view.edit.EditWebLink;
import view.edit.EditorToolbox;
import view.edit.GeneralInfoGrid;
import view.overview.HtmlTableRenderer;
import view.overview.OverviewTableHeader;

/**
 * Container class for all controller stuff. 
 */
public class Controller {
	
	public static final Logger log = Logger.getLogger("FitAppLog");
	
	/**
	 * The database URL. Edit this URL if the database location or driver differs. 
	 */
	public static final String url = "jdbc:ucanaccess:///home/alex/FiT/fit_sync.accdb"; 
//	public static final String url = "jdbc:ucanaccess://K:\\99_Programmieren\\FiTApp\\Test\\fit_copy.accdb"; 
//	public static final String url = "jdbc:ucanaccess://C:\\Users\\Alexander\\SkyDrive\\Dokumente\\fit.accdb"; 

	/**
	 * Stage for the overview and the edit scene. 
	 */
	private Stage primaryStage; 
	
	/**
	 * Reference to the container object for the user interface. Used to assign listeners, i.e. functionality to gui elements. 
	 */
	
	private static GuiContainer gui; 
	public static GuiContainer getGui() {
		return gui;
	}
	private HostServices hostServices; 
	
	
	/**
	 * Event Listener that is assigned to the buttons in the overview table. Not a JavaFX Event Handler because the table is rendered 
	 * in a WebView in HTML format. Every button (edit AND copy) gets this listener, which sets the according article in {@link Edit} 
	 * and switches to the edit view, which implies setting the handlers in the edit view. If it is a copy button, necessary 
	 * adjustments are made in {@link Edit} and in the edit view. 
	 */
	private final EventListener editEventListener = new EventListener() {

		@Override
		public void handleEvent(Event evt) {
			EventTarget target = evt.getTarget();
			Element elem = (Element) target;
			int articleId = Integer.parseInt(elem.getAttribute(HtmlTableRenderer.ATTR_ARTICLE_ID));
			String buttonType = elem.getAttribute("id");
			Edit.INSTANCE.setArticle(articleId);
			FitArticle article = Edit.INSTANCE.getArticle();
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Konflikmeldung");
			alert.setHeaderText("Status");
			alert.setContentText(
					"Dieses Artikel befindet sich in Bearbeitung, Änderungen könnten zu Konflikten führen.");
			if (buttonType.equals("edit")) {
				if (Edit.INSTANCE.getArticle().getBeingUpdated() == false) {
					Edit.INSTANCE.markAsEditing(true);
					Edit.INSTANCE.getArticle().setBeingUpdated(true);
					Edit.INSTANCE.getArticle().updatedByMe = true;
				} else if (Edit.INSTANCE.getArticle().getBeingUpdated() == true) {
					alert.showAndWait();
				}
				gui.getEditWindow().setArticle(article);
				initializeEditListeners();
			} else if (!buttonType.equals("copy")) {
				return;
			}
			// Edit.INSTANCE.saveArticle();
			System.out.println("Controller- Zeile 154- Event");
			if (buttonType.equals("copy")) {
				if (article.getRecurrent()) {
					if (article.isChildArticlePresent()) {
						Alert alertCopyFromRecurrent = new Alert(AlertType.CONFIRMATION);
						alertCopyFromRecurrent.setTitle("Artikel kopieren");
						alertCopyFromRecurrent.setHeaderText(
								"Es handelt sich um einen wiederkehrenden Artikel, zu dem es bereits einen Verweis auf eine Kopie gibt. Es kann "
										+ "höchstens einen Verweis auf eine Kopie geben. Soll eine unabhängige Kopie erstellt werden?");
						alertCopyFromRecurrent.setContentText(
								"Wenn eine unabhängige Kopie erstellt wird, wird kein Verweis ziwschen den beiden Artikeln "
										+ "erzeugt, d.h. der Zusammenhang zwischen den beiden Artikeln kann nicht mehr nachvollzogen werden. Um einen konstistenten "
										+ "Zusammenhang zwischen kopierten Artikeln zu gewährleisten, kann von dem zuletzt erzeugten Artikel dieser Reihe eine Kopie erstellt werden.");
						ButtonType buttonTypeCopy = new ButtonType("Unabhängig Kopieren");
						ButtonType buttonTypeCopyFromLatest = new ButtonType("Kopie des letzten Artikel");
						
						ButtonType buttonTypeJumpToLatest = new ButtonType("Letzten Artikel anzeigen");
						ButtonType buttonTypeCancel = new ButtonType("Abbrechen", ButtonData.CANCEL_CLOSE);
						alertCopyFromRecurrent.getButtonTypes().setAll(buttonTypeCopy, buttonTypeCopyFromLatest,
								buttonTypeJumpToLatest, buttonTypeCancel);
						alertCopyFromRecurrent.getDialogPane().setPrefWidth(gui.getOverviewTable().getWidth() / 1.7);
						alertCopyFromRecurrent.getDialogPane().getChildren().stream()
								.filter(node -> node instanceof Label)
								.forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
						Optional<ButtonType> result = alertCopyFromRecurrent.showAndWait();
						if (result.get() == buttonTypeCopy) {
							// copy without reference to old article
							gui.getEditWindow().setArticle(article);
							initializeEditListeners();
							article.setId(0);
							article.setChildArticlePresent(false);
							article.setChildArticle(0);
							gui.getEditWindow().getArticleStatusGrid().setCopied();
							Edit.INSTANCE.markAsNewArticle();
						} else if (result.get() == buttonTypeCopyFromLatest || result.get() == buttonTypeJumpToLatest) {
							boolean hasChild = true;
							int childId = article.getChildArticle();
							while (hasChild) {
								Edit.INSTANCE.setArticle(childId);
								article = Edit.INSTANCE.getArticle();
								hasChild = article.isChildArticlePresent();
							}
							if (result.get() == buttonTypeCopyFromLatest) {
								// make a copy from the latest article in the
								// inheritance chain
								FitArticle articleCopy = article.copy();
								article.copyFromAnArticle(articleCopy);
								article.makeChildCopy(article.getId());
								gui.getEditWindow().setArticle(article);
								initializeEditListeners();
								// article.setParentArticle(article.getId());
								// article.setParentArticlePresent(true);
								article.setId(0);
								// gui.getEditWindow().getArticleStatusGrid().setCopied();
								Edit.INSTANCE.markAsNewArticle();
							} else {
								// just open edit view of the latest article
								gui.getEditWindow().setArticle(article);
								initializeEditListeners();
							}
						} else {
							return;
						}
					} else {
						// no child article yet
						FitArticle articleCopy = article.copy();
						
						//article.copyFromAnArticle(articleCopy);
						//article.makeChildCopy(articleId);
						articleCopy.makeChildCopy(articleId);				
						gui.getEditWindow().setArticle(Edit.INSTANCE.getArticle());
						initializeEditListeners();
						Edit.INSTANCE.getArticle().setId(0);
						// gui.getEditWindow().getArticleStatusGrid().setCopied();
						Edit.INSTANCE.markAsNewArticle();
						// article.setParentArticle(articleId);
						// article.setParentArticlePresent(true);
					}
				} else {
					System.out.println("Controller - zeile 232");
					// copy the article without reference to the old one -> set
					// id to 0 and call setCopied() in view
					article.setId(0);
					article.makeChildCopy(articleId);
					List<FitArticleElement> newElements = new ArrayList<>();
					for(FitArticleElement element: article.getElements()) {
						element.setId(0);
						element.setFitArticle(article);
						newElements.add(element);
					}
					article.setElements(newElements);
					gui.getEditWindow().setArticle(article);
					initializeEditListeners();
					gui.getEditWindow().getArticleStatusGrid().setCopied();
					Edit.INSTANCE.markAsNewArticle();
				}
			}
			gui.showEditWindow();
		}

	};

	/**
	 * Event Handler for the new article button. Sets a new article in {@link Edit} and switches to the edit view, 
	 * which implies setting the handlers in the edit view. 
	 */
	private final EventHandler<ActionEvent> editEventHandlerNewArticle = new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
			Edit.INSTANCE.setNewArticle();
			gui.getEditWindow().setArticle(Edit.INSTANCE.getArticle());
			initializeEditListeners();
			gui.showEditWindow();
		}
	};

	/**
	 * Change Listener to refresh the article preview in the edit view. This listener is added to all elements 
	 * in the view which are relevant to the preview. When fired, the listener just renders the preview, s. 
	 * {@link view.edit.EditWindow#refreshPreview(FitArticle) refreshPreview}. 
	 */
	ChangeListener<String> changeListenerRefresh = new ChangeListener<String>() {
		@Override
		public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
		}
	};

	/**
	 * Constructor for the controller container, called in the main function. Within this constructor, 
	 * the event handlers for the elements in the overview scene are defined. 
	 * @param primaryStage
	 */
	public Controller(HostServices hostServices) {
		
		try {
			MyLogger.setup(log);
			MyLogger.setup(Service.getLog());
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		gui = new GuiContainer();

		Service.INSTANCE.setController(this);
		
		this.primaryStage = new Stage(); 
		this.primaryStage.setTitle("FiT");
		this.primaryStage.setScene(gui);
		
		this.hostServices = hostServices; 
		
		this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				if (Edit.INSTANCE.getArticle() != null && Edit.INSTANCE.getArticle().updatedByMe) {
					Edit.INSTANCE.markAsEditing(false);
				}
			}
		});
		
		this.primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					if (primaryStage.isMaximized()) {
						primaryStage.setMaximized(false);	
					}
				} 
			}
		});
		
		this.primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					primaryStage.setIconified(false);
				}
			}
		});
		
		/*
		 * *******************************************************************************************************
		 * open browser when hyperlinks in preview are clicked
		 * *******************************************************************************************************
		 */

		WebEngine webEngine = gui.getEditWindow().getEditPreview().getEngine(); 
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (newValue == Worker.State.SUCCEEDED) {
					
					EventListener listener = new EventListener() {
						@Override
						public void handleEvent(Event evt) {
							String href = ((Element)evt.getTarget()).getAttribute("href"); 
							hostServices.showDocument(href);
						}
					};
					
					Document doc = webEngine.getDocument(); 
					NodeList links = doc.getElementsByTagName("a"); 
					for (int i = 0; i < links.getLength(); i++) {
						((EventTarget)links.item(i)).addEventListener("click", listener, false);
					}
				}
			}
		});

		
		Service.INSTANCE.showStartOverview(); // generates new articles if necessary due to recurrent articles that were archived lately 
		
		OverviewTableHeader header = gui.getOverviewTable().getHeader(); 
		
		header.getStandard().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (header.getFilterToggleGroup().getSelectedToggle() != null) {
					header.getFilterToggleGroup().getSelectedToggle().setSelected(false);
				}
				Service.INSTANCE.setFilterConstraints(Filter.STANDARD);
				int allArticles = Service.INSTANCE.getCount(); 
				System.out.println("Controller - 376");
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
		
			}
		});

		header.getRefresh().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		/*
		 * *******************************************************************************************************
		 * search and sort elements in header
		 * *******************************************************************************************************
		 */
		// TODO
		header.getSearchField().getTextField().setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.getCode().equals(KeyCode.ENTER)) {
					String substring = header.getSearchField().getTextField().getText().toLowerCase(); 
					if (substring != null && substring.length() != 0) {
						header.getSearchField().getChanged().set(false);
						Service.INSTANCE.setSimpleSearchConstraints(substring, true);
						int allArticles = Service.INSTANCE.getCount(); 
						gui.getOverviewTable().getTable().setup(allArticles);
						Service.INSTANCE.showOverview(1, false);
					}
				}
			}
		});
		
		header.getSearchField().getImageView().setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("Search Field");
				if (header.getSearchField().getChanged().get()) {
					String substring = header.getSearchField().getTextField().getText().toLowerCase(); 
					if (substring != null && substring.length() != 0) {
						header.getSearchField().getChanged().set(false);
						int allArticles = Service.INSTANCE.getCount(); 
						gui.getOverviewTable().getTable().setup(allArticles);
						Service.INSTANCE.showOverview(1, false);
					}
				} else {
					header.getSearchField().getTextField().setText("");
					header.getSearchField().getChanged().set(true);
					Service.INSTANCE.resetSearchConstraints();
					int allArticles = Service.INSTANCE.getCount(); 			
					gui.getOverviewTable().getTable().setup(allArticles);
					Service.INSTANCE.showOverview(1, false);
				}
			}
		});
		
		header.getSearchButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.showSearch();
				gui.getSearchWindow().setCurrentlyApplied();
			}
		});
		
		header.getSearchResetButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				header.hideSearchReset();
				System.out.println("SearchResetButton");
				if (gui.getSearchWindow().isActive()) {
					gui.getSearchWindow().reset();
					Service.INSTANCE.searchString =false;
			
					Service.INSTANCE.resetAdvanceSearch();
					int allArticles = Service.INSTANCE.getCount(); 
					gui.getOverviewTable().getTable().setup(allArticles);
					Service.INSTANCE.showOverview(1, false);					
				}
			}
		});
		
		header.getSort().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.showSort();
			}
		});

		header.getSortResetButton().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				header.hideSortReset();
				if (gui.getSortingWindow().isActive()) {
					gui.getSortingWindow().reset();
					Service.INSTANCE.setOrdering(new ArrayList<>());
					gui.closeSort();
					int allArticles = Service.INSTANCE.getCount(); 
					gui.getOverviewTable().getTable().setup(allArticles);
					Service.INSTANCE.showOverview(1, false);
				}
			}
		});


		/*
		 * *******************************************************************************************************
		 * filter elements
		 * *******************************************************************************************************
		 */

		header.getOnlyArchive().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NUR_ARCHIV);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getWithArchive().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				log.info("filter - with archive");
				Service.INSTANCE.setFilterConstraints(Filter.MIT_ARCHIV);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtAbgeschlossenAktuell().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_ABG_AKTUELL);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtKorrigiertAktuell().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_KORR_AKTUELL);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtExportfertigAktuell().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_EXP_AKTUELL);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getExportFertigAktuell().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.EXP_AKTUELL);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtAbgeschlossenAlle().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_ABG_ALLE);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtKorrigiertAlle().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_KORR_ALLE);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getNichtExportfertigAlle().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.NICHT_EXP_ALLE);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		header.getExportFertigAlle().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Service.INSTANCE.setFilterConstraints(Filter.EXP_ALLE);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		/*
		 * *******************************************************************************************************
		 * export elements
		 * *******************************************************************************************************
		 */
		
		header.getExportHtml().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				try {
					File dir = gui.getExportDir();
					if (dir == null)
						return;
					System.out.println("Export Called\n");
					DateFormat dtf = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");
					String fileName = "fit-" + dtf.format(new Date()) + ".html";
					File exportFile = new File(dir, fileName);
					List<FitArticle> issue = Export.INSTANCE.getExportArticles();
					StringBuilder stb = new StringBuilder("<!DOCTYPE html>\n");
					stb.append("<html>\n");
					stb.append("<head>\n");
					stb.append("<meta charset=\"UTF-8\">\n")
							.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>\n");
					stb.append("<style>\n").append(extractCss()).append("\n</style>");

					stb.append("<body>");
					for (FitArticle a : issue) {
						String html = FitHtmlExport.export(a, "\n<article>\n", "\n</article>");
						stb.append(html);
					}
					stb.append("</body>\n");
					stb.append("</html>");

					Files.write(exportFile.toPath(), Arrays.asList(stb.toString()), StandardCharsets.UTF_8);
					doneDialog(issue.size());
				} catch (Exception excp) {
					excp.printStackTrace();
				}
			}

		});
		//TODO show ContactPartner
		header.getContactPartner().setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				gui.showContact();
			}
			
		});
		
		
		
		
		
		header.getExportIdml().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try {
                    File dir = gui.getExportDir();
                    if (dir == null)
                        return;

                    DateFormat dtf = new SimpleDateFormat("yy-MM-dd_HH-mm-ss");
                    String fileName = "fit-" + dtf.format(new Date()) + ".xml";
                    File exportFile = new File(dir, fileName);
                    List<FitArticle> issue = Export.INSTANCE.getExportArticles();
                    String stb = ArticlesToXML.exportXML(issue);
                    Files.write(exportFile.toPath(), Arrays.asList(stb.toString()), StandardCharsets.UTF_8);
                    doneDialog(issue.size());
                } catch (Exception excp) {
                    excp.printStackTrace();
                }
            }
        });

		/*
		 * *******************************************************************************************************
		 * new article
		 * *******************************************************************************************************
		 */ 
		
		header.getNewArticle().setOnAction(editEventHandlerNewArticle);

		/*
		 * *******************************************************************************************************
		 * event handlers for sort window
		 * *******************************************************************************************************
		 */
		
		gui.getSortingWindow().getOk().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Ordering> order = new ArrayList<>();
				Ordering ordering1 = Ordering.fromString(gui.getSortingWindow().getComboBox1().getValue());
				ordering1.setAscending(gui.getSortingWindow().getAscending1().isSelected());
				Ordering ordering2 = Ordering.fromString(gui.getSortingWindow().getComboBox2().getValue());
				ordering2.setAscending(gui.getSortingWindow().getAscending2().isSelected());
				Ordering ordering3 = Ordering.fromString(gui.getSortingWindow().getComboBox3().getValue());
				ordering3.setAscending(gui.getSortingWindow().getAscending3().isSelected());
				Ordering ordering4 = Ordering.fromString(gui.getSortingWindow().getComboBox4().getValue()); 
				ordering4.setAscending(gui.getSortingWindow().getAscending4().isSelected());
				order.add(ordering1); 
				order.add(ordering2); 
				order.add(ordering3); 
				order.add(ordering4); 
				
				if (gui.getSortingWindow().isActive()) {
					header.showSortReset();
				} else {
					header.hideSortReset();
				}
				
				gui.closeSort();
				Service.INSTANCE.setOrdering(order);
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});

		gui.getSortingWindow().getCancel().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getSortingWindow().reset();
				gui.closeSort();
			}
		});

		gui.getSortingWindow().getReset().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getSortingWindow().reset();
				Service.INSTANCE.resetOrder();
//				gui.closeSort();
//				Service.INSTANCE.setOrdering(new ArrayList<>());
//				gui.getOverviewTable().getTable().setup();
//				Service.INSTANCE.showOverview();
			}
		});
		
		/*
		 * *******************************************************************************************************
		 * event handlers for advanced search window 
		 * *******************************************************************************************************
		 */
				
		gui.getSearchWindow().getOk().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				
				Service.INSTANCE.setAdvancedSearchConstraints();
				
				if (gui.getSearchWindow().isActive()) {
					header.showSearchReset();
				} else {
					header.hideSearchReset();
				}
				
				
				gui.closeSearch();
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
			}
		});
		
		gui.getSearchWindow().getCancel().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getSearchWindow().resetToCurrentlyApplied();
				gui.closeSearch();
			}
		});
		
		gui.getSearchWindow().getReset().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getSearchWindow().reset();
			}
		});
		
		gui.getSearchStage().setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				gui.getSearchWindow().resetToCurrentlyApplied();
			}
		});
		
		/*
		 * *******************************************************************************************************
		 * initialize overview with standard filter
		 * *******************************************************************************************************
		 */
		
		gui.getOverviewTable().getTable().getTabPane().getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				int page = gui.getOverviewTable().getTable().getTabPane().getSelectionModel().getSelectedIndex() + 1; 
				if (page > 0) {
					Service.INSTANCE.showOverview(page, false);	
					System.out.println("Tab changeß\n");
				}
			}
		});
		
		Service.INSTANCE.setFilterConstraints(Filter.STANDARD);
		int allArticles = Service.INSTANCE.getCount(); 
		gui.getOverviewTable().getTable().setup(allArticles);
		//Service.INSTANCE.showOverview(2, true);
		System.out.println("First call");
		//Service.INSTANCE.showOverview(1, true);
				
//		stage.close();
	}

	/**
	 * Initiates view to render a page (one TabPane) in the overview table. This method is called from 
	 * {@link Service} when a list of articles is generated from the database according to the constraints
	 * from filter/search. 
	 * @param results List of articles to render in the overview. 
	 * @param page Page number. 
	 */
	public void renderPage(List<FitArticle> results, int page, boolean firstCall) {
		log.info("in renderPage");
		EventListener linkEventListener = new EventListener() {
			@Override
			public void handleEvent(Event evt) {
				String href = ((Element)evt.getTarget()).getAttribute("href"); 
				hostServices.showDocument(href);
			}
		};
		gui.getOverviewTable().getTable().renderNextPage(page, results, editEventListener, linkEventListener, firstCall);
	}

	/**
	 * Initializes the event handlers in the edit view. This method has to be called every time a new article 
	 * is set in {@link Edit} in order to bind view elements to the model components of the right article. 
	 */
	private void initializeEditListeners() {
		
	
		/*
		 * *******************************************************************************************************
		 * toolbar of edit window
		 * *******************************************************************************************************
		 */
		
		EditorToolbox toolbox = gui.getEditWindow().getToolbox(); 

		toolbox.getBack().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				log.info("getBack pressed");
				boolean goBack = true;
				if (Edit.INSTANCE.isChanged()) {
					log.info("sth changed");
					goBack = gui.discardChanges();
				}
				if (goBack) {
					log.info("going back");
					if (Edit.INSTANCE.getArticle().updatedByMe) {
						log.info("edited by me");
						Edit.INSTANCE.markAsEditing(false);						
					}
					log.info("showing table");
					gui.showTable();
				}
			}
		});
		;

		toolbox.getSave().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
			
				if (gui.getEditWindow().getArticleStatusGrid().getApplicationDeadline().getValue() == null) {
					gui.missingApplicationDeadline();
					return;
				}
				if (Edit.INSTANCE.getArticle().getRecurrent() && gui.getEditWindow().getArticleStatusGrid().getNextApplicationDeadline().getValue() == null) {
					gui.missingRecurrentFrom();
					return; 
				}
				// TODO check if recurrent and no recurrent date -> error
				if (Edit.INSTANCE.getArticle().getBeingUpdated() && !Edit.INSTANCE.getArticle().updatedByMe){
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Warnung");
					alert.setHeaderText("Status");
					alert.setContentText("Dieses Artikel befindet sich in Bearbeitung, Speichern kann zu Konflikten führen! Soll der Artikel trotzdem gespeichert werden?");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() != ButtonType.OK) {
						return; 
					}
				}
				toolbox.getSave().setDisable(true);
				
				
				if (Edit.INSTANCE.getArticle().updatedByMe) {
					Edit.INSTANCE.getArticle().setBeingUpdated(false);
					Edit.INSTANCE.getArticle().setUpdatedByMe(false);					
				}
				Edit.INSTANCE.markAsEditing(false);
				Edit.INSTANCE.saveArticle();
				int allArticles = Service.INSTANCE.getCount(); 
				gui.getOverviewTable().getTable().setup(allArticles);
				Service.INSTANCE.showOverview(1, false);
				gui.showTable();
					
				toolbox.getSave().setDisable(false);
			}
		});

		toolbox.getDelete().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				boolean d = gui.deleteConfirmation();
				if (d) {
					toolbox.getDelete().setDisable(true);
					Edit.INSTANCE.deleteArticle();
//					Service.INSTANCE.setFilterConstraints(Filter.STANDARD);
					int allArticles = Service.INSTANCE.getCount(); 
					gui.getOverviewTable().getTable().setup(allArticles);
					Service.INSTANCE.showOverview(1, false);
					gui.showTable();
					toolbox.getDelete().setDisable(false);
				}
			}
		});

		toolbox.getAddHeadline().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				TextEntry newHeader = new TextEntry( newOrderNr, TextEntryFormat.HEADING_3, "");
				EditHeader newHeaderView = new EditHeader("", TextEntryFormat.HEADING_3, elementNodes.getElementsWidthBinding());
				setHeaderListener(newHeader, newHeaderView);
				setElementButtonsListener(newHeaderView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newHeader);
				elementNodes.getChildren().add(newHeaderView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(newHeaderView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}

		});

		toolbox.getAddCode().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				Code newCode = new Code( newOrderNr, "");
				EditCode codeView = new EditCode(elementNodes.getElementsWidthBinding());
				newCode.code().bind(codeView.getEditText().textProperty());
				newCode.code().addListener(changeListenerRefresh);
				setElementButtonsListener(codeView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newCode);
				elementNodes.getChildren().add(codeView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(codeView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}
		});

		toolbox.getAddEditBBTextArea().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				TextEntry newText = new TextEntry( newOrderNr, TextEntryFormat.TEXT_BB, "");
				EditText newTextView = new EditText("", elementNodes.getElementsWidthBinding());
				newText.content().bind(newTextView.getTextArea().textProperty());
				newText.content().addListener(changeListenerRefresh);
				setElementButtonsListener(newTextView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newText);
				elementNodes.getChildren().add(newTextView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(newTextView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}
		});

		toolbox.getAddInfoListElement().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				InfoListEntry newInfo = new InfoListEntry( newOrderNr, "", "");
				EditInfoListEntry newInfoView = new EditInfoListEntry("", "", elementNodes.getElementsWidthBinding());
				newInfo.name().bind(newInfoView.getNameText().textProperty());
				newInfo.name().addListener(changeListenerRefresh);
				newInfo.value().bind(newInfoView.getValueText().textProperty());
				newInfo.value().addListener(changeListenerRefresh);
				setElementButtonsListener(newInfoView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newInfo);
				elementNodes.getChildren().add(newInfoView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(newInfoView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}
		});
		
		toolbox.getAddWebLink().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				WebLinkEntry newLink = new WebLinkEntry( newOrderNr, "", "");
				EditWebLink newLinkView = new EditWebLink("", "http://", elementNodes.getElementsWidthBinding());
				newLink.info().bind(newLinkView.getInfoText().textProperty());
				newLink.info().addListener(changeListenerRefresh);
				newLink.url().bind(newLinkView.getUrlText().textProperty());
				newLink.url().addListener(changeListenerRefresh);
				setElementButtonsListener(newLinkView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newLink);
				elementNodes.getChildren().add(newLinkView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(newLinkView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}
		});

		toolbox.getAddContactPartner().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<FitArticleElement> elementsList = Edit.INSTANCE.getArticle().getElements();
				int newOrderNr;
				if (elementsList.size() == 0) {
					newOrderNr = 1;
				} else {
					newOrderNr = elementsList.get(elementsList.size() - 1).getOrderNr() + 1;
				}
				ArticleElements elementNodes = gui.getEditWindow().getArticleElements(); 
				ContactPerson newContact = new ContactPerson( newOrderNr, "", "", "", "", "");
				EditConatcPerson newContactView = new EditConatcPerson(elementNodes.getElementsWidthBinding());
				// TODO
				setElementButtonsListener(newContactView, newOrderNr);
				Edit.INSTANCE.getArticle().addElement(newContact);
				elementNodes.getChildren().add(newContactView);
				DragAndDropLabel dragLabel = elementNodes.addDragLabel();
				setDragListener(newContactView, dragLabel); 
				gui.getEditWindow().expandElements();
				gui.getEditWindow().jumpToBottom();
			}
		});

		toolbox.getExpand().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getEditWindow().getEditAccordion().setExpandedPane(gui.getEditWindow().getSectionArticleElements());
				Iterator<Node> iteratorElementPanes = gui.getEditWindow().getArticleElements().getChildren().iterator();
				while (iteratorElementPanes.hasNext()) {
					Node nextNode = iteratorElementPanes.next();
					if (nextNode instanceof ArticleElementPane) {
						((ArticleElementPane) nextNode).setExpanded(true);
					}
				}
			}
		});

		toolbox.getCollapse().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				gui.getEditWindow().getEditAccordion().setExpandedPane(gui.getEditWindow().getSectionArticleElements());
				Iterator<Node> iteratorElementPanes = gui.getEditWindow().getArticleElements().getChildren().iterator();
				while (iteratorElementPanes.hasNext()) {
					Node nextNode = iteratorElementPanes.next();
					if (nextNode instanceof ArticleElementPane) {
						((ArticleElementPane) nextNode).setExpanded(false);
					}
				}
			}
		});

		/*
		 * *******************************************************************************************************
		 * general information pane
		 * *******************************************************************************************************
		 */
		
		GeneralInfoGrid genInfoGrid = gui.getEditWindow().getGeneralInfoGrid(); 

		genInfoGrid.getToggleGroupMainSubjectArea().selectedToggleProperty()
				.addListener(new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
							Toggle newValue) {
						Edit.INSTANCE.getArticle().setMainSubject((Subject) newValue.getUserData());
					}
				});
		genInfoGrid.getNfachTP().selectedProperty()
				.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							Edit.INSTANCE.getArticle().getMinorSubjects().add(Subject.THEMENOFFENES_PROGRAMM);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()+1);
						} else {
							Edit.INSTANCE.getArticle().getMinorSubjects().remove(Subject.THEMENOFFENES_PROGRAMM);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()-1);
						}
					}
				});
		genInfoGrid.getNfachSSH().selectedProperty()
				.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							Edit.INSTANCE.getArticle().getMinorSubjects().add(Subject.SOCIAL_SCIENCES_AND_HUMANITIES);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()+8);
						} else {
							Edit.INSTANCE.getArticle().getMinorSubjects()
									.remove(Subject.SOCIAL_SCIENCES_AND_HUMANITIES);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()-8);
						}
					}
				});
		genInfoGrid.getNfachPSE().selectedProperty()
				.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							Edit.INSTANCE.getArticle().getMinorSubjects()
									.add(Subject.PHYSICAL_SCIENCES_AND_ENGINEERING);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()+4);
						} else {
							Edit.INSTANCE.getArticle().getMinorSubjects()
									.remove(Subject.PHYSICAL_SCIENCES_AND_ENGINEERING);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()-4);
						}
					}
				});
		genInfoGrid.getNfachLS().selectedProperty()
				.addListener(new ChangeListener<Boolean>() {
					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						if (newValue) {
							Edit.INSTANCE.getArticle().getMinorSubjects().add(Subject.LIFE_SCIENCES);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()+2);
						} else {
							Edit.INSTANCE.getArticle().getMinorSubjects().remove(Subject.LIFE_SCIENCES);
							Edit.INSTANCE.getArticle().setMinorSubjectCode(Edit.INSTANCE.getArticle().getMinorSubjectCode()-2);
						}
					}
				});

		Edit.INSTANCE.getArticle().getTargetAudience().TargetAudienceExperienced()
				.bind(genInfoGrid.getExperiencedResearchers().selectedProperty());
		Edit.INSTANCE.getArticle().getTargetAudience().TargetAudienceYoung()
				.bind(genInfoGrid.getYoungResearchers().selectedProperty());

		Edit.INSTANCE.getArticle().getSupportType().supportIndividualResearch
				.bind(genInfoGrid.getSupportIndividualResearch().selectedProperty());
		Edit.INSTANCE.getArticle().getSupportType().supportPrice
				.bind(genInfoGrid.getSupportPrice().selectedProperty());
		Edit.INSTANCE.getArticle().getSupportType().supportResearchGroups
				.bind(genInfoGrid.getSupportResearchGroups().selectedProperty());
		Edit.INSTANCE.getArticle().getSupportType().supportScholarship
				.bind(genInfoGrid.getSupportScholarship().selectedProperty());
		Edit.INSTANCE.getArticle().getSupportType().supportTransferSpinOff
				.bind(genInfoGrid.getSupportTransferSpinOff().selectedProperty());

		genInfoGrid.getToggleGroupRubric().selectedToggleProperty()
				.addListener(new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
							Toggle newValue) {
						Edit.INSTANCE.getArticle().setRubric((Rubric) newValue.getUserData());
					}
				});

		genInfoGrid.getToggleGroupPrio().selectedToggleProperty()
				.addListener(new ChangeListener<Toggle>() {
					@Override
					public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue,
							Toggle newValue) {
						Edit.INSTANCE.getArticle().setPriority((Integer) newValue.getUserData());
					}
				});

		/*
		 * *******************************************************************************************************
		 * article status pane
		 * *******************************************************************************************************
		 */
		
		ArticleStatusGrid statusGrid = gui.getEditWindow().getArticleStatusGrid(); 
		
		Edit.INSTANCE.getArticle().authorCorrected()
				.bind(statusGrid.getCorrected().selectedProperty());
		Edit.INSTANCE.getArticle().completed()
				.bind(statusGrid.getCompleted().selectedProperty());
		Edit.INSTANCE.getArticle().en()
				.bind(statusGrid.getEnglVersion().selectedProperty());
		Edit.INSTANCE.getArticle().exportFit()
				.bind(statusGrid.getExportFit().selectedProperty());
		Edit.INSTANCE.getArticle().exportReady()
				.bind(statusGrid.getExportReady().selectedProperty());
		Edit.INSTANCE.getArticle().exportWeb()
				.bind(statusGrid.getExportWeb().selectedProperty());

		Edit.INSTANCE.getArticle().recurrent()
				.bind(statusGrid.getRecurrent().selectedProperty());
		Edit.INSTANCE.getArticle().applicationAnytime()
				.bind(statusGrid.getRunning().selectedProperty());

		statusGrid.getApplicationDeadline().valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (newValue == null) {
					Edit.INSTANCE.getArticle().setApplicationDeadline(null);
				} else {
					Date date = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant());
					Edit.INSTANCE.getArticle().setApplicationDeadline(date);
				}				
			}
		});

		
		statusGrid.getRecurrent().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					statusGrid.getNextApplicationDeadline().setDisable(false);
					
					LocalDate applicationDeadline = statusGrid.getApplicationDeadline().getValue(); 
					statusGrid.getNextApplicationDeadline().setDisable(false); 
					LocalDate recurrentDate; 
					
					if (applicationDeadline != null) {
						if (statusGrid.getRunning().isSelected()) {
							// also running selected 
							recurrentDate = applicationDeadline.plusMonths(4); 
							if (statusGrid.getRecurrentCheckFrom().getValue() == null) {
								LocalDate processDeadlineDate = recurrentDate.minusMonths(4); 
								statusGrid.getRecurrentCheckFrom().setValue(processDeadlineDate);
							}
						} else {
							// only recurrent but not running selected 
							recurrentDate = applicationDeadline.plusYears(1); 
							if (statusGrid.getRecurrentCheckFrom().getValue() == null) {
								LocalDate processDeadlineDate = recurrentDate.minusMonths(4); 
								statusGrid.getRecurrentCheckFrom().setValue(processDeadlineDate);
							}
						}
						statusGrid.getNextApplicationDeadline().setValue(recurrentDate);
					}
					
				} else {
					statusGrid.getRunning().setSelected(false);
					statusGrid.getNextApplicationDeadline().setValue(null);
					statusGrid.getNextApplicationDeadline().setDisable(true);	
				}
			}
		});
		
		statusGrid.getRunning().selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				CheckBox recurrenctCheck = statusGrid.getRecurrent(); 
				boolean recurrentSelected = recurrenctCheck.isSelected();  
				LocalDate applicationDeadline = statusGrid.getApplicationDeadline().getValue(); 
				if (newValue) {
					if (recurrentSelected) {
						statusGrid.getNextApplicationDeadline().setValue(applicationDeadline.plusMonths(4));							
					}
					recurrenctCheck.setSelected(true);
				} else {
					statusGrid.getNextApplicationDeadline().setValue(applicationDeadline.plusYears(1));
				}
			}
		});
		
		statusGrid.getNextApplicationDeadline().valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (newValue == null) {
					Edit.INSTANCE.getArticle().setNextApplicationDeadline(null);
				} else {
					Date date = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
					Edit.INSTANCE.getArticle().setNextApplicationDeadline(date);
					if (statusGrid.getRecurrentCheckFrom().getValue() == null) {
						statusGrid.getRecurrentCheckFrom().setValue(newValue.minusMonths(4));
					}
				}
			}
		});
		
		statusGrid.getRecurrentCheckFrom().valueProperty().addListener(new ChangeListener<LocalDate>() {
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue,
					LocalDate newValue) {
				if (newValue == null) {
					Edit.INSTANCE.getArticle().setRecurrentCheckFrom(null);	
				} else {
					Date date = Date.from(newValue.atStartOfDay(ZoneId.systemDefault()).toInstant()); 
					Edit.INSTANCE.getArticle().setRecurrentCheckFrom(date);
				}
			}
		});		

		/*
		 * *******************************************************************************************************
		 * article elements
		 * *******************************************************************************************************
		 */

		ObservableList<Node> articleElementsPanes = gui.getEditWindow().getArticleElements().getChildren();
		setElementsListener(articleElementsPanes);

		// trigger refreshPreview when list of elements in view has changed
		articleElementsPanes.addListener(new ListChangeListener<Node>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Node> c) {
				gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
			}

		});
		
		/*
		 * *******************************************************************************************************
		 * comment pane
		 * *******************************************************************************************************
		 */
		
		Edit.INSTANCE.getArticle().comment().bind(gui.getEditWindow().getEditComment().textProperty());
		
	}

	/**
	 * Sets the event handlers for the control elements of a {@link ArticleElementPane} in the edit view. 
	 * Every {@link ArticleElementPane} has three buttons - for moving this element up/down in the order of 
	 * article elements and for deleting this element. 
	 * @param currentNode The pane associated with the element. 
	 * @param currentIndex The position in the order of the article's elements. 
	 */
	private void setElementButtonsListener(ArticleElementPane currentNode, int currentIndex) {

		ObservableList<Node> articleElementsPanes = gui.getEditWindow().getArticleElements().getChildren();

		((ArticleElementPane) currentNode).getAllUp().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int currentIndex = articleElementsPanes.indexOf(currentNode) / 2;
				Edit.INSTANCE.getArticle().moveAllUp(currentIndex);
				gui.getEditWindow().getArticleElements().setArticle(Edit.INSTANCE.getArticle());
				setElementsListener(gui.getEditWindow().getArticleElements().getChildren());
			}
		});
		((ArticleElementPane) currentNode).getAllDown().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int currentIndex = articleElementsPanes.indexOf(currentNode) / 2;
				Edit.INSTANCE.getArticle().moveAllDown(currentIndex);
				gui.getEditWindow().getArticleElements().setArticle(Edit.INSTANCE.getArticle());
				setElementsListener(gui.getEditWindow().getArticleElements().getChildren());
			}
		});
		((ArticleElementPane) currentNode).getUp().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int currentIndexPane = articleElementsPanes.indexOf(currentNode); 
				int currentIndex = currentIndexPane / 2;
				if (currentIndex != 0) {
					Edit.INSTANCE.getArticle().switchElements(currentIndex, currentIndex - 1);
					gui.getEditWindow().getArticleElements().swapChildren(currentIndexPane, currentIndexPane - 2);
				}
			}
		});
		((ArticleElementPane) currentNode).getDown().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int currentIndexPane = articleElementsPanes.indexOf(currentNode); 
				int currentIndex = currentIndexPane / 2;
				if (currentIndex != articleElementsPanes.size() - 1) {
					Edit.INSTANCE.getArticle().switchElements(currentIndex, currentIndex + 1);
					gui.getEditWindow().getArticleElements().swapChildren(currentIndexPane, currentIndexPane + 2);
				}
			}
		});
		((ArticleElementPane) currentNode).getDelete().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int currentIndexPane = articleElementsPanes.indexOf(currentNode);
				deleteElementDiolog(currentIndexPane);
			}
		});

	}

	/**
	 * Sets the event handlers/change listeners for {@link EditHeader}s and binds the input in the view to the model. 
	 * @param textEntry The element (in this case a {@link TextEntry}) in the article. Should be the {@link TextEntry} associated with the editHeader. 
	 * @param editHeader The pane in the view representing a specific article element (here: a header). 
	 */
	private void setHeaderListener(TextEntry textEntry, EditHeader editHeader) {
		textEntry.content().bind(editHeader.getHeader().textProperty());
		// fire refreshPreview when input text is modified
		editHeader.getHeader().textProperty().addListener(changeListenerRefresh);
		editHeader.getH3().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textEntry.setFormat(TextEntryFormat.HEADING_3);
				gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
			}
		});
		editHeader.getH4().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textEntry.setFormat(TextEntryFormat.HEADING_4);
				gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
			}
		});
		editHeader.getH5().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textEntry.setFormat(TextEntryFormat.HEADING_5);
				gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
			}
		});
		editHeader.getH6().setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				textEntry.setFormat(TextEntryFormat.HEADING_6);
				gui.getEditWindow().refreshPreview(Edit.INSTANCE.getArticle());
			}
		});
	}

	private static String extractCss() throws IOException {
		InputStream input = FitHtmlExport.class.getResourceAsStream("FitHtmlExportStyle.css");
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(input))) {
			String css = buffer.lines().collect(Collectors.joining("\n"));
			return css.substring(81);
		}
	}

	private void setElementsListener(ObservableList<Node> articleElementsPanes) {
		
		for (int i = 0; i < articleElementsPanes.size(); i++) {
			Node currentNode = articleElementsPanes.get(i);
			
			if (currentNode instanceof DragAndDropLabel) {
				((DragAndDropLabel) currentNode).setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
					@Override
					public void handle(MouseDragEvent event) {
//						((ArticleElementPane) currentNode).getLabel().setTextFill(Color.CRIMSON);
//						((DragAndDropLabel) currentNode).getLabel().setStyle("-fx-background-color: lightgray;");
//						((ArticleElementPane) currentNode).get.setStyle("-fx-background-color: slateblue;"); 
//						System.out.println("yes");
						((DragAndDropLabel) currentNode).setPrefHeight(30);
					}
				});
				((DragAndDropLabel) currentNode).setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
					@Override
					public void handle(MouseDragEvent event) {
//						((ArticleElementPane) currentNode).getLabel().setTextFill(Color.BLACK);
//						((DragAndDropLabel) currentNode).getLabel().setStyle("");
						((DragAndDropLabel) currentNode).setPrefHeight(5);
					}
				});
				currentNode.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
					@Override
					public void handle(MouseDragEvent event) {
						int sourceIndex = articleElementsPanes.indexOf(event.getGestureSource()) / 2;
						int targetIndex = articleElementsPanes.indexOf(currentNode) / 2; 
						Edit.INSTANCE.getArticle().moveElement(sourceIndex, targetIndex);
						gui.getEditWindow().getArticleElements().setArticle(Edit.INSTANCE.getArticle());
						setElementsListener(gui.getEditWindow().getArticleElements().getChildren());
						event.consume();
					}
				});
				
			}
			
			if (currentNode instanceof ArticleElementPane) {
				
				int currentIndex = articleElementsPanes.indexOf(currentNode) / 2;

				currentNode.setOnDragDetected(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						currentNode.startFullDrag();
					}
				});

				setElementButtonsListener((ArticleElementPane) currentNode, currentIndex);

				// type specific listeners
				if (currentNode instanceof EditCode) {

					// bind text property in model to the text property in the
					// view...
					FitArticleElement currentElement = Edit.INSTANCE.getArticle().getElements().get(i / 2);
					if (currentElement instanceof Code) {
						((Code) currentElement).code().bind(((EditCode) currentNode).getEditText().textProperty());
					}
					// fire refreshPreview when input text is modified
					((EditCode) currentNode).getEditText().textProperty().addListener(changeListenerRefresh);

				} else if (currentNode instanceof EditHeader) {

					TextEntry currentElement = (TextEntry) Edit.INSTANCE.getArticle().getElements().get(i / 2);
					setHeaderListener(currentElement, (EditHeader) currentNode);

				} else if (currentNode instanceof EditInfoListEntry) {

					// bind text property in model to the text property in the
					// view...
					FitArticleElement currentElement = Edit.INSTANCE.getArticle().getElements().get(i / 2);
					if (currentElement instanceof InfoListEntry) {
						((InfoListEntry) currentElement).name()
								.bind(((EditInfoListEntry) currentNode).getNameText().textProperty());
						((InfoListEntry) currentElement).value()
								.bind(((EditInfoListEntry) currentNode).getValueText().textProperty());
					}
					// fire refreshPreview when input text is modified
					((EditInfoListEntry) currentNode).getNameText().textProperty().addListener(changeListenerRefresh);
					((EditInfoListEntry) currentNode).getValueText().textProperty().addListener(changeListenerRefresh);

				} else if (currentNode instanceof EditText) {

					// bind text property in model to the text property in the
					// view...
					FitArticleElement currentElement = Edit.INSTANCE.getArticle().getElements().get(i / 2);
					if (currentElement instanceof TextEntry) {
						((TextEntry) currentElement).content()
								.bind(((EditText) currentNode).getTextArea().textProperty());
						;
					}
					// fire refreshPreview when input text is modified
					((EditText) currentNode).getTextArea().textProperty().addListener(changeListenerRefresh);

				} else if (currentNode instanceof EditWebLink) {

					// bind text property in model to the text property in the
					// view...
					FitArticleElement currentElement = Edit.INSTANCE.getArticle().getElements().get(i / 2);
					if (currentElement instanceof WebLinkEntry) {
						((WebLinkEntry) currentElement).info()
								.bind(((EditWebLink) currentNode).getInfoText().textProperty());
						((WebLinkEntry) currentElement).url()
								.bind(((EditWebLink) currentNode).getUrlText().textProperty());
					}
					// fire refreshPreview when input text is modified
					((EditWebLink) currentNode).getInfoText().textProperty().addListener(changeListenerRefresh);
					((EditWebLink) currentNode).getUrlText().textProperty().addListener(changeListenerRefresh);

				}

			}
		}
		
	}
	
	private void setDragListener(ArticleElementPane elementPane, DragAndDropLabel dragLabel) {

		ObservableList<Node> articleElementsPanes = gui.getEditWindow().getArticleElements().getChildren(); 
		
		dragLabel.setOnMouseDragEntered(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				dragLabel.setPrefHeight(30);
			}
		});
		dragLabel.setOnMouseDragExited(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				dragLabel.setPrefHeight(5);
			}
		});
		dragLabel.setOnMouseDragReleased(new EventHandler<MouseDragEvent>() {
			@Override
			public void handle(MouseDragEvent event) {
				int sourceIndex = articleElementsPanes.indexOf(event.getGestureSource()) / 2;
				int targetIndex = articleElementsPanes.indexOf(dragLabel) / 2; 
				Edit.INSTANCE.getArticle().moveElement(sourceIndex, targetIndex);
				gui.getEditWindow().getArticleElements().setArticle(Edit.INSTANCE.getArticle());
				setElementsListener(gui.getEditWindow().getArticleElements().getChildren());
				event.consume();
			}
		});
		
		elementPane.setOnDragDetected(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				elementPane.startFullDrag();
			}
		});

	}

	/**
	 * Shows info dialogue after export of articles. 
	 * @param numExportedArticles Number of exported articles. 
	 */
	private void doneDialog(int numExportedArticles) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Export fertig");
		alert.setHeaderText("Status");
		if (numExportedArticles == 1) {
			alert.setContentText("Es wurde ein Artikel exportiert");
		} else {
			alert.setContentText("Es wurden " + numExportedArticles + " Artikel exportiert");			
		}
		alert.showAndWait();
	}

	/**
	 * Shows dialogue to confirm the deletion of an article element. 
	 * @param indexOfElement Index in the list of elements of this article. 
	 */
	private void deleteElementDiolog(int indexOfElementPane) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Element löschen");
		alert.setHeaderText("Element löschen");
		alert.setContentText("Soll dieses Element wirklich gelöscht werden?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Edit.INSTANCE.getArticle().removeElement(indexOfElementPane / 2);
			gui.getEditWindow().getArticleElements().getChildren().remove(indexOfElementPane);
			gui.getEditWindow().getArticleElements().getChildren().remove(indexOfElementPane - 1);	// delete dragLabel
		}
	}

	/**
	 * Makes the {@link #primaryStage} visible. Called from main. 
	 */
	public void init(Stage initStage) {
		
		initStage.close();

		this.primaryStage.setMaximized(true);
		this.primaryStage.show();
		
		WebView webView = (WebView) gui.getOverviewTable().getTable().getTabPane().getTabs().get(0).getContent(); 
//		System.out.println(webView.getWidth());
//		System.out.println(primaryStage.getWidth());
		double screenWidth = Screen.getPrimary().getVisualBounds().getMaxX(); 
		
		while ((webView.getWidth()*webView.getZoom()) > screenWidth) {
			webView.setZoom(webView.getZoom() - 0.05);
		}
		
	}

}
