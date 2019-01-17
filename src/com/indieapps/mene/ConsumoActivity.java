package com.indieapps.mene;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ConsumoActivity extends Activity
{
	@Bind(R.id.edtConsumo)
	EditText edtConsumo;
	
	@Bind(R.id.edtHoras)
	EditText edtHoras;
	
	@Bind(R.id.edtDias)
	EditText edtDias;
	
	@Bind(R.id.txConsumoKwh)
	TextView qtdKwh;
	
	@Bind(R.id.txConsumoValor)
	TextView consumoValor;
	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_consumo_medio);
		
		ButterKnife.bind(this);
		
		//cria as preferencias
		pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_consumo_medio, menu);		

		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		Intent it;
		
		switch(item.getItemId())
		{
			case R.id.menuCalcula:
				calculaConsumo();
				return true;
				
			case R.id.menuPrincipal:
				it = new Intent(this, PrincipalActivity.class);
				startActivity(it);
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
		
		super.onBackPressed();
	}
	
	private void calculaConsumo() 
	{
		//Testa se tem algum campo sem nada
		if(edtConsumo.getText().length() == 0 || edtDias.getText().length() == 0 || edtHoras.getText().length() == 0)
		{
			Toast.makeText(this, "Todos os campos devem ser preenchidos.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		int consumo, horas, dias, kwh = 0;
		float valor = 0.00f;
		float valorRes = pref.getFloat("disResidencial", 0.1f);
		
		Log.d("DEbug", "res = " + valorRes);
		
		consumo = Integer.parseInt(edtConsumo.getText().toString());
		horas = Integer.parseInt(edtHoras.getText().toString());
		dias = Integer.parseInt(edtDias.getText().toString());
		
		if(consumo == 0 || horas == 0 || dias == 0)
		{
			Toast.makeText(this, "Reveja os valores digitados.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		float calculo = ((float)consumo / 1000.0000f) * (float)horas * (float)dias;
		kwh = (int)calculo;
		
		valor = kwh * valorRes;
		
		DecimalFormat d = new DecimalFormat("#####.##");
		//Coloca na tela
		qtdKwh.setText("" + kwh);
		consumoValor.setText("R$ " + d.format(valor));
		
	}
	
}
