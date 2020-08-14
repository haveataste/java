package l;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * 
 * @author 
 * @version 1.0
 * @date 2016/2/7
 *
 */

public class NumberPuzzleGame extends JFrame{
	private static final long serialVersionUID = 1L;
	JMenuBar menubar;
	JMenu level,restart,pause,step,time,autoPlay;
	JMenuItem level1,level2,level3;
	JPanel panel;
	JButton cell[]=new JButton[25];//方格
	int J[] =new int [25];//按方格顺序，存放每个方格上的数字
	int levelnum=3;
	int pauseValue=1;//pauseValue为偶数时，暂停游戏，停止计时
	int counter=0;//步数
	long startTime=0;//开始时刻
	long usedSec=0;//流逝的时间
	int Location;//点击的方格位置，值取0-8/15/24
	int blankLocation;//空格位置，值取-1，0-8/15/24
	Font myfont = new Font("TimesRoman",Font.PLAIN,18);
	Font myfont1= new Font("TimesRoman",Font.BOLD,40);//Courier
	
	public NumberPuzzleGame(){
		//创建菜单
		menubar=new JMenuBar();
		level=new JMenu("Level");
		level1=new JMenuItem("3X3");
		level2=new JMenuItem("4X4");
		level3=new JMenuItem("5X5");
		level.setFont(myfont);
		level1.setFont(myfont);
		level2.setFont(myfont);
		level3.setFont(myfont);
		level1.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){levelnum=3;initButton();}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		level2.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){levelnum=4;initButton();}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		level3.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){levelnum=5;initButton();}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		level.add(level1);
		level.add(level2);
		level.add(level3);
		menubar.add(level);
		restart=new JMenu("Restart");
		restart.setFont(myfont);
		restart.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){initButton();}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		menubar.add(restart);	
		autoPlay=new JMenu("AutoPlay");
		autoPlay.setFont(myfont);
		autoPlay.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){if(levelnum==3)bfs();}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		menubar.add(autoPlay);
		pause=new JMenu("Pause");
		pause.setFont(myfont);
		pause.addMouseListener(new MouseListener(){
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				pauseValue++;
				if(pauseValue%2==0){remove(panel);repaint();}
				else{startTime=System.currentTimeMillis()-usedSec;add(panel);repaint();}
			}
			public void mouseReleased(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
		});
		menubar.add(pause);
		step=new JMenu("Step0");
		step.setFont(myfont);
		menubar.add(step);
		time=new JMenu("Time0m0s");
		time.setFont(myfont);
		menubar.add(time);
		setJMenuBar(menubar);

		//创建方格面板
		panel=new JPanel();
		add(panel);
		initButton();
		
		setTitle("Number Puzzle Game");
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	//初始化方格
	private void initButton(){
		remove(panel);
		panel=new JPanel();
		panel.setBackground(Color.GREEN);
		panel.setLayout(new GridLayout(levelnum,levelnum,5,5));
		add(panel);
		
	    for(int i=0;i<levelnum*levelnum;i++){
	    	cell[i]=new JButton();
	    	cell[i].setFont(myfont1);
	    	cell[i].setBackground(Color.orange);
	    	cell[i].addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e){
						int num=Integer.parseInt(e.getActionCommand());
						//getActionCommand(),Returns the command name of the action event fired by this button. If the command name is null (default) then this method returns the label of the button. 
						exchange(num);
				   		if(checkFinish(J)){
				   			pauseValue=0;//游戏成功，停止计时
				   			int choice=JOptionPane.showConfirmDialog(null,"Congratulations!Contiune?","挑战成功",JOptionPane.YES_NO_OPTION);
							if(choice == JOptionPane.YES_OPTION) initNumber();
							else System.exit(0);
				   		}
     				}
  				}//实现接口ActionListener的匿名类结束
			);
			panel.add(cell[i]);
		}
	    initNumber();
	}
	
	//初始化方格上数字
	private void initNumber(){
		pauseValue=1;
		counter=0;
		step.setText("Step0");
		startTime=0;
		usedSec=0;
		time.setText("Time0m0s");
		
		Vector<Integer> optionalNum=new Vector<Integer>();//字符串向量
		for(int i=0;i<levelnum*levelnum;i++)optionalNum.add(i);	
 		int index=-1;
 		for(int i=0;i<levelnum*levelnum;i++){
 			index=(int)(Math.random()*optionalNum.size());
	  		if(optionalNum.get(index)==0){
	  			cell[i].setText("");
	   			J[i]=0;
	 		}else{
	  			cell[i].setText(String.valueOf(optionalNum.get(index)));
	   			J[i]=optionalNum.get(index);
	  		}
			optionalNum.remove(index);
  		}
 		//解决逆序数问题
 		int temp=0;
   		for(int i=1;i<levelnum*levelnum;i++){
			if(J[i]!=0){
	    		for(int j=0;j<i;j++){
    				if(J[j]>J[i])temp++;
    			}
    		}
		}
   		if(temp%2!=0)initNumber();
   		//解决开局胜利问题
   		if(checkFinish(J))initNumber();
	}

	private void exchange(int num){
		//获取点击的方格位置Location，值取0-8/15/24
		for(Location=0;Location<levelnum*levelnum;Location++)
			if(num==J[Location])break;
		//获取空白方格位置blankLocation，值取-1，0-8/15/24
		for(blankLocation=0;blankLocation<levelnum*levelnum;blankLocation++)
			if(J[blankLocation]==0)break;
		if(levelnum==3){
			if(Location==0&&blankLocation!=1&&blankLocation!=3)blankLocation = -1;
			else if(Location==2&&blankLocation!=1&&blankLocation!=5)blankLocation = -1;
			else if(Location==6&&blankLocation!=3&&blankLocation!=7)blankLocation = -1;
			else if(Location==8&&blankLocation!=7&&blankLocation!=5)blankLocation = -1;
		    else if(Location==1&&blankLocation!=Location-1&&blankLocation!=Location+1&&blankLocation!=Location+3)blankLocation = -1;
		    else if(Location==3&&blankLocation!=Location-3&&blankLocation!=Location+3&&blankLocation!=Location+1)blankLocation = -1;
		    else if(Location==7&&blankLocation!=Location-1&&blankLocation!=Location-3&&blankLocation!=Location+1)blankLocation = -1;
			else if(Location==5&&blankLocation!=Location-3&&blankLocation!=Location+3&&blankLocation!=Location-1)blankLocation = -1;
			else if(Location==4&&blankLocation!=Location-3&&blankLocation!=Location+3&&blankLocation!=Location-1&&blankLocation!=Location+1)blankLocation = -1;
		}
		else if(levelnum==4){
			if(Location==0&&blankLocation!=1&&blankLocation!=4)blankLocation = -1;
			else if(Location==3&&blankLocation!=2&&blankLocation!=7)blankLocation = -1;
			else if(Location==12&&blankLocation!=8&&blankLocation!=13)blankLocation = -1;
			else if(Location==15&&blankLocation!=14&&blankLocation!=11)blankLocation = -1;
			else if((Location==1||Location==2)&&blankLocation!=Location-1&&blankLocation!=Location+1&&blankLocation!=Location+4)blankLocation = -1;
		    else if((Location==4||Location==8)&&blankLocation!=Location-4&&blankLocation!=Location+4&&blankLocation!=Location+1)blankLocation = -1;
		    else if((Location==13||Location==14)&&blankLocation!=Location-1&&blankLocation!=Location-4&&blankLocation!=Location+1)blankLocation = -1;
		    else if((Location==7||Location==11)&&blankLocation!=Location-4&&blankLocation!=Location+4&&blankLocation!=Location-1)blankLocation = -1;
		    else if((Location==5||Location==6||Location==9||Location==10)&&blankLocation!=Location-4&&blankLocation!=Location+4&&blankLocation!=Location-1&&blankLocation!=Location+1)blankLocation = -1;
		}
		else if(levelnum==5){
			if(Location==0&&blankLocation!=1&&blankLocation!=5)blankLocation = -1;
			else if(Location==4&&blankLocation!=3&&blankLocation!=9)blankLocation = -1;
			else if(Location==20&&blankLocation!=15&&blankLocation!=21)blankLocation = -1;
			else if(Location==24&&blankLocation!=23&&blankLocation!=19)blankLocation = -1;
		    else if((Location==1||Location==2||Location==3)&&blankLocation!=Location-1&&blankLocation!=Location+1&&blankLocation!=Location+5)blankLocation = -1;
		    else if((Location==5||Location==10||Location==15)&&blankLocation!=Location-5&&blankLocation!=Location+5&&blankLocation!=Location+1)blankLocation = -1;
		    else if((Location==21||Location==22||Location==23)&&blankLocation!=Location-1&&blankLocation!=Location-5&&blankLocation!=Location+1)blankLocation = -1;
		    else if((Location==9||Location==14||Location==19)&&blankLocation!=Location-5&&blankLocation!=Location+5&&blankLocation!=Location-1)blankLocation = -1;
		    else if((Location==6||Location==7||Location==8||Location==11||Location==12||Location==13||Location==16||Location==17||Location==18)&&blankLocation!=Location-5&&blankLocation!=Location+5&&blankLocation!=Location-1&&blankLocation!=Location+1)blankLocation = -1;
		}
		
		//交换点击方格与空格
		if(blankLocation!=-1){
			cell[blankLocation].setText(String.valueOf(num));
			cell[Location].setText("");
			J[blankLocation]=num;
			J[Location]=0;
			step.setText("Step"+(++counter)+"");
		}
	}
	
	private void timer(){
		while(true){
			System.out.println(startTime+"  "+usedSec+"  "+pauseValue+"  "+counter);
			if(counter==0)startTime=System.currentTimeMillis();
			if(counter>0&&pauseValue%2==1){
				usedSec=System.currentTimeMillis()-startTime;
				time.setText("Time"+usedSec/1000/60+"m"+usedSec/1000%60+"s");
			}
		}
	}
	
	//检查是否完成游戏
	private boolean checkFinish(int J[]){
   		boolean flag=true;
   		for(int i=1;i<levelnum*levelnum;i++)
    		if(i!=J[i-1]){flag=false;break;}
   		return flag;
	}
	
	private void bfs(){
		int i,j,k,front=1,rear=2,row,col,nrow,ncol,nblankLocation,code,cnt,length=1;
		for(i=1;i<=levelnum*levelnum;i++)length=length*i;	//状态共有362880种，数组稍微开大点
		int st[][]=new int[length][levelnum*levelnum];	//st为状态数组
		int dis[][]=new int[length][2];	//dis为每种状态的已走的父节点序号和当前状态空白方格位置
		int fact[]=new int[levelnum*levelnum];
		int vis[]=new int[length];
		int der[][]={{-1,0},{1,0},{0,-1},{0,1}};	//der为方向:上，下，左，右

	    for(i=1,fact[0]=1;i<levelnum*levelnum;i++)fact[i]=fact[i-1]*i;
		for(i=0;i<levelnum*levelnum;i++){
			st[1][i]=J[i];
			if(J[i]==0)dis[1][1]=i;
		}
	    
	    while(front<rear){
	   		if(checkFinish(st[front]))break;
	   		blankLocation=dis[front][1];
	        row=blankLocation/levelnum;
	        col=blankLocation%levelnum;
	        for(k=0;k<4;k++){	//按照上，下，左，右四个方向进行搜索
				nrow=row+der[k][0];
	            ncol=col+der[k][1];
	            nblankLocation=nrow*levelnum+ncol;
	            if(nrow>=0&&nrow<levelnum&&ncol>=0&&ncol<levelnum){
	            	for(i=0;i<levelnum*levelnum;i++)st[rear][i]=st[front][i];
	            	st[rear][blankLocation]=st[front][nblankLocation];
	            	st[rear][nblankLocation]=st[front][blankLocation];
	                dis[rear][0]=front;
					dis[rear][1]=nblankLocation;
					
					for(i=0,code=0;i<levelnum*levelnum;i++){
						for(cnt=0,j=i+1;j<levelnum*levelnum;j++)if(st[rear][i]>st[rear][j])cnt++;
						code+=cnt*fact[8-i];
					}
					for(i=0;i<rear;i++)if(vis[i]==code)break;
					if(i==rear){vis[rear]=code;rear++;}	//判断st[rear]这种状态是否已经出现过
	            }
	        }
	        front++;
	    }
	    
	    for(k=1;front!=0;k++){
	    	vis[k]=dis[front][1];
	    	front=dis[front][0];
	    }
	    
	    for(i=k-1;i>1;i--){
			cell[vis[i]].setText(String.valueOf(J[vis[i-1]]));
			cell[vis[i-1]].setText("");
			J[vis[i]]=J[vis[i-1]];
			J[vis[i-1]]=0;
			step.setText("Step"+(++counter)+"");
	    }
	    
	    pauseValue=0;
	    int choice=JOptionPane.showConfirmDialog(null,"Congratulations!Contiune?","挑战成功",JOptionPane.YES_NO_OPTION);
		if(choice == JOptionPane.YES_OPTION) initNumber();
		else System.exit(0);
	}
	
	public static void main(String args[]){
		NumberPuzzleGame game=new NumberPuzzleGame();
		Toolkit tool=game.getToolkit();
		Image image=tool.getImage("Lena.png");
		game.setIconImage(image);
		//游戏窗口在屏幕中居中显示
		int width=Toolkit.getDefaultToolkit().getScreenSize().width;
		int height=Toolkit.getDefaultToolkit().getScreenSize().height;
		game.setBounds((width-height*2/3)/2,height/6,height*2/3,height*2/3);
		game.timer();
	}
}
