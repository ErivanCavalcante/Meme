package com.indieapps.mene;

import java.util.List;

import com.google.gson.GsonBuilder;
import com.indieapps.mene.bancodados.DistribuidoraSqlite;
import com.indieapps.mene.bancodados.RestApi;
import com.indieapps.mene.model.ValorAtualModel;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConfActivity  extends Activity /*implements TaskOnUpdate*/
{
	@Bind(R.id.spResidencia)
	Spinner spTipoResidencia;
	
	@Bind(R.id.spDistribuidora)
	Spinner spDistribuidora;
	
	@Bind(R.id.spBeneficio)
	Spinner spBeneficio;
	
	//TaskBancoDist taskDis;
	ProgressDialog dialog;
	
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_conf);
		
		ButterKnife.bind(this);
		
		String strRes[] = {"Urbana", "Rural", "BR"};
		String strDis[] = {"Energisa", "Cemar", "Eletrobras Piauí", "Coelba"};
		String strBen[] = {"Sem Beneficio", "Baixa Renda"};
		
		ArrayAdapter<String> adpRes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strRes);
		ArrayAdapter<String> adpDis = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strDis);
		ArrayAdapter<String> adpBen = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, strBen);
		
		spTipoResidencia.setAdapter(adpRes);
		spDistribuidora.setAdapter(adpDis);
		spBeneficio.setAdapter(adpBen);
		
		pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		spTipoResidencia.setSelection(pref.getInt("tipoRes", 0));
		spDistribuidora.setSelection(pref.getInt("disId", 0) - 1);
		spBeneficio.setSelection(pref.getInt("tipoBen", 0));
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
	protected void onDestroy() 
	{
		super.onDestroy();
		
		/*
		if(taskDis != null)
		{
			if(taskDis.isCancelled() == false)
				taskDis.cancel(true);
		}
		*/
	}
	
	@OnItemSelected(R.id.spResidencia)
	public void selecaoResidencia(int posicao)
	{
		switch(posicao)
		{
			case 0:
				pref.edit().putInt("tipoRes", ValorAtualModel.RESI_URBANA).apply();
				break;
			case 1:
				pref.edit().putInt("tipoRes", ValorAtualModel.RESI_RURAL).apply();
				break;
			case 2:
				pref.edit().putInt("tipoRes", ValorAtualModel.RESI_BR).apply();
				break;
		}
	}
	
	@OnItemSelected(R.id.spDistribuidora)
	public void selecaoDistribuidora(int posicao)
	{
		if(pref.getInt("disId", 1) == posicao + 1)
			return;
		
		//Testa s tem conecao com a net
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo inf = cm.getActiveNetworkInfo();
		if(inf == null)
		{
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Erro");
			b.setMessage("Nenhuma coneção com a internet encontrada! \nPor favor conecte-se e volte a executar a função.");
			b.setPositiveButton("Ok", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					spDistribuidora.setSelection(pref.getInt("disId", 0) - 1);
				}
			});
			
			b.create().show();
			
			return;
		}
		else 
		{
			if(!inf.isConnected())
			{
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle("Erro");
				b.setMessage("Nenhuma coneção com a internet encontrada! \nPor favor conecte-se e volte executar a função.");
				b.setPositiveButton("Ok", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						spDistribuidora.setSelection(pref.getInt("disId", 0) - 1);
					}
				});
				
				b.create().show();
				
				return;
			}
		}
		
		//Carrega do banco externo
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient cliente = new OkHttpClient.Builder().addInterceptor(interceptor).build();
		
		Retrofit retro = new Retrofit.Builder()
							 .baseUrl("http://indieappsbr.esy.es/")
							 .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
							 .client(cliente)
							 .build();
		
		RestApi api = retro.create(RestApi.class);
		
		Call<List<DistribuidoraSqlite>> call = api.pegarDadosDistribuidora(pref.getInt("disId", 1));
		
		dialog = new ProgressDialog(this);
		
		dialog.setTitle("Carregando"); 
		dialog.setMessage("Aguarde...");
		dialog.setIndeterminate(true);
		dialog.show();
		
		call.enqueue(new Callback<List<DistribuidoraSqlite>>() 
		{
			
			@Override
			public void onResponse(Call<List<DistribuidoraSqlite>> call, Response<List<DistribuidoraSqlite>> res) 
			{
				int r = res.code();
				
				if(r == 200)
				{
					OnUpdate(res.body().get(0));
					
					dialog.dismiss();
				}
			}
			
			@Override
			public void onFailure(Call<List<DistribuidoraSqlite>> arg0, Throwable erro) 
			{
				Log.d("HTTP", erro.getMessage());
				dialog.dismiss();
				//erroConexao();
			}
		});
		
		//String url = new String("http://indieappsbr.esy.es/pega_tabela.php?dist=" + (posicao + 1));
		
		//Carrega as informaçoes
		//taskDis = new TaskBancoDist(this);
		//taskDis.execute(url);
	}
	
	@OnItemSelected(R.id.spBeneficio)
	public void selecaoBandeira(int posicao)
	{
		SharedPreferences pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		pref.edit().putInt("tipoBen", posicao).apply();
	}

	//@Override
	public void OnUpdate(DistribuidoraSqlite dis) 
	{
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putInt("disId", dis.get_id());
		editor.putString("disNome", dis.getNome());
		editor.putFloat("disKwh30", dis.getKwh30());
		editor.putFloat("disKwh100", dis.getKwh100());
		editor.putFloat("disKwh220", dis.getKwh220());
		editor.putFloat("disKwhMaior220", dis.getKwhMaior220());
		editor.putFloat("disResidencial", dis.getResidencial());
		editor.putFloat("disRural", dis.getRural());
		editor.putFloat("disPis", dis.getPis());
		editor.putFloat("disCofins", dis.getCofins());
		editor.putInt("disIcmsFaixaIsento", dis.getIcmsFaixaIsento());
		editor.putInt("disIcmsFaixaMedio", dis.getIcmsFaixaMedio());
		editor.putInt("disIcmsFaixaAlta", dis.getIcmsFaixaAlta());
		editor.putFloat("disIcmsValorMedio", dis.getIcmsValorMedio());
		editor.putFloat("disIcmsValorAlto", dis.getIcmsValorAlto());
		
		editor.apply();
		
		Log.d( "Debug net", "nome = " + dis.getNome());
		Log.d( "Debug net", "id = " + dis.get_id());
		Log.d("Debug Net", "faixa = " + dis.getIcmsFaixaMedio());
		
		//taskDis.cancel(true);
	}
	
	/*
	@Override
	protected void onSaveInstanceState(Bundle out) 
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(out);
		
		if(taskDis == null)
			out.putBoolean("task_on", false);
		else
		{
			if(taskDis.isCancelled())
				out.putBoolean("task_on", false);
			else
			{
				taskDis.cancel(true);
				out.putBoolean("task_on", true);
			}
		}
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle saved) 
	{
		super.onRestoreInstanceState(saved);
		
		if(saved.getBoolean("task_on") == true)
		{
			String url = new String("http://indieappsbr.esy.es/pega_tabela.php?dist=" + (posicao + 1));
			
			//Carrega as informaçoes
			taskDis = new TaskBancoDist(this);
			taskDis.execute(url);
		}
	}
	*/
}
