package Executors;
import Objects.*;
import Structure.*;

import java.io.BufferedReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.PriorityQueue;

public class TaskManager {
    private final String fileName;
    private GenericTaskTree dependenciesTree;
    private Processor[] processors;
    private PriorityQueue<Task> toDo;
    
    public TaskManager(String fileName){        
        dependenciesTree = new GenericTaskTree();
        this.fileName = fileName;
        readAndFillDependencies();
    }

    public int run(Politica politica){
        //inicia tiks com 0
        int tiks = 0;
        this.toDo = new PriorityQueue<>(politica.getComparator());
        //this.toDo = new TreeSet<>(politica.getComparator());  
        //pega as primeiras tarefas na lista de dependencia e joga pra fila
        toDo.addAll(dependenciesTree.getFirstFreeTasks());
        //preenche os processadores com tasks
        fill();

        while(!isProcessorsFree()){ 
            //pega a lista de processadores filtra por aqueles que tem tarefas, faz um countDown na task.
            //se a task do processador acabou ele bota as dependencias da task dentro da fila e remove a task dele.
            //se a task não acabou o processador continua com a tarefa para o próximo tik
             Arrays.asList(processors).stream()
             .filter(p -> p.isOccuped())
             .forEach(p -> {
                if(p.getTask().doing()){
                    toDo.addAll(dependenciesTree.getTasksOf(p.getTask()));
                    p.setTask(null);
                    p.setOccuped(false);
                }
            });
            fill();
            tiks++;
        };
        return tiks;
    }

    //O(p)
    private boolean isProcessorsFree(){
        return !Arrays.asList(processors).stream().filter(Processor::isOccuped).findAny().isPresent();
    }

    //O(p)
    private void fill(){
        for(Processor p: processors){
            if( !p.isOccuped() ){
                if(!toDo.isEmpty()){
                    Task task = toDo.poll();
                    p.setTask(task);
                    p.setOccuped(true);
                } else break;
            }
           
        }
    }


    
    private void readAndFillDependencies(){
        Path path = Paths.get("Data/" + fileName);
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF8"))){
            String line = reader.readLine();

            processors = new Processor[Integer.parseInt(line.split(" ")[2])];

            for(int i = 0; i < processors.length; i++){
                processors[i] = new Processor();
            }
            
            while((line = reader.readLine()) != null){
                String[] tokens = line.split(" -> ");
                String[] task1 = tokens[0].split("_");
                String[] task2 = tokens[1].split("_");
      
                Task dependency = new Task(task1[0], Integer.parseInt(task1[1]));
                Task dependent = new Task(task2[0], Integer.parseInt(task2[1]));
                
                dependenciesTree.addDependency(dependency, dependent);
                
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    public Processor[] getProcessors(){
        return processors;
    }
    
    public GenericTaskTree getDependenciesTree(){
        return dependenciesTree;
    }
}
