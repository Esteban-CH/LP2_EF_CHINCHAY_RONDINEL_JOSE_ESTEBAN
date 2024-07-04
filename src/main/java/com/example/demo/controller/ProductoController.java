package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.CategoriaEntity;
import com.example.demo.entity.ProductoEntity;
import com.example.demo.entity.UsuarioEntity;
import com.example.demo.service.CategoriaService;
import com.example.demo.service.PdfService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;
import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class ProductoController {

	@Autowired
    private PdfService pdfService;
	
	@Autowired
	private ProductoService productoService;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
    private CategoriaService categoriaService;
	
	@GetMapping("/listado_Productos")
    public String ListadoProductos(Model model, HttpSession session) {
        List<ProductoEntity> productos = productoService.listaproducto();
        model.addAttribute("productos", productos);

        if (session.getAttribute("usuario") != null) {
            String correo = session.getAttribute("usuario").toString();
            UsuarioEntity usuario = usuarioService.buscarUsuarioPorCorreo(correo);
            model.addAttribute("usuario", usuario);
        } else {
            return "redirect:/login";
        }
        return "Producto/Listado";
    }
	
	@GetMapping("/registrar_producto")
	public String mostrarFormularioRegistro(Model model, HttpSession session) {
	    ProductoEntity producto = new ProductoEntity();
	    model.addAttribute("producto", producto);

	    List<CategoriaEntity> categorias = categoriaService.listarCategoria();
	    model.addAttribute("categorias", categorias);

	    String correo = (String) session.getAttribute("usuario");
	    UsuarioEntity usuario = usuarioService.buscarUsuarioPorCorreo(correo);
	    model.addAttribute("usuario", usuario);

	    return "Producto/Registro";
	}
	
	@PostMapping("/registrar_producto")
	public String registrarProducto(@ModelAttribute("producto") ProductoEntity producto, Model model) {
	    productoService.crearProducto(producto);
	    return "redirect:/listado_Productos";
	}
	
	@GetMapping("/ver_producto/{id}")
    public String verProducto(@PathVariable("id") Integer id, Model model) {
        // Obtener el producto por su ID
        ProductoEntity producto = productoService.buscarProductoPorId(id);
        if (producto == null) {
            // Manejar el caso donde el producto no existe
            return "redirect:/listado_Productos";
        }

        model.addAttribute("producto", producto);

        return "Producto/Ver";
    }
	
	@GetMapping("/editar_producto/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Integer id, Model model) {
        // Obtener el producto por su ID
        ProductoEntity producto = productoService.buscarProductoPorId(id);
        if (producto == null) {
            // Manejar el caso donde el producto no existe
            return "redirect:/listado_Productos";
        }

        model.addAttribute("producto", producto);

        // Obtener lista de categorías para el formulario de edición
        List<CategoriaEntity> categorias = categoriaService.listarCategoria();
        model.addAttribute("categorias", categorias);

        return "Producto/Editar";
    }
	
	@PostMapping("/actualizar_producto")
    public String actualizarProducto(@ModelAttribute ProductoEntity producto) {
        productoService.actualizarProducto(producto);
        return "redirect:/listado_Productos";
    }

	@PostMapping("/eliminar_producto/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoService.eliminarProducto(id);
        return "redirect:/listado_Productos";
    }
	
	@GetMapping("/generar_pdf_productos")
    public ResponseEntity<InputStreamResource> generarPdfProductos(HttpSession session) throws IOException {
        // Simulamos obtener el usuario logueado (reemplaza con tu lógica real)
        UsuarioEntity usuarioLogueado = new UsuarioEntity();
        usuarioLogueado.setNombre("nombre_usuario"); // Ajusta según cómo guardas el nombre de usuario en sesión

        // Obtener lista de productos
        List<ProductoEntity> productos = productoService.listaproducto();

        // Preparar datos para el PDF
        Map<String, Object> datosPdf = new HashMap<>();
        datosPdf.put("productos", productos);

        // Agregar nombre de usuario
        String nombreUsuario = obtenerNombreUsuarioDesdeSesion(session, usuarioLogueado);
        datosPdf.put("nombre_usuario", nombreUsuario);

        // Generar el PDF
        ByteArrayInputStream pdfBytes = pdfService.generarPdfDeHtml("reporteProductos", datosPdf);

        // Configurar cabeceras HTTP para la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=productos.pdf");

        // Retornar la respuesta con el PDF generado
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdfBytes));
    }

	private String obtenerNombreUsuarioDesdeSesion(HttpSession session, UsuarioEntity usuario) {
        Object nombreUsuarioObj = session.getAttribute(usuario.getNombre());
        if (nombreUsuarioObj != null) {
            return nombreUsuarioObj.toString();
        } else {
            return "Usuario Ejemplo";
        }
    }




}
