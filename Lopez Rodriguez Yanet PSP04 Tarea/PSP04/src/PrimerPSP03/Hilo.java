package PrimerPSP03;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

 /** @author yanet
 * Actividad 3.1. El objetivo del ejercicio es crear una aplicación cliente/servidor que se comunique por el puerto 2000 y realice lo siguiente:
 * El servidor debe generar un número secreto de forma aleatoria entre el 0 al 100. El objetivo de cliente es solicitarle al usuario un número y 
 * enviarlo al servidor hasta que adivine el número secreto. Para ello, el servidor para cada número que le envía el cliente le indicará si es 
 * menor, mayor o es el número secreto del servidor.*/

public class Hilo extends Thread{
    //CREAMOS UN NUEVO OBJETO Socket
    private Socket socket;
     
    //CREAMOS EL CONSTRUCTOR DE LA CLASE
    public Hilo(Socket socket){
        this.socket = socket;
    }
    
    //CREAMOS EL MÉTODO RUN 
    public void run(){
        //MOSTRAMOS POR PANTALLA EN EL SERVIDOR UN MENSAJE PARA VERIFICAR QUE LOS CLIENTES INICIAN CORRECTAMENTE
        System.out.println("El cliente se ha conectado correctamente");
           
        //CREAMOS LA INSTANCIA PARA LEER LOS DATOS DEL SOCKET
        DataInputStream inputStream = null;
        //CREAMOS LA INSTANCIA PARA ESCRIBIR LOS DATOS EN EL SOCKET
        DataOutputStream ouputStream = null;
        
        try {   
            //ESPECIFICAMOS EL SOCKET PARA LEER SU FLUJO DE ENTRADA
            inputStream = new DataInputStream(socket.getInputStream());
            //ESPECIFICAMOS EL SOCKET PARA LEER SU FLUJO DE SALIDA
            ouputStream = new DataOutputStream(socket.getOutputStream());
            
            //CREAMOS UN NUEVO ENTERO PARA ALMACENAR EN EL EL NÚMERO RECIBIDO Y LO INICIALIZAMOS 
            int numeroRecibido = 0;
            
            //INSTANCIAMOS LA CLASE RANDOM 
            Random random = new Random();
            //CREAMOS EL ENTERO numeroSecreto ASIGNANDOLE UN NÚMERO ALEATÓRIO GENERADO POR LA INSTANCIA
            int numeroSecreto = random.nextInt(101);
            
            //MOSTRAMOS EN EL SERVIDOR EL NÚMERO SECRETO
            System.out.println("Numero secreto: " + numeroSecreto);
            
            //MOSTRAMOS AL CLIENTE EL MENSAJE "Adivina el número secreto."
            ouputStream.writeUTF("Adivina el número secreto.");
            
            //CREAMOS UN BUCLE do while EL CUAL SE EJECUTARÁ MIENTRAS EL NÚMERO RECIBIDO SEA DISTINTO AL SECRETO
            do{
                //GUARDAMOS EN SU VARIABLE EL NÚMERO RECIBIDO
                numeroRecibido = inputStream.readInt();

                //MOSTRAMOS EN EL SERVIDOR EL NÚMERO RECIBIDO
                System.out.println("Número recibido: " + numeroRecibido);

                //SI EL NÚMERO RECIBIDO ES IGUAL AL NÚMERO SECRETO MOSTRAMOS "Has ganado!!" AL CLIENTE
                if(numeroRecibido == numeroSecreto){
                    ouputStream.writeUTF("Has ganado!!");
                //SI EL NÚMERO RECIBIDO ES MAYOR AL NÚMERO SECRETO MOSTRAMOS "El número secreto es menor"
                } else if(numeroRecibido > numeroSecreto){
                    ouputStream.writeUTF("El número secreto es menor\n");
                //SI EL NÚMERO RECIBIDO ES MENOR AL NÚMERO SECRETO MOSTRAMOS "El número secreto es mayor"
                } else{
                    ouputStream.writeUTF("El número secreto es mayor\n");
                }  
                //ESCRIBIMOS EN EL FLUJO DE SALIDA UN BOOLEANO QUE NOS DIRÁ SI EL NÚMERO SECRETO Y EL RECIBIDO SON IGUALES O NO 
                ouputStream.writeBoolean(numeroRecibido == numeroSecreto);
            }while(numeroRecibido != numeroSecreto);
                    
            //CERRAMOS LA CONEXIÓN Y LO COMUNICAMOS EN EL SERIDOR
            socket.close();
            System.out.println("Cliente desconectado");
            
        //SI SE PRODUCE UNA EXCEPCIÓN EL catch LO CAPTURA Y MUESTRA EL ERROR Y EL stack trace     
        } catch (IOException ex) {
            Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                //CERRAMOS LOS FLUJOS DE ENTRADA Y SALIDA DE LOS DATOS
                inputStream.close();
                ouputStream.close();
            //SI SE PRODUCE UNA EXCEPCIÓN EL catch LO CAPTURA Y MUESTRA EL ERROR Y EL stack trace   
            } catch (IOException ex) {
                Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
