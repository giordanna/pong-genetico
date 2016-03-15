package pong.Outros;

public class Configuracao {
    
    // todo fazer input
    public static MersenneTwisterFast R = new MersenneTwisterFast();
    
    public static String FONTE = "Courier New";
    
    public static int LARGURA_TELA = 800;
    public static int ALTURA_TELA = 600;
    
    public static int RAQUETE_LARGURA = 25;
    public static int RAQUETE_ALTURA = 150;
    public static int BOLA_RAIO = 20;
    
    // max raquete = 196
    // max bola = 768
    public static int MAX_VELOCIDADE_RAQUETE = 15;
    public static int MAX_VELOCIDADE_BOLA   = 5;

    public static int RAQUETE_INCLINACAO = 3;
    
    public static int TAMANHO_CROMOSSOMO = 3;
    public static int MAX_POPULACAO = 10;
    
    public static double INTERVALO_GENES = 1.5;
    public static double MUTACAO = 0.05;
    public static double DESVIO_MUTACAO = 0.9;
    public static double PORCENTAGEM_SUBSTITUIR = 0.50;

    public static int VELOCIDADE_GERAL = 5; // to do ver como usar isso para acelerar o jogo inteiro
    
}
