package Structure;
import Objects.Task;    

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GenericTaskTree {    
    private Node root;
    private int count;

    public GenericTaskTree() {
        root = new Node(null);
        count = 0;
    }

    public int length(){
        return count;
    }
    
    public boolean addDependency(Task dependency, Task task) { 
        Node dependencyAux = searchTaskNodeRef(dependency, root);
        Node taskAux = searchTaskNodeRef(task, root);
        
        if(dependencyAux == null && taskAux == null){
            Node father = new Node(dependency);
            Node child = new Node(task);
            father.addSubtree(child);
            count = count + 2;
            root.addSubtree(father);
            return true;
        }
        else if(dependencyAux != null && taskAux == null){
            Node child = new Node(task);
            dependencyAux.addSubtree(child);
            count++;
            return true;
        } else if(dependencyAux == null && taskAux != null){
            if(taskAux.father.element == null){
                Node n = new Node(dependency);
                n.subtrees.add(taskAux);
                taskAux.father.addSubtree(n);
                taskAux.father.removeSubtree(taskAux);
                taskAux.father = n;
                count++;
                return true;
            }
        } else if(dependencyAux != null && taskAux != null){
            if(taskAux.father.element == null){
                dependencyAux.subtrees.add(taskAux);
                taskAux.father.removeSubtree(taskAux);
                taskAux.father = dependencyAux;
                return true;
            }
        }
        return false;
    }
    
    public List<Task> getFirstFreeTasks(){  
        return root.subtrees.stream().map(n -> n.element).collect(Collectors.toList());
    }
    
    public boolean add(Task elem, Task father) { 
        Node n = new Node(elem);
        //se o father for == null então significa que quero adicionar o elemento no topo da arvore
        if(father == null){
            root.addSubtree(n);
            count++;
            return true;
        }
        else{
            Node aux = searchNodeRef(father, root);//vai procurar o pai começando pela RAIZ
            if(aux != null){ //se encontrou o pai
                aux.addSubtree(n); // adiciona N como galho do Father
                n.father = aux; //faz o n apontar pro pai Father
                count++;
                return true;
            }
        }
        return false;
    }
    //O(n²)
    public List<Task> getTasksOf(Task task){
        List<Task> lista;
        Node taskNode = searchNodeRef(task, root);
        
        lista = new ArrayList<>();
        for(Node n : taskNode.subtrees) {
            lista.add(n.element);
        }
        
        return lista;
    }

    /**
     *O(n) 
     * @param elem  procura na arvore este elemento
     * @param n sempre vai ser chamado como root
     * @return  retorna null se o elemento não estiver ou a referencia para ele.
     */
    private Node searchNodeRef(Task elem, Node n) {
        if(n == null){
        return null;
        }
        if(elem.equals(n.element)){
            return n;
        }
        else{
            Node aux = null;
            int i = 0;
            while((aux == null) && (i < n.getSubtreesSize())){//enquanto não encontrou
                aux = searchNodeRef(elem, n.getSubtree(i));
                i++;
            }
            return aux;
        }
    }

    //O(n)
    private Node searchTaskNodeRef(Task elem, Node n) {
        if(n == null)
            return null;
        if(n.element != null && elem.getName().equals(n.element.getName()) 
            && elem.getTime() == n.element.getTime()){
            return n;
        }
        else{
            Node aux = null;
            int i = 0;
            while((aux == null) && (i < n.getSubtreesSize())){//enquanto não encontrou
                aux = searchTaskNodeRef(elem, n.getSubtree(i));
                i++;
            }
            return aux;
        }
    }
    
    private class Node {
        // Atributos da classe Node
        public Node father;
        public Task element;
        public LinkedList<Node> subtrees;
        // Métodos da classe Node
        public Node(Task element) {
            father = null;
            this.element = element;
            subtrees = new LinkedList<>();
        }
        private void addSubtree(Node n) {
            n.father = this;
            subtrees.add(n);
        }
        private boolean removeSubtree(Node n) {
            n.father = null;
            return subtrees.remove(n);
        }
        public Node getSubtree(int i) {
            if ((i < 0) || (i >= subtrees.size())) {
                throw new IndexOutOfBoundsException();
            }
            return subtrees.get(i);
        }
        public int getSubtreesSize() {
            return subtrees.size();
        }
    }
}
