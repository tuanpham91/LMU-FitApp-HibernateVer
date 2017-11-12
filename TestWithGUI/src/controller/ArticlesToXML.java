package controller;


import java.util.ArrayList;
import java.util.List;

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


public class ArticlesToXML {
	/* Tag file still not created 
	 * 
	 * Headerlist :
	 * h1 : International oder National
	 * h2 : Rubric
	 * h3 : Heading 3
	 * h4 : Heading 4
	 * h5 : Heading 5
	 * tx : Text
	 * 
	 * Mehrere typen : Absatz nach am Ende(?), Absatz nach Infoliste , Absatz nach Ansprechpartner, Absatz nach Ansprehpartner und am Ende, Absatz nach InfoListe und am Ende
	 * - Absatz nach Infoliste : txni
	 * - Absatz nach ContactPartner : txnc 
	 * - Absatz nach Infoliste und am Ende : txnie
	 * - Absatz nach ContactPartner und am Ende: txnce
	 * - Absatz am Ende = txe
	 *  Bin nicht so sicher wie "am Ende"  gemeint ist.
	 *  TODO : Am Ende heißt, dass es als nächstes ein ContactPartner oder Infolist kommt
	 *  TODO : Fix Articles Kategoriesierung
	 * infa : für erster InfoListEntry
	 * inf : InfoListEntry
	 * 
	 * Code and Code without distance
	 * cdd : Code with distance
	 * cd : Code without distance
	 * wl : Weblink ( Weblink soll nach Zeilenbreite getrennt werden)
	 * cp : Contactperson (Jede Kontaktpersonentry hat einen Tag)  
	 * 
	 * Alle Erweiterung deutet an themenspezifische Tags:
	 *  Themenoffene : 1
	 *  Life Science : 2
	 *  Physic Engineering :3
	 *  Social and Humanities : 4
	 */
	public boolean lastArticle;
	// Eigentliche Länge ist 47, zur Sicherheit wird es um 3 verrigert
	// Nicht physikalische Länge von String, aber geht noch
	public static int rowWidth = 44;
	

