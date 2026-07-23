package com.medapp.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.medapp.backend.model.Patient;
import com.medapp.backend.model.Sexe;
import com.medapp.backend.repository.PatientRepository;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Test
    void creerPatient_reussit_siDonneesValides(){
        //given
        Patient patient = new Patient("Dupont", "Marie", LocalDate.of(1990, 5, 12), Sexe.F,
                "12345678", "12 rue de la Paix", "1900512123456",
                List.of("Diabète type 2"), null, null, null);

        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        Patient patientCree = patientService.creerPatient(patient);

        //then
        assertEquals("Dupont", patientCree.getNom());
        assertNotNull(patientCree.getDateCreation());
        verify(patientRepository).save(any(Patient.class));
    }
    
}
