package com.example.rev_task_management_project02.services;


import com.example.rev_task_management_project02.dao.ClientRepository;
import com.example.rev_task_management_project02.models.Client;
import com.example.rev_task_management_project02.utilities.EntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private ClientRepository clientRepository;
    private EntityUpdater entityUpdater;

    @Autowired
    public ClientService(ClientRepository clientRepository,EntityUpdater entityUpdater) {
        this.clientRepository = clientRepository;
        this.entityUpdater=entityUpdater;
    }


    public Client createClient(Client client) {
        client.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return clientRepository.save(client);
    }


    public Optional<Client> getClientById(long clientId) {
        return clientRepository.findById(clientId);
    }


    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }


    public Client updateClient(long clientId, Client clientDetails) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + clientId));

        Client updateclient = entityUpdater.updateFields(client,clientDetails);

        return clientRepository.save(client);
    }


    public void deleteClient(long clientId) {
        clientRepository.deleteById(clientId);
    }
}