	public static String exportXML (List<FitArticle> a) {
		// Divide in to 4 Categories
		List<FitArticle> themenOffen = new ArrayList<FitArticle>();
		List<FitArticle> lifeScience = new ArrayList<FitArticle>();
		List<FitArticle> physicEngi = new ArrayList<FitArticle>();
		List<FitArticle> societyHum = new ArrayList<FitArticle>();
		StringBuilder all = new StringBuilder();
		
		all.append("<xml>\n"); // Header of a XML file	
		for (FitArticle article : a) {
			if (article.getMainSubject()== Subject.THEMENOFFENES_PROGRAMM) {
				themenOffen.add(article);
			} else if (article.getMainSubject() == Subject.LIFE_SCIENCES){
				lifeScience.add(article);
			} else if (article.getMainSubject() == Subject.PHYSICAL_SCIENCES_AND_ENGINEERING) {
				physicEngi.add(article);
			} else {
				societyHum.add(article);
			}		
		}
		System.out.println(themenOffen.size() + " " + lifeScience.size() + " " + physicEngi.size()+"\n");
		all.append(h1("Themenoffene Programme",1));
		all.append(themenArticles(themenOffen,1));
		all.append(h1("Life Sciences",2));
		all.append(themenArticles(lifeScience,2));
		all.append(h1("Physical Sciences und Engineering",3));
		all.append(themenArticles(physicEngi,3));
		all.append(h1("Social Sciences und Humanities",4));
		all.append(themenArticles(societyHum,4));
		all.append("</xml>"); // End of a XML File
		return all.toString();
	}
	public static String themenArticles(List<FitArticle> themenArticles, int themenIndex) {
		StringBuilder all1 = new StringBuilder();
		List<FitArticle> international = new ArrayList<FitArticle>();
		List<FitArticle> national = new ArrayList<FitArticle>();
		for (FitArticle article : themenArticles) {
			if (article.getRubric().isInternational()) {
				international.add(article);
			} else {
				System.out.println("Oh i found this\n");
				System.out.println(article.getRubric().toString());
				national.add(article);
			}
		}
			all1.append(h1("Internationale Programme",themenIndex));
			all1.append(intXMLArticle(international,themenIndex));
			all1.append(h1("Nationale Programme",themenIndex));
			all1.append(natXMLArticle(national,themenIndex));
		return all1.toString();
	}
	public static String intXMLArticle(List<FitArticle> intArticles, int themenIndex) {
		StringBuilder intArticleXML = new StringBuilder();
		List<FitArticle> horizonArticles = new ArrayList<FitArticle>();
		List<FitArticle> euProgrammArticles = new ArrayList<FitArticle>();
		List<FitArticle> intProgrammArticle = new ArrayList<FitArticle>();

		for (FitArticle article : intArticles) {
			System.out.println(article.getRubric().toString());
			if (article.getRubric() == Rubric.HORIZONT_2020) {
				horizonArticles.add(article);
				System.out.println("Horizon aaaaaaaa\n");
			} else if (article.getRubric()== Rubric.OTHER_EU_PROGRAMM) {
				euProgrammArticles.add(article);
			} else {
				intProgrammArticle.add(article);
			}
		}
		
			if (horizonArticles.size() >0) {
				intArticleXML.append(h2("EU Programme : Horizont 2020",themenIndex));
				for(FitArticle article : horizonArticles) {
					if (article != null) {
						intArticleXML.append(articleToXML(article,1));
					}
				}
			}
			if (euProgrammArticles.size() > 0) {
				intArticleXML.append(h2("EU-Programme",themenIndex));
				for(FitArticle article : euProgrammArticles) {
					if (article != null) {
						intArticleXML.append(articleToXML(article,1));
					}
				}
			}
			if (intProgrammArticle.size()>0) {
				intArticleXML.append(h2("Weitere Förderinstitutionen (int)",themenIndex));
				for(FitArticle article : intProgrammArticle ) {
					if (article != null) {
						intArticleXML.append(articleToXML(article,1));
					}
				}
			}
			
		return intArticleXML.toString();
		
	}
	public static String natXMLArticle(List<FitArticle> natArticles, int themenIndex) {
		StringBuilder natArticleXML = new StringBuilder();
		List<FitArticle> lmuArticles = new ArrayList<FitArticle>();
		List<FitArticle> bmbfArticles = new ArrayList<FitArticle>();
		List<FitArticle> dfgArticles = new ArrayList<FitArticle>();
		List<FitArticle> natProgrammArticles = new ArrayList<FitArticle>();

		for (FitArticle article : natArticles) {
			if (article.getRubric().getAkronym() == "LMU") {
				lmuArticles.add(article);
			} else if (article.getRubric().getAkronym()== "BMBF") {
				bmbfArticles.add(article);
			} else if (article.getRubric().getAkronym()=="DFG"){
				dfgArticles.add(article);
			} else {
				natProgrammArticles.add(article);
			}
		}
		if (lmuArticles.size() >0) {
			natArticleXML.append(h2("LMU",themenIndex));
			for(FitArticle article : lmuArticles) {
				if (article != null) {
					natArticleXML.append(articleToXML(article,themenIndex));
				}
			}
		}
		if (bmbfArticles.size() > 0) {
			natArticleXML.append(h2("EU-Programme",themenIndex));
			for(FitArticle article : bmbfArticles) {
				if (article != null) {
					natArticleXML.append(articleToXML(article,themenIndex));
				}
			}
		}
		if (dfgArticles.size()>0) {
			natArticleXML.append(h2("DFG",themenIndex));
			for(FitArticle article : dfgArticles ) {
				if (article != null) {
					natArticleXML.append(articleToXML(article,themenIndex));
				}
			}
		}
		if (natProgrammArticles.size()>0) {
			natArticleXML.append(h2("Weitere Förderinstitution",themenIndex));
			for(FitArticle article : natProgrammArticles ) {
				if (article != null) {
					natArticleXML.append(articleToXML(article,themenIndex));
				}
			}
		}
		
		return natArticleXML.toString();
		
	}

