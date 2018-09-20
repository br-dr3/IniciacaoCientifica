import java.io.*;
import java.util.*;
import java.lang.Math;
import java.lang.StringBuilder;

public class CalculateGroups
{
	static int zz = 0;

	static void Combinations(int v[], int n, int ini, int r[], int S[][])
	{
		if(n == 0)
		{
			for(int j = 0; j < r.length; j++)
				S[zz][j] = r[j];

			zz++;
			return;
		}

		for(int i = ini; i <= v.length - n; i++)
		{
			r[r.length - n] = v[i];
			Combinations(v, n-1, i+1, r, S);
		}
	}

	static double numComb(int n, int k)
	{
		if(k == 0)
			return 1;

		else
			return (n*numComb(n-1, k-1))/k;
	}

	static void buildDistanceMatrix(Grafo g, String arqE1)
	{
		try
		{
			File matrizD = new File(arqE1);

			if(!matrizD.exists())
				matrizD.createNewFile();

			FileWriter fw1 = new FileWriter(matrizD.getAbsoluteFile());
			BufferedWriter bw1 = new BufferedWriter(fw1);

			String mD = "";
			int numV = g.getNumberOfVertex();
			int D[][] = g.getDistMatrix();

			for(int i = 0; i < numV; i++)
			{
				for(int j = 0; j < numV; j++)
				{
					if(D[i][j] == numV)
						mD += " ";

					else
						mD += D[i][j] + " ";
				}

				mD += "\n";
				bw1.write(mD);
				mD = "";
				System.out.println("buildDistanceMatrix: Linha " + (i + 1) + "/" + numV);
			}

			bw1.close();
		}

		catch(IOException e)
		{
			e.getStackTrace();
		}
	}

	static void buildInfo(Grafo g, String arqE2)
	{
		int v = g.getNumberOfInsulatedVertexes();
		int a = g.getNumberOfEdges();
		int f[] = g.getFrequency();
		int numV = g.getNumberOfVertex();

		String inf = "Número de Vértices: " + numV + "\n"
				   + "Vértices Isolados: " + v + "\n"
			 	   + "Arestas: " + a + "\n\n"
			 	   + "Frequência:";

		for(int i = 1; i < f.length-1; i++)
			if(f[i] > 0)
				inf += "\nDistância " + i + ": " + f[i];

		inf += "\n\nSub-Grafo pertencente (A pessoa de maior índice presente no sub-grafo é o nome do sub-grafo):\n";

		for(int i = 0; i < numV; i++)
			inf += "Pessoa " + (i+1) + ": Sub-Grafo " + g.getFather(i) + "\n";

		try
		{
			File info = new File(arqE2);

			if(!info.exists())
				info.createNewFile();

			FileWriter fw2 = new FileWriter(info.getAbsoluteFile());
			BufferedWriter bw2 = new BufferedWriter(fw2);

			bw2.write(inf);
			bw2.close();
		}

		catch(IOException e)
		{
			e.getStackTrace();
		}

		System.out.println("buildInfo: Ok");
	}

