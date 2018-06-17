package me.akshanshjain.popularmovies.Fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.akshanshjain.popularmovies.Adapters.SimilarMovieAdapter;
import me.akshanshjain.popularmovies.Object.SimilarItem;
import me.akshanshjain.popularmovies.R;

public class SimilarMovies extends Fragment {

    private List<SimilarItem> similarItemList;
    private RecyclerView similarRecycler;
    private SimilarMovieAdapter adapter;

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;

    private String SIMILAR_URL;
    private String BASE_URL;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_similar_layout, container, false);

        SIMILAR_URL = getContext().getResources().getString(R.string.similarUrl);
        BASE_URL = getContext().getResources().getString(R.string.baseUrl);

        similarItemList = new ArrayList<>();
        similarRecycler = view.findViewById(R.id.similar_recycler);
        adapter = new SimilarMovieAdapter(this.getContext(), similarItemList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        similarRecycler.setLayoutManager(layoutManager);
        similarRecycler.setItemAnimator(new DefaultItemAnimator());
        similarRecycler.setAdapter(adapter);

        Bundle receiverBundle = getActivity().getIntent().getExtras();
        if (receiverBundle != null) {
            String id = receiverBundle.getString("ID");
            //Constructing the URL by adding the movie ID from the data.
            SIMILAR_URL = SIMILAR_URL.replace("{movieID}", id);
            Log.d("ADebug", SIMILAR_URL);
        }

        requestQueue = Volley.newRequestQueue(getContext());
        if (isConnected()) {
            similarItemList.clear();
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, SIMILAR_URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            extractFromJSON(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getContext().getApplicationContext(), getResources().getString(R.string.check_network_connection), Toast.LENGTH_SHORT).show();
                }
            });
        }

        requestQueue.add(jsonObjectRequest);
        adapter.notifyDataSetChanged();

        return view;
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null;
    }

    private void extractFromJSON(JSONObject baseJsonResponse) {
        try {
            JSONArray results = baseJsonResponse.getJSONArray("results");
            for (int c = 0; c < results.length(); c++) {
                JSONObject similarObject = results.getJSONObject(c);

                String name = similarObject.getString("original_title");
                String overview = similarObject.getString("overview");
                String poster_path = similarObject.getString("poster_path");
                String image = BASE_URL + poster_path;

                similarItemList.add(new SimilarItem(name, overview, image));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
