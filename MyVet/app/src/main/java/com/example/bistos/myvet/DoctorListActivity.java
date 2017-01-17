package com.example.bistos.myvet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.bistos.myvet.Model.Animal;
import com.example.bistos.myvet.Service.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * An activity representing a list of Doctors. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DoctorDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class DoctorListActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_ID = "id";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private ArrayList<Animal> animals;
    private Realm realm;
    private AnimalAdapter animalAdapter;


    //new
    private Service service;
    //

    public static final String PETS="pets";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    DatabaseReference mAnmialsRef=mRootRef.child("animals");
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_list);
        //realm = Realm.getInstance(this);


        realm=Realm.getDefaultInstance();
        animals = new ArrayList<>();


        //new
        service=new Service();

        //

        //Firebase stuff
        mAuth=FirebaseAuth.getInstance();

        mAuthListener=new FirebaseAuth.AuthStateListener(){
          @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
              FirebaseUser user=firebaseAuth.getCurrentUser();
              if(user!=null){
                  Log.d("User is signed in"," ");
                  Toast.makeText(DoctorListActivity.this, "Welcome"+user.getEmail(),
                          Toast.LENGTH_SHORT).show();
              }
          }
        };



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoctorListActivity.this, DoctorDetailActivity.class);
                startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.doctor_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.doctor_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:{
                NavUtils.navigateUpTo(this,new Intent(this,HomeActivity.class));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        animalAdapter=new AnimalAdapter(service.getAnimals());
        recyclerView.setAdapter(animalAdapter);
    }

    public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.ViewHolder> implements RealmChangeListener {

        private final RealmResults<Animal> mValues;

        public AnimalAdapter(RealmResults<Animal> items) {
            mValues = items;
            items.addChangeListener(this);
        }

        @Override
        public void onChange() {
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.doctor_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.name.setText(mValues.get(position).getName());
            holder.type.setText(mValues.get(position).getType());


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(DoctorDetailFragment.ARG_ITEM_ID, holder.mItem.getName());
                        DoctorDetailFragment fragment = new DoctorDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.doctor_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DoctorDetailActivity.class);
                        intent.putExtra(EXTRA_NAME, holder.mItem.getName());
                        intent.putExtra(EXTRA_TYPE, holder.mItem.getType());
                        intent.putExtra(EXTRA_ID,holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView name;
            public final TextView type;
            public Animal mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.name);
                type = (TextView) view.findViewById(R.id.type);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + type.getText() + "'";
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
