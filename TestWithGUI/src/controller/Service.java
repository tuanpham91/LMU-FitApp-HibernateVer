package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;
import lombok.Getter;
import lombok.Setter;
import model.FitArticle;
import model.Rubric;
import model.Subject;
import model.SupportType;
import model.TargetAudience;
import model.edit.Code;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.TextEntry.TextEntryFormat;
import model.edit.WebLinkEntry;
import test.PerformanceTest;
import view.Search;

/**
 * Singleton to handle constraints (filter, search, ordering) for overview.
 * <p>
 * Constraints are managed in several lists, depending on which tables in the
 * database they refer to and how they are set by the user. Constraints relevant
 * to the table t_fit_article are realized within the sql queries (via
 * {@link Constraint#getQueryPart()}). Other constraints are processed in
 * {@link #getNextArticle(ResultSet, ResultSet, ResultSet, ResultSet, ResultSet, ResultSet)}
 * , which is called in order to generate {@link FitArticle} instances from the
 * ResultSets retrieved from the sql queries.
 * <p>
 * 
 *
 */
public enum Service {

	INSTANCE;
	// Tuan
	public static boolean advanceSearch = false;
	public static boolean searchString = false;
	public static String currentSearchString;
	public MsAccess db = new MsAccess();
	public static List<Integer> listIds;
	public static Filter filter = Filter.STANDARD;
	
	@Getter
	// private static final Logger log =
	// Logger.getLogger(Service.class.getName());
	private static final Logger log = Controller.log;

	@Setter
	private Controller controller;
	
	@Getter
	private List<Ordering> orderList = new ArrayList<>();
	
	private List<FitArticle> resList  = new ArrayList<>();
	
	private int allArticles; 

	public void showStartOverview() {
		//Find all Article, die im Archiv sind, also  recurrent, aber noch kein Child Copy hat
		db.createRecurrentArticles();
	}
	public void showOverview(int page, boolean firstCall) {	
		PerformanceTest.setStarttime();
		controller.renderPage(db.getArticleByFilter(listIds, page), page, firstCall);
		PerformanceTest.printTime("Fetching and rendering");
	}
	
	public int getCount() {
		PerformanceTest.setStarttime();
		this.listIds = db.getCountArticleByFilter(filter, orderList);
		PerformanceTest.printTime("Counting");
		return this.listIds.size();
	}
	
	public void setFilterConstraints(Filter filter) {
		this.filter = filter;	
	}
	
	public void setSimpleSearchConstraints(String search, boolean a) {
		searchString = true;
		this.currentSearchString= search;
	}

	public void resetSearchConstraints() {
		searchString = false;
		this.currentSearchString="";
	}
	
	public void resetAdvanceSearch() {
		advanceSearch = false;	
		
	}
	
	public void setOrdering(List<Ordering> order) {
		this.orderList = order;
	}

	public void resetOrder() {
		this.orderList = new ArrayList<>();
	}

	public void setAdvancedSearchConstraints() {
		advanceSearch =true;
	}


	public enum Filter {
		STANDARD, MIT_ARCHIV, NUR_ARCHIV, NICHT_ABG_AKTUELL, NICHT_ABG_ALLE, NICHT_KORR_AKTUELL, NICHT_KORR_ALLE, NICHT_EXP_AKTUELL, NICHT_EXP_ALLE, EXP_AKTUELL, EXP_ALLE;
	}

	private enum HeaderType {
		HEADING_1, HEADING_2, HEADING_3, HEADING_4, HEADING_5, HEADING_6, HEADING_7, TEXT_BB;
	}

}
