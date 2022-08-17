import java.io.IOException;
import java.util.ArrayList;

public class Processor extends Element{

    private String cmd_1 = "top -bn 1 ";
    private String regex = "(\\D+\\d+){7}"+"(\\.)?\\d\\D+\\d+(\\.)?\\d"+"(\\D+\\d+){2}"+"(\\.)"+"(\\d+\\D+)";
    private double sum_occupancy = 0;

    public Processor(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("%");
        this.setName("CPU occupancy");
    }

    public double getSum(){
        return this.sum_occupancy;
    }

    @Override
    public void grab(){
        int line_index = 0;
        String temp;
        String[] parts;
        ArrayList<String> lines;
        lines = this.getResult();

        while(line_index < lines.size()){
            temp = lines.get(line_index);
            parts = temp.split(" ");

            this.sum_occupancy += Double.valueOf(parts[parts.length - 7]);
            line_index++;
        }
        this.setValue(this.getSum());
    }

    @Override
    protected void show(){
        System.out.print(this.getName() + ": " + this.getValue() + " " + this.getMeasure() + " [" + this.getDate() + "]\n\n" );
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
