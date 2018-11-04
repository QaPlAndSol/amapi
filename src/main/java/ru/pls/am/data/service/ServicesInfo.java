package ru.pls.am.data.service;

import java.util.LinkedList;

public class ServicesInfo {

    private LinkedList<Service> services;

    public Service getService(String serviceName) {
        for (Service service : services) {
            if (service.getName().toLowerCase().equals(serviceName.toLowerCase())) {
                return service;
            }
        }
        return null;
    }
}
