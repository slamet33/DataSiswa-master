package id.idn.datasiswa.ResponseServer;

public class DataItem{

	private String hometown;

	private String address;

	private String sex;

	private String name;

	private String id;

	private String jsonMemberClass;

	public void setHometown(String hometown){
		this.hometown = hometown;
	}

	public String getHometown(){
		return hometown;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getSex(){
		return sex;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setJsonMemberClass(String jsonMemberClass){
		this.jsonMemberClass = jsonMemberClass;
	}

	public String getJsonMemberClass(){
		return jsonMemberClass;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"hometown = '" + hometown + '\'' + 
			",address = '" + address + '\'' + 
			",sex = '" + sex + '\'' + 
			",name = '" + name + '\'' + 
			",id = '" + id + '\'' + 
			",class = '" + jsonMemberClass + '\'' + 
			"}";
		}
}