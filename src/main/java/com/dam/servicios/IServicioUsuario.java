package com.dam.servicios;

import java.util.List;

import com.dam.modelos.Usuario;

public interface IServicioUsuario {

	Usuario insert(Usuario usuario);

	boolean update(Usuario usuario);

	boolean delete(int id);

	List<Usuario> findAll();

	List<Usuario> findAllSorted();

	Usuario findByUserName(String username);

	Usuario findById(int id);

	int blockUser(int id);

	int activateUser(int id);

}
