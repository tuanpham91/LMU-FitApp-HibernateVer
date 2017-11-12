package view.edit;

import javafx.beans.binding.NumberBinding;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import model.FitArticle;
import model.Rubric;
import model.Subject;

public class GeneralInfoGrid extends GridPane {
	
	private Label mainSubject, secondarySubjects, targetGroup, supportType; 
	@Getter
	private ToggleGroup toggleGroupMainSubjectArea, toggleGroupRubric, toggleGroupPrio;
	@Getter
	private RadioButton hfachTP, hfachLS, hfachPSE, hfachSSH; 
	@Getter
	private CheckBox nfachTP, nfachLS, nfachPSE, nfachSSH;
	@Getter
	private CheckBox youngResearchers, experiencedResearchers;
	@Getter
	private CheckBox supportIndividualResearch, supportResearchGroups, 
						   supportScholarship, supportPrice, supportTransferSpinOff;
	private Label rubric, international, national, priority; 
	@Getter
	private RadioButton euHorizon, euOther, intOther, lmu, bmbf, dfg, natOther; 
	@Getter
	private RadioButton prio1, prio2, prio3; 
	private VBox left, right; 
	/**
	 * Subclass for General Information, such as Priority, Rubric, Instutition..
	 */
	public GeneralInfoGrid(NumberBinding elementsWidthBinding) {
		
		super(); 
		
		this.prefWidthProperty().bind(elementsWidthBinding);
		
		getStylesheets().add("/view/ArticleEditPage.css"); 
		getStyleClass().add("article-fixed-fields"); 
		
		mainSubject = new Label("Hauptfach"); 
		mainSubject.getStyleClass().add("section-title"); 
		secondarySubjects = new Label("Nebenfach"); 
		secondarySubjects.getStyleClass().add("section-title"); 
		targetGroup = new Label("Zielgruppe"); 
		targetGroup.getStyleClass().add("section-title"); 
		supportType = new Label("Förderart"); 
		supportType.getStyleClass().add("section-title"); 
		
		toggleGroupMainSubjectArea = new ToggleGroup(); 
		hfachTP = new RadioButton("Themenoffenes Programm");
		hfachTP.setToggleGroup(toggleGroupMainSubjectArea);
		hfachTP.setUserData(Subject.THEMENOFFENES_PROGRAMM);
		hfachLS = new RadioButton("Life Sciences"); 
		hfachLS.setToggleGroup(toggleGroupMainSubjectArea);
		hfachLS.setUserData(Subject.LIFE_SCIENCES);
		hfachPSE = new RadioButton("Physical Sciences & Engineering"); 
		hfachPSE.setToggleGroup(toggleGroupMainSubjectArea);
		hfachPSE.setUserData(Subject.PHYSICAL_SCIENCES_AND_ENGINEERING);
		hfachSSH = new RadioButton("Social Sciences & Humanities"); 
		hfachSSH.setToggleGroup(toggleGroupMainSubjectArea);
		hfachSSH.setUserData(Subject.SOCIAL_SCIENCES_AND_HUMANITIES);
		
		nfachTP = new CheckBox("Themenoffenes Programm"); 
		nfachLS = new CheckBox("Life Sciences"); 
		nfachPSE = new CheckBox("Physical Sciences & Engineering"); 
		nfachSSH = new CheckBox("Social Sciences & Humanities"); 
		
		youngResearchers = new CheckBox("Wissenschaftlicher Nachwuchs"); 
		experiencedResearchers = new CheckBox("Erfahrene Wissenschaftler/innen"); 
		
		supportIndividualResearch = new CheckBox("Forschungsprojekt Individualförderung");
		supportResearchGroups = new CheckBox("Forschungsprojekt Verbundförderung"); 
		supportScholarship = new CheckBox("Stipendium / Eigene Stelle"); 
		supportPrice = new CheckBox("Preis"); 
		supportTransferSpinOff = new CheckBox("Transfer / Ausgründung"); 
		
		left = new VBox(); 
		left.getChildren().addAll(mainSubject, hfachTP, hfachLS, hfachPSE, hfachSSH, 
				secondarySubjects, nfachTP, nfachLS, nfachPSE, nfachSSH, 
				targetGroup, youngResearchers, experiencedResearchers, 
				supportType, supportIndividualResearch, supportResearchGroups, supportScholarship, supportPrice, supportTransferSpinOff); 
		
		rubric = new Label("Rubrik"); 
		rubric.getStyleClass().add("section-title"); 
		international = new Label("Internationales Förderprogramm"); 
		international.getStyleClass().add("section-subtitle"); 
		national = new Label("Nationales Förderprogramm"); 
		national.getStyleClass().add("section-subtitle"); 
		priority = new Label("Priorität"); 
		priority.getStyleClass().add("section-title"); 
		
		toggleGroupRubric = new ToggleGroup(); 
		euHorizon = new RadioButton("EU-Programme: Horizont 2020"); 
		euHorizon.setToggleGroup(toggleGroupRubric);
		euHorizon.setUserData(Rubric.HORIZONT_2020);
		euOther = new RadioButton("Sonstige EU-Programme"); 
		euOther.setToggleGroup(toggleGroupRubric);
		euOther.setUserData(Rubric.OTHER_EU_PROGRAMM);
		intOther = new RadioButton("Weitere Förderinstitutionen (int)"); 
		intOther.setToggleGroup(toggleGroupRubric);
		intOther.setUserData(Rubric.OTHER_INSTITUTION_INT);
		lmu = new RadioButton("LMU"); 
		lmu.setToggleGroup(toggleGroupRubric);
		lmu.setUserData(Rubric.LMU);
		bmbf = new RadioButton("BMBF"); 
		bmbf.setToggleGroup(toggleGroupRubric);
		bmbf.setUserData(Rubric.BMBF);
		dfg = new RadioButton("DFG"); 
		dfg.setToggleGroup(toggleGroupRubric);
		dfg.setUserData(Rubric.DFG);
		natOther = new RadioButton("Weitere Förderinstitutionen (de)"); 
		natOther.setToggleGroup(toggleGroupRubric);
		natOther.setUserData(Rubric.OTHER_INSTITUTION);
		
		toggleGroupPrio = new ToggleGroup(); 
		prio1 = new RadioButton("1"); 
		prio1.setToggleGroup(toggleGroupPrio);
		prio1.setUserData(1);
		prio2 = new RadioButton("2"); 
		prio2.setToggleGroup(toggleGroupPrio);
		prio2.setUserData(2);
		prio3 = new RadioButton("3"); 
		prio3.setToggleGroup(toggleGroupPrio);
		prio3.setUserData(3);
		
		right = new VBox(); 
		right.getChildren().addAll(rubric, international, euHorizon, euOther, intOther, national, lmu, bmbf, dfg, natOther, 
				priority, prio1, prio2, prio3); 
		
		this.add(left, 0, 0);
		this.add(right, 1, 0);
		
	}
	
