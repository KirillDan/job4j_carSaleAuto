package ru.job4j.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
public class Car {
	@Id
	@GeneratedValue
	private int id;
	@Size(min = 2, max = 30)
	private String mark;
	@Size(min = 2, max = 30)
	private String model;
	@Pattern(regexp = "\\d.\\d")
	private String litre;
	@Pattern(regexp = "\\d{2,4}")
	private String horsepower;
	@Pattern(regexp = "^Автомат$|^Ручная$|^Робот$")
	private String gearbox;
	@Pattern(regexp = "^Задний$|^Передний$|^Полный$")
	private String drive;
	@Size(min = 4, max = 15)
	private String body;
	@Size(min = 4, max = 15)
	private String color;
	@DecimalMin("1900")
	@DecimalMax("2022")
	private Integer year;
	@Size(min = 1, max = 7)
	private String mileage;
	@Size(min = 3, max = 7)
	private String cost;
	private boolean sell;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="photoIds", joinColumns=@JoinColumn(name="photoIds_id"))
	private List<String> photoids;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinTable(name = "car_user", joinColumns = { @JoinColumn(name = "car_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private User user;
	
	public static Car of(int id, String mark, String model, String litre, String horsepower, String gearbox, String drive, String body,
			String color, int year, String mileage, String cost) {
		Car car = new Car();
		car.id = id;
		car.mark = mark;
		car.model = model;
		car.litre = litre;
		car.horsepower = horsepower;
		car.gearbox = gearbox;
		car.drive = drive;
		car.body = body;
		car.color = color;
		car.year = year;
		car.mileage = mileage;
		car.cost = cost;
		car.sell = false;
		return car;
	}
	
	public static Car of(String mark, String model, String litre, String horsepower, String gearbox, String drive, String body,
			String color, int year, String mileage, String cost, User user) {
		Car car = new Car();
		car.mark = mark;
		car.model = model;
		car.litre = litre;
		car.horsepower = horsepower;
		car.gearbox = gearbox;
		car.drive = drive;
		car.body = body;
		car.color = color;
		car.year = year;
		car.mileage = mileage;
		car.cost = cost;
		car.user = user;
		car.sell = false;
		return car;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		id = id;
	}

	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getLitre() {
		return litre;
	}

	public void setLitre(String litre) {
		this.litre = litre;
	}

	public String getHorsepower() {
		return horsepower;
	}
	
	public void setHorsepower(String horsepower) {
		this.horsepower = horsepower;
	}

	public String getGearbox() {
		return gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}

	public String getDrive() {
		return drive;
	}

	public void setDrive(String drive) {
		this.drive = drive;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public boolean getSell() {
		return sell;
	}

	public void setSell(Boolean sell) {
		this.sell = sell;
	}

	public List<String> getPhotoIds() {
		return photoids;
	}

	public void setPhotoIds(List<String> photoIds) {
		this.photoids = photoIds;
	}
	
	public void addPhotoIds(String photoId) {
		if (this.photoids == null) {
			this.photoids = new ArrayList();
		}
		this.photoids.add(photoId);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Car other = (Car) obj;
		if (id != other.id)
			return false;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (cost == null) {
			if (other.cost != null)
				return false;
		} else if (!cost.equals(other.cost))
			return false;
		if (drive == null) {
			if (other.drive != null)
				return false;
		} else if (!drive.equals(other.drive))
			return false;
		if (gearbox == null) {
			if (other.gearbox != null)
				return false;
		} else if (!gearbox.equals(other.gearbox))
			return false;
		if (horsepower == null) {
			if (other.horsepower != null)
				return false;
		} else if (!horsepower.equals(other.horsepower))
			return false;
		if (litre == null) {
			if (other.litre != null)
				return false;
		} else if (!litre.equals(other.litre))
			return false;
		if (mark == null) {
			if (other.mark != null)
				return false;
		} else if (!mark.equals(other.mark))
			return false;
		if (mileage == null) {
			if (other.mileage != null)
				return false;
		} else if (!mileage.equals(other.mileage))
			return false;
		if (model == null) {
			if (other.model != null)
				return false;
		} else if (!model.equals(other.model))
			return false;
		if (photoids == null) {
			if (other.photoids != null)
				return false;
		} else if (!photoids.equals(other.photoids))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", mark=" + mark + ", model=" + model + ", litre=" + litre + ", horsepower="
				+ horsepower + ", gearbox=" + gearbox + ", drive=" + drive + ", body=" + body + ", color=" + color
				+ ", year=" + year + ", mileage=" + mileage + ", cost=" + cost + ", photoIds=" + photoids + "]";
	}
}
