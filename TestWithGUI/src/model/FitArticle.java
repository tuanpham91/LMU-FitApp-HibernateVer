package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;
import lombok.Setter;
import model.edit.TextEntry;
import model.edit.Code;
import model.edit.FitArticleElement;
import model.overview.CodeList;
import model.overview.HeaderList;
//http://stackoverflow.com/questions/916169/cannot-use-identity-column-key-generation-with-union-subclass-table-per-clas
@Entity
@Table(name = "T_FIT_ARTICLE")
@Setter @Getter
public class FitArticle {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "i_id")
	private int id;
	//@Column(name = "b_is_being_updated")
	@Transient
	private Boolean beingUpdated = false;
	
	@Transient 
	public boolean updatedByMe;
	//Subject-Code
	@Column(name = "i_minor_subject_areas_code")
	private Integer minorSubjectCode; //Tuan - added 3.3.2017
	
	@Column(name = "b_child_article_present")
	private boolean childArticlePresent; 
	
	@Enumerated(EnumType.STRING)
	@Column(name ="e_n_main_subject_area", nullable = true)
	private Subject mainSubject; 
	
	@Transient
	private List<Subject> minorSubjects=new ArrayList<Subject>(); 
	@Enumerated(EnumType.STRING)
	@Column(name ="e_n_rubric", nullable = true)
	private Rubric rubric; 
	
	@Column(name = "i_priority")
	private int priority; 
	
	@Transient
	private BooleanProperty en = new SimpleBooleanProperty(); 
	
	@Transient
	private HeaderList headers = new HeaderList(); 
	
	@Column(name = "d_application_deadline")
	private Date applicationDeadline; 
	
	
	@Transient
	private BooleanProperty applicationAnytime= new SimpleBooleanProperty(); 


	
	
	@Embedded
	private TargetAudience targetAudience = new TargetAudience() ; 
	
	@Embedded
	public SupportType supportType = new  SupportType(); 
	
	/**
	 * Export in the next FiT Issue, on the web or on the en web
	 */
	@Transient
	private BooleanProperty exportFit = new SimpleBooleanProperty(); // 37
	
	@Transient
	private BooleanProperty exportWeb = new SimpleBooleanProperty();// 38
	
	@Transient
	private BooleanProperty exportWebEn= new SimpleBooleanProperty(); // 39
	
	/**
	 * Article is completed, German version only.
	 * Should automatically change to false when the application deadline is over
	 * (Create new protocol entry in this case).
	 */
	@Transient
	private BooleanProperty completed =  new SimpleBooleanProperty(); // 40

	@Transient 
	private BooleanProperty completedEn = new SimpleBooleanProperty(); // 41
	
	@Transient 
	private boolean archiv;
	
	/**
	 * Should automatically change to false when the application deadline is over
	 */
	
	@Transient
	private BooleanProperty authorCorrected=  new SimpleBooleanProperty(); // 42
	
	@Transient
	private BooleanProperty authorCorrectedEn= new SimpleBooleanProperty(); // 43 
	
	/**
	 * Article is checked by an administrator and is ready to export.
	 */
	@Transient 
	private BooleanProperty exportReady = new SimpleBooleanProperty();// 44
	
	@Transient
	private BooleanProperty exportReadyEn= new SimpleBooleanProperty(); // 45
	
	/**
	 * If the call is recurrent (date has to be specified if yes).
	 * There is no validation right now if the recurrentCheckFrom is behind 
	 * the applicationDeadline (if one is set)
	 */
	
	@Transient
	private BooleanProperty recurrent = new SimpleBooleanProperty(); // 46
	
	
	@Column(name ="d_next_application_deadline") 
	private Date nextApplicationDeadline; // 47
	
	@Column(name ="d_recurrent_check_from")
	private Date recurrentCheckFrom;
	
	@Column(name ="i_parent_article")
	private Integer parentArticle;
	
	@Column(name ="b_parent_article_present")
	private boolean parentArticlePresent;
	
	@Column( nullable = true, name ="i_child_article")
	private Integer childArticle;
	
	@OneToMany (fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL,  mappedBy ="fitArticle")
	@OrderBy ("orderNr ASC")
	private List<FitArticleElement> elements = new ArrayList<>(); 
	
	@Transient
	private StringProperty comment = new SimpleStringProperty(); 
	
	public FitArticle() {
		super(); 
		priority = 3; 
		mainSubject = Subject.THEMENOFFENES_PROGRAMM; 
		rubric = Rubric.OTHER_INSTITUTION; 
		applicationDeadline = null; 
		
	}
		
	public List<Subject> getMinorSubjects() {
		if (this.minorSubjectCode != null ) {
			this.minorSubjects = Subject.getMinorSubjectAreas(this.minorSubjectCode);
		}
		return this.minorSubjects;
	}
	private void setMinorSubjects(int code) {
		this.minorSubjectCode = code;
		this.minorSubjects = Subject.getMinorSubjectAreas(this.minorSubjectCode);
	}
	public HeaderList getHeaderList() {
		clearDuplicates();
		for (FitArticleElement element : this.elements) {
			if (element instanceof TextEntry ) {
				TextEntry e = (TextEntry) element;
				switch (e.getFormat()) {
				case HEADING_3:
					this.headers.setHeading3(e.getContent());
					break;
				case HEADING_4:
					this.headers.setHeading4(e.getContent());
					break;
				case HEADING_5:
					this.headers.setHeading5(e.getContent());
					break;
				case HEADING_6:
					this.headers.addHeading6(e.getContent());
					break;
				case TEXT_BB:
					this.headers.setHeading7(e.getContent());
					break;
				}
				
			}
		}
		return this.headers;
	}
	public void clearDuplicates() {
		Set<FitArticleElement> depdupeElements = new LinkedHashSet<>(elements);
		elements.clear();
		elements.addAll(depdupeElements);
	}
	
	public void setLinkToChild(int childId) {
		setChildArticlePresent(true);
		setChildArticle(childId);
	}
	public void setMinorSubjects(List<Subject> list) {
		this.minorSubjects = list;
	}
	
	
	public void addElement(FitArticleElement newElement) {
		int i = 0; 
		while(i < elements.size() && elements.get(i).getOrderNr() < newElement.getOrderNr()) {
			i++; 
		}
		newElement.setFitArticle(this);
		elements.add(i, newElement); 
		
	}
	
	public void removeElement(int orderNr) {
		elements.get(orderNr).setFitArticle(null);
		elements.remove(orderNr); 
		
	}
	
	public void switchElements(int i, int j) {
		// indices acc to oderNr
		
		FitArticleElement oldI = elements.get(i); 
		FitArticleElement oldJ = elements.get(j); 
		int orderNrOldI = oldI.getOrderNr(); 
		oldI.setOrderNr(oldJ.getOrderNr());
		oldJ.setOrderNr(orderNrOldI);
		if (i < j) {
			elements.remove(i); 
			elements.remove(j-1); 
			elements.add(i, oldJ);
			elements.add(j, oldI); 
		} else {
			elements.remove(i); 
			elements.remove(j); 
			elements.add(j, oldI);
			elements.add(i, oldJ);
		}
	}
	public void moveAllUp(int index) {
		FitArticleElement e = elements.remove(index); 
		elements.add(0, e);
		for (int i = 0; i <= index; i++) {
			elements.get(i).setOrderNr(i + 1);
		}
	}
	public void moveAllDown(int index) {
		FitArticleElement e = elements.remove(index); 
		elements.add(e); 
		for (int i = index; i < elements.size(); i++) {
			elements.get(i).setOrderNr(i + 1);
		}
	}
	public void moveElement(int source, int target) {
		FitArticleElement e = elements.get(source); 
//		e.setOrderNr(target);
		elements.remove(source); 
		if (source < target) {
			for (int i = source; i < target - 1; i++) {
				elements.get(i).setOrderNr(i + 1);
			}
			e.setOrderNr(target);
			elements.add(target - 1, e);
		} else {
			e.setOrderNr(target + 1);
			elements.add(target, e); 
			for (int i = source; i > target; i--) {
				elements.get(i).setOrderNr(i + 1);
			}
		}
	}
	@Override
	public boolean equals(Object o) {
		
    	if (o == null) return false; 
    	if (o == this) return true; 
    	if (!(o instanceof FitArticle)) return false; 
    	FitArticle prev = (FitArticle)o; 
    	if (prev.getId() == this.id) return true; 
    	return false; 
		
	}
	public void copyFromAnArticle(FitArticle a) {
		
//		Calendar c = Calendar.getInstance(); 
//		c.setTime(a.getApplicationDeadline()); 
//		c.add(Calendar.MONTH, 8);
		
		if (a.getNextApplicationDeadline() != null) {
			Date newApplicationDeadline = new Date(a.getNextApplicationDeadline().getTime()); 
			this.setApplicationDeadline(newApplicationDeadline);
		}
		
//		this.setApplicationDeadline(c.getTime());
		this.setHeaders(a.getHeaders());
		
		this.setChildArticlePresent(false); 
		this.setParentArticlePresent(false); 
		this.setMainSubject(a.getMainSubject()); // TODO 
		List<Subject> minorSubjects = new ArrayList<>(a.getMinorSubjects()); 
		this.setMinorSubjects(minorSubjects); // TODO 
		this.setRubric(a.getRubric()); // TODO 
		this.setPriority(a.getPriority()); 
		this.setEn(a.getEn()); 
		 
		this.setTargetAudience(a.getTargetAudience().clone()); 
		this.setSupportType(a.getSupportType().clone());  
		this.setExportFit(a.getExportFit()); 
		this.setExportWeb(a.getExportWeb()); 
		this.setExportWebEn(a.getExportWebEn()); 
		this.setCompleted(a.getCompleted()); 
		this.setCompletedEn(a.getCompletedEn()); 
		this.setAuthorCorrected(a.getAuthorCorrected()); 
		this.setAuthorCorrectedEn(a.getAuthorCorrectedEn()); 
		this.setExportReady(a.getExportReady()); 
		this.setApplicationAnytime(a.isApplicationAnytime());
		this.setExportReadyEn(a.getExportReadyEn()); 
		
		this.setApplicationAnytime(a.isApplicationAnytime()); 
		this.setRecurrent(false);
		this.setNextApplicationDeadline(null);

		if (!a.getRecurrent()) {
			// original article not recurrent 
			this.setApplicationDeadline(null);
		}
		this.setRecurrentCheckFrom(null);
		
		this.setNextApplicationDeadline(null);
				
		elements.clear();
		Iterator<FitArticleElement> elementsIterator = a.elements.iterator(); 
		while (elementsIterator.hasNext()) {
			FitArticleElement currentElement = elementsIterator.next().clone(); 
			this.addElement(currentElement);
		}
		
		this.comment.set(this.comment.get()); 
	
	}
	

		
