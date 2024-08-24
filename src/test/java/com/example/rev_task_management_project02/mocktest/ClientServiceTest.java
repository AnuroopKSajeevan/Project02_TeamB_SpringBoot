package com.example.rev_task_management_project02.mocktest;


import com.example.rev_task_management_project02.dao.ClientRepository;
import com.example.rev_task_management_project02.models.Client;
import com.example.rev_task_management_project02.services.ClientService;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private EntityUpdater entityUpdater;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateClient() {
        Client client = new Client();
        client.setClientName("John Doe");
        client.setClientCompanyName("Doe Inc.");
        client.setClientEmail("john.doe@example.com");
        client.setClientPhone("1234567890");

        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client createdClient = clientService.createClient(client);

        assertNotNull(createdClient);
        assertEquals("John Doe", createdClient.getClientName());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testGetClientById() {
        Client client = new Client();
        client.setClientId(1L);
        client.setClientName("John Doe");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> foundClient = clientService.getClientById(1L);

        assertTrue(foundClient.isPresent());
        assertEquals("John Doe", foundClient.get().getClientName());
        verify(clientRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllClients() {
        Client client1 = new Client();
        Client client2 = new Client();
        List<Client> clients = Arrays.asList(client1, client2);

        when(clientRepository.findAll()).thenReturn(clients);

        List<Client> foundClients = clientService.getAllClients();

        assertEquals(2, foundClients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testUpdateClient() {
        Client existingClient = new Client();
        existingClient.setClientId(1L);
        existingClient.setClientName("John Doe");

        Client updatedDetails = new Client();
        updatedDetails.setClientName("Jane Doe");

        when(clientRepository.findById(1L)).thenReturn(Optional.of(existingClient));
        when(entityUpdater.updateFields(existingClient, updatedDetails)).thenReturn(existingClient);
        when(clientRepository.save(existingClient)).thenReturn(existingClient);

        Client updatedClient = clientService.updateClient(1L, updatedDetails);

        assertEquals("John Doe", updatedClient.getClientName());
        verify(clientRepository, times(1)).findById(1L);
        verify(entityUpdater, times(1)).updateFields(existingClient, updatedDetails);
        verify(clientRepository, times(1)).save(existingClient);
    }

    @Test
    void testDeleteClient() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.deleteClient(1L);

        verify(clientRepository, times(1)).deleteById(1L);
    }
}


