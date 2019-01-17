package com.indieapps.mene.bancodados;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ConexaoBanco extends OrmLiteSqliteOpenHelper
{
	public ConexaoBanco(Context context, String databaseName, int databaseVersion) 
	{
		super(context, databaseName, null, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource cs)
	{
		try 
		{
			TableUtils.createTableIfNotExists(cs, ListaLeituras.class);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource cs, int old, int ne) 
	{
		try 
		{
			TableUtils.dropTable(cs, ListaLeituras.class, true);
			
			onCreate(db, cs);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

}
