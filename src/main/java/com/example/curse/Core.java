package com.example.curse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Core {

    Processor proc = new Processor(); //создание экземпляра класса для сборки информации об использовании процессора
    Memory mem = new Memory();
    Disk disk = new Disk();
    Interface intfc = new Interface();
    public Core() throws InterruptedException {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time; //получение текущей даты
        System.out.println();

        Properties prop = new Properties();
        String fileName = "src/main/resources/static/my_config.cfg";
        FileInputStream fis = null;
        int repeat = 0;
        long delay = 0;
        try {
            fis = new FileInputStream(fileName);
            prop.load(fis);
            delay = Long.valueOf(prop.getProperty("delay_time"));
            repeat = Integer.valueOf(prop.getProperty("repeat_times"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(repeat > 0) {
            current_time = dateTimeFormatter.format(LocalDateTime.now());
            Thread.sleep(delay);

            proc.setDate(current_time); // установка текущей даты
            proc.grab(); //сбор информации
            proc.show(); //вывод в консоль информации

            mem.setDate(current_time);
            mem.grab();
            mem.show();

            disk.setDate(current_time);
            disk.grab();
            disk.show();

            intfc.setDate(current_time);
            intfc.grab();
            intfc.show();
            repeat--;
        }
    }
}
