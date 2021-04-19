package com.dam.servicios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.dam.modelos.Categoria;

public interface IServicioCategoria {
	
	Categoria insert(Categoria categoria);
	
	boolean update(Categoria categoria);
	
	boolean delete(int id);
	
	List<Categoria> findAll();
	
	List<Categoria> findAllSorted();
	
	Page<Categoria> findAllPaginated(Pageable page);
	
	Page<Categoria> findAllPaginatedAndSorted(Pageable page);
	
	Categoria findById(int id);
}
