package com.medapp.backend.exception;

public class PatientIntrouvableException extends RuntimeException {

    public PatientIntrouvableException(String id){
        super("Aucun patient trouvé avec l'id : " + id);
    }
    
}
