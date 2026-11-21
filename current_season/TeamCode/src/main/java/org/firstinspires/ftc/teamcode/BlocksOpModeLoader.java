package org.firstinspires.ftc.teamcode;

import android.content.Context;
import android.os.FileObserver;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.ftccommon.external.OnCreateEventLoop;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.File;

/**
 * Workaround so our Blocks OpModes (programs made with drag-and-drop Blocks)
 * show up on the Driver Station after the robot starts.
 *
 * Why it's needed: Pedro Pathing's tuning library includes "Sloth", a tool that
 * reloads code onto the robot quickly. Sloth replaces the FTC list of OpModes.
 * Its Blocks support (version 0.3.2) only adds Blocks OpModes when a file in the
 * robot's Blocks folder CHANGES, so right after the robot starts, no Blocks
 * OpModes are listed. Saving any Blocks OpMode in the Blocks editor makes them
 * appear; this does the same thing automatically at startup.
 *
 * How: @OnCreateEventLoop is an FTC "app hook". The robot app runs this method
 * once when it starts. We ask Sloth's own Blocks loader to run, so Sloth keeps
 * track of these OpModes and later Blocks edits still update the list correctly.
 *
 * Sloth comes inside Pedro's library and isn't visible to our code when it's
 * compiled, so we look it up by name while the app runs ("reflection"). If a
 * future Pedro update removes Sloth, this quietly does nothing.
 *
 * Remove this file once Sloth loads Blocks OpModes at startup by itself.
 */
public final class BlocksOpModeLoader {

    private static final String TAG = "BlocksOpModeLoader";
    private static final String SLOTH_BLOCKS_LOADER =
            "dev.frozenmilk.sinister.sdk.opmodes.BlocksOpModeRegistrar";

    private BlocksOpModeLoader() {
    }

    @OnCreateEventLoop
    public static void loadBlocksOpModes(Context context, FtcEventLoop eventLoop) {
        try {
            Class<?> loaderClass = Class.forName(SLOTH_BLOCKS_LOADER);
            Object loader = loaderClass.getField("INSTANCE").get(null);
            // Pretend a file in the Blocks folder was just saved; Sloth then
            // (re)registers every Blocks OpMode in that folder.
            loaderClass.getMethod("onEvent", int.class, File.class)
                    .invoke(loader, FileObserver.CLOSE_WRITE, AppUtil.BLOCK_OPMODES_DIR);
            RobotLog.ii(TAG, "Asked Sloth to load Blocks OpModes");
        } catch (ClassNotFoundException notInstalled) {
            RobotLog.ii(TAG, "Sloth isn't installed; the normal FTC code loads Blocks OpModes");
        } catch (Throwable error) {
            // Never let this workaround stop the robot app from starting.
            RobotLog.ee(TAG, error, "Couldn't load Blocks OpModes");
        }
    }
}
