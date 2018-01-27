package oreo.com.bittext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by rkark on 1/27/2018.
 */

public class CreateChat extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView users;
    String name;
    List<String> listOfUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat);
        name = getIntent().getStringExtra("name");

        users = (ListView)findViewById(R.id.users);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, listOfUsers);
        users.setAdapter(arrayAdapter);

        db.collection("users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                List<DocumentSnapshot> vals = documentSnapshots.getDocuments();
               for (DocumentSnapshot a : vals){
                   try {
                       String temp = a.get("username").toString();
                       if (!temp.equals(name)) {
                           listOfUsers.add(temp);
                           System.out.println(listOfUsers);
                           arrayAdapter.notifyDataSetChanged();
                       }
                   }catch (NullPointerException e){
                       e.printStackTrace();
                   }
               }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error Getting Users", Toast.LENGTH_LONG).show();
            }
        });

        users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent I = new Intent(getBaseContext(), Message.class);
                I.putExtra("name", listOfUsers.get(i));
                startActivity(I);
            }
        });




    }

}



