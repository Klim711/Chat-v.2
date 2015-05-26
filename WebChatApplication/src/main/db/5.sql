SELECT users.name  from messages left join users
ON messages.user_id=users.id
GROUP BY name
having count(messages.id)>3;