package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity 
@Table (name = "fit_contact_partner")
public class ContactPartner{
	
	private final static String EMPTY = "Kein Kontaktpartner eingetragen";
	@Id
	@Column (name = "i_id")
	private int id;
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
	@Transient
	private String contactText;
	
	public ContactPartner() {
		super(); 
	}
	
	public ContactPartner(String firstName, String surname, String department, String phone, String email) {
		
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

	
}
