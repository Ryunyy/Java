import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Core {
    public static void main(String[] args) {

        Random rand = new Random();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты

        Processor proc = new Processor();
        proc.grab();
        proc.setDate(current_time);
        proc.show();

        Memory mem = new Memory();
        mem.grab();
        mem.setDate(current_time);
        mem.show();

        Disk disk = new Disk();
        disk.grab();
        disk.setDate(current_time);
        disk.show();

        /*Interface intfc = new Interface();
        intfc.setName("Bytes sent: ");
        intfc.setMeasure("B");
        intfc.setValue(rand.nextInt(1000000));
        intfc.setDate(current_time);
        intfc.show();*/
    }
}
