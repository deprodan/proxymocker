package com.dr.proxymocker.managers;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.dr.proxymocker.pojos.Request;

public class Utils {

	public static Request getUrlFromStream(InputStream is) {

		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		int cnt = 0;

		Request req = new Request();
		try {
			while ((inputLine = in.readLine()) != null) {
			
				StringTokenizer tok = new StringTokenizer(inputLine);
				tok.nextToken();

				// parse the first line of the request to find the url
				 System.out.println("linea" + cnt + " -> " + inputLine);
				if (cnt == 0) {
					String[] tokens = inputLine.split(" ");

					int port = 80;
					int index = tokens[1].lastIndexOf(":");
					String url = tokens[1].substring(0, index);
					try {
						port = Integer.parseInt(tokens[1].split(":")[1]);
					} catch (Exception e) {
						port = 80;
					}

					if (url.toLowerCase().equals("http")) {
						url = tokens[1];
						port = 80;
					}

					req.setHost(hostFinder(url));
					req.setMethod(tokens[0]);
					req.setPort(port);

				}

				cnt++;
			}
			is.close();
			in.close();
		} catch (Exception e) {

		}

		return req;

	}

	public static String hostFinder(String url) {
		System.out.println("Finding host of :" + url);
		String host = "";
		
		if(!url.toLowerCase().contains("http")){
			return url;
		}
		try {
			URI uri = new URI(url);
			host = uri.getHost();
			
			System.out.println("request host :" + host);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("host not found");
			e.printStackTrace();
		}

		return host;
	}
	
	


}
