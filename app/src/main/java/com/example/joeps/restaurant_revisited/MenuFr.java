package com.example.joeps.restaurant_revisited;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFr extends ListFragment {
    ArrayAdapter<String> adapter;
    String dish;
    Double price;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            String lijst = getArguments().getString("category");
            openMenu(lijst);
        }
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    public void openMenu(final String picked) {

        final ArrayList<String> listdata = new ArrayList<String>();
        RequestQueue queue = Volley.newRequestQueue((MainActivity)getActivity());

        String url = "https://resto.mprog.nl/menu";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public JSONArray ja_data = null;

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());
                            ja_data = jsonObj.getJSONArray("items");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        JSONArray jArray = (JSONArray) ja_data;
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    JSONObject jsonArray2 = new JSONObject(jArray.getString(i));
                                    String catgor = jsonArray2.getString("category");

                                    if (catgor.equals(picked)) {
                                        listdata.add(jsonArray2.getString("name"));
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                            }
                            ArrayList<String> myArray = listdata;
                            adapter =
                                    new ArrayAdapter<String>(
                                            getActivity(),
                                            R.layout.main_layout,
                                            myArray);
                            doAdapter();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }
    public void doAdapter(){
        this.setListAdapter(adapter);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        setItem(("" +(l.getItemAtPosition(position))));
    }
    public void setItem(final String picked){
        RequestQueue queue = Volley.newRequestQueue((MainActivity)getActivity());

        String url = "https://resto.mprog.nl/menu";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    public JSONArray ja_data = null;

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());
                            ja_data = jsonObj.getJSONArray("items");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        JSONArray jArray = (JSONArray) ja_data;
                        if (jArray != null) {
                            for (int i = 0; i < jArray.length(); i++) {
                                try {
                                    JSONObject jsonArray2 = new JSONObject(jArray.getString(i));
                                    String catgor = jsonArray2.getString("name");

                                    if (catgor.equals(picked)) {
                                        RestoDatabase db = RestoDatabase.getInstance(getContext());
                                        dish = jsonArray2.getString("name");
                                        price = jsonArray2.getDouble("price");
                                        db.addItem(dish,price);
                                        Toast toast = Toast.makeText(getContext(), "Added "+dish + " to the order!", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });
        queue.add(stringRequest);
    }
}
