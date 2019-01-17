package com.indieapps.mene;

import com.indieapps.mene.model.LeituraModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

public class EconomiaActivity extends Activity
{
	@Bind(R.id.edtEconomia)
	EditText edtValor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_economia);
		
		ButterKnife.bind(this);
		
		//cria as preferencias
		SharedPreferences pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		if(pref.getBoolean("modoEcon", false))
		{
			edtValor.setText("" + pref.getFloat("valorEcon", 0));
			edtValor.setEnabled(false);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_economia, menu);
		
		MenuItem eco = menu.findItem(R.id.menuConfirma);
		
		SharedPreferences pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		if(!pref.getBoolean("modoEcon", false))
		{
			eco.setIcon(R.drawable.icone_ok);
		}
		else
		{
			eco.setIcon(R.drawable.icone_cancel);
		}
		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		if(item.getItemId() == R.id.menuConfirma)
		{
			//Adiciona o valor e inicia o modo economia
			//cria as preferencias
			SharedPreferences pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
			
			if(!pref.getBoolean("modoEcon", false))
			{
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("modoEcon", true);
				editor.putFloat("valorEcon", Float.parseFloat(edtValor.getText().toString()));
				editor.putInt("banEcon", LeituraModel.ECONOM_VERDE);
				
				editor.apply();
				
				Toast.makeText(getApplicationContext(), "Modo de Economia Ativo!", Toast.LENGTH_LONG).show();
			}
			else
			{
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean("modoEcon", false);
				editor.putFloat("valorEcon", 0);
				editor.putInt("banEcon", 0);
				
				editor.apply();
				
				Toast.makeText(getApplicationContext(), "Modo de Economia Desativado!", Toast.LENGTH_LONG).show();
			}
			
			
			Intent it = new Intent(this, PrincipalActivity.class);
	        startActivity(it);
	        overridePendingTransition(R.anim.anim_dir_esq, R.anim.anim_hold);
			finish();
			
			return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent it = new Intent(this, PrincipalActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_dir_esq, R.anim.anim_hold);
		finish();
	}
}
