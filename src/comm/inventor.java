package comm;

public class inventor {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		   String t="405*1234567_nikhph-dkarmude-ph-d";
		   String t="87*5134776_richard-j-melker-m-d-ph-d-retired";
		   if(t.charAt(t.length()-1)=='d')
			if(t.charAt(t.length()-2)=='-')
				{
					//System.out.println("yup");
					t=t.substring(0, t.length()-2)+"d";
			//		System.out.println("modified string: "+t);
				}
		   
		  
		   String inventorUID=t.substring(t.indexOf("*")+1, t.indexOf("_", t.indexOf("*")));
		   
		 if(t.contains("ph-d"))
			 t=t.replace("ph-d ", "phd");
		 if(t.contains("ph-d-"))
			 t=t.replace("ph-d-", "phd");
		  
		 if(t.contains("m-d"))
			 t=t.replace("m-d", "md");
		System.out.println("fname: after operations "+t);
		    String fname= (t.substring(t.indexOf("_")+1, t.lastIndexOf("-"))).replaceAll("-"," ");
		    fname = Character.toUpperCase(fname.charAt(0)) + fname.substring(1);
		    if(fname.contains(" ")) fname=fname+".";
		    //fname=UpperCase_name(fname);
		    System.out.println("fname:==== "+fname);
		    String lname= t.substring(t.lastIndexOf("-")+1);
		    lname = Character.toUpperCase(lname.charAt(0)) + lname.substring(1);
		    //System.out.println("FKID: "+fkid);
		    //System.out.println("UniversityUID: "+universityUID);
		    //System.out.println("inventorUID: "+inventorUID);
		   
		    System.out.println("lname:==== "+lname);

	}

}
