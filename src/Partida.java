/** Regras independentes da interface: podem ser testadas sem abrir uma janela. */
public class Partida {
    public static final int LARGURA = 800, ALTURA = 600, META = 5;
    public enum Dificuldade {
        FACIL("Fácil", 3), DIFICIL("Difícil", 6);
        private final String nome;
        private final int velocidade;
        Dificuldade(String nome, int velocidade) { this.nome = nome; this.velocidade = velocidade; }
        public int getVelocidade() { return velocidade; }
        @Override public String toString() { return nome; }
    }
    private final Raquete jogador = new Raquete(30, 250, 14, 100, 7);
    private final Raquete computador = new Raquete(756, 250, 14, 100, 3);
    private final Bola bola = new Bola(390, 290, 20, 6, 4);
    private final Placar placar = new Placar();
    private Dificuldade dificuldade = Dificuldade.FACIL;
    private boolean encerrada;

    public Raquete getJogador() { return jogador; }
    public Raquete getComputador() { return computador; }
    public Bola getBola() { return bola; }
    public Placar getPlacar() { return placar; }
    public Dificuldade getDificuldade() { return dificuldade; }
    public boolean isEncerrada() { return encerrada; }
    public String getVencedor() {
        if (!encerrada) return "";
        return placar.getJogador() >= META ? "Jogador" : "Computador";
    }
    public void novaPartida(Dificuldade nivel) {
        dificuldade = nivel; placar.zerar(); encerrada = false;
        jogador.centralizar(ALTURA); computador.centralizar(ALTURA);
        computador.definirVelocidade(nivel.getVelocidade());
        bola.reiniciar(LARGURA, ALTURA, 1);
    }
    public void atualizar(boolean subir, boolean descer) {
        if (encerrada) return;
        if (subir != descer) {
            if (subir) jogador.moverParaCima(ALTURA); else jogador.moverParaBaixo(ALTURA);
        }
        double diferenca = bola.getCentroY() - computador.getCentroY();
        if (diferenca < -8) computador.moverParaCima(ALTURA);
        else if (diferenca > 8) computador.moverParaBaixo(ALTURA);
        bola.mover(); bola.tratarLimitesVerticais(ALTURA);
        bola.rebater(jogador, true); bola.rebater(computador, false);
        if (bola.getX() + bola.getDiametro() <= 0) {
            placar.marcarPontoComputador(); concluirPonto(-1);
        } else if (bola.getX() >= LARGURA) {
            placar.marcarPontoJogador(); concluirPonto(1);
        }
    }
    private void concluirPonto(int direcao) {
        encerrada = placar.getJogador() >= META || placar.getComputador() >= META;
        if (!encerrada) bola.reiniciar(LARGURA, ALTURA, direcao);
    }
}
