package com.profesorp.countriesservice;

import org.springframework.data.jpa.repository.JpaRepository;

import com.profesorp.countriesservice.entities.Countries;

public interface CountriesRepository extends JpaRepository <Countries,String>{

}
