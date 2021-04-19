package com.dam.modelos;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Daniel
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(length = 45, nullable = false)
	private String nombre;
	@Column(length = 100, nullable = false)
	private String email;
	@Column(nullable = false)
	private int estatus;
	@Column(length = 45, nullable = false)
	private String username;
	@Column(length = 100, nullable = false)
	private String password;
	@Column(name = "fecha_registro", nullable = true)
	private LocalDate fechaRegistro;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "perfiles_usuarios", 
	joinColumns = @JoinColumn(name = "idUsuario"),
	inverseJoinColumns = @JoinColumn(name = "idPerfil"))
	private Set<Perfil> perfiles;
	
	/*
	 * Método para añadir perfiles a la lista de perfiles
	 * los diferentes perfiles aue se van a asignar a un usuario
	 */
	public void incluirPerfil(Perfil perfil) {
		if (perfiles == null) {
			perfiles = new HashSet<Perfil>(3);
		}
		perfiles.add(perfil);
	}
}
