package com.profesorp.capitalsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CapitalsServiceController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CapitalsServiceProxy proxy;
	
	@GetMapping("/{pais}")
	public CapitalsBean getPais(@PathVariable String pais) {
		CapitalsBean response = proxy.getPais(pais);
		logger.info("CapitalesService -> {} ",response);
		return response;
	}
}
