package com.bilalbaloch.wifidirecttestapp.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bilalbaloch.wifidirecttestapp.utils.DeveloperKey;
import com.bilalbaloch.wifidirecttestapp.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

/**
 * Created by bilal on 2017-11-02.
 */

public class CustomYoutubeFragment
        extends com.google.android.youtube.player.YouTubePlayerFragment
        implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerFragment playerFragment;
    private YouTubePlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {

        init();
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    private void init() {
        playerFragment = (YouTubePlayerFragment) getFragmentManager().
                findFragmentById(R.id.youtube_player_fragment);

        playerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        mPlayer = youTubePlayer;

        //Enables automatic control of orientation
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        //Show full screen in landscape mode always
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);

        //System controls will appear automatically
        mPlayer.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CONTROL_SYSTEM_UI);

        mPlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {
                mPlayer.play();
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        });

        if (!wasRestored) {
            //player.cueVideo("QeMuEWxtjVk");
            mPlayer.loadVideo("QeMuEWxtjVk");
        } else {
            mPlayer.play();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        mPlayer = null;
    }
}
