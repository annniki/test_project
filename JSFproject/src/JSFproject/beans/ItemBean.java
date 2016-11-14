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

//Класс для отображения и изменения конкретной записи
@ManagedBean
@ViewScoped
public class ItemBean {
	
	//Сущность-запись
	private Item item;
	//Сессия
	private Session session;
	
	//Конструктор
	public ItemBean()
	{
		//Открываем сессию
		session = getNewSession();
		
		//Получаем переданный параметр для определения сущности-записи для редактирования 
		int item_id = getParamId();
		
		//Если эта запись существует в БД
		if (item_id > 0)
		{
			//Открываем транзакцию для получения данных о записи
			Transaction tran = session.beginTransaction();
			
			try
			{
				//Получаем сущность-запись
				item = (Item) session.load(Item.class, getParamId());
				
				//Коммитим транзакцию
				tran.commit();
			}
			catch (Exception ex) 
			{
				//В случае ошибкt откатываем транзакцию и закрываем ссесию
				tran.rollback();
				session.close();
			}
		}
		//Если этой записи нет в БД, то создаем пустую (новую) сущность-запись
		else
		{
			item = new Item();
		}
	}
	
	//Подключение к БД и открытае сессии
	private Session getNewSession()
	{
		Configuration config = new Configuration().configure();			
		ServiceRegistry servReg = new StandardServiceRegistryBuilder().applySettings(config.getProperties()).build();
		SessionFactory factory = config.buildSessionFactory(servReg);
		return factory.openSession();
	}
	
	//Функция получения переданного параметра
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
			//При ошибке считаем, что это новая запись
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
	
	//Метод сохранения данных
	public void saveItem(ActionEvent actionEvent)
	{
		//Открываем транзакцию
		Transaction tran = session.beginTransaction();
		
		try
		{			
			//Ищем запись в БД с таким же именем (так как поле item_name в таблице уникально)
			Criteria query = session.createCriteria(Item.class, "name = '" + item.getName() + "'");
			//Если запись нашлась
			if (query.list().size() > 0)
			{
				//ППолучаем найденную сущность-запись
				Item exist_item = (Item) query.list().get(0);
				//Увеличиваем кол-во на введеное в форме значение
				exist_item.setAmount(exist_item.getAmount() + item.getAmount());
				
				item = exist_item;
			}

			//Сохраняем данные в БД
			session.saveOrUpdate(item);
			
			//Коммитим транзакцию и закрываем ссесию
			tran.commit();
			session.close();
		}
		catch(Exception ex)
		{
			//При ошибке откатываем транзацию и закрываем сессию
			tran.rollback();
			session.close();
		}
	}
}
