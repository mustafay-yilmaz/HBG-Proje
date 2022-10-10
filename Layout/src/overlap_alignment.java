
public class overlap_alignment {
	
	//Verilen iki sekansi hizalayip skor matrisi olusturuyor.
	int[][] skor_matrix(String A,String B,int match,int mismatch,int indel) {
				
			int L=A.length();
			int [][] skor= new int [L+1][L+1];
			
			for (int i=0 ; i<L+1 ; i++) {
				for (int j=0; j<L+1;j++)
					skor[i][j]=0;
			}
				
			for (int i=1; i<L+1 ; i++) {
				
				for (int j=1; j<L+1 ; j++) {
					
					skor[i][j]=max(skor[i-1][j]-indel, skor[i][j-1]-indel,skor[i-1][j-1]+s(A.charAt(i-1),B.charAt(j-1),match,mismatch));
				}
			}
			
			return skor;
			
		}
	
	 //Olusturulan skor matrisindeki son satir ve sutunlarında olusan en buyuk skoru bulup indisini geri donduruyor.
	 int [] best_indis(String A,String B,int match,int mismatch,int indel) {
	    	
		    int L=A.length();
	    	int [][] skor= new int [L+1][L+1];
	    	skor=skor_matrix(A,B,match,mismatch,indel);
			
			int [] best_indis= new int[2];
			int [] best_indis_i = {L,1};
			int [] best_indis_j = {1,L};
			
			int i_max_skor=skor[L][1];
			int j_max_skor=skor[1][L];
			
			for (int k=1; k<L+1;k++) {
				
				if (i_max_skor<skor[L][k]) {
					i_max_skor=skor[L][k];
					best_indis_i[0]=L;
					best_indis_i[1]=k;
				}	
				
				if (j_max_skor<skor[k][L]) {
					j_max_skor=skor[k][L];
					best_indis_j[0]=k;
					best_indis_j[1]=L;
				}
			}
			
			int best_skor=Math.max(i_max_skor, j_max_skor);
				if(best_skor==i_max_skor) {
					best_indis[0]=best_indis_i[0];
					best_indis[1]=best_indis_i[1];
				}
				else {
					best_indis[0]=best_indis_j[0];
					best_indis[1]=best_indis_j[1];
					
				}
			
				int z=0;
				if(best_indis[0]==L) {
					z=best_indis[1];
				   for (int i=L;i>z;i--) {
					   if(best_skor==skor[L][i] && i>z ) {
						   best_indis[0]=L;
						   best_indis[1]=i;
						   break;
					 }
					
					 }
			       }
				else {
					z=best_indis[1];
						
						for (int i=L;i>z;i--) {
							if(best_skor==skor[i][L] && i>z ) {
								best_indis[0]=i;
								best_indis[1]=L;
								break;
					}
				}
			}
		
			return best_indis;
		 }
	  
	 //skor_matrix ve best_indis fonksiyonlari yardimiyle best satir ve sutunlardaki en buyuk skoru geri donduruyor.
	 int best_skor (String A,String B,int match,int mismatch,int indel) {
	    	
	    	int L=A.length();
	    	int [][] skor= new int [L+1][L+1];
	    	
	    	skor=skor_matrix(A,B,match,mismatch,indel);
	    	
	    	int [] best_indis= new int [2];
	    	
	    	best_indis=best_indis(A,B,match,mismatch,indel);
	    	
	    	return skor[best_indis[0]][best_indis[1]];
	    }
		
