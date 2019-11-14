package io.ona.rdt.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class})
public class RDTJsonFormActivityPresenterTest {

    @Mock
    private RDTJsonFormActivityContract.View activity;

    private RDTJsonFormActivityPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = new RDTJsonFormActivityPresenter(activity);
    }

    @Test
    public void testOnBackPress() {
        mockStaticClasses();

        // test back-press disabled for expiration date and rdt-capture screens
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(6);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(13);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(14);
        presenter.onBackPress();
        verifyZeroInteractions(activity);

        // allow back-press for other screens
        when(RDTJsonFormFragment.getCurrentStep()).thenReturn(3);
        presenter.onBackPress();
        verify(activity).onBackPress();
    }

    private void mockStaticClasses() {
        mockStatic(RDTJsonFormFragment.class);
    }
}
