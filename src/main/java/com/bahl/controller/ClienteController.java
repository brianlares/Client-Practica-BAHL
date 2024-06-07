package com.bahl.controller;

import com.bahl.model.dto.ClienteDto;
import com.bahl.model.entity.Cifrado;
import com.bahl.model.entity.Cliente;
import com.bahl.model.entity.Pokemon;
import com.bahl.model.payload.MensajeResponse;
import com.bahl.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ClienteController {

    @Autowired
    private IClienteService clienteService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("clientes")
    public ResponseEntity<?> showAll() {
        List<Cliente> getList = clienteService.listAlll();

        if (getList == null) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje("No hay registros")
                            .object(null)
                            .build()
                    , HttpStatus.OK);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mnesaje("")
                        .object(getList)
                        .build()
                , HttpStatus.OK);
    }

    @PostMapping("cliente")
    public ResponseEntity<?> create(@RequestBody ClienteDto clienteDto) {
        //Cliente clienteSave = null;
        try {
            Cliente clienteSave = clienteService.save(clienteDto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mnesaje("Guardado correctamente")
                    .object(ClienteDto.builder()
                            .idCliente(clienteSave.getIdCliente())
                            .nombre(clienteSave.getNombre())
                            .apellido(clienteSave.getApellido())
                            .correo(clienteSave.getCorreo())
                            .fechaRegistro(clienteSave.getFechaRegistro())
                            .build())
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PutMapping("cliente/{id}")
    public ResponseEntity<?> update(@RequestBody ClienteDto clienteDto, @PathVariable Integer id) {
        Cliente clienteUpdate = null;
        try {
            if (clienteService.existsById(id)) {
                clienteDto.setIdCliente(id);
                clienteUpdate = clienteService.save(clienteDto);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mnesaje("Guardado correctamente")
                        .object(ClienteDto.builder()
                                .idCliente(clienteUpdate.getIdCliente())
                                .nombre(clienteUpdate.getNombre())
                                .apellido(clienteUpdate.getApellido())
                                .correo(clienteUpdate.getCorreo())
                                .fechaRegistro(clienteUpdate.getFechaRegistro())
                                .build())
                        .build()
                        , HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mnesaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @DeleteMapping("cliente/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            Cliente clienteDelete = clienteService.findById(id);
            clienteService.delete(clienteDelete);
            return new ResponseEntity<>(clienteDelete, HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("cliente/{id}")
    public ResponseEntity<?> showById(@PathVariable Integer id) {
        Cliente cliente = clienteService.findById(id);

        if (cliente == null) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build()
                    , HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mnesaje("")
                        .object(ClienteDto.builder()
                                .idCliente(cliente.getIdCliente())
                                .nombre(cliente.getNombre())
                                .apellido(cliente.getApellido())
                                .correo(cliente.getCorreo())
                                .fechaRegistro(cliente.getFechaRegistro())
                                .build())
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("cliente/nombre/{nombre}")
    public ResponseEntity<?> showByName(@PathVariable String nombre) {
        List<Cliente> getList = clienteService.findByNombre(nombre);

        if (getList == null) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje("No hay registros")
                            .object(null)
                            .build()
                    , HttpStatus.OK);
        }

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mnesaje("")
                        .object(getList)
                        .build()
                , HttpStatus.OK);
    }

    @GetMapping("cliente/pokemon/{nombre}")
    public ResponseEntity<?> showPokemon(@PathVariable String nombre) {
        String url = "https://pokeapi.co/api/v2/pokemon/" + nombre;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Pokemon getList = restTemplate.exchange(url, HttpMethod.GET, entity, Pokemon.class).getBody();

        try
        {
            if (getList == null) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mnesaje("No hay registros")
                                .object(null)
                                .build()
                        , HttpStatus.OK);
            }

            return new ResponseEntity<>(getList, HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(e.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PostMapping("cliente/cifrar")
    public ResponseEntity<?> encriptar(@RequestBody Cifrado info) {

        try {
            String encriptado = clienteService.cifrar(info.getText());
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mnesaje("Proceso terminado")
                    .object(encriptado)
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @PostMapping("cliente/descifrar")
    public ResponseEntity<?> desencriptar(@RequestBody Cifrado info) {

        try {
            String descifrado = clienteService.descifrar(info.getText());
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mnesaje("Proceso terminado")
                    .object(descifrado)
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mnesaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    @GetMapping("authorized")
    public Map<String, String> authorized(@RequestParam String code)
    {
        return Collections.singletonMap("code", code);
    }
}