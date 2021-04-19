package com.dam.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dam.modelos.Vacante;
import com.dam.repositorios.RepositorioVacante;

@Service
public class ServicioVacante implements IServicioVacante {

	@Autowired
	private RepositorioVacante daoVacante;

	@Override
	public Vacante insert(Vacante vacante) {
		return daoVacante.save(vacante);
	}

	@Override
	public boolean update(Vacante vacante) {
		boolean exito = false;
		if (daoVacante.existsById(vacante.getId())) {
			daoVacante.save(vacante);
			exito = true;
		}
		return exito;
	}

	@Override
	public boolean delete(int id) {
		boolean exito = false;
		if (daoVacante.existsById(id)) {
			daoVacante.deleteById(id);
			exito = true;
		}
		return exito;
	}

	@Override
	public List<Vacante> findAll() {
		return daoVacante.findAll();
	}

	@Override
	public List<Vacante> findAllNoted() {
		return daoVacante.findByDestacadaAndEstatusOrderById(1, "Aprobada");
	}
	
	@Override
	public Page<Vacante> findAllPaginated(Pageable page) {
		return daoVacante.findAll(page);
	}
	
	@Override
	public List<Vacante> findByExample(Example<Vacante> example) {
		return daoVacante.findAll(example);
	}

	@Override
	public Vacante findById(int id) {
		Optional<Vacante> vacante = daoVacante.findById(id);

		if (vacante.isPresent()) {
			return vacante.get();
		}
		return null;
	}

}
