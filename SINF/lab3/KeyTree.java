package com.Sergio;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.pow;

public class KeyTree {
    private static int log2nlz(int a) {//logaritmo base 2 utilizando NumberOfLoadingZeros
        if (a == 0)
            return 0;
        return 31 - Integer.numberOfLeadingZeros(a);
    }

    private static int nextPowerOf2(int a) {//obtiene la siguiente potencia de 2, lo hace mediante operaciones de bits
        int b = 1;
        while (b < a) {
            b = b << 1;
        }
        return b;
    }


    private ArrayList<Integer> compromised;//Lista de dispositivos comprometidos
    private SecretKey[] tree;//Árbol de claves
    private int treeDepth;//número de niveles del árbol
    private int nDevices;//número máximo de dispositivos del árbol

    public KeyTree(int nDevices) {//se le pasa el número de dispositivos
        this.nDevices = nextPowerOf2(nDevices);//si el número de dispositivos no es potencia de 2, esta función pone la siguiente potencia de 2 más grande

        compromised = new ArrayList<>();
        try {
            KeyGenerator keyFactory = KeyGenerator.getInstance("AES");
            tree = new SecretKey[2 * this.nDevices - 1];
            for (int i = 0; i < tree.length; i++) {
                tree[i]= keyFactory.generateKey();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        treeDepth = log2nlz(this.tree.length + 1);

        byte[] IV = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
    }

    public SecretKey[] getTree() {
        return tree;
    }

    public void setTree(SecretKey[] tree) {
        this.tree = tree;
    }

    public SecretKey getKey(int id) {
        return tree[id - 1];
    }

    public void addCompromisedDevice(int id) {
        if (!compromised.contains(id)) compromised.add(id);
    }

    public boolean isCompromised(int id) {
        return compromised.contains(id);
    }

    public void removeCompromised(int id) {
        compromised.remove(Integer.valueOf(id));
    }

    public ArrayList<Integer> uncompromisedKeys() {
        ArrayList<Double> compDev_div = new ArrayList<>();
        for (Integer in :
                compromised) {
            compDev_div.add(in / (double) nDevices);
        }
        /*
        El ArrayList CompDev_div contiene los ids de los dispositivos comprometidos dividido entre el número total de
        dispositivos. Para comprobar si la clave N, la cual se sitúa en la n-ésima posición del nivel m del árbol,
        ha sido comprometida debido a que el dispositivo d ha sido comprometido, basta con comprobar que:

                      (n+1)/2^m >=d/(número de dispositivos) && (n+1)/2^m >=d/(número de dispositivos)
        El valor d/(número de dispositivos) estará almacenado en compDev_div

        La idea es partir el número de dispositivos en 2^m cachos (tantos cachos como nodos tenga el nivel del árbol)
        y si el dispositivo está en el cacho correspondiente al n-ésimo cacho, estará
         */

        return uncKRecursive(0, 0, compDev_div);
    }

    private ArrayList<Integer> uncKRecursive(int level, int column, ArrayList<Double> compDev_div) {
        boolean isCompromised = false;
        for (Double d :
                compDev_div) {
            if ((column + 1) / pow(2, level) >= d && (column) / pow(2, level) < d) {
                isCompromised = true;
                break;
            }
        }
        if (!isCompromised) {//si no está comprometido un nodo, devuelvo el nodo y dejo de explorar la rama
            ArrayList<Integer> ret = new ArrayList<>();
            ret.add((int) (pow(2, level) + column));
            return ret;
        } else {//si sí está comprometido, devuelvo lo que devuelvan los hijos
            if (level + 1 < treeDepth) {
                ArrayList<Integer> r1 = uncKRecursive(level + 1, column * 2, compDev_div);
                ArrayList<Integer> r2 = uncKRecursive(level + 1, column * 2 + 1, compDev_div);
                if (r1 == null) return r2;//si uno de los hijos devuelve null, devuelvo el otro hijo
                if (r2 == null) return r1;
                r2.addAll(r1);//si ninguno de los hijos devuelve null, devuelvo todos los valores que hayan devuelto los hijos.
                return r2;
            } else return null;//si no tiene hijos, devuelvo null
        }
    }

    public ArrayList<Integer> keyIDsForDevice(int Dev_id) {
        ArrayList<Integer> ret = new ArrayList<>();
        int Nodeid = tree.length - (((tree.length + 1) / 2) - Dev_id);
        ret.add(Nodeid);//Node id for the device
        while (Nodeid != 1) {//parents
            if (Nodeid % 2 != 0) Nodeid--;
            Nodeid /= 2;
            ret.add(Nodeid);
        }
        return ret;
    }

    public HashMap<Integer, SecretKey> keysForDevice(int Dev_id) {
        HashMap<Integer, SecretKey> ret = new HashMap<>();
        ArrayList<Integer> keyIDs = keyIDsForDevice(Dev_id);
        for (Integer kID :
                keyIDs) {
            ret.put(kID,getKey(kID));
        }
        return ret;
    }
}
