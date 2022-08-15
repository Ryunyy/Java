import java.io.IOException;

public class Processor extends Element{

    private String cmd = "top -bn 1 ";
    public String result = "";

    public Processor(){

    }

    @Override
    public void show_info(){
        try {
            parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("\n" + this.getName() + ": " + this.getValue() + " " + this.getMeasure() + " [" + this.getDate() + "]" );
        if(result.length() > 0) {
            System.out.print("\nResult of command:\n");
            System.out.println(result);
        }
    }

    public void parse() throws IOException {
        Parser parser = new Parser();
        result = parser.getInfo(cmd);
    }
}
