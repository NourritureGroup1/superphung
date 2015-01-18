package com.superphung.nourriture;

import task.WorkerRemoveRestrictedFood;
import android.app.Activity;
import android.os.Bundle;

public class RemoveRFood extends Activity {

	@Override
	protected void onCreate(Bundle saveInstanceState){
		super.onCreate(saveInstanceState);
		
		String listRemainingIngredients = "";
		if (Globals.rfood != null) {
			for (int i = 0; i < Globals.rfood.size();i++) {
				if (Globals.rfood.get(i).getChecked() == false) {
					if (!listRemainingIngredients.equals(""))
						listRemainingIngredients += ",";
					listRemainingIngredients += Globals.rfood.get(i).getId();						
				}
			}
		}
		new WorkerRemoveRestrictedFood(getApplicationContext(), listRemainingIngredients).execute();
		finish();
	}
}
