package Ejercicio1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente {
    public static Socket cliente;
    public static void main(String[] args) {
        MarcoClienteEcho marcoClienteEcho = new MarcoClienteEcho();
        MarcoClienteHora marcoClienteHora = new MarcoClienteHora();
        marcoClienteEcho.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marcoClienteHora.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

class MarcoClienteEcho extends JFrame{
    public MarcoClienteEcho(){
        setBounds(600,300,200,350);
        PanelCliente panel = new PanelCliente();
        add(panel);
        setVisible(true);
    }
}

class PanelCliente extends JPanel{
    private JTextField entrada;
    private JButton btnEnviar;
    PanelCliente(){
        JLabel texto = new JLabel("CLIENTE ECHO");
        add(texto);

        entrada = new JTextField(20);
        add(entrada);

        btnEnviar = new JButton("Enviar");
        btnEnviar.addActionListener(new EnviarTexto());
        add(btnEnviar);
    }

    private class EnviarTexto implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try{
                Socket cliente = new Socket("localhost",9999);
                DataOutputStream flujo_salida_cliente = new DataOutputStream(cliente.getOutputStream());
                flujo_salida_cliente.writeUTF(entrada.getText());
                flujo_salida_cliente.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class MarcoClienteHora extends JFrame{
    private JButton pedirHora;
    MarcoClienteHora(){
        setBounds(850,300,200,350);

        JPanel panel = new JPanel();
        panel.add(new JLabel("CLIENTE HORA"));
        pedirHora = new JButton("Pedir Hora");
        pedirHora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    Socket cliente = new Socket("localhost",9998);
                    DataOutputStream flujo_salida_cliente = new DataOutputStream(cliente.getOutputStream());
                    flujo_salida_cliente.writeInt(1);
                    flujo_salida_cliente.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        panel.add(pedirHora);
        add(panel);
        setVisible(true);
    }
}