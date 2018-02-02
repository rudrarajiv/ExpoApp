package com.exportershouse.expoapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
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
 * Created by Shrey on 29-01-2018.
 */

public class CategoryFragment extends Fragment
{
//    String Url=getResources().getString(R.string.url);

    List<DataAdapter> ListOfdataAdapter;

    RecyclerView recyclerView;


    int RecyclerViewItemPosition ;

    RecyclerView.LayoutManager layoutManagerOfrecyclerView;

    RecyclerView.Adapter recyclerViewadapter;

    ArrayList<String> ImageTitleNameArrayListForClick;

    ArrayList<String> Category_Id = new ArrayList<String>();


    View view;
    GridLayoutManager mLayoutManager;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        view = inflater.inflate(R.layout.category_fragment_, container, false);
        String HTTP_JSON_URL = getResources().getString(R.string.cat_url);

        ImageTitleNameArrayListForClick = new ArrayList<>();

        ListOfdataAdapter = new ArrayList<>();

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview1);

        recyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(mLayoutManager);

        JSON_HTTP_CALL(HTTP_JSON_URL);

        // Implementing Click Listener on RecyclerView.
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


                view = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if(view != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked Item value.
                    RecyclerViewItemPosition = Recyclerview.getChildAdapterPosition(view);

                    FragmentTransaction transection=getFragmentManager().beginTransaction();
                    SubCategoryFragment mfragment=new SubCategoryFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("CatId", Category_Id.get(RecyclerViewItemPosition));

                    mfragment.setArguments(bundle);


                    transection.replace(R.id.content_frame, mfragment);
                    transection.addToBackStack(null).commit();

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

        return view;
    }

    private void JSON_HTTP_CALL(String url)
    {
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("category");
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        DataAdapter GetDataAdapter2 = new DataAdapter();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);



                        GetDataAdapter2.setImageTitle(jsonObject1.getString("name"));
                        String temp_url = "http://192.168.1.224/~expohous/application/public/"+jsonObject1.getString("icon");
                        String id = jsonObject1.getString("id");
                        GetDataAdapter2.setImageUrl(temp_url);

                        Category_Id.add(id);
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