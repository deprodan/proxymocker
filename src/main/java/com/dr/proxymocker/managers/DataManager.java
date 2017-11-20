package com.dr.proxymocker.managers;

import java.io.InputStream;
import java.util.List;

import com.dr.proxymocker.pojos.ProxyConf;

public class DataManager {

	public String getStoredData(String url) {
		String data = null;

		if (url == "buscar en la base de datos y ver si hay datos en el cache para este servicio") {
			data = "data recuperada desde la base de datos";
		}

		return data;

	}

	public Boolean saveData(String url, byte[] data) {
		
		String doc2 = new String(data);
		// Almacenar la información de la respuesta de esta url en la base de
		// datos.

		return true;

	}

	public List<String> getCacheableHost() {

		return null;

	}
	
	public Boolean isCacheableHost(String host){
		
		List<String> listaHost=getCacheableHost();
		if(listaHost==null)
			return false;
		
		if(listaHost.indexOf(host)!=-1)
			return true;
		
		return false;
		
		
	}
	
	public ProxyConf getProxy(){
		
		ProxyConf prx=new ProxyConf();
		prx.setHost("localhost");
		prx.setPort(3001);
		
		return null;
	}

}
