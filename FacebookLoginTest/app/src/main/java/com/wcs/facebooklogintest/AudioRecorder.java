package com.wcs.facebooklogintest;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.octiplex.android.rtmp.AACAudioFrame;
import com.octiplex.android.rtmp.AACAudioHeader;
import com.octiplex.android.rtmp.RtmpMuxer;

import net.sourceforge.jaad.aac.Decoder;
import net.sourceforge.jaad.aac.SampleBuffer;
import net.sourceforge.jaad.adts.ADTSDemultiplexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by apprenti on 12/06/17.
 */

public class AudioRecorder implements Runnable {

    private static final String MIME_TYPE = MediaFormat.MIMETYPE_VIDEO_AVC; // H.264 Advanced Video Coding
    private static final int FRAME_RATE = 15; // 15 fps
    private static final int IFRAME_INTERVAL = 1; // 1 seconds between I-frames
    private static final String AUDIO_MIME_TYPE = MediaFormat.MIMETYPE_AUDIO_AAC;
    private static final int AUDIO_CHANNEL_COUNT = 1;
    private static final int AUDIO_MAX_INPUT_SIZE = 8820;
    private static final int AUDIO_RECORD_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int AUDIO_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_RATE = 44100;
    private static final int AUDIO_BITRATE = 32000;

    /**
     * Instance of media recorder used to record audio
     */
    private MediaRecorder mediaRecorder;
    /**
     * File descriptors used to extract data from the {@link #mediaRecorder}
     */
    private ParcelFileDescriptor[] fileDescriptors;
    /**
     * Thread that will handle aac parsing using {@link #fileDescriptors} output
     */
    private Thread aacParsingThread;
    /**
     * Has AAC header been send yet
     */
    private boolean headerSent;
    /**
     * Already started muxer.
     */
    private RtmpMuxer muxer;

    private MediaCodec mAudioEncoder;

    public void configure( RtmpMuxer muxer) throws IOException
    {
        this.muxer = muxer;
        fileDescriptors = ParcelFileDescriptor.createPipe();
        aacParsingThread = new Thread(this);
        headerSent = false;

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // If you want to use the camera's microphone
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setAudioEncodingBitRate(128000);
        mediaRecorder.setAudioSamplingRate(48000);
        mediaRecorder.setOutputFile(fileDescriptors[1].getFileDescriptor());
        mediaRecorder.prepare();
    }

    public void startAudio()
    {
        mediaRecorder.start();
        aacParsingThread.start();
    }

    @Override
    public void run()
    {
        FileInputStream is = null;
        try
        {
            is = new FileInputStream(fileDescriptors[0].getFileDescriptor());
            ADTSDemultiplexer adts = new ADTSDemultiplexer(is);
            byte[] decoderSpecificInfo = adts.getDecoderSpecificInfo();
            byte[] frame;
            final Decoder dec = new Decoder(decoderSpecificInfo);

            final SampleBuffer buf = new SampleBuffer();

            while (true)
            {
                frame = adts.readNextFrame();
                dec.decodeFrame(frame, buf);

                // TODO parse AAC: This sample doesn't provide AAC extracting complete method since it's not the purpose of this repository.

                if( !headerSent )
                {
                    // TODO extract header data
                    int numberOfChannel = 1;
                    int sampleSizeIndex = 1;

                    final byte[] finalAacHeader = decoderSpecificInfo;
                    final int finalNumberOfChannel = numberOfChannel;
                    final int finalSampleSizeIndex = sampleSizeIndex;
                    muxer.setAudioHeader(new AACAudioHeader()
                    {
                        @NonNull
                        @Override
                        public byte[] getData()
                        {
                            return finalAacHeader;
                        }

                        @Override
                        public int getNumberOfChannels()
                        {
                            return finalNumberOfChannel;
                        }

                        @Override
                        public int getSampleSizeIndex()
                        {
                            return finalSampleSizeIndex;
                        }
                    });

                    headerSent = true;
                }

                // TODO extract frame data

                final byte[] aacData = buf.getData();
                final long timestamp = System.currentTimeMillis();

                // Don't call postAudio from the extracting thread.
                new AsyncTask<Void, Void, Void>()
                {
                    @Override
                    protected Void doInBackground(Void... params)
                    {
                        try
                        {
                            muxer.postAudio(new AACAudioFrame()
                            {
                                @Override
                                public long getTimestamp()
                                {
                                    return timestamp;
                                }

                                @NonNull
                                @Override
                                public byte[] getData()
                                {
                                    return aacData;
                                }
                            });
                        }
                        catch(IOException e)
                        {
                            // An error occured while sending the audio frame to the server
                        }

                        return null;
                    }
                }.execute();
            }
        }
        catch (Exception e)
        {
            // TODO handle error
        }
        finally
        {
            if( is != null )
            {
                try
                {
                    is.close();
                }
                catch (Exception ignored){}
            }
        }
    }
}
