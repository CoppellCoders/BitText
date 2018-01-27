package oreo.com.bittext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class HomeScreen extends Activity{


    TextView header;
    ListView chats;
    FloatingActionButton add;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String name;
    boolean has;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        name = acct.getDisplayName();
        header = (TextView)findViewById(R.id.header);
        header.setText(name);
        chats = (ListView)findViewById(R.id.chats);
        add = (FloatingActionButton)findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(getBaseContext(), CreateChat.class);
                I.putExtra("name", name);
                startActivity(I);
            }
        });
        final List<String> chatNames = new ArrayList<>();
        chatNames.add("Rohith K");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, chatNames);
        chats.setAdapter(arrayAdapter);
        addUsers(name);
        chatNames.add("Hello");

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent(getBaseContext(), Message.class);
                I.putExtra("name", chatNames.get(i));
                startActivity(I);
            }
        });

    }
    public void addUsers(final String name){
        has = true;
        DocumentReference docRef = db.collection("users").document(name);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.d(TAG, "DocumentSnapshot data: " + task.getResult().getData());
                        has = false;
                    } else {

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        if(!has) return;
        Map<String, Object> user = new HashMap<>();
        user.put("username", name);
        db.collection("users").document(""+name)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

}
