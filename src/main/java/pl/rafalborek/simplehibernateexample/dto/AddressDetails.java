package pl.rafalborek.simplehibernateexample.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

@Entity
public class AddressDetails {

	public AddressDetails() {
		super();
		
	}
	
	public AddressDetails(String address) {
		super();
		this.address = address;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private long id;
	
	@Type(type="text")
	private String address;
	
	@ManyToOne
	@JoinColumn(name="USERACCOUNT_ID")
	private UserAccount user;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public UserAccount getUser() {
		return user;
	}

	public void setUser(UserAccount user) {
		this.user = user;
	}


}
