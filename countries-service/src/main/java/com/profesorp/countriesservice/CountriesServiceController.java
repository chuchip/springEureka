package com.profesorp.countriesservice;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.profesorp.countriesservice.entities.Countries;

@RestController
public class CountriesServiceController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CountriesRepository countriesRepository;
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/{country}")
	public Countries getCountry(@PathVariable String country) {
		Countries  countryBean = countriesRepository.findById(country).orElseThrow(() -> new NotFoundException("Country: "+country+" not found"));
		countryBean.setPort( Integer.parseInt(environment.getProperty("local.server.port")) );
		logger.info("CountriesServiceControllerCountriesServiceController -> {} ",countryBean);
		return countryBean;		
	}		
	
}
