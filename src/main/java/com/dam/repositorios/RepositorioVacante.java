package com.dam.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dam.modelos.Vacante;

@Repository
public interface RepositorioVacante extends JpaRepository<Vacante, Integer>{
	
	List<Vacante> findByEstatus(String estatus);
	
	List<Vacante> findByDestacadaAndEstatusOrderById(int destacada, String estatus);
	
	List<Vacante> findBySalarioBetweenOrderBySalarioDesc(float s1, float s2);
	
	List<Vacante> findByEstatusIn(List<String> estatus);
}
