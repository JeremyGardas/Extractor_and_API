# **Extractor (Java)**
*Read packets from a Wireshark capture and insert them into a SQLite database*

* **The SQLite database must be created by yourself**
```SQL
CREATE TABLE "packets" (
	"id"	INTEGER,
	"data"	BLOB,
	PRIMARY KEY("id" AUTOINCREMENT)
);
```

* **Installation**
```
cd extractor/
mvn clean install
java -cp target/extractor-1.0.jar Main
```

# **Web API (PHP)**
*Make packets accessible through an API*

* **Installation**
```
cd web/
apt install php-sqlite3
php -S 0.0.0.0:80
```
