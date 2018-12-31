package com.profesorp.capitalsservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
public class CapitalsServiceController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CapitalsServiceProxy proxy;
	
	
	
	@GetMapping("/{country}")
	public CapitalsBean getCountry(@PathVariable String country) {
		CapitalsBean response = proxy.getCountry(country);
		logger.info("CapitalesService -> {} ",response);
		return response;
	}
	@GetMapping("/template/{country}")
	public CapitalsBean getCountryUsingRestTemplate(@PathVariable String country) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("country", country);		
		
		ResponseEntity<CapitalsBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/{country}", 
				CapitalsBean.class, 
				uriVariables );
		
		CapitalsBean response = responseEntity.getBody();
		
		return response;
	}
	
	@Autowired
	private CapitalsServiceProxySimple simpleProxy;
	@GetMapping("/feign/{country}")
	public CapitalsBean getCountryUsingFeign(@PathVariable String country) {
		CapitalsBean response = simpleProxy.getCountry(country);		
		return response;
	}
	
	
}
