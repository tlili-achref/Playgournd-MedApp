package com.medapp.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.medapp.backend.model.Patient;
import com.medapp.backend.repository.PatientRepository;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository){
        this.patientRepository = patientRepository;
    }
    
    public Patient creerPatient(Patient patient){
        patient.setDateCreation(LocalDateTime.now());
        return patientRepository.save(patient);
    }
}
