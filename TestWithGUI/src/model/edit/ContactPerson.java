package model.edit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity 
@Table (name = "fit_contact_partner")
public class ContactPerson extends FitArticleElement {
	
	private final static String EMPTY = "Kein Kontaktpartner eingetragen";
	
	@Column (name = "s_first_name")
	private String firstName;
	@Column (name = "s_surname")
	private String surname;
	@Column (name = "s_department")
	private String department;
	@Column (name = "s_phone")
	private String phone;
	@Column (name = "s_email")
	private String email;
	
	private String contactText;
	
	public ContactPerson() {
		super(); 
	}
	
	public ContactPerson(int orderNr, String firstName, String surname, String department, String phone, String email) {
		
		this.firstName = firstName; 
		this.surname = surname; 
		this.department = department; 
		this.phone = phone; 
		this.email = email; 
		
	}
	
	public String getContactText() {
		if(contactText == null) {
			contactText = firstName + " " + surname;
			contactText+= email;
			contactText+= phone;
		}
		return contactText;
	}
	
	public String getElementPreview() 
	{
		if(surname != null && firstName != null) {
			return firstName + " " + surname;
		}
		else {
			return EMPTY;
		}
	}

	@Override
	public FitArticleElement clone() {
		ContactPerson res = new ContactPerson(1,firstName, surname, department, phone, email); 
		return res;
	}
	
	public boolean equalContent(FitArticleElement element) {
		if (!(element instanceof ContactPerson)) return false; 
		if (((ContactPerson)element).getId() != this.id || 
				 
				((ContactPerson)element).getId()!= this.id) {
			return false; 
		}
		return true; 
	}

}
