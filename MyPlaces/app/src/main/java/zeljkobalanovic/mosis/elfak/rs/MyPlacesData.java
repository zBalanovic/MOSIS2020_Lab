package zeljkobalanovic.mosis.elfak.rs;

import java.util.ArrayList;

public class MyPlacesData {
    private ArrayList<MyPlace> myPlaces;

    private MyPlacesData(){
        myPlaces = new ArrayList<MyPlace>();
    }

    private static class SingltonHolder{
        public static final MyPlacesData instance = new MyPlacesData();
    }

    public static MyPlacesData getInstance(){
        return SingltonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces(){
        return myPlaces;
    }

    public void addNewPlace(MyPlace newPlace){
        myPlaces.add(newPlace);
    }

    public MyPlace getPlace(int index){
        return myPlaces.get(index);
    }

    public void deletePlace(int index){
        myPlaces.remove(index);
    }
}
