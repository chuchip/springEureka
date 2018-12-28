package com.chuchip.capitalesm.capitalesservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class CapitalesServiceController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CapitalesServiceProxy proxy;
	
	@GetMapping("/{pais}")
	public PaisesBean getPais(@PathVariable String pais) {
		PaisesBean response = proxy.getPais(pais);
		logger.info("CapitalesService -> {} ",response);
		return response;
	}
}
