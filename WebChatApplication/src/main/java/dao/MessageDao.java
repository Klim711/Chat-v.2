package dao;
import model.Message;
import java.util.List;

public interface MessageDao {
    void add(Message message);

    void update(Message message);

    void delete(int id);

    Message selectById(Message message);

    List<Message> selectAll();
}

