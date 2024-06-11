package entities;

import javax.swing.*;
import java.util.ArrayList;

public class Proprietario {
    public String nome;
    public String CNH;
    public ArrayList<Carro> meusCarros;

    public Proprietario(String nome, String CNH) {
        this.nome = nome;
        this.CNH = CNH;
        this.meusCarros = new ArrayList<Carro>();
    }

    public void registraCarroGUI() {
        String modelo = JOptionPane.showInputDialog("Digite o modelo do carro:");

        String placa = JOptionPane.showInputDialog("Digite a placa do carro (7 caracteres) ou deixe em branco para gerar aleatoriamente:");
        if (placa != null && !placa.isEmpty()) {
            // Verifica se a placa fornecida pelo usuário não é vazia e não é cancelada
            if (placa.length() != 7) {
                JOptionPane.showMessageDialog(null, "A placa deve ter exatamente 7 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
                return; // Retorna caso a placa não tenha 7 caracteres
            }
        } else {
            // Gera uma placa aleatória
            placa = GeradorPlacas.getPlaca();
        }

        Carro carro = new Carro(modelo, placa);
        meusCarros.add(carro);

        int addMulta = JOptionPane.showConfirmDialog(null, "Deseja adicionar uma multa a este carro?");
        if (addMulta == JOptionPane.YES_OPTION) {
            String infracao = JOptionPane.showInputDialog("Digite a infracao:");
            String valorStr = JOptionPane.showInputDialog("Digite o valor da multa:");
            double valor = Double.parseDouble(valorStr);

            Multa multa = new Multa(infracao, valor);
            carro.addMulta(multa);
        }
    }
}
