package com.indieapps.mene;

import java.sql.SQLException;
import java.util.ArrayList;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.indieapps.mene.model.LeituraModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import butterknife.Bind;
import butterknife.ButterKnife;

public class GraficoActivity extends Activity
{
	@Bind(R.id.chart)
	LineChart chart;
	
	LeituraModel model = new LeituraModel();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_graficos);
		
		ButterKnife.bind(this);
		
		try 
		{
			//Carrega a lista
			model.carregaInfoBanco(this);
			
			ArrayList<Entry> entries = new ArrayList<Entry>();
			ArrayList<Integer> li =  model.pegaListaKwh();
			for(int i = 0; i < li.size(); ++i)
			{
				entries.add(new Entry(li.get(i), i));
			}
			
			LineDataSet dataset = new LineDataSet(entries, "# Kwh");
			dataset.setDrawFilled(true);
			
			ArrayList<String> str = new ArrayList<String>();
			str.add("1");
			str.add("2");
			str.add("3");
			str.add("4");
			str.add("5");
			str.add("6");
			str.add("7");
			str.add("8");
			str.add("9");
			str.add("10");
			
			LineData data = new LineData(str, dataset);
			
			chart.setData(data);
			//Seta o titulo
			chart.setDescription("Evolução do Consumo");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
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
