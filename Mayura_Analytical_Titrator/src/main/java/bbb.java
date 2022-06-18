package main.java;


import javax.swing.*; 
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays; 

public class bbb {
	
static BufferedReader br1;
static double[] d = new double[1500];
static double[] d1 = new double[1500];

static double[] cur_val = new double[20];
static double[] end_point_mv = new double[2000];
static double[] end_position = new double[2000];
static double[] End_Point = new double[2000];
static double[] slp_diff = new double[2000];
static double[] diff = new double[20];


static double[] data1;
static int size=0;
static double  end_line = 0;
static double total_points = 0;
static int end_point_no = 0;
static boolean flag_stop = true;
static int typ;
static int ee = 0;
static int m1,d_flag;
static double k = 0,ky;
static int end_count = 0;
static double delta = 0.6;
static double t,k1,k2,c2,newdata1;
static int i;
static int factor =1,graph_mul1=1;
static double diff0,diff1,diff2,slope,corres_fact,ep_continue1,ep_continue2,ep_continue3,dossage_speed;
static boolean continue_data1=false,res_flag;
private static String dos_speed; 

static int burette_factor = 1;
static float dose_speed=4;
static float pre_dose = 0;
static double filter=1;
static int thershold = 100;

static int a=1; // enter the number for end points run by the VB experiment

public static void arr() 
{   
	for( i = 0; i <= (end_point_no - 1);i++)
		{
			for (int j = i + 1; j<= (end_point_no - 1) ;j++)
			{ 
				if ( Math.abs(slp_diff[i]) < Math.abs(slp_diff[j]))
				{
				c2 = End_Point[i];
				End_Point[i] = End_Point[j];
				End_Point[j] = c2;
				c2 = slp_diff[i];
				slp_diff[i] = slp_diff[j];
				slp_diff[j] = c2;
				c2 = end_position[i];
				end_position[i] = end_position[j];
				end_position[j] = c2;
				c2 = end_point_mv[i];
				end_point_mv[i] = end_point_mv[j];
				end_point_mv[j] = c2;
				}
			}
		}
			set_dose_speed();
			res_flag = true;
			System.out.println("dossage_speed = "+dossage_speed);

			System.out.println("cur_val[6] = "+cur_val[6]);
			System.out.println("cur_val[7] = "+cur_val[7]);
			System.out.println("cur_val[17] = "+burette_factor);
			System.out.println("dosage speed = "+dossage_speed);
			System.out.println("ep1 = "+End_Point[0]);
			System.out.println("ep2 = "+End_Point[1]);
			System.out.println("ep3 = "+End_Point[2]);			
		
			        if(a==1) {
			        	System.out.println("IFFFFFFFFFFFFFF");
			        	if (end_point_no > 0) {
			        			System.out.println("End_Point[0] = "+End_Point[0]+" : dossage_speed = "+dossage_speed+" : corres_factor = "+corres_fact+" : predose = "+pre_dose);
								System.out.println(((((End_Point[0] * dossage_speed / burette_factor) / 1000) - corres_fact) + pre_dose));
							}
							else
							{
								System.out.println("No End Points");
							}
			        }
			        else if(a==2) {
			        	System.out.println("ELSE IFFFFFFFFFFFFFF");
							if(end_point_no > 1) 
							{
								
			        			System.out.println("End_Point[0] = "+End_Point[0]+"  End_Point[1] = "+End_Point[1]+" : dossage_speed = "+dossage_speed+" : corres_factor = "+corres_fact+" : predose = "+pre_dose);

								System.out.println("rer1 = "+(((End_Point[0] * dossage_speed / 1) / 1000) - corres_fact) + pre_dose);
								System.out.println("rer2 = "+((((End_Point[1] * dossage_speed / 1) / 1000) - corres_fact) + pre_dose));
							}
							else 
							{
								System.out.println("No of End Pov  ints");
								System.out.println("No end point,try changing threshold");
							}
			        }
			        else if(a ==3 ) {
				        	System.out.println(" Auto Three End Point Detection ");
							if (end_point_no > 2) {
								System.out.println((((End_Point[0] * dossage_speed / burette_factor) / 1000) - corres_fact) + pre_dose);
								System.out.println((((End_Point[1] * dossage_speed / burette_factor) / 1000) - corres_fact) + pre_dose);
								System.out.println((((End_Point[2] * dossage_speed / burette_factor) / 1000) - corres_fact) + pre_dose);			
							}
							else {
								System.out.println("No of End Points");
							}
			        }
			        else {
						System.out.println("Not if");
					}
}
		  
	public static void set_dose_speed() {
		System.out.println(" setdose_speed ");
		if (dose_speed == 0.5) 
		dossage_speed = 1.8;
		else if (dose_speed == 1) 
		dossage_speed = 3.6;
		else if (dose_speed == 2) 
		dossage_speed = 7.2;
		else if (dose_speed == 3) 
		dossage_speed = 10.8;
		else if (dose_speed == 4) 
		dossage_speed = 16.67;
		else if (dose_speed == 5) 
		dossage_speed = 18;
		else if (dose_speed == 6) 
		dossage_speed = 26.4;
		else if (dose_speed == 8) 
		dossage_speed = 28.8;
		else if (dose_speed == 10) 
		dossage_speed = 36;
		else if (dose_speed == 12) 
		dossage_speed = 43.2;
		else if (dose_speed == 14) 
		dossage_speed = 50.4;
		else if (dose_speed == 16) 
		dossage_speed = 57.6;
		}
	
