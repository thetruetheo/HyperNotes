package com.example.hypernotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {


    FloatingActionButton mCreateNotesFAB;
    private FirebaseAuth firebaseAuth;

    RecyclerView mRecyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<FirebaseModel,NoteViewHolder> noteAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mCreateNotesFAB=findViewById(R.id.createNoteFAB);
        firebaseAuth=FirebaseAuth.getInstance();

        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();




        mCreateNotesFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NotesActivity.this,CreateNoteActivity.class));

            }
        });


        Query query=firebaseFirestore.collection("notes").
                document(firebaseUser.getUid()).collection("myNotes").
                orderBy("title",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<FirebaseModel> allUserNotes=
                new FirestoreRecyclerOptions.Builder<FirebaseModel>().
                        setQuery(query,FirebaseModel.class).build();

        noteAdapter=new FirestoreRecyclerAdapter<FirebaseModel, NoteViewHolder>(allUserNotes) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull FirebaseModel model) {

                holder.notetitle.setText(model.getTitle());
                holder.notecontent.setText(model.getContent());


            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.notes_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };


        mRecyclerView=findViewById(R.id.notesRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager=
                new StaggeredGridLayoutManager
                        (2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setAdapter(noteAdapter);


    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView notetitle;
        private TextView notecontent;
        LinearLayout mnote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            notetitle=itemView.findViewById(R.id.noteTitle);
            notecontent=itemView.findViewById(R.id.noteContent);
            mnote=itemView.findViewById(R.id.note);

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.logout){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(NotesActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();

        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(noteAdapter!=null){
            noteAdapter.stopListening();
        }
    }
}