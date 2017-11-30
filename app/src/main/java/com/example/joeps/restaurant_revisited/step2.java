package com.example.joeps.restaurant_revisited;



import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class step2 extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            String[] lijst = getArguments().getStringArray("saai");
            List<String> lst = Arrays.asList(lijst);
            ArrayList<String> array = new ArrayList<String>(lst);
            ArrayList<String> myArray = array;
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(
                            getActivity(),
                            R.layout.main_layout,
                            myArray);
            this.setListAdapter(adapter);
        }

        return inflater.inflate(R.layout.fragment_step2, container, false);
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        MenuFr menuFragment = new MenuFr();

        Bundle args = new Bundle();
        String picked = ("" +(l.getItemAtPosition(position)));
        args.putString("category", picked);
        menuFragment.setArguments(args);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, menuFragment)
                .addToBackStack(null)
                .commit();
    }



}
