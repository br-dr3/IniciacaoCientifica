import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.io.*;
import java.util.regex.*;

public class MemberList
{
	List<Member> Members = new ArrayList<Member>();

	public MemberList()
	{	}

	public void addMember(Member m)
	{
		this.Members.add(m);
	}

	public void generateMembersInfoFile(String name)
	{
		String s = "";

		for(Member m: this.Members)
			s += m.toString() + "\n";

		s = s.substring(0, s.length() - 1);

		try
		{
			File ris = new File(name + ".ris");

			if(!ris.exists())
				ris.createNewFile();

			FileWriter fw = new FileWriter(ris.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(s);
			bw.close();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void generateMembersLabels(String name)
	{
		String s = "";

		for(Member m: this.Members)
			s += m.getType() + ";" + m.getQualification() + ";" + m.getSeniority() + ";" + m.getWorkPlace() + ";\n";

		try
		{
			File ris = new File(name + ".txt");

			if(!ris.exists())
				ris.createNewFile();

			FileWriter fw = new FileWriter(ris.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(s);
			bw.close();
		}

		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setParameters()
	{
		this.Members.forEach
		(
			(Member m) ->
			{
				System.out.println("Setando parametros para: " + m.Properties.get("NOME"));
				m.setParameters();
			}
		);
	}
}
