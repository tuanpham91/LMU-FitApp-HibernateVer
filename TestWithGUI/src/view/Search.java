package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import lombok.Getter;

public class Search extends Scene {
	
	private GridPane root; 
	
	private VBox left, middle, right; 
	
	private Label subject, targetGroup, supportType; 
	private Label rubric, international, national, priority; 
	private Label status, en, export; 
	
	@Getter
	private CheckBox allSubjects, allRubric, allPrio, allTarget, allSupport, allStatus, allEn, allExport; 
	@Getter
	private CheckBox subjectTp, subjectLs, subjectPse, subjectSsh; 
	@Getter
	private CheckBox rubricHorizon, rubricOtherEu, rubricOtherInt, rubricLmu, rubricBmbf, rubricDfg, rubricOtherNat; 
	@Getter
	private CheckBox prio1, prio2, prio3; 
	@Getter
	private CheckBox targetYoung, targetExperienced; 
	@Getter
	private CheckBox supportIndividualResearch, supportResearchGroups, supportScholarship, supportPrice, supportTransferSpinOff; 
	@Getter
	private CheckBox notCompleted, completed, corrected, exportReady, 
						enVersion, noEnVersion, exportFit, exportWeb; 
	
	@Getter
	private TextField text1, text2, text3, text4; 
	@Getter
	private ComboBox<String> element1, element2, element3, element4; 
	
	@Getter
	private Button ok, cancel, reset; 
	
	private boolean deselectAllSubjects = true; 
	private boolean deselectAllTarget = true; 
	private boolean deselectAllSupport = true; 
	private boolean deselectAllRubric = true; 
	private boolean deselectAllPrio = true; 
	private boolean deselectAllStatus = true; 
	private boolean deselectAllEn = true; 
	private boolean deselectAllExport = true; 
	
	private HashMap<CheckBox, Boolean> currentlyApplied = new HashMap<>(); 
	
