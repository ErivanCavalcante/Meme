package com.indieapps.mene;

import java.io.IOException;
import java.lang.ref.WeakReference;

import com.google.gson.Gson;
import com.indieapps.mene.bancodados.DistribuidoraSqlite;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TaskBancoDist extends AsyncTask<String, Void, Void>
{
	DistribuidoraSqlite []dis;
	OkHttpClient client = new OkHttpClient();
	Response rs;
	Gson gson = new Gson();
	ProgressDialog dialog;
	WeakReference<Activity> activity;
	
	public TaskBancoDist(Activity a) 
	{
		if(a instanceof TaskOnUpdate)
		{
			activity = new WeakReference<Activity>(a);
		}
		else
			throw new IllegalArgumentException("A activity deve implementar o TaskOnUpdate");
	}
	
	@Override
	protected void onPreExecute() 
	{
		if(activity.get() != null)
		{
			dialog = new ProgressDialog(activity.get());
			
			dialog.setTitle("Carregando"); 
			dialog.setMessage("Aguarde...");
			dialog.show();
		}
		
		super.onPreExecute();
	}
	
	@Override
	protected Void doInBackground(String... params) 
	{
		try 
		{
			Request rq = new Request.Builder().url(params[0]).build();
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
			String json = rs.body().string();
			
			if(json.isEmpty())
				return;
			
			//Pega a distribuidora
			dis = gson.fromJson(json, DistribuidoraSqlite[].class);
			
			Log.d("Debug Json", json);
			
			if(dialog != null) 
				dialog.dismiss();
			
			if(activity.get() != null)
			{
				TaskOnUpdate update = (TaskOnUpdate)activity.get();
				update.OnUpdate(dis[0]);
			}
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public interface TaskOnUpdate
	{
		void OnUpdate(DistribuidoraSqlite dis);
	}
	
}
