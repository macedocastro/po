package jogoDaVelha;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JogoDaVelhaSupremoUI implements InterfaceJogoUI {
    private JFrame frame;
    private JButton[][][] botoesTabuleiros;
    private JogoDaVelhaSupremo jogo;
    private int proximoTabuleiroLinha;
    private int proximoTabuleiroColuna;

    public JogoDaVelhaSupremoUI(JogoDaVelhaSupremo jogo) {
        this.jogo = jogo;
        this.botoesTabuleiros = new JButton[3][3][9];
        this.proximoTabuleiroLinha = -1;
        this.proximoTabuleiroColuna = -1;
    }

    @Override
    public void inicializar() {
        frame = new JFrame("Jogo da Velha Supremo");
        frame.setLayout(new GridLayout(3, 3, 10, 10)); // Ajustar espaçamento entre tabuleiros
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Criar os botões e adicionar ao frame
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JPanel painelTabuleiro = new JPanel(new GridLayout(3, 3));
                painelTabuleiro.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Adicionar borda ao tabuleiro

                for (int k = 0; k < 9; k++) {
                    JButton botao = new JButton("");
                    int linhaTabuleiro = i;
                    int colunaTabuleiro = j;
                    int indiceCelula = k;
                    botao.setFont(new Font("Arial", Font.PLAIN, 24));
                    botao.setPreferredSize(new Dimension(60, 60)); // Ajustar tamanho dos botões
                    botao.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            lidarCliqueBotao(linhaTabuleiro, colunaTabuleiro, indiceCelula);
                        }
                    });
                    painelTabuleiro.add(botao);
                    botoesTabuleiros[i][j][k] = botao;
                }
                frame.add(painelTabuleiro);
            }
        }

        frame.setVisible(true);
    }

    private void lidarCliqueBotao(int linhaTabuleiro, int colunaTabuleiro, int indiceCelula) {
        System.out.println("Clique no botão: linhaTabuleiro " + linhaTabuleiro + ", colunaTabuleiro " + colunaTabuleiro + ", indiceCelula " + indiceCelula);
        System.out.println("proximoTabuleiroLinha: " + proximoTabuleiroLinha + ", proximoTabuleiroColuna: " + proximoTabuleiroColuna);
        System.out.println("Jogador atual: " + jogo.getJogadorAtual());

        int linhaCelula = indiceCelula / 3;
        int colunaCelula = indiceCelula % 3;

        if (jogo.fazerJogada(linhaTabuleiro, colunaTabuleiro, linhaCelula, colunaCelula, jogo.getJogadorAtual())) {
            botoesTabuleiros[linhaTabuleiro][colunaTabuleiro][indiceCelula].setText(jogo.getJogadorAtual().toString());
            
            if (jogo.tabuleiros[linhaTabuleiro][colunaTabuleiro].getVencedor() == jogo.getJogadorAtual()) {
                desabilitarBotõesTabuleiro(linhaTabuleiro, colunaTabuleiro);
                jogo.tabuleiroPrincipal.fazerJogada(linhaTabuleiro, colunaTabuleiro, jogo.getJogadorAtual());
            }
            
            proximoTabuleiroLinha = linhaCelula;
            proximoTabuleiroColuna = colunaCelula;

            // Se o próximo tabuleiro estiver cheio, permitir jogar em qualquer tabuleiro
            if (proximoTabuleiroLinha >= 0 && proximoTabuleiroColuna >= 0 &&
                jogo.tabuleiros[proximoTabuleiroLinha][proximoTabuleiroColuna].estaCheio()) {
                proximoTabuleiroLinha = -1;
                proximoTabuleiroColuna = -1;
            }

            atualizar();

            if (jogo.tabuleiroPrincipal.getVencedor() != Jogador.NENHUM) {
                exibirVencedor(jogo.tabuleiroPrincipal.getVencedor());
            } else if (jogo.getJogadorAtual() == Jogador.O) {
                SwingUtilities.invokeLater(() -> {
                    jogo.jogarMaquina();
                    atualizar();
                    if (jogo.tabuleiroPrincipal.getVencedor() != Jogador.NENHUM) {
                        exibirVencedor(jogo.tabuleiroPrincipal.getVencedor());
                    }
                });
            }
        } else {
            System.out.println("Jogada inválida. Célula já ocupada ou fora dos limites.");
        }
    }



    private void desabilitarBotõesTabuleiro(int linhaTabuleiro, int colunaTabuleiro) {
        for (JButton botao : botoesTabuleiros[linhaTabuleiro][colunaTabuleiro]) {
            botao.setEnabled(false);
        }
    }

    @Override
    public void atualizar() {
        // Atualiza o estado dos botões de acordo com o estado do jogo
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tabuleiro tabuleiro = jogo.tabuleiros[i][j];
                for (int k = 0; k < 9; k++) {
                    int linha = k / 3;
                    int coluna = k % 3;
                    Jogador estado = tabuleiro.getEstadoCelula(linha, coluna);
                    botoesTabuleiros[i][j][k].setText(estado.toString());
                    botoesTabuleiros[i][j][k].setEnabled(estado == Jogador.NENHUM); // Desabilita botões ocupados
                }
            }
        }
        System.out.println("Atualização completa. Jogador atual: " + jogo.getJogadorAtual());
    }

    @Override
    public void exibirVencedor(Jogador vencedor) {
        JOptionPane.showMessageDialog(frame, "Fim de jogo. Vencedor: " + vencedor);
        frame.dispose();
    }

    public static void main(String[] args) {
        // Exibir caixa de seleção de dificuldade
        String[] opcoesDificuldade = {"Fácil", "Médio", "Difícil", "Impossível"};
        String dificuldadeEscolhida = (String) JOptionPane.showInputDialog(null, "Escolha a dificuldade:", 
            "Selecionar Dificuldade", JOptionPane.QUESTION_MESSAGE, null, 
            opcoesDificuldade, opcoesDificuldade[0]);

        // Verifica se o usuário escolheu uma dificuldade
        if (dificuldadeEscolhida != null) {
            Dificuldade dificuldade = converterParaDificuldade(dificuldadeEscolhida);
            JogoDaVelhaSupremo jogo = new JogoDaVelhaSupremo(dificuldade);
            JogoDaVelhaSupremoUI jogoUI = new JogoDaVelhaSupremoUI(jogo);
            SwingUtilities.invokeLater(() -> jogoUI.inicializar()); // Executar UI na EDT
        } else {
            System.exit(0); // Encerra se a dificuldade não for escolhida
        }
    }

    private static Dificuldade converterParaDificuldade(String dificuldadeEscolhida) {
        switch (dificuldadeEscolhida) {
            case "Fácil":
                return Dificuldade.FACIL;
            case "Médio":
                return Dificuldade.MEDIO;
            case "Difícil":
                return Dificuldade.DIFICIL;
            case "Impossível":
                return Dificuldade.IMPOSSIVEL;
            default:
                throw new IllegalArgumentException("Dificuldade não reconhecida: " + dificuldadeEscolhida);
        }
    }

}
