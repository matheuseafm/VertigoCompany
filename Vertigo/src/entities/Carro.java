package entities;

import java.util.ArrayList;
import java.io.*;


//organiza e e gerencia os dados de carro de cada proprietario
public class Carro implements Serializable {
    transient public String modelo;
    transient public String placa;
    transient public ArrayList<Multa> multas;

    public Carro(String modelo, String placa) {
        this.modelo = modelo;
        this.placa = placa;
        this.multas = new ArrayList<Multa>();
    }

    public String getModelo() {
        return modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public ArrayList<Multa> getMultas() {
        return multas;
    }

    public void addMulta(Multa multa) {
        multas.add(multa);
    }
}
