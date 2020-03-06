# SINF
Entregas de la asignatura "Seguridad de la Información". Todos han sido resueltos en java.

Exercises from the subject "Information Security". All of them have been solved in java.

## Lab 1 - Cryptanalysis

Criptonanálisis de 10 textos cifrados encriptados con la misma clave (one-time pad XOR usado múltiples veces)

Cryptanalysis of 10 ciphertexts encrypted with the same key (XOR one-time pad used multiple times)

## Lab 2a - Bit Commitment

Protocolo de compromiso de bits https://es.wikipedia.org/wiki/Protocolos_de_compromiso

Bit commitment protocol https://www.quantiki.org/wiki/bit-commitment

## Lab 2b - PRG Composition

Composition of PRGs using the Blum-Micali algorithm and using parallel composition

Composición de Generadores Pseudo-Aleatorios usando el algoritmo Blum-Micali y composición paralela

## Lab 3 - AACS

Implementación de un sistema similar al AACS (Advanced Access Content System) (https://es.wikipedia.org/wiki/Advanced_Access_Content_System). La implementación es más simple que la original. Incluye una implementación del árbol de claves y un formato de archivo personalizado.

Simplified AACS (Advanced Access Content System) (https://en.wikipedia.org/wiki/Advanced_Access_Content_System), with custom implementation of the key tree data structure and custom file format.

## Lab 4 - Blockchain & Merkle Trees

Implementación de un sistema para comprobar que una transacción de Blockchain (en este caso transacciones del blockchain de Bitcoin) pertenece a un set de transacciones con la ayuda de un Árbol de Merkle (https://es.wikipedia.org/wiki/%C3%81rbol_de_Merkle). La implementación del Árbol de Merkle es personalizada y basada en la implementación del árbol de claves realizada para el Lab 3. Se usa bitcoinj (https://github.com/bitcoinj/bitcoinj) para ayudar en el parseo del blockchain.

Implementation of a program that can check whether a Blockchain Transaction (in this case we are using transactions from the Bitcoin blockchain) belongs to a given set of transactions. This implementation uses a Merkle Tree (https://en.wikipedia.org/wiki/Merkle_tree) to aid with the task. The Merkle Tree implementation is custom and it is based on the key tree from the previous lab. Bitcoinj (https://github.com/bitcoinj/bitcoinj) is used to help with the parsing of the blockchain.
