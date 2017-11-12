package controller.export;

import model.FitArticle;
import model.edit.Code;
import model.edit.ContactPerson;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.WebLinkEntry;
import model.edit.TextEntry.TextEntryFormat;

public class FitHtmlExport {
	
	public static final String ARTICLE_WRAPPER_DIV_ID = "articleContent";
	
	public static String export(FitArticle article) {
		return new HtmlRenderer(article).render("<div id=\""+ARTICLE_WRAPPER_DIV_ID+"\">", "</div>");
	}
	
	public static String export(FitArticle article, String articleWrapperOpenTag, String articleWrapperCloseTag) {
		return new HtmlRenderer(article).render(articleWrapperOpenTag, articleWrapperCloseTag);
	}
	
	private static class HtmlRenderer 
	{
		final FitArticle article;
		
		FitArticleElement lastElem = null;
		
		HtmlRenderer(FitArticle a) {
			this.article = a;
		}
		
		String render(String wrapperOpenTag, String wrapperCloseTag) 
		{
			final StringBuilder b = new StringBuilder(wrapperOpenTag);
			
//			b.append("<p> Artikel Nr. " + article.getId() + "</p>"); 
			b.append("<p align=\"right\"> Artikel-ID: " + article.getId() + "<br>"); 
			b.append("Parent-ID: "); 
			if (article.isParentArticlePresent()) {
				b.append(article.getParentArticle()); 
			} else {
				b.append("/"); 
			}
			b.append("<br>"); 
			b.append("Child-ID: "); 
			if (article.isChildArticlePresent()) {
				b.append(article.getChildArticle()); 
			} else {
				b.append("/"); 
			}
			b.append("</p>"); 

			for(FitArticleElement elem : article.getElements()) {
				
				// end old list
				if(lastElem != null && (lastElem instanceof InfoListEntry) && !(elem instanceof InfoListEntry)) {
					b.append("</ul>");
				}
				
				// start new list
				if((elem instanceof InfoListEntry) && (lastElem == null || !(lastElem instanceof InfoListEntry))) {
					b.append("<ul>");
				}
				
				renderElement(elem, b);
				lastElem = elem;
			}
						
			// close the list if there are not elements left in the loop
			if(lastElem != null && (lastElem instanceof InfoListEntry)) {
				b.append("</ul>");
			}

//			b.append("<p> Artikel Nr. " + article.getId() + "</p>"); 

			return b.append(wrapperCloseTag).toString();
		}
		
		private void renderElement(FitArticleElement e, StringBuilder b) {
			
			// SimpleText
			if(e instanceof TextEntry) 
			{
				TextEntry st = castElement(e);
				TextEntryFormat stFormat = st.getFormat();
				String tag, content;
				
				if(TextEntryFormat.ENUMERATION == stFormat) {
					tag = "ul";
					content = formatEnumeration(st.getContent());
				}
				
				else if(TextEntryFormat.TEXT_BB == stFormat) {
					tag = "p";
					content = BBCode.bbcode(st.getContent());
				}
				
				else {
					tag = stFormat.name().replace("EADING_", "").toLowerCase();
					content = st.getContent();
				}
				
				b.append("<").append(tag).append(">")
				 .append(content)
				 .append("</").append(tag).append(">");
			}
			
			else if(e instanceof ContactPerson) 
			{
				ContactPerson cpe = (ContactPerson) e;
				String cp = cpe.getContactText() != null ? cpe.getContactText().replace("\n", "<br/>") : "";
				b.append("<div class=\"contact-partner\">").append(cp).append("</div>");
			}
			
			// InfoListEntry
			else if(e instanceof InfoListEntry) 
			{
				InfoListEntry ile = castElement(e);
				b.append("<li><span class=\"info-list-name\">").append(ile.getName()).append("</span>")
				 .append("<span class=\"info-list-value\">").append(ile.getValue()).append("</span></li>");
			}
			
			// Code
			else if(e instanceof Code) 
			{
				Code code = castElement(e);
				b.append("<div class=\"code\">").append(code.getCode()).append("</div>");
			}
			
			else if(e instanceof WebLinkEntry) {
				WebLinkEntry webLink = castElement(e);
				b.append("<div class=\"web-link\">").append(webLink.getInfo()); 
				String link = webLink.getUrl(); 
				if (!link.startsWith("http")) {
					link = "http://" + link; 
				}
				b.append("<span>").append("<a href=\"").append(link).append("\">").append(link).append("</a></span>")
				 .append("</div>");
			}
			
			else 
			{
				b.append("<div>CANT NOT RENDER ").append(e.getClass().getSimpleName()).append("</div>");
			}
		}
		
		@SuppressWarnings("unchecked")
		private <T extends FitArticleElement> T castElement(FitArticleElement e) {
			return (T) e;
		}
		
	}
	
	private static String formatEnumeration(String in) {
		if(in == null || in.trim().length() < 1) {
			return "";
		}
		else {
			String[] parts = in.split("\n\n");
			StringBuilder b = new StringBuilder();
			for(String s : parts) {
				b.append("<li>").append(s.trim()).append("</li>");
			}
			return b.toString();
		}
	}
	
}
