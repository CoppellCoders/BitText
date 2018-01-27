package oreo.com.bittext;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkark on 1/27/2018.
 */

public class Message extends Activity implements View.OnClickListener{
    private ChatArrayAdapter chatArrayAdapter;
    TextView recep;
    ListView messages;
    EditText message;
    Button send;
    List<String> list = new ArrayList<>();
    private boolean side = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message);
        String name = getIntent().getStringExtra("name");
        recep = (TextView)findViewById(R.id.recep);
        messages = (ListView)findViewById(R.id.messages);
        message = (EditText)findViewById(R.id.message);
        send = (Button)findViewById(R.id.send);
        findViewById(R.id.send).setOnClickListener(this);
        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        messages.setAdapter(chatArrayAdapter);
        recep.setText(name);
        messages.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messages.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                messages.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.send:
               sendChatMessage();

        }
    }
    private boolean sendChatMessage() {
        chatArrayAdapter.add(new ChatMessage(side, message.getText().toString()));
        message.setText("");
        side = !side;
        return true;
    }
}
