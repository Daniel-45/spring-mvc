package com.dam.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dam.modelos.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Integer> {

	Usuario findByUsername(String username);

	List<Usuario> findByFechaRegistroNotNull();

	@Modifying
	@Query("UPDATE Usuario u SET u.estatus=0 WHERE u.id = :idUsuario")
	int lock(@Param("idUsuario") int idUsuario);

	@Modifying
	@Query("UPDATE Usuario u SET u.estatus=1 WHERE u.id = :idUsuario")
	int unlock(@Param("idUsuario") int idUsuario);
}
