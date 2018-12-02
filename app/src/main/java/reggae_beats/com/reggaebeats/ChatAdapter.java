package reggae_beats.com.reggaebeats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.chatViewHolder> {
    private Context mCtx;
    private ArrayList<chatItems> chatItemsArrayList;
    private ArrayList<FriendsItem> friendsItemsArraylist;
    private int Layout;

    public ChatAdapter(Context ctx, ArrayList<chatItems> chatItemsArrayList, ArrayList<FriendsItem> friendsItemsArraylist, int Layout) {
        this.mCtx = ctx;
        this.chatItemsArrayList = chatItemsArrayList;
        this.friendsItemsArraylist = friendsItemsArraylist;
        this.Layout = Layout;
    }

    @Override
    public int getItemViewType(int position) {

        String currentUser = new SharedPreferenceConfig(mCtx).findValue("USERNAME");

        if ((chatItemsArrayList.get(position).getUsers()).equals(currentUser)) {
            return R.layout.me_bubble;
        } else {
            return R.layout.friend_bubble;
        }
    }

    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        return new chatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder holder, int position) {
        String currentUser = new SharedPreferenceConfig(mCtx).findValue("USERNAME");
        Toast.makeText(mCtx.getApplicationContext(), "CURRENT_USER" + currentUser, Toast.LENGTH_LONG).show();


        holder.person.setText(chatItemsArrayList.get(position).getUsers());

        holder.chat.setText(chatItemsArrayList.get(position).getChats());
        holder.time.setText(chatItemsArrayList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return chatItemsArrayList.size();
    }

    public static class chatViewHolder extends RecyclerView.ViewHolder {

        TextView person, chat, time;
        LinearLayout chat_container, overall_Container;

        public chatViewHolder(View itemView) {
            super(itemView);
            chat_container = itemView.findViewById(R.id.chat_container);

            person = itemView.findViewById(R.id.person);
            chat = itemView.findViewById(R.id.chat);
            time = itemView.findViewById(R.id.time_stamp);


        }


    }
}
