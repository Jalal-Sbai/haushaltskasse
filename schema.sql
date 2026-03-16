-- Erstellen der Kategorien-Tabelle
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type ENUM('INCOME', 'EXPENSE') NOT NULL
);

-- Erstellen der Buchungs-Tabelle
CREATE TABLE IF NOT EXISTS entries (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    entry_date DATE NOT NULL,
    description TEXT,
    is_fixed_cost BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Beispiel-Kategorien einfügen
INSERT INTO categories (name, type) VALUES ('Gehalt', 'EINKOMMEN');
INSERT INTO categories (name, type) VALUES ('Miete', 'AUSGABE');
INSERT INTO categories (name, type) VALUES ('Lebensmittel', 'AUSGABE');
INSERT INTO categories (name, type) VALUES ('Freizeit', 'AUSGABE');
INSERT INTO categories (name, type) VALUES ('Sonstiges', 'AUSGABE');
