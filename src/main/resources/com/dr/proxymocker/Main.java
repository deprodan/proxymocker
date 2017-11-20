package com.dr.proxymocker;

import java.io.*;
import java.net.*;

import com.dr.proxymocker.managers.*;

public class Main {
	public static void main(String[] args) {
		try {
			if (args.length != 1)
				throw new IllegalArgumentException("insuficient arguments");
			
			int port = Integer.parseInt(args[0]);

			// Print a start-up message
			System.out.println("Starting proxy on localhost:"+port);
			ServerSocket server = new ServerSocket(port);
			
			while (true) {
				new ThreadProxyManager(server.accept());
			}
		} catch (Exception e) {
			System.err.println(e);
			System.err.println("Usage: java ProxyMultiThread " + "<host> <remoteport> <localport>");
		}
	}
}

