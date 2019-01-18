package fbintegration.app.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;


public class SecondFragment extends Fragment implements OnItemClick {

    private String LOG_TAG = this.getClass().getSimpleName();
    private LinearLayoutManager mLinearLayoutManager;

    private List<EventItem> eventList = new ArrayList<>();
    private RecyclerView recyclerView;
    private EventListAdapter mAdapter;
    private fbintegration.app.com.EventDescriptor eventDescriptor;
    private CoverResponse cover;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment_layot, container, false);
        recyclerView = view.findViewById(R.id.event_list_recycler_view);
        mAdapter = new EventListAdapter(eventList, this);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        eventDescriptor = new fbintegration.app.com.EventDescriptor();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart - SessionDataActivity");

        Bundle parameters = new Bundle();
        parameters.putString("type", "event");
        parameters.putString("q", "a");
        parameters.putString("since", "2010");
        parameters.putString("until", "now");

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/search", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            public void onCompleted(GraphResponse response) {
                Gson gson = new GsonBuilder().create();
                String json = response.getJSONObject().toString();
                Log.i("json", json);
                EventData eventResponse = gson.fromJson(json, EventData.class);

                for (EventItem eventItem : eventResponse.data) {
                    eventList.add(eventItem);
                    mAdapter.notifyDataSetChanged();

                }
            }
        }).executeAsync();
    }

    @Override
    public void onItemClick(final View view, final int position) {
        try {
            final long idCover = eventList.get(position).id;
            String query = String.valueOf(idCover).trim() + "?fields=cover";
            new GraphRequest(AccessToken.getCurrentAccessToken(), query, null, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    Gson gson = new GsonBuilder().create();
                    String json = response.getJSONObject().toString();
                    cover = gson.fromJson(json, CoverResponse.class);
                }
            }).executeAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String name = eventList.get(position).name;
            String description = eventList.get(position).description;
            String city = eventList.get(position).place.location.getCity();
            String country = eventList.get(position).place.location.getCountry();

            eventDescriptor.setName(name);
            eventDescriptor.setDescription(description);
            eventDescriptor.setCity(city);
            eventDescriptor.setCountry(country);
            eventDescriptor.clearUrl();
        } catch (Exception e) {
            e.printStackTrace();
            eventDescriptor.setName(eventList.get(position).name);
            eventDescriptor.setDescription(eventList.get(position).description);
            eventDescriptor.setCity("");
            eventDescriptor.setCountry("");
            eventDescriptor.clearUrl();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    eventDescriptor.setUrl(cover.cover.getSource());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Intent eventDetailsIntent = new Intent(getContext(), EventsDetailActivity.class);
                eventDetailsIntent.putExtra("eventdesc", new Gson().toJson(eventDescriptor));
                startActivity(eventDetailsIntent);

            }
        }, 370);

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop");
        eventList.clear();
    }

}

