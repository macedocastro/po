package jogoDaVelha;

public class Tabuleiro {
    private Jogador[][] tabuleiro;
    private Jogador vencedor;

    public Tabuleiro() {
        this.tabuleiro = new Jogador[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = Jogador.NENHUM;
            }
        }
        this.vencedor = Jogador.NENHUM;
    }

    public boolean fazerJogada(int linha, int coluna, Jogador jogador) {
        if (linha < 0 || linha >= 3 || coluna < 0 || coluna >= 3 || tabuleiro[linha][coluna] != Jogador.NENHUM) {
            return false;
        }
        tabuleiro[linha][coluna] = jogador;
        System.out.println("Jogada feita: linha " + linha + ", coluna " + coluna + ", jogador " + jogador);
        verificarVencedor();
        return true;
    }
    

    public Jogador getEstadoCelula(int linha, int coluna) {
        return tabuleiro[linha][coluna];
    }

    public Jogador getVencedor() {
        return vencedor;
    }

    public boolean estaCheio() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == Jogador.NENHUM) {
                    return false;
                }
            }
        }
        return true;
    }

    private void verificarVencedor() {
        // Verifica linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != Jogador.NENHUM) {
                vencedor = tabuleiro[i][0];
                return; // Vencedor encontrado, sair do método
            }
        }
        // Verifica colunas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i] != Jogador.NENHUM) {
                vencedor = tabuleiro[0][i];
                return; // Vencedor encontrado, sair do método
            }
        }
        // Verifica diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != Jogador.NENHUM) {
            vencedor = tabuleiro[0][0];
            return; // Vencedor encontrado, sair do método
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != Jogador.NENHUM) {
            vencedor = tabuleiro[0][2];
            return; // Vencedor encontrado, sair do método
        }
    }

    public void desfazerJogada(int linha, int coluna) {
        if (linha >= 0 && linha < 3 && coluna >= 0 && coluna < 3) {
            tabuleiro[linha][coluna] = Jogador.NENHUM;
            vencedor = Jogador.NENHUM; // Resetar o vencedor quando desfaz a jogada
            System.out.println("Jogada desfeita: linha " + linha + ", coluna " + coluna);
        } else {
            System.out.println("Posição inválida para desfazer jogada.");
        }
    }

}
