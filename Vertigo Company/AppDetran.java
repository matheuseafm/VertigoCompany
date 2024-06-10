import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class AppDetran {
    public static ArrayList<Proprietario> listaProprietario = new ArrayList<Proprietario>();
    static Random random = new Random();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Vertigo Company");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));

        JLabel welcomeLabel = new JLabel("Bem vindo a Vertigo", SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton novoProprietarioButton = new JButton("Novo proprietário");
        JButton mostraProprietariosButton = new JButton("Mostra proprietários");
        JButton adicionarCSVButton = new JButton("Adicionar do CSV");
        JButton sairButton = new JButton("Sair");

        panel.add(novoProprietarioButton);
        panel.add(mostraProprietariosButton);
        panel.add(adicionarCSVButton);
        panel.add(sairButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        novoProprietarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novoProprietario();
            }
        });

        mostraProprietariosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostraRegistros();
            }
        });

        adicionarCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarDoCSV();
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    public static void novoProprietario() {
        String quantidadeStr = JOptionPane.showInputDialog("Quantos proprietarios deseja adicionar:");
        int quantidade = Integer.parseInt(quantidadeStr);
        quantidade = quantidade == 0 ? 1 : quantidade;

        for (int i = 0; i < quantidade; i++) {
            Proprietario dono = registraProprietario();
            garagem(dono);
        }
    }

    public static void mostraRegistros() {
        StringBuilder sb = new StringBuilder();
        int aux2 = 0;
        int qtmulta = 0;
        int qtPlacas = 0;
        for (Proprietario proprietario : listaProprietario) {
            int aux = 1;
            aux2++;

            sb.append(String.format("\nO nome do proprietário é %s\n", proprietario.nome));
            sb.append(String.format("O CNH do proprietário é %s\n\n", proprietario.CNH));

            for (Carro carro : proprietario.meusCarros) {
                sb.append(String.format("O carro %d do(a) proprietário(a) é um %s ,com placa %s\n", aux, carro.getModelo(), carro.getPlaca()));
                aux++;
                qtPlacas++;
                for (Multa multa : carro.getMultas()) {
                    qtmulta++;
                    sb.append(String.format("O carro possui uma multa por %s no valor de %.2fR$\n\n", multa.infracao, multa.valor));
                    break;
                }
            }
            sb.append(String.format("O proprietário possui %d carro(s)\n", aux - 1));
        }
        sb.append(String.format("\nExistem %d proprietario(s) e %d carros registrado(s)\n", aux2, qtPlacas));
        sb.append("O total de multas é " + qtmulta + "\nSem mais proprietários!\n");

        JTextArea textArea = new JTextArea(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 200));
        JOptionPane.showMessageDialog(null, scrollPane, "Registros dos Proprietários", JOptionPane.INFORMATION_MESSAGE);
    }

    public static Proprietario registraProprietario() {
        String nome = JOptionPane.showInputDialog("Digite o nome do proprietário:");

        String CNH = String.format("ES%d", 1000 + random.nextInt(1000) + random.nextInt(26));
        JOptionPane.showMessageDialog(null, "A CNH do(a) " + nome + " é " + CNH);

        Proprietario dono = new Proprietario(nome, CNH);
        listaProprietario.add(dono);

        return dono;
    }

    public static void garagem(Proprietario motorista) {
        motorista.registraCarroGUI();
    }

    public static void adicionarDoCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String nome = data[0].trim();
                    String CNH = data[1].trim();
                    Proprietario proprietario = new Proprietario(nome, CNH);
                    for (int i = 2; i < data.length; i += 4) {
                        String modelo = data[i].trim();
                        String placa = data[i + 1].trim();
                        Carro carro = new Carro(modelo, placa);
                        if (data.length > i + 2) {
                            String infracao = data[i + 2].trim();
                            if (!infracao.isEmpty()) {
                                double valor = Double.parseDouble(data[i + 3].trim());
                                Multa multa = new Multa(infracao, valor);
                                carro.addMulta(multa);
                            }
                        }
                        proprietario.meusCarros.add(carro);
                    }
                    listaProprietario.add(proprietario);
                }
                JOptionPane.showMessageDialog(null, "Dados importados com sucesso!");
            } catch (IOException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

