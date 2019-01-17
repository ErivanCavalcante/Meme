package com.indieapps.mene;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MeneActivity extends Activity 
{
	SharedPreferences pref; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_mene);
		
		//cria as preferencias
		pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		if(pref.getBoolean("primeiraVez", true) == true)
		{	
			SharedPreferences.Editor editor = pref.edit();
			//Primeira vez q inicia o app
			editor.putBoolean("primeiraVez", false);
			
			//Modo economia de energia
			editor.putBoolean("modoEcon", false);
			editor.putFloat("valorEcon", 0);
			//Bandeira economia
			editor.putInt("banEcon", 0);
			
			//Dados da distribuidora
			editor.putInt("versao", -1);
			editor.putInt("banTar", 0);
			
			editor.putInt("tipoRes", 0);
			editor.putInt("tipoBen", 0);
			
			editor.putInt("disId", 1);
			editor.putString("disNome", "");
			editor.putFloat("disKwh30", 0);
			editor.putFloat("disKwh100", 0);
			editor.putFloat("disKwh220", 0);
			editor.putFloat("disKwhMaior220", 0);
			editor.putFloat("disResidencial", 0);
			editor.putFloat("disRural", 0);
			editor.putFloat("disPis", 0);
			editor.putFloat("disCofins", 0);
			editor.putInt("disIcmsFaixaIsento", 0);
			editor.putInt("disIcmsFaixaMedio", 0);
			editor.putInt("disIcmsFaixaAlta", 0);
			editor.putFloat("disIcmsValorMedio", 0);
			editor.putFloat("disIcmsValorAlto", 0);
			
			editor.apply();
			
			new Timer().schedule(new TimerTask() 
			{
				@Override
				public void run() 
				{
					vaiProximaActivity();
				}
				
			}, 2000);
		}
		else
		{
			vaiProximaActivity();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
	}
	
	void vaiProximaActivity()
	{
		Intent it = new Intent(MeneActivity.this, CarregaActivity.class);
		startActivity(it);
		
		finish();
	}
	
}
