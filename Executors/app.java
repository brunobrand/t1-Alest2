package Executors;

import Structure.Politica;
import java.time.LocalDateTime;
import java.util.Scanner;

public class app {
    public static void main(String []args){
        
        Scanner in = new Scanner(System.in);
        
        System.out.println("Digite o nome do arquivo de teste: ");

        String testCase = in.nextLine();

        System.out.println(LocalDateTime.now());
        TaskManager tm = new TaskManager(testCase);

        System.out.println(tm.getProcessors().length);
        System.out.println(tm.getDependenciesTree().length());

        System.out.println("MIN: " + tm.run(Politica.MIN));
        System.out.println(LocalDateTime.now());
        System.out.println("MAX: " + tm.run(Politica.MAX));
    }
}