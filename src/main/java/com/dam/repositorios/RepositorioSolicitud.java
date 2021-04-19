package com.dam.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dam.modelos.Solicitud;

@Repository
public interface RepositorioSolicitud extends JpaRepository<Solicitud, Integer> {

}
