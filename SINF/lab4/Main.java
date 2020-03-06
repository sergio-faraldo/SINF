package com.Sergio;

/*
Utilizo la librería bitcoinj para parsear los bloques
 */

import org.bitcoinj.core.Sha256Hash;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Prover p = new Prover("blk00000.dat");

        //pruebo con el TxID de la primera transacción de bitcoin
        Verifier.verboseIsInMKT(p, "0e3e2357e806b6cdb1f70b54c3a3a17b6714ee1f0e68bebb44a74b1efd512098");
        //a continuación dejo una lista de otros 100 TxIDs que he probado y están en el archivo
        String[] TxIDs = {
                "48883710787e7cd845e8014dcb421b6f2cad6a14c8f0139f7c30fa52291fd436",
                "ce436421b2dc8b8eaa52c9b1cc4f891c2756fe83669a7ca2de3c94bf147fe953",
                "0a605512a6ac71ef0ca5d631a0a56a643e43f89042a21e782f342047197fe453",
                "bb153dc34dce525ccdcc1bd7aac8b510c72c5e84c2855a8805ff257da3af5e83",
                "f2a475f518aee707bd95b351c184af95b9889ee54d6c747e82fc4a62189fe5ac",
                "bbbb306fa5c8ebccef96890d2e62a32b688dca994e08bed43579c50cd3cf2ef9",
                "69da9e3ac9810715bd776927fb658b597f4af0e14cc7fb838999b42dcd9f30a8",
                "92d1fa120866416a0a0de5743ef17b33a0060a1f977af97e5bf18fc3834f7e78",
                "3218ed62cc97a5769f6f25f8560e211b7def865c5d6dcd6d1e9c30a2d4ef29d7",
                "6a7da163af6b7aa47df9249e740014565247f015cf756581530bf97f321fcf26",
                "4db683261cbce5026862c19ab4f25e152b232f372bcb6967d90039240b5ff662",
                "b0adde02aa0b48978ff644cbb61beee72d187d4a3e55efe814ef451009cff48e",
                "09d62dd0baa05002a8a83fea5381940a605c15bbf92696c12ad9974ec16f3c2d",
                "b8929773cf8505424b94dddccda3ace4d983cc731a22845ffa0151ea9ddf6099",
                "9139e31e73380e2066734fc4521976a3f4d66d37fe68d6c9b54aef81c06f3d25",
                "071213730cb59230312f026b993cc59a589d31f8e07ddbc3fe8bd23c103fed74",
                "4afe09095e905f6fcefcbcf277aa521de099c4e0b28c29c4a63edb2dd6cf2b82",
                "406c57b009c14bec8fc8c7ae71e5957dda9521047423aabc7a8f5b6a891f744f",
                "181de79927c879b53de29045c2f79744d401a19385b35bb6f1ede34e5eefa3bf",
                "3fb2e6a6d7b07fcb9977ef7fd720aa448bb7b7007ff8d5c3f57e9e4e89ef74bd",
                "ebedebc54affec3026b835027db524ea0d761fcbbd4697e5fba7870ef92f0478",
                "c53893c468886b95b31e6ee7ef86e9ac92f2e434cf83214aa974cd7a7d8f80d8",
                "2f2bcd63c736e9e8fcfb373a92fbda1daa1e3008a1f17eae9d3384c9e0cf1d92",
                "379ef3a37b069ab90c83f66e2f2e6a438f137552db4fa2dbdd543454149fe9c0",
                "d93ccea9309bfe79a858bad00ee9be9c2fbcdd2b19893b143b58e1af3d5fc03e",
                "f09cdc47e03a3d341913393326a4584164627acdcc68a82303bf4c4697af6acd",
                "baddebffa03f189c13aece9da1b814378f1be9ae0819f545a4f71f4b977f6a1b",
                "04927d5e4944c1aaa3a4f0cf20a63739110327cc3fcb4ee858a9d1b47ebf83db",
                "6751c9dfbd395875b7e34e38b97424683179b5ae5d80387cf8ceb6771c2fe147",
                "537270a0f3cef8fd40e93c88ded9cf95402231e4de3c2d7fe10adf13b9af44c0",
                "088b57be14037284b432385e17d51a8c1f14f489cb3c81ae456273fe308fcdff",
                "d65ed905b1b1cac84e7067eaa00594b1c2ef95c877f9010bb24345700faff2de",
                "41078fc5e374ac4a6dccf70651d8aaca96c85a1dedffdd75c1e7368d62ef9f98",
                "45309ef3c8f97a765a602cc2a22b444277d4562eb1999f6030f78a6d621f9f68",
                "d7843ee990aa2f211ec218804efd3ae938d3b78cc21905e7319e05cbbc5f4128",
                "07a4e404794334125f88c9c31cd78f6c22e6a21a190042891f16a11db62f4b57",
                "e557fe294b68768a393313eec18c1f759a96de99fa163b315dc935caf8bf05c5",
                "f87edd0ecd1784878f48f5ce29ec11f25e8023a85d6544abad377fe1f2bf0fc3",
                "44226e9aafc637a04a0305503b66faf2feca1ba823b7f2ea82edef8dee7f1301",
                "9068d2c0f9bbac41cfd2ea69e6560782785d3df3a4e1988f85317b79344fc930",
                "ad2d32524bc5f06c7be8fe0ca0fa3efc060a54147f28d016d98e500c49afb42e",
                "c268debe2ce2f6a84822e6356697b2a1e0f40fdb4c52583c432e84be9e9f631e",
                "9a95e00a69495f9653bbdbd6b73ac69c05e885f3b9464f3ff733125c528faf0e",
                "ef8f0b47e9bfeb5f7fa8707a37a66531461149d2f3896f4ef3b812c6c01f3d9d",
                "0c11ac33a5a95dcc8c807c574f68cceb59c713477db263a03c26296a360fcb8c",
                "7e0f85ff9482571041121134514da7a90befe271aabdbb4ebbae661a146fe9e8",
                "65d61bf985bda92cea71e30bbc5b36f564610f6c3e2122e1911677c106fffb73",
                "6e465b6f7b0be5b74c63047202610ece494cf33aef73654072b19227072ffaa2",
                "2b22d162fd5955db438d56c5bbe41637a8851dd1ea13fef925f5bee9c75f3ace",
                "9db476163d1415acc9d487ec1f34fabb4be8b0f4a5a5d39174b82dcfd2ef2f7c",
                "a12385869ce02a2f6d70ba57f287fb8db955d86582021b60fffeffdaa45f59ca",
                "137e36e1ffae8f9f146d47ebd59fde2d11ead18cd5d6969515fc9666b65f4bc4",
                "f0b895cac8eaf13efaae32175eaed63ed07193fc318f12b56b3f40efec9f1100",
                "5161bcd87b71aee5edd9deab9d767efc23af08c656dbaf5e8941cc2cf72f0a8b",
                "87bf534291243540678c668bac1058b6fd805ba8921b5a5fc69f21c8b05f4df9",
                "1736cd2ba757e008d5b800f6280f91012274695d18f1b387e81834df189fe539",
                "a5a40cc215b6fe7306ce10cfdd0d88bb582320a26d417171fb6a0c88043ff997",
                "ad46d56df70160303c56950e6c38d07d9263fa6bf7d95a1391867a2564af9904",
                "2d70fa5989b1ba1207d748838212abbc913f193634480dec548e93b7c54f38e3",
                "0e0e9fec1195132b28662539528e936c981ebd3b0f140d9ad3c65717cf2f329a",
                "d19979225b8fffa1b871b13907b9f548fcc457b9edad3e0325ce4b08d3ff2e4a",
                "c4307f7977406cee556cd4b33125a4a25e3d2d52cd0bdd1a7a28eb54639f9e25",
                "6a8db742aecfa8dccadb7e92c2f1cb7e5457620dcf4d962ebc58fa901a3fe783",
                "91d180106bc805b1d73703451af71b2d770fcad8b9db519d5637816a73ef8e50",
                "09ec7b27cb1307db7bb8926e33344f4cf876b7f098afe06e58f1c918e97f14bf",
                "38137d05c44d3cfc20a39970cbe02e794f3380720a8b2f5ad4ce8e574eafb36c",
                "b78032c63f239a2eeb5cda47f8959b2bd97a727a6567a22dc2a20db00edff31a",
                "36e4238aae7dad4d9c133803413304f747e4e3c63f4e2147fa6a5fd4387fc5b5",
                "40c311c1bcefcfb58f091f8afa10b24de487d9f59cd7ff9853c77e6815dfe814",
                "2fccea7b98af610858a8ef422e8efb417430dbb2b362cab7ea84c23cdf2f22e0",
                "b3f1e78f68a9b644c040c8ad53bdb4d501c4af004815d225a488962971df8c0f",
                "dd59ffd5e3c4668549ecaa3eabe3b88f54fc2bc1d280fc6d96e7db30b6ef4b3b",
                "275cc52dad47cde896681880ec314ea0cf3bd15e752ed301c846ebfff3ef0e38",
                "060dae1bd514576219af461f5a9984ed32220c8803a24f4222ee329d7f5f8288",
                "e613d69a1c4567fcc7b1dd7bb82d3d10ea74ad0dbc9fa652576a852b7fef8237",
                "edb0e086abfa612c6e5fe8161952b690acf13e0e7d190ab4b1af0728cacf3715",
                "b6f156cd4dccb0a2133a5ca552dfc2c54bf4c1a8eab95f1a38cdd7077b0f86d5",
                "d5d70368802f733e62ae7d41973bdaee6ca83948da5de8e196ceb7ab33dfce04",
                "bef296b3cdf056dd5a727087806a8aeb5b770f0b794aa325160ec13ae03f1de3",
                "4ae6388e7cb06f8ea3d08c52e746324d8ec19b2f3fae57b2ca56bc4b769f8b7d",
                "86c92a4f0a287cac2ea5f4cbeb96599c9d70b27f73a76aac29276b9838ffc51b",
                "44f642e0d8ec3ed5b2156653fcc58c2afc8b1a84b08fb810d8c193ada39f5e7b",
                "432f6921b85800addae5588b41c47e06c2f9d67284411ee63c1baa549b2f66ca",
                "a52cd5bf42809ad2838fa55b3de295427fca1736bbf92ccd4f642fc43e0fc3e6",
                "be8a5e82b965dd6efb3c3d1c5436184ab556b6c887c85d65d7009dfa0c8ff17f",
                "cf64267857b48d3d97d4d3571759958a7e53cbe6589592748d613673477fba8f",
                "e2d0085ac45a052794c64b2c3ca4d9250e14c580a7216adad69af935d02f2dda",
                "6720d28725cd7aa7f43e5c9604dca201d40aad1a566769fe151790db6ecf933a",
                "ea19e076e93389d3ddf179fb0eb16e0f354c7ff0b7509e7539ecde495eefa312",
                "086f294cb26d82f19b37a48d9bec4bbe9cd339fe05fc9957efc65936b21f4c1f",
                "99fd8ed7f12a26d70bc3b3d63f362af52ddee9c9382d4980f4b121b3dd7f237f",
                "124910fd66bfa9162f3b45c310637e3727f7a43eb644423170e02bd7adbf53bf",
                "f8bc28ec7db11d72fb47c230f7b82acd8e092b476879fbde528b3828ac8f528d",
                "b48e0dcadd2ca81acb78b6d87be9b6eed9e59f173888dd97c8b18c527a6f846d",
                "ed7b741ad205f8d8d2bd13ee871e4474a75a54cd3adb8390a6681b7c404fbe49",
                "112b16be342afd219d1e4ffa4ef99a3270fa1fb6ff5f9f6d0ec1dd33219fdf97",
                "565dd6167e7d3cb835068e687bc2eb175f29bf74e17605d7036fe87cbbaf45bb",
                "4e28b61c7e5416c53094b7d5fd79eb8bb3fb55fcefcc9ccab8c511c8986f667a",
                "e895f9006c0f74c7514928b79ef3cacd8b1d2deb30ab083b380b11fc452fbb33",
                "5701de4059802217396e385e3ada060507d8364578a40a221b2a92af081ff602"
        };

        //compruebo que ninguno falla
        for (String TxID :
                TxIDs) {
            if (!Verifier.isInMerkleTree(p, TxID))
                System.out.println("La transacción no está en el archivo");//sólo imprimo algo si falla
        }

    }
}
