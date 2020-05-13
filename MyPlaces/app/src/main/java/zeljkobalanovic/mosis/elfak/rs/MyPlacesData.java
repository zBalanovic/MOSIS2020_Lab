package zeljkobalanovic.mosis.elfak.rs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPlacesData {

    private ArrayList<MyPlace> myPlaces;
    private HashMap<String, Integer> myPlacesKeyIndexMapping;
    private DatabaseReference database;
    private static final String FIREBASE_CHILD = "my-places";

    private MyPlacesData(){
        myPlaces = new ArrayList<MyPlace>();
        myPlacesKeyIndexMapping = new HashMap<String, Integer>();
        database = FirebaseDatabase.getInstance().getReference();
        database.child(FIREBASE_CHILD).addChildEventListener(childEventListener);
        database.child(FIREBASE_CHILD).addListenerForSingleValueEvent(parentEventListener);
    }

    ValueEventListener parentEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            if(!myPlacesKeyIndexMapping.containsKey(myPlaceKey)){
                MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
                myPlace.key = myPlaceKey;
                myPlaces.add(myPlace);
                myPlacesKeyIndexMapping.put(myPlaceKey, myPlaces.size() - 1);
                if(updateListener != null){
                    updateListener.onListUpdated();
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String myPlaceKey = dataSnapshot.getKey();
            MyPlace myPlace = dataSnapshot.getValue(MyPlace.class);
            myPlace.key = myPlaceKey;
            if(myPlacesKeyIndexMapping.containsKey(myPlaceKey)){
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.set(index, myPlace);
            } else{
                myPlaces.add(myPlace);
                myPlacesKeyIndexMapping.put(myPlaceKey, myPlaces.size() - 1);
            }
            if(updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            String myPlaceKey = dataSnapshot.getKey();
            if(myPlacesKeyIndexMapping.containsKey(myPlaceKey)){
                int index = myPlacesKeyIndexMapping.get(myPlaceKey);
                myPlaces.remove(index);
                recreateKeyIndexMapping();
                if(updateListener != null)
                    updateListener.onListUpdated();
            }
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private static class SingletonHolder{

        public static final MyPlacesData instance = new MyPlacesData();
    }
    public static MyPlacesData getInstance(){
        return SingletonHolder.instance;
    }

    public ArrayList<MyPlace> getMyPlaces(){
        return myPlaces;
    }

    public void addNewPlace(MyPlace newPlace){
        String key = database.push().getKey();
        myPlaces.add(newPlace);
        myPlacesKeyIndexMapping.put(key, myPlaces.size()-1);
        database.child(FIREBASE_CHILD).child(key).setValue(newPlace);
        newPlace.key = key;
    }

    public void updatePlace(int index, String name, String desc, String lng, String lat){
        MyPlace myPlace = myPlaces.get(index);
        myPlace.name = name;
        myPlace.description = desc;
        myPlace.longitude = lng;
        myPlace.latitude = lat;
        database.child(FIREBASE_CHILD).child(myPlace.key).setValue(myPlace);
    }

    public void deletePlace(int index){
        database.child(FIREBASE_CHILD).child(myPlaces.get(index).key).removeValue();
        myPlaces.remove(index);
        recreateKeyIndexMapping();
    }

    private void recreateKeyIndexMapping(){
        myPlacesKeyIndexMapping.clear();
        for (int i = 0; i < myPlaces.size(); i++) {
             myPlacesKeyIndexMapping.put(myPlaces.get(i).key, i);
        }
    }

    public MyPlace getPlace(int index){
        return myPlaces.get(index);
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener){
        updateListener = listener;
    }

    public interface ListUpdatedEventListener{
        void onListUpdated();
    }

}
