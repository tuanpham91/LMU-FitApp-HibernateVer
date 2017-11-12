package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import controller.Service.Filter;
import lombok.Getter;
import lombok.Setter;
import model.ContactPartner;
import model.FitArticle;
import model.Rubric;
import model.Subject;
import model.edit.Code;
import model.edit.ContactPerson;
import model.edit.FitArticleElement;
import model.edit.InfoListEntry;
import model.edit.TextEntry;
import model.edit.WebLinkEntry;
import view.Search;

@Setter @Getter
public class MsAccess {
	public List<FitArticle> articleList;
	private int maxIdsElem;
	private SessionFactory factory;
	private Session session ;
	private List<Integer> idListAll = new ArrayList<>();
	public MsAccess() {
		try  {
			// For the case of dynamical URL
			//factory = new Configuration().configure("hibernate.cfg.xml").setProperty("hibernate.connection.url",Controller.url).configure().buildSessionFactory();
			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
			session =factory.openSession();
		} catch (Throwable ex) {
 			System.err.println("Failed to create SessionFactory  object:" + ex);
 			throw new ExceptionInInitializerError(ex);
 		}
	}
	
	public List<ContactPartner> getContactPersons() {
		Transaction tx = null;
		session = factory.openSession();
		Criteria crit  = session.createCriteria(ContactPartner.class);
		return crit.list();
	}
	
