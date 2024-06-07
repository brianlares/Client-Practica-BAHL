package com.bahl.model.dao;

import com.bahl.model.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClienteDao extends CrudRepository<Cliente, Integer> {

    List<Cliente> findClienteByNombre(String nombre);
}
