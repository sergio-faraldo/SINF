package com.Sergio;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

public class MovieDecoder {
    private HashMap<Integer, SecretKey> keys;//Llaves que tiene el decoder
    private Cipher cipher;

    public MovieDecoder(HashMap<Integer, SecretKey> keys) {
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.keys = keys;
    }

    public void decodeFile(String in, String out) {
        try {
            File input = new File(in);
            File output = new File(out);
            Files.deleteIfExists(output.toPath());
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(input));//Uso BufferedStreams para agilizar la ejecución y para no tener que cargar los archivos completos en memoria
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));

            //Busco en las primeras líneas si tengo una clave con la cual descifrar la clave con la que se cifraron los datos del archivo
            ArrayList<String> encKeys = new ArrayList<>();
            StringBuilder temp = new StringBuilder();
            int c;
            do {//Voy leyendo caracter a caracter hasta cada final de línea
                c = inputStream.read();

                if (c == '\n') {//Cuando llego a final de línea añado la línea entera al arraylist de claves cifradas
                    encKeys.add(temp.toString());
                    temp = new StringBuilder();
                } else {//si el caracter no es final de línea, lo añado al string temp
                    temp.append((char) c);
                }
            } while (!temp.toString().equals("-IV BEGIN-") && c !=-1);//Paro al llegar al EOF o al -IV BEGIN-

            if (!temp.toString().equals("-IV BEGIN-")) {//Si paré de leer en algo que no sea -IV BEGIN-, error
                System.out.println("Error en el archivo. No se encuentra el marcador de comienzo del IV");
                outputStream.close();
                inputStream.close();
                Files.deleteIfExists(output.toPath());
                return;
            }

            //Leo el \n
            inputStream.read();

            //Busco entre las líneas del archivo leídas anteriormente para buscar la clave "maestra" del archivo cifrada con una clave que conozca
            byte[] mainKey = null;
            for (String str :
                    encKeys) {
                if (keys.containsKey(Integer.parseInt(str.split("-", 3)[0]))) {//si coincide el ID con el ID de alguna de mis claves, descifro la clave

                    //ocupa mucho porque lo quise hacer en una línea, pero básicamente pongo la instancia de cipher en modo descifrar y le paso la clave correcta y el IV
                    cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keys.get(Integer.parseInt(str.split("-", 3)[0])).getEncoded(),"AES"),new IvParameterSpec(Base64.getDecoder().decode(str.split("-", 3)[1])));
                    //y aquí descifro
                    mainKey = cipher.doFinal(Base64.getDecoder().decode(str.split("-", 3)[2]));
                }
            }

            if (mainKey == null) {//si mainKey sigue a null, no he encontrado una clave para descifrar
                System.out.println("No se puede descifrar el archivo, no se encuentra una clave compatible");
                outputStream.close();
                inputStream.close();
                Files.deleteIfExists(output.toPath());
                return;
            }

            //leo el IV
            byte[] IV=null;
            temp = new StringBuilder();
            do {
                c = inputStream.read();
                if (c == '\n') {
                    IV=Base64.getDecoder().decode(temp.toString());
                    temp = new StringBuilder();
                } else temp.append((char) c);
            } while (!temp.toString().equals("-DATA BEGIN-") && c != -1);

            if (!temp.toString().equals("-DATA BEGIN-")) {
                System.out.println("Error en el archivo. No se encuentra el marcador de comienzo del IV");
                outputStream.close();
                inputStream.close();
                Files.deleteIfExists(output.toPath());
                return;
            }

            if(IV==null){
                System.out.println("Error en el Vector de Inicialización");
                outputStream.close();
                inputStream.close();
                Files.deleteIfExists(output.toPath());
                return;
            }

            //leo el \n
            inputStream.read();

            //una vez tengo el IV y la clave, la función _decode se encarga del resto
            _decode(inputStream,outputStream,new SecretKeySpec(mainKey, "AES"), new IvParameterSpec(IV));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void _decode(InputStream inputStream, OutputStream outputStream, SecretKey key, IvParameterSpec iv) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key,iv);
            CipherOutputStream out = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[8192];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            out.flush();
            out.close();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