	public FitArticle getArticleById(int id) {
		Transaction tx = null;
		session = factory.openSession();
		List<FitArticle> list = new ArrayList<FitArticle>();
		try {
			tx = session.beginTransaction();
			list = (List<FitArticle>) session.createQuery("SELECT i FROM FitArticle i WHERE i.id= " + id).list();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list.get(0);
	}
	public List<FitArticle> getExportArticles() {
		Transaction tx = null;
		session = factory.openSession();
		List<FitArticle> list = new ArrayList<FitArticle>();
		Criteria crit = session.createCriteria(FitArticle.class);
		try {
			ConstrainsEditor.filterConstraint(Filter.EXP_AKTUELL, crit);
			crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			list = crit.list();
		}
		catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return list;
	}
	
	public int saveNewArticle(FitArticle art) {
		this.setUpMaxIDs();
		int id= 0;
		for (FitArticleElement elem : art.getElements()) {
			elem.setFitArticle(art);
			if(elem.getId() != 0) {
				continue;
			}
			
			elem.setId(this.maxIdsElem);
			this.maxIdsElem++;
		}
		Transaction tx = null;
		session = factory.openSession();
		try { 
			tx = session.beginTransaction();
			id =  (int) session.save(art);
			session.flush();
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return id;
	}
	public void saveArticle(FitArticle art) {
		System.out.println("SAVE!!\n");
		this.setUpMaxIDs();
		for (FitArticleElement elem : art.getElements()) {
			elem.setFitArticle(art);
			if(elem.getId() != 0) {
				continue;
			}
			
			elem.setId(this.maxIdsElem);
			this.maxIdsElem++;
			System.out.println(this.maxIdsElem);
		}
		Transaction tx = null;
		session = factory.openSession();
		try { 
			tx = session.beginTransaction();
			session.merge(art);
			System.out.println(art.getId());
			session.flush();
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		} 
	}
	public int getMaxId() {
		Session newSession =factory.openSession();
		int maxId = (int) newSession.createQuery("Select max(id) from FitArticle").list().get(0);
		return maxId;
	}
	public void setUpMaxIDs() {
		Session newSession = factory.openSession();
		int maxIdCode = (int) newSession.createQuery("Select max(id) from Code").list().get(0)+1;
		int maxIdText = (int) newSession.createQuery("Select max(id) from TextEntry").list().get(0)+1;
		int maxIdTextInfo = (int) newSession.createQuery("Select max(id) from InfoListEntry").list().get(0)+1;
		int maxIdWebLink = (int) newSession.createQuery("Select max(id) from WebLinkEntry").list().get(0)+1;
		this.maxIdsElem=Math.max(maxIdCode, Math.max(maxIdText, Math.max(maxIdTextInfo, maxIdWebLink)));
		newSession.close();
	}


	public void deleteArticle(FitArticle ar) {
		Transaction tx = null;
		session = factory.openSession();
		try { 
			tx = session.beginTransaction();
			FitArticle toDel = session.load(FitArticle.class, ar.getId());
			session.delete(toDel);
			session.flush();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}
	public List<Integer> simpleSearchString(String search) {
		String lSearch = search.toLowerCase();
		List<Integer> list = new ArrayList<>();
		session = factory.openSession();
		Criteria crit = session.createCriteria(FitArticle.class,"fitArticle");
		if (lSearch.contains("nachwuchs")) {
			lSearch = lSearch.replaceFirst("nachwuchs","") ;
			ConstrainsEditor.targetAudienceTypeConstraint(crit, "targetAudienceYoungResearchers");
		}
		if (lSearch.contains("erfahrene")) {
			lSearch = lSearch.replaceFirst("erfahren","") ;
			ConstrainsEditor.targetAudienceTypeConstraint(crit, "targetAudienceExperiencedResearchers");
		}
		if (lSearch.contains("eigene stelle")){
			lSearch = lSearch.replaceFirst("eigene stelle", "");
			ConstrainsEditor.supportTypeConstraint(crit,"supportScholarship" , true);
		}
		if (lSearch.contains("forschungsprojekt individual")) {
			lSearch = lSearch.replaceFirst("forschungsprojekt individual", "");
			ConstrainsEditor.supportTypeConstraint(crit,"supportIndividualResearch", true);
		}
		if (lSearch.contains("forschungsprojekt verbund")) {
			lSearch = lSearch.replaceFirst("forschungsprojekt verbund","") ;
			ConstrainsEditor.supportTypeConstraint(crit, "supportResearchGroup", true);
		}
		if (lSearch.contains("preis")) {
			lSearch = lSearch.replaceFirst("preis","") ;
			ConstrainsEditor.supportTypeConstraint(crit, "supportPrice", true);
		}
		if (lSearch.contains("transfer")) {
			lSearch = lSearch.replaceFirst("transfer","") ;
			ConstrainsEditor.supportTypeConstraint(crit, "supportTransferSpinOff", true);
		}
		if (lSearch.contains("lmu")) {
			lSearch = lSearch.replaceFirst("lmu", "");
			ConstrainsEditor.rubricTypeConstraint(crit, Rubric.LMU);
		}
		if (lSearch.contains("bmbf")) {
			lSearch = lSearch.replaceFirst("bmbf", "");
			ConstrainsEditor.rubricTypeConstraint(crit, Rubric.BMBF);
		}
		if (lSearch.contains("dfg")) {
			lSearch = lSearch.replaceFirst("dfg", "");
			ConstrainsEditor.rubricTypeConstraint(crit, Rubric.DFG);
		}
		if (lSearch.contains("horizon")) {
			lSearch = lSearch.replaceFirst("horizon", "");
			ConstrainsEditor.rubricTypeConstraint(crit, Rubric.HORIZONT_2020);
		}
		if (lSearch.contains("dfg")) {
			lSearch = lSearch.replaceFirst("dfg", "");
			ConstrainsEditor.rubricTypeConstraint(crit, Rubric.DFG);
		}
		if (lSearch.contains("themenoffenes programm")) {
			lSearch = lSearch.replaceFirst("themenoffenes programm", "");
			ConstrainsEditor.subjectConstraint(crit, Subject.THEMENOFFENES_PROGRAMM );
		}
		if (lSearch.contains("engineering")) {
			lSearch = lSearch.replaceFirst("engineering", "");
			ConstrainsEditor.subjectConstraint(crit, Subject.PHYSICAL_SCIENCES_AND_ENGINEERING );
		}
		if (lSearch.contains("humnanities")) {
			lSearch = lSearch.replaceFirst("humanities", "");
			ConstrainsEditor.subjectConstraint(crit, Subject.SOCIAL_SCIENCES_AND_HUMANITIES );
		}
		if (lSearch.contains("life sciences")) {
			lSearch = lSearch.replaceFirst("life sciences", "");
			ConstrainsEditor.subjectConstraint(crit, Subject.LIFE_SCIENCES );
		}
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		Date date = null;
		try {
			date = df.parse(lSearch);
		} catch (ParseException e) {
		}
		if (date != null) {
			ConstrainsEditor.dateConstraint(crit, date);
			lSearch.replaceFirst(df.format(date), "");
		}
		String[] lsearchs = lSearch.split("\\s+");
		List <Integer> listIds = searchHeaders(lsearchs[0]);
		for (int k = 1 ; k<lsearchs.length;k++) {
			listIds.retainAll(searchHeaders(lsearchs[k]));
		}
		if (listIds.size() >0 ) {
			crit.add(Restrictions.in("id",listIds));
		}
		
		crit.setProjection(Projections.property("id"));
		list = crit.list();
		session.close();
		return list;
	}
	
	public List<Integer> searchHeaders(String search) {
		List<Integer> res = new ArrayList<Integer>();
		Session newSession = factory.openSession();
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM TextEntry WHERE content like :search").setParameter("search", "%"+search+"%").list());
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM Code WHERE code like :search").setParameter("search", "%"+search+"%").list());	
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM InfoListEntry WHERE value like :search").setParameter("search", "%"+search+"%").list());
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM WebLinkEntry WHERE url like :search").setParameter("search", "%"+search+"%").list());
		newSession.close();
		return res;
	}
	public List<Integer> searchUeberschrift(String search) {
		List<Integer> res = new ArrayList<Integer>();
		Session newSession = factory.openSession();
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM TextEntry WHERE content like :search AND format like '%HEADING%'").setParameter("search", "%"+search+"%").list());
		newSession.close();
		return res;
	}
	public List<Integer> searchText(String search) {
		List<Integer> res = new ArrayList<Integer>();
		Session newSession = factory.openSession();
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM TextEntry WHERE content like :search AND format like '%TEXT%'").setParameter("search", "%"+search+"%").list());
		newSession.close();
		return res;
	}
	public List<Integer> searchCode(String search) {
		List<Integer> res = new ArrayList<Integer>();
		Session newSession = factory.openSession();
		res.addAll(newSession.createQuery("SELECT DISTINCT fitArticle.id FROM Code WHERE code like :search").setParameter("search", "%"+search+"%").list());	
		newSession.close();
		return res;
	}
	public List<FitArticle> getArticleByFilter(List<Integer> listIds, int page) {
		List<FitArticle> resList = new ArrayList<>();
		if (listIds.size() ==0)  {
			System.out.println("MsAccess - Z 306 , kein Artikel");
			return resList;
		}
		Transaction tx = null;
		session = factory.openSession();
		Criteria crit = session.createCriteria(FitArticle.class);
		crit.add(Restrictions.in("id", listIds.subList(page*40-40, Math.min(listIds.size(), page*40))));
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return crit.list();
	}
	
	public List<Integer> getCountArticleByFilter(Filter filter, List<Ordering> orders) {
		
		List<Integer> list = new ArrayList<>();
		Transaction tx = null;
		session = factory.openSession();
		Criteria crit = session.createCriteria(FitArticle.class);
		ConstrainsEditor.filterConstraint(filter, crit);
		List<FitArticle> arList = new ArrayList<>();
		if (Service.INSTANCE.searchString) {
			List<Integer> list1 = this.simpleSearchString(Service.INSTANCE.currentSearchString);
			if (list1.size()>0) {
				crit.add(Restrictions.in("id", list1));		
			} else {
				return list;
			}
				
		}
		if (Service.INSTANCE.advanceSearch) {
			List<Integer> list2 = this.setAdvanceSearch();
			if (list2.size() >0 ) {
				crit.add(Restrictions.in("id", this.setAdvanceSearch()));
			} else {
				return list;
			}
		}

		List<FitArticle> listArticles = new ArrayList<>();
		crit.setProjection(Projections.projectionList().add(Projections.property("id"))
				.add(Projections.property("applicationDeadline"))
				.add(Projections.property("priority"))
				.add(Projections.property("rubric"))
				.add(Projections.property("mainSubject")));
		setOrdering(orders,crit);
		List<Object[]> listOb = crit.list();
		System.out.println(listOb.size());
		List<Integer> listIds = new ArrayList<>();
		for (Object[] ob : listOb) {
			listIds.add((Integer) ob[0]);
		}
		session.close();
		return listIds;
	}
	public static void setOrdering (List<Ordering> orders, Criteria crit) {
		if (orders.size() > 0 ) {
			System.out.println("Size of orders" + orders.size()+"\n");
			for (Ordering order : orders) {
				switch (order) {
				case  APPLICATION_DEADLINE :
					if (order.getAscending()) {
						crit.addOrder(Order.asc("applicationDeadline"));
					} else {
						crit.addOrder(Order.desc("applicationDeadline"));
					}
					break;
				case  PRIORITY :
					if (order.getAscending()) {
						crit.addOrder(Order.asc("priority"));
					} else {
						crit.addOrder(Order.desc("priority"));
					}
					break;
				case  RUBRIC :
					if (order.getAscending()) {
						crit.addOrder(Order.asc("rubric"));
					} else {
						crit.addOrder(Order.desc("rubric"));
					}
					break;
				case SUBJECT :
					if (order.getAscending()) {
						crit.addOrder(Order.asc("mainSubject"));
					} else {
						crit.addOrder(Order.desc("mainSubject"));
					}
					break;
				default:
					break;
				}
			}
		}
		else {
			crit.addOrder(Order.asc("priority"));		
			crit.addOrder(Order.asc("id"));

		}
	}
	
	public void createRecurrentArticles() {
		List<FitArticle> list = new ArrayList<FitArticle>();
		Transaction tx = null;
		session = factory.openSession();
		Criteria crit = session.createCriteria(FitArticle.class);	
		ConstrainsEditor.archivConstraint(crit, true);
		ConstrainsEditor.recurrentConstraint(crit, true);
		ConstrainsEditor.hasChildConstraint(crit, false);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		list = crit.list();
		session.close();
		for(FitArticle article : list) {
			FitArticle child = article.copy();
			child.makeChildCopy(article.getId());
			article.setChildArticlePresent(true);
			article.setChildArticle(this.saveNewArticle(child));
			this.saveArticle(article);
		}
	}
	
	public FitArticle loadByID(int id) {
		FitArticle res = (FitArticle) session.load(FitArticle.class, id);
		return res;
	}

	public List<Integer> setAdvanceSearch() {
		List<Integer> list = new ArrayList<>();
		session =factory.openSession();
		Criteria crit =  session.createCriteria(FitArticle.class);
		Search searchWindow = Controller.getGui().getSearchWindow();
		List<Subject> mainSubjects = new ArrayList<>();		
		if (!searchWindow.getAllSubjects().isSelected()) {
			if (searchWindow.getSubjectLs().isSelected()) {
				mainSubjects.add(Subject.LIFE_SCIENCES);
			}
			if (searchWindow.getSubjectPse().isSelected()) {
				mainSubjects.add(Subject.PHYSICAL_SCIENCES_AND_ENGINEERING);
			}
			if (searchWindow.getSubjectSsh().isSelected()) {
				mainSubjects.add(Subject.SOCIAL_SCIENCES_AND_HUMANITIES);
			}
			if (searchWindow.getSubjectTp().isSelected()) {
				mainSubjects.add(Subject.THEMENOFFENES_PROGRAMM);
			}
		}
		ConstrainsEditor.disjunktSubjectConstrain(crit, mainSubjects);
		
		if (!searchWindow.getAllTarget().isSelected()) {
			if (searchWindow.getTargetYoung().isSelected()) {
				ConstrainsEditor.targetAudienceTypeConstraint(crit, "targetAudienceYoungResearchers");
			} else {
				ConstrainsEditor.targetAudienceTypeConstraint(crit, "targetAudienceExperiencedResearchers");
			}
			
		}
		if (!searchWindow.getAllSupport().isSelected()) {
			ConstrainsEditor.supportTypeConstraint(crit, "supportIndividualResearch", searchWindow.getSupportIndividualResearch().isSelected());
			ConstrainsEditor.supportTypeConstraint(crit, "supportResearchGroups", searchWindow.getSupportResearchGroups().isSelected());
			ConstrainsEditor.supportTypeConstraint(crit, "supportScholarship",searchWindow.getSupportScholarship().isSelected());
			ConstrainsEditor.supportTypeConstraint(crit, "supportPrice",searchWindow.getSupportPrice().isSelected());
			ConstrainsEditor.supportTypeConstraint(crit, "supportTransferSpinOff",searchWindow.getSupportTransferSpinOff().isSelected());
		}
		if (!searchWindow.getAllRubric().isSelected()) {
			if (searchWindow.getRubricHorizon().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.HORIZONT_2020);
			}
			if (searchWindow.getRubricOtherEu().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.OTHER_EU_PROGRAMM);
			}
			if (searchWindow.getRubricOtherInt().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.OTHER_INSTITUTION_INT);
			}
			if (searchWindow.getRubricLmu().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.LMU);
			}		
			if (searchWindow.getRubricBmbf().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.BMBF);
			}
			if (searchWindow.getRubricDfg().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.DFG);
			}
			if (searchWindow.getRubricOtherNat().isSelected()) {
				ConstrainsEditor.rubricTypeConstraint(crit,Rubric.OTHER_INSTITUTION);
			}
		}
		if (!searchWindow.getAllStatus().isSelected()) {
			ConstrainsEditor.completedConstraint(crit, !searchWindow.getNotCompleted().isSelected());
			ConstrainsEditor.completedConstraint(crit,searchWindow.getCompleted().isSelected());
			ConstrainsEditor.correctedConstraint(crit,searchWindow.getCorrected().isSelected());
			ConstrainsEditor.exportReadyConstraint(crit,searchWindow.getExportReady().isSelected());
		}
		if (!searchWindow.getAllPrio().isSelected()) {
			if(searchWindow.getPrio1().isSelected()) {
				ConstrainsEditor.prioConstraint(crit, 1);
			}
			if(searchWindow.getPrio2().isSelected()) {
				ConstrainsEditor.prioConstraint(crit, 2);
			}
			if(searchWindow.getPrio3().isSelected()) {
				ConstrainsEditor.prioConstraint(crit, 3);
			}
		}
		if (!searchWindow.getAllEn().isSelected()) {
			if (searchWindow.getEnVersion().isSelected()) {
				if (searchWindow.getNoEnVersion().isSelected()) {
					// both check boxes selected -> no constraint
				} else {
					// en version
					ConstrainsEditor.enConstraint(crit, true);
				}		
			} else {
				// no en version
				ConstrainsEditor.enConstraint(crit, false);
			}
		}
		
		if (!searchWindow.getAllExport().isSelected()) {
			if (searchWindow.getExportFit().isSelected()) {
				ConstrainsEditor.exportFitConstraint(crit,true);
			}
			if (searchWindow.getExportWeb().isSelected()) {
				ConstrainsEditor.exportWebConstraint(crit,true);
			}
			
		}
		
		List<Integer> ids = new ArrayList<Integer>();
		
		if (!(searchWindow.getText1().getText().length() <= 0)) {
			String elem = searchWindow.getElement1().getValue();
			System.out.println(elem);
			String text = searchWindow.getText1().getText();
			if (elem.contains("Beliebig")) {
				ids.addAll(searchHeaders(text));
			}
			else if (elem.contains("Überschrift")) {
				ids.addAll(searchUeberschrift(text));
			}
			else if (elem.contains("Text")) {
				ids.addAll(searchText(text));
			}
			else if (elem.contains("Code")) {
				ids.addAll(searchCode(text));
			}
			else if (elem.contains("ID")) {
				list.clear();
				list.add(new Integer(Integer.parseInt(text)));
				return list; 
			}
		}
		
		if (!(searchWindow.getText2().getText().length() <= 0)) {
			String elem = searchWindow.getElement2().getValue();
			String text = searchWindow.getText2().getText();
			if (elem.contains("Beliebig")) {
				ids.addAll(searchHeaders(text));
			}
			else if (elem.contains("Überschrift")) {
				ids.addAll(searchUeberschrift(text));
			}
			else if (elem.contains("Text")) {
				ids.addAll(searchText(text));
			}
			else if (elem.contains("Code")) {
				ids.addAll(searchCode(text));
			}
			else if (elem.contains("ID")) {
				list.clear();
				list.add(new Integer(Integer.parseInt(text)));
				return list; 
			}
		}
		if (!(searchWindow.getText3().getText().length() <= 0)) {
			String elem = searchWindow.getElement3().getValue();
			String text = searchWindow.getText3().getText();
			if (elem.contains("Beliebig")) {
				ids.addAll(searchHeaders(text));
			}
			else if (elem.contains("Überschrift")) {
				ids.addAll(searchUeberschrift(text));
			}
			else if (elem.contains("Text")) {
				ids.addAll(searchText(text));
			}
			else if (elem.contains("Code")) {
				ids.addAll(searchCode(text));
			}
			else if (elem.contains("ID")) {
				list.clear();
				list.add(new Integer(Integer.parseInt(text)));
				return list; 
			}
		}
		if (!(searchWindow.getText4().getText().length() <= 0)) {
			String elem = searchWindow.getElement4().getValue();
			String text = searchWindow.getText4().getText();
			if (elem.contains("Beliebig")) {
				ids.addAll(searchHeaders(text));
			}
			else if (elem.contains("Überschrift")) {
				ids.addAll(searchUeberschrift(text));
			}
			else if (elem.contains("Text")) {
				ids.addAll(searchText(text));
			}
			else if (elem.contains("Code")) {
				ids.addAll(searchCode(text));
			}
			else if (elem.contains("ID")) {
				list.clear();
				list.add(new Integer(Integer.parseInt(text)));
				return list;  
			}
		}
		if (ids.size()>0) {
			crit.add(Restrictions.in("id", ids));
		}
		crit.setProjection(Projections.property("id"));
		
		list= crit.list();
		session.close();
		return list;
	}

}	

