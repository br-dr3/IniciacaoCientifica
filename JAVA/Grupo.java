public class Grupo
{
	public int k;
	public Pessoa examiners[];

	public Grupo()
	{	}

	public Grupo(Pessoa banca[], Grafo g)
	{
		this.setK(banca.length);
		this.examiners = new Pessoa[this.getK()];

		for(int i = 0; i < this.getK(); i++)
			this.setExaminer(banca[i], i);

	}

	private void setK(int k)
	{
		this.k = k;
	}

	public int getK()
	{
		return this.k;
	}

	public void setExaminer(Pessoa p, int i)
	{
		this.examiners[i] = p;
	}

	public Pessoa getExaminers(int i)
	{
		return this.examiners[i];
	}

	public Pessoa getIndex(int i)
	{
		return this.getExaminers(i);
	}

	public Pessoa[] getExaminers()
	{
		return this.examiners;
	}

	public int getSom(Grafo g)
	{
		int som = 0;
		for(int i = 0; i < this.getK(); i++)
			som += this.getExaminers(i).getSom(g);

		return som;
	}

	public int getMin(Grafo g)
	{
		int min = this.getExaminers(0).getMin(g), c;

		for(int i = 1; i < this.getK(); i++)
		{
			c = this.getExaminers(i).getMin(g);

			if(c < min)
				min = c;
		}

		return min;
	}

	public int getMax(Grafo g)
	{
		int max = this.getExaminers(0).getMax(g), c;

		for(int i = 1; i < this.getK(); i++)
		{
			c = this.getExaminers(i).getMax(g);
			if(c > max)
				max = c;
		}

		return max;
	}

	public double getMedia(Grafo g)
	{
		double med = 0;
		for(int i = 0; i < this.getK(); i++)
			med += this.getExaminers(i).getSom(g);

		return med / this.getK();
	}

	public double getVar(Grafo g)
	{
		double var = 0, x;

		for(int i = 0; i < this.getK(); i++)
		{
			x = (this.getExaminers(i).getSom(g) - this.getMedia(g));
			var += x*x;
		}

		return var / this.getK();
	}

	public double getMedianAge()
	{
		double sum = 0;

		for(int i = 0; i < this.getK(); i++)
			sum += this.getExaminers(i).getAge();

		return sum / this.getK();
	}

	public int getMaxAge()
	{
		int maxAge = -1;

		for(int i = 0; i < this.getK(); i++)
			if(this.getExaminers(i).getAge() > maxAge)
				maxAge = this.getExaminers(i).getAge();

		return maxAge;
	}

	public int getMinAge()
	{
		int minAge = 9999;

		for(int i = 0; i < this.getK(); i++)
			if(this.getExaminers(i).getAge() < minAge)
				minAge = this.getExaminers(i).getAge();

		return minAge;
	}
	
	public double getVarAge()
	{
		double var = 0, x;

		for(int i = 0; i < this.getK(); i++)
		{
			x = this.getExaminers(i).getAge() - this.getMedianAge();
			var += x*x;
		}

		return var / this.getK();
	}

	public int getNumberOfDifferentRegions()
	{
		String R[] = new String[this.getK()];
		int s = 0;
		boolean hasFind;
		String aux;

		for(int i = 0; i < R.length; i++)
		{
			hasFind = false;
			aux = this.getExaminers(i).getRegion();

			for(int j = 0; j < s && !hasFind; j++)
				if(R[j].equals(aux))
					hasFind = true;

			if(!hasFind)
				R[s++] = aux;
		}

		return s;
	}
}