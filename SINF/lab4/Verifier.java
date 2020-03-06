package com.Sergio;

import org.bitcoinj.core.Sha256Hash;

import java.util.ArrayList;

public class Verifier {

    public static boolean isInMerkleTree(Prover p, byte[] TxID) {//comprueba si un TxID está en el merkle tree del Prover

        //inicialización de variables
        Sha256Hash hash = (Sha256Hash.wrap(TxID));//aquí iré acumulando el hash a medida que vaya subiendo por el árbol
        int idhash = p.getPosHoja(Sha256Hash.wrap(TxID));
        if (idhash < 0) return false;//si no se encuentra en los nodos hoja, devuelvo false directamente

        //Obtengo los ids de los nodos del coverage
        ArrayList<Integer> ci = p.coverageNodes(idhash);

        //por cada nodo de la lista (excepto el último, el último es el nodo raíz, lo utilizo después para verificar)
        for (int j = 0; j < ci.size() - 1; j++) {
            int idNode = ci.get(j);

            byte[] a;
            byte[] b;

            //ordeno según el id para que la concadenación siga el mismo orden que cuando se crea el árbol merkle
            if (idNode % 2 == 0) {
                a = p.getHash(idNode).getBytes();
                b = hash.getBytes();
            } else {
                a = hash.getBytes();
                b = p.getHash(idNode).getBytes();
            }

            //concadeno los hashes de los dos nodos hijos
            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);

            //calculo el hash del nodo padre
            hash = Sha256Hash.wrap(Sha256Hash.hash(c));
        }

        //cuando llegue al final, debería tener el mismo hash que el nodo raíz del árbol Merkle
        if (hash.equals(p.getHash(ci.get(ci.size() - 1)))) {
            return true;
        }
        return false;
    }

    public static boolean isInMerkleTree(Prover p, String TxID) {//para tener el código un poco más limpio, esta función llama a la anterior, traduciendo antes de String de valores Hexadecimales a byte[]
        byte[] val = new byte[TxID.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(TxID.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return Verifier.isInMerkleTree(p, val);
    }

    public static void verboseIsInMKT(Prover p, String TxID) {//para ahorrar espacio en el main, simplemente imprime por pantalla los resultados
        if (Verifier.isInMerkleTree(p, TxID)) System.out.println("La transacción está en el archivo");
        else System.out.println("La transacción no está en el archivo");
    }
}
