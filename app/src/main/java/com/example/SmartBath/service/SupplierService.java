package com.example.SmartBath.service;

import com.example.SmartBath.model.MedicalOffice;
import com.example.SmartBath.model.Supplier;

public class SupplierService {

    public void addSupplier (MedicalOffice medicalOffice, Supplier supplier) {
        //int nextAvailableIndex = getNumberOfSuppliers(medicalOffice);
        medicalOffice.getSuppliers().add(supplier);
    }

    public void updateName (Supplier supplier, String name) { supplier.setName(name); }

    public void updateLocation (Supplier supplier, String location) { supplier.setLocation(location); }

    private int getNumberOfSuppliers(MedicalOffice medicalOffice) {
        int numberOfSuppliers = 0;
        for (Supplier p : medicalOffice.getSuppliers())
            if (p != null) {
                numberOfSuppliers++;
            }
        return numberOfSuppliers;
    }
}
