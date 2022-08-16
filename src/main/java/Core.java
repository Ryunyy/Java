import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class Core {
    public static void main(String[] args) {

        Random rand = new Random();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты

        /*Processor proc = new Processor();
        proc.grab();
        proc.setName("Processor occupancy");
        proc.setMeasure("%");
        proc.setValue(proc.getSum());
        proc.setDate(current_time);
        proc.show();
        System.out.print("\n\n");*/


        Memory mem = new Memory();
        mem.grab();
        mem.setName("RAM usage: ");
        mem.setMeasure("MB");
        mem.setValue(mem.getMb());
        mem.setDate(current_time);
        mem.show();

        /*Disk disk = new Disk();
        disk.setName("ROM occupancy: ");
        disk.setMeasure("GB");
        disk.setValue(rand.nextInt(100));
        disk.setDate(current_time);
        disk.show();

        Interface intfc = new Interface();
        intfc.setName("Bytes sent: ");
        intfc.setMeasure("B");
        intfc.setValue(rand.nextInt(1000000));
        intfc.setDate(current_time);
        intfc.show();*/
    }
}
