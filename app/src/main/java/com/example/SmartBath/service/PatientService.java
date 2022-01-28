package com.example.SmartBath.service;

import com.example.SmartBath.model.MedicalOffice;
import com.example.SmartBath.model.Patient;

public class PatientService {
    public void addPatient (MedicalOffice medicalOffice, Patient patient) {
        medicalOffice.getPatients().add(patient);
    }

    public Patient searchPatientByFullName (MedicalOffice medicalOffice, String name, String surname) {
        Patient patient = null;
        for (Patient p : medicalOffice.getPatients())
            if (p != null && p.getName().equals(name) && p.getSurname().equals(surname)) {
                patient = p;
            }
        return patient;
    }

    public void updateCondition (Patient patient, String condition) {
        patient.setCondition(condition);
    }

    private int getNumberOfPatients(MedicalOffice medicalOffice) {
        int numberOfPatients = 0;
        for (Patient p : medicalOffice.getPatients())
            if (p != null) {
                numberOfPatients++;
            }
        return numberOfPatients;
    }

    public void printPatients(MedicalOffice medicalOffice) {
        for (Patient p : medicalOffice.getPatients())
            if (p != null) {
                System.out.println(p);
            }
    }

}
