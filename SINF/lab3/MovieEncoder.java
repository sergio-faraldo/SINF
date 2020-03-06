package com.Sergio;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;

public class MovieEncoder {
    private KeyTree ktr;//Árbol de llaves completo
    private Cipher cipher;

    public MovieEncoder(KeyTree ktr) {
        this.ktr = ktr;
        try {
            cipher = Cipher.getInstance("AES/CBC/NoPadding");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void encodeFile(String in, String out) {
        try {
            File input = new File(in);
            File output = new File(out);
            Files.deleteIfExists(output.toPath());
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(input));//Uso BufferedStreams para agilizar la ejecución y para no tener que cargar los archivos completos en memoria
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(output));

            SecretKeySpec mainKey = new SecretKeySpec(KeyGenerator.getInstance("AES").generateKey().getEncoded(), "AES");//Clave con la que se cifrará el archivo original

            ArrayList<Integer> kIDs=ktr.uncompromisedKeys();//Saco los IDs de las claves que no han sido comprometidas
            for (Integer kID :
                    kIDs) {//Cifro la clave del archivo con cada una de las claves no comprometidas e introduzco en el archivo el id de la clave con la que se encriptó la clave, junto con el IV y la clave encriptada en codificación Base64
                cipher.init(Cipher.ENCRYPT_MODE,new SecretKeySpec(ktr.getKey(kID).getEncoded(),"AES"));
                String tmp=kID.toString()+"-"+Base64.getEncoder().encodeToString(cipher.getIV())+"-"+Base64.getEncoder().encodeToString(cipher.doFinal(mainKey.getEncoded()))+"\n";
                outputStream.write(tmp.getBytes());
            }

            //Escribo -IV BEGIN- para marcar que en la siguiente línea va el IV que se usará en el cifrado del archivo
            outputStream.write("-IV BEGIN-\n".getBytes());

            //llamo a _encode, que se encargará del resto
            _encode(inputStream,outputStream,mainKey);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void _encode(InputStream inputStream, OutputStream outputStream, SecretKey key) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getEncoded(),"AES"));
            //inicializo el cipher y añado al archivo el IV que se va a utilizar, codificado en Base64
            outputStream.write(Base64.getEncoder().encodeToString(cipher.getIV()).getBytes());
            //Escribo -DATA BEGIN- para marcar el inicio de los datos del archivo original cifrados
            outputStream.write("\n-DATA BEGIN-\n".getBytes());

            //Cifrado del archivo
            CipherOutputStream out = new CipherOutputStream(outputStream, cipher);
            byte[] buffer = new byte[8192];
            int count;
            while ((count = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            //Finalizo la escritura del archivo
            out.flush();
            out.close();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
