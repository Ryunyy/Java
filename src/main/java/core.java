import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class Core {
    public static void main(String[] args) {
        ArrayList<Processor> proc = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            proc.add(new Processor());
            proc.get(i).setName("Proccessor Thread #" + (i+1));
            Random rand = new Random();
            proc.get(i).setValue(rand.nextInt(100));
            proc.get(i).setMeasure("KB");

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
            String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты
            proc.get(i).setDate(current_time);
        }

        show(proc);
    }

    public static void show(ArrayList<Processor> proc){
        for(int i= 0; i < proc.size(); i++){
            proc.get(i).show_info();
        }
    }
}