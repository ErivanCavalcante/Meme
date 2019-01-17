package com.indieapps.mene.bancodados;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApi 
{
	@GET("pega_comum.php")
	Call<List<ComumJson>> pegarDadosComuns();
	
	@GET("pega_tabela.php")
	Call<List<DistribuidoraSqlite>> pegarDadosDistribuidora(@Query("dist") Integer id);
}
