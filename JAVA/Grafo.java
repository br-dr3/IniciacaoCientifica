import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;

public class Grafo
{
	private int v, e, father[];
	private double adjMatrix[][];
	private int distMatrix[][];
	private Pessoa element[];
    private HashSet<String> regions = new HashSet<String>();
    private ArrayList<String> av = new ArrayList<String>();
    private ArrayList<String> ca = new ArrayList<String>();

	public Grafo(String arq1, String arq2)
	{
		int lines = 1;

		try
		{
			FileReader f1 = new FileReader(arq1);
			BufferedReader in1 = new BufferedReader(f1);

			String line = in1.readLine();
			while(line != null)
			{
				line = in1.readLine();

				if(line != null)
					lines++;
			}

			this.setNumberOfVertex(lines);
			this.element = new Pessoa [this.getNumberOfVertex()];
			this.adjMatrix = new double [this.getNumberOfVertex()][this.getNumberOfVertex()];

			FileReader a = new FileReader(arq1);
			Scanner x = new Scanner(a);

			for(int i = 0; i < this.getNumberOfVertex(); i++)
				for(int j = 0; j < this.getNumberOfVertex(); j++)
					this.setAdjMatrix(i, j, x.nextDouble());

			this.setDistMatrix();

			FileReader f2 = new FileReader(arq2);
			BufferedReader br = new BufferedReader(f2);

			String line2 = br.readLine();
			String aux[];

			for(int i = 0; line2 != null; i++)
			{
				aux = line2.split(";");
				this.setElement(i, aux[0], aux[1], Integer.parseInt(aux[2]), aux[3].toUpperCase());
				line2 = br.readLine();
			}

			for(int i = 0; i < this.getNumberOfVertex(); i++)
			{
				this.element[i].setElegible(this);
				this.element[i].setInsulated(this);
			}

			this.setNumberOfEdges();
			this.setFather();
		}

		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void setNumberOfVertex(int k)
	{
		this.v = k;
	}

	public int getNumberOfVertex()
	{
		return this.v;
	}

	public void setAdjMatrix(int i, int j, double k)
	{
		this.adjMatrix[i][j] = k;
	}

	public double[][] getAdjMatrix()
	{
		return this.adjMatrix;
	}

	public double getAdjMatrix(int i, int j)
	{
		return this.adjMatrix[i][j];
	}

	public void setDistMatrix()
	{
		this.distMatrix = FloydWarshallAlgorithm(this.adjMatrix);
	}

	public int[][] getDistMatrix()
	{
		return this.distMatrix;
	}

	public int getDistMatrix(int i, int j)
	{
		return this.distMatrix[i][j];
	}

	private int[][] FloydWarshallAlgorithm(double M[][])
	{
		int FWM[][] = new int[M.length][M[0].length];

		for(int i = 0; i < M.length; i++)
			for(int j = 0; j < M[0].length; j++)
			{
				if(M[i][j] == 0 && i != j)
					FWM[i][j] = M.length;

				if(M[i][j] != 0)
					FWM[i][j] = 1;
			}

		for(int k = 0; k < M.length; k++)
			for(int i = 0; i < M.length; i++)
				for(int j = 0; j < M.length; j++)
					if(FWM[i][j] > FWM[i][k] + FWM[k][j])
					{
						FWM[i][j] = FWM[i][k] + FWM[k][j];
						FWM[j][i] = FWM[i][k] + FWM[k][j];
					}

		return FWM;
	}

	public void setElement(int i, String whatIs, String g, int age, String r)
	{
		this.element[i] = new Pessoa(i, whatIs, g, age, r);
        this.regions.add(r);

        if(whatIs.equals("1"))
            this.av.add(whatIs);
        else
            this.ca.add(whatIs);
	}

	public Pessoa getElement(int i)
	{
		return this.element[i];
	}

	public int getNumberOfElegible()
	{
		int c = 0;

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			if(this.getElement(i).isElegible())
				c++;

		return c;
	}

	public int[] getIndexesOfElegible()
	{
		int iElegiveis[] = new int[this.getNumberOfElegible()];

		int j = 0;

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			if(this.getElement(i).isElegible())
			{
				iElegiveis[j] = i;
				j++;
			}

		return iElegiveis;
	}

	public int getNumberOfInsulatedVertexes()
	{
		int vertexs = 0;

		boolean x;

		for(int i = 0; i < this.getNumberOfVertex(); i++)
		{
			x = true;

			for(int j = 0; j < this.getNumberOfVertex() && x; j++)
				if(this.getAdjMatrix(i, j) != 0)
					x = false;

			if(x)
				vertexs++;
		}

		return vertexs;
	}

	public void setNumberOfEdges()
	{
		int a = 0;

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			for(int j = 0; j < this.getNumberOfVertex(); j++)
				if(this.getAdjMatrix(i, j) != 0)
					a++;

		a /= 2;
		this.e = a;
	}

	public int getNumberOfEdges()
	{
		return this.e;
	}

	public void setFather()
	{
		this.father = new int [this.getNumberOfVertex()];
		int D[][] = this.getDistMatrix();

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			this.father[i] = (i+1);

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			for(int j = 0; j < this.getNumberOfVertex(); j++)
				if(D[i][j] < this.getNumberOfVertex())
					this.father[j] = (i+1);
	}

	public int getFather(int i)
	{
		return this.father[i];
	}

	public int[] getFather()
	{
		return this.father;
	}

	public int[] getFrequency()
	{
		int Freq[] = new int [this.getNumberOfVertex() + 1];
		int D[][] = this.getDistMatrix();

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			for(int j = 0; j < this.getNumberOfVertex(); j++)
				Freq[D[i][j]]++;

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			Freq[i] /= 2;

		return Freq;
	}

	public boolean[] getElegible()
	{
		boolean elegible[] = new boolean[this.getNumberOfVertex()];

		for(int i = 0; i < this.getNumberOfVertex(); i++)
			elegible[i] = this.getElement(i).isElegible();

		return elegible;
	}

	public boolean[] getInsulated()
	{
		boolean insulated[] = new boolean[this.getNumberOfVertex()];

		for(int i = 0; i < insulated.length; i++)
			insulated[i] = this.getElement(i).isInsulated();

		return insulated;
	}

	public boolean isInsulated(int i)
	{
		return this.getElement(i).isInsulated();
	}

	public boolean isExaminer(int i)
	{
		return this.getElement(i).isExaminer();
	}

	public double[] subGraphClustering(Grupo List[])
	{
		double cc[] = new double [List.length];
		double M[][] = new double [List[0].getK()][List[0].getK()];
		int av[] = new int[List[0].getK()];

		for(int i = 0; i < List.length; i++)
		{
			for(int j = 0; j < M.length; j++)
				av[j] = List[i].getExaminers(j).getIndex();


			for(int j = 0; j < M.length; j++)
			{
				for(int k = 0; k < M[0].length; k++)
				{
					M[j][k] = this.getAdjMatrix(av[j], av[k]);
					//System.out.println(M[j][k] + " ");
				}

				//System.out.println();
			}

			for(int j = 0; j < av.length; j++)
				cc[i] += clustering(M, j);

			cc[i] /= List[0].getK();
			//System.out.println();
		}

		return cc;
	}

	private double clustering(double M[][], int v)
	{
		double links = 0;
		double neighbor = 0;

		for(int w = 0; w < M.length; w++)
			for(int u = 0; u < M[0].length; u++)
				if(M[v][w] > 0 && M[v][u] > 0)
				{
					neighbor++;

					if(M[w][u] > 0)
						links += 0.5;
				}

		return (neighbor == 0 || neighbor == 1)? 0 : 2.0*links/(neighbor*(neighbor-1));
	}

	public double[] subGraphDegreeAverage(Grupo List[])
	{
		double dgr[] = new double[List.length];
		double M[][] = new double[List[0].getK()][List[0].getK()];
		int av[] = new int[List[0].getK()];

		for(int i = 0; i < List.length; i++)
		{
			for(int j = 0; j < List[i].getK(); j++)
				av[j] = List[i].getExaminers(j).getIndex();

			for(int j = 0; j < M.length; j++)
			{
				for(int k = 0; k < M[0].length; k++)
				{
					M[j][k] = this.getAdjMatrix(av[j], av[k]);
					//System.out.println(M[j][k] + " ");
				}

				// System.out.println();
			}

			for(int j = 0; j < av.length; j++)
				dgr[i] += degree(M, j);

			dgr[i] /= List[i].getK();

			// System.out.println(dgr[i] + "\n");
		}

		return dgr;
	}

	private static double degree(double M[][], int j)
	{
		int E = 0;

		for(int i = 0; i < M.length; i++)
			if(M[i][j] > 0)
				E++;

		return E;
	}


	public Grupo makeGroup(int Index[], int k)
	{
		Pessoa p[] = new Pessoa[Index.length];

		if(Index.length == k)
			for(int i = 0; i < Index.length; i++)
				p[i] = this.getElement(Index[i]);

		Grupo g = new Grupo(p, this);
		return g;
	}

    public String[] getRegions()
    {
        return this.regions.toArray(new String[regions.size()]);
    }

    public int getNumberOfAv()
    {
        return this.av.size();
    }

    public int getNumberOfCan()
    {
        return this.ca.size();
    }
}
