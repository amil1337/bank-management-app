package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID id;
    private String fullName;
    private String email;
    private final List<String> accountsList;

    public User(String fullName, String email) {
        id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.accountsList = new ArrayList<>();
    }

    public User(UUID id, String fullName, String email) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.accountsList = new ArrayList<>();
    }

    public void addAccount(String accountNumber) {
        this.accountsList.add(accountNumber);
    }

    public void removeAccount(String accountNumber) {
        this.accountsList.remove(accountNumber);
    }

    public UUID getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAccountsList() {
        return accountsList;
    }
}
