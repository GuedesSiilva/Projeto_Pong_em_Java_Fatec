public class Placar {
    private int jogador, computador;
    public int getJogador() { return jogador; }
    public int getComputador() { return computador; }
    public void marcarPontoJogador() { jogador++; }
    public void marcarPontoComputador() { computador++; }
    public void zerar() { jogador = 0; computador = 0; }
}
