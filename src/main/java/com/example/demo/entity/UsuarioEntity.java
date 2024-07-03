package com.example.demo.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "tb_usuario")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioEntity {

	@Id
	@Column(name = "usuario_id",nullable = false)
	private String correo;
	
	@Column(name = "nombre_usuario",nullable = false)
	private String nombre;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_nacimiento_usuario")
	private Date fchaNacimiento;
	
	@Column(name = "celular_usuario",nullable = false)
	private String celular;
	
	@Column(name = "clave_usuario",nullable = false)
	private String password;
	
	@Column(name = "imagen_usuario",nullable = false)
	private String urlImagen;
}
