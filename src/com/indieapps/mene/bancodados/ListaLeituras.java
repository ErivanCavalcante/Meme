package com.indieapps.mene.bancodados;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "leituras")
public class ListaLeituras 
{
	@DatabaseField(generatedId = true, columnName = "_id")
	int _id;
	@DatabaseField
	int leitura;
	
	public ListaLeituras() 
	{
		// TODO Auto-generated constructor stub
	}
	
	public ListaLeituras(int l) 
	{
		leitura = l;
	}
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public int getLeitura() {
		return leitura;
	}
	public void setLeitura(int leitura) {
		this.leitura = leitura;
	}
}
