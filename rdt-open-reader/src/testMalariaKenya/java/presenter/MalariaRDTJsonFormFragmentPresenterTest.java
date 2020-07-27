package presenter;

import org.json.JSONException;
import org.junit.Test;

import io.ona.rdt.presenter.BaseRDTJsonFormFragmentPresenterTest;
import io.ona.rdt.util.Constants;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class MalariaRDTJsonFormFragmentPresenterTest extends BaseRDTJsonFormFragmentPresenterTest {

    @Test
    public void testPerformNextButtonActionShouldShowImageViewsForONARDT() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        doReturn(Constants.RDTType.ONA_RDT).when(rdtFormFragmentView).getRDTType();
        presenter.performNextButtonAction("step8", null);
        verify(rdtFormFragmentView).navigateToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForOnaRDT() throws JSONException {
        doReturn(Constants.RDTType.ONA_RDT).when(rdtFormFragmentView).getRDTType();
        mockStaticClasses();
        presenter.performNextButtonAction("step9", null);
        verify(rdtFormFragmentView).navigateToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSkipImageViewsForCarestartRDT() throws JSONException {
        doReturn(Constants.RDTType.CARESTART_RDT).when(rdtFormFragmentView).getRDTType();
        mockStaticClasses();
        presenter.performNextButtonAction("step9", null);
        verify(rdtFormFragmentView).transactFragment(eq(formFragment));
    }
}
