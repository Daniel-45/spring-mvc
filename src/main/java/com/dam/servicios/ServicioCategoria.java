package com.dam.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dam.modelos.Categoria;
import com.dam.repositorios.RepositorioCategoria;

@Service
public class ServicioCategoria implements IServicioCategoria {
	
	@Autowired
	private RepositorioCategoria daoCategoria;
	
	@Override
	public Categoria insert(Categoria categoria) {
		return daoCategoria.save(categoria);
	}

	@Override
	public boolean update(Categoria categoria) {
		boolean exito = false;
		if (daoCategoria.existsById(categoria.getId())) {
			daoCategoria.save(categoria);
			exito = true;
		}
		return exito;
	}
	
	@Override
	public boolean delete(int id) {
		boolean exito = false;
		if (daoCategoria.existsById(id)) {
			daoCategoria.deleteById(id);
			exito = true;
		}
		return exito;
	}

	@Override
	public List<Categoria> findAll() {
		return daoCategoria.findAll();
	}

	@Override
	public List<Categoria> findAllSorted() {
		return daoCategoria.findAll(Sort.by("nombre"));
	}

	@Override
	public Page<Categoria> findAllPaginated(Pageable page) {
		return daoCategoria.findAll(page);
	}

	@Override
	public Page<Categoria> findAllPaginatedAndSorted(Pageable page) {
		Page<Categoria> lista = daoCategoria.findAll(PageRequest.of(0, 5,Sort.by("nombre")));
		return lista;
	}
	
	@Override
	public Categoria findById(int id) {
		Optional<Categoria> categoria = daoCategoria.findById(id);
		if (categoria.isPresent()) {
			return categoria.get();
		}
		return null;
	}
}
