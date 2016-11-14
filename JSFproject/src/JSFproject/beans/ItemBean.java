package JSFproject.beans;

import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import JSFproject.model.Item;

//����� ��� ����������� � ��������� ���������� ������
@ManagedBean
@ViewScoped
public class ItemBean {
	
	//��������-������
	private Item item;
	//������
	private Session session;
	
	//�����������
	public ItemBean()
	{
		//��������� ������
		session = getNewSession();
		
		//�������� ���������� �������� ��� ����������� ��������-������ ��� �������������� 
		int item_id = getParamId();
		
		//���� ��� ������ ���������� � ��
		if (item_id > 0)
		{
			//��������� ���������� ��� ��������� ������ � ������
			Transaction tran = session.beginTransaction();
			
			try
			{
				//�������� ��������-������
				item = (Item) session.load(Item.class, getParamId());
				
				//�������� ����������
				tran.commit();
			}
			catch (Exception ex) 
			{
				//� ������ �����t ���������� ���������� � ��������� ������
				tran.rollback();
				session.close();
			}
		}
		//���� ���� ������ ��� � ��, �� ������� ������ (�����) ��������-������
		else
		{
			item = new Item();
		}
	}
	
	//����������� � �� � �������� ������
	private Session getNewSession()
	{
		Configuration config = new Configuration().configure();			
		ServiceRegistry servReg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		SessionFactory factory = config.buildSessionFactory(servReg);
		return factory.openSession();
	}
	
	//������� ��������� ����������� ���������
	public int getParamId() 
	{
		try
		{
	        FacesContext context = FacesContext.getCurrentInstance();
	        ExternalContext extContext = context.getExternalContext();
	        Map<String, String> params = extContext.getRequestParameterMap();
	        return Integer.parseInt(params.get("item_id"));
		}
		catch (Exception ex) {
			//��� ������ �������, ��� ��� ����� ������
			return 0;
		}
    }
	
	public Item getItem()
	{
		return item;
	}
	
	public void setItem(Item value) 
	{
		item = value;
	}
	
	//����� ���������� ������
	public void saveItem(ActionEvent actionEvent)
	{
		//��������� ����������
		Transaction tran = session.beginTransaction();
		
		try
		{			
			//���� ������ � �� � ����� �� ������ (��� ��� ���� item_name � ������� ���������)
			Criteria query = session.createCriteria(Item.class, "name = '" + item.getName() + "'");
			//���� ������ �������
			if (query.list().size() > 0)
			{
				//��������� ��������� ��������-������
				Item exist_item = (Item) query.list().get(0);
				//����������� ���-�� �� �������� � ����� ��������
				exist_item.setAmount(exist_item.getAmount() + item.getAmount());
				
				item = exist_item;
			}

			//��������� ������ � ��
			session.saveOrUpdate(item);
			
			//�������� ���������� � ��������� ������
			tran.commit();
			session.close();
		}
		catch(Exception ex)
		{
			//��� ������ ���������� ��������� � ��������� ������
			tran.rollback();
			session.close();
		}
	}
}
