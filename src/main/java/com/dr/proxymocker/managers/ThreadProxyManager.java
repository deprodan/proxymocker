package com.dr.proxymocker.managers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;

import com.dr.proxymocker.pojos.ProxyConf;
import com.dr.proxymocker.pojos.Request;

public class ThreadProxyManager extends Thread {
	private Socket sClient;

	public ThreadProxyManager(Socket sClient) {

		this.sClient = sClient;
		this.start();
	}

	@Override
	public void run() {
		try {
			final byte[] request = new byte[1024];
			byte[] reply = new byte[4096];
			final InputStream bInFromClient = sClient.getInputStream();

			DataManager dataM = new DataManager();

			final BufferedInputStream inFromClient = new BufferedInputStream(bInFromClient);

			inFromClient.mark(0);
			Request req = Utils.getUrlFromStream(inFromClient);
			inFromClient.reset();

			final OutputStream outToClient = sClient.getOutputStream();
			Socket client = null, server = null;
			// connects a socket to the server

			ProxyConf proxy = dataM.getProxy();

			try {

				if (proxy != null) {
					// server = new Socket(new Proxy(Proxy.Type.SOCKS, new
					// InetSocketAddress(proxy.getHost(), proxy.getPort())));

					req.setHost(proxy.getHost());
					req.setPort(proxy.getPort());
				} else {
					if (req.method.equals("CONNECT")) {
						Utils.getUrlFromStream(inFromClient);

						outToClient.write("HTTP/1.1 200 Connection established\r\nProxy-connection: Keep-alive\r\n\r\n"
								.getBytes());
						outToClient.flush();
					}

				}
				server = new Socket();
				server.connect(new InetSocketAddress(req.getHost(),req.getPort()));
				System.out.println("Conectando a : " + req.toString());

			} catch (UnknownHostException e) {
				outToClient.write("".getBytes());
				outToClient.flush();
				System.out.println("#### Error 68   host:" + req.getHost() + "---" + e.getMessage());
				e.printStackTrace();
				return;
			}

			catch (Exception e) {
				PrintWriter out = new PrintWriter(new OutputStreamWriter(outToClient));
				out.flush();

				System.out.println("#### Error 72   host:" + req.getHost() + "---" + e.getMessage());
				e.printStackTrace();

			}
			// a new thread to manage streams from server to client (DOWNLOAD)

			try {

				InputStream inFromServer = server.getInputStream();
				OutputStream outToServer = server.getOutputStream();

			} catch (Exception e) {
				System.out.println("#### Error 95: " + e.getLocalizedMessage());
			}

			InputStream inFromServer = server.getInputStream();
			OutputStream outToServer = server.getOutputStream();
			// a new thread for uploading to the server

			// verificar url en el cache, si hay cache reponder al cliente

			try {
				new IOManager().consume(inFromClient, outToServer, req.getHost(), true, null, null);
				new IOManager().consume(inFromServer, outToClient, req.getHost(), false, sClient, server);

			} catch (Exception e) {
				System.out.println("#### Error 114: " + e.getLocalizedMessage());
			}
			// } finally {
			// try {
			// if (server != null)
			// server.close();
			// if (client != null)
			// client.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
			// outToClient.close();
			// sClient.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

}
