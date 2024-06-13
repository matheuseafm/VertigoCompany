package entities;

import java.io.*;
import javax.swing.*;
import java.util.ArrayList;

//organiza e inicia o prporietario a ser gerenciado
public class Proprietario implements Serializable {
    private static final long serialVersionUID = 1L;
    public String nome;
    public String CNH;
    public ArrayList<Carro> meusCarros;

    public Proprietario(String nome, String CNH) {
        this.nome = nome;
        this.CNH = CNH;
        this.meusCarros = new ArrayList<>();
    }

    //salva o proprietario
    public void salvar(String nome_arquivo) throws IOException {
        try (FileOutputStream arquivo = new FileOutputStream(nome_arquivo);
             ObjectOutputStream gravador = new ObjectOutputStream(arquivo)) {
            gravador.writeObject(this);
        }
    }

    //persistencia de objetos
    public static Proprietario abrir(String nome_arquivo) throws IOException, ClassNotFoundException {
        Proprietario proprietario = null;
        try (FileInputStream arquivo = new FileInputStream(nome_arquivo);
             ObjectInputStream restaurador = new ObjectInputStream(arquivo)) {
            proprietario = (Proprietario) restaurador.readObject();
        }
        return proprietario;
    }

    //interface grafica para registro
    public void registraCarroGUI() {
        String modelo = JOptionPane.showInputDialog("Digite o modelo do carro:");

        String placa = JOptionPane.showInputDialog("Digite a placa do carro (7 caracteres) ou deixe em branco para gerar aleatoriamente:");
        if (placa != null && !placa.isEmpty()) {
            if (placa.length() != 7) {
                JOptionPane.showMessageDialog(null, "A placa deve ter exatamente 7 caracteres.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            placa = GeradorPlacas.getPlaca();
        }

        Carro carro = new Carro(modelo, placa);
        meusCarros.add(carro);

        int addMulta = JOptionPane.showConfirmDialog(null, "Deseja adicionar uma multa a este carro?");
        if (addMulta == JOptionPane.YES_OPTION) {
            String infracao;
            double valor = 0.0;

            do {
                infracao = JOptionPane.showInputDialog("Digite a infração:");
                if (infracao == null || infracao.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "O nome da infração não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } while (infracao == null || infracao.trim().isEmpty());

            String valorStr;
            do {
                valorStr = JOptionPane.showInputDialog("Digite o valor da multa: (Cancelar implica no cancelamento de multa)");
                if (valorStr == null) {
                    return;
                }
                try {
                    valor = Double.parseDouble(valorStr);
                    if (valor <= 0) {
                        JOptionPane.showMessageDialog(null, "O valor da multa deve ser maior que zero.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um valor numérico válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                    valor = -1;
                }
            } while (valor <= 0);

            Multa multa = new Multa(infracao, valor);
            carro.addMulta(multa);
        }
    }
}
