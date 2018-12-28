package com.chuchip.capitalesm.paisesservice.entities;

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
public class Paises {
	@Id
	private String pais;
	
	@Column 
	@NotNull
	private String nombre;

	@Column 
	@NotNull
	private String moneda;
	
	@Column 
	@NotNull
	private String simbolomoneda;

	@Column 
	@NotNull
	private String idioma;
	
	@Transient
	int port;
}
