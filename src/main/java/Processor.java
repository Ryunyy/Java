public class Processor extends Element{

    private String cmd_1 = "top -bn 1 ";

    public Processor(){
        this.setCmd(cmd_1);
    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
