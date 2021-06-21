package jm.task.core.jdbc;
import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import org.apache.logging.log4j.util.PropertiesUtil;

public class Main {
    public static void main(String[] args) {

        // TODO: реализуйте алгоритм здесь
        //  Создание таблицы User(ов)
        //  Добавление 4 User(ов) в таблицу с данными на свой выбор.
        //  После каждого добавления должен быть вывод в консоль ( User с именем – name добавлен в базу данных )
        //  Получение всех User из базы и вывод в консоль ( должен быть переопределен toString в классе User)
        //  Очистка таблицы User(ов)
        //  Удаление таблицы
        UserDao ud = new UserDaoJDBCImpl();
        ud.createUsersTable();
        ud.dropUsersTable();
    }
}
