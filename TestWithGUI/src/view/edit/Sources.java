package view.edit;

import java.io.File;
import java.io.IOException;

import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import model.FitArticle;

public class Sources extends GridPane {
	
	private Button addSource; 
	private String dir = ""; 
	private ListView<String> fileView = new ListView<String>(); 
//	private String dir = ""; 
	
	public Sources(NumberBinding elementsWidthBinding) {
		
		super(); 
		
		this.prefWidthProperty().bind(elementsWidthBinding);
		
		getStylesheets().add("/view/ArticleEditPage.css"); 
		getStyleClass().add("article-fixed-fields"); 
		
		fileView.setMaxWidth(10000);
		fileView.prefWidthProperty().bind(elementsWidthBinding);
		
		this.setPadding(new Insets(10, 10, 10, 10));
		
		addSource = new Button("add source"); 
		this.add(addSource, 0, 0);
		this.add(fileView, 0, 1);
		
		ObservableList<String> exampleList = FXCollections.observableArrayList("test.pdf", "test.txt"); 
		fileView.setItems(exampleList);
		
		// TODO 
		// in controller class, after setArticle has been called 
		fileView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String filePath = dir + File.separator + newValue; 
//				File file = new File(filePath); 
				System.out.println(filePath);
				try {
					
//					if (Desktop.isDesktopSupported()) {
//						Desktop.getDesktop().open(new File(filePath));
//						return; 
//					}
					
					if (filePath.endsWith("pdf")) {
//						System.out.println("open pdf");
//						ProcessBuilder pb = new ProcessBuilder(); 
//						pb.command("bash", dir + File.separator + "opdenPdf.sh " + filePath); 
//						pb.start(); 
						new ProcessBuilder("/home/alex/FiT/SourcesExamples/openPdf.sh", filePath).start();						
					} else if (filePath.endsWith("msg")) {
						
					} else if (filePath.endsWith("html")) {
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				// open file
			}
		});
		
		fileView.setOnDragOver(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard(); 
				if (db.hasFiles()) {
					event.acceptTransferModes(TransferMode.COPY);
				} else {
					event.consume();
				}
			}
		});
		
		fileView.setOnDragDropped(new EventHandler<DragEvent>() {
			@Override
			public void handle(DragEvent event) {
				Dragboard db = event.getDragboard(); 
				boolean success = false; 
				if (db.hasFiles()) {
					success = true; 
					String filePath = null; 
					for (File file : db.getFiles()) {
						filePath = file.getAbsolutePath(); 
						System.out.println(filePath);
					}
				}
				event.setDropCompleted(success);
				event.consume();
			}
		});
		
	}
	
	public void setArticle(FitArticle article) {
		
		if (article.getId() != 0) {
			// show file list 
			try {

				// TODO use relative path!!! 
//				dir = "K:\\99_Programmieren\\FiTApp\\Quellen\\" + article.getId();
				dir = "Quellen\\" + article.getId();
				
				
				dir = "/home/alex/FiT/SourcesExamples/" + article.getId(); 
				
				// test if directory already exists 
				// if not: ask if it should be created 

			} catch (Exception e) {
				// tell user that sources can't be found (possibly due to wrong or missing directory) 
				// disable source pane, ask if sources folder should be created 
				// if new article: 
			}
			
		}	
			
	}
		
	

}
