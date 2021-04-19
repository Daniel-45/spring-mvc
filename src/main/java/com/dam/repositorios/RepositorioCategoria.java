package com.dam.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dam.modelos.Categoria;

@Repository
public interface RepositorioCategoria extends JpaRepository<Categoria, Integer> {

}
