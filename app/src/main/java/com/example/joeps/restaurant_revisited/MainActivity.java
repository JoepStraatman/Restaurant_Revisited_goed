package com.example.joeps.restaurant_revisited;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


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

public class MainActivity extends AppCompatActivity {
    public String[] itemlist;
    private step2 fragment;
    public ArrayList<String> listdata2 = new ArrayList<String>();;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                FragmentTransaction ftt = getSupportFragmentManager().beginTransaction();
                OrderFragment fr = new OrderFragment();
                fr.show(ftt, "dialog");
        }return super.onOptionsItemSelected(item);
    }
    public MainActivity() {
        super();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openCategory();
    }
    public void slaOp(){
        Bundle bundle = new Bundle();
        bundle.putStringArray("saai",itemlist);
        FragmentManager fm = getSupportFragmentManager();
        fragment = new step2();
        fragment.setArguments(bundle);
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment, "categories");
        ft.commit();
    }
    public void openCategory(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://resto.mprog.nl/categories";

        JsonObjectRequest stringRequest = new JsonObjectRequest (Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    public JSONArray ja_data;
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response.toString());
                            ja_data = jsonObj.getJSONArray("categories");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        JSONArray jArray = (JSONArray)ja_data;
                        if (jArray != null) {
                            itemlist = new String[jArray.length()];
                            for (int i=0;i<jArray.length();i++) {
                                try {

                                    itemlist[i] = (jArray.getString(i));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            slaOp();
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
