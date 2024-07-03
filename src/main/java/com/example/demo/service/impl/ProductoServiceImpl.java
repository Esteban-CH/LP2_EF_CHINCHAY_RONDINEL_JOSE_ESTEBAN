package com.example.demo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ProductoEntity;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoRepository productoRepository;
	
	@Override
	public List<ProductoEntity> listaproducto() {
		return productoRepository.findAll(); 
	}

	@Override
	public ProductoEntity buscarProductoPorId(Integer id) {
		return productoRepository.findById(id).get();
	}

	@Override
	public ProductoEntity crearProducto(ProductoEntity producto) {
		return productoRepository.save(producto);
	}

	@Override
    public ProductoEntity actualizarProducto(ProductoEntity producto) {
        ProductoEntity buscadoProducto = buscarProductoPorId(producto.getProductoId());
        if (buscadoProducto != null) {
            buscadoProducto.setNombreProducto(producto.getNombreProducto());
            buscadoProducto.setPrecio(producto.getPrecio());
            buscadoProducto.setStock(producto.getStock());
            buscadoProducto.setCategoria(producto.getCategoria());
            return productoRepository.save(buscadoProducto);
        }
        return null;
    }

	@Override
	public void eliminarProducto(Integer id) {
		productoRepository.deleteById(id);
		
	}

}
