package view.overview;

import java.util.List;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

import controller.Controller;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import lombok.Getter;
import lombok.Setter;
import model.FitArticle;

public class ArticlesTable extends StackPane {
	
	private static final Logger log = Controller.log;
	
	private static final String EVENT_TYPE_CLICK = "click";
	
	private static final String CONTAINER_ARTICLES = "articlesContainer";
	
	private OverviewTable overviewTable; 
	@Getter
	private TabPane tabPane; 
	
	private ProgressIndicator progressIndicator; 
	
	@Setter
	private int rowsPerTab; 
	/**
	 *  
	 */
	public ArticlesTable(OverviewTable overviewTable) {
		
		super(); 
		
		this.overviewTable = overviewTable; 
		
		// set initial tab in order to make initial resizing possible when starting the application 
		tabPane = new TabPane(); 
		Tab initialTab = new Tab(); 
		initialTab.setText("" + 1);
		initialTab.setClosable(false);
		WebView newWebView = new WebView(); 
		newWebView.setVisible(true);
		initialTab.setContent(newWebView); 
		tabPane.getTabs().add(initialTab); 
		tabPane.setVisible(true);
		
//		webView = new WebView();
//		webView.getEngine().setJavaScriptEnabled(false);
//		webView.getEngine().getLoadWorker().stateProperty().addListener((ov, ostate, nstate) -> {
//			if(nstate == Worker.State.SUCCEEDED) {
//				Document doc = webView.getEngine().getDocument();
//				
//				Element articlesTable = doc.getElementById(CONTAINER_ARTICLES);
//				NodeList trs = articlesTable.getElementsByTagName("button");
//				for(int e = 0, elen = trs.getLength(); e < elen; e++) 
//				{
//					EventTarget tr = (EventTarget) trs.item(e);
//					tr.addEventListener(EVENT_TYPE_CLICK, editEventListener, false);
//				}	
//			}
//		});	
//		webView.setVisible(true);
		
		progressIndicator = new ProgressIndicator(); 
		progressIndicator.setVisible(false);
		
		rowsPerTab = 40; 
		
		this.getChildren().addAll(tabPane, progressIndicator); 
		
	}
	
	public void showEmptyResults() {
		
		WebView newWebView = new WebView(); 
		newWebView.getEngine().setJavaScriptEnabled(false);
		newWebView.setVisible(true);
		renderPage(null, newWebView, false); 
		Tab newTab = new Tab(); 
		newTab.setText("" + 1);
		newTab.setClosable(false);
		newTab.setContent(newWebView);
		tabPane.getTabs().add(newTab); 
		
	}
	
	public void setup(int allArticles) {
		
//		System.out.println("setup thread: " + Thread.currentThread().getName()); 
		
		overviewTable.setCount(allArticles);
		tabPane.getTabs().clear();
		int tabs = (allArticles + rowsPerTab - 1) / rowsPerTab + 1; // round up integer division
		if (allArticles == 0) {tabs = 2;}
		for (int i = 1; i < tabs; i++) {
			Tab newTab = new Tab(); 
			newTab.setText("" + i);
			newTab.setClosable(false);
			tabPane.getTabs().add(newTab); 
		}

		
	}
	
	public void renderNextPage(int page, List<FitArticle> results, EventListener editEventListener, EventListener linkEventListener, boolean firstCall) {
		
		log.info("in renderNextPage");
		
		WebView newWebView = new WebView(); 
		newWebView.getEngine().setJavaScriptEnabled(true);
		newWebView.getEngine().getLoadWorker().stateProperty().addListener((ov, ostate, nstate) -> {
			if(nstate == Worker.State.SUCCEEDED) {
				
				if (firstCall) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {
//							 TODO Auto-generated method stub
							Document doc = newWebView.getEngine().getDocument();
							
							Element articlesTable = doc.getElementById(CONTAINER_ARTICLES);
							NodeList trs = articlesTable.getElementsByTagName("button");
							for(int e = 0, elen = trs.getLength(); e < elen; e++) 
							{
								EventTarget tr = (EventTarget) trs.item(e);
								tr.addEventListener(EVENT_TYPE_CLICK, editEventListener, false);
							}	
							NodeList links = doc.getElementsByTagName("a"); 
							for (int i = 0; i < links.getLength(); i++) {
								((EventTarget)links.item(i)).addEventListener("click", linkEventListener, false);
							}
							
						}
					});
				} else {
					Document doc = newWebView.getEngine().getDocument();
					
					Element articlesTable = doc.getElementById(CONTAINER_ARTICLES);
					NodeList trs = articlesTable.getElementsByTagName("button");
					for(int e = 0, elen = trs.getLength(); e < elen; e++) 
					{
						EventTarget tr = (EventTarget) trs.item(e);
						tr.addEventListener(EVENT_TYPE_CLICK, editEventListener, false);
					}	
					NodeList links = doc.getElementsByTagName("a"); 
					for (int i = 0; i < links.getLength(); i++) {
						((EventTarget)links.item(i)).addEventListener("click", linkEventListener, false);
					}					
				}
			}
		});	
		newWebView.setVisible(true);
		newWebView.zoomProperty().bind(overviewTable.getHeader().getZoom().valueProperty());
		
		log.info("before rendering on webview (listener of worker set)");
		renderPage(results, newWebView, firstCall); 
		
		log.info("before setting content of tab");
		tabPane.getTabs().get(page - 1).setContent(newWebView);
		log.info("after setting content of tab");
		
	}
	
	
	private void renderPage(List<FitArticle> results, WebView currentWebView, boolean firstCall) {
		
		log.info("in renderPage");
		
		StringBuffer html = new StringBuffer(); 
		
    	html.append("<html>");
    	html.append("<head>");
    	html.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"")
    	    .append(ArticlesTable.class.getResource("style.css"))
    	    .append("\">");
    	html.append("</head>");
    	html.append("<body>");
    	
		html.append("<div class=\"js-articlesPage\" id=\"").append(CONTAINER_ARTICLES).append("\">");
		if (results != null && results.size() > 0) {
			HtmlTableRenderer.render(results, html);
		} else {
			html.append("<div class=\"").append("empty-result").append("\">");
			html.append("<b>Keine Resultate</b>");
			html.append("</div>");
		}
		html.append("</div>");
		
//    	html.append("<script type=\"text/javascript\" src=\"jquery.js\"></script>"); 
//    	html.append("<script type=\"text/javascript\" src=\"search.js\"></script>"); 
//		html.append("<script type=\"text/javascript\" src=\"").append(ArticlesTable.class.getResource("jquery.js")).append("\"></script>"); 
		html.append("<script type=\"text/javascript\" src=\"").append(ArticlesTable.class.getResource("search.js")).append("\"></script>"); 
//		html.append("<script type=\"text/javascript\">document.getElementById(\"edit\").innerHTML=\"test\"</script>"); 

    	html.append("</body>");
    	html.append("</html>");
    	
    	WebEngine engine = currentWebView.getEngine(); 
    	
    	log.info("before loading content in engine");
//    	System.out.println(html.toString().substring(0, 5000));
    	if (firstCall) {
        	Platform.runLater(new Runnable() {
    			@Override
    			public void run() {
    				engine.loadContent(html.toString());	
    			}
    		});    		
    	} else {
			engine.loadContent(html.toString());	    		
    	}
		
	}

}
