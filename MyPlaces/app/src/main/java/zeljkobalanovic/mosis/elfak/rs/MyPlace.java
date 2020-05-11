package zeljkobalanovic.mosis.elfak.rs;

public class MyPlace {
    private String name;
    private String description;
    private String longitude;
    private String latitude;
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public MyPlace(String name, String description){
        this.name = name;
        this.description = description;
    }

    public MyPlace(String name){
        this(name, "");
    }

    public String getName(){
        return this.name;
    }

    public void setName(String newName){
        this.name = newName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
