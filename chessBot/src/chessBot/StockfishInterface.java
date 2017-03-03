package chessBot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StockfishInterface {
	
	private Process stockfishProcess;
	private BufferedReader stockfishReader;
	private OutputStreamWriter stockfishWriter;
	
	//private static final String STOCKFISH = "C:\\Users\\Seven\\Documents\\stockfish-8-win\\Windows\\stockfish_8_x64.exe";
	private static final String STOCKFISH = "stockfish";
	
	public StockfishInterface() throws IOException
	{
		stockfishProcess = Runtime.getRuntime().exec(STOCKFISH);
		stockfishReader = new BufferedReader(new InputStreamReader(stockfishProcess.getInputStream()));
		stockfishWriter = new OutputStreamWriter(stockfishProcess.getOutputStream());
	}
	
	public void envoyerCommande(String commande) throws IOException
	{
		try{
		stockfishWriter.write(commande + "\n");
		stockfishWriter.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public String recevoirSortie(int wait) throws InterruptedException
	{
		StringBuffer buffer = new StringBuffer();
		Thread.sleep(wait);
		try {
			//envoyerCommande("isready");
			while(true)
			{
				String retour = stockfishReader.readLine();
				
				if(retour.contains("bestmove"))
				{
					buffer.append(retour +"\n");
					break; //On a r�cup�r� toute la sortie
				}
				else {
					buffer.append(retour+"\n");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return buffer.toString();
	}
	
	public String nextMove(String fenstring, int wait) throws IOException, InterruptedException
	{
		envoyerCommande("position fen " + fenstring);
		envoyerCommande("go movetime " + wait);
		System.out.println(fenstring);
		String s = recevoirSortie(20+wait);
		//System.out.println(s);

		return s.split("bestmove ")[1].split(" ")[0];

	}
	
	
	

}
