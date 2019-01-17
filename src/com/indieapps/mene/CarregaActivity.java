package com.indieapps.mene;

import java.util.List;

import com.google.gson.GsonBuilder;
import com.indieapps.mene.bancodados.ComumJson;
import com.indieapps.mene.bancodados.DistribuidoraSqlite;
import com.indieapps.mene.bancodados.RestApi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CarregaActivity extends Activity /*implements TaskOnUpdate*/
{
	SharedPreferences pref; 
	//TaskBancoVersao taskVersao;
	//TaskBancoDist taskDis;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_mene);
		
		if(!testaConectividade())
		{
			finish();
			return;
		}
			
		//cria as preferencias
		pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		criaVariaveis();
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		
		/*
		if(taskVersao != null)
		{
			if(taskVersao.isCancelled() == false)
				taskVersao.cancel(true);
		}
		
		if(taskDis != null)
		{
			if(taskDis.isCancelled() == false)
				taskDis.cancel(true);
		}
		*/
	}
	
	void vaiProximaActivity()
	{
		Intent it = new Intent(this, PrincipalActivity.class);
		startActivity(it);
		overridePendingTransition(R.anim.anim_esq_dir, R.anim.anim_hold);
		finish();
	}
	
	private void criaVariaveis()
	{
		//Se ja existir nao faz nada
		//www.hostinger.com.br
		//erivancavalcantedt@gmail.com
		
		HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
		
		OkHttpClient cliente = new OkHttpClient.Builder().addInterceptor(interceptor).build();
		
		Retrofit retro = new Retrofit.Builder()
							 .baseUrl("http://indieappsbr.esy.es/")
							 .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
							 .client(cliente)
							 .build();
		
		RestApi api = retro.create(RestApi.class);
		
		Call<List<ComumJson>> call = api.pegarDadosComuns();
		
		call.enqueue(new Callback<List<ComumJson>>() 
		{
			
			@Override
			public void onResponse(Call<List<ComumJson>> call, Response<List<ComumJson>> res) 
			{
				int r = res.code();
				
				if(r == 200)
				{
					setaVersao(res.body().get(0));
				}
			}
			
			@Override
			public void onFailure(Call<List<ComumJson>> arg0, Throwable erro) 
			{
				Log.d("HTTP", erro.getMessage());
				erroConexao();
			}
		});
		
		//taskVersao = new TaskBancoVersao(this);
		//taskVersao.execute(new String("http://indieappsbr.esy.es/pega_comum.php"));	
	}
	
	public void setaVersao(ComumJson c)
	{
		//testa a versao
		if(pref.getInt("versao", -1) == c.getVersao())
		{
			vaiProximaActivity();
			return;
		}
		
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt("versao", c.getVersao());
		editor.putInt("banTar", c.getBandeiraTar());
		editor.apply();
		
		Log.d("Debug", "versao = " + c.getVersao());
		
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
		
		call.enqueue(new Callback<List<DistribuidoraSqlite>>() 
		{
			
			@Override
			public void onResponse(Call<List<DistribuidoraSqlite>> call, Response<List<DistribuidoraSqlite>> res) 
			{
				int r = res.code();
				
				if(r == 200)
				{
					OnUpdate(res.body().get(0));
				}
			}
			
			@Override
			public void onFailure(Call<List<DistribuidoraSqlite>> arg0, Throwable erro) 
			{
				Log.d("HTTP", erro.getMessage());
				erroConexao();
			}
		});
		
		//taskDis = new TaskBancoDist(this);
		//taskDis.execute(new String("http://indieappsbr.esy.es/pega_tabela.php?dist=" + pref.getInt("disId", 1) ));	
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
		
		Log.d("Debug Net", "faixa = " + dis.getIcmsFaixaMedio());
		
		editor.apply();
		
		vaiProximaActivity();
	}
	
	boolean testaConectividade()
	{
		//Testa s tem conecao com a net
		Log.d("Debug", "Chamou funcao....");
		
		ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo inf = cm.getActiveNetworkInfo();
		
		if(inf == null)
		{
			/*
			Log.d("Debug", "Nao tem info");
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Erro");
			b.setMessage("Nenhuma coneção com a internet encontrada! \nPor favor conecte-se e volte ao aplicativo.");
			b.setPositiveButton("Ok", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
		
				}
			});
			
			b.create().show();
			*/
			Toast.makeText(getApplicationContext(), "Nenhuma coneção com a internet encontrada!", Toast.LENGTH_LONG).show();
			Log.d("Debug", "Deveria criar a mensagem");
			return false;
		}
		else 
		{
			Log.d("Debug", "Tem info...");
			if(!inf.isConnected())
			{
				Log.d("Debug", "Esta conectado...");
				/*
				AlertDialog.Builder b = new AlertDialog.Builder(this);
				b.setTitle("Erro");
				b.setMessage("Nenhuma coneção com a internet encontrada! \nPor favor conecte-se e volte ao aplicativo.");
				b.setPositiveButton("Ok", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
					}
				});
				
				b.create().show();
				*/
				Toast.makeText(getApplicationContext(), "Nenhuma coneção com a internet encontrada!", Toast.LENGTH_LONG).show();
				return false;
			}
		}
		
		return true;
	}
	
	public void erroConexao()
	{
		//cria as preferencias
		pref = getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		//Testa se eh a primeira vez abrindo o app
		if(pref.getBoolean("primeiraVez", true) == true)
		{	
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			b.setTitle("Erro");
			b.setMessage("Parece que você esta com problemas na sua internet! \nPor favor conecte-se e volte ao aplicativo.");
			b.setPositiveButton("Ok", new OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
				}
			});
			
			b.create().show();
			
			finish();
		}
		else 
		{
			vaiProximaActivity();
		}
	}
}
