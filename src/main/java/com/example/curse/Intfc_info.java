package com.example.curse;

public class Intfc_info { //вспомогательный класс для хранения информации о интерфейсах
    private String name; //имя интерфейса
    private int rx_all, tx_all, rx_new, tx_new; //значения принятых/отправленных байтов

    public Intfc_info(String new_name){
        if(new_name.length() > 0)
            this.name = new_name; //установка имени
    }

    public String getName(){ //геттер имени
        return this.name;
    }

    public void setRxAll(int value){ //сеттер принятых байтов
        if(value > 0)
            this.rx_all = value;
    }

    public int getRxAll(){ //геттер принятых байтов
        return this.rx_all;
    }

    public void setTxAll(int value){ //сеттер переданных байтов
        if(value > 0)
            this.tx_all = value;
    }

    public int getTxAll(){ //геттер переданных байтов
        return this.tx_all;
    }

    public void setRxNew(int value){
        if(value > 0)
            this.rx_new = value;
    }

    public int getRxNew(){
        return this.rx_new;
    }

    public void setTxNew(int value){
        if(value > 0)
            this.tx_new = value;
    }

    public int getTxNew(){
        return this.tx_new;
    }
}
