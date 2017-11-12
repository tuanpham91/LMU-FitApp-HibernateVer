package view.overview;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import controller.export.FitHtmlExport;
import model.FitArticle;
import model.edit.Code;
import model.edit.FitArticleElement;
/**
 * Translate data into Webview Accordingly in HTML Code
 * This is not really a Table but rather HTLM code that makes it look like one
 * @author tuan
 *
 */
public class HtmlTableRenderer {
	
	private static final String CHECKED_COLOR = "/resources/checked-green.png";
	private static final String CHECKED_GRAY = "/resources/checked-gray.png";	

	private static final String WEB_COLOR = "/resources/web-color.png";	
	private static final String WEB_GRAY = "/resources/web-gray.png";	

	private static final String FLAG_EN      = "/resources/ico_en.png";
	private static final String FLAG_EN_GRAY = "/resources/ico_en_gray.png";
	
	private static final String LOGO_FIT      = "/resources/ico_fit.png";
	private static final String LOGO_FIT_GRAY = "/resources/ico_fit_gray.png";
	
	public static final String ATTR_ARTICLE_ID = "data-article-id";
	
	private static final String DATE_FORMAT = "dd.MM.yyyy";
		
    public static void render(List<FitArticle> results, StringBuffer b) {
    	
		b.append("<table id=\"overviewTable\" cellspacing=\"0\" cellpadding=\"0\">");
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
//		b.append("<col width\"=100px\">"); 
		tableHeader(b);

		Iterator<FitArticle> iterator = results.iterator();
		int i = 0;
		while (i<40 && iterator.hasNext()) {
			i++;
			FitArticle prev = iterator.next(); 
			newTableRow(prev, b); 
			// newTableRow();

		}

		b.append("</table>");
    }
    /*
     * Table Header in Overview with Column names will be drawn here.
     * 
     */
	private static void tableHeader(StringBuffer b) {
		b.append("<tr id=\"header\" class=\"table-header\">");
		b.append("<td width=\"5\">FB</td>"); 
		b.append("<td width=\"26\">Rubrik</td>"); 
		b.append("<td width=\"4\">Prio.</td>"); 
//		b.append("<td width=\"20\">EN</td>"); 
		b.append("<td width=\"60\">Programm</td>");
		b.append("<td width=\"18\">Frist</td>"); 
		b.append("<td width=\"30\">Code</td>"); 
		b.append("<td width=\"18\">Zielgruppe</td>"); 
		b.append("<td width=\"30\">Förderart</td>"); 
		b.append("<td align=\"center\" width=\"10\">Export</>"); 
		b.append("<td colspan=\"3\" align=\"center\" width=\"20\">Status</td>"); 
//		b.append("<td>Rubrik / Bewerb. Frist / Zielgruppe</td>");
//		b.append("<td>Exp. Fit? / Exp. Web? / Exp. Web (en)?</td>");
//		b.append("<td>Prio. / En.</td>");
//		b.append("<td>Abg.?</td>");
//		b.append("<td>Korr.?</td>");
//		b.append("<td>Exp.f.?</td>");
		b.append("<td width=\"6\"></td>");
		b.append("</tr>");
	}
	
	/**
	 * Appending a new Article in to the given Table
	 * 
	 * @param prev
	 * @param b
	 */
	private static void newTableRow(FitArticle prev, StringBuffer b) {
	
    	b.append("<tr class=\"detailsRow\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">");
    	
    	// column 1
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">").append("<div>"); 
    	b.append(prev.getMainSubject() != null ? prev.getMainSubject().getAkronym() : "Kein HB");
    	if (prev.getMinorSubjects().size() > 0) {
    		b.append(" (").append(prev.getMinorSubjects().get(0).getAkronym()); 
    		if (prev.getMinorSubjects().size() > 1) {
    			for (int i = 1; i < prev.getMinorSubjects().size(); i++) {
    				b.append(", " + prev.getMinorSubjects().get(i).getAkronym());
    			}
    		}
    		b.append(")"); 
    	}
    	b.append("</div>").append("</td>"); 
    	
    	// column 2
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">").append("<div>"); 
    	b.append("<div>").append(prev.getRubric() != null ? prev.getRubric().getAkronym() : "Keine Rubrik").append("</div>");
    	b.append("</div>").append("</td>"); 
    	
    	// column 3
    	b.append("<td style=\"text-align: center\" class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">").append("<div>"); 
    	b.append(prev.getPriority());
    	b.append("</div>").append("</td>"); 
    	
    	// column 4
//    	b.append("<td style=\"text-align: center\" class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">").append("<div class=\"list-images\">"); 
//		if (prev.isEn()) {
//			appendImage(FLAG_EN, b);
//		}
//    	b.append("</div>").append("</td>"); 
    	
    	// column 5
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">").append("<div>"); 
    	if (prev.getHeaderList().getHeading4() != null) {
        	b.append("<fit_h4>" + prev.getHeaderList().getHeading4() + "</fit_h4>" + "<br>");    		
    	}
    	b.append("<fit_h3>" + prev.getHeaderList().getHeading3() + "</fit_h3>");
    	if (prev.getHeaderList().getHeading5() != null) {
        	b.append("<br>" + prev.getHeaderList().getHeading5());     		
    	}
    	if (prev.getHeaderList().getHeading6().size() != 0) {

    		b.append("<ul>"); 
    		Iterator<String> iterator = prev.getHeaderList().getHeading6().iterator(); 
    		while(iterator.hasNext()) {
    			b.append("<li> <fit_h6>" + iterator.next() + "</fit_h6> </li>"); 
//    			b.append("<br>" + "<fit_h6>" + iterator.next() + "</fit_h6>");  
    		}
    		b.append("</ul>"); 
    		
    	}
    	b.append("</div>").append("</td>");
    	
