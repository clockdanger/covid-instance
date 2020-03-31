package com.damatooliveira.covid.instance.game.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.badlogic.gdx.utils.Logger;

public class GameData {
	
	private final Logger log = new Logger(this.getClass().getSimpleName(), Logger.DEBUG);
	
	private File file;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	
	public GameData() {
		
		// create dir
		File fileDir = new File(System.getProperty("user.home") + "/CovidInstanceGame");
		
		// check dir and info dir
		if ( !fileDir.exists() ) {
			fileDir.mkdir();
			System.out.println("criou a pasta");
		}
		
		// create file
		this.file = new File(System.getProperty("user.home") + "/CovidInstanceGame/data.save");
		
	}
	
	public void save(String name, int level, int h, int m, int s) {
		Data dataReaded;
		
		try {
			// check file
			if ( this.file.exists()) {
				// read data
				dataReaded = (Data) this.load();
				
				// close input
				this.input.close();
				dataReaded.addScore(name, level, h, m ,s);
				
				// System.out.println("newSize: " + dataReaded.getPlayersAndScore().size() );
				
				this.saveInFile(dataReaded);
				
			} else {
				dataReaded = new Data();
				dataReaded.addScore(name, level, h, m ,s);
				this.saveInFile(dataReaded);;
			} 
			
			
			
		} catch(IOException e) {
			log.debug("Erro ao salvar os dados: " + e.getMessage());
			
			try {
				this.file.delete();
				dataReaded = new Data();
				dataReaded.addScore(name, level, h, m ,s);
				this.saveInFile(dataReaded);
			} catch(IOException e1) {
				log.debug("Erro ao salvar os dados em um novo arquivo: " + e1.getMessage());
			}
		} 
		catch(ClassNotFoundException e) {
			log.debug("Erro ao ler os dados: " + e.getMessage());
		}
		
	}
	
	public Object load() throws IOException, ClassNotFoundException {
		// create input
		Object object = null;
		
		if ( this.file.exists() ) {
			this.input = new ObjectInputStream(new FileInputStream(this.file));
			
			 object = this.input.readObject();
		}
		 
		return object;
	}
	
	public void saveInFile(Data data) throws IOException{
		
		FileOutputStream fileStream = new FileOutputStream(this.file);
		this.output = new ObjectOutputStream(fileStream);
		this.output.writeObject(data);
		this.output.close();
		fileStream.close();

		System.out.println("Salvou em: " + System.getProperty("user.home") + "/CovidInstance");
		
	}

}