	public static String articleToXML(FitArticle ar, int themenIndex) {
		
		boolean hasInfoList = false;
		boolean hasContactPartner = false; //check-out condition is not implemented because contact partner is completly not used.
		
		StringBuilder aString = new StringBuilder();
		
		if (ar!= null && ar.getElements().size()>0) {
			for (int i = 0 ; i< ar.getElements().size(); i++) {
				FitArticleElement e = ar.getElements().get(i);
				
				FitArticleElement next = null;
				if (i<ar.getElements().size()-1) {
					next = ar.getElements().get(i+1);
				}
				if (e instanceof TextEntry) {
					TextEntry st = castElement(e);				
					TextEntryFormat stFormat = st.getFormat();
					if (TextEntryFormat.HEADING_3 == st.getFormat()) {
						aString.append(h3(st.getContent(), ar));
					}
					if (TextEntryFormat.HEADING_4 == st.getFormat()) {
						aString.append(h4(st.getContent()));
					}
					if (TextEntryFormat.HEADING_5 == st.getFormat()) {
						aString.append(h5(st.getContent()));
					}
					if (TextEntryFormat.TEXT_BB == st.getFormat()) {
						boolean amEnde = false;
						if (next instanceof ContactPerson || next instanceof InfoListEntry) {
							amEnde = true;
						}
						String[] parts = st.getContent().split("\n");
						for (int k = 0 ; k< parts.length; k++) {
							if (parts[k].equals("")) {
								continue;
							}
							if (k==0) {
								aString.append(tx(parts[0],hasInfoList,hasContactPartner,false));
							} else if (k==parts.length-1 && k != 0) {
								aString.append(tx(parts[k],hasInfoList,hasContactPartner,true));
							} else {
								aString.append(tx(parts[k],false,false,false));
							}
						}
						
					}
				}
			
			else if (e instanceof Code){
				Code eCode = castElement(e);
				aString.append(cd(eCode.getCode(),false)); // TODO - Wann ist code ohne Abstand ???
			} 
		
			else if ( e instanceof WebLinkEntry) {
				WebLinkEntry eWeb = castElement(e);
				if (eWeb.getInfo()!="") {
					aString.append(wl(eWeb.getInfo(), themenIndex));
				}
				String urlAppend = eWeb.getUrl();
				if (urlAppend.startsWith("https")) {
					urlAppend = urlAppend.replaceAll("https", "http");
				}
				
				aString.append(wl(urlAppend,themenIndex));
				
				
			} else if (e instanceof InfoListEntry) {
				InfoListEntry eInf = castElement(e);
                if (hasInfoList) {	
                	aString.append(inf(eInf.getName() + eInf.getValue(),themenIndex));
                } else {
                	aString.append(infa(eInf.getName() + eInf.getValue(),themenIndex));
                	hasInfoList = true;
                }
                
			}
		}
		}
		return aString.toString();
	}
	
	public static <T extends FitArticleElement> T castElement(FitArticleElement e) {
		return (T) e;
	}
	
	public static String h1(String a, int themenIndex) {
		return "<h1"+themenIndex+">" +a + "</h1"+themenIndex+">\n";
	}
	
	public static String infa(String a, int b) {
		return "<infa"+ b+">" +a + "</infa"+b+">\n";
	}
	
	public static String h2(String a, int themenIndex) {
		return "<h2"+themenIndex+">"+a+"</h2"+themenIndex+">\n";
	}
	
	public static String h3(String a, FitArticle b) {
		return "<h3>"+a+"</h3>\n";
	}
	
	public static String h4(String a) {
		return "<h4>"+a+"</h4>\n";
	}
	
	public static String h5(String a) {
		return "<h5>"+a+"</h5>\n";
	}
	
	public static String tx(String a,boolean hasInfoList, boolean hasContactPartner,boolean amEnde) {
		
		if (hasInfoList && amEnde) {
			return "<txnie>"+a+"</txnie>\n";
		}
		if (hasContactPartner &&amEnde) {
			return "<txnce>"+a+"</txnce>\n";
		}
		if (hasInfoList) {
			return "<txni>"+a+"</txni>\n";
		}
		if (hasContactPartner) {
			return "<txnc>"+a+"</txnc>\n";
		}
		if (amEnde) {
			return "<txe>"+a+"</txe>\n";
		}
		return "<tx>"+a+"</tx>\n";
	}
	
	public static String cd(String a, boolean distance) {
		if (distance) {
			return "<cdd>"+a+"</cdd>\n";
		}
		return "<cd>"+a+"</cd>\n";
	}
	
	public static String wl(String a, int themenIndex) {
		//TODO - apply rowWidth still on working 
		// Update , it actually works but still have to do real test	
		StringBuilder res = new StringBuilder();
		
		int count = 0;
		int i = 0;
		int countLines = 1;
		while(i< a.length()) {
			if (count < rowWidth) {
				res.append(a.charAt(i));
				count ++;
				i++;
			} else {
				res.append("\n");
				count = 0;
				countLines ++;
			}
		}
		StringBuilder resultFinal = new StringBuilder();
		resultFinal.append("<wl"+countLines+themenIndex+">");
		resultFinal.append(res.toString());
		resultFinal.append("</wl"+countLines+themenIndex+">\n");
		return resultFinal.toString();
	}
	
	public static String inf (String a, int b) {
		return "<inf"+b+">" + a + "</inf"+b+">\n";
	}
	
	public static String cp (String a, int themenIndex) {
		return "<cp"+ themenIndex+">" + a + "</cp" + themenIndex + ">";
	}
} 
