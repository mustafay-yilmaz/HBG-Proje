
import java.util.*;
import java.io.*;

public class Layout {
	
	public static void main(String[] args) throws IOException{
		
		//10-41 satirlar arasinda kullanicidan gerekli bilgileri aliyor.
		Scanner c= new Scanner(System.in) ;
		String DNA;
		int DNA_L;
		
		while(true) {
			
		System.out.print("DNA dizilimini siz mi girmek istiyorsunuz? (E/H) ");
		char cevap=c.next().charAt(0); 
		
		if(cevap=='E'||cevap=='e') {
		System.out.print("Sekanslanacak DNA dizilimini giriniz: ");
		DNA=c.next(); 
		break;
		}
		
		else if(cevap=='H'||cevap=='h') {
		System.out.print("Sekanslanacak DNA diziliminin uzunlugunu giriniz: ");
		DNA_L=c.nextInt(); 
		
		//Kullanicinin verdigi uzunlukta rastgele DNA uretiyor.
		DNA= rand_dna_create(DNA_L);
		break;
	    }
		
		else 
			System.out.println("Yanlis ifade girdiniz!!");
		}
		
		
		System.out.print("Istediginiz sekans sayisi: "); 
		int N =c.nextInt(); 
		System.out.print("Her sekansin uzunlugu: "); 
		int L =c.nextInt();
		
		//Kullanicinin verdigi verilere gore rastgele sekanslar olusturuyor.
		String [] sekanslar= new String[N];
		sekanslar=sekans_olusturma(DNA,L,N);
		
		//Olusturulan sekanslardan reverse complementleri olusturuyor ve diziye kaydediyor.
		String [] compl_sekans= new String[N];
		compl_sekans=revandcompl(sekanslar,N,L);
		
		//Olusturulan sekanslari ve reverse complementleri dosyaya yazdiriyor.
		yazdir_sekansandcomp_sekans(sekanslar,compl_sekans,N);
		
		//56-63 satirlari arasinda skor hesaplamak icin gerekli olan bilgiler aliniyor.
		System.out.print("Eslesme odulunu giriniz: ");
		int match=c.nextInt(); 
		System.out.print("Eslesmeme cezasini giriniz: ");
		int mismatch=c.nextInt(); 
		System.out.print("Indel cezasini giriniz: ");
		int indel =c.nextInt();
		System.out.print("Matriste kabul edilebilecek en kucuk skor degerini (T) giriniz: ");
		int T =c.nextInt();
		
		//Overlap matrisini olusturup dosyaya yazdiriyor.
		overlap_matrix(sekanslar, compl_sekans,T,N,match,mismatch,indel);
		
		//Overlap matrisindeki butun sekansları en yuksek skorlu sekanslarla hizalayip layout olusturuyor ve bunlari dosyaya yazdiriyor.
		yazdir_best_skor_alignment(sekanslar, compl_sekans,T,N,match,mismatch,indel);
		
		c.close();

		}
	
	//Kullanicinin verdigi uzunlukta rastgele DNA uretiyor.
	static String rand_dna_create(int DNA_L) throws IOException{
		//DNA'nin tutulacagi string.
		String DNA="";
		
		int sayi=0;
		Random r= new Random();
		
		//Bir dongu yardimiyla 0.25 ihtimalle A,C,G,T bazlarini stringe ekliyor.
		for (int i=0; i<DNA_L; i++) {
			
			sayi=Math.abs((r.nextInt())%4);
			
			switch (sayi) {
			case 0:
				DNA=DNA + "A";
				break;
			case 1:
				DNA=DNA + "C";
				break;
			case 2:
				DNA=DNA + "G";
				break;
			case 3:
				DNA=DNA + "T";
				break;
			}	
			
		}
		
		//Olusturalan DNA'yi DNA.txt dosyasina yazdiriyor.
		File file = new File("DNA.txt"); 
		if (!file.exists()) { file.createNewFile(); 
		} FileWriter fileWriter = new FileWriter(file, false); 
		BufferedWriter bWriter = new BufferedWriter(fileWriter); 
		bWriter.write(DNA);
		bWriter.close(); 
		
		System.out.println("DNA.txt basariyla olusturuldu.");
		
		return DNA;
	}
	