	public static boolean xxx()
	   {
	       String st;

		   File file = new File("C:\\Users\\mayur\\Desktop\\Test19051.dat");

				try 
				{
					br1 = new BufferedReader(new FileReader(file));
					int r =0;
					while ((st = br1.readLine()) != null) {
						
//						if(r%3 == 0) {
						    d[ee] = (Double.valueOf(st));
						    //System.out.println(st +" Size = "+ee+" d[] "+d[ee]);
						    ee++;
//						}
//						r++;
					}
				} 
				catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
				//System.out.println("Size = "+ee);
				
//				for(int i=0;i<d1.length;i++) {
//					if(i!=0) {
//						d[i] = (d[i-1]/2)+(d1[i]/2);
//					}
//					else {
//						d[0] = d1[0];
//					}
//				}
				
				if(flag_stop == false) {
					return false;
				}
				if(typ == 1 || typ == 3) {
					return false;
				}
				data1= new double[ee];
				
				t = (filter / 5) * delta;
				k1 = delta / (t + delta);
				k2 = t / (t + delta);
			    data1 = new double[ee];

				if (ee > 10) 
				{
					for( int k = 0;k<=10;k++)
					{
						data1[k] = d[k];
						System.out.println("initial for loop "+data1[k] + "  "+d[k]);
					}				
				}
				else
				{
					data1[5] = 0;
					data1[10] = 0;
				}
				
				diff[1] = 0;
				c2 = 0;
				diff[0] = 0;
				diff[1] = 0;
				diff[2] = 0;
				diff[3] = 0;
				diff[4] = (data1[10] - data1[5]) * 20;
				//System.out.println("diff[4] = "+diff[4]);
				diff[5] = diff[4];
				diff[6] = diff[4];
				diff[7] = diff[4];
				diff[8] = diff[4];
				diff[9] = diff[4];
				diff[10] = diff[4];
				newdata1 = diff[4];
				c2 = 1;
				i = 0;
				while (i < 10) 
				{
					c2 = c2 + 0.2 * factor;
					i = i + 1;
				}	
				//System.out.println("C2 = "+c2);
				k = 4;
				m1 = 0;
				
				
				Arrays.fill(diff, 0);

				
				//main while
				
				for(int z=10;z<ee;z++)
				{
				k = k + 1;
				data1[10] = d[z];
				//System.out.println("while d["+z+"] = "+data1[10]+" K = "+k);

				for(int y=1;y<=9;y++) 
				{
					diff[y] = diff[y + 1];
				}
				diff[10] = (data1[10] - data1[5]) * 60;
				delta = 0.6;
				t = filter * delta * 5;
				k1 = delta / (t + delta);
				k2 = t / (t + delta);
				diff[10] = diff[9] * k2 + diff[10] * k1;
				data1[5]= data1[6];
				data1[6] = data1[7];
				data1[7] = data1[8];
				data1[8] = data1[9];
				data1[9] = data1[10];
				diff0 = diff[7];
				diff1 = diff[8];
				diff2 = diff[9];
				
				//System.out.println("diff[0] = "+diff0+" : diff[1] = "+diff1+" : diff[2] = "+diff2);

				
				
				if (diff0 < 0) {
					diff0 = -diff0;
				}
				if (diff1 < 0) {
					diff1 = -diff1;
				}
				if (diff2 < 0) {
					diff2 = -diff2;
				}
				if ((diff1 > thershold) && (diff0 < diff1) && (diff1 > diff2)) {
					System.out.println("indise if   diff[0] = "+diff0+" : diff[1] = "+diff1+" : diff[2] = "+diff2);
					if (slope < Math.abs(diff[8])) 
					{
						end_count = (int) c2;
						slope = diff[8];
						ky = k;
						d_flag = 1;
					}
				}
				else if (d_flag == 1) {
				if (Math.abs(slope) > Math.abs(diff[8]) / 1) {
					System.out.println("indise abs-slope if    "+ Math.abs(slope));

				end_position[end_point_no] = end_count;
				End_Point[end_point_no] = (float)ky;  //End_Point[end_point_no] = Format((ky), "###0.0###");
				slp_diff[end_point_no] = slope;
				if( dose_speed == 0.5) {
				corres_fact = 0.032;
				}
				else if (dose_speed == 1) {
				corres_fact = 0.017;
				}
				else if (dose_speed == 2) {
				corres_fact = -0.018;
				}
				else if (dose_speed == 3) {
				corres_fact = -0.037;
				}
				else if (dose_speed == 4) {
				corres_fact = 0;
				}
				else if (dose_speed == 5) {
				corres_fact = -0.054;
				}
				else if (dose_speed == 6) {
				corres_fact = -0.094;
				}
				else if (dose_speed == 8) {
				corres_fact = -0.147;
				}
				else if (dose_speed == 10) {
				corres_fact = -0.164;
				}
				else if (dose_speed == 12) {
				corres_fact = -0.22;
				}
				else if ( dose_speed == 14){
				corres_fact = -0.238;
				}
				else if (dose_speed == 16) {
				corres_fact = -0.245;
				}
				end_point_mv[end_point_no] = (data1[10]);
			    System.out.println("EP Somewhere = "+end_point_mv[end_point_no]);

				end_point_no = end_point_no + 1;
				total_points = end_point_no;
				d_flag = 0;
				slope = 0;
	   			}
	   			}
				c2 = c2 + 0.2 * factor * graph_mul1;
				//System.out.println("C2 = "+c2);
				}
				arr();
				return false;
	   }
	
	
    public static void main(String[] args) 
    {
    	xxx();
    } 
} 