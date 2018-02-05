package yashit.chatsup;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import yashit.chatsup.DataObjects.ChatMessage;

public class PrivateChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private EditText message;
    private RecyclerView rv;

    private DatabaseReference root;
    private ArrayList<ChatMessage> chatObject = new ArrayList<>();
    private ChatMessageAdapter adapter;

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        username = getIntent().getStringExtra("SELECTED_USERNAME");

        this.setTitle(username);

        message = (EditText) findViewById(R.id.msg_input);
        rv = (RecyclerView) findViewById(R.id.chatPageRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        assert mUser != null;
        adapter = new ChatMessageAdapter(chatObject,mUser.getDisplayName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            rv.setAdapter(adapter);
        }
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setItemAnimator(new DefaultItemAnimator());
    }

    public void sendChat(View view) {
        String messageText = message.getText().toString();
        if(!messageText.isEmpty()) {
            chatObject.add(new ChatMessage(messageText, mUser.getDisplayName(), username));
            
            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(PrivateChatActivity.this, "Please Enter a Message", Toast.LENGTH_SHORT).show();
        }
    }
}
