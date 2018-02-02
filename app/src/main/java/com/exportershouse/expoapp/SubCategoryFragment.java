package com.exportershouse.expoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shrey on 01-02-2018.
 */

public class SubCategoryFragment extends Fragment
{
    View rootview;

    List<DataAdapter> ListOfdataAdapter;
    RecyclerView recyclerView;
    int RecyclerViewItemPosition ;
    RecyclerView.Adapter recyclerViewadapter;
    ArrayList<String> ImageTitleNameArrayListForClick;
    GridLayoutManager mLayoutManager;

    String Category_Id;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootview=inflater.inflate(R.layout.subcategory_fragment,container,false);

        String HTTP_JSON_URL = getResources().getString(R.string.subcat_url);

        ImageTitleNameArrayListForClick = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)rootview.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);


        mLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(mLayoutManager);

        Bundle bundle=getArguments();
        Category_Id= String.valueOf(bundle.getString("CatId"));


        JSON_HTTP_CALL(HTTP_JSON_URL);



        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });
            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent)
            {


                rootview = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(rootview != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(rootview);


//                    // Showing RecyclerView Clicked Item value using Toast.
//                    Toast.makeText(getActivity(), ImageTitleNameArrayListForClick.get(RecyclerViewItemPosition), Toast.LENGTH_LONG).show();
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });





        rootview.setFocusableInTouchMode(true);
        rootview.requestFocus();
        rootview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    // DO WHAT YOU WANT ON BACK PRESSED
                        getFragmentManager().popBackStack();
                    return true;
                }
                else {
                    return false;
                }
            }
        });

       return rootview;
    }


    private void JSON_HTTP_CALL(String url)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url+"?id="+Category_Id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("sub");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        DataAdapter GetDataAdapter2 = new DataAdapter();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        GetDataAdapter2.setImageTitle(jsonObject1.getString("name"));

                        // Adding image title name in array to display on RecyclerView click event.
                        ImageTitleNameArrayListForClick.add(jsonObject1.getString("name"));

                        String temp_url = "http://192.168.1.224/~expohous/application/public/"+jsonObject1.getString("icon");

                        GetDataAdapter2.setImageUrl(temp_url);

                        ListOfdataAdapter.add(GetDataAdapter2);
                    }

                    recyclerViewadapter = new RecyclerViewAdapter(ListOfdataAdapter,getActivity());
                    recyclerView.setAdapter(recyclerViewadapter);
                }catch (JSONException e){e.printStackTrace();}
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }



}
