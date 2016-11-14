package JSFproject.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/*
 * Класс сущности, подключенный к БД с помощью Hibernate
 */
@Entity
@Table(name = "Items")
public class Item {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "itemid", nullable = false, unique = true)
	private int id;
	
	@Column(name = "itemname", nullable = false, unique = true)
	private String name;
	
	@Column(name = "itemamount", nullable = false)
	private int amount;
	
	public Item(){}
	
	public int getId()
	{
		return id;
	}
	
	public void setId (int value)
	{
		id = value;
	}

	public String getName()
	{
		return name;
	}
	
	public void setName (String value)
	{
		name = value;
	}
	
	public int getAmount()
	{
		return amount;
	}
	
	public void setAmount (int value)
	{
		amount = value;
	}
}
