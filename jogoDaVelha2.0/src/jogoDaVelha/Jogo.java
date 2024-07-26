package jogoDaVelha;
public abstract class Jogo {
    protected Jogador jogadorAtual;

    public Jogo() {
        this.jogadorAtual = Jogador.X;
    }

    public Jogador getJogadorAtual() {
        return jogadorAtual;
    }

    protected void alternarJogador() {
        if (jogadorAtual == Jogador.X) {
            jogadorAtual = Jogador.O;
        } else {
            jogadorAtual = Jogador.X;
        }
        System.out.println("Jogador alternado para: " + jogadorAtual);
    }

    public abstract void jogar();

	public Jogador getVencedor() {
		// TODO Auto-generated method stub
		return null;
	}
}



