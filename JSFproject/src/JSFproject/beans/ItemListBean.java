package JSFproject.beans;

import java.util.List;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.hibernate.*;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.*;
import org.hibernate.service.ServiceRegistry;

import JSFproject.model.Item;

/*
 * ����� ��� ����������� ������ ���������
 */
@ManagedBean
public class ItemListBean {
	
	//���� - ������ ���������
	private List<Item> items;
	//�������� ������ ��� ������� � ��
	private SessionFactory session_factory;
	
	//����������
	public ItemListBean() {
		items = new ArrayList<Item>();
		session_factory = getNewSessionFactory();
	}
	
	//���� ����������� ��� ��������� ������ ���������
	@PostConstruct
	public void populateItemList(){
		try
		{
			//�������� ������ ���� ������� � ������� Items
			ReloadList();
		}
		catch (Exception ex) {
		}
	}
	
	//����������� � �� � �������� ������� ������
	private SessionFactory getNewSessionFactory()
	{
		Configuration config = new Configuration().configure();			
		ServiceRegistry servReg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		SessionFactory factory = config.buildSessionFactory(servReg);
		return factory;
	}
	
	@SuppressWarnings("unchecked")
	private void ReloadList()
	{
		//��������� ������ � ��������� ���������� ��� ��������� ������ �� ��
		Session session = session_factory.openSession();
		Transaction tran = session.beginTransaction();
		
		//������ ��� ��������� ������ � ������� HQL
		Query query = session.createQuery("from Item");
		items = (List<Item>) query.list();
		
		//�������� ��������� � ��������� ������
		tran.commit();
		session.close();
	}
	
	public List<Item> getItems()
	{
		return items;
	}
	
	public void setItems(List<Item> value)
	{
		items = value;
	}
	
	//����� �������� ������
	public void deleteItem(ActionEvent actionEvent)
	{
		//��������� ������ � ��������� ���������� ��� ��������� ������ �� ��
		Session session = session_factory.openSession(); 
		Transaction tran = session.beginTransaction();
		
		try
		{
			//�������� ������� (id ������)
			int item_id = (int) actionEvent.getComponent().getAttributes().get("item_id");
			
			//�������� �� �� ������-��������
			Item it = (Item) session.get(Item.class, item_id);
			//������� �� ��
			session.delete(it);
			
			//�������� ��������� � ��������� ������
			tran.commit();
			session.close();
			
			//��������� ������ ���������-�������
			ReloadList();
		}
		catch(Exception ex)
		{
			//� ������ ������ ���������� ���������� � �������� ������
			tran.rollback();
			session.close();
		}
	}
}