	static void buildElegible(Grafo g, String arqE3)
	{
		String elegible = "Professores Elegíveis (índices):\n";
		int count = 0;
		boolean Elegible[] = g.getElegible();

		for(int i = 0; i < Elegible.length; i++)
			elegible += (Elegible[i])? ((i+1) + "\n"):"";

		elegible += "\nElegiveis = " + g.getNumberOfElegible();

		try
		{
			File eleg = new File (arqE3);

			if(!eleg.exists())
				eleg.createNewFile();

			FileWriter fw = new FileWriter(eleg.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(elegible);
			bw.close();
		}

		catch(IOException e)
		{
			e.getStackTrace();
		}

		System.out.println("buildElegible: Ok");
	}

	static void buildInsulated(Grafo g, String arqE5)
	{
		String insulated = "Professores Isolados (índices):\n";
		int count = 0;

		for(int i = 0; i < g.getNumberOfVertex(); i++)
			if(g.isInsulated(i) && g.isExaminer(i))
			{
				insulated += (i+1) + "\n";
				count++;
			}

		insulated += "\n" + "Isolados = " + count;

		try
		{
			File insul = new File(arqE5);

			if(!insul.exists())
				insul.createNewFile();

			FileWriter fw = new FileWriter(insul.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(insulated);
			bw.close();
		}

		catch (IOException e)
		{
			e.getStackTrace();
		}

		System.out.println("buildInsulated: Ok");
	}

	static int insulatedExaminerApplicant(Grupo gr, Grafo g)
	{
		int numeroI = 0, aux;

		for(int i = 0; i < gr.getK(); i++)
		{
			aux = gr.getExaminers(i).getIndex();

			if(g.isInsulated(aux))
				numeroI++;
		}

		return numeroI;

	}

	static int insulatedInsideGroup(Grupo gr, Grafo g)
	{
		int ans = 0;
		int k, l;
		boolean aux;

		for(int i = 0; i < gr.getK(); i++)
		{
			aux = true;
			k = gr.getExaminers(i).getIndex();

			for(int j = 0; j < gr.getK(); j++)
			{
				l = gr.getExaminers(j).getIndex();
				if(i != j && g.getDistMatrix(k, l) != g.getNumberOfVertex())
					aux = false;
			}

			if(aux)
				ans++;
		}

		return ans;
	}

	static int min(Grupo gr, Grafo g)
	{
		int min = g.getNumberOfVertex() -1;

		for(int i = 0; i < gr.getK(); i++)
			for(int j = 0; j < g.getNumberOfVertex(); j++)
				if(!g.isExaminer(j) && g.getDistMatrix(gr.getExaminers(i).getIndex(), j) < min)
					min = g.getDistMatrix(gr.getExaminers(i).getIndex(), j);

		return min;
	}

	static int max(Grupo gr, Grafo g)
	{
		int max = -1;

		for(int i = 0; i < gr.getK(); i++)
			for(int j = 0; j < g.getNumberOfVertex(); j++)
				if(!g.isExaminer(j) && g.getDistMatrix(gr.getExaminers(i).getIndex(), j) > max)
					max = g.getDistMatrix(gr.getExaminers(i).getIndex(), j);

		return max;
	}

	static void buildList(Grupo List[], Grafo g, String arqE4)
	{

		StringBuilder list = new StringBuilder();
        list.append("id;");

		for(int i = 0; i < List[i].getK(); i++)
			list.append("g" + (i+1) + ";");

		list.append("minDist;maxDist;somDist;mediaDist;");
		list.append("varDist;standardDeviationDist;insulated;");
		list.append("insulatedInGroup;clusteringCoef;averageDegree;");
		list.append("minAge;maxAge;medianAge;varAge;regions\n");

		int n = List[0].getExaminers().length;
		double cc[] = g.subGraphClustering(List);
		double dgr[] = g.subGraphDegreeAverage(List);
		int nI, nIG, minD, maxD;

		for(int i = 0; i < List.length; i++)
		{
			list.append((i+1) + ";");

            for(int j = 0; j < n; j
                list.append((List[i].getExaminers(j).getIndex()+1) + ";");

			nI = insulatedExaminerApplicant(List[i], g);
			nIG = insulatedInsideGroup(List[i], g);
			minD = min(List[i], g);
			maxD = max(List[i], g);

			list.append(minD + ";" + maxD + ";" + List[i].getSom(g) + ";");
			list.append(List[i].getMedia(g) + ";" + List[i].getVar(g) + ";");
			list.append(Math.sqrt(List[i].getVar(g)) + ";" + nI + ";" + nIG + ";");
	    	list.append(cc[i] + ";" + dgr[i] + ";" + List[i].getMinAge() + ";");
	    	list.append(List[i].getMaxAge() + ";" + List[i].getMedianAge() + ";");
	    	list.append(List[i].getVarAge() + ";" + List[i].getNumberOfDifferentRegions());
	    	list.append("\n");

			System.out.println("buildList: Linha " + (i+1) + "/" + List.length + " - " + arqE4);
		}

        try
        {
            File ListOfTeachers = new File(arqE4);

            if(!ListOfTeachers.exists())
            ListOfTeachers.createNewFile();

            FileWriter fw = new FileWriter(ListOfTeachers.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(list.toString());
			bw.close();
		}

		catch(IOException e)
		{
			e.getStackTrace();
		}
	}

	public static Grupo[] List(Grafo g, int k)
	{
		int n = (int)numComb(g.getNumberOfElegible(), k);

		Grupo List[] = new Grupo[n];
		int Elegible[] = g.getIndexesOfElegible();
		int M[][] = new int [n][k];
		Combinations(Elegible, k, 0, new int[k], M);

		for(int i = 0; i < n; i++)
		{
			List[i] = g.makeGroup(M[i], k);
			System.out.println("List: " + (i+1) + "/" + n);
		}

		return List;
	}

	public static void printArray(int M[])
	{
		for(int i = 0; i < M.length-1; i++)
			System.out.print(M[i] + " ");

		System.out.println(M[M.length-1]);
	}

	static void numberGroups(int n, int k, String arqE3)
	{
		int num = (int)numComb(n, k);

		String nG = "NumeroGrupos = " + num;

		try
		{
			File numberGroups = new File(arqE3);

			if(!numberGroups.exists())
				numberGroups.createNewFile();

			FileWriter fw = new FileWriter(numberGroups.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nG);
            bw.close();
		}
		catch (IOException e)
		{
			e.getStackTrace();
		}
	}

    static void numberOfRegions(Grafo g, String arqE)
    {
        String Regioes[] = g.getRegions();

        String nR = "Numero de Regioes = " + Regioes.length + "\n";

        for(int i = 0; i < Regioes.length; i++)
            nR += Regioes[i] + "\n";

        try
        {
            File numberRegions = new File(arqE);

            if(!numberRegions.exists())
                numberRegions.createNewFile();

            FileWriter fw = new FileWriter(numberRegions.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(nR);
            bw.close();
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
    }

    static void numberOfAvaliators(Grafo g, String arqE)
    {
        try
        {
            File numberOfAv = new File(arqE);

            if(!numberOfAv.exists())
                numberOfAv.createNewFile();

            FileWriter fw = new FileWriter(numberOfAv.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Numero de Avaliadores = " + g.getNumberOfAv());
            bw.close();
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
    }

    static void numberOfCandidates(Grafo g, String arqE)
    {
        try
        {
            File numberOfCa = new File(arqE);

            if(!numberOfCa.exists())
                numberOfCa.createNewFile();

            FileWriter fw = new FileWriter(numberOfCa.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Numero de Candidatos = " + g.getNumberOfCan());
            bw.close();
        }
        catch (IOException e)
        {
            e.getStackTrace();
        }
    }

	public static void main(String args[])
	{
		String arqL1 = args[0];
        String arqL2 = args[1];
		String caminhoArqEscrito = args[2];
		int k = Integer.parseInt(args[3]);

       	Grafo g = new Grafo(arqL1, arqL2);

       	buildElegible(g, caminhoArqEscrito + ".ele");
       	buildInsulated(g, caminhoArqEscrito + ".ins");

		int n = g.getNumberOfElegible();

		numberGroups(n, k, caminhoArqEscrito + ".num");

        Grupo List[] = List(g, k);

        numberOfRegions(g, caminhoArqEscrito + ".reg");
        numberOfAvaliators(g, caminhoArqEscrito + ".ava");
        numberOfCandidates(g, caminhoArqEscrito + ".can");

		if(List.length >= 1)
		      buildList(List, g, caminhoArqEscrito + ".csv");
	}
}
