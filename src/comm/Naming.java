package comm;

import java.util.ArrayList;
 /**
 * @author nkarmude
 *
 */
class Naming
{
static String isSuffixPresent(String abc)
{	String Suffix=null;

	if(abc.contains("Ph.D."))
	{
		Suffix=(abc.substring(abc.indexOf("Ph.D."), abc.length())).trim();
		return Suffix;

	}
	else if(abc.contains("IV"))
		{
		Suffix=(abc.substring(abc.indexOf("IV"), abc.length())).trim();
		return Suffix;
		}
	else if(abc.contains("Jr."))
	{
	Suffix=(abc.substring(abc.indexOf("Jr."), abc.length())).trim();
	return Suffix;
	}
	else if(abc.contains("MD"))
	{
	Suffix=(abc.substring(abc.indexOf("MD"), abc.length())).trim();
	return Suffix;
	}
	else if(abc.contains("M.D."))
	{
	Suffix=(abc.substring(abc.indexOf("M.D."), abc.length())).trim();
	return Suffix;
	}
	else if(abc.contains("III"))
	{
	Suffix=(abc.substring(abc.indexOf("III"), abc.length())).trim();
	return Suffix;
	}
	else if(abc.contains("II"))
	{
	Suffix=(abc.substring(abc.indexOf("II"), abc.length())).trim();
	return Suffix;
	}
	else if(abc.contains(" I"))
	{
	Suffix=(abc.substring(abc.indexOf("I"), abc.length())).trim();
	System.out.println("In suffix present==");
	return Suffix;
	}
	
	return Suffix;
		
}
static String NameSuffix(String abc)	
{ 	String Suffix=null;
	abc=abc.trim();
	if(abc.contains("Ph.D") && abc.contains("M.D.,"))
	{  
		Suffix=(abc.substring(abc.indexOf("M.D.,"), abc.length())).trim();
		return Suffix;

	}
	
	else {
	Suffix=isSuffixPresent(abc);
	return Suffix;
			
	}



}

static String NameFname(String abc)
{ String firstname=null; String Lastname=null; String Suffix= null;abc=abc.trim();
	if(abc.contains("Ph.D") && abc.contains("M.D.,"))
	{  
			Suffix=(abc.substring(abc.indexOf("M.D.,"), abc.length())).trim();
			String newname=(abc.substring(0, abc.indexOf(Suffix))).trim();
			Lastname=newname.substring(newname.lastIndexOf(" "), newname.length());
			firstname= (newname.substring(0, newname.indexOf(Lastname))).trim();
			return firstname;
	}
	
	else {

			Suffix=isSuffixPresent(abc);
			if(Suffix!=null)		abc=(abc.substring(0, abc.lastIndexOf(Suffix))).trim();
			//else abc=(abc.substring(0, abc.lastIndexOf(" "))).trim();
			Lastname=abc.substring(abc.lastIndexOf(" "), abc.length());
			firstname= (abc.substring(0, abc.indexOf(Lastname))).trim();
			return firstname;
			
	}
	
}
static String NameLname(String abc)
{   String Suffix= null; String Lastname=null;abc=abc.trim();
	if(abc.contains("Ph.D") && abc.contains("M.D.,"))
	{  
		 Suffix=(abc.substring(abc.indexOf("M.D.,"), abc.length())).trim();
		 String newname=(abc.substring(0, abc.indexOf(Suffix))).trim();
		 Lastname=(newname.substring(newname.lastIndexOf(" "), newname.length())).trim();
		 return Lastname;
	}
	
	else {

			Suffix=isSuffixPresent(abc);
			if(Suffix!=null)
			abc=(abc.substring(0, abc.lastIndexOf(Suffix))).trim();
			Lastname=(abc.substring(abc.lastIndexOf(" "), abc.length())).trim();
		    return Lastname;
			
	}
	
}
 static String[] invFname(String t)
{
	String fname="";String suffix=""; String lastname=""; int count = 0;

	char []temp=t.toCharArray();
	t=t.trim();
	
		if(t.contains("ph-d"))		 t=t.replace("ph-d", "PhD");
		if(t.contains("m-d"))		 t=t.replace("m-d", "MD");
		for(int i=0;i<t.length();i++)
		{ if(temp[i]=='-')		{count++;}			
			
		}
		String inventorUID=t.substring(t.indexOf("*")+1, t.indexOf("_", t.indexOf("*")));
	t=t.substring(t.indexOf("_")+1, t.length());
	String[] name =new String[count+1];
	name = t.split("-");
	int j=name.length-1;
		while(isQualificationPresent(name[j].toString())==true && j>=0)
		{ 
			suffix=suffix+name[j]+", ";
			j--;
			
		}
		if(suffix.length()!=0)
		suffix=suffix.trim().substring(0, suffix.length()-2);
	
	for(int i=0;i<=j;i++) fname=fname+" "+name[i].toString();
	 
	 fname=capitalize(fname.trim());
	 if(suffix.length()!=0)
	 lastname=capitalize(suffix.trim());
	 System.out.println("firstname: "+fname);
	 System.out.println("lastname: "+lastname);
	 System.out.println("inventorUID: "+inventorUID);
	 return new String[] {fname, lastname, inventorUID};
	 //return fname;
	 

}
 
static boolean isQualificationPresent(String qual)
	{  
	//System.out.println("yes..");
	if(qual.equalsIgnoreCase("retired"))	return true;
	if(qual.equalsIgnoreCase("deceased"))	return true;
	if(qual.equalsIgnoreCase("phd"))	return true;
	if(qual.equalsIgnoreCase("md"))	return true;
	if(qual.equalsIgnoreCase("iv"))	return true;
	if(qual.equalsIgnoreCase("iii"))	return true;
	if(qual.equalsIgnoreCase("ii"))	return true;
	if(qual.equalsIgnoreCase("jr"))	return true;
	return false;
	}
static  String capitalize(String word){
	String fWord = "";
	word = word.toLowerCase();
	String[] split = word.split(" ");
	for(String w:split){
		w = w.substring(0, 1).toUpperCase() + w.substring(1);
		fWord = fWord + w + " "; 
	}
	fWord = fWord.substring(0,fWord.length()-1);
return fWord;
}

}

//public class Naming {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		//String abc="Richard J. Melker M.D., Ph.D (RETIRED) ";
//	String abc="1*5146867_richard-j-melker-m-d-ph-d-retired";
//		//String abc="284*5134773_william-stratford-jr-may-m-d-ph-d";
//		Name n= new Name();
////		String []invfname=n.invFname(abc);
////		System.out.println("NEW INVENTOR fNAME: "+ invfname[0]+" "+invfname[1]);
//
//		String fn=n. invFname(abc);
//		System.out.println("FIRST NAME: "+ fn);
//		
//		String ln=n. invLname(abc);
//		System.out.println("LAST NAME: "+ ln);
// 		
//			}
//
//}
