-- Database Droid del Frac --

------------
-- TABLES --
------------
CREATE TABLE users (
	id serial,
    name text NOT NULL, 
    mail text NOT NULL,
    phone text,
    pass character varying(32) NOT NULL,
    data_register date NOT NULL DEFAULT current_date,
    active boolean NOT NULL DEFAULT true,
    photo_user bytea,
    CONSTRAINT pass_length CHECK (length(pass)> 7),
    CONSTRAINT users_key PRIMARY KEY (id),
    CONSTRAINT phone_unique UNIQUE (phone),
    CONSTRAINT mail_unique UNIQUE (mail)
);

CREATE TABLE users_phantom (
	id serial,
    name text NOT NULL,
    mail text,
    phone text,    
    CONSTRAINT users_phantom_key PRIMARY KEY (id),
    CONSTRAINT phone_phantom_unique UNIQUE (phone),
    CONSTRAINT mail_phantom_unique UNIQUE (mail)
);

CREATE TABLE contacts (
	id_contact1 integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	id_contact2 integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	CONSTRAINT contacts_key PRIMARY KEY (id_contact1,id_contact2)
);

CREATE TABLE events (
	id serial NOT NULL,
	name text NOT NULL,
	description text,
	data_event date,
	place text,
	id_admin integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	data_register date NOT NULL DEFAULT current_date,
	status integer NOT NULL DEFAULT 1, 
	photo bytea,
	import double precision,
	CONSTRAINT events_key PRIMARY KEY (id)
);

CREATE TABLE users_events (
	id_event integer NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	id_user integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	weight integer NOT NULL DEFAULT 1,
	confirmet integer NOT NULL DEFAULT 0 -- 0 en espera de confirmación, 1 confirmado, 2 rechazado
);

CREATE TABLE users_events_phantom (
	id_event integer NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	id_user_phantom integer NOT NULL REFERENCES users_phantom(id) ON DELETE CASCADE,
	weight integer NOT NULL DEFAULT 1
);

CREATE TABLE event_expense (
	id serial,
	id_event integer NOT NULL,
	id_user integer NOT NULL,
	concept text,
	import double precision NOT NULL,
	CONSTRAINT events_expense_key PRIMARY KEY (id)
);

CREATE TABLE bills (
	id_event integer NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	id_payer integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	id_receiver integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	quantity double precision NOT NULL
);

CREATE TABLE bills_phantom_payer (
	id_event integer NOT NULL REFERENCES events(id) ON DELETE CASCADE,
	id_payer integer NOT NULL REFERENCES users_phantom(id) ON DELETE CASCADE,
	id_receiver integer NOT NULL REFERENCES users(id) ON DELETE CASCADE,
	quantity double precision NOT NULL
);

------------------------
-- FUNCTIONS CONTACTS --
------------------------
-- Obtiene una tabla con los contactos de un usario
CREATE or replace FUNCTION get_contacts(id_user integer) RETURNS table (id_contacts integer) AS $$
	SELECT CASE 
		WHEN id_contact1!=$1 then id_contact1 
		WHEN id_contact2!=$1 then id_contact2 
		END from contacts 
		WHERE id_contact1=$1 or id_contact2=$1;
$$ LANGUAGE SQL;

-- Función para borrar un usuario. 
-- Un usuario solo se puede borrar si todos los eventos que administra tienen status 3
CREATE or replace FUNCTION delete_user(id_user integer) RETURNS boolean AS $$ -- mirar tb usuarios normales
DECLARE
	mviews RECORD;
BEGIN
	FOR mviews IN select * from events WHERE id_admin = $1 LOOP

		IF mviews.status != 3 THEN
			RETURN FALSE;
		END IF;

    END LOOP;
	DELETE FROM users WHERE id = $1; 
	RETURN TRUE;
	
END
$$ LANGUAGE PLPGSQL;

-- Función para agregar usuarios.
-- Se encripta el pass y se hace el insert con el pass encriptado
CREATE or replace FUNCTION insert_user(name text, mail text, phone text, pass text) RETURNS integer AS $$
DECLARE
	id_user integer;
BEGIN
	INSERT INTO users(name,mail,phone,pass) VALUES (name,mail,phone, md5(pass)) RETURNING id INTO id_user;
	RETURN id_user;
END
$$ LANGUAGE PLPGSQL;

CREATE or replace FUNCTION insert_user(name text, mail text, pass text) RETURNS integer AS $$
DECLARE
	id_user integer;
BEGIN
	INSERT INTO users(name,mail,pass) VALUES (name,mail, md5(pass)) RETURNING id INTO id_user;
	RETURN id_user;
END
$$ LANGUAGE PLPGSQL;

-- Función para validar un usuario.
-- Busca el usuario y compara el pass encriptado de la BB con el pass encriptado que 
-- introduce el usuario. Si el usuario y pass coincide devuelve un true.
CREATE or replace FUNCTION validation_user(email text, password text) RETURNS integer AS $$
DECLARE
	id_user integer;
	pass_md5 character varying(32);
BEGIN
	SELECT pass FROM users WHERE mail = email INTO pass_md5;
	IF pass_md5 = md5(password) THEN
		id_user:= (SELECT id FROM users WHERE mail = email);
		RETURN id_user;
	ELSE
		id_user := -1;
		RETURN id_user;
	END IF;
END
$$ LANGUAGE PLPGSQL;

------------------------------
-- FUNCTION USER AND EVENTS --
------------------------------

-- Función para añadir usuarios a un evento
CREATE or replace FUNCTION add_users_event(event integer, email text) RETURNS boolean AS $$
DECLARE
	id_user integer;
BEGIN
	SELECT id FROM users WHERE mail = email INTO id_user;
	INSERT INTO users_events VALUES (event, id_user);
	RETURN true;
END
$$ LANGUAGE PLPGSQL;

-- Trigger para comprobar que dos contactos no esten referenciados
CREATE OR REPLACE FUNCTION add_contact() RETURNS TRIGGER AS $$
DECLARE
	mcontacts RECORD;
BEGIN
		
	FOR mcontacts IN SELECT * FROM get_contacts(NEW.id_contact1) LOOP

		IF mcontacts.id_contacts = NEW.id_contact2 THEN
			RAISE NOTICE 'ERROR: ya son amigos';
			RETURN null;
		END IF;

    END LOOP;
	
	RAISE NOTICE 'se ha hecho el insert';
	RETURN new;
	
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER add_contact BEFORE INSERT 
ON contacts  FOR EACH ROW EXECUTE PROCEDURE add_contact();

--Trigger que calcula el valor total de gastos de un evento
-- cada vez que se inserta un nuevo gasto
CREATE or replace FUNCTION update_import_event() RETURNS TRIGGER AS $$ 
DECLARE
	actual_import numeric := (SELECT import FROM events WHERE id = NEW.id_event);
BEGIN
	UPDATE events SET import = (actual_import + NEW.import) WHERE id = NEW.id_event;
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_import_event AFTER INSERT 
ON event_expense FOR EACH ROW EXECUTE PROCEDURE update_import_event();

-- Trigger que inserta al administrador como participante del evento
CREATE or replace FUNCTION update_admin_event() RETURNS TRIGGER AS $$
BEGIN 
	INSERT INTO users_events (id_event, id_user, confirmet) VALUES (NEW.id, NEW.id_admin, 1);
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_admin_event AFTER INSERT 
ON events FOR EACH ROW EXECUTE PROCEDURE update_admin_event();
