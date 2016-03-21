package pong.Outros;

import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Configuracao {
    
    // todo fazer input
    public static MersenneTwisterFast R = new MersenneTwisterFast();
    
    public static String FONTE = "Courier New";
    
    public static int LARGURA_TELA = 800;
    public static int ALTURA_TELA = 600;
    
    public static int RAQUETE_LARGURA = 25;
    public static int RAQUETE_ALTURA = 150;
    public static int BOLA_RAIO = 20;

    public static int MAX_VELOCIDADE_RAQUETE = 15;
    public static int MAX_VELOCIDADE_BOLA   = 5;

    public static int RAQUETE_INCLINACAO = 3;
    
    public static int TAMANHO_CROMOSSOMO = 3;
    public static int MAX_POPULACAO = 30;
    
    public static double INTERVALO_GENES = 1.5;

    public static int VELOCIDADE_GERAL = 5; // to do ver como usar isso para acelerar o jogo inteiro
    public static int RODADA = 3;
    
    public static List<Double> config = new ArrayList<>();
    
    public Configuracao() throws FileNotFoundException{
        
        File f = new File("input.txt");
        if(f.exists() && !f.isDirectory()) { 
            try (Scanner s = new Scanner(f)) {
                while (s.hasNextLine()){
                    config.add(s.nextDouble());
                }
            }
            
            LARGURA_TELA = config.get(0).intValue();
            ALTURA_TELA = config.get(1).intValue();
            RAQUETE_LARGURA = config.get(2).intValue();
            RAQUETE_ALTURA = config.get(3).intValue();
            BOLA_RAIO = config.get(4).intValue();
            MAX_VELOCIDADE_RAQUETE = config.get(5).intValue();
            MAX_VELOCIDADE_BOLA = config.get(6).intValue();
            RAQUETE_INCLINACAO = config.get(7).intValue();
            TAMANHO_CROMOSSOMO = config.get(8).intValue();
            MAX_POPULACAO = config.get(9).intValue();
            INTERVALO_GENES = config.get(10);
            VELOCIDADE_GERAL = config.get(11).intValue();
            RODADA = config.get(12).intValue();
            
            
        }
        
        
    }
    
}
