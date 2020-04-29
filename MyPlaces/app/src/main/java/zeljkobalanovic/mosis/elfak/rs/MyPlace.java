package zeljkobalanovic.mosis.elfak.rs;

public class MyPlace {
    private String name;
    private String description;

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
