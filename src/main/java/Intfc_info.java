public class Intfc_info {
    private String name;
    private int rx, tx;

    public Intfc_info(String new_name){
        if(new_name.length() > 0)
            this.name = new_name;
    }

    public String getName(){
        return this.name;
    }

    public void setRx(int value){
        if(value > 0)
            this.rx = value;
    }

    public int getRx(){
        return this.rx;
    }

    public void setTx(int value){
        if(value > 0)
            this.tx = value;
    }

    public int getTx(){
        return this.tx;
    }
}
