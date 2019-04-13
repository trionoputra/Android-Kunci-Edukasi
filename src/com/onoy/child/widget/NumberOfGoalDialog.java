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

public class NumberOfGoalDialog extends DialogFragment {
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),DialogFragment.STYLE_NO_FRAME);
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View v = inflater.inflate(R.layout.view_numberofgoals, null);
		builder.setView(v);
		
		AlertDialog dialog = builder.create();
		
		Button ok = (Button)v.findViewById(R.id.btnSave);
		Button cancel = (Button)v.findViewById(R.id.btnCancel);
		
		ok.setTypeface(Shared.ArialRounded);
		cancel.setTypeface(Shared.ArialRounded);
		
		TextView t2 = (TextView)v.findViewById(R.id.textView2);
		
		t2.setTypeface(Shared.ArialRounded);

		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		
		final EditText number = (EditText)v.findViewById(R.id.editText1);
		number.setText(Shared.read(Constants.SETTING_NUMBER_OF_GOALS, "10"));
		
		number.setTypeface(Shared.ArialRounded);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String nb = number.getText().toString();
				
				if(nb.equals(""))
				{
					Toast.makeText(getActivity(), "field tidak boleh kosong", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(Integer.valueOf(nb) < 0)
				{
					Toast.makeText(getActivity(), "jumlah tidak benar", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(Integer.valueOf(nb) < 1)
				{
					Toast.makeText(getActivity(), "jumlah pertanyaan minimal 1", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(Integer.valueOf(nb) > Integer.valueOf(Shared.read(Constants.SETTING_NUMBER_OF_QUESTION, "10")))
				{
					Toast.makeText(getActivity(), "jumlah goal tidak lebih dari jumlah pertanyaan", Toast.LENGTH_SHORT).show();
					return;
				}
				
				Shared.write(Constants.SETTING_NUMBER_OF_GOALS, nb);
				TextView txtnumber = (TextView)getActivity().findViewById(R.id.txtnumberofgoal);
				txtnumber.setText(nb);
				dismiss();
			}
		});
		
	  	    
		return dialog;
	}
	

}
