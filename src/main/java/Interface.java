import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interface extends Element{

    private final String cmd_1 = "ifconfig";
    private String regex = ".+";
    private String regexName = ".+" + "<UP,"+"(.+)+"+">"+"(.+)";
    private String regexRX = "(.+)"+"RX packets"+"(.+)";
    private String regexTX = "(.+)"+"TX packets"+"(.+)";
    ArrayList<Intfc_info> int_info = new ArrayList<>();

    public Interface(){
        this.setCmd(cmd_1);
        this.setRegex(regex);
        try {
            this.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setMeasure("Bytes");
        this.setName("Interfaces TX/RX");

    }

    @Override
    public void grab() {
        int line_index = 0;
        String temp;
        String[] parts;
        ArrayList<String> lines;
        lines = this.getResult();

        while(line_index < lines.size()){
            temp = lines.get(line_index);

            Pattern pattern = Pattern.compile(regexName);
            Matcher matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(":");
                int_info.add(new Intfc_info(parts[0]));
            }


            pattern = Pattern.compile(regexRX);
            matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(" ");
                for(int i = 0; i < parts.length; i++){
                    if(parts[i].contains("bytes")){
                        int_info.get(int_info.size()-1).setRx(Integer.valueOf(parts[i+1]));
                    }
                }
            }

            pattern = Pattern.compile(regexTX);
            matcher = pattern.matcher(temp);
            if(matcher.find()){
                parts = temp.split(" ");
                for(int i = 0; i < parts.length; i++){
                    if(parts[i].contains("bytes")){
                        int_info.get(int_info.size()-1).setTx(Integer.valueOf(parts[i+1]));
                    }
                }
            }
            line_index++;
        }
    }

    @Override
    public void show(){
        System.out.print(this.getName() + "\n");
        for(int i = 0; i < int_info.size(); i++){
            System.out.print("Name of interface: " + int_info.get(i).getName());
            System.out.print(" | RX: "+int_info.get(i).getRx() + " " + this.getMeasure());
            System.out.print(" | TX: "+int_info.get(i).getTx() + " " + this.getMeasure());
            System.out.print(" [" + this.getDate() + "]");
            System.out.println();
        }
    }

    @Override
    public boolean recordInDB(){
        return false;
    }


}
