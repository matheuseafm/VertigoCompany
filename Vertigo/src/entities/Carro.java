package entities;

import java.util.ArrayList;

public class Carro {
    private String modelo;
    private String placa;
    private ArrayList<Multa> multas;

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
