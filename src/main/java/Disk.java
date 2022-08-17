import java.io.IOException;
import java.util.ArrayList;

public class Disk extends Element{

    private final String cmd_1 = "df --output=size,used,avail /";
    private String regex = "(.*)"+"(\\d+\\D+){2}"+"(\\d)";
    private double total_rom, used_rom;

    public Disk(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("GB");
        this.setName("ROM occupancy");
    }

    public double getTotal_rom(){
        return this.total_rom;
    }

    public double getUsed_rom(){
        return this.used_rom;
    }

    public void show(){
        System.out.print(this.getName() + ": " + this.getUsed_rom() + " " + this.getMeasure() + " | Total: " + this.getTotal_rom() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public void grab(){
        double temp1, temp2;
        ArrayList<String> parse_res;
        parse_res = this.getResult();
        String temp = "";
        String[] parts = parse_res.get(0).split(" ");
        for(int i = 0;i < parts.length; i++) {
            if(!parts[i].contains(" ")) {
                temp1 = Double.valueOf(parts[i]) / 1024 / 1024;
                temp2 = Double.valueOf(parts[i+1]) / 1024 / 1024;
                temp = String.format("%.2f", temp1);
                this.total_rom = Double.valueOf(temp);
                temp = String.format("%.2f", temp2);
                this.used_rom = Double.valueOf(temp);
            }
        }

    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