	 //Verilen iki stringi hizaliyor.
	 String [] alignment(String A,String B,int match,int mismatch,int indel) {
		
		   int L=A.length();
		   int [][] skor= new int [L+1][L+1];
    	
		   skor=skor_matrix(A,B,match,mismatch,indel);
    	
		   int [] best_indis= new int [2];
    	   best_indis=best_indis(A,B,match,mismatch,indel);
    	
		   int satir=best_indis[0];
		   int sutun=best_indis[1];
		   
		   //Hizalanacak sekanslar.
		   String s1="";
		   String s2="";
		
		   int capraz=0,yukari=0,sol=0;
		   int t=A.length()-1;
		   int y=B.length()-1;
		   
		   //Baslangicta karsi sutun veya satirda bos birakilan bazlara karsi indel atiliyor.
		   	if(satir!=sutun) {
		   		if(satir>sutun) {
		   			for(int i=0;i<L-sutun;i++) {
		   				s2=s2+"-";
		   				s1=s1+B.charAt(y--);
				    }
			   }
			   else {
				   for(int i=0;i<L-satir;i++) {
					   s1=s1+"-";
					   s2=s2+A.charAt(t--);
				   }		
			   }
		    }
			
		   		int x=0;
		   		
		   		//En iyi skorun indisine gore yukari sol ve capraz giderek uygun ifadeler konuluyor.
		   		while(satir>-1 && sutun>-1){
		   			
		   		//Sonda karsi sutun veya satirda bos birakilan bazlara karsi indel atiliyor.
		   			if(satir==0 || sutun==0) {
		   				
		   				if(sutun==0) {
		   					
		   					for(int i=satir; i>0 ;i--,t--) {
		   						s1=s1+"-";
		   						s2=s2+A.charAt(t);  
		   					}
		   					satir=-1;
		   				}
				  
		   				else {
		   					for(int i=sutun; i>0 ;i--,y--) {
		   						s2=s2+"-";
		   						s1=s1+B.charAt(y);
		   					}
		   					sutun=-1;
		   				}
				
		   			}
		   			
		   			//Asil geri gitme islemi burada yapiliyor.
		   			else {	   
				
		   				capraz=skor[satir-1][sutun-1];
		   				yukari=skor[satir-1][sutun];
		   				sol=skor[satir][sutun-1];
		   				x=max(capraz,yukari,sol);
				
		   				while(true) {
			
		   					if (x==capraz && skor[satir][sutun]-match==capraz || skor[satir][sutun]+mismatch==capraz) {
		   						
		   						satir--;
		   						sutun--;
		   						s1=s1+B.charAt(y--);
		   						s2=s2+A.charAt(t--);
		   						break;
		   					}
					
		   					else if (x==yukari && skor[satir][sutun]+indel==yukari) {
		   						satir--;
		   						s1=s1+"-";
		   						s2=s2+A.charAt(t--);
		   						break;
		   					}
				
		   					else if (x==sol && skor[satir][sutun]+indel==sol) {
		   						sutun--;
		   						s1=s1+B.charAt(y--);
		   						s2=s2+"-";
		   						break;
		   					}
		   					
		   					else {
				
		   						if (x==capraz)
		   							x=Math.max(yukari, sol);
		   						
		   						else if (x==yukari)
		   							x=Math.max(sol, capraz);
		   						
		   						else
		   							x=Math.max(yukari, capraz);	
		   					}
		   				}
		   			}	
		   		} 
		   			
		   			//Sekanslar ters bir sekilde hizalandigindan strinler ters ceviriliyor ve bir string dizisine aktariliyor.
		   			StringBuffer a = new StringBuffer(s1);
		   			StringBuffer b = new StringBuffer(s2);
		   			s1=a.reverse().toString();
		   			s2=b.reverse().toString();
	
		   			
		   			String [] s=new String[2];
		   			s[0]=s1;
		   			s[1]=s2;
		
		   		return s;
			}
	
	 //Verilen harflerin eslesip eslesmemesine gore match veya mismatch degerini donduruluyor.
	 int s (char A, char B,int match,int mismatch) {
		
		if (A==B)
			return match;
		else
			return -mismatch;
	}
	 
	 //3 parametreli max bulan fonksiyon. 
	 static int max(int A,int B,int C) {
		
		int max=Math.max(A, B);		
		return Math.max(max, C);		
	 }
 }
