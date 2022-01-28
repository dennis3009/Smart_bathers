package com.example.SmartBath.service;

import com.example.SmartBath.model.Doctor;
import com.example.SmartBath.model.MedicalOffice;

public class DoctorService {
    public void addDoctor (MedicalOffice medicalOffice, Doctor doctor) {
        medicalOffice.getDoctors().add(doctor);
    }

    public Doctor searchDoctorByFullName(MedicalOffice medicalOffice, String name, String surname) {
        Doctor doc = null;
        for (Doctor d : medicalOffice.getDoctors())
            if (d != null && d.getName().equals(name) && d.getSurname().equals(surname)) {
                doc = d;
            }
        return doc;
    }



    public void updateSpecialization (Doctor doctor, String specialization) {
        doctor.setSpecialization(specialization);
    }

    private int getNumberOfDoctors(MedicalOffice medicalOffice) {
        int numberOfDoctors = 0;
        for (Doctor d : medicalOffice.getDoctors())
            if (d != null) {
                numberOfDoctors++;
            }
        return numberOfDoctors;
    }

    public void printDoctors(MedicalOffice medicalOffice) {
        for (Doctor d : medicalOffice.getDoctors())
            if (d != null) {
                System.out.println(d);
            }
    }
}
