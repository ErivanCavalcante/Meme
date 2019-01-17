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
import android.view.View;

public class PrincipalActivity extends Activity
{
	@Override
	protected void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        // Set our view from the "main" layout resource
        setContentView(R.layout.view_principal);
    }

	@Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_principal, menu);
		
		MenuItem eco = menu.findItem(R.id.menuModoEconomiaOn);
		
		//Se a economia estiver ativa
		SharedPreferences pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
        if(pref.getBoolean("modoEcon", false))
        {
        	int cor = pref.getInt("banEcon", LeituraModel.ECONOM_VERDE);
        	
        	if(cor == LeituraModel.ECONOM_VERDE)
        	{
        		eco.setIcon(R.drawable.icone_verde);
        	}
        	else if(cor == LeituraModel.ECONOM_AMARELO)
        	{
        		eco.setIcon(R.drawable.icone_amarelo);
        	}
        	else if(cor == LeituraModel.ECONOM_VERMELHO)
        	{
        		eco.setIcon(R.drawable.icone_vermelho);
        	}
        }
        else
        {
        	eco.setIcon(R.drawable.icone_cinza);
        }
		
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		Intent it;
		
		switch(item.getItemId())
		{
			case R.id.menuModoEconomiaOn:
				it = new Intent(this, EconomiaActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
				finish();
				return true;
				
			case R.id.menuConsumoMedio:
				it = new Intent(this, ConsumoActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
				finish();
				return true;
				
			case R.id.menuSobre:
				it = new Intent(this, SobreActivity.class);
				startActivity(it);
				overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
				finish();
				return true;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
	
	public void clickLeitura(View v)
	{
		Intent it = new Intent(this, LeituraActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
        finish();
	}
	
	public void clickGrafico(View v)
	{
		Intent it = new Intent(this, GraficoActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
        finish();
	}
	
	public void clickConf(View v)
	{
		Intent it = new Intent(this, ConfActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
        finish();
	}
	
	public void clickCreditos(View v)
	{
		Intent it = new Intent(this, CreditosActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
        finish();
	}

}
