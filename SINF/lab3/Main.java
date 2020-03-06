package com.Sergio;

public class Main {

    /*
    Estructura del fichero generado por MovieEncoder
----------------------------------------------------------------
    [KeyID1]-[IV1]-[Clave del archivo encriptada con KeyID1]
    [KeyID2]-[IV2]-[Clave del archivo encriptada con KeyID2]
    ...
    -IV BEGIN-
    [IV del archivo]
    -DATA BEGIN-
    [Archivo encriptado con la clave del archivo]
----------------------------------------------------------------

    En las primeras líneas, los IV y las claves van codificadas en Base64 para evitar problemas con los caracteres separadores.
    El IV del archivo también va codificado en Base64.
    Los datos que van a partir del -DATA BEGIN- no van en Base64, dado que a partir de ese momento, se leerá el archivo hasta el EOF


    Ejemplo de archivo:
----------------------------------------------------------------
    3-kg9AIJaLvESI90V1CqFkew==-fAW0/nRQihotq7PbL5pdsw==
    11-afj+BU+zC55WA91VqCUGkA==-0f3xaj7zRvfwfHciKNX0bg==
    4-Xy6DspghfhrH5xQx6SIciw==-tIANCtL0bF5lyfoiUNENZQ==
    -IV BEGIN-
    1kOVIm6ZbLBxPhq9CtSrXQ==
    -DATA BEGIN-
    A{Ú-☺'{¿v0HÁ(ºqÊ»wÀT«.gx8Y]ß7©
                              [...]
----------------------------------------------------------------
     */

    public static void main(String[] args) {
        //Creo un árbol para 8 dispositivos
        KeyTree tr = new KeyTree(8);//Dispositivos del 1 al 8

        //tr.addCompromisedDevice(1);//añadir el dispositivo 1 a la lista de comprometidos
        tr.addCompromisedDevice(3);//añadir el dispositivo 1 a la lista de comprometidos

        //Creo un "reproductor", un decoder, con id 1 y un encoder
        MovieDecoder player = new MovieDecoder(tr.keysForDevice(1));
        MovieEncoder encoder = new MovieEncoder(tr);

        //Pruebo a cifrar y descifrar un archivo
        encoder.encodeFile("./test.mp4","./test.mp4.encoded");
        player.decodeFile("./test.mp4.encoded","./test-1.mp4");



        //---------------------------------------------------------------
        //Código para comprobar el funcionamiento del árbol de claves
        //---------------------------------------------------------------

        /*
        for (int i = 1; i <= 8; i++) {
            ArrayList<Integer> claves = tr.keyIDsForDevice(i);
            System.out.print(i + ": ");
            for (Integer j :
                    claves) {
                System.out.print(j + " ");
            }
            System.out.println();
        }
        System.out.println("-----------------");
        ArrayList<Integer> keys = tr.uncompromisedKeys();
        for (Integer i :
                keys) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.println("-----------------");
        tr.addCompromisedDevice(1);
        tr.addCompromisedDevice(2);
        tr.addCompromisedDevice(3);
        tr.addCompromisedDevice(4);
        tr.addCompromisedDevice(5);
        tr.addCompromisedDevice(6);
        tr.addCompromisedDevice(7);
        tr.addCompromisedDevice(8);
        keys = tr.uncompromisedKeys();
        if (keys != null) {
            for (Integer i :
                    keys) {
                System.out.print(i + " ");
            }
        }
         */
    }
}
