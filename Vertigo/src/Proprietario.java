import javax.swing.*;
import java.util.ArrayList;

class Proprietario {
    String nome;
    String CNH;
    ArrayList<Carro> meusCarros;

    public Proprietario(String nome, String CNH) {
        this.nome = nome;
        this.CNH = CNH;
        this.meusCarros = new ArrayList<Carro>();
    }

    public void registraCarroGUI() {
        String modelo = JOptionPane.showInputDialog("Digite o modelo do carro:");

        String placa = "";
        while (true) {
            placa = JOptionPane.showInputDialog("Digite a placa do carro (7 caracteres):");
            if (placa != null && placa.length() == 7) {
                break;
            } else {
                JOptionPane.showMessageDialog(null, "A placa deve ter exatamente 7 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
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
