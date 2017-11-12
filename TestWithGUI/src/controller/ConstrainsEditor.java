package controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import controller.Service.Filter;
import model.Rubric;
import model.Subject;
import model.SupportType;

public class ConstrainsEditor {
	public  ConstrainsEditor() {
		
	}
	public static void archivConstraint(Criteria crit, boolean isArchived) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		if (!isArchived) {
			crit.add(Restrictions.disjunction().add(Restrictions.eq("exportReady", false))
					.add(Restrictions.ge("applicationDeadline", today.getTime())));
		} else {
			crit.add(Restrictions.eq("exportReady",true))
			.add(Restrictions.lt("applicationDeadline", today.getTime()));
		}
	}
	
	public static void filterConstraint(Filter filter, Criteria crit) {
		switch(filter){
		case STANDARD:
			ConstrainsEditor.archivConstraint(crit, false);
			break;
		case MIT_ARCHIV:
			break;
		case NUR_ARCHIV:
			ConstrainsEditor.archivConstraint(crit, true);
			break;
		case NICHT_ABG_AKTUELL:
			ConstrainsEditor.completedConstraint(crit, false);
			ConstrainsEditor.currentConstraint(crit);
			break;
		case NICHT_ABG_ALLE:
			ConstrainsEditor.completedConstraint(crit, true);
			break;
		case NICHT_KORR_AKTUELL:
			ConstrainsEditor.completedConstraint(crit, true);
			ConstrainsEditor.correctedConstraint(crit, false);
			ConstrainsEditor.archivConstraint(crit,false);
			break;
		case NICHT_KORR_ALLE:
			ConstrainsEditor.completedConstraint(crit, true);
			ConstrainsEditor.correctedConstraint(crit, false);
			break;
		case NICHT_EXP_AKTUELL:
			ConstrainsEditor.correctedConstraint(crit, true);
			ConstrainsEditor.exportReadyConstraint(crit, false);
			ConstrainsEditor.currentConstraint(crit);
			break;
		case NICHT_EXP_ALLE:	
			ConstrainsEditor.correctedConstraint(crit, true);
			ConstrainsEditor.exportReadyConstraint(crit, false);
			break;
		case EXP_AKTUELL:
			ConstrainsEditor.exportReadyConstraint(crit, true);
			ConstrainsEditor.currentConstraint(crit);
			break;
		case EXP_ALLE:
			ConstrainsEditor.exportReadyConstraint(crit, true);
			ConstrainsEditor.archivConstraint(crit, false);
			break;
		}	
	}
	public static void disjunktSubjectConstrain(Criteria crit, List<Subject> subs) {
		Disjunction disjunct = Restrictions.disjunction();
		for (Subject sub : subs) {
			if (sub.getCode() ==1) {
				Integer codes[] = {1,3,5,7,9,11,13,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
			}
			if (sub.getCode() ==2) {
				Integer codes[] = {2,3,6,7,10,11,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
			}
			if (sub.getCode() ==4) {
				Integer codes[] = {4,5,6,7,12,13,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
			}
			if (sub.getCode() ==8) {
				Integer codes[] = {8,9,10,11,12,13,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
			}
		}
		crit.add(disjunct);		
	}
	public static void completedConstraint(Criteria crit, boolean completed) {
		crit.add(Restrictions.eq("completed",completed));
	}
	public static void recurrentConstraint(Criteria crit, boolean recurrent) {
		crit.add(Restrictions.eq("recurrent", true));
	}
	public static void hasChildConstraint(Criteria crit, boolean hasChild) {
		crit.add(Restrictions.eq("childArticlePresent", hasChild));
	}
	public static void currentConstraint(Criteria crit) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.SECOND, 0);
		crit.add(Restrictions.ge("applicationDeadline", today.getTime()));
		crit.add(Restrictions.disjunction()
				.add(Restrictions.disjunction()
						.add(Restrictions.eq("recurrent",false))
						.add(Restrictions.lt("recurrentCheckFrom",today.getTime())))
				.add(Restrictions.gtProperty("recurrentCheckFrom","applicationDeadline")));
	}
	public static void correctedConstraint(Criteria crit, boolean corrected) {
		crit.add(Restrictions.eq("authorCorrected", corrected));
	}
	public static void exportReadyConstraint(Criteria crit, boolean exportReady) {
		crit.add(Restrictions.eq("exportReady", exportReady));
	}
	public static void supportTypeConstraint(Criteria crit, String var, boolean value) {
		crit.add(Restrictions.eq("supportType."+var, value));
	}
	public static void rubricTypeConstraint(Criteria crit, Rubric value) {
		crit.add(Restrictions.eq("rubric", value));
	}
	public static void notRubricTypeConstraint(Criteria crit, Rubric value) {
		crit.add(Restrictions.ne("rubric", value));
	}
	public static void prioConstraint(Criteria crit, int a) {
		crit.add(Restrictions.eq("priority", a));
	}
	public static void notPrioConstraint(Criteria crit, int a) {
		crit.add(Restrictions.ne("priority", a));
	}
	public static void enConstraint(Criteria crit, boolean b) {
		crit.add(Restrictions.eq("en", b));
	}
	public static void targetAudienceTypeConstraint(Criteria crit, String value) {
		crit.add(Restrictions.eq("targetAudience."+value,true));
	}
	public static void exportFitConstraint(Criteria crit, boolean value) {
		crit.add(Restrictions.eq("exportFit", value));
	}
	public static void exportWebConstraint(Criteria crit, boolean value) {
		crit.add(Restrictions.eq("exportWeb", value));
	}
	public static void dateConstraint(Criteria crit, Date date) {
		crit.add(Restrictions.disjunction().add(Restrictions.eq("applicationDeadline", date)).add(Restrictions.eq("recurrentCheckFrom", date)));
	}
	public static void disjunktSupportConstrain(Criteria crit, List<SupportType> sups) {
		Disjunction disjunct = Restrictions.disjunction();
		for (SupportType sub : sups) {
			
		}
		crit.add(disjunct);		
	}
	
	public static void exportConstrain (Criteria crit) {
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		crit.add(Restrictions.eq("exportFit", true));
		crit.add(Restrictions.eq("exportReady", true));
		crit.add(Restrictions.ge("applicationDeadline", today.getTime()));
	}
	
	public static void mainSubjectConstraint(Criteria crit, Subject sub, boolean a) {
		if (a) {
			crit.add(Restrictions.eq("mainSubject", sub));
		} else {
			crit.add(Restrictions.ne("mainSubject", sub));
		}

	}
	public static void subjectConstrain(Criteria crit, List<Subject> subs) {
		Disjunction disjunct = Restrictions.disjunction();
		for (Subject sub : subs) {
			if (sub.getCode() ==1) {
				Integer codes[] = {1,3,5,7,9,11,13,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
				disjunct.add(Restrictions.eq("mainSubject", sub));
			}
			if (sub.getCode() ==2) {
				Integer codes[] = {2,3,6,7,10,11,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
				disjunct.add(Restrictions.eq("mainSubject", sub));
			}
			if (sub.getCode() ==4) {
				Integer codes[] = {4,5,6,7,12,13,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
				disjunct.add(Restrictions.eq("mainSubject", sub));
			}
			if (sub.getCode() ==8) {
				Integer codes[] = {8,9,10,11,12,13,14,15};
				disjunct.add(Restrictions.in("minorSubjectCode", codes));
				disjunct.add(Restrictions.eq("mainSubject", sub));
			}
		}
		crit.add(disjunct);		
	}
	public static void subjectConstraint(Criteria crit, Subject sub) {		
		if (sub.getCode() ==1) {
			Integer codes[] = {1,3,5,7,9,11,13,15};
			crit.add(Restrictions.disjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.in("minorSubjectCode", codes)));
		}
		if (sub.getCode() ==2) {
			Integer codes[] = {2,3,6,7,10,11,14,15};
			crit.add(Restrictions.disjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.in("minorSubjectCode", codes)));
		}
		if (sub.getCode() ==4) {
			Integer codes[] = {4,5,6,7,12,13,14,15};
			crit.add(Restrictions.disjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.in("minorSubjectCode", codes)));
		}
		if (sub.getCode() ==8) {
			Integer codes[] = {8,9,10,11,12,13,14,15};
			crit.add(Restrictions.disjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.in("minorSubjectCode", codes)));
		}
	}
	
	public static void notSubjectConstraint(Criteria crit, Subject sub) {		
		if (sub.getCode() ==1) {
			Integer codes[] = {1,3,5,7,9,11,13,15};
			crit.add(Restrictions.conjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.not(Restrictions.in("minorSubjectCode", codes))));
		}
		if (sub.getCode() ==2) {
			Integer codes[] = {2,3,6,7,10,11,14,15};
			crit.add(Restrictions.conjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.not(Restrictions.in("minorSubjectCode", codes))));
		}
		if (sub.getCode() ==4) {
			Integer codes[] = {4,5,6,7,12,13,14,15};
			crit.add(Restrictions.conjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.not(Restrictions.in("minorSubjectCode", codes))));
		}
		if (sub.getCode() ==8) {
			Integer codes[] = {8,9,10,11,12,13,14,15};
			crit.add(Restrictions.conjunction().add(Restrictions.eq("mainSubject", sub)).add(Restrictions.not(Restrictions.in("minorSubjectCode", codes))));
		}
	}
	
}
