package com.bahl.service;

import com.bahl.model.dto.ClienteDto;
import com.bahl.model.entity.Cliente;

import java.util.List;

public interface IClienteService {

    List<Cliente> listAlll();

    Cliente save(ClienteDto cliente);

    Cliente findById(Integer id);

    void delete(Cliente cliente);

    boolean existsById(Integer id);

    List<Cliente> findByNombre(String nombre);

    String cifrar(String clearText);

    String descifrar(String encrypted);
}
