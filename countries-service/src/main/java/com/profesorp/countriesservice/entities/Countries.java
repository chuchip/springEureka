package com.profesorp.countriesservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Countries {
	
	@Id
	private String country;
	
	@Column 
	@NotNull
	private String name;

	@Column 
	@NotNull
	private String currency;
	
	@Column 
	@NotNull
	private String currencysimbol;

	@Column 
	@NotNull
	private String language;
	
	@Column 
	@NotNull
	private String capital;
	
	@Transient
	int port;
}
