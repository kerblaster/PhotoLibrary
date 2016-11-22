package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import interfaces.Alertable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/*
 *  Photo model
 *  @author Renard Tumbokon, Nikhil Menon
 */
public class Photo implements Alertable{
	private Image image;
	private String caption;
	private List<Tag> tags;
	private Calendar cal;
	private LocalDate date;
	
	/*
	 * Constructor
	 * @Image file
	 */
	public Photo(Image image){
		this.image = image;
		this.caption = "";
		this.tags = new ArrayList<Tag>();
		
		this.cal = Calendar.getInstance();
		this.cal.set(Calendar.MILLISECOND, 0);
		this.date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	/*
	 * Getter image
	 * @return Image image
	 */
	public Image getImage(){
		return image;
	}
	
	/*
	 * Caption setter
	 * @param String str
	 */
	public void setCaption(String str){
		caption = str;
	}
	
	/*
	 * Caption getter
	 * @return String caption
	 */
	public String getCaption(){
		if (caption == null){
			return "n/a";
		}
		return caption;
	}
	
	/*
	 * Date to string
	 * @return String date
	 */
	public String getDateStr(){
		DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		return date.format(d);
	}
	
	/*
	 * Date getter
	 * @return String date
	 */
	public LocalDate getDate(){
		return date;
	}
	
	/*
	 * Boolean return if date is between given range 
	 * @param Date min
	 * @param Date max
	 * @return boolean
	 */
	public boolean isBetweenDates(LocalDate min, LocalDate max){
		return (date.isAfter(min) && date.isBefore(max)) || date.equals(min) || date.equals(max); 
	}
	
	/*
	 * Getter list of tags
	 * @return List<Tag>
	 */
	public List<Tag> getTags(){
		return tags;
	}
	
	/*
	 *  Getter tag
	 * @return Tag
	 */
	public Tag getTag(int index){
		return tags.get(index);
	}
	
	/*
	 * Add tag to photo
	 */
	public void addTag(Tag tagToAdd){
		for (Tag tag : tags){
			if (tag.equals(tagToAdd)){
				alert(AlertType.ERROR, "Tag Already Exists", "Input a unique tag:value pair");
				return;
			}
		}
		tags.add(tagToAdd);
	}
	
	
	/*
	 * Booleans if tag exists in list
	 * @param Tag tagToFind
	 * @return boolean
	 */
	public boolean hasTag(Tag tagToFind){
		for (Tag tag : tags){
			if (tag.equals(tagToFind)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Add tag to photo
	 * @param String type, String value
	 */
	public void addTag(String type, String value){
		tags.add(new Tag(type, value));
	}
	
	/*
	 * Remove tag fiven index
	 * @param int index
	 */
	public void removeTag(int index){
		tags.remove(index);
	}
	
	/*
	 * Boolean see if tag with same values exist
	 * @param Tag tagToFind
	 */
	public boolean findTag(Tag tagToFind){
		for (Tag t: tags){
			if (t.getType().equals(tagToFind.getType()) && t.getValue().equals(tagToFind.getValue())){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Delete tag given index of tag in list
	 */
	public void deleteTag(int index){
		if (index > tags.size()){
			alert(AlertType.ERROR, "Invalid Index", "Cannot delete tag because of invalid index");
			return;
		}
		tags.remove(index);
	}

}
