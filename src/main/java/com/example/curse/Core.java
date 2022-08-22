package com.example.curse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Core {


    public Core() throws InterruptedException {

    }

    public void start(){
        Processor proc = new Processor(); //создание экземпляра класса для сборки информации об использовании процессора
        Memory mem = new Memory();
        Disk disk = new Disk();
        Interface intfc = new Interface();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm_ss"); //создание формата даты
        String current_time; //получение текущей даты
        System.out.println();
        current_time = dateTimeFormatter.format(LocalDateTime.now());
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
    }
}
