package com.example.rev_task_management_project02.mockmvc;

import com.example.rev_task_management_project02.controllers.ClientController;
import com.example.rev_task_management_project02.models.Client;
import com.example.rev_task_management_project02.services.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateClient() throws Exception {
        Client client = new Client();
        client.setClientName("John Doe");
        client.setClientCompanyName("Doe Inc.");
        client.setClientEmail("john.doe@example.com");
        client.setClientPhone("1234567890");

        Mockito.when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientName").value("John Doe"))
                .andExpect(jsonPath("$.clientCompanyName").value("Doe Inc."))
                .andExpect(jsonPath("$.clientEmail").value("john.doe@example.com"))
                .andExpect(jsonPath("$.clientPhone").value("1234567890"));
    }

    @Test
    void testGetClientById() throws Exception {
        Client client = new Client();
        client.setClientId(1L);
        client.setClientName("John Doe");

        Mockito.when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.clientName").value("John Doe"));
    }

    @Test
    void testGetClientByIdNotFound() throws Exception {
        Mockito.when(clientService.getClientById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client1 = new Client();
        client1.setClientId(1L);
        client1.setClientName("John Doe");

        Client client2 = new Client();
        client2.setClientId(2L);
        client2.setClientName("Jane Doe");

        Mockito.when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].clientId").value(1L))
                .andExpect(jsonPath("$[0].clientName").value("John Doe"))
                .andExpect(jsonPath("$[1].clientId").value(2L))
                .andExpect(jsonPath("$[1].clientName").value("Jane Doe"));
    }

    @Test
    void testUpdateClient() throws Exception {
        Client updatedClient = new Client();
        updatedClient.setClientId(1L);
        updatedClient.setClientName("Jane Doe");

        Mockito.when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(updatedClient);

        mockMvc.perform(put("/api/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.clientName").value("Jane Doe"));
    }

    @Test
    void testUpdateClientNotFound() throws Exception {
        Mockito.when(clientService.updateClient(anyLong(), any(Client.class)))
                .thenThrow(new RuntimeException("Client not found"));

        mockMvc.perform(put("/api/clients/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Client())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/{id}", 1L))
                .andExpect(status().isNoContent());

        Mockito.verify(clientService, Mockito.times(1)).deleteClient(1L);
    }
}

