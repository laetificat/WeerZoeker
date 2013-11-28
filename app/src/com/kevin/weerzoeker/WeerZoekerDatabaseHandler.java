package com.kevin.weerzoeker;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeerZoekerDatabaseHandler extends SQLiteOpenHelper {

	// Naamgeving van de database en tabel middels constanten
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "weerzoeker.db"; // Let op, gebruik .db als extensie
	private static final String WEERZOEKER_TABLE = "weerzoeker";
	
	// Constanten om de table layout te definieren
	private static final String KEY_ID = "_id";
	private static final String PLAATS = "plaats";
	private static final String NAAM = "naam";
	private static final String LAND = "land";
	

	// De constructor. Deze zorgt ervoor dat de juiste initialisatie plaats vind. Als de database 
	// nog niet bestaan wordt onCreate getriggerd. Bij een nieuwer versienummer van de database
	// wordt juist onUpgrade aangeroepen.
	public WeerZoekerDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// In de onCreate komt alle code om de tabellen die nodig zijn te maken. Dat gebeurd door
	// een SQL statement samen te stellen en uit te voeren.
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_WEERZOEKER_TABLE = "CREATE TABLE " + WEERZOEKER_TABLE + "(" +
				KEY_ID + " INTEGER PRIMARY KEY," + PLAATS + " TEXT," + NAAM +
				" TEXT," + LAND + " TEXT" + ")";
		db.execSQL(CREATE_WEERZOEKER_TABLE);
	}

	// Om te zorgen dat bij database wijzigingen tussen versies niks kwijt te raken kan het
	// onUpgrade mechanisme gebruikt worden. Nu wist hij de tabel en maakt hem opnieuw aan.
	// Wat gebeurd er met de data die er in zit?
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + WEERZOEKER_TABLE);
		onCreate(db);
	}
	

	// addRecord voegt een enkel record toe in de tabel. Dat kan door SQL te schrijven maar
	// in dit geval wordt gebruik gemaakt van ContentValues. De benodigde velden in een record
	// worden als kolomnaam en waarde in de ContentValues structuur geplaatst en vervolgens 
	// met insert in de database geschoten
	public void addRecord(WeerZoeker record)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(PLAATS, record.getPlaats());
		values.put(NAAM, record.getNaam());
		values.put(LAND, record.getLand());
		
		db.insert(WEERZOEKER_TABLE, null, values);
		db.close();
	}
	
	
	//probeersel
	public void removeRecord(String naam) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(WEERZOEKER_TABLE, NAAM + " = ?", new String[] {naam});
	}
	//probeersel
	
	
	// zoekPlaats geeft aan de hand van de naam die als parameter wordt doorgegeven de plaatsnaam
	// waar de betreffende persoon woont.
	public String zoekPlaats(String naam) {
		SQLiteDatabase db = this.getReadableDatabase();
	
		Cursor cursor = db.rawQuery("SELECT " + PLAATS + " FROM " + WEERZOEKER_TABLE + " WHERE " + NAAM + "='" + naam + "';", null);
		if (cursor.moveToFirst()) {
			return cursor.getString(0);
		}
		return "";
	}
	
	// emptyBierWeer maakt de tabel leeg. Wat gebeurd er als er door upgrades meer tabel in de database
	// zitten met de andere tabellen?
	public void emptyWeerZoeker() {
		SQLiteDatabase db = this.getWritableDatabase();

		db.execSQL("DROP TABLE IF EXISTS " + WEERZOEKER_TABLE);
		onCreate(db);
	}

	// getBierWeer geeft een lijst met de records die in de tabel zitten terug. De objecten in de 
	// lijst zijn van het type BierWeer, de klasse die je als data access objects geprogrameerd hebt.
	// In de presentatie bij Les 3 vind je terug hoe je deze lijst kunt gebruiken om je GUI te vullen.
	public List<WeerZoeker> getWeerZoeker()
	{
		List<WeerZoeker> weerZoekerList = new ArrayList<WeerZoeker>();
		String selectQuery = "SELECT * FROM " + WEERZOEKER_TABLE + ";";
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				WeerZoeker weerZoeker = new WeerZoeker();
				weerZoeker.setID(Integer.parseInt(cursor.getString(0)));
				weerZoeker.setPlaats(cursor.getString(1));
				weerZoeker.setNaam(cursor.getString(2));
				weerZoeker.setLand(cursor.getString(3));
				weerZoekerList.add(weerZoeker);
			} while(cursor.moveToNext());
		}
		return weerZoekerList;
	}
	
}