	/**
	 * Interface class for search windows in overview
	 * 
	 */
	public Search() {
		
		super(new Pane(), 830, 720); 
		
		root = new GridPane(); 

		root.getStylesheets().add("/view/ArticleEditPage.css"); 
		root.getStyleClass().add("article-fixed-fields"); 
		
		subject = new Label("Fachbereich"); 
		subject.getStyleClass().add("section-title"); 
		targetGroup = new Label("Zielgruppe"); 
		targetGroup.getStyleClass().add("section-title"); 
		supportType = new Label("Förderart"); 
		supportType.getStyleClass().add("section-title"); 
		rubric = new Label("Rubrik"); 
		rubric.getStyleClass().add("section-title"); 
		international = new Label("Internationales Förderprogramm"); 
		international.getStyleClass().add("section-subtitle"); 
		national = new Label("Nationales Förderprogramm"); 
		national.getStyleClass().add("section-subtitle"); 
		priority = new Label("Priorität"); 
		priority.getStyleClass().add("section-title"); 
		status = new Label("Status"); 
		status.getStyleClass().add("section-title"); 
		en = new Label("Englische Version"); 
		en.getStyleClass().add("section-title"); 
		export = new Label("Export"); 
		export.getStyleClass().add("section-title"); 
		
		allSubjects = new CheckBox("Alle"); 
		subjectTp = new CheckBox("Themenoffenes Programm"); 
		subjectLs = new CheckBox("Life Sciences"); 
		subjectPse = new CheckBox("Physical Sciences & Engineering"); 
		subjectSsh = new CheckBox("Social Sciences & Humanities"); 
		
		allTarget = new CheckBox("Alle"); 
		targetYoung = new CheckBox("Wissenschaftlicher Nachwuchs"); 
		targetExperienced = new CheckBox("Erfahrene Wissenschaftler/innen"); 
		
		allSupport = new CheckBox("Alle"); 
		supportIndividualResearch = new CheckBox("Forschungsprojekt Individualförderung");
		supportResearchGroups = new CheckBox("Forschungsprojekt Verbundförderung"); 
		supportScholarship = new CheckBox("Stipendium / Eigene Stelle"); 
		supportPrice = new CheckBox("Preis"); 
		supportTransferSpinOff = new CheckBox("Transfer/Ausgründung"); 
		
		allRubric = new CheckBox("Alle"); 
		rubricHorizon = new CheckBox("EU-Programme: Horizont 2020"); 
		rubricOtherEu = new CheckBox("Sonstige EU-Programme"); 
		rubricOtherInt = new CheckBox("Weitere Förderinstitutionen (int)"); 
		rubricLmu = new CheckBox("LMU"); 
		rubricBmbf = new CheckBox("BMBF"); 
		rubricDfg = new CheckBox("DFG"); 
		rubricOtherNat = new CheckBox("Weitere Förderinstitutionen (de)"); 

		allPrio = new CheckBox("Alle"); 
		prio1 = new CheckBox("Priorität 1"); 
		prio2 = new CheckBox("Priorität 2"); 
		prio3 = new CheckBox("Priorität 3"); 
		
		allStatus = new CheckBox("Alle"); 
		notCompleted = new CheckBox("Nicht abgeschlossen"); 
		completed = new CheckBox("Abgeschlossen"); 
		corrected = new CheckBox("Korrigiert"); 
		exportReady = new CheckBox("Exportfertig"); 
		
		allEn = new CheckBox("Alle"); 
		enVersion = new CheckBox("Englische Version");
		noEnVersion = new CheckBox("Keine engl. Version"); 
		
		allExport = new CheckBox("Alle"); 
		exportFit = new CheckBox("Export FiT"); 
		exportWeb = new CheckBox("Export WWW"); 
		
		left = new VBox(); 
		left.getChildren().addAll(subject, allSubjects, subjectTp, subjectLs, subjectPse, subjectSsh, 
				targetGroup, allTarget, targetYoung, targetExperienced, 
				supportType, allSupport, supportIndividualResearch, supportResearchGroups, supportScholarship, supportPrice, supportTransferSpinOff); 
		Insets insets = new Insets(0, 0, 0, 10); 
		for (Node n : left.getChildren()) {
			if (n instanceof CheckBox) VBox.setMargin(n, insets);
		}
		VBox.setMargin(allSubjects, Insets.EMPTY);
		VBox.setMargin(allTarget, Insets.EMPTY);
		VBox.setMargin(allSupport, Insets.EMPTY);
		
		middle = new VBox(); 
		middle.getChildren().addAll(rubric, allRubric, international, rubricHorizon, rubricOtherEu, rubricOtherInt, national, rubricLmu, rubricBmbf, rubricDfg, rubricOtherNat, 
				priority, allPrio, prio1, prio2, prio3); 
		for (Node n : middle.getChildren()) {
			if (n instanceof CheckBox) VBox.setMargin(n, insets);
		}
		VBox.setMargin(allRubric, Insets.EMPTY);
		VBox.setMargin(allPrio, Insets.EMPTY);
		
		right = new VBox(); 
		right.getChildren().addAll(status, allStatus, notCompleted, completed, corrected, exportReady, 
				en, allEn, enVersion, noEnVersion, export, allExport, exportFit, exportWeb); 
		for (Node n : right.getChildren()) {
			if (n instanceof CheckBox) VBox.setMargin(n, insets);
		}
		VBox.setMargin(allStatus, Insets.EMPTY);
		VBox.setMargin(allEn, Insets.EMPTY);
		VBox.setMargin(allExport, Insets.EMPTY);
		
		text1 = new TextField(); 
		text1.setMaxWidth(1000);
		element1 = new ComboBox<>(); 
		element1.getItems().add("Beliebig"); 
		element1.getItems().add("Überschrift");
		element1.getItems().add("ID");
		element1.getItems().add("Text");
		element1.getItems().add("Code (Author)");
		element1.getItems().add("Code (Gesamt)");
		element1.setValue("Beliebig");
		element1.setMaxWidth(1000);;
		
		text2 = new TextField(); 
		text2.setMaxWidth(1000);
		element2 = new ComboBox<>(); 
		element2.getItems().add("Beliebig"); 
		element2.getItems().add("Überschrift");
		element2.getItems().add("ID");
		element2.getItems().add("Text");
		element2.getItems().add("Code (Author)");
		element2.getItems().add("Code (Gesamt)");
		element2.setValue("Beliebig");
		element2.setMaxWidth(1000);;
		
		text3 = new TextField(); 
		text3.setMaxWidth(1000);
		element3 = new ComboBox<>(); 
		element3.getItems().add("Beliebig"); 
		element3.getItems().add("Überschrift");
		element3.getItems().add("ID");
		element3.getItems().add("Text");
		element3.getItems().add("Code (Author)");
		element3.getItems().add("Code (Gesamt)");
		element3.setValue("Beliebig");
		element3.setMaxWidth(1000);;
		
		text4 = new TextField(); 
		text4.setMaxWidth(1000);
		element4 = new ComboBox<>(); 
		element4.getItems().add("Beliebig"); 
		element4.getItems().add("Überschrift");
		element4.getItems().add("ID");
		element4.getItems().add("Text");
		element4.getItems().add("Code (Author)");
		element4.getItems().add("Code (Gesamt)");
		element4.setValue("Beliebig");
		element4.setMaxWidth(1000);;
		
		ok = new Button("OK"); 
		ok.setMaxWidth(1000);
		GridPane.setMargin(ok, new Insets(30, 0, 0, 0));
		cancel = new Button("Abbrechen"); 
		cancel.setMaxWidth(1000);
		GridPane.setMargin(cancel, new Insets(30, 0, 0, 0));
		reset = new Button("Zurücksetzen"); 
		reset.setMaxWidth(1000);
//		GridPane.setMargin(reset, new Insets(30, 0, 0, 0));
		
		root.add(left, 0, 0);
		root.add(middle, 1, 0);
		root.add(right, 2, 0);
		
		root.add(text1, 0, 1, 2, 1);
		root.add(element1, 2, 1);
		root.add(text2, 0, 2, 2, 1);
		root.add(element2, 2, 2);
		root.add(text3, 0, 3, 2, 1);
		root.add(element3, 2, 3);
		root.add(text4, 0, 4, 2, 1);
		root.add(element4, 2, 4);
		
		root.add(ok, 1, 5);
		root.add(cancel, 2, 5);
		root.add(reset, 2, 6);
		
		allSubjects.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllSubjects) {
					// a subject box was deselected -> deselectAllSubjects temporary set false 
					deselectAllSubjects = true; 					
				} else {
					subjectLs.setSelected(newValue);
					subjectPse.setSelected(newValue);
					subjectSsh.setSelected(newValue);
					subjectTp.setSelected(newValue);	
					deselectAllSubjects = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerSubjects = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllSubjects = false; 
					allSubjects.setSelected(false);
				}
			}
		};
		subjectLs.selectedProperty().addListener(changeListenerSubjects);
		subjectPse.selectedProperty().addListener(changeListenerSubjects);
		subjectSsh.selectedProperty().addListener(changeListenerSubjects);
		subjectTp.selectedProperty().addListener(changeListenerSubjects);
		
		allTarget.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllTarget) {
					deselectAllTarget = true; 
				} else {
					targetYoung.setSelected(newValue);
					targetExperienced.setSelected(newValue);
					deselectAllTarget = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerTarget = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllTarget = false; 
					allTarget.setSelected(false);
				}
			}
		};
		targetYoung.selectedProperty().addListener(changeListenerTarget);
		targetExperienced.selectedProperty().addListener(changeListenerTarget);
		
		allSupport.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllSupport) {
					deselectAllSupport = true; 
				} else {
					supportIndividualResearch.setSelected(newValue);
					supportResearchGroups.setSelected(newValue);
					supportScholarship.setSelected(newValue);
					supportPrice.setSelected(newValue);
					supportTransferSpinOff.setSelected(newValue);
					deselectAllSupport = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerSupport = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllSupport = false; 
					allSupport.setSelected(false);
				}
			}
		};
		supportIndividualResearch.selectedProperty().addListener(changeListenerSupport);
		supportResearchGroups.selectedProperty().addListener(changeListenerSupport);
		supportScholarship.selectedProperty().addListener(changeListenerSupport);
		supportPrice.selectedProperty().addListener(changeListenerSupport);
		supportTransferSpinOff.selectedProperty().addListener(changeListenerSupport);
		
		allRubric.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllRubric) {
					deselectAllRubric = true; 
				} else {
					rubricHorizon.setSelected(newValue);
					rubricOtherEu.setSelected(newValue);
					rubricOtherInt.setSelected(newValue);
					rubricLmu.setSelected(newValue);
					rubricBmbf.setSelected(newValue);
					rubricDfg.setSelected(newValue);
					rubricOtherNat.setSelected(newValue);
					deselectAllRubric = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerRubric = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllRubric = false; 
					allRubric.setSelected(false);
				}
			}
		};
		rubricHorizon.selectedProperty().addListener(changeListenerRubric);
		rubricOtherEu.selectedProperty().addListener(changeListenerRubric);
		rubricOtherInt.selectedProperty().addListener(changeListenerRubric);
		rubricLmu.selectedProperty().addListener(changeListenerRubric);
		rubricBmbf.selectedProperty().addListener(changeListenerRubric);
		rubricDfg.selectedProperty().addListener(changeListenerRubric);
		rubricOtherNat.selectedProperty().addListener(changeListenerRubric);
		
		allPrio.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllPrio) {
					deselectAllPrio = true; 
				} else {
					prio1.setSelected(newValue);
					prio2.setSelected(newValue);
					prio3.setSelected(newValue);
					deselectAllPrio = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerPrio = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllPrio = false; 
					allPrio.setSelected(false);
				}
			}
		};
		prio1.selectedProperty().addListener(changeListenerPrio);
		prio2.selectedProperty().addListener(changeListenerPrio);
		prio3.selectedProperty().addListener(changeListenerPrio);
		
		allStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllStatus) {
					deselectAllStatus = true; 
				} else {
					notCompleted.setSelected(newValue);
					completed.setSelected(newValue);
					corrected.setSelected(newValue);
					exportReady.setSelected(newValue);
					deselectAllStatus = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerStatus = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllStatus = false; 
					allStatus.setSelected(false);
				}
			}
		};
		notCompleted.selectedProperty().addListener(changeListenerStatus);
		completed.selectedProperty().addListener(changeListenerStatus);
		completed.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					corrected.setSelected(false);
				}
			}
		});
		corrected.selectedProperty().addListener(changeListenerStatus); 
		corrected.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					completed.setSelected(true);
				} else {
					exportReady.setSelected(false);
				}
			}
		});
		exportReady.selectedProperty().addListener(changeListenerStatus);
		exportReady.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					corrected.setSelected(true);
				}
			}
		});
		
		allEn.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllEn) {
					deselectAllEn = true; 
				} else {
					enVersion.setSelected(newValue);
					noEnVersion.setSelected(newValue);					
					deselectAllEn = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerEn = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllEn = false; 
					allEn.setSelected(false);
				}
			}
		};
		enVersion.selectedProperty().addListener(changeListenerEn);
		noEnVersion.selectedProperty().addListener(changeListenerEn);
		
		allExport.selectedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue && !deselectAllExport) {
					deselectAllExport = true; 
				} else {
					exportFit.setSelected(newValue);
					exportWeb.setSelected(newValue);
					deselectAllExport = true; 
				}
			}
		});
		
		ChangeListener<Boolean> changeListenerExport = new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					deselectAllExport = false; 
					allExport.setSelected(false);
				}
			}
		};
		exportFit.selectedProperty().addListener(changeListenerExport);
		exportWeb.selectedProperty().addListener(changeListenerExport);
		
		allSubjects.setSelected(true);
		allTarget.setSelected(true);
		allSupport.setSelected(true);
		allRubric.setSelected(true);
		allPrio.setSelected(true);
		allStatus.setSelected(true);
		allEn.setSelected(true);
		allExport.setSelected(true);
		
		List<Node> checkBoxes = new ArrayList<>(); 
		checkBoxes.addAll(left.getChildren()); 
		checkBoxes.addAll(middle.getChildren()); 
		checkBoxes.addAll(right.getChildren()); 
		Iterator<Node> childrenIterator = checkBoxes.iterator(); 
		while (childrenIterator.hasNext()) {
			Node n = childrenIterator.next(); 
			if (n instanceof CheckBox) {
				currentlyApplied.put((CheckBox)n, true); 
			}
		}
		
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(10, 10, 10, 10));
		
		this.setRoot(root);

	}
	/** 
	 * Reset all the attributes ofs Search window
	 */
	public void reset() {
		allSubjects.setSelected(true);
		allTarget.setSelected(true);
		allSupport.setSelected(true);
		allRubric.setSelected(true);
		allPrio.setSelected(true);
		allStatus.setSelected(true);
		allEn.setSelected(true);
		allExport.setSelected(true);
		text1.setText("");
		text2.setText("");
		text3.setText("");
		text4.setText("");
		element1.setValue("Beliebig");
		element2.setValue("Beliebig");
		element3.setValue("Beliebig");
		element4.setValue("Beliebig");
	}
	
	public boolean isActive() {
		for (Node n : this.root.getChildren()) {
			
			if (n instanceof VBox) {
				for (Node innerNode : ((VBox) n).getChildren()) {
					if (innerNode instanceof CheckBox) {
						if (!((CheckBox) innerNode).isSelected()) {
							return true; 
						}
					} 
				}
			} else if (n instanceof TextField) {
				if (((TextField) n).getText().length() > 0) {
					return true; 
				}
			} else if (n instanceof ComboBox<?>) {
				if (!((ComboBox<?>) n).getValue().equals("Beliebig")) {
					return true; 
				}
			}
		}
		return false; 
	}
	
	public void resetToCurrentlyApplied() {
		// iterate hashmap -> each entry: set selected value of the key to the value (of hash map entry) 
//		Iterator<Entry<CheckBox, Boolean>> entrySetIt = currentlyApplied.entrySet().iterator(); 
		Iterator<CheckBox> iteratorCheckBoxes = currentlyApplied.keySet().iterator(); 
		while (iteratorCheckBoxes.hasNext()) {
//			System.out.println("resetting");
			CheckBox cb = iteratorCheckBoxes.next(); 
			cb.setSelected(currentlyApplied.get(cb));
//			Entry<CheckBox, Boolean> entry = entrySetIt.next(); 
//			entry.getKey().setSelected(entry.getValue());
		}
	}
	
	public void setCurrentlyApplied() {
		// iterate hashmap -> each entry: set value to the selected value of the key (of hash map entry) 
		Iterator<Entry<CheckBox, Boolean>> entrySetIt = currentlyApplied.entrySet().iterator(); 
		while (entrySetIt.hasNext()) {
			Entry<CheckBox, Boolean> entry = entrySetIt.next(); 
			currentlyApplied.put(entry.getKey(), entry.getKey().isSelected()); 
		}
	}

}
