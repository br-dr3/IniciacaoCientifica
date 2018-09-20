import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class slPickAxe
{
	static void printArray(String[] a)
	{
		System.out.println("{");
		for(int i = 0; i < a.length; i++)
			System.out.println(a[i]);

		System.out.println("}");
	}

	public static void main(String[] args)
	{
		printArray(args);

		String pickaxe;

		pickaxe = "\n";
		pickaxe += "\t\t                    .(#\n";
		pickaxe += "\t\t              ,%(.,#\n";
		pickaxe += "\t\t          *# ,/#(\n";
		pickaxe += "\t\t        # *//#\n";
		pickaxe += "\t\t      *,**//.\n";
		pickaxe += "\t\t    /###(/&%\n";
		pickaxe += "\t\t   *####%%%(*(\n";
		pickaxe += "\t\t  /##%.    #%/,/\n";
		pickaxe += "\t\t ,#%         (%/,/\n";
		pickaxe += "\t\t #%            /%/,(\n";
		pickaxe += "\t\t(#               /%/%&,\n";
		pickaxe += "\t\t(                  %@&&&(\n";
		pickaxe += "\t\t                     #@&&&&\n";
		pickaxe += "\t\t                       &@&&&&\n";
		pickaxe += "\t\t                         &@@&&&(\n";
		pickaxe += "\t\t                           &@@&&&%\n";
		pickaxe += "\t\t                             &@@@&&&.\n";
		pickaxe += "\t\t                               &@@@&&&,\n";
		pickaxe += "\t\t                                .&@@@@&\n\n";
		pickaxe += "\t\t\t\t\t\tBrian Alves Andreossi\n";
		pickaxe += "\t\t\t\t\t      ScriptLattes pickAxe v1.1";

		System.out.println(pickaxe);

		if(args.length == 3)
		{
			String arquivoLido[] = args[0].split("\\.");
			String listaRotulos = args[2];

			if(arquivoLido.length == 2 && arquivoLido[1].equals("ris"))
			{
				try
				{
					FileReader fl = new FileReader(args[0]);
					BufferedReader br = new BufferedReader(fl);
					String line = "", l[];

					FileReader fl2 = new FileReader(args[1]);
					BufferedReader br2 = new BufferedReader(fl2);
					String line2 = "";

					MemberList Membros = new MemberList();
					Member m = new Member();
					boolean hasPassed = false;

					while(true)
					{
						while(line != null && line.split("  - ", -1).length < 2)
							line = br.readLine();

						if(line == null)
						{
							Membros.addMember(m);
							break;
						}

						if(line.split("  - ", -1)[0].trim().equals("TY"))
						{
							if(hasPassed)
								Membros.addMember(m);

							line2 = br2.readLine().split(";")[0];
							while(line2 == "")
								line2 = br2.readLine().split(";")[0];

							if
							(
								line2.equals("SR") || line2.equals("1B") ||
								line2.equals("1C") || line2.equals("1D") ||
								line2.equals("1A")
							)
								m = new Member("1");
							else
								m = new Member(line2);

							l = line.split("  - ", -1);
							m.newProperty(l[0].trim(), l[1].trim());
							hasPassed = true;
						}

						else
						{
							l = line.split("  - ", -1);
							m.newProperty(l[0].trim(), l[1].trim());
						}

						line = br.readLine();
					}

					Membros.setParameters();
					Membros.generateMembersLabels(listaRotulos);
				}

				catch (IOException e)
				{
					e.printStackTrace();
				}

				System.out.println("\nFinalizado com Sucesso.\n\nSaídas:");
				System.out.println(listaRotulos + ".txt");
			}

			else if(arquivoLido.length == 4 && !arquivoLido[1].equals("ris"))
			{
				System.out.println("'" + args[0] + "' não é arquivo do tipo '.ris', é necessário informar um arquivo deste tipo para ser processado.\nFinalizado com erro.");
				return;
			}

			else
			{
				System.out.println("Erro com o nome do arquivo informado.\nFinalizado com erro.");
				return;
			}
		}

		else if(args.length < 3)
		{
			System.out.println("Deve-se passar um arquivo para ser mineirado e os Rótulos sequencialmente, e a pasta de saida.\nFinalizado com erro.");
			System.out.println("Modo de uso: java slPickAxe [arquivo .ris] [lista de Rotulos] [path do arquivo de saida, incluindo o nome, sem extensao].");
		}

		else
		{
			System.out.println("Muitos parâmetros informados. Informe somente um arquivo para ser mineirado.\nFinalizado com erro.");
			System.out.println("Modo de uso: java slPickAxe [arquivo .ris] [lista de Rotulos] [path do arquivo de saida, incluindo o nome, sem extensao].");
		}
	}
}
