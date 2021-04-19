package com.dam.servicios;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dam.modelos.Vacante;

public interface IServicioVacante {
	
	Vacante insert(Vacante vacante);
	
	boolean update(Vacante vacante);
	
	boolean delete(int id);
	
	List<Vacante> findAll();
	
	List<Vacante> findAllNoted();
	
	Page<Vacante> findAllPaginated(Pageable page);
	
	List<Vacante> findByExample(Example<Vacante> example);
	
	Vacante findById(int id);
	
}
