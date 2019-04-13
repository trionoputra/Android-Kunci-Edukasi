package com.onoy.child.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.onoy.child.R;
import com.onoy.child.entity.Category;
import com.onoy.child.sqlite.DatabaseManager;
import com.onoy.child.sqlite.ds.CategoryDataSource;

public class SpinnerCategoryAdapter extends BaseAdapter {	
	private Activity activity;
	private LayoutInflater inflater;
	private List<Category> dtlist ;
	public SpinnerCategoryAdapter(Activity activity) {
		this.activity = activity;
		SQLiteDatabase db =  DatabaseManager.getInstance().openDatabase();
		CategoryDataSource DS = new CategoryDataSource(db);
		dtlist = new ArrayList<Category>(DS.getAll());
		DatabaseManager.getInstance().closeDatabase();
		
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		return dtlist.size();
	}
	@Override
	public Object getItem(int location) {
		return dtlist.get(location);
	}
	
	public void add(Category item) {
	
		dtlist.add(item);
		notifyDataSetChanged();
	}
	
	public List<Category> getAll() {
		
		return dtlist;
	}
	
	public void add(List<Category> item){
	
		for (int i = 0; i < item.size(); i++) {
			dtlist.add(item.get(i));
		}
		notifyDataSetChanged();
	}
	
	public void set(List<Category> data) {
		dtlist = data;
		notifyDataSetChanged();
	}
	
	public void insert(Category item,int pos) {
	
		dtlist.add(pos, item);
		notifyDataSetChanged();
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public static class ViewHolder{
	     public TextView text1;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View vi = convertView;
        ViewHolder holder;
        
        if(convertView==null){
            vi = inflater.inflate(R.layout.spinner_category_item, null);
            
            holder = new ViewHolder();
            
            holder.text1 = (TextView) vi.findViewById(R.id.textView2);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
        final Category data = this.dtlist.get(position);
        holder.text1.setText(data.getName());
        
		return vi;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
        ViewHolder holder;
        
        if(convertView==null){
            vi = inflater.inflate(R.layout.spinner_category_dropdown_item, null);
            
            holder = new ViewHolder();
            
            holder.text1 = (TextView) vi.findViewById(R.id.textView1);
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
        final Category data = this.dtlist.get(position);
        holder.text1.setText(data.getName());
        
		return vi;
	}
	
	

}