	public void setArticle(FitArticle article) {
		
		if (!(article.getMainSubject() == null)) {
			
			switch (article.getMainSubject()) {
			case LIFE_SCIENCES: 
				this.hfachLS.setSelected(true);
				break; 
			case PHYSICAL_SCIENCES_AND_ENGINEERING: 
				this.hfachPSE.setSelected(true);
				break; 
			case SOCIAL_SCIENCES_AND_HUMANITIES: 
				this.hfachSSH.setSelected(true);
				break; 
			case THEMENOFFENES_PROGRAMM: 
				this.hfachTP.setSelected(true);
			}
			
		}
		
		for (int i = 0; i < article.getMinorSubjects().size(); i++) {
			switch (article.getMinorSubjects().get(i)) {
			case LIFE_SCIENCES: 
				nfachLS.setSelected(true);
				break; 
			case PHYSICAL_SCIENCES_AND_ENGINEERING: 
				nfachPSE.setSelected(true);
				break; 
			case SOCIAL_SCIENCES_AND_HUMANITIES: 
				nfachSSH.setSelected(true);
				break; 
			case THEMENOFFENES_PROGRAMM: 
				nfachTP.setSelected(true);
			}
		}

		experiencedResearchers.setSelected(article.getTargetAudience().getTargetAudienceExperiencedResearchers());
		youngResearchers.setSelected(article.getTargetAudience().getTargetAudienceYoungResearchers());
		supportIndividualResearch.setSelected(article.getSupportType().getSupportIndividualResearch());
		supportPrice.setSelected(article.getSupportType().getSupportPrice());
		supportResearchGroups.setSelected(article.getSupportType().getSupportResearchGroups());
		supportScholarship.setSelected(article.getSupportType().getSupportScholarship());
		supportTransferSpinOff.setSelected(article.getSupportType().getSupportTransferSpinOff());

		if (article.getRubric() != null) {
			switch (article.getRubric()) {
			case BMBF: 
				bmbf.setSelected(true);
				break; 
			case BMWI: 
				natOther.setSelected(true);
				break; 
			case DFG: 
				dfg.setSelected(true);
				break; 
			case HORIZONT_2020: 
				euHorizon.setSelected(true);
				break; 
			case LMU: 
				lmu.setSelected(true);
				break; 
			case OTHER_EU_PROGRAMM: 
				euOther.setSelected(true);
				break; 
			case OTHER_INSTITUTION: 
				natOther.setSelected(true);
				break; 
			case OTHER_INSTITUTION_INT: 
				intOther.setSelected(true);
				break; 
			case VW_STIFTUNG: 
				natOther.setSelected(true);
				break; 
			}	
		}
		
		switch (article.getPriority()) {
		case 1: 
			prio1.setSelected(true);
			break; 
		case 2: 
			prio2.setSelected(true);
			break; 
		case 3: 
			prio3.setSelected(true);
			break; 
		}
		
	}
	
}
