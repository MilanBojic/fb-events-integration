package fbintegration.app.com;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

public class EventsDetailActivity extends AppCompatActivity {

    fbintegration.app.com.EventDescriptor mEventDescriptor;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        setContentView(R.layout.event_details_layout);

        ImageView logo = findViewById(R.id.event_details_image_id);

        TextView name = findViewById(R.id.event_details_name);
        TextView desc = findViewById(R.id.event_details_desc);
        TextView city = findViewById(R.id.event_details_city);
        TextView country = findViewById(R.id.event_details_country);
        Gson gson = new Gson();

        super.onCreate(savedInstanceState);
        if (getIntent().getExtras().get("eventdesc") != null) {
            String temp = (String) getIntent().getSerializableExtra("eventdesc");
            mEventDescriptor = gson.fromJson(temp, fbintegration.app.com.EventDescriptor.class);
            name.setText(mEventDescriptor.getName());
            desc.setText(mEventDescriptor.getDescription());
            city.setText(mEventDescriptor.getCity());
            country.setText(mEventDescriptor.getCountry());
            try {

                if (mEventDescriptor.getUrl().isEmpty()) {
                    logo.setImageDrawable(getDrawable(R.drawable.events));
                } else {
                    new fbintegration.app.com.DownloadImageTask(logo).execute(mEventDescriptor.getUrl());
                }
            } catch (Exception e) {
                e.printStackTrace();
                logo.setImageDrawable(getDrawable(R.drawable.events));
            }

        }
    }

    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

