package com.profesorp.capitalsservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name="countries-service")
@RibbonClient(name="countries-service")
public interface CapitalsServiceProxy {
	@GetMapping("/{country}")
	public CapitalsBean getPais(@PathVariable("country") String pais);
}
