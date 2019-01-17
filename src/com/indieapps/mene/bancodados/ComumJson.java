package com.indieapps.mene.bancodados;

import com.google.gson.annotations.SerializedName;

public class ComumJson 
{
	@SerializedName("bandeira")
	int bandeiraTar;
	@SerializedName("versao")
	int versao;
	
	
	public int getBandeiraTar() {
		return bandeiraTar;
	}
	public void setBandeiraTar(int bandeiraTar) {
		this.bandeiraTar = bandeiraTar;
	}
	public int getVersao() {
		return versao;
	}
	public void setVersao(int versao) {
		this.versao = versao;
	}
	
}
