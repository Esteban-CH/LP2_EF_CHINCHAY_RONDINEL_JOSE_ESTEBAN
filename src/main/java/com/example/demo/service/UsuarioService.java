package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UsuarioEntity;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

public interface UsuarioService {

	void crearUsuario(UsuarioEntity usuarioEntity, org.springframework.ui.Model model, MultipartFile foto);
	boolean validarUsuario(UsuarioEntity usuarioEntity, HttpSession session);
	UsuarioEntity buscarUsuarioPorCorreo(String correo);
}
