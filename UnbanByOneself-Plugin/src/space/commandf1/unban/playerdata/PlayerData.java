package space.commandf1.unban.playerdata;

import java.io.Serializable;

public class PlayerData implements Serializable {
    private final String hwid;
    private int times;

    public PlayerData(String hwid) {
        this.hwid = hwid;
    }

    public String getHwid() {
        return this.hwid;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
