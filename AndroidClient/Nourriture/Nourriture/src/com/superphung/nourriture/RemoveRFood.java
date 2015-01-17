package com.superphung.nourriture;

import java.util.List;

import model.Ingredient;
import task.WorkerAddRestrictedFood;
import task.WorkerRemoveRestrictedFood;
import adapter.RestrictedFoodAdapter;
import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;

public class RemoveRFood extends Activity {
	private ImageLoader imageLoader;
	private RestrictedFoodAdapter adapteur_autocomplete;
	private List<Ingredient> list_autocomplete;

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
