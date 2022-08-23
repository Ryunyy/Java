package com.example.curse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Element {
    private String name = "", measure = "", cmd = "", regex = "", date = "no date"; //элементы для дочерних классов. используются по необходимости
    private String url = "jdbc:postgresql://localhost:5432/parse_res", user = "tester", password = "qwerty";
    private double value = 0;
    private ArrayList<String> result; // для получения вывода результатов команды

    private int max_count = 10;

    public Element(){

    }

    public int getMaxCount(){
        return this.max_count;
    }

    public String getUrl(){
        return this.url;
    }
    public String getUser(){
        return this.user;
    }
    public String getPassword(){
        return this.password;
    }

    public void setCmd(String new_cmd){ //сеттер команды
        if(new_cmd.length()>0)
            this.cmd = new_cmd;
    }

    public String getCmd(){ //геттер команды
        return this.cmd;
    }

    public void setRegex(String new_reg){ //сеттер регулярки
        if(new_reg.length() > 0)
            this.regex = new_reg;
    }

    public String getRegex(){ //геттер регулярки
        return regex;
    }

    public void setName(String new_name){ //сеттер имени
        if(new_name.length() > 0)
            this.name = new_name;
    }

    public String getName(){ //геттер имени
        return this.name;
    }

    public void setValue(double new_value){ //сеттер значения
        if(new_value >= 0)
            this.value = new_value;
    }

    public double getValue(){ //геттер значения
        return this.value;
    }

    public void setMeasure(String new_measure){ //сеттер ед.измерения
        if(new_measure.length() > 0)
            this.measure = new_measure;
    }

    public String getMeasure(){ //геттер ед.измерения
        return this.measure;
    }

    public void setDate(String new_date){ //сеттер даты
        if(date_check(new_date)) {
            this.date = new_date;
        }
    }

    public String getDate(){ //геттер даты
        return this.date;
    }

    protected abstract void show(); //метод вывода в консоль. переопределяемый

    public void setResult(ArrayList<String> new_res){ //сеттер вывода результатов команды
        if(new_res.size() >0)
            this.result = new_res;
    }

    public ArrayList<String> getResult(){ //гетер вывода результатов команды
        return this.result;
    }

    public void parse() throws IOException { //метод парсинга рещультатов команды
        Parser parser = new Parser(); //создание экземпляра класса
        try {
            this.result = parser.getInfo(cmd); //получение результатов вывода команды
        } catch (IOException e) {
            e.printStackTrace();
            System.out.print("parse unsuccessful");
        }
        filter(); //пропуск вывода через фильтр по регулярке

    }

    public void filter(){
        ArrayList<String> res = new ArrayList<>(); //массив строк вывода результатов команды
        int i = 0; //номер текущей строки
        while(i < result.size()){ //пока не конец вывода
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(result.get(i)); //если строка подходит по регулярке
            if (matcher.find()) {
                res.add(result.get(i)); //добавляем ее в массив подходящих строк
            }
            else{
            }
            i++;
        }
        this.setResult(res); //после того, как мы отсеяли ненужные строки, обновляем массив вывода, заменяя его на только нужные строки
    }

    public boolean date_check(String name){ //проверка даты по регулсярке. пока не знаю зачем, тестированл навыки с регулярками
        String regex = "(0[1-9]|[1-2]\\d|3[01])_(0[1-9]|1[0-2])_(\\d{4})_([01][0-9]|2[0-3])_([0-5][0-9])_([0-5][0-9])";
        return Pattern.matches(regex, name);
    }

    public abstract boolean recordInDB(); //запись в бд, переопределяемый

    public abstract void grab(); //метод сбора данных. у каждого класса свой - поэтому было проще сделать его абстрактным
}
