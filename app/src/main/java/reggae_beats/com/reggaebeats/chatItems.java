package reggae_beats.com.reggaebeats;

public class chatItems {
    private String Chats;
    private String Users;
    String Time;

    public chatItems(String chats, String users, String time) {
        Chats = chats;
        Users = users;
        Time = time;
    }

    public String getChats() {
        return Chats;
    }

    public void setChats(String chats) {
        Chats = chats;
    }

    public String getUsers() {
        return Users;
    }

    public void setUsers(String users) {
        Users = users;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}