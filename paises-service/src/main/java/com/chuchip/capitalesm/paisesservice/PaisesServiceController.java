package com.chuchip.capitalesm.paisesservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.chuchip.capitalesm.paisesservice.entities.Paises;

@RestController
public class PaisesServiceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private PaisesRepository paisesRepository;
	
	@Autowired
	private Environment environment;
	

	@GetMapping("/{pais}")
	public Paises getPais(@PathVariable String pais) {
		Paises  paisBean = paisesRepository.findById(pais).orElseThrow(() -> new NotFoundException("Pais: "+pais+" NO encontrado"));
		paisBean.setPort( Integer.parseInt(environment.getProperty("local.server.port")) );
		logger.info("PaisesServiceController -> {} ",paisBean);
		return paisBean;
		
	}	
	
	
}
