package com.indieapps.mene.bancodados;

import com.google.gson.annotations.SerializedName;

public class DistribuidoraSqlite 
{
	@SerializedName("id")
	int _id;
	
	@SerializedName("nome")
	String nome;
	
	@SerializedName("kwh_30")
	float kwh30;
	
	@SerializedName("kwh_100")
	float kwh100;
	
	@SerializedName("kwh_220")
	float kwh220;
	
	@SerializedName("kwh_maior_220")
	float kwhMaior220;
	
	@SerializedName("residencial")
	float residencial;
	
	@SerializedName("rural")
	float rural;
	
	@SerializedName("pis")
	float pis;
	
	@SerializedName("cofins")
	float cofins;
	
	@SerializedName("icms_faixa_isento")
	int icmsFaixaIsento;
	
	@SerializedName("icms_faixa_medio")
	int icmsFaixaMedio;
	
	@SerializedName("icms_faixa_alta")
	int icmsFaixaAlta;
	
	@SerializedName("icms_valor_medio")
	float icmsValorMedio;
	
	@SerializedName("icms_valor_alto")
	float icmsValorAlto;
	
	public DistribuidoraSqlite() {
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public float getKwh30() {
		return kwh30;
	}

	public void setKwh30(float kwh30) {
		this.kwh30 = kwh30;
	}

	public float getKwh100() {
		return kwh100;
	}

	public void setKwh100(float kwh100) {
		this.kwh100 = kwh100;
	}

	public float getKwh220() {
		return kwh220;
	}

	public void setKwh220(float kwh220) {
		this.kwh220 = kwh220;
	}

	public float getKwhMaior220() {
		return kwhMaior220;
	}

	public void setKwhMaior220(float kwhMaior220) {
		this.kwhMaior220 = kwhMaior220;
	}

	public float getResidencial() {
		return residencial;
	}

	public void setResidencial(float residencial) {
		this.residencial = residencial;
	}

	public float getRural() {
		return rural;
	}

	public void setRural(float rural) {
		this.rural = rural;
	}

	public float getPis() {
		return pis;
	}

	public void setPis(float pis) {
		this.pis = pis;
	}

	public float getCofins() {
		return cofins;
	}

	public void setCofins(float cofins) {
		this.cofins = cofins;
	}

	public int getIcmsFaixaIsento() {
		return icmsFaixaIsento;
	}

	public void setIcmsFaixaIsento(int icmsFaixaIsento) {
		this.icmsFaixaIsento = icmsFaixaIsento;
	}

	public int getIcmsFaixaMedio() {
		return icmsFaixaMedio;
	}

	public void setIcmsFaixaMedio(int icmsFaixaMedio) {
		this.icmsFaixaMedio = icmsFaixaMedio;
	}

	public int getIcmsFaixaAlta() {
		return icmsFaixaAlta;
	}

	public void setIcmsFaixaAlta(int icmsFaixaAlta) {
		this.icmsFaixaAlta = icmsFaixaAlta;
	}

	public float getIcmsValorMedio() {
		return icmsValorMedio;
	}

	public void setIcmsValorMedio(float icmsValorMedio) {
		this.icmsValorMedio = icmsValorMedio;
	}

	public float getIcmsValorAlto() {
		return icmsValorAlto;
	}

	public void setIcmsValorAlto(float icmsValorAlto) {
		this.icmsValorAlto = icmsValorAlto;
	}

}
