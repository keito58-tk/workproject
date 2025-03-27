
-- rolesテーブル
INSERT IGNORE INTO roles (id, name) 
	VALUES (1, 'ROLE_GENERAL'),
		    (2, 'ROLE_ADMIN');

-- usersテーブル
INSERT IGNORE INTO users (id, name, password, role_id, enabled) 
	VALUES (1, '猫 太郎', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 1, true),
		    (2, '猫 次郎', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', 2, true);

-- productsテーブル
INSERT IGNORE INTO products (id, name,image_name) 
	VALUES (1, '商品1', 'test1.webp'),
	        (2, '商品2', 'test2.webp');

-- manualsテーブル
INSERT IGNORE INTO manuals (id, name,image_name)
	VALUES (1, 'マニュアル1', 'test3.webp'),
			(2, 'マニュアル2', 'test4.webp');

-- summarizesテーブル
INSERT IGNORE INTO summarizes (id, name, product_id, manual_id)
	VALUES (1, 'まとめ1', 1, 1),
		    (2, 'まとめ2', 2, 2);
