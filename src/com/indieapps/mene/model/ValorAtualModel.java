package com.indieapps.mene.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//import com.indieapps.mene.bancodados.BancoTabela;
import com.indieapps.mene.bancodados.ConexaoBanco;
import com.indieapps.mene.bancodados.DistribuidoraSqlite;
import com.indieapps.mene.bancodados.ListaLeituras;
import com.indieapps.mene.bancodados.ListaLeiturasDao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class ValorAtualModel 
{
	//Baneiras
    public final static int BANDEIRA_VERDE = 0;
    public final static int BANDEIRA_AMARELO = 1;
    public final static int BANDEIRA_VERMELHO_1 = 2;
    public final static int BANDEIRA_VERMELHO_2 = 3;
    
    public final static int RESI_URBANA = 0;
    public final static int RESI_BR = 2;
    public final static int RESI_RURAL = 1;
    
    
    public final static int BENEFICIO_NENHUM = 0;
    public final static int BENEFICIO_RENDA_BAIXA = 1;
    
    
	//Valore e taxas
    int tipoResidencia = 0;
    int tipoBeneficio = 0;
    int bandeiraTar = 0;
    int kwhTotal = 0; //valor dos kwh por mes
    float valorKwhSemImp = 0.0000f;
    float valorKwhTotal = 0.0000f; //valor final do a ser pago
    float valorPisCofins = 0.0000f; //valor da base para os calculos
    float valorIcms = 0.0000f; //valor da base para os calculos
    
    DistribuidoraSqlite dis;

    //Lista com as leituras
    //public BancoTabela banco;
    ArrayList<ListaLeituras> listaLeituras;

    
	public float getValorKwhTotal() {
		return valorKwhTotal;
	}
	
	public void setValorKwhTotal(float valorKwhTotal) {
		this.valorKwhTotal = valorKwhTotal;
	}
	
	public int getKwhTotal() {
		return kwhTotal;
	}

	public void setKwhTotal(int kwhTotal) {
		this.kwhTotal = kwhTotal;
	}

	public float getValorPisCofins() {
		return valorPisCofins;
	}

	public void setValorPisCofins(float valorPisCofins) {
		this.valorPisCofins = valorPisCofins;
	}

	public float getValorIcms() {
		return valorIcms;
	}

	public void setValorIcms(float valorIcms) {
		this.valorIcms = valorIcms;
	}
	
	public float getValorKwhSemImp() {
		return valorKwhSemImp;
	}

	public void setValorKwhSemImp(float valorKwhSemImp) {
		this.valorKwhSemImp = valorKwhSemImp;
	}

	public DistribuidoraSqlite getDis() {
		return dis;
	}

	public void setDis(DistribuidoraSqlite dis) {
		this.dis = dis;
	}

	public void carregaInfoBanco(Context ct) throws SQLException
	{
		//Pega a lista de leituras
		ConexaoBanco con = new ConexaoBanco(ct, "info.db", 2);
		ListaLeiturasDao dao = new ListaLeiturasDao(con.getConnectionSource());
		
		SharedPreferences pref = ct.getSharedPreferences("tabela", Context.MODE_PRIVATE);
		
		dis = new DistribuidoraSqlite();
		
		dis.set_id(pref.getInt("disId", 1));
		dis.setNome(pref.getString("disNome", "Energisa"));
		dis.setKwh30(pref.getFloat("disKwh30", 0));
		dis.setKwh100(pref.getFloat("disKwh100", 0));
		dis.setKwh220(pref.getFloat("disKwh220", 0));
		dis.setKwhMaior220(pref.getFloat("disKwhMaior220", 0));
		dis.setResidencial(pref.getFloat("disResidencial", 0));
		dis.setRural(pref.getFloat("disRural", 0));
		dis.setPis(pref.getFloat("disPis", 0));
		dis.setCofins(pref.getFloat("disCofins", 0));
		dis.setIcmsFaixaIsento(pref.getInt("disIcmsFaixaIsento", 1));
		dis.setIcmsFaixaMedio(pref.getInt("disIcmsFaixaMedio", 1));
		dis.setIcmsFaixaAlta(pref.getInt("disIcmsFaixaAlta", 1));
		dis.setIcmsValorMedio(pref.getFloat("disIcmsValorMedio", 0));
		dis.setIcmsValorAlto(pref.getFloat("disIcmsValorAlto", 0));
		
		tipoResidencia = pref.getInt("tipoRes", RESI_URBANA);
		tipoBeneficio = pref.getInt("tipoBen", BENEFICIO_NENHUM);
		bandeiraTar = pref.getInt("banTar", BENEFICIO_NENHUM);
		
		List<ListaLeituras> b = dao.queryForAll();
		listaLeituras = new ArrayList<ListaLeituras>(b);
		
		con.close();
	}
	
	//Calcula quantos kwh foram gastos nesse mes
	boolean calculaQtdKwhMes()
    {
        if (listaLeituras.size() < 2)
            return false;

        int ultimaLeitura = listaLeituras.get(listaLeituras.size() - 1).getLeitura();
        int penultimaLeitura = listaLeituras.get(listaLeituras.size() - 2).getLeitura();

        kwhTotal = (ultimaLeitura - penultimaLeitura < 0) ? 0 : ultimaLeitura - penultimaLeitura;
        
        return true;
    }
	
	//Calcular o valor totoal do consumo
	public void calculaValorAtual()
	{
		//Calcula a qtd d Kwh
        if(!calculaQtdKwhMes())
            return;
             
        //Calcula o valor de acordo com a distribuidora
        valorKwhSemImp = calculaValorKwhMes(kwhTotal);
        
        //Calcula as bandeiras
        float valorBandeira = calculaBandeiras();
        
        //Calcula o valor do consumo + bandeiras + subsidio
        float valor = valorKwhSemImp + valorBandeira /*+ subsidio*/;
        
        //Valor final
        valorKwhTotal = calculaImposto(valor);
        
        //Calcula iluminação publica
	}
	
	//Calculando tudo certo
	public float calculaValorKwhMes(int qtdKhw)
	{
		float valor = 0.00000f;
		
		if (tipoResidencia == RESI_URBANA)
		{
			if(tipoBeneficio == BENEFICIO_RENDA_BAIXA)
			{
				if(qtdKhw < 31)
	            {
	          
					valor = dis.getKwh30() * qtdKhw;
	            }
	            else if (qtdKhw > 30 && qtdKhw < 101)
	            {
	            	
	            	valor = dis.getKwh30() * 30;
	            	
	            	valor += dis.getKwh100() * (qtdKhw - 30);
	            }
	            else if (qtdKhw > 100 && qtdKhw < 221)
	            {
	            	
	            	valor = dis.getKwh30() * 30;
	            	
	            	valor += dis.getKwh100() * 70;
	                
	            	valor += dis.getKwh220() * (qtdKhw - 100);
	            }
	            else if (qtdKhw > 220)
	            {
	            	
	            	valor = dis.getKwh30() * 30;
	            	
	            	valor += dis.getKwh100() * 70;
	                
	            	valor += dis.getKwh220() * 120;
	                
	            	valor += dis.getKwhMaior220() * (qtdKhw - 220);
	            }
			}
			else 
			{
				valor = dis.getResidencial() * qtdKhw;
			}
		}
        else if (tipoResidencia == RESI_BR)
        {
            Log.d("Debug Valor", "Valor desconhecido");
        }
        else if(tipoResidencia == RESI_RURAL)
            valor = dis.getRural() * qtdKhw;
		
		
		Log.d("Debug Valor", "Valor = " + valor);
		return valor;
	}
	
	//Calcula e retorna o valor das bandeiras
	//Tudo ok
	float calculaBandeiras()
    {
        float valorFinal = 0.000f;
        
        if(bandeiraTar == BANDEIRA_AMARELO)
        {
            valorFinal = kwhTotal * 0.015f;
        }
        else if(bandeiraTar == BANDEIRA_VERMELHO_1)
        {
            valorFinal = kwhTotal * 0.030f;
        }
        else if (bandeiraTar == BANDEIRA_VERMELHO_2)
        {
            valorFinal = kwhTotal * 0.045f;
        }
   
        return valorFinal;
    }
    
	//Calcula os impostos ICMS pis cofins
    public float calculaImposto(float valorBaseCalculo)
    {
    	float icms = 0.00000f;
    	float icmsPer = 0.00000f;
    	float pis = 0.00000f;
    	float cofins = 0.000000f;
    	
    	//Valores para os calculos
    	float deflator = 0.0000f;
    	
    	//Pega o icms pela faixa
		if(kwhTotal > dis.getIcmsFaixaIsento() && kwhTotal <= dis.getIcmsFaixaAlta())
			icmsPer = dis.getIcmsValorMedio();
		else if(kwhTotal >= dis.getIcmsFaixaAlta())
			icmsPer = dis.getIcmsValorAlto();
		
		//calcula deflator
		
		deflator = 100.0000f - (dis.getPis() + dis.getCofins() + icmsPer);
		
		//Calcula icms
		icms = (valorBaseCalculo / deflator) * icmsPer;
		
		//Calcula pis
		pis = (valorBaseCalculo / deflator) * dis.getPis();
		
		//Calcular cofins
		cofins = (valorBaseCalculo / deflator) * dis.getCofins();
		
		//Seta os valores
		valorIcms = icms;
		valorPisCofins = pis + cofins;
		
		//Logs
		Log.d("Debug Valor", "Calculo dos juros");
		Log.d("Debug Valor", "faixa = " + dis.getIcmsValorMedio() + " faixa = " + dis.getIcmsValorAlto() );
		Log.d("Debug Valor", "Base calculo = " + valorBaseCalculo );
		Log.d("Debug Valor", "Deflator = " + deflator );
		Log.d("Debug Valor", "Dis Valores = " + dis.getPis() );
		Log.d("Debug Valor", "Dis Valores = " + dis.getCofins() );
		Log.d("Debug Valor", "Dis Valores = " + icmsPer );
		Log.d("Debug Valor", "Valor kwh = " + kwhTotal );
		Log.d("Debug Valor", "Valor icms = " + icms );
		Log.d("Debug Valor", "Valor pis = " + pis );
		Log.d("Debug Valor", "Valor cofins = " + cofins );
		Log.d("Debug Valor", "Valor imposto = " + (pis + cofins + icms) );
		Log.d("Debug Valor", "Valor total = " + (valorBaseCalculo + pis + cofins + icms) );
		
    	return (valorBaseCalculo + pis + cofins + icms);
    }
}
