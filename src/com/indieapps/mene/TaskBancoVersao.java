package com.indieapps.mene;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.google.gson.Gson;
import com.indieapps.mene.bancodados.ComumJson;

import android.os.AsyncTask;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskBancoVersao extends AsyncTask<String, Void, Void>
{
	OkHttpClient client = new OkHttpClient();
	Response rs;
	
	WeakReference<CarregaActivity> activity;
	
	public TaskBancoVersao(CarregaActivity act) 
	{
		activity = new WeakReference<CarregaActivity>(act);
	}
	
	@Override
	protected Void doInBackground(String... p) 
	{
		try 
		{
			//Olha primeio s precisa atualizar
			Request rq = new Request.Builder().url(p[0]).build();
			//faz a chamada
			rs = client.newCall(rq).execute();
		
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) 
	{
		try 
		{
			CarregaActivity act = activity.get();
			
			if(act == null)
				return;
			
			if(rs.body() == null)
			{
				act.erroConexao();
				return;
			}
			
			//Pega a resposta
			String resposta = rs.body().string();
			Gson g = new Gson();
			
			ComumJson[] j = g.fromJson(resposta, ComumJson[].class);
			
			act.setaVersao(j[0]);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}