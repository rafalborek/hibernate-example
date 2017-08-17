package pl.rafalborek.simplehibernateexample.dto;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class UserAccountExtended extends UserAccount {
	@Type(type="text")
	private String description;

	
	public UserAccountExtended() {
		super();
		
	}


	public UserAccountExtended(String userName, String description) {
		super();
		this.userName = userName;
		this.description = description;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
