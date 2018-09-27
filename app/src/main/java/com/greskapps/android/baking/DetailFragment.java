package com.greskapps.android.baking;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class DetailFragment extends Fragment implements ExoPlayer.EventListener {
    int position;
    private static final String TAG = DetailFragment.class.getSimpleName();
    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    long playerPosition;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null)
            playerPosition = savedInstanceState.getLong("playerPosition");

        final View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        mPlayerView = rootView.findViewById(R.id.mediaViewer);
        final TextView textView = rootView.findViewById(R.id.description_tv);
        Button prevBtn = rootView.findViewById(R.id.prev_btn);
        Button nextBtn = rootView.findViewById(R.id.next_btn);
        final Recipe recipe = getActivity().getIntent().getParcelableExtra("parcel_data");

        position = 0;
        if (getArguments() != null)
            position = getArguments().getInt("position", 0);
        textView.setText(recipe.instructions.get(position));

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.ic_launcher_foreground));

        initializeMediaSession();

        if (!recipe.media.get(position).equals(""))
            initializePlayer(Uri.parse(recipe.media.get(position)));
        else
            mPlayerView.setVisibility(View.GONE);

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != 0) {
                    position -= 1;
                    textView.setText(recipe.instructions.get(position));

                    if (mExoPlayer == null) {
                        if (!recipe.media.get(position).equals("")) {
                            mPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else
                            mPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")) {
                            releasePlayer();
                            mPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            mPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                } else {
                    position = recipe.instructions.size() - 1;
                    textView.setText(recipe.instructions.get(recipe.instructions.size() - 1));

                    if (mExoPlayer == null) {
                        if (!recipe.media.get(position).equals(""))
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        else
                            mPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")) {
                            releasePlayer();
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            mPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                }
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != recipe.instructions.size() - 1) {
                    position += 1;
                    textView.setText(recipe.instructions.get(position));

                    if (mExoPlayer == null) {
                        if (!recipe.media.get(position).equals("")) {
                            mPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else
                            mPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")) {
                            releasePlayer();
                            mPlayerView.setVisibility(View.VISIBLE);
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            mPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                } else {
                    position = 0;
                    textView.setText(recipe.instructions.get(0));

                    if (mExoPlayer == null) {
                        if (!recipe.media.get(position).equals(""))
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        else
                            mPlayerView.setVisibility(View.GONE);
                    } else {
                        if (!recipe.media.get(position).equals("")) {
                            releasePlayer();
                            initializePlayer(Uri.parse(recipe.media.get(position)));
                        } else {
                            mPlayerView.setVisibility(View.GONE);
                            releasePlayer();
                        }
                    }
                }
            }
        });

        return rootView;
    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);

        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Don't restart the player if the app is invisible.
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new MySessionCallback());

        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);

            String userAgent = Util.getUserAgent(getContext(), "baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
        mExoPlayer.seekTo(playerPosition);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null)
            releasePlayer();
        mMediaSession.setActive(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
            Bundle bundle = new Bundle();
            onSaveInstanceState(bundle);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong("playerPosition", mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }


    @Override
    public void onRepeatModeChanged(int repeatMode) {
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    @Override
    public void onSeekProcessed() {
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }
}
