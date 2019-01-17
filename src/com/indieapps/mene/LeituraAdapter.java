package com.indieapps.mene;

import java.util.ArrayList;

import com.indieapps.mene.bancodados.ListaLeituras;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

public class LeituraAdapter extends ListViewAdapter<ListaLeituras>
{

	public LeituraAdapter(Context ct, int idResource, ArrayList<ListaLeituras> objLista) 
	{
		super(ct, idResource, objLista);
	}

	@Override
	public void onLayoutInflate(View container, int pos) 
	{
		TextView nome = (TextView)container.findViewById(R.id.txtNome);
	 	
		nome.setText("" + lista.get(pos).getLeitura());
	}
	
}
