package com.example.joeps.restaurant_revisited;


import android.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Gebruiker on 30-11-2017.
 */

public class OrderFragment extends DialogFragment implements View.OnClickListener  {
    RestoDatabase db;
    RestoAdapter adapter;
    ListView order;
    @Nullable
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        order = view.findViewById(R.id.order);
        TextView tot = (TextView) view.findViewById(R.id.total);
        RestoDatabase db = RestoDatabase.getInstance(getContext());
        tot.setText("Total price: "+db.getTotal());
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setText("Cancel");
        cancel.setOnClickListener(this);
        Button place = (Button) view.findViewById(R.id.place);
        place.setText("Place order");
        place.setOnClickListener(this);
        return view;

    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        db = RestoDatabase.getInstance(getContext());
        Cursor cursor = db.selectAll();
        adapter = new RestoAdapter(getContext(), cursor);
        order.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                db = RestoDatabase.getInstance(getContext());
                db.clear();
                break;
            case R.id.place:
                db = RestoDatabase.getInstance(getContext());
                submit();
                db.clear();
                break;
        }
    }
    public void submit() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest sr = new StringRequest(Request.Method.POST, "https://resto.mprog.nl/order", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast toast = Toast.makeText(getContext(), response, Toast.LENGTH_LONG);
                toast.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast toast = Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }) ;
        queue.add(sr);
    }
}
