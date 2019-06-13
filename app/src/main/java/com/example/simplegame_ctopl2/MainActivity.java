package com.example.simplegame_ctopl2;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    SoundPool soundPool;
    int mySound=-1;

    Paint recPaint= new Paint();

    Timer timer = new Timer();
    ImageView myImage, starImage;
    int playersTurn=1;
    int player1Score=0;
    int player2Score=0;
    int imageX= 0, imageY=0;
    int speed=25;
    int directionX =1;
    int directionY = 1;
    int screenX, screenY;

    int starX=1;
    int starY=1;
    int starSpeed=1;
    int starXdirection=1;
    int starYdirection=1;

    Rect graphic1Rect, graphic2Rect;

    Button buttonA, buttonB, buttonC, buttonD, resetButton;
    TextView myLabel1, myLabel2, player1, player2, textView4, textView3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myLabel1=(TextView)findViewById(R.id.myLabel1);
        myLabel2=(TextView)findViewById(R.id.myLabel2);

        player1=(TextView)findViewById(R.id.textView);
        player2=(TextView)findViewById(R.id.textView2);
        textView3=(TextView)findViewById(R.id.textView3);
        textView4=(TextView)findViewById(R.id.textView4);

        if(playersTurn==1){
            player1.setVisibility(View.VISIBLE);
            player2.setVisibility(View.INVISIBLE);
        }
        else{
            player2.setVisibility(View.VISIBLE);
            player1.setVisibility(View.INVISIBLE);
        }



        buttonA=(Button)findViewById(R.id.button1);
        buttonB=(Button)findViewById(R.id.button2);
        buttonC=(Button)findViewById(R.id.button3);
        buttonD=(Button)findViewById(R.id.button4);
        resetButton=(Button)findViewById(R.id.button5);

        myImage = (ImageView)this.findViewById(R.id.imageView1);
        starImage = (ImageView)this.findViewById(R.id.starImage);
        final int FPS= 40;
        TimerTask updateGame= new UpdateGameTask();
        timer.scheduleAtFixedRate(updateGame, 0, 1000/FPS);

        Display display= getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenX=size.x;
        screenY=size.y;







        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundPool= new SoundPool(20,AudioManager.STREAM_MUSIC,0);
        AssetManager assetManager = getAssets();
        try{
            AssetFileDescriptor descriptor= assetManager.openFd("magicsound.ogg");
            mySound=soundPool.load(descriptor,1);
        }catch (IOException e){
            e.printStackTrace();
        }






    }
    public boolean checkCollision(View v1, View v2){
        Rect r1= new Rect(v1.getLeft(), v1.getTop(), v1.getRight(),v1.getBottom());
        Rect r2= new Rect(v2.getLeft(), v2.getTop(), v2.getRight(),v2.getBottom());
        return r1.intersect(r2);
    }

    public void buttonAPressed(View view) {
        soundPool.play(mySound,1,1,0,0,1);
        speed= speed+20;
        directionX=0;
        directionY=1;

        if(playersTurn==1){

            player1Score+=3;
            myLabel1.setText(String.valueOf(player1Score));
        }
        if(playersTurn==2){
            player2Score+=3;
            myLabel2.setText(String.valueOf(player2Score));
        }


    }

    public void buttonBPressed(View view) {
        soundPool.play(mySound,1,1,0,0,1);
        speed=speed-20;
        directionY=0;
        directionX=1;


        if(playersTurn==1){

            player1Score+=2;
            myLabel1.setText(String.valueOf(player1Score));
        }
        if(playersTurn==2){
            player2Score+=2;
            myLabel2.setText(String.valueOf(player2Score));
        }


    }
    public int winner(int player1Score, int player2Score){
        int max=0;
        if(player1Score>player2Score){
            max=1;
        }
        if(player1Score<player2Score){
            max=2;
        }
        return max;
    }
    public void isGameOver(int score){
        int player= winner(player1Score, player2Score);
        if(player1Score >score || player2Score > score){
            textView4.setText("Game Over");
            switch (player){
                case 1:
                    textView3.setText("Player 1 Wins");
                    speed=0;
                    break;

                case 2:
                    textView3.setText("Player 2 Wins");
                    speed=0;
            }
        }

    }

    public void buttonCPressed(View view) {
        soundPool.play(mySound,1,1,0,0,1);
        speed=1;
        directionX=1;
        directionY=1;

        if(playersTurn==1){

            player1Score+=1;
            myLabel1.setText(String.valueOf(player1Score));
        }
        if(playersTurn==2){
            player2Score+=1;
            myLabel2.setText(String.valueOf(player2Score));
        }

    }

    public void buttonDPressed(View view) {
        soundPool.play(mySound,1,1,0,0,1);
        imageX= 0;
        imageY=0;
        speed=25;
        directionX =1;
        directionY = 1;

        switch(playersTurn){
            case 1:
            playersTurn=2;
            break;

            case 2:
                playersTurn=1;
                break;
        }


        if(playersTurn==1){
            player1.setVisibility(View.VISIBLE);
            player2.setVisibility(View.INVISIBLE);
        }
        else{
            player2.setVisibility(View.VISIBLE);
            player1.setVisibility(View.INVISIBLE);
        }


    }

    public void resetGame(View view) {
        playersTurn=1;
        player1Score=0;
        player2Score=0;
        imageX= 0;
        imageY=0;
        speed=25;
        directionX =1;
        directionY = 1;

        starX=50;
        starY=0;
        starSpeed=1;
        starXdirection=1;
        starYdirection=1;
        myLabel1.setText("0");
        myLabel2.setText("0");
        player1.setVisibility(View.VISIBLE);
        player2.setVisibility(View.INVISIBLE);
        starImage.setVisibility(View.VISIBLE);



    }

    class UpdateGameTask extends TimerTask{
    @Override
    public void run(){
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(checkCollision(myImage,starImage)){
                    starImage.setVisibility(View.INVISIBLE);
                }
                imageX +=(speed * directionX);
                imageY += (speed * directionY);
                myImage.setX(imageX);
                myImage.setY(imageY);

                starX +=(starSpeed * starXdirection);
                starY += (starSpeed * starYdirection);
                starImage.setX(starX);
                starImage.setY(starY);


                if((imageX + myImage.getWidth())> screenX || imageX <0){
                        directionX = directionX * -1;
                }
                if((imageY + myImage.getWidth())> screenY || imageY <0){
                    directionY = directionY * -1;
                }



                if((starX + starImage.getWidth())> screenX || starX <0){
                    starXdirection = starXdirection * -1;
                }
                if((starY + starImage.getWidth())> screenY || starY <0){
                    starYdirection = starYdirection * -1;
                }

                isGameOver(25);

















            }
        });
    }
}

}
