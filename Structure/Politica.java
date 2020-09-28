package Structure;
import Objects.Task;

import java.util.Comparator;
public enum Politica {
    MAX(maxComparator()), MIN(minComparator());

    private final Comparator<Task> comparablePolice;

    Politica(Comparator<Task> comparablePolice) {
        this.comparablePolice = comparablePolice;
    }
    
    public Comparator<Task> getComparator(){
        return this.comparablePolice;
    }
    
    private static Comparator<Task> maxComparator() {
        return Comparator.comparingInt(Task::getTime).reversed().thenComparing(alphabeticComparator());
    }
    
    private static Comparator<Task> minComparator() {
        return Comparator.comparingInt(Task::getTime).thenComparing(alphabeticComparator());
    }

    private static Comparator<Task> alphabeticComparator() {
        return Comparator.comparing(Task::getName);
    }
}
