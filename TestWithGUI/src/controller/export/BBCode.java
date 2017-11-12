package controller.export;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// http://stackoverflow.com/questions/849396/java-bbcode-library
public class BBCode {

    final static Map<String,String> BB = new HashMap<>();

    static {
	    BB.put("(\r\n|\r|\n|\n\r)", "<br/>");
	    BB.put("\\[b\\](.+?)\\[/b\\]", "<strong>$1</strong>");
	    BB.put("\\[i\\](.+?)\\[/i\\]", "<span style='font-style:italic;'>$1</span>");
	    BB.put("\\[u\\](.+?)\\[/u\\]", "<span style='text-decoration:underline;'>$1</span>");
	    BB.put("\\[h1\\](.+?)\\[/h1\\]", "<h1>$1</h1>");
	    BB.put("\\[h2\\](.+?)\\[/h2\\]", "<h2>$1</h2>");
	    BB.put("\\[h3\\](.+?)\\[/h3\\]", "<h3>$1</h3>");
	    BB.put("\\[h4\\](.+?)\\[/h4\\]", "<h4>$1</h4>");
	    BB.put("\\[h5\\](.+?)\\[/h5\\]", "<h5>$1</h5>");
	    BB.put("\\[h6\\](.+?)\\[/h6\\]", "<h6>$1</h6>");
	    BB.put("\\[quote\\](.+?)\\[/quote\\]", "<blockquote>$1</blockquote>");
	    BB.put("\\[p\\](.+?)\\[/p\\]", "<p>$1</p>");
	    BB.put("\\[p=(.+?),(.+?)\\](.+?)\\[/p\\]", "<p style='text-indent:$1px;line-height:$2%;'>$3</p>");
	    BB.put("\\[center\\](.+?)\\[/center\\]", "<div align='center'>$1");
	    BB.put("\\[align=(.+?)\\](.+?)\\[/align\\]", "<div align='$1'>$2");
	    BB.put("\\[color=(.+?)\\](.+?)\\[/color\\]", "<span style='color:$1;'>$2</span>");
	    BB.put("\\[size=(.+?)\\](.+?)\\[/size\\]", "<span style='font-size:$1;'>$2</span>");
	    BB.put("\\[img\\](.+?)\\[/img\\]", "<img src='$1' />");
	    BB.put("\\[img=(.+?),(.+?)\\](.+?)\\[/img\\]", "<img width='$1' height='$2' src='$3' />");
	    BB.put("\\[email\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$1</a>");
	    BB.put("\\[email=(.+?)\\](.+?)\\[/email\\]", "<a href='mailto:$1'>$2</a>");
	    BB.put("\\[url\\](.+?)\\[/url\\]", "<a href='$1'>$1</a>");
	    BB.put("\\[url=(.+?)\\](.+?)\\[/url\\]", "<a href='$1'>$2</a>");
	    BB.put("\\[youtube\\](.+?)\\[/youtube\\]", "<object width='640' height='380'><param name='movie' value='http://www.youtube.com/v/$1'></param><embed src='http://www.youtube.com/v/$1' type='application/x-shockwave-flash' width='640' height='380'></embed></object>");
	    BB.put("\\[video\\](.+?)\\[/video\\]", "<video src='$1' />");
    }
    
	public static String bbcode(String text) {
		String html = text;
		for (Entry<String, String> entry : BB.entrySet()) {
			html = html.replaceAll(entry.getKey().toString(), entry.getValue().toString());
		}
		return html;
	}
}
