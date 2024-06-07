package com.bahl.controller;

import com.bahl.model.dto.ClienteDto;
import com.bahl.model.entity.Cifrado;
import com.bahl.model.entity.Cliente;
import com.bahl.model.entity.Pokemon;
import com.bahl.service.IClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IClienteService clienteService;

    @MockBean
    RestTemplate restTemplate;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = Cliente.builder()
                .idCliente(1)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();
    }

    @Test
    public void showAll() throws Exception
    {
        Cliente ge1 = new Cliente(1,"Rachel", "Green", "a@b.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        Cliente ge2 = new Cliente(2,"Monica", "Geller", "c@d.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        Cliente ge3 = new Cliente(3,"Phoebe", "red", "e@f.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        List<Cliente> clienteList = List.of(ge1, ge2, ge3);


        Mockito.when(clienteService.listAlll()).thenReturn(clienteList);
        mockMvc.perform(get("/api/v1/clientes").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void create() throws Exception{
        ClienteDto postCliente = ClienteDto.builder()
                .idCliente(0)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();

        Cliente cl = new Cliente(1,"Brian", "Lares", "zizou@hotmail.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));

        Mockito.when(clienteService.save(postCliente)).thenReturn(cl);
        mockMvc.perform(post("/api/v1/cliente").contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        " \"idCliente\":1,\n" +
                        " \"nombre\": \"Brian\", \n"+
                        " \"apellido\": \"Lares\", \n"+
                        " \"correo\": \"zizou@hotmail.com\", \n"+
                        " \"fechaRegistro\": \"2024-05-29T21:53:26.303Z\" \n"+
                        "}"))
                .andExpect(status().isOk());
                //.andDo(print());
    }

    @Test
    public void update() throws Exception{
        ClienteDto updateCliente = ClienteDto.builder()
                .idCliente(1)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();

        Integer idCliente = updateCliente.getIdCliente();

        Mockito.when(clienteService.existsById(updateCliente.getIdCliente())).thenReturn(true);
        Mockito.when(clienteService.save(updateCliente)).thenReturn(cliente);
        mockMvc.perform(put("/api/v1/cliente/{id}",idCliente).contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"idCliente\":1,\n" +
                                " \"nombre\": \"Brian\", \n"+
                                " \"apellido\": \"Lares\", \n"+
                                " \"correo\": \"zizou@hotmail.com\", \n"+
                                " \"fechaRegistro\": \"2024-05-29T21:53:26.303Z\" \n"+
                                "}"))
                .andExpect(status().isOk());
        //.andDo(print());
    }

    @Test
    public void eliminar() throws Exception{
        ClienteDto deleteCliente = ClienteDto.builder()
                .idCliente(1)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();

        Integer idCliente = deleteCliente.getIdCliente();

        Mockito.when(clienteService.findById(deleteCliente.getIdCliente())).thenReturn(cliente);
        Mockito.doNothing().when(clienteService).delete(cliente);
        mockMvc.perform(delete("/api/v1/cliente/{id}",idCliente).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void showById() throws Exception{
        ClienteDto showCliente = ClienteDto.builder()
                .idCliente(1)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();

        Integer idCliente = showCliente.getIdCliente();

        Mockito.when(clienteService.findById(showCliente.getIdCliente())).thenReturn(cliente);
        mockMvc.perform(get("/api/v1/cliente/{id}",idCliente).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void showByName() throws Exception{
        ClienteDto nameCliente = ClienteDto.builder()
                .idCliente(1)
                .nombre("Brian")
                .apellido("Lares")
                .correo("zizou@hotmail.com")
                .fechaRegistro(Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")))
                .build();

        Cliente ge1 = new Cliente(1,"Brian", "Green", "a@b.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        Cliente ge2 = new Cliente(2,"Brian", "Geller", "c@d.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        Cliente ge3 = new Cliente(3,"Brian", "red", "e@f.com",Timestamp.from(Instant.parse("2024-05-29T21:53:26.303Z")));
        List<Cliente> clienteList = List.of(ge1, ge2, ge3);

        String nombreCLiente = nameCliente.getNombre();

        Mockito.when(clienteService.findByNombre(nameCliente.getNombre())).thenReturn(clienteList);
        mockMvc.perform(get("/api/v1/cliente/nombre/{nombre}",nombreCLiente).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void showPokemon() throws Exception{

        String nombre = "ditto";

        Pokemon dto = new Pokemon();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        Mockito.when(restTemplate.exchange("https://pokeapi.co/api/v2/pokemon/" + nombre, HttpMethod.GET, entity, Pokemon.class).getBody()).thenReturn(dto);
        mockMvc.perform(get("/api/v1/cliente/pokemon/{nombre}",nombre).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void encriptar() throws Exception{
        Cifrado cifrado = new Cifrado();
        cifrado.setText("Hola");

        Mockito.when(clienteService.cifrar(cifrado.getText())).thenReturn("ic9sZLSdj8xEP0qzuD1Lmg==");
        mockMvc.perform(post("/api/v1/cliente/cifrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"text\": \"Hola\" \n"+
                                "}"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void desencriptar() throws Exception{
        Cifrado cifrado = new Cifrado();
        cifrado.setText("ic9sZLSdj8xEP0qzuD1Lmg==");

        Mockito.when(clienteService.descifrar(cifrado.getText())).thenReturn("Hola");
        mockMvc.perform(post("/api/v1/cliente/cifrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                " \"text\": \"ic9sZLSdj8xEP0qzuD1Lmg==\" \n"+
                                "}"))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}