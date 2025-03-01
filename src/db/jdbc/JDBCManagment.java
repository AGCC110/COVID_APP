package db.jdbc;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import db.interfaces.Cov_Manager;
import db.pojos.*;

public class JDBCManagment implements Cov_Manager {

	private Connection c;

	// Open database connection
	public void connect() {
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:./database/covid.database");
			c.createStatement().execute("PRAGMA foreign_keys = ON");
			System.out.println("Database connection opened");
			this.creatTables();
		} catch (SQLException E) {
			System.out.println("There was a database exception.");
			E.printStackTrace();
		} catch (Exception e) {
			System.out.println("There was a general exception.");
			e.printStackTrace();
		}

	}

	// Close database connection
	public void disconnect() {
		try {
			c.close();
		} catch (SQLException e) {
			System.out.println("There was a problem while closing the database connection.");
			e.printStackTrace();
		}
	}

	// Create tables:
	public void creatTables() {
		try {
			Statement stmt1 = c.createStatement();
			String sql1 = "CREATE TABLE doctors " + "(id       			INTEGER  	 	PRIMARY KEY AUTOINCREMENT,"
					+ " name     			TEXT    	 	NOT NULL,"
					+ " speciality 			TEXT  		 	NOT NULL,"
					+ " birth_date			DATE		 	NOT NULL,"
					+ " collegiate_number	TEXT	 	 	NOT NULL," 
					+ " sex 				TEXT	 	 	NOT NULL,"
					+ " hospital  			TEXT		 	NOT NULL"
					+ " image 				LONGVARBINARY   NOT NULL)";
			stmt1.executeUpdate(sql1);
			stmt1.close();

			Statement stmt2 = c.createStatement();
			String sql2 = "CREATE TABLE patients " + "(id       			INTEGER  	 PRIMARY KEY AUTOINCREMENT,"
					+ " name     			TEXT    	 NOT NULL," + " hos_location 		TEXT  		 NOT NULL,"
					+ " birthday			DATE  		 NOT NULL," + " social_security   	TEXT  	 	 NOT NULL,"
					+ " height 				float   	 NOT NULL," + " weight 				float   	 NOT NULL,"
					+ " sex 			    TEXT	   	 NOT NULL," + " infected 			boolean  	 NOT NULL,"
					+ " alive 				boolean  	 NOT NULL," + " hospital  			TEXT	 	 NOT NULL,"
					+ " score 				INTEGER		 NOT NULL,"
					+ " id_adm				INTEGER		 REFERENCES administration(id),"
					+ " vaccinated			boolean		 NOT NULL," + " bloodType			TEXT		 NOT NULL)";
			stmt2.executeUpdate(sql2);
			stmt2.close();

			Statement stmt3 = c.createStatement();
			String sql3 = "CREATE TABLE lab " + "(id       		INTEGER  	 	PRIMARY KEY AUTOINCREMENT,"
					+ " name     		TEXT    	 	NOT NULL," + " adress	 		TEXT	 	 	NOT NULL,"
					+ " cif			    TEXT  	 	 	NOT NULL," + " vacciness  		INTEGER	 	 	NOT NULL"
					+ " image 			LONGVARBINARY   NOT NULL)";
			stmt3.executeUpdate(sql3);
			stmt3.close();

			Statement stmt4 = c.createStatement();
			String sql4 = "CREATE TABLE shipment " + "(id       		INTEGER  	PRIMARY KEY AUTOINCREMENT,"
					+ " vacciness  		INTEGER	 	NOT NULL," + " date			DATE		NOT NULL,"
					+ " id_lab			INTEGER		REFERENCES lab(id),"
					+ " id_adm			INTEGER		REFERENCES administration(id))";
			stmt4.executeUpdate(sql4);
			stmt4.close();

			Statement stmt5 = c.createStatement();
			String sql5 = "CREATE TABLE administration "
					+ "(id       				INTEGER  		PRIMARY KEY AUTOINCREMENT,"
					+ " total_vacciness  		INTEGER	 		NOT NULL"
					+ " image 				    LONGVARBINARY   NOT NULL)";
			stmt5.executeUpdate(sql5);
			stmt5.close();

			Statement stmt6 = c.createStatement();
			String sql6 = "CREATE TABLE other_pathologies " + "(id       		INTEGER  	 PRIMARY KEY AUTOINCREMENT,"
					+ " name  			TEXT	 	 NOT NULL)";
			stmt6.executeUpdate(sql6);
			stmt6.close();

			Statement stmt7 = c.createStatement();
			String sql7 = "CREATE TABLE medication " + "(id       		INTEGER  	PRIMARY KEY AUTOINCREMENT,"
					+ " name  			TEXT	 	NOT NULL)";
			stmt7.executeUpdate(sql7);
			stmt7.close();

			Statement stmt8 = c.createStatement();
			String sql8 = "CREATE TABLE pat_doc " + "(id_doc       	INTEGER  	REFERENCES doctors(id),"
					+ " id_pat 			INTEGER	 	REFERENCES pathient(id)," + " PRIMARY KEY (id_doc, id_pat))";
			stmt8.executeUpdate(sql8);
			stmt8.close();

			Statement stmt9 = c.createStatement();
			String sql9 = "CREATE TABLE pat_patho " + "(id_pat       	INTEGER  	REFERENCES pathient(id),"
					+ " id_patho 		INTEGER	 	REFERENCES other_pathologies(id),"
					+ " PRIMARY KEY (id_pat, id_patho))";
			stmt9.executeUpdate(sql9);
			stmt9.close();

			Statement stmt10 = c.createStatement();
			String sql10 = "CREATE TABLE pat_medi " + "(id_pat       	INTEGER  	REFERENCES pathient(id),"
					+ " id_medi 		INTEGER	 	REFERENCES medication(id)," + " PRIMARY KEY (id_pat, id_medi))";
			stmt10.executeUpdate(sql10);
			stmt10.close();

			Statement stmt11 = c.createStatement();
			String sql11 = "CREATE TABLE pat_lab " + "(id_pat       	INTEGER  	REFERENCES pathient(id),"
					+ " id_lab 			INTEGER	 	REFERENCES lab(id)," + " PRIMARY KEY (id_pat, id_lab))";
			stmt11.executeUpdate(sql11);
			stmt11.close();

			Statement stmt12 = c.createStatement();
			String sql12 = "CREATE TABLE days " + "(id       	    INTEGER  	PRIMARY KEY AUTOINCREMENT,"
					+ " deaths 			INTEGER	 	NOT NULL," + " average			FLOAT		NOT NULL,"
					+ " daytime			DATE 		NOT NULL";
			stmt11.executeUpdate(sql12);
			stmt11.close();

		} catch (Exception e) {
			if (!e.getMessage().contains("already exist")) {
				e.printStackTrace();
			}
		}
	}

	public void addPatient(Patient p) {
		try {
			String sexo;
			if (p.getSex().equals(Sex.Male)) {
				sexo = "M";
			} else {
				sexo = "F";
			}
			String sql = "INSERT INTO patients (name, birthday, social_security, height, weight, sex, infected, alive, hospital, hos_location, score, bloodType, vaccinated)"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, p.getName());
			prep.setDate(2, p.getBirthday());
			prep.setString(3, p.getSocial_security());
			prep.setFloat(4, p.getheight());
			prep.setFloat(5, p.getWeight());
			prep.setString(6, sexo);
			prep.setBoolean(7, p.isInfected());
			prep.setBoolean(8, p.isAlive());
			prep.setString(9, p.getHospital());
			prep.setString(10, p.getHos_location());
			prep.setInt(11, 0); // TODO hay que insertar formula del score para poder introducir bien los
								// pacientes en la tabla
			prep.setString(12, p.getBloodType());
			prep.setBoolean(13, p.Is_vaccinated());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addDoctor(Doctor d) {
		try {
			String sexo;
			if (d.getSex().equals(Sex.Male)) {
				sexo = "M";
			} else {
				sexo = "F";
			}
			String sql = "INSERT INTO Doctor (name, speciality, birth_date, collegiate_number, sex, hospital, image) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, d.getName());
			prep.setString(2, d.getSpeciality());
			prep.setDate(3, d.getBirthday());
			prep.setString(4, d.getCollegiate_number());
			prep.setString(5, sexo);
			prep.setString(6, d.getHospital());
			// TODO insertar imagen en tabla prep.set(7, d.getImage());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addLab(Lab l) {
		try {
			String sql = "INSERT INTO Laboratory (name, adress, cif, vacciness) VALUES (?, ?, ?, ?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, l.getName());
			prep.setString(2, l.getAddress());
			prep.setString(3, l.getCif());
			prep.setInt(4, l.getVaccines_produce());
			// TODO insertar imagen en tabla prep.set(5, l.getImage());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addShipment(Shipment s, Lab l, Administration a) {
		try {
			String sql = "INSERT INTO Shipment (vacciness, date, id_lab, id_adm) VALUES (?,?,?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, s.getVaccines());
			prep.setDate(2, s.getDate_ship());
			prep.setInt(3, l.getId());
			prep.setInt(4, a.getId());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addGoverment(Administration a) {
		try {
			String sql = "INSERT INTO Administration (total_vacciness) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, a.getVaccines());
			// TODO insertar imagen en tabla prep.set(2, a.getImage());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOtherPathologies(Other_Pathologies op) {
		try {
			String sql = "INSERT INTO Other_Pathologies (name) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, op.getName());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addMedication(Medication m) {
		try {
			String sql = "INSERT INTO Medication (name) VALUES (?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, m.getName());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Patient> searchPatientByName(String name) {
		List<Patient> patients = new ArrayList<Patient>();
		try {
			String sql = "SELECT * FROM  patients WHERE name LIKE ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, "%" + name + "%");
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String patientname = rs.getString("name");
				Date birthday = rs.getDate("birthday");
				String social_security = rs.getString("social_security");
				float height = rs.getFloat("height");
				float weight = rs.getFloat("weight");
				Sex sexo;
				if (rs.getString("sex").equalsIgnoreCase("m")) {
					sexo = Sex.Male;
				} else {
					sexo = Sex.Female;
				}
				boolean infec = rs.getBoolean("infected");
				boolean alive = rs.getBoolean("alive");
				String hosp = rs.getString("hospital");
				String loc_hosp = rs.getString("hos_location");
				String blood = rs.getString("bloodType");
				boolean vaccin = rs.getBoolean("vaccinated");

				Patient patient = new Patient(id, loc_hosp, patientname, birthday, social_security, height, weight,
						sexo, infec, alive, hosp, vaccin, blood);
				patients.add(patient);
			}
			rs.close();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	@Override
	public Patient getPatient(int id) {
		try {
			String sql = "SELECT * FROM patients WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			if (rs.next()) {
				String patientName = rs.getString("name");
				String hos_loc = rs.getString("hos_location");
				Date bdate = rs.getDate("birthday");
				String socsec = rs.getString("social_security");
				float high = rs.getFloat("heigh");
				float weig = rs.getFloat("weight");
				String sexo = rs.getString("sex");
				Sex sexoo;
				if (sexo.equalsIgnoreCase("M")) {
					sexoo = Sex.Male;
				} else {
					sexoo = Sex.Female;
				}
				boolean infec = rs.getBoolean("infected");
				boolean vivo = rs.getBoolean("alive");
				String hos = rs.getString("hospital");
				boolean vacc = rs.getBoolean("vaccinated");
				String sangre = rs.getString("bloodType");
				return new Patient(id, hos_loc, patientName, bdate, socsec, high, weig, sexoo, infec, vivo, hos, vacc,
						sangre);
			}
			prep.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Patient> getPatientsOfDoctor(int doc_id) {
		List<Patient> patients = new ArrayList<Patient>();
		try {
			String sql = "SELECT * FROM pat_doc WHERE id_doc = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, doc_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int patientId = rs.getInt("id_pat");
				patients.add(this.getPatient(patientId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return patients;
	}

	@Override
	public Doctor getDoctor(int id) {
		try {
			String sql = "SELECT * FROM doctors WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			if (rs.next()) {
				String doctorName = rs.getString("name"); 
				String espe = rs.getString("speciality");
				Date nacido = rs.getDate("birth_date");
				String numCol = rs.getString("collegiate_number");
				String hos = rs.getString("hospital");
				byte[] foto = rs.getBytes("image");
				String sexo = rs.getString("sex");
				Sex sexoo;
				if (sexo.equalsIgnoreCase("M")) {
					sexoo = Sex.Male;
				} else {
					sexoo = Sex.Female;
				}

				return new Doctor(id, espe, doctorName, nacido, numCol, sexoo, hos, foto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Doctor> getDoctorsOfPatient(int pat_id) {
		List<Doctor> doctors = new ArrayList<Doctor>();
		try {
			String sql = "SELECT * FROM pat_doc WHERE id_pat = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, pat_id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int doctorId = rs.getInt("id_doc");
				doctors.add(this.getDoctor(doctorId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doctors;
	}

	@Override
	public void assignPatho(Patient p, Other_Pathologies op) {
		try {
			String sql = "INSERT INTO pat_patho (id_pat, id_patho) VALUES (?, ?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, p.getId());
			prep.setInt(2, op.getId());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void assignMed(Patient p, Medication m) {
		try {
			String sql = "INSERT INTO pat_medi (id_pat, id_medi) VALUES (?, ?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, p.getId());
			prep.setInt(2, m.getId());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public Lab getLab(int id) {
		try {
			String sql = "SELECT * FROM lab WHERE id = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			if (rs.next()) {
				String lab_name = rs.getString("name");
				String direccion = rs.getString("adress");
				String doc = rs.getString("cif");
				Integer vacc = rs.getInt("vacciness");
				// TODO SELECT image
				// return new Lab(id, lab_name, direccion, doc, vacc);
				return new Lab();
			}
			prep.close();
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Lab> showLabs() {
		List<Lab> labs = new ArrayList<Lab>();
		try {
			String lab_name = "";
			String sql = "SELECT * FROM lab WHERE name = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setString(1, lab_name);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String Lname = rs.getString("name");
				String Ladress = rs.getString("adress");
				String Lcif = rs.getString("cif");
				int Lvacciness = rs.getInt("vacciness");
				// TODO SELECT image
				// Lab lab = new Lab(id, Lname, Ladress, Lcif, Lvacciness);
				// labs.add(lab);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return labs;
	}

	@Override
	public void addDay(Day f) {
		try {
			String sql = "INSERT INTO days (deaths, average, daytime) VALUES (?,?,?)";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, f.getDeaths());
			prep.setFloat(2, f.getAverage());
			prep.setDate(3, f.getDate());
			prep.executeUpdate();
			prep.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Day> getDay(int id) {
		List<Day> savedDays = new ArrayList<Day>();
		try {
			String sql = "SELECT * FROM days WHERE id_pat = ?";
			PreparedStatement prep = c.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				int dayID = rs.getInt("id");
				int falle = rs.getInt("deaths");
				float media = rs.getFloat("average");
				Date dia = rs.getDate("daytime");
				savedDays.add(new Day(dayID, falle, media, dia));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return savedDays;
	}

}
