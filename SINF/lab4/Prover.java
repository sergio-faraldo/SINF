package com.Sergio;

//imports del paquete bitcoinj, se encarga del parseado de bloques

import org.bitcoinj.core.Block;
import org.bitcoinj.core.Context;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.utils.BlockFileLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Prover {
    private MerkleTree mt;//árbol Merkel
    private HashMap<Sha256Hash, Integer> diccHojas;//Diccionario de los nodos hoja del árbol. Para cada Hash en los nodos hoja almacena su número de hoja

    public Prover(String filename) {//Cargado de un archivo .dat

        List<File> blockChainFiles = new ArrayList<>();
        blockChainFiles.add(new File(filename));
        MainNetParams params = MainNetParams.get();
        Context context = new Context(params);
        BlockFileLoader bfl = new BlockFileLoader(params, blockChainFiles);

        long nTransactions = 0;
        for (Block block : bfl) {//en una primera pasada cuento el número de transacciones para crear el árbol Merkle
            nTransactions += block.getTransactions().size();
        }

        bfl = new BlockFileLoader(params, blockChainFiles);//reinicio el File Loader

        mt = new MerkleTree(nTransactions);//inicializo el árbol con el tamaño adecuado
        diccHojas = new HashMap<>();//y el diccionario de las hojas
        int i = 1;

        for (Block block : bfl) {//Parseo y añado los elementos
            for (Transaction t :
                    block.getTransactions()) {
                mt.setHoja(i, t.getTxId());
                diccHojas.put(t.getTxId(), i);
                i++;
            }
        }

        //ahora mismo solamente están los valores hash de los nodos hoja
        //con esta llamada a compute, se calculan los demás valores
        mt.compute();
    }

    public int getPosHoja(Sha256Hash hash) {
        if (!diccHojas.containsKey(hash)) return -1;
        return diccHojas.get(hash);
    }

    public ArrayList<Integer> coverageNodes(int idHoja) {
        return mt.coverageNodes(idHoja);
    }

    public Sha256Hash getHash(int idNodo) {
        return mt.getNode(idNodo);
    }

    public MerkleTree getMt() {
        return mt;
    }

    public HashMap<Sha256Hash, Integer> getDiccHojas() {
        return diccHojas;
    }
}
