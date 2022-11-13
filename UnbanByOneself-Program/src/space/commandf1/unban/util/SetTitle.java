package space.commandf1.unban.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface SetTitle extends Library {
    SetTitle INSTANCE = (SetTitle) Native.loadLibrary((Platform.isWindows() ? "kernel32" : "c"), SetTitle.class);

    void SetConsoleTitleA(String title);
}
