package com.indieapps.mene.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.indieapps.mene.bancodados.ConexaoBanco;
import com.indieapps.mene.bancodados.ListaLeituras;
import com.indieapps.mene.bancodados.ListaLeiturasDao;

import android.content.Context;
import android.content.SharedPreferences;

public class LeituraModel 
{
	public final static int ECONOM_VERDE = 0;
    public final static int ECONOM_AMARELO = 1;
    public final static int ECONOM_VERMELHO = 2;
    public final static int ECONOM_NENHUM = 3;
    
	//Lista com as leituras
	public ArrayList<ListaLeituras> listaLeituras;
	
	public ArrayList<ListaLeituras> getListaLeituras() {
		return listaLeituras;
	}
	
	public void setListaLeituras(ArrayList<ListaLeituras> listaLeituras) {
		this.listaLeituras = listaLeituras;
	}
   
	public void carregaInfoBanco(Context ct) throws SQLException
	{
		//Abre o banco ou cria s nao exitir
		ConexaoBanco con = new ConexaoBanco(ct, "info.db", 2);
		
		//cria o dao pra fazer os updates
		ListaLeiturasDao dao = new ListaLeiturasDao(con.getConnectionSource());
		
		//Se nao tem nada
		if(dao.countOf() == 0)
		{
			//Carrega so a lista
			listaLeituras = new ArrayList<ListaLeituras>();
			
			return;
		}
		
		List<ListaLeituras> banco = dao.queryForAll();
		
		//Carrega so a lista
		listaLeituras = new ArrayList<ListaLeituras>(banco);
		
		con.close();
	}
	
	public void salvaInfoBanco(Context ct) throws SQLException
	{
		//Abre o banco ou cria s nao exitir
		ConexaoBanco con = new ConexaoBanco(ct, "info.db", 2);
				
		//cria o dao pra fazer os updates
		ListaLeiturasDao dao = new ListaLeiturasDao(con.getConnectionSource());
		
		for(ListaLeituras l : listaLeituras)
		{
			dao.createOrUpdate(l);
		}
		
		con.close();
	}
	
	//Adiciona as leituras
    public boolean adicionaLeitura( Context ct, int leitura ) throws SQLException
    {
        if (listaLeituras.size() > 0 && leitura < listaLeituras.get(listaLeituras.size() - 1).getLeitura())
            return false;
        
        //Banco de dados
        ConexaoBanco con = new ConexaoBanco(ct, "info.db", 2);
		ListaLeiturasDao dao = new ListaLeiturasDao(con.getConnectionSource());

		ListaLeituras nova = new ListaLeituras(leitura);;
		
        //Adiciona o maximo de 10 leituras
        if(listaLeituras.size() < 11)
        {
            listaLeituras.add(nova);
            dao.create(nova);
        }
        else
        {
        	dao.delete(listaLeituras.get(0));
            listaLeituras.remove(0);
            listaLeituras.add(nova);
            dao.create(nova);
        }
        
        con.close();
        
        SharedPreferences pref = ct.getSharedPreferences("tabela", Context.MODE_PRIVATE);
        if(pref.getBoolean("modoEcon", false))
        {
        	float MetaMes = pref.getFloat("valorEcon", 0);
        	
        	ValorAtualModel m = new ValorAtualModel();
        	
        	SharedPreferences.Editor e = pref.edit();
        	
        	m.carregaInfoBanco(ct);
        	
        	m.calculaValorAtual();
        	
        	if (m.getValorKwhTotal() < MetaMes / 2)
            {
               e.putInt("banEcon", ECONOM_VERDE);
            }
            else if (m.getValorKwhTotal() > MetaMes / 2 &&
            		m.getValorKwhTotal() < MetaMes)
            {
            	e.putInt("banEcon", ECONOM_AMARELO);
            }
            else if (m.getValorKwhTotal() > MetaMes)
            {
            	e.putInt("banEcon", ECONOM_VERMELHO);
            }
        	
        	e.apply();
        }
        
        return true;
    }
    
    public ArrayList<Integer> pegaListaKwh()
    {
    	ArrayList<Integer> lista = new ArrayList<Integer>();

        int valorBase = 0;
        int valorProximo = 0;
        int valor = 0;
        int i = 0;

        while(true)
        {
            if (listaLeituras.size() - 1 < i + 1)
                break;

            valorBase = listaLeituras.get(i).getLeitura();
            valorProximo = listaLeituras.get(i + 1).getLeitura();

            valor = valorProximo - valorBase;

            lista.add(valor);

            i++;
        }

        return lista;
    }

	public void limpaLista(Context ct) throws SQLException 
	{
		if(listaLeituras.isEmpty())
			return;
		
		//Banco de dados
        ConexaoBanco con = new ConexaoBanco(ct, "info.db", 2);
		ListaLeiturasDao dao = new ListaLeiturasDao(con.getConnectionSource());

		dao.delete(listaLeituras);
		
		listaLeituras.clear();
		
		con.close();
	}
}
