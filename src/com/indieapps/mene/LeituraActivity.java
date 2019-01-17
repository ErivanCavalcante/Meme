package com.indieapps.mene;

import java.sql.SQLException;

import com.indieapps.mene.model.LeituraModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;

public class LeituraActivity extends Activity
{
	@Bind(R.id.listLeitura)
	ListView lista;
	LeituraAdapter adpLeituras;
	
	@Bind(R.id.edtLeitura)
	EditText edtLeitura;
	
	LeituraModel model = new LeituraModel();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_leitura);
		
		ButterKnife.bind(this);
		
		try 
		{
			model.carregaInfoBanco(getApplicationContext());
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		adpLeituras = new LeituraAdapter(this, R.layout.view_item_leitura, model.getListaLeituras());
		lista.setAdapter(adpLeituras);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		MenuInflater inf = getMenuInflater();
		inf.inflate(R.menu.menu_confirma, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) 
	{
		if(item.getItemId() == R.id.menuConfirma)
		{
			try 
			{
				if( model.adicionaLeitura(this, Integer.parseInt(edtLeitura.getText().toString())) )
				{
					Toast.makeText(this, "Leitura adicionada com sucesso!", Toast.LENGTH_SHORT).show();
					edtLeitura.setText("");
					
					adpLeituras.notifyDataSetChanged();
				}
			} 
			catch (NumberFormatException e) 
			{
				e.printStackTrace();
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else if(item.getItemId() == R.id.menuValor)
		{
			Intent it = new Intent(this, ValorAtualActivity.class);
	        startActivity(it);
	        overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
			finish();
		}
		else if(item.getItemId() == R.id.menuModoFabrica)
		{
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("O que você deseja fazer?");
			b.setMessage("Você realmente deseja limpar a lista?");
			b.setPositiveButton("Ok", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					try 
					{
						model.limpaLista(LeituraActivity.this);
						adpLeituras.notifyDataSetChanged();
					} 
					catch (SQLException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			b.setNegativeButton("Cancela", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{	
				}
			});
			
			b.create().show();
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
	
	@Override
	protected void onSaveInstanceState(Bundle out) 
	{
		out.putInt("valor", Integer.parseInt( edtLeitura.getText().toString() ));
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle saved) 
	{
		edtLeitura.setText(saved.getInt("valor"));
	}
}
