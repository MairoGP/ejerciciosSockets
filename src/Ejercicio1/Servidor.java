package Ejercicio1;

import javax.swing.*;
import java.awt.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

public class Servidor {
    public static void main(String[] args) {
        MarcoServidor main = new MarcoServidor();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}


class MarcoServidor extends JFrame implements Runnable{
    public static JTextArea areaTexto;
    MarcoServidor(){
        setBounds(1200,300,280,350);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        areaTexto = new JTextArea();
        panel.add(areaTexto);
        add(panel);
        setVisible(true);

        Thread hilo = new Thread(this);
        hilo.start();
    }

    @Override
    public void run() {
        ServerSocket servidorEcho = null;
        ServerSocket servidorHora = null;
        Socket clienteEcho = null;
        Socket clienteHora = null;
        try{
            servidorEcho = new ServerSocket(9999);
            servidorHora = new ServerSocket(9998);
            servidorEcho.setSoTimeout(1000);
            servidorHora.setSoTimeout(1000);
                do{
                    try{
                        clienteEcho = servidorEcho.accept();
                        new GestionEcho(clienteEcho).start();
                        clienteEcho = null;
                        break;
                    }catch (SocketTimeoutException e){}
                    try{
                        clienteHora = servidorHora.accept();
                        new GestionHora(clienteHora).start();
                        clienteHora = null;
                        break;
                    }catch (SocketTimeoutException e){}
                }while(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class GestionEcho extends Thread{
    private Socket cliente;
    GestionEcho(Socket s){
        cliente = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream flujo_entrada_cliente = new DataInputStream(cliente.getInputStream());
            String mensajeEcho = flujo_entrada_cliente.readUTF();

            if(mensajeEcho.toUpperCase().equals("BYE")){
                flujo_entrada_cliente.close();
                cliente.close();
                return;
            }else{
                MarcoServidor.areaTexto.append("\n"+mensajeEcho);
                cliente.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

class GestionHora extends Thread{
    private Socket cliente;
    GestionHora(Socket s){
        cliente = s;
    }

    @Override
    public void run() {
        try {
            DataInputStream flujo_entrada_cliente_hora = new DataInputStream(cliente.getInputStream());
            int devolverHora = flujo_entrada_cliente_hora.readInt();
            Date fecha = new Date();
            if (devolverHora == 1) {
                JOptionPane.showMessageDialog(null,fecha.toString());
            } else {
                flujo_entrada_cliente_hora.close();
                cliente.close();
                return;
            }
            flujo_entrada_cliente_hora.close();
            cliente.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
