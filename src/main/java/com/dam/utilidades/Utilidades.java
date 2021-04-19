package com.dam.utilidades;

import java.io.File;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class Utilidades {
	public static String guardarArchivo(MultipartFile file, String ruta) {
		// Obtener el nombre original del archivo
		String nombreOriginal = file.getOriginalFilename();
		// Evitar duplicados y espacios en blanco
		String nombreFinal = alfanumericoAleatorio(6).concat("-" + nombreOriginal.replace(" ", "-"));
		try {
			// Formar el nombre del archivo
			File archivoImagen = new File(ruta + nombreFinal);
			System.out.println("Archivo: " + archivoImagen.getAbsolutePath());
			// Guardar el archivo
			file.transferTo(archivoImagen);
			return nombreFinal;
		} catch (IOException e) {
			System.out.println("Error " + e.getMessage());
			return null;
		}
	}
	
	/**
	 * Método para generar una cadena aleatoria
	 * @param count número que pasamos como parámetro para la longitud de la cadena
	 * @return cadena aleatoria
	 */
	public static String alfanumericoAleatorio(int count) {
		String CARACTERES = "abcdefghijklmnopqrstuvwxyz0123456789";
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * CARACTERES.length());
			builder.append(CARACTERES.charAt(character));
		}
		return builder.toString();
	}
}
