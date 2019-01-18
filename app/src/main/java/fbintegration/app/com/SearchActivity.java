package fbintegration.app.com;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends AppCompatActivity implements fbintegration.app.com.OnItemClick {

    private LinearLayoutManager mLinearLayoutManager;
    private fbintegration.app.com.EventDescriptor mEventDescriptor;
    private List<fbintegration.app.com.EventItem> eventList = new ArrayList<>();
    private List<fbintegration.app.com.EventItem> cloneList = new ArrayList<>();
    private List<fbintegration.app.com.EventItem> tempListCity;
    private List<fbintegration.app.com.EventItem> tempListName;
    private EventListAdapter mAdapter;
    private EditText editText;
    private fbintegration.app.com.CoverResponse cover;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        Gson gson = new Gson();
        if (getIntent().getExtras().get("list") != null) {
            String temp = (String) getIntent().getSerializableExtra("list");
            Type type = new TypeToken<List<fbintegration.app.com.EventItem>>() {}.getType();
            eventList = gson.fromJson(temp, type);
            cloneList.addAll(eventList);
        }
        ImageView clearQuery = findViewById(R.id.clear_query_id);
        clearQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                editText.clearFocus();
                editText.setText("");
                tempListCity.clear();
                tempListName.clear();
                eventList.clear();
                eventList.addAll(cloneList);
                mAdapter.notifyDataSetChanged();

            }
        });
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getApplicationContext(), SessionDataActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final RecyclerView recyclerView = findViewById(R.id.search_list_recycler);
        mAdapter = new fbintegration.app.com.EventListAdapter(eventList, this);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tempListCity = new ArrayList<>();
        tempListName = new ArrayList<>();
        recyclerView.setAdapter(mAdapter);
        editText = findViewById(R.id.search_box);

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                               @Override
                                               public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                                                   if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                                                       eventList.clear();
                                                       eventList.addAll(cloneList);
                                                       tempListCity.clear();
                                                       tempListName.clear();
                                                       String query = v.getText().toString().trim().toUpperCase();
                                                       for (fbintegration.app.com.EventItem eventItem : eventList) {
                                                           try {
                                                               String[] splitNameArray = eventItem.getName().toUpperCase().split("\\s+");
                                                               for (int i = 0; i < splitNameArray.length; i++) {
                                                                   if (splitNameArray[i].trim().toUpperCase().equals(query)) {
                                                                       tempListName.add(eventItem);
                                                                   }
                                                               }

                                                               if (eventItem.place.location.getCity().trim().toUpperCase().equals(query)) {
                                                                   tempListCity.add(eventItem);
                                                               }

                                                           } catch (Exception e) {
                                                               e.printStackTrace();
                                                           }

                                                       }

                                                       eventList.clear();
                                                       eventList.addAll(tempListCity);
                                                       eventList.addAll(tempListName);
                                                       if (eventList.size() == 0) {
                                                           Toast.makeText(getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                                                       } else {
                                                           mAdapter.notifyDataSetChanged();
                                                       }
                                                       editText.setText("");
                                                   }

                                                   return true;
                                               }
                                           }

        );
        mEventDescriptor = new

                fbintegration.app.com.EventDescriptor();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        eventList.clear();
        cloneList.clear();
        tempListCity.clear();
        tempListName.clear();
        Intent intent = new Intent(getApplicationContext(), SessionDataActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        editText.setText("");
        eventList.clear();
        tempListCity.clear();
        tempListName.clear();
        eventList.addAll(cloneList);
        mAdapter.notifyDataSetChanged();

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
                    cover = gson.fromJson(json, fbintegration.app.com.CoverResponse.class);
                }
            }).executeAsync();
        } catch (Exception e) {
            e.printStackTrace();

        }
        try {

            String name = eventList.get(position).getName();
            String description = eventList.get(position).description;
            String city = eventList.get(position).place.location.getCity();
            String country = eventList.get(position).place.location.getCountry();

            mEventDescriptor.setName(name);
            mEventDescriptor.setDescription(description);
            mEventDescriptor.setCity(city);
            mEventDescriptor.setCountry(country);
            mEventDescriptor.clearUrl();
        } catch (Exception e) {
            e.printStackTrace();

            mEventDescriptor.setName(eventList.get(position).name);
            mEventDescriptor.setDescription(eventList.get(position).description);
            mEventDescriptor.setCity("");
            mEventDescriptor.setCountry("");
            mEventDescriptor.clearUrl();
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    mEventDescriptor.setUrl(cover.cover.getSource());
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                Intent eventDetailsIntent = new Intent(getApplicationContext(), EventsDetailActivity.class);
                eventDetailsIntent.putExtra("eventdesc", new Gson().toJson(mEventDescriptor));
                startActivity(eventDetailsIntent);

            }
        }, 370);
    }
}
