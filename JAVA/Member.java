import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.*;

public class Member
{
	public Map<String, String> Properties = new LinkedHashMap<String, String>();
	public String type;
	public String qualification;
	public int seniority;
	public String workPlace;

	public Member()
	{	}

	public Member(String t)
	{
		this.setType(t);
	}

	public void newProperty(String property, String info)
	{
		this.Properties.put(property, info);
	}

	public void setType(String t)
	{
		this.type = t;
	}

	public String getType()
	{
		return this.type;
	}

	public String toString()
	{
		String s = "";

		for(Map.Entry<String, String> en: this.Properties.entrySet())
			s += en.getKey() + "  - " + en.getValue() + "\n";

		return s;
	}

	public int length()
	{
		return this.Properties.size();
	}

	public void setParameters()
	{
		List<String> possibleQualifications = new ArrayList<String>();

		for(Map.Entry<String, String> en: this.Properties.entrySet())
			if(en.getKey().matches("FO\\dc"))
				possibleQualifications.add(en.getKey());

		String q = "", v, nk;
		int snr = 0, year = Calendar.getInstance().get(Calendar.YEAR);
		int aux;
		boolean isNumeric;

		for(String k: possibleQualifications)
		{
			v = this.Properties.get(k);

			if(v.toUpperCase().contains("DOUTORADO"))
			{
				nk = k.substring(0, k.length()-1) + "b";
				isNumeric = isNumeric(this.Properties.get(nk));

				if(isNumeric)
					aux = year - Integer.parseInt(this.Properties.get(nk));
				else
					aux = 0;

				if(q.equals("Doutorado") && snr < aux)
					snr = aux;

				if(!q.equals("Doutorado"))
				{
					q = "Doutorado";

					if(isNumeric)
						snr = aux;
					else
						snr = 0;
				}

			}

			if(v.toUpperCase().contains("MESTRADO"))
			{
				nk = k.substring(0, k.length()-1) + "b";
				isNumeric = isNumeric(this.Properties.get(nk));

				if(isNumeric)
					aux = year - Integer.parseInt(this.Properties.get(nk));
				else
					aux = 0;

				if(q.equals("Mestrado") && snr < aux)
					snr = aux;

				if(!q.equals("Doutorado") && !q.equals("Mestrado"))
				{
					q = "Mestrado";

					if(isNumeric)
						snr = aux;
					else
						snr = 0;
				}
			}

			if(v.toUpperCase().contains("GRADUAÇÃO"))
			{
				nk = k.substring(0, k.length()-1) + "b";
				isNumeric = isNumeric(this.Properties.get(nk));

				if(isNumeric)
					aux = year - Integer.parseInt(this.Properties.get(nk));
				else
					aux = 0;

				if(q.equals("Graduação") && snr < aux)
					snr = aux;

				if(!q.equals("Doutorado") && !q.equals("Mestrado") && !q.equals("Graduação"))
				{
					q = "Graduação";

					if(isNumeric)
						snr = aux;
					else
						snr = 0;
				}
			}
		}

		if(this.Properties.containsKey("ENDE"))
		{
			if(this.Properties.get("ENDE") != null)
			{
				Pattern p = Pattern.compile(", [A-Z][A-Z] - ");
				Matcher m = p.matcher(this.Properties.get("ENDE"));

				if(m.find())
					this.setWorkPlace(m.group().substring(1, m.group().length() - 2).trim());
				else
					this.setWorkPlace("NA");
			}

			else
				this.setWorkPlace("NA");
		}

		this.setQualification(q);
		this.setSeniority(snr);

		System.out.println(this.getWorkPlace());
	}

	public boolean isNumeric(String s)
	{
    	return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}

	public void setQualification(String q)
	{
		this.qualification = q;
	}

	public void setSeniority(int snr)
	{
		this.seniority = snr;
	}

	public void setWorkPlace(String loc)
	{
		this.workPlace = loc;
	}

	public int getSeniority()
	{
		return this.seniority;
	}

	public String getQualification()
	{
		return this.qualification;
	}

	public String getWorkPlace()
	{
		return this.workPlace;
	}
}
