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
 * Класс для отображения списка сущностей
 */
@ManagedBean
public class ItemListBean {
	
	//Поле - список сущностей
	private List<Item> items;
	//Файбрика сессий для доступа к БД
	private SessionFactory session_factory;
	
	//Коструктор
	public ItemListBean() {
		items = new ArrayList<Item>();
		session_factory = getNewSessionFactory();
	}
	
	//Пост конструктор для получения списка сущностей
	@PostConstruct
	public void populateItemList(){
		try
		{
			//Получаем список всех записей в таблице Items
			ReloadList();
		}
		catch (Exception ex) {
		}
	}
	
	//Подключение к БД и создание фабрики сессий
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
		//Открываем сессию и открываем транзакцию для получения данных из БД
		Session session = session_factory.openSession();
		Transaction tran = session.beginTransaction();
		
		//Запрос для получения данных в формате HQL
		Query query = session.createQuery("from Item");
		items = (List<Item>) query.list();
		
		//Коммитим тразакцию и закрываем сессию
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
	
	//Метод удаления записи
	public void deleteItem(ActionEvent actionEvent)
	{
		//Открываем сессию и открываем транзакцию для получения данных из БД
		Session session = session_factory.openSession(); 
		Transaction tran = session.beginTransaction();
		
		try
		{
			//получаем атрибут (id записи)
			int item_id = (int) actionEvent.getComponent().getAttributes().get("item_id");
			
			//Получаем из БД запись-сущность
			Item it = (Item) session.get(Item.class, item_id);
			//Удаляем из БД
			session.delete(it);
			
			//Коммитим тразакцию и закрываем сессию
			tran.commit();
			session.close();
			
			//Обновляем список сущностей-записей
			ReloadList();
		}
		catch(Exception ex)
		{
			//В случае ошибки откатываем транзакцию и закрывам сессию
			tran.rollback();
			session.close();
		}
	}
}
