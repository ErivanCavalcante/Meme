package com.indieapps.mene;

import java.sql.SQLException;
import java.text.DecimalFormat;

import com.indieapps.mene.model.ValorAtualModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ValorAtualActivity extends Activity
{
	@Bind(R.id.txtValor)
	TextView txtValor;
	
	@Bind(R.id.txDist)
	TextView txDist;
	
	@Bind(R.id.txValorSemImp)
	TextView txValorSemImp;
	
	@Bind(R.id.txPisCofins)
	TextView txPisCofins;
	
	@Bind(R.id.txIcms)
	TextView txIcms;
	
	@Bind(R.id.txQtdKwh)
	TextView txQtdKwh;
	
	ValorAtualModel model = new ValorAtualModel();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.view_valor);
		
		ButterKnife.bind(this);
		
		try 
		{
			model.carregaInfoBanco(this);
			
			model.calculaValorAtual();
			
			DecimalFormat format = new DecimalFormat("#####.##");
			
			txQtdKwh.setText("" + model.getKwhTotal() );
			txDist.setText(model.getDis().getNome());
			txValorSemImp.setText("R$ " + format.format( model.getValorKwhSemImp() ));
			txPisCofins.setText("R$ " + format.format( model.getValorPisCofins() ));
			txIcms.setText("R$ " + format.format( model.getValorIcms() ));
			txtValor.setText("R$ " + format.format( model.getValorKwhTotal() ));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() 
	{
		Intent it = new Intent(this, LeituraActivity.class);
        startActivity(it);
        overridePendingTransition(R.anim.anim_dir_esq, R.anim.anim_hold);
		finish();
	}
}
