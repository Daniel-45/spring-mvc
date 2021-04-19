package com.dam.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dam.modelos.Solicitud;
import com.dam.repositorios.RepositorioSolicitud;

@Service
public class ServicioSolicitud implements IServicioSolicitud {

	@Autowired
	private RepositorioSolicitud daoSolicitud;

	@Override
	public Solicitud insert(Solicitud solicitud) {
		return daoSolicitud.save(solicitud);
	}

	@Override
	public boolean delete(int id) {
		boolean exito = false;

		if (daoSolicitud.existsById(id)) {
			daoSolicitud.deleteById(id);
			exito = true;
		}
		return exito;
	}

	@Override
	public List<Solicitud> findAll() {
		return daoSolicitud.findAll();
	}

	@Override
	public List<Solicitud> findAllSorted() {
		return daoSolicitud.findAll(Sort.by("fecha"));
	}

	@Override
	public Page<Solicitud> findAllPaginated(Pageable page) {
		return daoSolicitud.findAll(page);
	}

	@Override
	public Page<Solicitud> findAllPaginatedAndSorted(Pageable page) {
		Page<Solicitud> lista = daoSolicitud.findAll(PageRequest.of(0, 6,Sort.by("fecha")));
		return lista;
	}

	@Override
	public Solicitud findById(int id) {
		Optional<Solicitud> solicitud = daoSolicitud.findById(id);

		if (solicitud.isPresent()) {
			return solicitud.get();
		}
		return null;
	}
	
	
}
