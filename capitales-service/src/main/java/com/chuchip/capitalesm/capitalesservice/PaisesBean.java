package com.chuchip.capitalesm.capitalesservice;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaisesBean {
	private String pais;
	
	private String nombre;

	private String moneda;
	private String simbolomoneda;
	private String idioma;
	private int port;	
}
