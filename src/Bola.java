import java.awt.Graphics;
import java.awt.Rectangle;

public class Bola {
    private int x, y;
    private final int diametro;
    private int velocidadeX, velocidadeY;
    private static final int VELOCIDADE_MAXIMA_X = 12;
    private static final int VELOCIDADE_MAXIMA_Y = 8;

    public Bola(int x, int y, int diametro, int velocidadeX, int velocidadeY) {
        this.x = x; this.y = y; this.diametro = diametro;
        this.velocidadeX = velocidadeX; this.velocidadeY = velocidadeY;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getDiametro() { return diametro; }
    public int getVelocidadeX() { return velocidadeX; }
    public int getVelocidadeY() { return velocidadeY; }
    public double getCentroY() { return y + diametro / 2.0; }
    public void mover() { x += velocidadeX; y += velocidadeY; }
    public void tratarLimitesVerticais(int alturaCampo) {
        if (y <= 0 && velocidadeY < 0) { y = 0; velocidadeY = -velocidadeY; }
        if (y + diametro >= alturaCampo && velocidadeY > 0) {
            y = alturaCampo - diametro; velocidadeY = -velocidadeY;
        }
    }
    private void aumentarVelocidade() {
        if (Math.abs(velocidadeX) < VELOCIDADE_MAXIMA_X) {
            velocidadeX += velocidadeX > 0 ? 1 : -1;
        }

        if (Math.abs(velocidadeY) < VELOCIDADE_MAXIMA_Y) {
            velocidadeY += velocidadeY > 0 ? 1 : -1;
        }
    }
    public void rebater(Raquete raquete, boolean esquerda) {
        if (!getLimites().intersects(raquete.getLimites())) {
            return;
        }

        if (esquerda && velocidadeX < 0) {
            x = raquete.getX() + raquete.getLargura();
            velocidadeX = -velocidadeX;
            aumentarVelocidade();

        } else if (!esquerda && velocidadeX > 0) {
            x = raquete.getX() - diametro;
            velocidadeX = -velocidadeX;
            aumentarVelocidade();
        }
    }
    public void reiniciar(int larguraCampo, int alturaCampo, int direcao) {
        x = (larguraCampo - diametro) / 2; y = (alturaCampo - diametro) / 2;
        velocidadeX = direcao < 0 ? -6 : 6; velocidadeY = 4;
    }
    public Rectangle getLimites() { return new Rectangle(x, y, diametro, diametro); }
    public void desenhar(Graphics g) { g.fillOval(x, y, diametro, diametro); }
}
