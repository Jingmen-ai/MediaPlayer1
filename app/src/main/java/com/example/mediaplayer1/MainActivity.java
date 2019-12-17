package com.example.mediaplayer1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MediaPlayer mediaPlayer=new MediaPlayer();
    public static List<String> getFilesAllName(String path) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            Log.e("error", "空目录");
            return null;
        }
        List<String> s = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            String str1=files[i].getAbsolutePath();
            str1=str1.substring(44);
            s.add(str1);
        }
        return s;
    }


    TextView T;
    private SeekBar seekBar;
    private TextView tvstart;
    private TextView tvend;
    private File file;
    TextView tw1;
    ListView listView;
    String XX;
    String XS;
    Button BTord;
    Button BTsj;
    Button BTback;
    Button BTnext;
    FloatingActionButton BTrefresh;
    Boolean isfinish;
    String playing;
    String sr;
    int inlist;
    List<String> list=new ArrayList<>();
    String nextPlaying;
    public String getNextPlaying() {
        return nextPlaying;
    }
    public void setNextPlaying(String nextPlaying) {
        this.nextPlaying = nextPlaying;
    }
    public String getPlaying() {
        return playing;
    }
    public void setPlaying(String playing) {
        this.playing = playing;
    }
    public void playNextSong(String string0,int i)
    {
        String cd=string0;
        if(i==1){
            int bb=list.indexOf(cd);
            if (bb < list.size() - 1) {
                String nn=list.get(bb + 1).toString();
                T.setText("歌单顺序循环模式已开启 即将播放下一首："+nn);
            }
            else{
                String nn=list.get(0).toString();
                T.setText("歌单顺序循环模式已开启 即将播放下一首："+nn);
            }

        }
        mediaPlayer.reset();
                    initMediaPlayer(cd);
                    findView();
                    setPlaying(cd);
                    mediaPlayer.start();
                    tw1.setText("正在播放"+cd);

    }
    List<String> listk=new ArrayList<>();
    public String formatTime(long time){
        time=time/1000;
        String strHour=""+(time/3600);
        String strMinute=""+time%3600/60;
        String strSecond=""+time%3600%60;
        strHour=strHour.length()<2?"0"+strHour:strHour
        ;strMinute=strMinute.length()<2?"0"+strMinute:strMinute;
        strSecond=strSecond.length()<2?"0"+strSecond:strSecond;
        String strRsult="";
        if(!strHour.equals("00")){strRsult+=strHour+":";}
        //if(!strMinute.equals("00")){
            strRsult+=strMinute+":";
        //}
        strRsult+=strSecond;
        return strRsult;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw1=(TextView)findViewById(R.id.textView) ;
        T=(TextView)findViewById(R.id.textView5);
        Button play=(Button)findViewById(R.id.button3);
        Button stop=(Button)findViewById(R.id.button2);
        Button loop=(Button)findViewById(R.id.button4);
        Button pause=(Button)findViewById(R.id.button) ;
        BTord=(Button)findViewById(R.id.button5) ;
        BTsj=(Button)findViewById(R.id.button8) ;
        BTback=(Button)findViewById(R.id.button6) ;
        BTnext=(Button)findViewById(R.id.button7) ;
        BTrefresh=(FloatingActionButton)findViewById(R.id.floatingActionButton2) ;
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        //
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        loop.setOnClickListener(this);
        pause.setOnClickListener(this);
        String str= "/data/user/0/com.example.mediaplayer1/files/";
        //String str= "/data/data/com.example.mediaplayer1/files/";
        //String str="/storage/emulated/0/neteast/cloudmusic/Music/";
        tw1.setText("准备就绪");
        list=getFilesAllName(str);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list);
        listView=(ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1); }
        else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    handler1.removeCallbacks(update);
                    handler2.removeCallbacks(update1);

                    T.setText("");
                    mediaPlayer.reset();

                    XX=list.get(i).toString();
                    initMediaPlayer(XX);
                    seekBar.setVisibility(View.VISIBLE);
                    tw1.setText("准备播放"+XX);
                    findView();
                }
            });
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mediaPlayer.reset();
                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("删除确认");
                    builder.setMessage("是否从歌单中删除此项，此操作不会删除源文件");
                    final int ccc=i;
                    builder.setPositiveButton("确认删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            list.remove(ccc);
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list);
                            listView.setAdapter(adapter);

                        }
                    });
                    builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                    return true;
                }
            });
        }
        BTord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.setLooping(false);
                handler2.removeCallbacks(update1);
                sr = getPlaying();
                inlist = list.indexOf(sr);
                if (inlist < list.size() - 1) {
                    XS = list.get(inlist + 1).toString();
                    T.setText("歌单顺序循环模式已开启 即将播放下一首："+XS);}
                else {
                    XS = list.get(0).toString();
                    T.setText("歌单顺序循环模式已开启 即将播放下一首："+XS);}
                handler1.post(update);


            }
        });
        BTsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handler1.removeCallbacks(update);
                mediaPlayer.setLooping(false);
                T.setText("歌单随机循环模式已开启");
                handler2.post(update1);

            }
        });
        BTrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str= "/data/data/com.example.mediaplayer1/files/";
                list=getFilesAllName(str);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,list);
                listView.setAdapter(adapter);
            }
        });
        BTback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wq=getPlaying();
                int Q=list.indexOf(wq);
                String st;
                if(Q>0) {
                    st=list.get(Q-1).toString();
                    mediaPlayer.reset();
                    initMediaPlayer(st);
                    findView();
                    mediaPlayer.start();
                    setPlaying(st);
                    if(mediaPlayer.isPlaying()) tw1.setText("正在播放"+st);
                }
                //T.setText(XX);
                else {
                    st=list.get(list.size()-1).toString();
                    mediaPlayer.reset();
                    initMediaPlayer(st);
                    findView();
                    mediaPlayer.start();
                    setPlaying(st);
                    if(mediaPlayer.isPlaying()) tw1.setText("正在播放"+st);
                }
            }
        });
        BTnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String wq=getPlaying();
                int Q=list.indexOf(wq);
                String st;
                if(Q<list.size()-1) {
                    st=list.get(Q+1).toString();
                    mediaPlayer.reset();
                    initMediaPlayer(st);
                    findView();
                    mediaPlayer.start();
                    setPlaying(st);
                    if(mediaPlayer.isPlaying()) tw1.setText("正在播放"+st);
                }
                //T.setText(XX);
                else {
                    st=list.get(0).toString();
                    mediaPlayer.reset();
                    initMediaPlayer(st);
                    findView();
                    mediaPlayer.start();
                    setPlaying(st);
                    if(mediaPlayer.isPlaying()) tw1.setText("正在播放"+st);
                }
            }
        });
}
    private void findView(){

        tvstart=(TextView)findViewById(R.id.textView2);
        tvend=(TextView)findViewById(R.id.textView3);
        tvstart.setText("00:00");
        String str=formatTime(mediaPlayer.getDuration());
        tvend.setText(str);
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser==true){
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });}
        Handler handler = new Handler();
        Runnable updateThread = new Runnable(){
            public void run() {

                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                String str=formatTime(mediaPlayer.getCurrentPosition());
                tvstart.setText(str);
                handler.postDelayed(updateThread, 100);
            }
        };
        Handler handler1=new Handler();
        Runnable update=new Runnable() {
            @Override
            public void run() {
                sr = getPlaying();
                inlist = list.indexOf(sr);
                    if(mediaPlayer.getCurrentPosition()>=mediaPlayer.getDuration()){
                        isfinish=true;
                        if (inlist < list.size() - 1) {
                            XS = list.get(inlist + 1).toString();
                            //T.setText("歌单顺序循环模式已开启 即将播放下一首："+XS);
                            playNextSong(XS,1);
                        } else {
                            XS = list.get(0).toString();
                            //T.setText("歌单顺序循环模式已开启 即将播放下一首："+XS);
                            playNextSong(XS,1);
                        }
                    }else isfinish=false;
                    handler1.postDelayed(update, 100);
                }

        };
