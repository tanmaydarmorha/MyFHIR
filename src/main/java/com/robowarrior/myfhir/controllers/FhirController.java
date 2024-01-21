package com.robowarrior.myfhir.controllers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
public class FhirController {

    private static final String FHIR_SERVER_BASE_URL = "https://hapi.fhir.org/baseR4";

    private final FhirContext fhirContext = FhirContext.forR4();

    private final IGenericClient client;

    private final IParser jsonParser;

    public FhirController() {
        client = fhirContext.newRestfulGenericClient(FHIR_SERVER_BASE_URL);
        jsonParser = fhirContext.newJsonParser();
    }

    @GetMapping(value = "/readPatient", produces = MediaType.APPLICATION_JSON_VALUE)
    public String readPatient(@RequestParam String patientId) {
        Optional<Patient> optionalPatient;
        try {
            optionalPatient = Optional.ofNullable(client
                    .read()
                    .resource(Patient.class)
                    .withId(patientId)
                    .execute());
        } catch (ResourceNotFoundException exception) {
            optionalPatient = Optional.empty();
        }
        String returnItem;
        if (optionalPatient.isPresent()) {
            returnItem = jsonParser.encodeResourceToString(optionalPatient.get());
        } else {
            returnItem = "Could not find patient for given id : " + patientId;
        }
        return returnItem;
    }

    @GetMapping(value = "/searchForCoverages", produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchForCoverages() {
        Bundle returnItem;

        returnItem = client
                .search()
                .forResource(Coverage.class)
                .returnBundle(Bundle.class)
                .execute();

        return jsonParser.encodeResourceToString(returnItem);
    }
}
