package com.example.bistos.myvet.Service;

import android.util.Log;

import com.example.bistos.myvet.Model.Animal;
import com.example.bistos.myvet.Model.BaseAnimal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
/**
 * Created by Bistos12 on 1/17/2017.
 */

public class Service {

    private Realm realm;
    private ArrayList<Animal> animals;
    private String[] animalTypes = {"Dog", "Cat", "Rabbit", "Snake", "Hamster", "Chameleon"};

    private  BehaviorSubject<RealmResults<Animal>>  animalSubject;
    private ValueEventListener animalsListLisener;

    //Firebase
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mAnmialsRef=database.getReference("animals");

    //DatabaseReference mAnmialsRef=mRootRef.child("animals");

    public Service() {
        //final RealmConfiguration config = new RealmConfiguration.Builder(this).build();

       // Realm.setDefaultConfiguration(config);
        realm=Realm.getDefaultInstance();
       // mRootRef= FirebaseDatabase.getInstance().getReference();

        animalSubject=BehaviorSubject.create(getAnimals());

        animalsListLisener=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                RealmResults<Animal> allFromRealm=realm.where(Animal.class).findAll();
                realm.beginTransaction();
                allFromRealm.clear();
                realm.commitTransaction();
                for (DataSnapshot data:dataSnapshot.getChildren()
                     ) {
                    realm.beginTransaction();
                    Animal animal=data.getValue(Animal.class);
                    animal.setId(data.getKey());
                    realm.copyToRealm(animal);
                    realm.commitTransaction();
                }
                allFromRealm=realm.where(Animal.class).findAll();
                animalSubject.onNext(allFromRealm);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("onCancelled","Error at value listner");

            }
        };
        mAnmialsRef.addValueEventListener(animalsListLisener);
    }

    public RealmResults<Animal> getAnimals(){

        int a=3;
        return realm.allObjects(Animal.class);
    }

    public List<Animal> getAnimalsList(){
        RealmResults<Animal> realmAnimals=getAnimals();
        List<Animal> animals=new ArrayList<>();
        for (Animal a:realmAnimals
             ) {
            animals.add(a);
        }
        return animals;
    }

    public void addAnimal(String name,String type){
        BaseAnimal baseAnimal=new BaseAnimal(type,name);
        DatabaseReference newAnimaReference=mAnmialsRef.push();
        newAnimaReference.setValue(baseAnimal);

        realm.beginTransaction();
        Animal animal=realm.createObject(Animal.class);
        animal.setName(name);
        animal.setType(type);
        animal.setId(newAnimaReference.getKey());
        realm.commitTransaction();


    }

    public void updateAnimal(String id,String name,String type){
        realm.beginTransaction();
        Animal animal=realm.where(Animal.class).equalTo("id",id).findFirst();
        animal.setType(type);
        animal.setName(name);
        realm.commitTransaction();
        BaseAnimal baseAnimal=new BaseAnimal(type,name);
        DatabaseReference updateReference=mAnmialsRef.child(id);
        updateReference.setValue(baseAnimal);
    }

    public void deleteAnimal(String id){
        realm.beginTransaction();;
        Animal animal=realm.where(Animal.class).equalTo("id",id).findFirst();
        if(animal!=null){
            animal.removeFromRealm();
        }
        realm.commitTransaction();
        mAnmialsRef.child(id).removeValue();

    }

    public int[] getEveryTypeCount(){
        int[] myList = new int[10];
        for (int i = 0; i < animalTypes.length; i++) {
            int nr = realm.where(Animal.class).equalTo("type", animalTypes[i]).findAll().size();
            myList[i] = nr;
        }
        return myList;
    }
}
