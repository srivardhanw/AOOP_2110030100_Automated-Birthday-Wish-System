
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;



public class Logic implements ActionListener{

	JLabel errLabel;
	JLabel label1;
	JLabel label2, label3, label4, label5, label6, label7;
	JTextField senderfield;
	JTextField occasionfield;
	JTextField receiverfield;
	JTextField wishfield;
	JPasswordField passwordfield;
	DatePicker dp1;
	TimePickerSettings sett;
	TimePicker tp1;
	Box hBox;
	Box hBox2, hBox3, hBox4, hBox5, hBox6;
	Box vBox;
	JButton addBtn;
	JFrame frame;
	
	static int minutes = 1;
	
	public static void main(String args[]) throws ClassNotFoundException{
		Logic lg = new Logic();
		DBconn.connect();

		Timer timer = new Timer();
		timer.schedule( new TimerTask() {
			@Override
			public void run() {
				lg.scheduleEmails();
			}
		}, 0, 1000 * 60 * minutes);
		
		
	    lg.createGUI();	     
	    }
	
	public void createGUI() {
	       frame = new JFrame("Automated Emailer");
	       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	       frame.setSize(600,700);

	       GridLayout grid = new GridLayout(0, 2, 20, 48);
	       JRootPane root = frame.getRootPane();
	       
	       root.setLayout(grid);
	       root.setBorder(new EmptyBorder(10, 10, 20, 20));

	       occasionfield = new JTextField();
	       senderfield = new JTextField();
	       receiverfield = new JTextField();
	       wishfield = new JTextField();
	       passwordfield = new JPasswordField();
	       
	      
	       
	       errLabel = new JLabel();
	       label1 = new JLabel("Date");
	       label2 = new JLabel("Occasion");
	       label3 = new JLabel("Sender's Email");
	       label4 = new JLabel("Receiver's Email");
	       label5 = new JLabel("Wish");
	       label6 = new JLabel("Password");
	       label7 = new JLabel("Time");
	       
	       addBtn = new JButton("Add");
	       addBtn.addActionListener(this);
	       addBtn.setActionCommand("Add");
	       
	       label1.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label2.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label3.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label4.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label5.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label6.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       label7.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       errLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
	       

	       dp1 = new DatePicker();
	       sett = new TimePickerSettings();
	       sett.setDisplaySpinnerButtons(true);
	       sett.setAllowKeyboardEditing(true);
	       sett.setDisplayToggleTimeMenuButton(true);
	       sett.initialTime = LocalTime.now();
	       sett.use24HourClockFormat();
	       tp1 = new TimePicker(sett);
	       
	     
	       root.add(label1);
	       root.add(dp1);
	       root.add(label7);
	       root.add(tp1);
	       root.add(label2);
	       root.add(occasionfield);
	       root.add(label3);
	       root.add(senderfield);
	       root.add(label6);
	       root.add(passwordfield);
	       root.add(label4);
	       root.add(receiverfield);
	       root.add(label5);
	       root.add(wishfield);
	       root.add(errLabel);
	       root.add(addBtn);
	       

	       
	       frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
        String action = ae.getActionCommand();
        if (action.equals("Add")) {
            String sender = senderfield.getText();
            String receiver = receiverfield.getText();
            String occasion = occasionfield.getText();
            String wish = wishfield.getText();
            String password = new String(passwordfield.getPassword());
            String datestring = dp1.getDateStringOrSuppliedString("(null)");
            String timestring = tp1.getTimeStringOrSuppliedString("(null)");
            
            

            if(sender.length() == 0  || password.length() == 0 || receiver.length() == 0 || occasion.length() == 0 || wish.length() == 0 || datestring.length() == 0 || timestring.length() == 0) {
            	errLabel.setForeground(new Color(255, 0, 0));
            	errLabel.setText("Please fill all the fields");
            	
            }
            else {
            	LocalDateTime datetime = LocalDateTime.parse(datestring+"T"+timestring);
            	String query = "Insert into `automated-emailer`.info (date, occasion, sender_email, password, receiver_email, wish) values('"+datetime+"', '"+occasion+"', '"+sender+"', '"+password +"', '"+receiver+"', '"+wish+"')";
            	try {
					Statement stmt = DBconn.conn.createStatement();
					stmt.executeUpdate(query);
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
            	errLabel.setForeground(new Color(0, 255, 0));
            	errLabel.setText("Information Successfully added");
            	occasionfield.setText("");
            	senderfield.setText("");
            	passwordfield.setText("");
            	receiverfield.setText("");
            	wishfield.setText("");
            	dp1.clear();

            	
            }
        }
    }
	
	
	public void scheduleEmails() {
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
			LocalDateTime now = LocalDateTime.now();
			String query = "SELECT sender_email, password, receiver_email, occasion, wish FROM `automated-emailer`.info where date = '"+dtf.format(now)+"'";
			Statement stmt = DBconn.conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next()) {
				String sender = rs.getString("sender_email");
				String password = rs.getString("password");
				String receiver = rs.getString("receiver_email");
				String occasion = rs.getString("occasion");
				String wish = rs.getString("wish");
				Email.sendEmail(sender, password, receiver, occasion, wish);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

}
