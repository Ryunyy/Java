import java.io.IOException;
import java.util.ArrayList;

public class Processor extends Element{

    private String cmd_1 = "top -bn 1 ";
    private String regex = "PID";
    private double sum_occupancy = 0;

    public Processor(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double getSum(){
        return this.sum_occupancy;
    }

    public void split(){
        int line_index = 0;
        String temp;
        String[] parts;
        ArrayList<String> lines;
        lines = this.getResult();

        while(line_index < lines.size()){
            temp = lines.get(line_index);
            parts = temp.split(" ");

            /*try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(parts[parts.length - 7]);
            System.out.println();*/

            this.sum_occupancy += Double.valueOf(parts[parts.length - 7]);
            line_index++;
        }
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
