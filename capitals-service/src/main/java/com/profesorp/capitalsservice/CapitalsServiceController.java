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
	
	@Autowired
	private CapitalsServiceProxySimple simpleProxy;
	
	@GetMapping("/{pais}")
	public CapitalsBean getPais(@PathVariable String pais) {
		CapitalsBean response = proxy.getPais(pais);
		logger.info("CapitalesService -> {} ",response);
		return response;
	}
	@GetMapping("/template/{pais}")
	public CapitalsBean getTemplatePais(@PathVariable String pais) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("pais", pais);		
		
		ResponseEntity<CapitalsBean> responseEntity = new RestTemplate().getForEntity(
				"http://localhost:8000/{pais}", 
				CapitalsBean.class, 
				uriVariables );
		
		CapitalsBean response = responseEntity.getBody();
		
		return response;
	}
	
	@GetMapping("/feign/{pais}")
	public CapitalsBean getFeignPais(@PathVariable String pais) {
		CapitalsBean response = simpleProxy.getPais(pais);		
		return response;
	}
}
