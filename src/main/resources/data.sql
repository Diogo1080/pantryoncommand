REPLACE INTO users(email, encrypted_password, user_name, role) VALUES ("213@213.com","$2a$12$GmBSoa/XGe/orZyZxNWUL.12JgleIEyCR3J.I/fyvof3KPCCrP08G","Diogo","ADMIN")
REPLACE INTO category(name) VALUES ("Fish"),("Meat"),("Dairy")
REPLACE INTO ingredients(name,category_id) VALUES("Sword-Fish","1"),("Cow beef","2"),("Butter","3")
REPLACE INTO recipes(user_id,name,steps,prep_time) VALUES ("1","Grilled Sword Fish","Grill Sword fish","40 minutes"),("1","Fried Cow Beef on butter","put butter on frying pan and then put beef","30 minutes")
REPLACE INTO recipe_has(recipe_id,ingredient_id) VALUES ("1","1"),("2","2"),("2","3")