public FitArticle copy() {
		
		FitArticle copy = new FitArticle(); 	
		copy.setChildArticlePresent(this.isChildArticlePresent()); 
		copy.setParentArticlePresent(this.isParentArticlePresent());
		copy.setMainSubject(this.getMainSubject());  
		List<Subject> minorSubjects = new ArrayList<>(this.getMinorSubjects()); 
		copy.setMinorSubjects(minorSubjects);  
		copy.setRubric(this.getRubric()); 
		copy.setPriority(this.getPriority()); 
		copy.setEn(this.getEn()); 
		
		copy.setApplicationDeadline(this.getApplicationDeadline()); 
		copy.setApplicationAnytime(this.isApplicationAnytime()); 
		copy.setTargetAudience(this.getTargetAudience().clone()); 
		copy.setSupportType(this.getSupportType().clone());  
		copy.setExportFit(this.getExportFit()); 
		copy.setExportWeb(this.getExportWeb()); 
		copy.setExportWebEn(this.exportWebEn.get()); 
		copy.setCompleted(this.getCompleted()); 
		copy.setCompletedEn(this.getCompletedEn()); 
		copy.setAuthorCorrected(this.getAuthorCorrected()); 
		copy.setAuthorCorrectedEn(this.getAuthorCorrectedEn()); 
		copy.setExportReady(this.getExportReady()); 
		copy.setExportReadyEn(this.getExportReadyEn()); 
		copy.setApplicationAnytime(this.isApplicationAnytime());
		copy.setRecurrent(this.getRecurrent()); 
		if (this.getNextApplicationDeadline() != null) {
			Date newNextApplicationDeadline = new Date(this.getNextApplicationDeadline().getTime()); 
			copy.setNextApplicationDeadline(newNextApplicationDeadline); 			
		}
		if (this.getRecurrentCheckFrom() != null) {
			Date recurrentCheckFrom = new Date(this.getRecurrentCheckFrom().getTime()); 
			copy.setRecurrentCheckFrom(recurrentCheckFrom);
		}
		
		Iterator<FitArticleElement> elementsIterator = this.elements.iterator(); 
		while (elementsIterator.hasNext()) {
			FitArticleElement currentElement = elementsIterator.next().clone(); 
			copy.addElement(currentElement);
		}
		
		copy.comment.set(this.comment.get()); 
		copy.makeChildCopy(this.id);
		return copy; 
		
	}
		
	
	public void makeChildCopy(int parentId) {
		setEn(false);
		setExportFit(false);
		if (!isApplicationAnytime()) {
			setExportWeb(false);			
		}
		setExportWebEn(false);

		setCompleted(false);
		setCompletedEn(false);
		setAuthorCorrected(false);
		setAuthorCorrectedEn(false);
		setExportReady(false);
		setExportReadyEn(false);
	
		setParentArticlePresent(true);
		setParentArticle(parentId);
	
		if (isApplicationAnytime()) {
			this.setRecurrent(true); 	
			Calendar cal = Calendar.getInstance(); 
			cal.setTime(applicationDeadline);
			cal.add(Calendar.MONTH, 4);
			this.setNextApplicationDeadline(cal.getTime());
		} else {
		// new article is not recurrent, if old article is not running 
		// --> if old article is just recurrent (without being running), new article is not recurrent any more 
		this.setRecurrent(false);
		this.setNextApplicationDeadline(null);
	}

}
	public void setSupportType(SupportType clone) {
		this.supportType = clone;
	}
	public boolean isApplicationAnytime() {
		return this.applicationAnytime.get();
	}
	public Date getApplicationDeadline() {
		return this.applicationDeadline;
	}
	public void setTargetAudience(TargetAudience clone) {
		this.targetAudience = clone;
	}
	public void setRubric(Rubric rubric2) {
		this.rubric = rubric2;		
	}
	public void setApplicationDeadline(Date newApplicationDeadline) {
		this.applicationDeadline = newApplicationDeadline;
		
	}
	public boolean isArchived() {
		if(exportReady.get() == false ) {
			return false;
		} 
		else {
			Calendar today = Calendar.getInstance();
			today.set(Calendar.HOUR_OF_DAY, 0);
			today.set(Calendar.MINUTE, 0);
			today.set(Calendar.SECOND, 0);	
			today.add(Calendar.DATE, -1);
			
			Calendar deadline = Calendar.getInstance();
			deadline.setTime(applicationDeadline);
			return (today.after(deadline));
		}
	}
	
	@Column(name = "s_comment") 
	@Access(AccessType.PROPERTY)
	public String getComment(){
		return this.comment.get();
	}
	
	
	@Column(name = "b_completed")
	@Access(AccessType.PROPERTY)
	public boolean getCompleted() {
		return this.completed.get();
	}
	
	@Column(name = "b_completed_en")
	@Access(AccessType.PROPERTY)
	public boolean getCompletedEn() {
		return this.completedEn.get();
	}
	@Column(name = "b_english_version")
	@Access(AccessType.PROPERTY)
	public boolean getEn() {
		return this.en.get();
	}
	@Column(name = "b_export_fit")
	@Access(AccessType.PROPERTY)
	public boolean getExportFit() {
		return this.exportFit.get();
	}
	@Column(name = "b_export_ready")
	@Access(AccessType.PROPERTY)
	public boolean getExportReady() {
		return this.exportReady.get();
	}
	@Column(name = "b_export_ready_en")
	@Access(AccessType.PROPERTY)
	public boolean getExportReadyEn() {
		return this.exportReadyEn.get();
	}
	@Column(name = "b_export_web")
	@Access(AccessType.PROPERTY)
	public boolean getExportWeb() {
		return this.exportWeb.get();
	}
	@Column(name = "b_export_web_en")
	@Access(AccessType.PROPERTY)
	public boolean getExportWebEn() {
		return this.exportWebEn.get();
	}
	@Column(name = "b_recurrent")
	@Access(AccessType.PROPERTY)
	public boolean getRecurrent() {
		return this.recurrent.get();
	}
	@Column(name = "b_author_corrected")
	@Access(AccessType.PROPERTY)
	public boolean getAuthorCorrected() {
		return this.authorCorrected.get();
	}
	@Column(name = "b_author_corrected_en")
	@Access(AccessType.PROPERTY)
	public boolean getAuthorCorrectedEn() {
		return this.authorCorrectedEn.get();
	}
	
	@Column(name = "b_application_anytime")
	@Access(AccessType.PROPERTY)
	public boolean getApplicationAnytime() {
		return this.applicationAnytime.get();
	}
	public int getId() {
		return this.id;
	}
	public boolean isChildArticlePresent() {
		return this.childArticlePresent;
	}
	public void setComment(String a) {
		this.comment.set(a);
	}
	public void setCompleted(boolean a) {
		this.completed.set(a);
	}
	public void setCompletedEn(boolean a) {
		this.completedEn.set(a);
	}
	public void setApplicationAnytime(boolean a) {
		this.applicationAnytime.set(a);
	}
	public Subject getMainSubject() {
		return this.mainSubject;
	}
	public Rubric getRubric() {
		return this.rubric;
	}
	public List<FitArticleElement> getElements() {
		return this.elements;
	}
	public void setEn(boolean a) {
		this.en.set(a);
	}
	public void setExportFit(boolean a) {
		this.exportFit.set(a);
	}
	public void setExportReady(boolean a) {
		this.exportReady.set(a);
	}
	public void setExportReadyEn(boolean a) {
		this.exportReadyEn.set(a);
	}
	public void setExportWeb(boolean a) {
		this.exportWeb.set(a);
	}
	public void setExportWebEn(boolean a) {
		this.exportWebEn.set(a);
	}
	public void setRecurrent(boolean a) {
		this.recurrent.set(a);
	}
	public void setChildArticle(int a) {
		this.childArticle = a;
	}
	public boolean getBeingUpdated() {
		return this.beingUpdated;
	}
	public void setBeingUpdated(boolean a) {
		this.beingUpdated = a;
	}
	public int getChildArticle(){
		return this.childArticle;
	}
	public void setChildArticlePresent(boolean a) {
		this.childArticlePresent = a;
	}
	public void setMainSubject(Subject a) {
		this.mainSubject = a;
	}
	public int getPriority() {
		return this.priority;
	}
	public void setPriority(int a) {
		this.priority = a;
	}
	public void setAuthorCorrected(boolean a) {
		this.authorCorrected.set(a);
	}
	public void setAuthorCorrectedEn(boolean a) {
		this.authorCorrectedEn.set(a);
	}
	public BooleanProperty authorCorrected() {
		return this.authorCorrected;
	}
	public BooleanProperty completed() {
		return this.completed;
	}
	public BooleanProperty en() {
		return this.en;
	}
	public BooleanProperty exportFit() {
		return this.exportFit;
	}
	public BooleanProperty exportReady() {
		return this.exportReady;
	}
	public BooleanProperty exportWeb() {
		return this.exportWeb;
	}
	public BooleanProperty recurrent() {
		return this.recurrent;
	}
	public BooleanProperty applicationAnytime() {
		return this.applicationAnytime;
	}
	
	public StringProperty comment() {
		return this.comment;
	}
}

