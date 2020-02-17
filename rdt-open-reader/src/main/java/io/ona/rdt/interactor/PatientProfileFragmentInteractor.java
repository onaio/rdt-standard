package io.ona.rdt.interactor;

import java.util.List;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.repository.RDTTestsRepository;
import io.ona.rdt.util.FormLauncher;

/**
 * Created by Vincent Karuri on 15/01/2020
 */
public class PatientProfileFragmentInteractor extends FormLauncher {

    private final RDTTestsRepository rdtTestsRepository = RDTApplication.getInstance().getRdtTestsRepository();

    public List<RDTTestDetails> getRDTTestDetailsByBaseEntityId(String baseEntityId) {
       return rdtTestsRepository.getRDTTestDetailsByBaseEntityId(baseEntityId);
    }
}
