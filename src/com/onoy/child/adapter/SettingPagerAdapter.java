package com.onoy.child.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.onoy.child.fragment.AppLockListFragment;
import com.onoy.child.fragment.SettingFragment;

public class SettingPagerAdapter  extends FragmentStatePagerAdapter  {
	 public SettingPagerAdapter(FragmentManager fm) {
         super(fm);
     }
	 
     @Override
     public Fragment getItem(int position) {
    	 if(position == 0)
    		 return new SettingFragment();
    	 else  if(position == 1)
    		 return new AppLockListFragment();
    	 else
    		 return null;
     }
     @Override
    public CharSequence getPageTitle(int position) {
    	// TODO Auto-generated method stub
    	 if(position == 0)
    		 return "Settings";
    	 else  if(position == 1)
    		 return "App Lock";
    	 else
    		 return null;
    }
     
     @Override
     public int getCount() {
         return 2;
     }
}
