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

    public void split(){
        int line_index = 0;
        String temp;
        String[] parts;
        ArrayList<String> lines;
        lines = this.getResult();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while(line_index < lines.size()){
            temp = lines.get(line_index);
            parts = temp.split(" ");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print("|"+parts[parts.length - 7]+"|");
            System.out.println();
            line_index++;
        }
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