	//Kullanicinin verdigi verilere gore rastgele sekanslar olusturuyor.
	static String [] sekans_olusturma (String DNA, int L,int N) {
		
		int s=0,b=0;
		
		//Sekanslari tutmak icin ici bos string dizisi tanimliyor.
		String [] sekanslar= new String[N];
		for (int i=0; i<N ;i++)
			sekanslar[i]="";
		
		//Rastgele sayilar uretiliyor bu sayede DNA rastgele sekanslara bolunuyor.
		Random r=new Random();
		while (s<N){
			b=r.nextInt(DNA.length()-L+1);
			sekanslar[s++]=DNA.substring(b, b+L);
		 }
		 return sekanslar;
	   }
	
	//Olusturulan sekanslardan reverse complementleri olusturuyor ve diziye kaydediyor.
	static String [] revandcompl (String[] sekanslar,int N, int L) {
		
		//Complementleri ve reverse complementleri tutacak string dizileri tanimlaniyor.
		String [] compl_sekans= new String[N];
		String [] reverse=new String[N];
		 for(int i=0 ; i<N ; i++) {
			 compl_sekans[i]="";
			 reverse[i]="";
			 
			//DNA sekanslarinin complementleri aliniyor.
			for (int j=0 ; j<L ; j++) {
				
				char x=sekanslar[i].charAt(j);
				
				switch (x) {
				case 'A':
					compl_sekans[i]=compl_sekans[i] +"T";
					break;
				case 'C':
					compl_sekans[i]=compl_sekans[i] + "G";
					break;
				case 'G':
					compl_sekans[i]=compl_sekans[i] + "C" ;
					break;
				case 'T':
					compl_sekans[i]=compl_sekans[i] + "A";
					break;
				}	
			}
			//Complementlerin reverse hali aliniyor.
			 for (int k = L - 1 ; k >= 0 ; k--)
			      reverse[i] = reverse[i] + compl_sekans[i].charAt(k);
		}
		 return reverse;
	}
	
	//Olusturulan sekanslari ve reverse complementleri dosyaya yazdiriyor.
	static void yazdir_sekansandcomp_sekans(String [] sekanslar, String [] compl_sekans, int N) throws IOException {
		
		File file = new File("sekans&comp_sekans.txt"); 
		if (!file.exists()) { file.createNewFile(); 
		} FileWriter fileWriter = new FileWriter(file, false); 
		BufferedWriter bWriter = new BufferedWriter(fileWriter); 
		int k=0;
		for (int i=0; i<N ; i++) {
			k=i;
			k++;
			bWriter.write("Sekans "+ k+  ": " +sekanslar[i]+System.lineSeparator()+"Comp_Sekans "+k+ ": "+compl_sekans[i]
				+ System.lineSeparator()+" ----------------------------------------------------------------"+System.lineSeparator());
		}	
		bWriter.close(); 
		System.out.println("sekans&comp_sekans.txt basariyla olusturuldu!");
		
	}
	
	//Overlap matrisini olusturup dosyaya yazdiriyor.
	static void overlap_matrix(String []sekanslar, String []compl_sekans,int T,int N,int match,int mismatch,int indel) throws IOException {
		
		overlap_alignment a= new overlap_alignment();
		int [][] skor= new int [N][N];
		
		//best_skor yardimiyla sekanslari eslestirip en iyi skorları çift boyutlu int diziye kaydediyor.
		for (int i=0;i<N;i++) {
			
			for (int j=i+1; j<N ;j++) 
				skor[i][j]=a.best_skor(sekanslar[i], sekanslar[j], match, mismatch, indel);	
		}
		
		//best_skor yardimiyla sekanslari reverse complementleriyle eslestirip en iyi skorları çift boyutlu int diziye kaydediyor.
		for (int i=0;i<N;i++) {
			
			for (int j=0; j<i ;j++) 
				skor[i][j]=a.best_skor(sekanslar[i], compl_sekans[j], match, mismatch, indel);	
		}
		
		//Kaydedilen skorlari ders kitabinda yer alan matrise benzer sekilde dosyaya yazdiriyor.
		File file = new File("overlap_matrix.txt"); 
		if (!file.exists()) { file.createNewFile(); 
		} FileWriter fileWriter = new FileWriter(file, false); 
		PrintWriter pWriter = new PrintWriter(fileWriter); 
		pWriter.print(" ");
		for (int i=0 ; i<N ;i++) {
			pWriter.printf("%3d",i);
			
			if(i==9)
				pWriter.print(" ");
		}
		pWriter.println("\n");
		
		for (int i=0 ;i<N;i++) {
			pWriter.printf("%-3d",i);
			for (int j=0;j<N;j++) {
				
				if (i==j)
					pWriter.printf("%-3s","*");
				else if(skor[i][j]>=T)
					pWriter.printf("%-3d",skor[i][j]);
				else
					pWriter.printf("   ");
				
			}
			pWriter.println("\n");
				
		}
			
		pWriter.close(); 
		System.out.println("overlap_matrix.txt basariyla olusturuldu!");
		
	}
	
