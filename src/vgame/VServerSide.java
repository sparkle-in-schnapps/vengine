/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vgame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author yew_mentzaki
 */
public class VServerSide {

    private class VServerProcessor implements Runnable {

        public VServerProcessor(Socket client, VGame vg, VServerSide vs) {
            this.client = client;
            this.vs = vs;
        }

        public void run() {
            try {

                InputStream inStream = client.getInputStream();
                BufferedReader inputLine = new BufferedReader(new InputStreamReader(inStream));

                while (true) {
                    String stringFromClient = inputLine.readLine();
                    if (stringFromClient.equals("exit")) {
                        break;
                    }
                }
                client.close();
                VServerSide.numberOfOnline--;
                System.out.println("Now there are " + VServerSide.numberOfOnline + " clients online");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        private VGame vg;
        private VServerSide vs;
        private Socket client;
    }

    public VServerSide(final VGame vg) {
        try {
            ServerSocket server = null;
            client = null;
            try {
                server = new ServerSocket(12126);
                System.out.println("Waiting...");
                numberOfOnline = 0;
                // Сервер ждет подключения клиентов в бесконечном цикле и каждому подключившемуся клиенту создает  
                // свой поток, что позволяет подключаться к серверу более чем 1му потоку одновременно  
                while (true) {
                    client = server.accept(); //Ожидает подключение клиента  
                    numberOfOnline++; // Увеличивается счетчик активных клиентов  
                    System.out.println("One more client has been connected");

                    Runnable r = new VServerProcessor(client, vg, this);
                    Thread t = new Thread(r);
                    t.start();
                }
            } //Закрываем сокеты  
            finally {
                client.close();
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Socket client;
    static int numberOfOnline;
}
