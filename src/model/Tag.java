package model;

/*
 * Tag model
 * @author Renard Tumbokon, Nikhil Menon
 */
public class Tag {
	private String type;
	private String value;
	
	/*
	 * Constructor
	 * @param String type
	 * @param String value
	 */
	public Tag(String type, String value){
		this.type = type;
		this.value = value;
	}
	
	/*
	 * Getter type
	 * @return String type
	 */
	public String getType(){
		return type;
	}
	
	/*
	 * Getter value
	 * @return String value
	 */
	public String getValue(){
		return value;
	}
	
	/*
	 * Setter type
	 * @return String str
	 */
	public void setType(String str){
		type = str;
	}
	
	/*
	 * Setter value
	 * @param String str
	 */
	public void setValue(String str){
		value = str;
	}	
	
	/*
	 * See if input tag is equal to this tag
	 * @param Tag tag
	 * @return boolean
	 */
	public boolean equals(Tag tag){
		if (tag.getType().toLowerCase().equals(this.type.toLowerCase()) 
			&& tag.getValue().toLowerCase().equals(this.value.toLowerCase())){
				return true;
		}
		return false;
	}
	
	/*
	 * Prints string of type and value
	 * @return "type: value"
	 */
	public String toString(){
		return type + ": " + value;
	}
}
