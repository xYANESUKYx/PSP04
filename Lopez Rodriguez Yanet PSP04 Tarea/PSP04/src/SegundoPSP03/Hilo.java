package SegundoPSP03;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

 /**@author yanet
 *Actividad 3.2. El objetivo del ejercicio es crear una aplicación cliente/servidor que permita el envío de ficheros al cliente. Para ello, 
 * el cliente se conectará al servidor por el puerto 1500 y le solicitará el nombre de un fichero del servidor. Si el fichero existe, el 
 * servidor, le enviará el fichero al cliente y éste lo mostrará por pantalla. Si el fichero no existe, el servidor le enviará al cliente 
 * un mensaje de error. Una vez que el cliente ha mostrado el fichero se finalizará la conexión.*/

public class Hilo extends Thread{
    //CREAMOS UN NUEVO OBJETO Socket
    private Socket socket;
     
    //CREAMOS EL CONSTRUCTOR DE LA CLASE
    public Hilo(Socket socket){
        this.socket = socket;
    }
    
    //CREAMOS EL MÉTODO RUN 
    @Override
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
            
            //LEEMOS LA RUTA Y LA GUARDAMOS EN SU VARIABLE
            String ruta = inputStream.readUTF();
            //CREAMOS UN OBJETO File USANDO LA VARIABLE ruta
            File file = new File(ruta);
            
            //SI LA RUTA EXISTE EJECUTAMOS EL CÓDIGO
            if(file.exists()){
                //ESCRIBIMOS EN EL FLUJO DE SALIDA QUE LA RUTA SI EXISTE
                ouputStream.writeBoolean(true);
                
                //CREAMOS UN OBJETO BufferedReader QUE LEERÁ EL CONTENIDO DE LA RUTA USANDO FileReader PARA ABRIR EL ARCHIVO
                BufferedReader reader = new BufferedReader(new FileReader(ruta));
                //CREAMOS DOS NUEVOS Strings Y LOS INICIALIZAMOS
                String linea, contenido = "";
                
                //CREAMOS UN BUCLE while QUE LEE LAS LINEAS DE reader HASTA QUE NO HALLAN MÁS
                while((linea = reader.readLine()) != null){
                    //LE AÑADIMOS LA LINEA LEÍDA A contenido JUNTO CON EL CARACTER DE RETORNO \r Y UNA NUEVA LÍNEA \n
                    contenido += linea + "\r\n";
                }
                
                //CERRAMOS OBJETO BufferedReader UTILIZANDO EL MÉTODO "close()"
                reader.close();
                
                //CONVERTIMOS EL CONTENDIDO DEL ARCHIVO EN BYTES
                byte[] contenidoArchivo = contenido.getBytes();
                //ESCRIBIMOS EN EL FLUJO DE SALIDA EL TAMAÑO DEL CONTENIDO
                ouputStream.writeInt(contenidoArchivo.length);
                
                //RECORREMOS CADA ELEMENTO DE contenidoArchivo 
                for (int i = 0; i < contenidoArchivo.length; i++) {
                    //Y ESCRIBIMOS EN EL FLUJO DE SALIDA EL VALOR DE CADA UNO 
                    ouputStream.writeByte(contenidoArchivo[i]);
                }
                
                //CERRAMOS LA CONEXIÓN Y LO COMUNICAMOS EN EL SERVIDOR
                socket.close();
                System.out.println("Cliente desconectado");
             
            //SI LA RUTA NO EXISTE LO ESCRIBIMOS EN EL FLUJO DE SALIDA
            }else{
                ouputStream.writeBoolean(false);
            }
            
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
