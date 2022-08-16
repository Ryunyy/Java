public class Disk extends Element{

    private final String cmd_1 = "df";

    public Disk(){
        this.setCmd(cmd_1);

    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
