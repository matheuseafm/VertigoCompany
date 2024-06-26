package entities;

import java.util.Random;
import java.io.*;


//gerador automatico de placas para caso o usuario nao tenha uma placa disponivel para ser registrada
//funciona como um "banco de placas disponiveis"
public class GeradorPlacas implements Serializable {
    public static Random random = new Random();
    public static int qtPlacas =1;

    public static String getPlaca(){
        String placaCompleta;

        placaCompleta = String.format("%c%c%c%c%c%03d",geraLetras(),geraLetras(),geraLetras(),geraNumero(),geraLetras(),qtPlacas);

        ++qtPlacas;

        return placaCompleta;

    }

    public static char geraLetras(){
        char letra='A';
        int set = random.nextInt(26);
        char letraAleatoria = (char)(letra+set);

        return letraAleatoria;
    }

    public static char geraNumero(){
        int numero = random.nextInt(10);
        char numeroChar = (char)(numero + '0');

        return numeroChar;
    }

}
