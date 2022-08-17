import java.io.IOException;
import java.util.ArrayList;

public class Memory extends Element{

    private final String cmd_1 = "top -bn 1 -i -c";
    private String regex = "KiB Mem :";
    private double mb_used, mb_total;

    public Memory(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setName("RAM usage");
        this.setMeasure("MB");
    }

    public double getMb_used(){
        return this.mb_used;
    }

    public double getMb_total(){
        return this.mb_total;
    }

    @Override
    public void show(){
        System.out.print(this.getName() + ": " + this.getMb_used() + " " + this.getMeasure() + " | Total: " + this.getMb_total() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public void grab(){
        ArrayList<String> parse_res;
        parse_res = this.getResult();
        double temp1, temp2;
        String temp;
        String parts[] = parse_res.get(0).split(" ");
        for(int i = 0; i < parts.length; i++){
            if(parts[i].contains("used")){
                temp1 = (Double.valueOf(parts[i-1]) / 1024);
                temp = String.format("%.2f",temp1);
                this.mb_used = Double.valueOf(temp);
            }
            if(parts[i].contains("total")){
                temp2 = (Double.valueOf(parts[i-1]) / 1024);
                temp = String.format("%.2f",temp2);
                this.mb_total = Double.valueOf(temp);
            }
        }
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
