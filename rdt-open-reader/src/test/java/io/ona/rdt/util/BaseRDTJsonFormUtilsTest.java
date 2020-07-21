package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.ImageRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.view.activity.DrishtiApplication;

import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.ubicomplab.rdt_reader.utils.ImageUtil;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;

import static io.ona.rdt.TestUtils.getTestFilePath;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 16/07/2020
 */

@PrepareForTest({AssetHandler.class, ImageUtil.class, RDTApplication.class, org.smartregister.Context.class, DrishtiApplication.class})
public abstract class BaseRDTJsonFormUtilsTest extends PowerMockTest {

    protected final String UNIQUE_ID = "unique_id";

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    protected ImageRepository imageRepository;

    @Mock
    protected UniqueIdRepository uniqueIdRepository;

    @Mock
    protected AllSettings allSettings;

    @Mock
    private StepStateConfig stepStateConfig;


    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPrePopulateFormFieldsShouldPopulateCorrectValues() throws JSONException {
        mockStaticMethods();

        Patient patient = new Patient("patient", "female", "entity_id");
        JSONObject formJsonObj = getFormUtils().launchForm(mock(Activity.class), getFormToPrepopulate(), patient, getIDs());

        int numOfPopulatedFields = 0;
        JSONArray fields = getMultiStepFormFields(formJsonObj);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            numOfPopulatedFields = assertFieldsArePopulated(field, patient, numOfPopulatedFields);
        }
        assertAllFieldsArePopulated(numOfPopulatedFields);
    }

    protected void mockStaticMethods() {
        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when(AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(getMockForm());

        mockStatic(ImageUtil.class);

        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.imageRepository()).thenReturn(imageRepository);
        PowerMockito.when(drishtiContext.getUniqueIdRepository()).thenReturn(uniqueIdRepository);
        PowerMockito.when(drishtiContext.allSettings()).thenReturn(allSettings);

        // Drishti
        mockStatic(DrishtiApplication.class);
        PowerMockito.when(DrishtiApplication.getAppDir()).thenReturn(getTestFilePath());

        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
        doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
    }

    private List<String> getIDs() {
        List<String> rdtIds = new ArrayList<>();
        rdtIds.add(UNIQUE_ID);
        return rdtIds;
    }

    protected abstract void assertAllFieldsArePopulated(int numOfPopulatedFields);

    protected abstract int assertFieldsArePopulated(JSONObject field, Patient patient, int numOfPopulatedFields) throws JSONException;

    protected abstract RDTJsonFormUtils getFormUtils();

    protected abstract String getFormToPrepopulate();

    protected abstract String getMockForm();
}
