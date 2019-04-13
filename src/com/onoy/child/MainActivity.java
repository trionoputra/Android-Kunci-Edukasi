package com.onoy.child;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.onoy.child.adapter.SettingPagerAdapter;
import com.onoy.child.entity.Category;
import com.onoy.child.sqlite.DatabaseHelper;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.utils.Shared;
import com.onoy.child.widget.SlidingTabLayout;

public class MainActivity extends FragmentActivity {
	private ViewPager pager;
	private SettingPagerAdapter pagerAdapter;
	private RelativeLayout loader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Shared.initialize(getBaseContext());
		DatabaseManager.initializeInstance(new DatabaseHelper(this));
		
		setContentView(R.layout.activity_main);
		pager = (ViewPager)findViewById(R.id.pagerSetting);
		pagerAdapter = new SettingPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(pagerAdapter);
		
		SlidingTabLayout pagerTabStrip = (SlidingTabLayout)findViewById(R.id.pager_title_strip);
		pagerTabStrip.setViewPager(pager);
		
		loader = (RelativeLayout)findViewById(R.id.loader);
		
		getCategori();
	}
	
	private void getCategori()
	{
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(Shared.SERVER_URL+"getclass", new AsyncHttpResponseHandler() {
		    @Override
		    public void onStart() {
		    	loader.setVisibility(View.VISIBLE);
		    }
		    @Override
		    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
		        // called when response HTTP status is "200 OK"
		    	loader.setVisibility(View.GONE);
		    	if(statusCode == 200)
		    	{
		    		try {
		    			String res = new String(response);
		    			JSONObject jsonObject = new JSONObject(res);
		    			
						Object json = new JSONTokener(res).nextValue();
						if (json instanceof JSONArray)
						{
							JSONArray array = new JSONArray(jsonObject.getString("result"));
							ArrayList<Category> list = new ArrayList<Category>();
							for (int i = 0; i < array.length(); i++) {
								JSONObject obj = array.getJSONObject(i);
								Category data = new Category();
								data.setCode(obj.getString("id_kelas"));
								if(obj.getString("levels").toUpperCase().equals("TK"))
									data.setName(obj.getString("levels"));
								else
									data.setName(obj.getString("cls"));
								
								list.add(data);
							}
						}
					} catch (JSONException e) {}
		    	}
		    }

		    @Override
		    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
		        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
		    	loader.setVisibility(View.GONE);
		    }

		    @Override
		    public void onRetry(int retryNo) {
		        // called when request is retried
			}
		});
	}
	
}
