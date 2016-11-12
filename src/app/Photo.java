package app;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class Photo {
	private File image;
	private String caption;
	private ArrayList<Tag> tags;
	private Calendar cal;
	private Date date;
	
	public Photo(File file){
		this.image = file;
		this.caption = "";
		this.tags = new ArrayList<Tag>();
		
		this.cal = new GregorianCalendar();
		this.cal.set(Calendar.MILLISECOND, 0);
		this.date = cal.getTime();
	}
	
	/*
	 * Caption
	 */
	public void setCaption(String str){
		caption = str;
	}
	public String getCaption(){
		return caption;
	}
	
	/*
	 * Date function (unable to change)
	 */
	public String getDate(){
		DateFormat d = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
		return d.format(date);
	}
	
	/*
	 * Tag functions
	 */
	public ArrayList<Tag> getTags(){
		return tags;
	}
	public void addTag(Tag tagToAdd){
		tags.add(tagToAdd);
	}
	public void removeTag(Tag tagToRemove){
		tags.remove(tagToRemove);
	}
	public boolean findTag(Tag tagToFind){
		for (Tag t: tags){
			if (t.getType().equals(tagToFind.getType()) && t.getValue().equals(tagToFind.getValue())){
				return true;
			}
		}
		return false;
	}
}
