# Pong em Java — FATEC

Jogo de um jogador contra o computador, feito somente com Java SE, Swing e AWT.
Identificação: Guedes (completar turma e identificação exigida pelo professor).
JDK utilizado para validação: 21.0.10. Sem Maven, Gradle ou dependências externas.

## Executar

Abra esta pasta como projeto no IntelliJ ou VS Code, configure o JDK 21 e execute `src/Main.java`.
No terminal, a partir desta pasta:

```powershell
mkdir bin -Force
javac -encoding UTF-8 -d bin src/*.java
java -cp bin Main
```

No Linux/macOS, troque a primeira linha por `mkdir -p bin`.

## Testar

```powershell
javac -encoding UTF-8 -d bin src/*.java tests/*.java
java '-Djava.awt.headless=true' -cp bin TestesPong
```

Os testes usam verificações explícitas que lançam AssertionError: não precisam de `-ea`.
Cobrem limites, comandos opostos, liberação de comandos, colisões e direção,
correção de sobreposição, saque, pontuação única, tolerância do computador,
dificuldades, vitória de ambos, bloqueio após vitória e reinícios sucessivos.
Uma simulação de 20.000 atualizações verifica os limites das raquetes e bola.
O código de regras é independente do Swing para permitir testes sem desktop.

## Controles e regras

- W ou seta para cima: subir. S ou seta para baixo: descer.
- Comandos opostos simultâneos anulam o movimento. Soltar todas as teclas de uma direção interrompe o comando.
- N: selecionar dificuldade e começar nova partida. Fechar a janela encerra o programa.
- A bola precisa sair completamente pela lateral para contar ponto ao adversário.
- Vitória aos 5 pontos. O jogo oferece nova partida; também é possível pressionar N depois.
- Fácil: computador move 3 pixels por atualização. Difícil: 6 pixels. Jogador: 7 pixels.
- A bola começa com velocidade de 6 pixels horizontais e 4 verticais por atualização.
- A cada rebatida em uma raquete, a velocidade aumenta em 1 em cada eixo, até o máximo de 12 na horizontal e 8 na vertical.
- Após cada ponto, a bola retorna ao centro com a velocidade inicial.
- O computador compara os centros e só se move quando a diferença excede 8 pixels.
- O saque inicial vai para a direita; após ponto, segue para o lado em que a bola saiu.
- A janela tem campo útil fixo de 800 × 600; o timer nominal é de 16 ms.

## Responsabilidades e orientação a objetos

| Classe | Responsabilidade |
|---|---|
| Main | Inicializar na thread de eventos do Swing |
| JanelaJogo | Configurar JFrame e limpar comandos ao perder foco |
| CampoJogo | Desenhar, associar teclas, manter um timer e apresentar mensagens |
| Partida | Coordenar regras, dificuldade, pontos, vitória e nova partida |
| Raquete | Encapsular posição, dimensões, movimento e área de colisão |
| Bola | Encapsular movimento, colisões, área e saque |
| Placar | Armazenar e alterar pontuações por métodos |

Herança: CampoJogo estende JPanel e JanelaJogo estende JFrame.
Composição: Partida cria e mantém bola, duas raquetes e placar.
O desenho apenas consulta o estado. Eventos de tecla atualizam comandos; o timer consulta esses comandos e move os objetos.
Uma colisão inverte a componente adequada da velocidade. Uma saída altera o placar e reinicia a bola ou encerra a partida.

## Registro das fases

1. Janela e campo: inicialização no Swing, pack e dimensões preferidas.
2. Raquetes: duas instâncias independentes, atributos privados e desenho reutilizado.
3. Teclado: pressionar/soltar por key bindings; limites e limpeza ao perder foco.
4. Bola e ciclo: bola centralizada e apenas um timer reutilizado.
5. Colisões: Rectangle, verificação de direção e reposicionamento após contato.
6. Pontuação: saída completa, métodos de Placar e saque centralizado.
7. Computador: velocidade limitada, tolerância e seleção de dois níveis.
8. Encerramento: meta de 5, parada antes de diálogo e restauração de estado.

Problema prevenido: inverter velocidade repetidamente com a bola dentro da raquete.
Solução: verificar a direção de aproximação e retirar a bola da área da raquete após rebater.

## Validação manual necessária

Executar em ambiente gráfico e conferir: tamanho útil, suavidade do teclado,
combinação W + seta para cima, perda de foco com tecla pressionada, diálogos de
dificuldade/vitória, encerramento e duas novas partidas sem aceleração.
Essas interações visuais não são comprovadas pelos testes de regras.

## Limitações

Colisão retangular aproxima a bola circular. A velocidade da bola aumenta nas rebatidas,
com limites de 12 pixels na horizontal e 8 na vertical por atualização.
atravessar raquetes. O timer do Swing não garante frequência exata. Não há sons,
pausa, rede ou persistência. A partida continua ao perder foco, com comandos limpos.
