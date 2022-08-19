package com.example.curse;

import java.io.IOException;
import java.util.ArrayList;

public class Processor extends Element{

    private String cmd_1 = "top -bn 1 "; //командя для парсинга результатов
    private String regex = "[^%](\\D+\\d+){7}"+"(\\.)?\\d\\D+\\d+(\\.)?\\d"+"(\\D+\\d+){2}"+"(\\.)"+"(\\d+\\D+)"; //регулярка для получения результатов таблицы
    private double sum_occupancy = 0; //итоговая загруженность процессора

    public Processor(){
        this.setCmd(cmd_1); //установка команды для парсинга
        this.setRegex(regex); //установка регулярки
        try {
            this.parse(); //парсинг команды
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("%"); //установка ед.измерений
        this.setName("CPU occupancy"); //установка названия (для веба)
    }

    public double getSum(){ //геттер загруженности процессора
        return this.sum_occupancy;
    }

    @Override
    public void grab(){
        int line_index = 0; //номер линии в спаршеном выводе
        String temp; //строка для ее разбиения и поиска нужного параметра
        String[] parts; //массив частей, на которые была разбита строка
        ArrayList<String> lines; //массив строк для поиска среди них нужных параметров
        lines = this.getResult(); //копирование из буффера результатов команды

        while(line_index < lines.size()){ //пока не дошли до конца вывода
            temp = lines.get(line_index); //копируем строку для ее разбиения
            parts = temp.split(" "); //разбиваем строку по пробелам
            System.out.println();

            this.sum_occupancy += Double.valueOf(parts[parts.length - 7]); //получаем нужное значение (из столбца) и суммируем его с предыдущими для подсчета общей загруженности CPU
            line_index++; //индекс для следующей строки
        }
        this.setValue(this.getSum()); //установка итоговой суммы
    }

    @Override
    protected void show(){ //вывод информации в консоль
        System.out.print(this.getName() + ": " + this.getValue() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public boolean recordInDB(){ //запись в БД
        return false;
    }

}
