package lmu.de.unificiencyandroid.components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import lmu.de.unificiencyandroid.R;

public class Chat extends AppCompatActivity {
  @BindView(R.id.chat_view)
  ChatView chatView;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.chat);
    ButterKnife.bind(this);

    chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
      @Override
      public boolean sendMessage(ChatMessage chatMessage){
        Logger.d("Chat: " + chatMessage.getMessage());
        return true;
      }
    });

    chatView.setTypingListener(new ChatView.TypingListener(){
      @Override
      public void userStartedTyping(){
        Logger.d("Chat: typing" );
      }

      @Override
      public void userStoppedTyping(){
        Logger.d("Chat: stopped typing");
      }
    });

  }
}
