package Main;

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
            
            
            
            
            //ESCRIBIMOS EN EL FLUJO DE SALIDA EL MENSAJE PARA SOLICITAR EL USUARIO
            ouputStream.writeUTF("USUARIO:");
            //LEEMOS EN EL FLUJO DE ENTRADA EL USUARIO Y LO ALMACENAMOS EN "usuario"
            String usuario = inputStream.readUTF().trim();
            
            
            //ESCRIBIMOS EN EL FLUJO DE SALIDA EL MENSAJE PARA SOLICITAR LA CONTRASEÑA
            ouputStream.writeUTF("CONTRASEÑA:");
            //LEEMOS EN EL FLUJO DE ENTRADA LA CONTRASEÑA Y LA ALMACENAMOS EN "contrasenia"
            String contrasenia = inputStream.readUTF().trim();
            
            //SI EL USUARIO ES "javier" Y LA CONTRASEÑA ES "secreta"
            if(usuario.equals("javier") && contrasenia.equals("secreta")){
                //ESCRIBIMOS EN EL FLUJO DE SALIDA QUE EL CLIENTE SE HA LOGUEADO CON EXITO
                ouputStream.writeBoolean(true);
                //ESCRIBIMOS EN EL FLUJO DE SALIDA EL MENSAJE CON LAS OPCIONES
                ouputStream.writeUTF("\n1 VER CONTENIDO DE UN ARCHIVO"
                                   + "\n2 VER CONTENIDO DEL DIRECTORIO ACTUAL"
                                   + "\n3 Salir");
                //LEEMOS EN EL FLUJO DE ENTRADA LA OPCIÓN SELECCIONADA Y LA ALMACENAMOS EN "opcion"
                String opcion = inputStream.readUTF().trim();
                //CREAMOS UN BOOLEANO PARA FINALIZAR EL PROGRAMA
                boolean salir = false;
                
                while(!salir){
                   if(opcion.equals("1")){
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
                        //FINALIZAMOS EL PROGRAMA
                        salir = true;
                    }else if(opcion.equals("2")){
                        //CREAMOS UN OBJETO File QUE REPRESENTA LA RUTA ACTUAL
                        File directorioActual = new File("./");
                        //GUARDAMOS EN "contenido" LOS ARCHIVOS Y CARPETAS
                        File[] contenido = directorioActual.listFiles();
                        
                        //ESCRIBIMOS EN EL FLUJO DE SALIDA EL NÚMERO DE ARCHIVOS Y CARPETAS QUE HAY
                        ouputStream.writeInt(contenido.length);
                                
                        //ESCRIBIMOS EN EL FLUJO DE SALIDA UNO A UNO EL NOMBRE DE CADA ARCHIVO Y CARPETA
                        for (int i = 0; i < contenido.length; i++) {
                           ouputStream.writeUTF(contenido[i].getName());
                       } 
                       //FINALIZAMOS EL PROGRAMA
                       salir = true;
                    //SI EL CLIENTE ESCOJE LA OPCIÓN 3 O ESCRIBE CUALQUIER COSA QUE NO SEA
                    //"1" o "2" EL PROGRAMA FINALIZARÁ
                    }else{
                        //FINALIZAMOS EL PROGRAMA
                        salir = true;
                    } 
                }
                
            }else{
                //ESCRIBIMOS EN EL FLUJO DE SALIDA QUE EL CLIENTE NO SE HA LOGUEADO CON EXITO
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
