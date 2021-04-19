package com.dam.servicios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dam.modelos.Solicitud;

public interface IServicioSolicitud {
	
	Solicitud insert(Solicitud solicitud);
	
	boolean delete(int id);
	
	List<Solicitud> findAll();
	
	List<Solicitud> findAllSorted();
	
	Page<Solicitud> findAllPaginated(Pageable page);
	
	Page<Solicitud> findAllPaginatedAndSorted(Pageable page);
	
	Solicitud findById(int id);
}
