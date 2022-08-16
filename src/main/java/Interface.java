public class Interface extends Element{

    private final String cmd_1 = "ifconfig";

    public Interface(){
        this.setCmd(cmd_1);

    }

    @Override
    public boolean recordInDB(){
        return false;
    }

}
