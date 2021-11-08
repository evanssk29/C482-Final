package Model;

public class InHouse extends Part {

    private int machineID;

    public InHouse(int partID, String name, double price, int numInStock, int min, int max, int machID) {

        setId(partID);
        setName(name);
        setPrice(price);
        setStock(numInStock);
        setMin(min);
        setMax(max);
        setMachineID(machID);
    }

    public int getMachineID() {
        return machineID;
    }

    public void setMachineID(int id) {
        this.machineID = id;
    }

}
