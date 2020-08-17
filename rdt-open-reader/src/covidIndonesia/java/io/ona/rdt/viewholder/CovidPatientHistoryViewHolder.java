package io.ona.rdt.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;

/**
 * Created by Vincent Karuri on 17/08/2020
 */
public class CovidPatientHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView tvHistoryKey;
    public TextView tvHistoryValue;

    public CovidPatientHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        tvHistoryKey = itemView.findViewById(R.id.tv_history_key);
        tvHistoryKey = itemView.findViewById(R.id.tv_history_value);
    }
}
