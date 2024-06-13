import entities.*;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class VertigoInc implements Serializable {
    public static ArrayList<Proprietario> listaProprietario = new ArrayList<Proprietario>();
    static Random random = new Random();

    //inicio da GUI com botoes e gerenciamento frontend
    public static void main(String[] args) {
        JFrame frame = new JFrame("Vertigo Company");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(11, 1));

        JLabel welcomeLabel = new JLabel("Bem vindo a Vertigo Inc", SwingConstants.CENTER);
        JLabel highLabel = new JLabel("", SwingConstants.CENTER);
        JLabel midLabel = new JLabel("", SwingConstants.CENTER);
        panel.add(welcomeLabel);

        JButton novoProprietarioButton = new JButton("Novo proprietário");
        JButton mostraProprietariosButton = new JButton("Mostrar proprietários");
        JButton adicionarCSVButton = new JButton("Adicionar do CSV");
        JButton salvarRegistroButton = new JButton("Salvar Registro");
        JButton limparRegistroButton = new JButton("Limpar Registro");
        JButton recuperarProprietarioButton = new JButton("Recuperar Proprietário (.ser)");
        JButton sairButton = new JButton("Sair");
        JButton sobreButton = new JButton("Sobre");

        panel.add(novoProprietarioButton);
        panel.add(mostraProprietariosButton);
        panel.add(recuperarProprietarioButton);
        panel.add(highLabel);
        panel.add(salvarRegistroButton);
        panel.add(limparRegistroButton);
        panel.add(adicionarCSVButton);
        panel.add(midLabel);
        panel.add(sobreButton);
        panel.add(sairButton);


        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        novoProprietarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    novoProprietario();
                } catch (CNHDuplicadaException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
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
                try {
                    adicionarDoCSV();
                } catch (FormatoInvalidoException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        salvarRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarRegistros();
            }
        });

        limparRegistroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparRegistros();
            }
        });

        recuperarProprietarioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Escolha o arquivo .ser");
                int userSelection = fileChooser.showOpenDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    String nomeArquivo = fileChooser.getSelectedFile().getAbsolutePath();
                    Proprietario proprietario = RecuperarDadosSerializados.recuperarProprietario(nomeArquivo);
                    if (proprietario != null) {
                        listaProprietario.add(proprietario);
                    }
                }
            }
        });

        sairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        sobreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirSobre();
            }
        });
    }


    //Inicio de registro de um novo proprietario. Faz validaçoes e salva o objeto automaticamente num arqurivo .ser
    public static void novoProprietario() throws CNHDuplicadaException {
        String quantidadeStr;
        int quantidade = 0;

        do {
            quantidadeStr = JOptionPane.showInputDialog("Quantos proprietários deseja adicionar:");
            if (quantidadeStr != null) {
                try {
                    quantidade = Integer.parseInt(quantidadeStr);
                    if (quantidade <= 0) {
                        JOptionPane.showMessageDialog(null, "Por favor, insira um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } while (quantidadeStr != null && quantidade <= 0);

        if (quantidadeStr != null) {
            for (int i = 0; i < quantidade; i++) {
                Proprietario dono = registraProprietario();
                try {
                    dono.salvar(dono.nome + ".ser");
                    JOptionPane.showMessageDialog(null, "Proprietário criado e salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Exceção de I/O", "Erro", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
                garagem(dono);
            }
        }
    }

    //mostra informaçoes de proprietario do arquivo .ser recuparado
    public static void mostrarInformacoes(Proprietario proprietario) {
        StringBuilder sb = new StringBuilder();
        sb.append("Nome do proprietário: ").append(proprietario.nome).append("\n");
        sb.append("CNH: ").append(proprietario.CNH).append("\n");

        if (!proprietario.meusCarros.isEmpty()) {
            sb.append("Carros registrados:\n");
            for (Carro carro : proprietario.meusCarros) {
                sb.append(" - Modelo: ").append(carro.getModelo()).append(", Placa: ").append(carro.getPlaca()).append("\n");

                if (!carro.getMultas().isEmpty()) {
                    sb.append("   Multas:\n");
                    for (Multa multa : carro.getMultas()) {
                        sb.append("    - Infração: ").append(multa.infracao).append(", Valor: ").append(multa.valor).append("R$\n");
                    }
                } else {
                    sb.append("   Sem multas registradas.\n");
                }
            }
        }
        else {
            sb.append("Sem carros registrados.\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JOptionPane.showMessageDialog(null, scrollPane, "Informações do Proprietário", JOptionPane.INFORMATION_MESSAGE);
        }

    //limpa os registros do sistema quando necessário
    public static void limparRegistros() {
        listaProprietario.clear();
        JOptionPane.showMessageDialog(null, "Todos os registros foram limpos.");
    }

    //botao de salvar os registros atuais em um arquivo CSV
    public static void salvarRegistros() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar como");
        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                for (Proprietario proprietario : listaProprietario) {
                    writer.append(proprietario.nome).append(",").append(proprietario.CNH);
                    for (Carro carro : proprietario.meusCarros) {
                        writer.append(",").append(carro.getModelo()).append(",").append(carro.getPlaca());
                        for (Multa multa : carro.getMultas()) {
                            writer.append(",").append(multa.infracao).append(",").append(String.valueOf(multa.valor));
                        }
                    }
                    writer.append("\n");
                }
                writer.flush();
                JOptionPane.showMessageDialog(null, "Dados salvos com sucesso!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //mostra os resgistros de proprietarios no momento com seus veiculos, placas e multas. faz a soma de quantos tem no sistema e retorna
    public static void mostraRegistros() {
        if (listaProprietario.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Não há proprietários registrados.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder();
        int aux2 = 0; //soma quantidade de proprietarios
        int qtmulta = 0; //soma quantidade de multas
        int qtPlacas = 0; //soma quantidade de placas para numero total de veiculos
        for (Proprietario proprietario : listaProprietario) {
            int aux = 1;
            aux2++;

            sb.append(String.format("\nO nome do proprietário é %s\n", proprietario.nome));
            sb.append(String.format("O CNH do proprietário é %s\n\n", proprietario.CNH));

            for (Carro carro : proprietario.meusCarros) {
                sb.append(String.format("O carro %d do(a) proprietário(a) é um %s, com placa %s\n", aux, carro.getModelo(), carro.getPlaca()));
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


    //Registro de informaçoes do proprietario
    public static Proprietario registraProprietario() throws CNHDuplicadaException {
        String nome;
        do {
            nome = JOptionPane.showInputDialog("Digite o nome do proprietário:");
            if (nome == null) {
                return null;
            }
            if (nome.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "O nome não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } while (nome.trim().isEmpty());

        String CNH = String.format("PR%d", 1000 + random.nextInt(1000) + random.nextInt(26));

        for (Proprietario p : listaProprietario) {
            if (p.CNH.equals(CNH)) {
                throw new CNHDuplicadaException("CNH duplicada: " + CNH);
            }
        }

        JOptionPane.showMessageDialog(null, "A CNH do(a) " + nome + " é " + CNH);

        Proprietario dono = new Proprietario(nome, CNH);
        listaProprietario.add(dono);

        return dono;
    }

    //representa o proprietário para o qual queremos registrar um carro na garagem. se nao for nulo inicia o registro do carro
    public static void garagem(Proprietario motorista) {
        if (motorista != null) {
            motorista.registraCarroGUI();
        }
    }

    //adiciona informaçoes de registro diretamente do CSV
    public static void adicionarDoCSV() throws FormatoInvalidoException {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length < 2) {
                        throw new FormatoInvalidoException("Formato inválido no arquivo CSV.");
                    }
                    String nome = data[0].trim();
                    String CNH = data[1].trim();
                    Proprietario proprietario = new Proprietario(nome, CNH);
                    for (int i = 2; i < data.length; i += 4) {
                        String modelo = data[i].trim();
                        String placa = data[i + 1].trim();
                        Carro carro = new Carro(modelo, placa);
                        if (data.length > i + 2) {
                            String infracao = data[i + 2].trim();
                            double valor = Double.parseDouble(data[i + 3].trim());
                            Multa multa = new Multa(infracao, valor);
                            carro.addMulta(multa);
                        }
                        proprietario.meusCarros.add(carro);
                    }
                    listaProprietario.add(proprietario);
                }
                JOptionPane.showMessageDialog(null, "Dados importados do CSV com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao ler o arquivo CSV: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro na conversão de valor no arquivo CSV.", "Erro", JOptionPane.ERROR_MESSAGE);
            } catch (FormatoInvalidoException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //sobre
    public static void exibirSobre() {
        String informacoes = "Vertigo Inc\n\n" +
                "Este programa foi desenvolvido por Matheus Moreira.\n" +
                "instagram.com/matheuseafm\n" +
                "github.com/matheuseafm\n" +
                "linkedin.com/in/matheuseafm\n\n" +
                "Versão 1.5\n\n" +
                "Copyright © 2024 Vertigo Company. Todos os direitos reservados.";
        JOptionPane.showMessageDialog(null, informacoes, "Sobre", JOptionPane.INFORMATION_MESSAGE);
    }
}
