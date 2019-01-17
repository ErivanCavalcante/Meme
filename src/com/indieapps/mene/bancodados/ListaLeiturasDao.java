package com.indieapps.mene.bancodados;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

public class ListaLeiturasDao extends BaseDaoImpl<ListaLeituras, Integer> 
{
	public ListaLeiturasDao(ConnectionSource connectionSource) throws java.sql.SQLException 
	{
		super(ListaLeituras.class);
		setConnectionSource(connectionSource);
		initialize();
	}
}

