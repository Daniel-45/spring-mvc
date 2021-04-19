package com.dam.seguridad;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class DatabaseWebSecurity extends WebSecurityConfigurerAdapter {
	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("SELECT username, password, estatus FROM usuarios WHERE username=?")
				.authoritiesByUsernameQuery("SELECT u.username, p.perfil FROM perfiles_usuarios pu "
						+ "INNER JOIN usuarios u on u.id = pu.idUsuario "
						+ "INNER JOIN Perfiles p on p.id = pu.idPerfil " + "WHERE u.username = ?");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// Los recursos estáticos no requieren autenticación
				.antMatchers("/bootstrap/**", "/css/**", "/fonts/**", "/js/**", "/images/**", "/tinymce/**").permitAll()
				// Las vistas públicas no requieren autenticación
				.antMatchers("/", "/signup", "/search", "/bcrypt/**", "/vacantes/vacante/**", "/solicitudes/solicitud/**").permitAll()
				// Asignar permisos a URLs por Roles
				.antMatchers("/solicitudes/**").hasAnyAuthority("Supervisor", "Administrador")
				.antMatchers("/vacantes/**").hasAnyAuthority("Supervisor", "Administrador")
				.antMatchers("/categorias/**").hasAnyAuthority("Supervisor", "Administrador")
				.antMatchers("/usuarios/**").hasAnyAuthority("Administrador")
				// Todas las demás URLs de la Aplicación requieren autenticación
				.anyRequest().authenticated()
				// El formulario de Login no requiere autenticación
				// Para el formulario de login personalizado 'método loginPage'
				.and().formLogin().loginPage("/login").permitAll();
	}

	/* Encriptar contraseñas */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}