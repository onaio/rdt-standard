package io.ona.rdt.widget;

import android.view.View;

import org.json.JSONException;
import org.junit.Test;

import io.ona.rdt.callback.CovidOnLabelClickedListener;
import io.ona.rdt.callback.OnLabelClickedListener;
import io.ona.rdt.util.CovidConstants;

import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_CONDUCT_RDT_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_ENTER_DELIVERY_DETAILS_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_OPT_IN_WBC_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SCAN_BARCODE_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_TEST_COMPLETE_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_WBC_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_XRAY_PAGE;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidRDTLabelFactoryTest extends RDTLabelFactoryTest {

    @Override
    @Test
    public void testOnClickShouldPerformCorrectAction() throws Exception {
        mockStaticMethods();

        OnLabelClickedListener onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_SCAN_BARCODE));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_SCAN_BARCODE_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_ENTER_RDT_MANUALLY));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(MANUAL_EXPIRATION_DATE_ENTRY_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_ADD_XRAY_RESULTS));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_XRAY_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_SKIP_XRAY_RESULTS));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_OPT_IN_WBC_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_ADD_WBC_RESULTS));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_WBC_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_SKIP_WBC_RESULTS));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_SCAN_SAMPLE_BARCODE));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE));

        onLabelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs(CovidConstants.FormFields.LBL_ENTER_SAMPLE_DETAILS_MANUALLY));
        onLabelClickedListener.onClick(mock(View.class));
        verify(presenter).moveToNextStep(eq(COVID_ENTER_DELIVERY_DETAILS_PAGE));
    }

    @Override
    protected void mockStaticMethods() throws JSONException {
        super.mockStaticMethods();
        jsonObject.put(COVID_SCAN_BARCODE_PAGE, COVID_SCAN_BARCODE_PAGE);
        jsonObject.put(COVID_CONDUCT_RDT_PAGE, COVID_CONDUCT_RDT_PAGE);
        jsonObject.put(COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE, COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE);
        jsonObject.put(COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE, COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE);
        jsonObject.put(COVID_TEST_COMPLETE_PAGE, COVID_TEST_COMPLETE_PAGE);
        jsonObject.put(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE, COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE);
        jsonObject.put(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE, COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE);
        jsonObject.put(COVID_XRAY_PAGE, COVID_XRAY_PAGE);
        jsonObject.put(COVID_OPT_IN_WBC_PAGE, COVID_OPT_IN_WBC_PAGE);
        jsonObject.put(COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE, COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE);
        jsonObject.put(COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE, COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE);
        jsonObject.put(COVID_ENTER_DELIVERY_DETAILS_PAGE, COVID_ENTER_DELIVERY_DETAILS_PAGE);
        jsonObject.put(COVID_WBC_PAGE, COVID_WBC_PAGE);
    }
}
