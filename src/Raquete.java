import java.awt.Graphics;
import java.awt.Rectangle;

public class Raquete {
    private final int x, largura, altura;
    private int y, velocidade;

    public Raquete(int x, int y, int largura, int altura, int velocidade) {
        this.x = x; this.y = y; this.largura = largura; this.altura = altura;
        definirVelocidade(velocidade);
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getLargura() { return largura; }
    public int getAltura() { return altura; }
    public int getVelocidade() { return velocidade; }
    public double getCentroY() { return y + altura / 2.0; }
    public void definirVelocidade(int valor) {
        if (valor <= 0) throw new IllegalArgumentException("Velocidade deve ser positiva");
        velocidade = valor;
    }
    public void moverParaCima(int alturaCampo) { mover(-velocidade, alturaCampo); }
    public void moverParaBaixo(int alturaCampo) { mover(velocidade, alturaCampo); }
    private void mover(int deslocamento, int alturaCampo) {
        y = Math.max(0, Math.min(alturaCampo - altura, y + deslocamento));
    }
    public void centralizar(int alturaCampo) { y = (alturaCampo - altura) / 2; }
    public Rectangle getLimites() { return new Rectangle(x, y, largura, altura); }
    public void desenhar(Graphics g) { g.fillRect(x, y, largura, altura); }
}
