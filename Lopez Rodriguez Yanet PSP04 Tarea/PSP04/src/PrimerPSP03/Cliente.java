package PrimerPSP03;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

 /** @author yanet
 * Actividad 3.1. El objetivo del ejercicio es crear una aplicación cliente/servidor que se comunique por el puerto 2000 y realice lo siguiente:
 * El servidor debe generar un número secreto de forma aleatoria entre el 0 al 100. El objetivo de cliente es solicitarle al usuario un número y 
 * enviarlo al servidor hasta que adivine el número secreto. Para ello, el servidor para cada número que le envía el cliente le indicará si es 
 * menor, mayor o es el número secreto del servidor.*/

public class Cliente {
    public static void main(String[] args){
        try {
            //CREAMOS UN NUEVO Socket Y LO CONECTAMOS AL SERVIDOR EL CUAL SE ENCUENTRA EN "localhost" EN EL PUERTO 2000
            Socket socket = new Socket("localhost", 2000);
            
            //CREAMOS UN NUEVO DataInputStream PARA LEER DATOS DEL Socket
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            //CREAMOS UN NUEVO DataOutputStream PARA ESCRIBIR DATOS DEL Socket
            DataOutputStream ouputStream = new DataOutputStream(socket.getOutputStream());
            
            //CREAMOS UNA VARIABLE LLAMADA salir Y LA INICIALIZAMOS EN false
            boolean salir = false;
         
            //MOSTRAMOS Adivina el número secreto. POR PANTALLA
            System.out.println(inputStream.readUTF());
            
            //CREAMOS UN BUCLE do while EL CUAL SE EJECUTARÁ MIENTRAS salir SEA false 
            do {
                //LEEMOS EL NÚMERO INGRESADO POR CONSOLA Y LO ESCRIBIMOS EN EL Socket
                Scanner scn = new Scanner(System.in);
                ouputStream.writeInt(scn.nextInt());
                //MOSTRAMOS POR PANTALLA SI EL NÚMERO SECRETO ES MAYOR, MENOR O IGUAL QUE EL RECIBIDO
                System.out.println(inputStream.readUTF()); 
                //IGUALAMOS LA VARIABLE SALIR A UN VALOR BOOLEANO DEL Socket QUE SERÁ false HASTA QUE
                //EL NÚMERO SECRETO SEA IGUAL AL NÚMERO RECIBIDO
                salir = inputStream.readBoolean();
            }while(!salir);
            
            //CERRAMOS LA CONEXIÓN
            socket.close();
            
        //SI SE PRODUCE UNA EXCEPCIÓN EL catch LO CAPTURA Y MUESTRA EL ERROR Y EL stack trace   
        }catch(IOException ex){
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
