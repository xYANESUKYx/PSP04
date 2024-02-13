package Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**@author yanet
 *Actividad 3.2. El objetivo del ejercicio es crear una aplicación cliente/servidor que permita el envío de ficheros al cliente. Para ello, 
 * el cliente se conectará al servidor por el puerto 1500 y le solicitará el nombre de un fichero del servidor. Si el fichero existe, el 
 * servidor, le enviará el fichero al cliente y éste lo mostrará por pantalla. Si el fichero no existe, el servidor le enviará al cliente 
 * un mensaje de error. Una vez que el cliente ha mostrado el fichero se finalizará la conexión.*/

public class Servidor {
    public static void main(String[] args){
        try {
            //CREAMOS UN ServerSocket QUE ESPERARÁ CONEXIONES DEL PUERTO 1500
            ServerSocket servidor = new ServerSocket(1500);
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