package com.dr.proxymocker.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import com.dr.proxymocker.pojos.ioVars;

public class IOManager {
	final byte[] request = new byte[4048];
	final byte[] response = new byte[4048];

	public void consume(InputStream in_in, OutputStream in_out, String in_address, Boolean in_fromClient,
			Socket in_client, Socket in_Server) throws URISyntaxException {

		final InputStream in = in_in;
		final OutputStream out = in_out;
		final Boolean fromClient = in_fromClient;
		final Socket client = in_client;
		final Socket Server = in_Server;
		final DataManager dm = new DataManager();
		final URI url = new URI(in_address);
		new Thread() {
			public void run() {
				int bytes_read;
				try {
					while ((bytes_read = in.read(request)) != -1) {

						Boolean sendInputToOutput = true;

						/*
						 * Si la data viene del cliente, buscamos si la uri es
						 * cacheable y si tiene datos almacenados, si no tiene
						 * datos enviamos al server.
						 */
						if (fromClient && url != null && dm.isCacheableHost(url.getHost())) {
							String cachedData = dm.getStoredData(url.getPath());
							if (cachedData != null) {
								sendInputToOutput = false;
								out.write(cachedData.getBytes());
							}
						}

						/*
						 * Si la data viene del server y la url es cacheable
						 * debemos almacenar la respuesta en la base de datos
						 */
						if (!fromClient && url != null && dm.isCacheableHost(url.getHost())) {
							String cachedData = dm.getStoredData(url.getPath());
							if (cachedData != null) {
								sendInputToOutput = false;
								out.write(cachedData.getBytes());
							}
						}

						if (sendInputToOutput) {
							out.write(request, 0, bytes_read);
						}

						out.flush();
					}
				} catch (IOException e) {
					System.out.println(this.getClass() + "#### Error 59: " + " --- " + e.getLocalizedMessage());
				}
				try {
					if (!fromClient) {
						out.close();
						Server.close();
						client.close();

					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}

}
