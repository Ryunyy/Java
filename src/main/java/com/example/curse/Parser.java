package com.example.curse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Parser { //метод из lab3 перестал работать по неизвестным причинам. недолго думая, нашел метод с помощью процесса
    
    public Parser(){
        
    }
    
    public ArrayList<String> getInfo(String cmd) throws IOException {
        String s; //строка для пкопирования строки из буфера
        ArrayList<String> output = new ArrayList<>(); //результирующий массив строк вывода
        Process p; //процесс для запуска команды

        try {
            p = Runtime.getRuntime().exec(cmd); //выполнение команды
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream())); //полусение рещультатов команды в буфер
            System.out.println();
            while ((s = br.readLine()) != null) {//пока не конец вывода
                output.add(s);//копируем строку по одной в массив вывода
                System.out.println(s);
            }
            System.out.println();
            p.waitFor(); //ожидание завершения процесса
            p.destroy(); //уничтожение процесса
        } catch (Exception e) {}
        return output; //возврат результатов команды
    }
}
