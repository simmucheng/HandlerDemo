package com.example.simmucheng.handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button Btn_start_sendmessage=null;
    private Button Btn_start_Runable=null;
    private Button Btn_from_main_to_thread=null;
    private Button Btn_stop=null;
    private TextView Tv_showtextview=null;
    private Handler ThreadHandler;

    Handler handler_main=new Handler();
    Handler handler_main_handlemessage=new Handler(){
        @Override
        public void handleMessage(Message msg) {
           // ThreadHandler.sendMessage(msg);
            Tv_showtextview.append("sendMessage = "+msg.arg1);
        }
    };

    Runnable MyRunnable=new Runnable() {
        @Override
        public void run() {
            Tv_showtextview.append("\nloading");
            handler_main.postDelayed(MyRunnable,1000);
        }
    };
    Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Tv_showtextview.append("\nheihei");
            Toast.makeText(MainActivity.this,"tttttt",Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        new MyThread().start();
        Btn_start_Runable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler_main.post(MyRunnable);
            }
        });
        Btn_start_sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        Message msg=new Message();
                        msg.arg1=1;
                        msg.what=2;
                        handler1.sendMessage(msg);
                    }
                }.start();

            }
        });
        Btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler_main.removeCallbacks(MyRunnable);
            }
        });
        Btn_from_main_to_thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg1=new Message();
                msg1.arg1=3;
                ThreadHandler.sendMessage(msg1);
            }
        });
    }

    private void initView() {
        Btn_start_sendmessage= (Button) findViewById(R.id.Start_Handler_sendmessage);
        Btn_from_main_to_thread= (Button) findViewById(R.id.from_main_to_thread);
        Btn_start_Runable= (Button) findViewById(R.id.Start_Handler_Runable);
        Btn_stop= (Button) findViewById(R.id.stop);
        Tv_showtextview= (TextView) findViewById(R.id.ShowTextView);
    }

    class MyThread extends Thread{
        @Override
        public void run() {
            Looper.prepare();
            ThreadHandler=new Handler(Looper.myLooper()){
                @Override
                public void handleMessage(Message msg) {
                    msg.arg1+=1000;
                    Message msg1=handler_main_handlemessage.obtainMessage();
                    msg1.arg1=msg.arg1;
                    handler_main_handlemessage.sendMessage(msg1);
                }
            };
            Looper.loop();
        }


    }
}