    	// column 6 
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\"><div>"); 
    	b.append(formatDate(prev.getApplicationDeadline())); 
    	b.append("</div></td>");    	
    	
    	// column 7
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\"><div>"); 
    	
    	for (FitArticleElement elem : prev.getElements()) {
    		if (elem instanceof Code ) {
    			Code code = (Code) elem;
    			b.append("<font size=\"2\">"); 
    			b.append( code.getCode() + "</font>" + "<p id=\"code\">"); 
    	
    		}
    	}
    	b.append("</div></td>");  
    	
    	// column 8 
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\"><div>"); 
    	b.append(prev.getTargetAudience().toShortString()); 
    	b.append("</div></td>");  
    	
    	// column 9
    	b.append("<td class=\"clickable\"").append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\"><div>"); 
//    	String supportTypeContent = prev.getSupportType().toString().replace(" ", "<br>"); 
    	String supportTypeContent = prev.getSupportType().toShortString("<br>"); 
    	b.append(supportTypeContent); 
    	b.append("</div></td>");     
    	
    	// column 10 
    	b.append("<td align=\"center\"><div class=\"list-images\">");
    	appendImage(prev.getExportFit() ? LOGO_FIT : LOGO_FIT_GRAY, b);
    	b.append("<br>"); 
    	appendImage(prev.getExportWeb() ? WEB_COLOR : WEB_GRAY, b);
    	b.append("<br>"); 
    	b.append("</div></td>");
    	
    	// column 11
    	appendImageColumn("Abgeschlossen", prev.getCompleted() ? CHECKED_COLOR : CHECKED_GRAY, englVersionOrNull(prev, prev.getCompletedEn()), b);
    	appendImageColumn("Korrigiert", prev.getAuthorCorrected() ? CHECKED_COLOR : CHECKED_GRAY, englVersionOrNull(prev, prev.getAuthorCorrectedEn()), b);
    	appendImageColumn("Exportfertig", prev.getExportReady() ? CHECKED_COLOR : CHECKED_GRAY, englVersionOrNull(prev, prev.getExportReadyEn()), b);
    	
    	// column 12
    	b.append("<td>");
    	if(prev.isArchived()) {
    		b.append("<span>Im Archiv</span>");
    	}
    	else {
	    	b.append("<button ").append("id=\"edit\" style=\"background-image: url(" + HtmlTableRenderer.class.getResource("/resources/pen_write.png") + ")\" ");
	    	b.append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">");
	    	b.append("</button>"); 
    	}
    	
    	b.append("<button ").append("id=\"copy\" style=\"background-image: url(" + HtmlTableRenderer.class.getResource("/resources/copy.png") + ")\" ");
    	b.append(ATTR_ARTICLE_ID).append("=\"").append(prev.getId()).append("\">"); 
    	b.append("</button>"); 
    	
    	b.append("</td>");
    	
    	b.append("</tr>");
    	
    	// preview 
    	b.append(String.format("<tr class=\"article-preview\" id=\"articlePreview%s\">", prev.getId()));
    	b.append("<td colspan=\"13\">"); 
    	b.append(FitHtmlExport.export(prev)); 
    	b.append("</td>"); 
    	b.append("</tr>"); 
		
	}
	
	/**
	 * add an image into table
	 * @param name
	 * @param html
	 */
    private static void appendImage(String name, StringBuffer html) {
    	if(name != null) {
    		html.append("<img src=\"");
    		html.append(HtmlTableRenderer.class.getResource(name));
    		html.append("\" align=\"middle\""); 
    		html.append(">");
    	}
    }
    
    private static void appendImageMouseover(String name, String title, StringBuffer html) {
    	if(name != null) {
    		html.append("<img src=\"");
    		html.append(HtmlTableRenderer.class.getResource(name));
    		html.append("\" title=\"" + title + "\" align=\"middle\""); 
    		html.append(">");
    	}
    }    
    
    private static void appendImageColumn(String title, String flag1, String flag2, StringBuffer b) {
    	b.append("<td class=\"block-images\">");
    	//b.append("<span>").append(title).append("</span>");
    	b.append("<div>");
    	if(flag1 != null) appendImageMouseover(flag1, title, b);
    	if(flag2 != null) appendImageMouseover(flag2, title, b);
    	b.append("</div>");
    	b.append("</td>");	
    }
    
    private static String englVersionOrNull(FitArticle fit, boolean condition) {
    	if(!fit.getEn()) return null;
    	return condition ? FLAG_EN : FLAG_EN_GRAY;
    }
    
	public static String formatDate(Date date) {
		if(date != null) {
			DateFormat format = new SimpleDateFormat(DATE_FORMAT);
			return format.format(date);	
		}
		else {
			return "";
		}
	}

}
