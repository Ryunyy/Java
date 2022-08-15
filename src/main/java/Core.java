import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Core {
    public static void main(String[] args) {
        Processor proc = new Processor();
        proc.setName("Proccessor Thread #" + 1);
        Random rand = new Random();
        proc.setValue(rand.nextInt(100));
        proc.setMeasure("KB");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты
        proc.setDate(current_time);

        show(proc);
    }

    public static void show(Processor proc){
            proc.show_info();
    }
}
