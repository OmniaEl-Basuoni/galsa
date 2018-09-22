package com.example.omnia.ourproject.AutoCompleteCountry.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.omnia.ourproject.AutoCompleteCountry.Class.Country;
import com.example.omnia.ourproject.R;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country> {
    private final String MY_DEBUG_TAG = "CustomerAdapter";
    private ArrayList<Country> items;
    private ArrayList<Country> itemsAll;
    private ArrayList<Country> suggestions;
    private int viewResourceId;

    public CountryAdapter(Context context, int viewResourceId, ArrayList<Country> items) {
        super(context, viewResourceId, items);
        this.items = items;
        this.itemsAll = (ArrayList<Country>) items.clone();
        this.suggestions = new ArrayList<Country>();
        this.viewResourceId = viewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        Country customer = items.get(position);
        if (customer != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.spinnerView);
            if (customerNameLabel != null) {
//              Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(customer.getCountry().getCountryName());
            }
        }
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((Country)(resultValue)).getCountry().getCountryName();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (Country customer : itemsAll) {
                    if(customer.getCountry().getCountryName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        suggestions.add(customer);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Country> filteredList = (ArrayList<Country>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (Country c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };

}