	//Olusturulan overlap matrisine gore sekanslarin en iyi eslestigi reverse complementlerin ve sekanslarin indisleri diziye kaydediliyor.
	static int [] sekans_best_skor_indis(String []sekanslar, String []compl_sekans,int A,int T,int N,int match,int mismatch,int indel){
		
		overlap_alignment a= new overlap_alignment();
		int [][] skor= new int [N][N];
		int [] max_indis = new int [N-1];
			
			int n=0;
			int max=0;
			
	       //best_skor yardimiyla sekanslari eslestirip en iyi skorları çift boyutlu int diziye kaydediyor.
		   for(int i=0; i<N;i++) {
			
			  for (int k=0; k<N ;k++)  
				skor[i][k]=a.best_skor(sekanslar[i], sekanslar[k], match, mismatch, indel);
		   }
		
		   //best_skor yardimiyla sekanslari reverse complementleriyle eslestirip en iyi skorları çift boyutlu int diziye kaydediyor.
		   for (int i=0;i<N;i++) {
			
			  for (int o=0; o<i ;o++) 
				skor[i][o]=a.best_skor(sekanslar[i], compl_sekans[o], match, mismatch, indel);	
		   }
			
		    //En dusuk skordan buyuk olacak sekilde rastgele bir skoru max ilan ediyoruz ve indisini tutuyoruz.
			for(int p=0; p<N;p++) {
				
				if(p==A) {
					continue;
				}
				
				else if(skor[A][p]>=T) {
					max=skor[A][p];
					max_indis[n++]=p;
					break;
				 }
			}
			
			//Gercek max skorlari buluyor ve eger birden fazla varsa max_indis dizisine indislerini kaydediyor.
			for (int k=0; k<N ;k++)  {
				
				if(k==A) {
					continue;
				}
				
				else if(skor[A][k]>=T) {
					
					if(skor[A][k]>max) {	
						
						max=skor[A][k];
						n=0;
						
						for (int i=0;i<N-1;i++) {
						max_indis[i]=0;
					  }
						max_indis[n++]=k;
				  }
					
				else if(skor[A][k]==max && max_indis[0]!=k) {
					
					max_indis[n++]=k;	
				}
			  }	
			 }
			return max_indis;
	     }
	
