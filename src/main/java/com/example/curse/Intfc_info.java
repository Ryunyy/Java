package com.example.curse;

public class Intfc_info { //вспомогательный класс для хранения информации о интерфейсах
    private String name; //имя интерфейса
    private int rx, tx; //значения принятых/отправленных байтов

    public Intfc_info(String new_name){
        if(new_name.length() > 0)
            this.name = new_name; //установка имени
    }

    public String getName(){ //геттер имени
        return this.name;
    }

    public void setRx(int value){ //сеттер принятых байтов
        if(value > 0)
            this.rx = value;
    }

    public int getRx(){ //геттер принятых байтов
        return this.rx;
    }

    public void setTx(int value){ //сеттер переданных байтов
        if(value > 0)
            this.tx = value;
    }

    public int getTx(){ //геттер переданных байтов
        return this.tx;
    }
}
