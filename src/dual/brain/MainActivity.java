package dual.brain;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;
import android.graphics.Point;
import android.util.SparseArray;
import android.graphics.PointF;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class MainActivity extends Activity{
	DualBrain v;
	int b1,b2,contatempo,fase,liberada,qtdfase=7;
	int[][] blocos={
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,975,975,975,0,0,0,0,0,0,510,510,510,0,0,0,0,0,0,975,975,975,0,0,0,0,0,0,495,495,495,0,0,0,0,0,0,990,990,990},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,766,766,766,0,0,0,0,0,0,766,766,766,0,0,0,0,0,0,509,509,509,0,0,0,0,0,0,509,509,509,0,0,0,0,0,0,765,765,765},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,510,510,510,0,0,0,0,0,0,975,975,975,0,0,0,0,0,0,951,951,951},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,975,975,975,0,0,0,0,0,0,495,495,495,0,0,0,0,0,0,990,990,990},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,975,975,975,0,0,0,0,495,495,495,0,0,0,0,990,990,990,0,0,0,0,0,975,975,975,0,0,0,0,495,495,495,0,0,0,0,990,990,990},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,330,330,330,330,330,330,330,330,879,0,0,0,0,0,0,975,975,975,0,0,0,0,0,0,0,0,330,330,330,330,330,330,330,330,894},
		{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,693,693,693,693,693,693,660,660,726,726,660,660,693,693,957,957,957}
	};
	int[][] tempogravidade={{},{},{11,39},{},{11,43},{35},{}};
	int[][] tempoctrl={{},{},{},{11},{44},{},{11}};
	float maxx,maxy;
	boolean esq,dir,invgravidade,invbtn;
	String nomefase[]={"Cypher","Slayer","Arrow","Phoenix","Legendary","Instinct","Tears","Rising","Immortal","Warlock","ClashforCash","BloodFlare","Summoners","Banedragon","Fear","Sunset","Striker","Outdated","Gladiator","Entropy","Storm","Thunder"};
	SharedPreferences faseliberada;
	SparseArray<PointF> dedosnatela=new SparseArray<PointF>();
	Timer timer=new Timer();
	final Handler handler=new Handler();
	final Runnable runnable=new Runnable(){public void run(){v.invalidate();}};
	@Override public void onBackPressed(){if(contatempo==0)super.onBackPressed();else menu();}
	@Override public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		v=new DualBrain(this);
		Point size=new Point();
		getWindowManager().getDefaultDisplay().getSize(size);
		maxx=Math.min(size.x,size.y);//800
		maxy=Math.max(size.x,size.y);//444
		faseliberada=PreferenceManager.getDefaultSharedPreferences(this);
		liberada=faseliberada.getInt("liberada",0);
		fase=liberada;
		timer.schedule(new TimerTask(){public void run(){
				int vlrlinha=blocos[fase][((contatempo/5)+2)%blocos[fase].length];
				boolean pode=true;
				if((!invgravidade&&esq)||(invgravidade&&!esq)){
					if(b1<24){
						if(b1/5==0&&vlrlinha%512>255)pode=false;
						if(b1/5==1&&vlrlinha%256>127)pode=false;
						if(b1/5==2&&vlrlinha%128>65)pode=false;
						if(b1/5==3&&vlrlinha%64>31)pode=false;
						if(pode)b1++;
					}
				}
				else if(b1>0){
					if(b1/5==1&&vlrlinha%1024>511)pode=false;
					if(b1/5==2&&vlrlinha%512>255)pode=false;
					if(b1/5==3&&vlrlinha%256>127)pode=false;
					if(b1/5==4&&vlrlinha%128>65)pode=false;
					if(pode)b1--;
				}
				if((!invgravidade&&dir)||(invgravidade&&!dir)){
					if(b2<24){
						if(b2/5==0&&vlrlinha%4>1)pode=false;
						if(b2/5==1&&vlrlinha%8>3)pode=false;
						if(b2/5==2&&vlrlinha%16>7)pode=false;
						if(b2/5==3&&vlrlinha%32>15)pode=false;
						if(pode)b2++;
					}
				}
				else if(b2>0){
					if(b2/5==1&&vlrlinha%2==1)pode=false;
					if(b2/5==2&&vlrlinha%4>1)pode=false;
					if(b2/5==3&&vlrlinha%8>3)pode=false;
					if(b2/5==4&&vlrlinha%16>7)pode=false;
					if(pode)b2--;
				}
				if(contatempo>0){
					contatempo++;
					if(b2/5==0&&vlrlinha%2==1)menu();
					if(b2/5==1&&vlrlinha%4>1)menu();
					if(b2/5==2&&vlrlinha%8>3)menu();
					if(b2/5==3&&vlrlinha%16>7)menu();
					if(b2/5==4&&vlrlinha%32>15)menu();
					if(b1/5==4&&vlrlinha%64>31)menu();
					if(b1/5==3&&vlrlinha%128>63)menu();
					if(b1/5==2&&vlrlinha%256>127)menu();
					if(b1/5==1&&vlrlinha%512>255)menu();
					if(b1/5==0&&vlrlinha%1024>511)menu();
					for(int x=0;x<tempogravidade[fase].length;x++)if(contatempo==(tempogravidade[fase][x]-2)*5)invgravidade=!invgravidade;
					for(int x=0;x<tempoctrl[fase].length;x++)if(contatempo==(tempoctrl[fase][x]-2)*5){
						invbtn=!invbtn;
						esq=false;
						dir=false;
					}
				}
				if(contatempo/5==blocos[fase].length+2){
					menu();
					if(liberada==fase){
						if(fase==qtdfase-1)contatempo=-1;
						else{
							liberada++;
							fase++;
							Editor editor=faseliberada.edit();
							editor.putInt("liberada",liberada);
							editor.commit();
						}
					}
				}
				handler.post(runnable);
		}},0,60);
		setContentView(v);
	}
	void menu(){
		contatempo=0;
		invgravidade=false;
		invbtn=false;
	}
	class DualBrain extends View{
		Context ct;
		DualBrain(Context c){super(c);ct=c;}
		@Override public boolean onTouchEvent(MotionEvent me){
			if(contatempo==-1&&me.getAction()==MotionEvent.ACTION_UP){
				esq=false;
				dir=false;
				contatempo=0;
			}
			else if(contatempo==0){
				if(me.getY()<maxy*.8){
					if(fase<=liberada){
						b1=0;
						b2=0;
						contatempo=1;
					}
				}
				else if(me.getAction()==MotionEvent.ACTION_DOWN){
					if(me.getX()>maxx*.5){
						if(fase+1<qtdfase)fase++;
					}
					else if(fase>0)fase--;
				}
			}
			else switch(me.getActionMasked()){
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
				case MotionEvent.ACTION_CANCEL:
					for(int dedo=0;dedo<me.getPointerCount();dedo++){
						if(!invbtn&&me.getX(dedo)<maxx/2)esq=false;
						if(!invbtn&&me.getX(dedo)>maxx/2)dir=false;
						if(invbtn&&me.getX(dedo)>maxx/2)esq=false;
						if(invbtn&&me.getX(dedo)<maxx/2)dir=false;
					}
					dedosnatela.remove(me.getPointerId(me.getActionIndex()));
				break;
				default:
					dedosnatela.put(me.getPointerId(me.getActionIndex()),new PointF());
					for(int dedo=0;dedo<me.getPointerCount();dedo++){
						if(!invbtn&&me.getX(dedo)<maxx/2)esq=true;
						if(!invbtn&&me.getX(dedo)>maxx/2)dir=true;
						if(invbtn&&me.getX(dedo)>maxx/2)esq=true;
						if(invbtn&&me.getX(dedo)<maxx/2)dir=true;
					}
				break;
			}
			return true;
		}
		@Override protected void onDraw(Canvas c){
			super.onDraw(c);
			Paint p=new Paint();
			if(contatempo==-1){
				p.setColor(new Color().rgb(37,187,120));
				p.setTextSize((float)(maxx*0.14));
				p.setTextAlign(Paint.Align.CENTER);
				c.drawText("Game Over",(float)(maxx*.5),(float)(maxy*.44),p);
			}
			else if(contatempo==0){
				p.setColor(new Color().rgb(237,187,20));
				p.setTextSize((float)(maxx*0.15));
				p.setTextAlign(Paint.Align.CENTER);
				c.drawText("Dual Brain",(float)(maxx*.5),(float)(maxy*.4),p);
				p.setTextSize((float)(maxx*0.10));
				p.setColor(new Color().rgb(37,187,120));
				c.drawText(nomefase[fase],(float)(maxx*.5),(float)(maxy*.65),p);
				if(fase>liberada)c.drawRect(maxx/12*3,(float)(maxy*.63),maxx/12*9,(float)(maxy*.64),p);
			}
			else{
				p.setColor(new Color().rgb(220,0,0));
				for(int x=0;x<tempogravidade[fase].length;x++)if(contatempo/5<tempogravidade[fase][x]){
					c.drawRect(((maxx/12)*(1))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(2))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(2))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(3))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(3))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(4))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(4))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(5))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(5))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(6))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(6))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(7))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(7))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(8))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(8))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(9))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(9))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(10))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(10))+8,((maxy/30)*((contatempo/5)+25-tempogravidade[fase][x]))+4,((maxx/12)*(11))-8,((maxy/30)*((contatempo/5)+26-tempogravidade[fase][x]))-4,p);
				}
				p.setColor(new Color().rgb(200,200,200));
				for(int x=0;x<tempoctrl[fase].length;x++)if(contatempo/5<tempoctrl[fase][x]){
					c.drawRect(((maxx/12)*(1))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(2))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(2))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(3))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(3))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(4))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(4))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(5))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(5))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(6))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(6))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(7))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(7))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(8))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(8))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(9))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(9))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(10))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
					c.drawRect(((maxx/12)*(10))+8,((maxy/30)*((contatempo/5)+25-tempoctrl[fase][x]))+4,((maxx/12)*(11))-8,((maxy/30)*((contatempo/5)+26-tempoctrl[fase][x]))-4,p);
				}
				p.setColor(new Color().rgb(237,187,20));
				for(int x=0;x<blocos[fase].length;x++){
					int vlrlinha=blocos[fase][((contatempo/5)+x)%blocos[fase].length];
					if(vlrlinha%2==1)c.drawRect(((maxx/12)*(10))+8,((maxy/30)*(25-x))+4,((maxx/12)*(11))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%4>1)c.drawRect(((maxx/12)*(9))+8,((maxy/30)*(25-x))+4,((maxx/12)*(10))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%8>3)c.drawRect(((maxx/12)*(8))+8,((maxy/30)*(25-x))+4,((maxx/12)*(9))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%16>7)c.drawRect(((maxx/12)*(7))+8,((maxy/30)*(25-x))+4,((maxx/12)*(8))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%32>15)c.drawRect(((maxx/12)*(6))+8,((maxy/30)*(25-x))+4,((maxx/12)*(7))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%64>31)c.drawRect(((maxx/12)*(5))+8,((maxy/30)*(25-x))+4,((maxx/12)*(6))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%128>63)c.drawRect(((maxx/12)*(4))+8,((maxy/30)*(25-x))+4,((maxx/12)*(5))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%256>127)c.drawRect(((maxx/12)*(3))+8,((maxy/30)*(25-x))+4,((maxx/12)*(4))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%512>255)c.drawRect(((maxx/12)*(2))+8,((maxy/30)*(25-x))+4,((maxx/12)*(3))-8,((maxy/30)*(26-x))-4,p);
					if(vlrlinha%1024>511)c.drawRect(((maxx/12)*(1))+8,((maxy/30)*(25-x))+4,((maxx/12)*(2))-8,((maxy/30)*(26-x))-4,p);
				}
				p.setColor(new Color().rgb(37,187,120));
				c.drawRect(((maxx/12)*(1+(b1/5)))+8,((maxy/30)*23)+4,((maxx/12)*(2+(b1/5)))-8,((maxy/30)*24)-4,p);
				c.drawRect(((maxx/12)*(10-(b2/5)))+8,((maxy/30)*23)+4,((maxx/12)*(11-(b2/5)))-8,((maxy/30)*24)-4,p);
				c.drawRect(((maxx/12)*(1))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(2))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(2))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(3))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(3))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(4))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(4))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(5))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(5))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(6))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(6))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(7))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(7))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(8))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(8))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(9))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(9))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(10))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
				c.drawRect(((maxx/12)*(10))+8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+22))+4,((maxx/12)*(11))-8,((maxy/30)*((contatempo/5)-(blocos[fase].length)+23))-4,p);
			}
			p.setColor(new Color().rgb(0,0,240));
			for(int x=0;x<12;x++)c.drawRect(((maxx/12)*x)+8,4,((maxx/12)*(x+1))-8,(maxy/30)-4,p);
			for(int x=0;x<12;x++)c.drawRect(((maxx/12)*x)+8,(25*maxy/30)+4,((maxx/12)*(x+1))-8,(26*maxy/30)-4,p);
			for(int x=0;x<25;x++)c.drawRect(8,((maxy/30)*x)+4,(maxx/12)-8,((maxy/30)*(x+1))-4,p);
			for(int x=0;x<25;x++)c.drawRect(maxx-(maxx/12)+8,((maxy/30)*x)+4,maxx-8,((maxy/30)*(x+1))-4,p);
			if((!invbtn&&esq)||(invbtn&&dir))p.setColor(new Color().rgb(142,142,142));
			else p.setColor(new Color().rgb(92,92,92));
			c.drawRect(8,(maxy/30*26)+10,(maxx/2)-6,maxy-4,p);
			if((!invbtn&&dir)||(invbtn&&esq))p.setColor(new Color().rgb(142,142,142));
			else p.setColor(new Color().rgb(92,92,92));
			c.drawRect((maxx/2)+6,(maxy/30*26)+10,maxx-8,maxy-4,p);
		}
	}
}
