import java.util.Random;

public class GeradorPlacas
{
    static Random random = new Random();
    static int qtPlacas =1;


    public static String getPlaca()
    {
        String placaCompleta;

        placaCompleta = String.format("%c%c%c%c%c%03d",geraLetras(),geraLetras(),geraLetras(),geraNumero(),geraLetras(),qtPlacas);

        ++qtPlacas;

        return placaCompleta;

    }

    public static char geraLetras()
    {
        char letra='A';
        int set = random.nextInt(26);
        char letraAleatoria = (char)(letra+set);

        return letraAleatoria;
    }
    public static char geraNumero()
    {
        int numero = random.nextInt(10);
        char numeroChar = (char)(numero + '0');

        return numeroChar;
    }

}
