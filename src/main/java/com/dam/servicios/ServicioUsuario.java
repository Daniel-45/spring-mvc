package com.dam.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dam.modelos.Usuario;
import com.dam.repositorios.RepositorioUsuario;

@Service
public class ServicioUsuario implements IServicioUsuario {

	@Autowired
	private RepositorioUsuario daoUsuario;

	@Override
	public Usuario insert(Usuario usuario) {
		return daoUsuario.save(usuario);
	}

	@Override
	public boolean update(Usuario usuario) {
		boolean exito = false;
		if (daoUsuario.existsById(usuario.getId())) {
			daoUsuario.save(usuario);
			exito = true;
		}
		return exito;
	}

	@Override
	public boolean delete(int id) {
		boolean exito = false;
		if (daoUsuario.existsById(id)) {
			daoUsuario.deleteById(id);
			exito = true;
		}
		return exito;
	}

	@Override
	public List<Usuario> findAll() {
		return daoUsuario.findAll();
	}

	@Override
	public List<Usuario> findAllSorted() {
		return daoUsuario.findAll(Sort.by("nombre"));
	}

	@Override
	public Usuario findByUserName(String username) {
		return daoUsuario.findByUsername(username);
	}

	@Override
	public Usuario findById(int id) {
		Optional<Usuario> usuario = daoUsuario.findById(id);
		if (usuario.isPresent()) {
			return usuario.get();
		}
		return null;
	}

	@Transactional
	@Override
	public int blockUser(int id) {
		int rows = daoUsuario.lock(id);
		return rows;
	}

	@Transactional
	@Override
	public int activateUser(int id) {
		int rows = daoUsuario.unlock(id);
		return rows;
	}

}
