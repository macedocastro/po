package jogoAtual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JogoDaVelhaUI {
    private JFrame frame;
    private JButton[][] botoesTabuleiro;
    private JLabel statusLabel;
    private JogoDaVelha jogo;
    private String vencedor; // Adicionado para armazenar o vencedor

    public JogoDaVelhaUI() {
        mostrarTelaInicial();
    }

    private void mostrarTelaInicial() {
        JFrame telaInicial = new JFrame("Escolher Dificuldade");
        telaInicial.setBounds(100, 100, 300, 150);
        telaInicial.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        telaInicial.setLayout(new GridLayout(2, 2));

        JLabel label = new JLabel("Escolha a dificuldade:");
        telaInicial.add(label);

        JComboBox<JogoDaVelha.Dificuldade> comboBox = new JComboBox<>(JogoDaVelha.Dificuldade.values());
        telaInicial.add(comboBox);

        JButton btnOk = new JButton("OK");
        telaInicial.add(btnOk);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JogoDaVelha.Dificuldade dificuldadeSelecionada = (JogoDaVelha.Dificuldade) comboBox.getSelectedItem();
                jogo = new JogoDaVelha(dificuldadeSelecionada);
                telaInicial.dispose();
                inicializarJogo();
            }
        });

        telaInicial.setVisible(true);
    }

    private void inicializarJogo() {
        frame = new JFrame("Jogo da Velha");
        frame.setBounds(100, 100, 300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel painelTabuleiro = new JPanel();
        painelTabuleiro.setLayout(new GridLayout(3, 3));
        botoesTabuleiro = new JButton[3][3];

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoesTabuleiro[i][j] = new JButton("");
                botoesTabuleiro[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
                botoesTabuleiro[i][j].addActionListener(createBotaoListener(i, j));
                painelTabuleiro.add(botoesTabuleiro[i][j]);
            }
        }

        statusLabel = new JLabel("É a vez do Jogador X");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(painelTabuleiro, BorderLayout.CENTER);
        frame.setVisible(true);

        atualizarBotoes();
    }

    private ActionListener createBotaoListener(int linha, int coluna) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (jogo.fazerJogada(linha, coluna)) {
                    atualizarBotoes();
                    if (!jogo.verificarVencedor() && !jogo.estaCheio()) {
                        jogo.jogarMaquina();
                        atualizarBotoes();
                    }
                }
            }
        };
    }

    private void atualizarBotoes() {
        SwingUtilities.invokeLater(() -> {
            boolean jogoTerminado = jogo.verificarVencedor() || jogo.estaCheio();

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    botoesTabuleiro[i][j].setText(jogo.getEstadoCelula(i, j).toString());
                    botoesTabuleiro[i][j].setEnabled(jogo.getEstadoCelula(i, j) == Jogador.NENHUM && !jogoTerminado);
                }
            }

            if (jogo.verificarVencedor()) {
                vencedor = jogo.getJogadorAtual().toString();
                statusLabel.setText("Jogador " + vencedor + " venceu!");
                desabilitarTodosBotoes();
                exibirTelaVencedor();
            } else if (jogo.estaCheio() && !jogo.verificarVencedor()) {
                statusLabel.setText("Empate!");
                desabilitarTodosBotoes();
                exibirTelaVencedor();
            } else {
                statusLabel.setText("É a vez do Jogador " + jogo.getJogadorAtual());
            }
        });
    }

    private void exibirTelaVencedor() {
        JFrame telaVencedor = new JFrame("Resultado do Jogo");
        telaVencedor.setBounds(100, 100, 300, 300);
        telaVencedor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        telaVencedor.setLayout(new BorderLayout());

        JLabel vencedorLabel = new JLabel( vencedor != null ?  vencedor  : "Empate!", SwingConstants.CENTER);
        vencedorLabel.setFont(new Font("Arial", Font.BOLD, 60)); // Aumentado o tamanho da fonte para destaque
        telaVencedor.add(vencedorLabel, BorderLayout.CENTER);

        JButton btnOk = new JButton("OK");
        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                telaVencedor.dispose();
                jogo = new JogoDaVelha(jogo.getDificuldade()); // Reiniciar o jogo com a mesma dificuldade
                atualizarBotoes();
            }
        });

        telaVencedor.add(btnOk, BorderLayout.SOUTH);
        telaVencedor.setVisible(true);
    }

    private void desabilitarTodosBotoes() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botoesTabuleiro[i][j].setEnabled(false);
            }
        }
    }
}