Handler handler2=new Handler();
Runnable update1=new Runnable() {
    @Override
    public void run() {
        if(mediaPlayer.getCurrentPosition()>=mediaPlayer.getDuration()) {
            Random random=new Random();
            int kk=random.nextInt(list.size());
            XS=list.get(kk);
            T.setText("歌单随机循环模式已开启");
            playNextSong(XS, 2);

        }else isfinish=false;
        handler1.postDelayed(update1, 100);
    }
};

    private void initMediaPlayer(String str) {
        try {

            //T.setText("");
            String str1=str;
            //File file=new File(Environment.getExternalStorageDirectory(),"music.mp3");
            file=new File(getFilesDir(),str1);


            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            //List getFilesAllName(file.getPath());
        }catch (Exception e) {e.printStackTrace();}
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    initMediaPlayer(XX);
                }
                else{
                    Toast.makeText(this,"拒绝储存权限",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
                default:
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.button3:
                if(!mediaPlayer.isPlaying())mediaPlayer.start();
                handler.post(updateThread);
                if(mediaPlayer.isPlaying()) tw1.setText("正在播放"+file.getName());

                setPlaying(file.getName());
                break;
            case R.id.button2:
                tw1.setText("准备就绪");
                T.setText("");
                mediaPlayer.reset();
                handler1.removeCallbacks(update);
                handler2.removeCallbacks(update1);
                initMediaPlayer(XX);
                break;
            case R.id.button:
                if(mediaPlayer.isPlaying()) mediaPlayer.pause();
                tw1.setText("播放暂停");
                break;
            case R.id.button4:
                if(mediaPlayer.isPlaying()) mediaPlayer.setLooping(true);

                T.setText("单曲循环已开启");
             default:break;
        }

    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
