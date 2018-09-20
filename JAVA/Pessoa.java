public class Pessoa
{
	int index, age = 0;
	boolean examiner, elegible, insulated;
	String region, graduation;

	public Pessoa(int i, String a, String g, int age, String r)
	{
		this.setIndex(i);
		this.setExaminer(a);
		this.setGraduation(g);
		this.setAge(age);
		this.setRegion(r);
	}

	public void setGraduation(String g)
	{
		this.graduation = g;
	}

	public String getGraduation()
	{
		return this.graduation;
	}

	public void setRegion(String r)
	{
		this.region = r;
	}

	public String getRegion()
	{
		return this.region;
	}

	public void setIndex(int i)
	{
		this.index = i;
	}

	public int getIndex()
	{
		return this.index;
	}

	private void setAge(int a)
	{
		this.age = a;
	}

	public int getAge()
	{
		return this.age;
	}

	private void setExaminer(String a)
	{
		if(a.equals("2"))
			this.examiner = false;

		else if(a.equals("1"))
			this.examiner = true;
	}

	public boolean isExaminer()
	{
		return this.examiner;
	}

	public void setElegible(Grafo g)
	{
		boolean b = true;

		if(!this.isExaminer())
			b = false;

		else
			for(int i = 0; i < g.getNumberOfVertex() && b; i++)
				if(!g.getElement(i).isExaminer() && g.getDistMatrix(this.getIndex(), i) == 1)
					b = false;

		this.elegible = b;
	}

	public boolean isElegible()
	{
		return this.elegible;
	}

	public void setInsulated(Grafo g)
	{
		boolean aux = true;
		int n = g.getNumberOfVertex();
		int distElements;

		if(!this.isExaminer())
			aux = false;

		for(int i = 0; i < n && aux; i++)
		{
			distElements = g.getDistMatrix(this.getIndex(), i);

			if(!g.getElement(i).isExaminer())
				if(0 < distElements && distElements < n)
					aux = false;
		}

		this.insulated = aux;
	}

	public boolean isInsulated()
	{
		return this.insulated;
	}

	public int getSom(Grafo g)
	{
		int som = 0;

		int D[] = g.getDistMatrix()[this.getIndex()];

		for(int i = 0; i < D.length; i++)
			if(!g.getElement(i).isExaminer())
				som += D[i];

		return som;
	}

	public int getMin(Grafo g)
	{
		int min = g.getNumberOfVertex()+1;

		int D[] = g.getDistMatrix()[this.getIndex()];

		for(int i = 0; i < D.length; i++)
			if(!g.getElement(i).isExaminer() && D[i] < min)
				min = D[i];

		return min;
	}

	public int getMax(Grafo g)
	{
		int max = -1;

		int D[] = g.getDistMatrix()[this.getIndex()];

		for(int i = 0; i < D.length; i++)
			if(!g.getElement(i).isExaminer() && D[i] > max)
				D[i] = max;

		return max;
	}
}
