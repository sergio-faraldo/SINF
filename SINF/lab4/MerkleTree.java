package com.Sergio;

import org.bitcoinj.core.Sha256Hash;

import java.util.ArrayList;

public class MerkleTree {
    private static long log2nlz(long a) {//logaritmo base 2 utilizando NumberOfLoadingZeros
        if (a == 0)
            return 0;
        return 63 - Long.numberOfLeadingZeros(a);
    }

    private static long nextPowerOf2(long a) {//obtiene la siguiente potencia de 2, lo hace mediante operaciones de bits
        long b = 1;
        while (b < a) {
            b = b << 1;
        }
        return b;
    }


    private Sha256Hash[] tree;//Árbol de claves
    private long treeDepth;//número de niveles del árbol
    private long nHojas;//número máximo de hojas del árbol

    public MerkleTree(long nHojas) {//se le pasa el número de hojas
        this.nHojas = nextPowerOf2(nHojas);//si el número de hojas no es potencia de 2, esta función pone la siguiente potencia de 2 más grande
        tree = new Sha256Hash[(int) (2 * this.nHojas - 1)];
        treeDepth = log2nlz(this.tree.length + 1);
    }

    public Sha256Hash[] getTree() {
        return tree;
    }

    public Sha256Hash getNode(int id) {
        return tree[id - 1];
    }

    public void setNode(int id, Sha256Hash in) {
        tree[id - 1] = in;
    }

    public void setHoja(int id, Sha256Hash in) {
        int Nodeid = tree.length - (((tree.length + 1) / 2) - id);
        tree[Nodeid - 1] = in;
    }

    public void setTree(Sha256Hash[] tree) {
        this.tree = tree;
    }

    public ArrayList<Integer> coverageNodes(int idHoja) {//El cálculo del coverage se hace de forma iterativa, no como en mi entrega anterior, dónde se hacía de forma recursiva

        //No envío directamente los hashes porque el Verifier necesita saber el ID de los nodos en los que están los hashes. Le mando un array con los IDs y luego el Verifier le pide
        // al prover los hashes por separado a medida que los vaya necesitando.

        ArrayList<Integer> ret = new ArrayList<>();
        int Nodeid = tree.length - (((tree.length + 1) / 2) - idHoja);

        //Añado el hijo hermano
        if (Nodeid % 2 == 1) Nodeid--;
        else Nodeid++;

        while (Nodeid > 0) {
            ret.add(Nodeid);//+++++++++
            if (Nodeid % 2 == 1) Nodeid--;
            Nodeid /= 2;//Padre

            //Añado el hermano del padre
            if (Nodeid % 2 == 1) Nodeid--;
            else Nodeid++;
        }//repeat

        if (ret.get(ret.size() - 1) != 1)
            ret.add(1);//en último lugar, añado el id del nodo raíz. Lo va a necesitar el Verifier para comprobar que el cálculo que obtuvo él coincide con el que tiene el prover.
        return ret;
    }

    public void compute() {//de forma recursiva calculo los hashes de todos los nodos que no sean hojas.
        _compute(1);
    }

    private byte[] _compute(int nodeID) {

        if (log2nlz(nodeID) < treeDepth - 1) {//si el nodo no es hoja

            //calculo y concateno los hashes de los hijos
            byte[] a = _compute(nodeID * 2);
            byte[] b = _compute(nodeID * 2 + 1);

            byte[] c = new byte[a.length + b.length];
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);

            //calculo el hash del padre, aplicando la función hash a los hashes de los hijos
            byte[] tmp = Sha256Hash.hash(c);
            setNode(nodeID, Sha256Hash.wrap(tmp));
            return tmp;

        } else {//si el nodo es hoja
            try {
                Sha256Hash t = getNode(nodeID);
                return t.getBytes();

            } catch (Exception e) {//si la hoja está vacía
                setNode(nodeID, Sha256Hash.wrap(Sha256Hash.hash(new byte[1])));//Las hojas vacías las lleno con el hash de 0x00
                return getNode(nodeID).getBytes();
            }
        }
    }
}
