package controller.export;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controller.Controller;
import controller.MsAccess;
import model.FitArticle;
import model.Rubric;
import model.Subject;
import model.SupportType;
import model.TargetAudience;
import model.edit.Code;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.WebLinkEntry;
import model.edit.TextEntry.TextEntryFormat;

public enum Export {
	
	INSTANCE; 
	
	
	public List<FitArticle> getExportArticles() {
		MsAccess db = new MsAccess();
		
		return 	db.getExportArticles(); 
		
	}

}
