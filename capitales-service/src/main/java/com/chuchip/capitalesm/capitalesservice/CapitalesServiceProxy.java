package com.chuchip.capitalesm.capitalesservice;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// @FeignClient(name="paises-service")
@FeignClient(name="spring-zuul-api-gateway-server")
@RibbonClient(name="paises-service")
public interface CapitalesServiceProxy {
//	@GetMapping("/{pais}")
	@GetMapping("/paises-service/{pais}")
	public PaisesBean getPais(@PathVariable("pais") String pais);
}
