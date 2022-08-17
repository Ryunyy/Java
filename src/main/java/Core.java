import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Core {
    public static void main(String[] args) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time = dateTimeFormatter.format(LocalDateTime.now()); //получение текущей даты

        Processor proc = new Processor(); //создание экземпляра класса для сборки информации об использовании процессора
        proc.grab(); //сбор информации
        proc.setDate(current_time); // установка текущей даты
        proc.show(); //вывод в консоль информации

        Memory mem = new Memory();
        mem.grab();
        mem.setDate(current_time);
        mem.show();

        Disk disk = new Disk();
        disk.grab();
        disk.setDate(current_time);
        disk.show();

        Interface intfc = new Interface();
        intfc.grab();
        intfc.setDate(current_time);
        intfc.show();
    }
}
