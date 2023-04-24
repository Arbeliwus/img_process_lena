import java.io.*;
import java.util.*;
class Imgviewer
{
	public static void main(String[] args)throws Exception{
		 FileInputStream fis = new FileInputStream(args[0]);//檔名
		 Scanner scn=new Scanner(System.in);
		 byte []Rawimg=RawImage.fread(fis);
		 RawImage lena=new RawImage(512,512,Rawimg);//原始檔案

		 while(true)
		 {
			 System.out.println("請選擇功能:\n1.Mirror\n2.Flip\n3.Rotate\n4.Nagative\n5.Vague");
			 int menu=Integer.parseInt(scn.next());
			 if(menu>5||menu<1)
			 {
				System.out.print("Exit code,bye~");
				break;
			 }
			 switch(menu)
			 {
				 case 1:
					lena.Mirror();
					break;
				 case 2:
					lena.Flip();
					break;
				 case 3:
					lena.Rotate();
					break;
				 case 4:
					lena.Nagative();
					break;
				 case 5:
					lena.Vague();
					break;
			 }
			 lena.open(menu);//開啟變更後的檔案
		 }		  
	}
}
class RawImage
{		private byte []rawimg;//舊檔案
		private byte []newimg;//新檔案
		private int hight;
		private int width;		
		RawImage(int hight,int width,byte buffer[])//constructor
		{
			this.hight=hight;
			this.width=width;
			this.rawimg=buffer;
			this.newimg=new byte[hight*width];
		}
		
		public void open(int menu)//開啟變更後的img
		{
			String []menu_name={"Mirror","Flip","Rotate","Nagative","Vague"};
			new RawImgViewer(menu_name[menu-1],this.newimg);
		}
		
		
		public static byte[] fread(FileInputStream fis)throws Exception 
		{
			int temp;
			byte []buffer=new byte[512*512];
			for(int i=0;(temp=fis.read()) != -1;i++) //read回傳的是int 檔案結尾=EOF=-1 
				buffer[i]=(byte)(temp);
			fis.close();
			return buffer;
		}
		
		public void Mirror()
		{
			for (int i = 0; i < this.width; i++)
				for (int j = 0; j < this.hight; j++)
					this.newimg[i * this.hight + j] = this.rawimg[ i * this.hight + this.width - j-1];
		}
		
		public void Flip()
		{
			for (int i = 0; i < this.width; i++)
				for (int j = 0; j < this.hight; j++)
					newimg[i * this.hight + j] = rawimg[(this.width - i-1)*this.width+ j];
		}
		
		public void Rotate()
		{
			for (int i = 0; i < this.width; i++)
				for (int j = 0; j < this.hight; j++)
					newimg[i * this.hight + j] = rawimg[j*this.hight+this.width-i-1];
		}
		
		public void Nagative()
		{
			for(int i=0;i<this.newimg.length;i++)
				newimg[i]=(byte)(255-rawimg[i]&0xff);//byte(-128~~127),透過位元運算進行隱性轉型int
		}											 //0xFF=00000000 00000000 00000000 11111111,第一位元代表正負數
		public void Vague()
		{
			for (int i = 0; i < this.hight; i++)
				for (int j = 0; j < this.width; j++)
					if (i == 0 || i == this.hight - 1 || j == 0 || j == this.width - 1)
						newimg[i * this.hight + j] = rawimg[i * this.hight + j];
					else
						newimg[i * this.hight + j] = (byte)(((rawimg[ (i - 1) * this.hight + j - 1]&0xff) + 
														 (rawimg[ (i - 1) * this.hight + j]&0xff) +//up
														 (rawimg[ (i - 1) * this.hight + j + 1]&0xff) + 
														 (rawimg[ (i - 1) * this.hight + j]&0xff)  +//left
														 (rawimg[ i * this.hight + j]&0xff)  + 
														 (rawimg[ i * this.hight + j + 1]&0xff)+//right
														 (rawimg[ (i + 1) * this.hight + j - 1]&0xff) + 
														 (rawimg[ (i + 1) * this.hight + j]&0xff)+//down
														 (rawimg[ (i + 1) * this.hight + j + 1]&0xff))/ 9);
		}
		
}