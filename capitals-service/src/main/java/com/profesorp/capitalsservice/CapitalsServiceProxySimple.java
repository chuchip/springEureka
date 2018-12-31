package com.profesorp.capitalsservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="simpleFeign",url="http://localhost:8000/")
public interface CapitalsServiceProxySimple {
	
	@GetMapping("/{country}")
	public CapitalsBean getPais(@PathVariable("country") String pais);
}
