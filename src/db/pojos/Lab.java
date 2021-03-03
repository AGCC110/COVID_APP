package db.pojos;

import java.io.Serializable;
import java.util.List;


public class Lab implements Serializable{
	private int id;
	private int vaccines_produce;
	private String address;
	private boolean online;
	private String name;
	private String cif;
	private List<Lab> labs;
	
	public Lab() {
		super();
	}

	public Lab(int id, int vaccines_produce, String address, boolean online, String name, String cif, List<Lab> labs) {
		super();
		this.id = id;
		this.vaccines_produce = vaccines_produce;
		this.address = address;
		this.online = online;
		this.name = name;
		this.cif = cif;
		this.labs = labs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((cif == null) ? 0 : cif.hashCode());
		result = prime * result + id;
		result = prime * result + ((labs == null) ? 0 : labs.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (online ? 1231 : 1237);
		result = prime * result + vaccines_produce;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Lab other = (Lab) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (cif == null) {
			if (other.cif != null)
				return false;
		} else if (!cif.equals(other.cif))
			return false;
		if (id != other.id)
			return false;
		if (labs == null) {
			if (other.labs != null)
				return false;
		} else if (!labs.equals(other.labs))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (online != other.online)
			return false;
		if (vaccines_produce != other.vaccines_produce)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Lab [id=" + id + ", vaccines_produce=" + vaccines_produce + ", address=" + address + ", online="
				+ online + ", name=" + name + ", cif=" + cif + ", labs=" + labs + "]";
	}

	public int getVaccines_produce() {
		return vaccines_produce;
	}

	public void setVaccines_produce(int vaccines_produce) {
		this.vaccines_produce = vaccines_produce;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCif() {
		return cif;
	}

	public void setCif(String cif) {
		this.cif = cif;
	}

	public List<Lab> getLabs() {
		return labs;
	}

	public void setLabs(List<Lab> labs) {
		this.labs = labs;
	}
	
	
}