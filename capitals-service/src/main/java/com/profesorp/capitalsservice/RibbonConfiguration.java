package com.profesorp.capitalsservice;

import org.springframework.context.annotation.Bean;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.NoOpPing;
import com.netflix.loadbalancer.WeightedResponseTimeRule;

public class RibbonConfiguration {
	
	 @Bean
	 public IPing ribbonPing() {
	        return new NoOpPing();
	 }
	 
	 @Bean	
	 public IRule ribbonRule() {
	        return new WeightedResponseTimeRule();
	 }
}
