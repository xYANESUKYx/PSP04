package PrimerPSP03;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 /** @author yanet
 * Actividad 3.1. El objetivo del ejercicio es crear una aplicación cliente/servidor que se comunique por el puerto 2000 y realice lo siguiente:
 * El servidor debe generar un número secreto de forma aleatoria entre el 0 al 100. El objetivo de cliente es solicitarle al usuario un número y 
 * enviarlo al servidor hasta que adivine el número secreto. Para ello, el servidor para cada número que le envía el cliente le indicará si es 
 * menor, mayor o es el número secreto del servidor.*/

public class Servidor {
    public static void main(String[] args){
        try {
            //CREAMOS UN ServerSocket QUE ESPERARÁ CONEXIONES DEL PUERTO 2000
            ServerSocket servidor = new ServerSocket(2000);
            //MOSTRAMOS POR PANTALLA UN MENSAJE PARA QUE SE PUEDA VERIFICAR QUE EL SERVIDOR ESTA INICIADO CORRECTAMENTE
            System.out.println("El servidor ha iniciado correctamente.");
            
            //CREAMOS UN BUCLE PARA QUE EL SERVIDOR ESTÉ SIEMPRE A LA ESPERA DE NUEVAS CONEXIONES
            while(true){
                //ACEPTAMOS LAS CONEXIONES ENTRANTES Y SE LA ASIGNAMOS A socket
                Socket socket = servidor.accept();
                //CREAMOS UN NUEVO OBJETO DE LA CLASE Hilo Y LO INICIALIZAMOS PASÁNDOLE EL SOCKET DE LA CONEXIÓN COMO PARÁMETRO
                Hilo hilo = new Hilo(socket);
                //EJECUTAMOS EL OBJETO Hilo CREADO
                hilo.start();
            }
        //SI SE PRODUCE UNA EXCEPCIÓN EL catch LO CAPTURA Y MUESTRA EL ERROR Y EL stack trace    
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
