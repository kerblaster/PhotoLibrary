package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import interfaces.Alertable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/*
 *  Photo model specialized for serialization
 *  @author Renard Tumbokon, Nikhil Menon
 */
public class Photo implements Alertable, Serializable{

	private static final long serialVersionUID = -4742944524742845197L;
	
	private int width, height; //SERIALIZABLE field
	private int[][] pixels; //SERIALIZABLE field
	
	private String caption;
	private List<Tag> tags;
	private Calendar cal;
	private LocalDate date;
	
	/*
	 * Constructor
	 * @Image file
	 */
	public Photo(Image image){
		setImage(image);
		this.caption = "";
		this.tags = new ArrayList<Tag>();
		
		this.cal = Calendar.getInstance();
		this.cal.set(Calendar.MILLISECOND, 0);
		this.date = cal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	/*	SERIALIZE FUNCTION
	 * Converts Image to pixel array
	 * @param image to beconverted
	 */
	private void setImage(Image image) {
		width = ((int) image.getWidth());
		height = ((int) image.getHeight());
		pixels = new int[width][height];
		
		PixelReader r = image.getPixelReader();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				pixels[i][j] = r.getArgb(i, j);
	}	
	
	/* SERIALIZE FUNCTION
	 * Getter image
	 * @return Image image
	 */
	public Image getImage(){
		WritableImage image = new WritableImage(width, height);
		
		PixelWriter w = image.getPixelWriter();
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				w.setArgb(i, j, pixels[i][j]);
		
		return image;
	}
	
	/**
	 * Getter width
	 * @return int
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Getter height
	 * @return height
	 */
	public int getHeight() {
		return height;
	}	
	
	/**
	 * Pixel array getter
	 * @return int[][]
	 */
	public int[][] getPixels() {
		return pixels;
	}
	
	/*
	 * Check equality of images
	 * @param image
	 * @return boolean
	 */
	public boolean equals(Photo photo) {
		if (width != photo.getWidth())
			return false;
		if (height != photo.getHeight())
			return false;
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				if (pixels[i][j] != photo.getPixels()[i][j])
					return false;
		return true;
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
