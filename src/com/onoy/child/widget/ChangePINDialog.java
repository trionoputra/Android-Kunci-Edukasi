package com.onoy.child.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onoy.child.R;
import com.onoy.child.utils.Constants;
import com.onoy.child.utils.Shared;

public class ChangePINDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),DialogFragment.STYLE_NO_FRAME);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.view_changepin, null);
		builder.setView(v);
		
		
		AlertDialog dialog = builder.create();
		
		Button ok = (Button)v.findViewById(R.id.btnSave);
		Button cancel = (Button)v.findViewById(R.id.btnCancel);
		
		ok.setTypeface(Shared.ArialRounded);
		cancel.setTypeface(Shared.ArialRounded);
		
		TextView t1 = (TextView)v.findViewById(R.id.textView1);
		TextView t2 = (TextView)v.findViewById(R.id.textView2);
		TextView t3 = (TextView)v.findViewById(R.id.textView3);
		TextView t4 = (TextView)v.findViewById(R.id.textView4);
		
		t1.setTypeface(Shared.ArialRounded);
		t2.setTypeface(Shared.ArialRounded);
		t3.setTypeface(Shared.ArialRounded);
		t4.setTypeface(Shared.ArialRounded);
		
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		final EditText txtoldpin = (EditText)v.findViewById(R.id.oldpin);
		final EditText txtnewpin = (EditText)v.findViewById(R.id.newpin);
		final EditText txtconfimpin = (EditText)v.findViewById(R.id.newpinconfim);
		
		txtoldpin.setTypeface(Shared.ArialRounded);
		txtnewpin.setTypeface(Shared.ArialRounded);
		txtconfimpin.setTypeface(Shared.ArialRounded);
		
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String oldpin = txtoldpin.getText().toString();
				String newpin = txtnewpin.getText().toString();
				String confimpin = txtconfimpin.getText().toString();
				
				if(oldpin.equals("")||newpin.equals("")||confimpin.equals(""))
				{
					Toast.makeText(getActivity(), "field tidak boleh kosong", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!newpin.equals(confimpin.toString()))
				{
					Toast.makeText(getActivity(), "kofirmasi PIN tidak sama", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!oldpin.equals(Shared.read(Constants.SETTING_PIN,Constants.DEFAULT_PIN)))
				{
					Toast.makeText(getActivity(), "PIN lama salah", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(newpin.length() < 4)
				{
					Toast.makeText(getActivity(), "PIN minimal 4 digits", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Shared.write(Constants.SETTING_PIN, newpin);
				Toast.makeText(getActivity(), "PIN berhasil diubah", Toast.LENGTH_SHORT).show();
				dismiss();
				
			}
		});
		
	  	    
		return dialog;
	}
	

}