	//Hizalanmış iki sekansi birlestirip string seklinde donduruyor.
    static String layout(String []sekanslar, String []compl_sekans,int A,int T,int N,int match,int mismatch,int indel) {
				
    			//Istenılen sekansin eslestigi en iyi skorlu sekanslarin indislerini max_indise kaydediyor.
				overlap_alignment a= new overlap_alignment();
				int [] max_indis = new int [N-1];
				max_indis=sekans_best_skor_indis(sekanslar,compl_sekans, A,T,N,match,mismatch,indel);
				
				//Hizalanmis sekanslari ve layoutu tutacak string dizisi ve string tanimlaniyor.
				String [] s =new String[2];
				String c="";
				
				//Sekansla hizalananin baska bir sekans mi yoksa reverse complement mi oldugu kontrolu yapiliyor.
				
				if(A>max_indis[0]) {	
					
					//Hizalanan sekans ve reverse complementler string dizisine aktariliyor.
					s=a.alignment(sekanslar[A], compl_sekans[max_indis[0]], match, mismatch, indel);
							
							/*Hizalanan sekanslar ust uste konularak '-' lerin yerine karsidaki baz,
					        farkli bazlarin karsina '*' konularak birlestiriliyor. */
							for (int o=0; o<s[1].length();o++) {
								
								if(s[0].charAt(o)==s[1].charAt(o)) {
									c=c+s[0].charAt(o);
								}
								else if(s[0].charAt(o)=='-') {
									
									if(o+1<s[0].length() && s[0].charAt(o+1)!=s[1].charAt(o+1))
										continue;
									
									else
									c=c+s[1].charAt(o);
								}
								
								else if(s[1].charAt(o)=='-') {
									
									if(o+1<s[0].length() && s[0].charAt(o+1)!=s[1].charAt(o+1))
										continue;
									else
									c=c+s[0].charAt(o);
								}
								
								else if(s[0].charAt(o)!=s[1].charAt(o))
									c=c+"*";
							  }				
				           }
				
				else {
						
					    //Hizalanan sekanslar string dizisine aktariliyor.
						s=a.alignment(sekanslar[A], sekanslar[max_indis[0]], match, mismatch, indel);
							
						    /*Hizalanan sekanslar ust uste konularak '-' lerin yerine karsidaki baz,
				            farkli bazlarin karsina '*' konularak birlestiriliyor. */
							for (int o=0; o<s[1].length();o++) {
								
								if(s[0].charAt(o)==s[1].charAt(o)) {
									c=c+s[0].charAt(o);
								}
								else if(s[0].charAt(o)=='-') {
									if(o+1<s[0].length() && s[0].charAt(o+1)!=s[1].charAt(o+1))
										continue;
									else
									c=c+s[1].charAt(o);
								}
								else if(s[1].charAt(o)=='-') {
									if(o+1<s[0].length() && s[0].charAt(o+1)!=s[1].charAt(o+1))
										continue;
									else
									c=c+s[0].charAt(o);
								}
								else if(s[0].charAt(o)!=s[1].charAt(o))
									c=c+"*";
							}
						
					
					
				}
				
					
				return c;
				
			}	
	
	//Overlap matrisindeki butun sekansları en yuksek skorlu sekanslarla hizalayip layout olusturuyor ve bunlari dosyaya yazdiriyor.
	static void yazdir_best_skor_alignment(String []sekanslar, String []compl_sekans,int T,int N,int match,int mismatch,int indel) throws IOException {
			
			//layout.txt adinda layoutlari yazdirmak icin dosya olusturuluyor.
			File file = new File("layout.txt"); 
			if (!file.exists()) { file.createNewFile(); 
			} FileWriter fileWriter = new FileWriter(file, false); 
			BufferedWriter bWriter = new BufferedWriter(fileWriter); 
			
			overlap_alignment a= new overlap_alignment();
			
			
			for (int i=0;i<N ;i++) {
				
				int [] max_indis = new int [N-1];
				max_indis=sekans_best_skor_indis(sekanslar,compl_sekans, i,T,N,match,mismatch,indel);
				String  [] s=new String[2];
				String c;
			
			//Olusan sekans & compl_sekans layoutlarini dosyaya yazdiriyor.
			if(i>max_indis[0]) {
				c=layout(sekanslar, compl_sekans,i,T,N,match,mismatch,indel);
				s=a.alignment(sekanslar[i], compl_sekans[max_indis[0]], match, mismatch, indel);
				
				bWriter.write("Sekans "+ (i+1)+  ": " +sekanslar[i]+System.lineSeparator()+"Comp_Sekans "+(max_indis[0]+1)+ ": "+compl_sekans[max_indis[0]]
						+ System.lineSeparator() +"Hizalama: "+ s[1]+ "  ----------->  "+ c+ System.lineSeparator()+"          "+s[0]+
						System.lineSeparator()+" ----------------------------------------------------------------"+
						System.lineSeparator());
			}
			
			//Olusan sekans & sekans layoutlarini dosyaya yazdiriyor.
			else {
				
				 c=layout(sekanslar, compl_sekans,i,T,N,match,mismatch,indel);
				 s=a.alignment(sekanslar[i], sekanslar[max_indis[0]], match, mismatch, indel);
				
				bWriter.write("Sekans "+ (i+1)+  ": " +sekanslar[i]+System.lineSeparator()+"Sekans "+(max_indis[0]+1)+ ": "+sekanslar[max_indis[0]]
						+ System.lineSeparator() +"Hizalama: "+ s[1]+ "  ----------->  "+ c+ System.lineSeparator()+"          "+s[0]+
						System.lineSeparator()+" ----------------------------------------------------------------"+
						System.lineSeparator());
			}
		}
		bWriter.close(); 
		System.out.println("layout.txt basariyla olusturuldu!");
		
		}
		
		

}
