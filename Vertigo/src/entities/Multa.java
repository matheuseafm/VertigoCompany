package entities;

import java.io.*;

//sistema de atribuiçao a um valor de multa para cada veiculo (se tiver multa)
public class Multa implements Serializable {
    public String infracao;
    public double valor;

    public Multa(String infracao, double valor) {
        this.infracao = infracao;
        this.valor = valor;
    }
}

