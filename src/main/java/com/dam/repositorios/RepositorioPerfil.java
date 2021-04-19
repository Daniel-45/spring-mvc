package com.dam.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dam.modelos.Perfil;

@Repository
public interface RepositorioPerfil extends JpaRepository<Perfil, Integer> {

}
