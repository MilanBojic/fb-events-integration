package fbintegration.app.com;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentEventDetails extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.event_details_layout, null);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
