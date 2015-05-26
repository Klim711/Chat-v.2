package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import db.ConnectionPool;
import model.Message;

public class MessageDaoImpl implements MessageDao {
    private static Logger logger = Logger.getLogger(MessageDaoImpl.class.getName());

    @Override
    public void add(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO messages (id, text,  user_id, date, isDeleted) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, Integer.parseInt(message.getId()));
            preparedStatement.setString(2, message.getText());
            preparedStatement.setInt(3, Integer.parseInt(message.getUser_id()));
            preparedStatement.setString(4, message.getDate());
            preparedStatement.setBoolean(5, message.isDeleted());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public void update(Message message) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnection();
            preparedStatement = connection.prepareStatement("Update Messages SET text = ?, isDeleted = ? WHERE id = ?");
            preparedStatement.setString(1, message.getText());
            preparedStatement.setBoolean(2, message.isDeleted());
            preparedStatement.setInt(3, Integer.parseInt(message.getId()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
    }

    @Override
    public Message selectById(Message Message) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Message> selectAll() {
        List<Message> Messages = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        ResultSet resultSet2 = null;

        try {
            connection = ConnectionPool.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Messages");
            while (resultSet.next()) {
                String id = String.valueOf(resultSet.getInt("id"));
                String text = resultSet.getString("text");
                int user_id = resultSet.getInt("user_id");
                resultSet2 = statement.executeQuery("select name from users where id=" + user_id);
                resultSet2.next();
                String name = resultSet2.getString("name");
                boolean isDeleted = resultSet.getBoolean("isDeleted");
                String date = resultSet.getString("date");
                Messages.add(new Message(id, name,  text, isDeleted, date));
            }
        } catch (SQLException e) {
            logger.error(e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error(e);
                }
            }
        }
        return Messages;
    }

    @Override
    public void delete(int id) {
        throw new UnsupportedOperationException();
    }

}
