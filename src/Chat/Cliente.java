package Chat;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {

        MarcoCliente mimarco = new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoCliente extends JFrame{
    public MarcoCliente(){
        setBounds(600,300,280,350);

        LaminaCliente milamina = new LaminaCliente();

        add(milamina);
        setVisible(true);
    }
}


class LaminaCliente extends JPanel implements Runnable{
    private JTextField campo1;
    private JTextField campoNick;
    private JTextField campoIp;
    private JButton btnEnviar;
    private JTextArea campoChat;

    public LaminaCliente() {

        campoNick = new JTextField(5);
        add(campoNick);

        JLabel texto = new JLabel("-CHAT-");
        add(texto);

        campoIp = new JTextField(8);
        add(campoIp);

        campoChat = new JTextArea(12,20);
        add(campoChat);

        campo1 = new JTextField(20);
        add(campo1);

        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new EnviarTexto());
        add(btnEnviar);

        Thread mihilo = new Thread(this);
        mihilo.start();
//
//        btnHora = new JButton("Pedir Hora");
//        btnHora.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent actionEvent) {
//                try {
//                    Socket clienteHora = new Socket("localhost",9998);
//                    DataOutputStream salidaDatosCliente = new DataOutputStream(clienteHora.getOutputStream());
//                    salidaDatosCliente.writeUTF("1");
//                    salidaDatosCliente.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        add(btnHora);
    }
    @Override
    public void run() {
        try{
            //Hacemos que la aplicacion del cliente este a la escucha
            ServerSocket servidorCliente = new ServerSocket(9090);
            Socket cliente;
            PaqueteEnvio paqueteRecibido;

            while(true){
                cliente = servidorCliente.accept();
                ObjectInputStream flujo_entrada = new ObjectInputStream(cliente.getInputStream());
                paqueteRecibido = (PaqueteEnvio) flujo_entrada.readObject();

                campoChat.append("\n"+paqueteRecibido.getNick()+": "+paqueteRecibido.getMensaje()+".");
            }

        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private class EnviarTexto implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                //Establece una comunicacion con el host "ip", mediante el puerto 9999;
                Socket cliente = new Socket("192.168.1.124",9999);

                //Recogemos los datos escritos
                PaqueteEnvio datos = new PaqueteEnvio();
                datos.setNick(campoNick.getText());
                datos.setIp(campoIp.getText());
                datos.setMensaje(campo1.getText());

                //Creacion de flujo para enviar los datos al destinatario
                ObjectOutputStream paqueteDatos = new ObjectOutputStream(cliente.getOutputStream());
                //Escribimos en el flujo de informacion el objeto (los datos recogidos).
                paqueteDatos.writeObject(datos);
                cliente.close();

                //Construimos el flujo de datos de salida del cliente, y este flujo va a circular por el socket cliente.
//                DataOutputStream salidaDatosCliente = new DataOutputStream(cliente.getOutputStream());
                //Escribe en el flujo, el texto del campo 1
//                salidaDatosCliente.writeUTF(campo1.getText());
//                salidaDatosCliente.close();

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}

class PaqueteEnvio implements Serializable {
    private String nick;
    private String ip;
    private String mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}