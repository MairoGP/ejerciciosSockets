package Chat;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args) {
        MarcoServidor mimarco = new MarcoServidor();
        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoServidor extends JFrame implements Runnable{
    private JTextArea areaTexto;
    public MarcoServidor(){
        setBounds(1200,300,280,350);

        JPanel milamina = new JPanel();
        milamina.setLayout(new BorderLayout());

        areaTexto = new JTextArea();

        milamina.add(areaTexto);
        add(milamina);
        setVisible(true);

        //Lanzamos un hilo para que la aplicacion este a la escucha.
        Thread mihilo = new Thread(this);
        mihilo.start();
    }

    @Override
    public void run() {
        //System.out.println("Estoy a la escucha");
        try {
            //Creamos el puerto del servidor.
            ServerSocket servidor = new ServerSocket(9999);
            //Instanciamos variables y el objeto para recoger los datos pasados por el cliente.
            String nick, ip, mensaje;
            PaqueteEnvio datosRecibidos;
            while(true) {
                //La funcion .accept() devuelve un Socket, el socket del cliente que se conecta.
                Socket cliente = servidor.accept();         //Gracias al bucle, la aplicacion esta siempre a la escucha en segundo plano.
                //Establecemos el flujo de datos de entrada del cliente.
                ObjectInputStream paqueteDatosCliente = new ObjectInputStream(cliente.getInputStream());
                //Leemos el objeto que se encontraba en el flujo de entrada del cliente.
                datosRecibidos = (PaqueteEnvio) paqueteDatosCliente.readObject();
                nick = datosRecibidos.getNick();
                ip = datosRecibidos.getIp();
                mensaje = datosRecibidos.getMensaje();

                //Mostramos la informacion recogida en el area de texto del servidor.
                areaTexto.append("\n"+nick+": "+mensaje+" para "+ip);

                //Por ultimo, devolvemos la informacion pasada al destinatario indicado en los datos.
                //Creamos un nuevo socket tipo cliente, ya que nos vamos a conectar al servidor de la aplicacion del cliente, indicando la ip recogida.
                Socket enviaDestinatario = new Socket(ip,9090);
                ObjectOutputStream paqueteReenvio = new ObjectOutputStream(enviaDestinatario.getOutputStream());
                //Escribimos los datos recogidos por el primer cliente para enviarsela al segundo mediante este nuevo flujo.
                paqueteReenvio.writeObject(datosRecibidos);

                //Por ultimo cerramos todos los flujos y sockets.
                paqueteDatosCliente.close();
                paqueteReenvio.close();
                enviaDestinatario.close();
                cliente.close();

                //Asigamos el flujo de datos de entrada del cliente al socket que acabamos de aceptar.
//                DataInputStream entradaDatosCliente = new DataInputStream(cliente.getInputStream());
//                String entradaCliente = entradaDatosCliente.readUTF();
//                if(entradaCliente.toUpperCase().equals("BYE") || entradaCliente.toUpperCase().equals("ADIOS")){
//                    break;
//                }
//                areaTexto.append("\n" + entradaCliente);
//                cliente.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}