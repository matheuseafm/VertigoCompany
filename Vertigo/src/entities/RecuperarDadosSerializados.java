package entities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;


//recupera dados de persistencia de objetos
public class RecuperarDadosSerializados {
    public static Proprietario recuperarProprietario(String nomeArquivo) {
        Proprietario proprietario = null;
        try (FileInputStream fileInput = new FileInputStream(nomeArquivo);
             ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {

            proprietario = (Proprietario) objectInput.readObject();
            JOptionPane.showMessageDialog(null, "Proprietário recuperado com sucesso!");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao recuperar o proprietário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
        return proprietario;
    }
}
